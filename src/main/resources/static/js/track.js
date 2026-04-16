let canvas = document.getElementById("trackCanvas");
let ctx = canvas.getContext("2d");

let margin = 50;
let minX, maxX, minZ, maxZ, trackWidth, trackHeight, scale, xOffset, yOffset;

let bestSplits = [];

// Resize + recompute scale
function resizeCanvas() {
    let parent = canvas.parentElement;
    canvas.width = parent.clientWidth;         // full width of column
    canvas.height = parent.clientWidth * 0.66; // keep ~3:2 aspect ratio

    // recompute track bounds
    minX = Math.min(...nodes.map(n => n.x));
    maxX = Math.max(...nodes.map(n => n.x));
    minZ = Math.min(...nodes.map(n => n.z));
    maxZ = Math.max(...nodes.map(n => n.z));

    trackWidth = maxX - minX;
    trackHeight = maxZ - minZ;

    let scaleX = (canvas.width - 2 * margin) / trackWidth;
    let scaleY = (canvas.height - 2 * margin) / trackHeight;
    scale = Math.min(scaleX, scaleY);

    xOffset = (canvas.width - trackWidth * scale) / 2;
    yOffset = (canvas.height - trackHeight * scale) / 2;

    drawTrack(); 
}
window.addEventListener("resize", resizeCanvas);
resizeCanvas();

function drawTrack() {
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    // --- White background line ---
    ctx.lineWidth = 20; // slightly thicker
    ctx.strokeStyle = "grey";
    ctx.beginPath();
    nodes.forEach((n, i) => {
        let x = (n.x - minX) * scale + xOffset;
        let y = (n.z - minZ) * scale + yOffset;
        if (i === 0) ctx.moveTo(x, y);
        else ctx.lineTo(x, y);
    });
    ctx.stroke();

    // --- Black foreground line ---
    ctx.lineWidth = 12; // thinner, drawn on top
    ctx.strokeStyle = "black";
    ctx.beginPath();
    nodes.forEach((n, i) => {
        let x = (n.x - minX) * scale + xOffset;
        let y = (n.z - minZ) * scale + yOffset;
        if (i === 0) ctx.moveTo(x, y);
        else ctx.lineTo(x, y);
    });
    ctx.stroke();
}


function drawCars(cars) {
    drawTrack();

    // Find the best lap among all cars
    let bestLap = Math.min(...cars.map(c => c.best_lap).filter(l => l > 0));

    cars.forEach(car => {
        // Car color
        let color = car.id === 0 ? "green" : "red";

        // If this car has the best lap, override color to purple
        if (car.best_lap === bestLap) {
            color = "purple";
        }

        ctx.fillStyle = color;

        let x = (car.position.x - minX) * scale + xOffset;
        let y = (car.position.z - minZ) * scale + yOffset;

        // Draw circle
        ctx.beginPath();
        ctx.arc(x, y, 16, 0, 2 * Math.PI);
        ctx.fill();

        // Draw player ID above the car
        ctx.fillStyle = "white";
        ctx.font = "bold 16px Arial";
        ctx.textAlign = "center"
        ctx.fillText(car.id, x - 1, y + 4);
    });
}


// Format time mm:ss.mmm
function formatTime(ms) {
    if (!ms || ms <= 0) return "-";
    let totalSeconds = Math.floor(ms / 1000);
    let minutes = Math.floor(totalSeconds / 60);
    let seconds = totalSeconds % 60;
    let milliseconds = ms % 1000;
    return `${String(minutes).padStart(2,'0')}:${String(seconds).padStart(2,'0')}.${String(milliseconds).padStart(3,'0')}`;
}

// WebSocket: Cars
let wsCars = new WebSocket("ws://localhost:8080/ws/cars");
wsCars.onmessage = function(event) {
    let cars = JSON.parse(event.data);
    drawCars(cars);
};

