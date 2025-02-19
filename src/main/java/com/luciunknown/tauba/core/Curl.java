package com.luciunknown.tauba.core;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpMethod;

import java.io.File;
import java.io.FileWriter;
import java.util.Map;

@Getter
@ToString
@Builder
public class Curl {
    private final HttpMethod httpMethod;
    private final String url;
    private final Map<String, String> parameters;
    private final Map<String, String> headers;
    private final String body;

    public void execute(String folderPath, String fileName) {
        StringBuilder curlCommand = new StringBuilder("curl -X ").append(httpMethod.name());

        // Add URL
        curlCommand.append(" '").append(url);

        if (!httpMethod.equals(HttpMethod.GET)) {
            curlCommand.append("'");
        }

        // Add parameters (for GET requests, parameters are added to the URL)
        if (parameters != null && !parameters.isEmpty()) {
            if (HttpMethod.GET.equals(httpMethod)) {
                // For GET requests, append parameters to the URL as query strings
                curlCommand.append("?");
                for (Map.Entry<String, String> entry : parameters.entrySet()) {
                    curlCommand.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                }
                // Remove the trailing '&'
                curlCommand.deleteCharAt(curlCommand.length() - 1);
                curlCommand.append("'");
            } else {
                // For other methods, use --data-urlencode or -d
                curlCommand.append(" \\\n  ");
                for (Map.Entry<String, String> entry : parameters.entrySet()) {
                    curlCommand.append("--data-urlencode '").append(entry.getKey()).append("=").append(entry.getValue()).append("' \\\n  ");
                }
            }
        }

        // Add headers
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                curlCommand.append(" \\\n  -H '").append(entry.getKey()).append(": ").append(entry.getValue()).append("'");
            }
        }

        // Add body (only for non-GET requests)
        if (body != null && !body.isEmpty() && !HttpMethod.GET.equals(httpMethod)) {
            curlCommand.append(" \\\n  -d '").append(body).append("'");
        }

        writeFile(curlCommand, folderPath, fileName);
    }

    private void writeFile(StringBuilder curlCommand, String folderPath, String fileName) {
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs(); // Create all necessary directories
        }
        String filePath = folderPath + File.separator + fileName;
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(curlCommand.toString());
            System.out.println("cURL command has been written to " + filePath);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}