
package com.emailgateway.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet("/analyze")
public class HeaderAnalyzerServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String rawHeader = request.getParameter("emailHeader");

        if (rawHeader == null || rawHeader.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(
                "{\"error\":\"Please provide email headers.\"}"
            );
            return;
        }

        StringBuilder authenticationResults = new StringBuilder();
        boolean collecting = false;

        for (String line : rawHeader.split("\\r?\\n")) {

            if (line.matches("(?i)^Authentication-Results\\s*:.*")) {
                collecting = true;
                authenticationResults.append(line).append(" ");

            } else if (collecting && line.matches("^\\s+.*")) {
                authenticationResults.append(line.trim()).append(" ");

            } else {
                collecting = false;
            }
        }

        String authResults = authenticationResults.toString();

        String spf = extractResult(authResults, "spf");
        String dkim = extractResult(authResults, "dkim");
        String dmarc = extractResult(authResults, "dmarc");

        String overallSummary = getOverallSummary(spf, dkim, dmarc);

        String json = "{"
                + "\"spf\":\"" + escapeJson(spf) + "\","
                + "\"dkim\":\"" + escapeJson(dkim) + "\","
                + "\"dmarc\":\"" + escapeJson(dmarc) + "\","
                + "\"overallSummary\":\""
                + escapeJson(overallSummary) + "\","
                + "\"authenticationResultsFound\":"
                + (!authResults.isEmpty()) + ","
                + "\"message\":\"Analysis completed\""
                + "}";

        response.getWriter().write(json);
    }

    private String extractResult(String header, String protocol) {

        Pattern pattern = Pattern.compile(
            "(?i)(?:^|[;\\s])" + protocol
            + "\\s*=\\s*(pass|fail|softfail|neutral|none|"
            + "temperror|permerror|policy|bestguesspass)"
        );

        Matcher matcher = pattern.matcher(header);

        if (matcher.find()) {
            return matcher.group(1).toLowerCase();
        }

        return "Not found";
    }

    private String getOverallSummary(String spf, String dkim, String dmarc) {

        if (spf.equals("fail") ||
            dkim.equals("fail") ||
            dmarc.equals("fail")) {

            return "Authentication failure detected.";
        }

        if (spf.equals("pass") &&
            dkim.equals("pass") &&
            dmarc.equals("pass")) {

            return "All authentication checks passed.";
        }

        if (spf.equals("Not found") ||
            dkim.equals("Not found") ||
            dmarc.equals("Not found")) {

            return "Authentication results are incomplete.";
        }

        return "Authentication requires further review.";
    }

    private String escapeJson(String value) {
        return value.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r");
    }
}