// WebSocket: Players
let wsPlayers = new WebSocket("ws://localhost:8080/ws/players");
wsPlayers.onmessage = function(event) {
    let players = JSON.parse(event.data);
    
    players.forEach(p => {
        p.realtime_leaderboard_pos += 1;
    })

    // sort by leaderboard position
    players.sort((a, b) => {
        let posA = a.realtime_leaderboard_pos || Infinity;
        let posB = b.realtime_leaderboard_pos || Infinity;
        return posA - posB;
    });

    let tbody = document.getElementById("playersTable").querySelector("tbody");
    tbody.innerHTML = "";

    let validBestLaps = players
    .map(p => p.best_lap)
    .filter(lap => lap && lap > 0);

    let bestLapTime = Math.min(...validBestLaps);

    players.forEach(player => {
        writeBestSplit(player);
    });

    players.forEach(player => {
        let lastLapDisplay;
        let pitClass = "bg-orange";

        if (player.in_box) {
            lastLapDisplay = "PIT";
        } else if (player.in_pit) {
            lastLapDisplay = "PITLANE";
        } else if (player.last_lap === "0") {
            lastLapDisplay = "OUT LAP";
        } else {
            pitClass = "";
            lastLapDisplay = formatTime(player.last_lap);
        }

        // Format best lap & highlight if fastest
        let bestLapDisplay = formatTime(player.best_lap);
        let bestLapClass = (player.best_lap === bestLapTime) ? "bg-purple" : "";

        let row = document.createElement("tr");

        // Base columns
        let html = `
            <td>${player.realtime_leaderboard_pos || ""}</td>
            <td>${player.id}</td>
            <td>${player.player_name}</td>
            <td class="${bestLapClass}">${bestLapDisplay}</td>
            <td class="${pitClass}">${lastLapDisplay}</td>
            <td>${player.tyre_compound.charAt(0) || ""}</td>
            <td>${player.car_name || ""}</td>
        `;

        // Add split times (format each)
        if (player.splits && Array.isArray(player.splits)) {
            player.splits.forEach((split, i) => {
                let splitDisplay = formatTime(split);
                html += `<td>${splitDisplay}</td>`;
            });
        }

        row.innerHTML = html;
        tbody.appendChild(row);
    });

};

function writeBestSplit(player) {
    if (!Array.isArray(player.splits)) return;

    player.splits.forEach((split, i) => {
        if (!split || split <= 0) return;

        if (!bestSplits[i]) {
            bestSplits[i] = {
                first: null,
                second: null
            };
        }

        const entry = bestSplits[i];

        // 🥇 FIRST BEST
        if (
            !entry.first ||
            split < entry.first.time
        ) {
            // Only shift to second if it's a DIFFERENT player
            if (
                entry.first &&
                entry.first.playerId !== player.id &&
                (
                    !entry.second ||
                    entry.first.time < entry.second.time
                )
            ) {
                entry.second = entry.first;
            }

            entry.first = {
                time: split,
                playerId: player.id
            };
            displayBestSplits()
        }

        // 🥈 SECOND BEST
        else if (
            entry.first.playerId !== player.id &&
            (
                !entry.second ||
                split < entry.second.time
            )
        ) {
            entry.second = {
                time: split,
                playerId: player.id
            };
            displayBestSplits()
        }
    });
}

function displayBestSplits() {
    const container = document.getElementById("fastest-sectors");
    if (!container) return;

    // Clear previous content
    container.innerHTML = "";

    const table = document.createElement("table");
    table.className = "best-sectors-table table table-dark table-striped table-hover";

    // ----- Header -----
    const thead = document.createElement("thead");
    const headerRow = document.createElement("tr");

    bestSplits.forEach((_, i) => {
        const thId = document.createElement("th");
        thId.textContent = "Num";

        const thTime = document.createElement("th");
        thTime.textContent = `S${i + 1}`;

        headerRow.appendChild(thId);
        headerRow.appendChild(thTime);
    });

    thead.appendChild(headerRow);
    table.appendChild(thead);

    // ----- Body -----
    const tbody = document.createElement("tbody");

    // Create TWO rows total
    const rowFirst = document.createElement("tr");
    const rowSecond = document.createElement("tr");

    bestSplits.forEach(sector => {
        // ----- FIRST BEST -----
        if (sector?.first) {
            rowFirst.innerHTML += `
                <td>${sector.first.playerId}</td>
                <td>${formatTime(sector.first.time)}</td>
            `;
        } else {
            rowFirst.innerHTML += `<td>-</td><td>-</td>`;
        }

        // ----- SECOND BEST -----
        if (sector?.second) {
            rowSecond.innerHTML += `
                <td>${sector.second.playerId}</td>
                <td>${formatTime(sector.second.time)}</td>
            `;
        } else {
            rowSecond.innerHTML += `<td>-</td><td>-</td>`;
        }
    });

    // Append rows ONCE
    tbody.appendChild(rowFirst);
    tbody.appendChild(rowSecond);

    table.appendChild(tbody);
    container.appendChild(table);
}
