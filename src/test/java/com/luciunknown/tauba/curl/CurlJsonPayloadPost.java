package com.luciunknown.tauba.curl;

import com.luciunknown.tauba.core.Curl;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CurlJsonPayloadPost {

    @Test
    public void testPostRequestWithJsonPayload() throws Exception {
        String url = "https://example.com/api";
        Map<String, String> headers = Map.of("Content-Type", "application/json", "Authorization", "Bearer token");
        String body = "{\"name\":\"John Doe\",\"age\":30}"; // JSON payload
        String folderPath = "test_output";
        String fileName = "post_json_request.txt";

        // Build the cURL command
        Curl curl = Curl.builder()
                .httpMethod(HttpMethod.POST)
                .url(url)
                .headers(headers)
                .body(body)
                .build();

        // Generate the file
        curl.execute(folderPath, fileName);

        // Verify the file content
        String fileContent = Files.readString(Paths.get(folderPath + File.separator + fileName));
        assertTrue(fileContent.contains("curl -X POST 'https://example.com/api'"));
        assertTrue(fileContent.contains("-H 'Content-Type: application/json'"));
        assertTrue(fileContent.contains("-H 'Authorization: Bearer token'"));
        assertTrue(fileContent.contains("-d '{\"name\":\"John Doe\",\"age\":30}'"));
    }
}