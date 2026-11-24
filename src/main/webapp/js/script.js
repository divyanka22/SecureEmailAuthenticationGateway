
document.getElementById("analyzeBtn").addEventListener("click", async function () {

    const emailHeader = document.getElementById("emailHeader").value.trim();
    const resultsDiv = document.getElementById("results");

    if (emailHeader === "") {
        resultsDiv.textContent = "Please paste email headers first.";
        return;
    }

    resultsDiv.textContent = "Analyzing email headers...";

    try {
        const response = await fetch("analyze", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: new URLSearchParams({
                emailHeader: emailHeader
            })
        });

        const data = await response.json();

        if (!response.ok) {
            resultsDiv.textContent = data.error || "Unable to analyze headers.";
            return;
        }

        resultsDiv.replaceChildren();

        const heading = document.createElement("h3");
        heading.textContent = "Authentication Analysis Results";
        resultsDiv.appendChild(heading);

        const resultContainer = document.createElement("div");
        resultContainer.className = "result-container";

        ["spf", "dkim", "dmarc"].forEach(function (protocol) {

            const status = data[protocol].toLowerCase();

            const card = document.createElement("div");
            card.className = "result-card";

            const title = document.createElement("h4");
            title.textContent = protocol.toUpperCase();

            const statusText = document.createElement("span");
            statusText.className = "result-status";

            if (status === "pass") {
                statusText.classList.add("status-pass");
            } else if (status === "fail") {
                statusText.classList.add("status-fail");
            } else {
                statusText.classList.add("status-warning");
            }

            statusText.textContent = status;

            card.appendChild(title);
            card.appendChild(statusText);
            resultContainer.appendChild(card);
        });

        resultsDiv.appendChild(resultContainer);

        const summary = document.createElement("div");
        summary.className = "overall-summary";

        const summaryHeading = document.createElement("h4");
        summaryHeading.textContent = "Overall Security Summary";

        const summaryText = document.createElement("p");
        summaryText.textContent = data.overallSummary;

        summary.appendChild(summaryHeading);
        summary.appendChild(summaryText);

        resultsDiv.appendChild(summary);

        const note = document.createElement("p");
        note.className = "result-note";
        note.textContent = data.authenticationResultsFound
            ? "Results were extracted from the Authentication-Results header."
            : "No Authentication-Results header was found.";

        resultsDiv.appendChild(note);

    } catch (error) {
        resultsDiv.textContent =
            "An error occurred. Please check the server and try again.";
    }
});
