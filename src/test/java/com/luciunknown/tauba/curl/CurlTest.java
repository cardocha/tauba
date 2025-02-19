package com.luciunknown.tauba.curl;

import com.luciunknown.tauba.core.Curl;


import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class CurlTest {

    @Test
    public void testGetRequest() throws Exception {
        String url = "https://example.com/api";
        Map<String, String> parameters = Map.of("param1", "value1", "param2", "value2");
        Map<String, String> headers = Map.of("Authorization", "Bearer token");
        String body = "";
        String folderPath = "test_output";
        String fileName = "get_request.txt";

        // Build the cURL command
        Curl Curl = com.luciunknown.tauba.core.Curl.builder()
                .httpMethod(HttpMethod.GET)
                .url(url)
                .parameters(parameters)
                .headers(headers)
                .body(body)
                .build();

        // Generate the file
        Curl.execute(folderPath, fileName);

        // Verify the file content
        String fileContent = Files.readString(Paths.get(folderPath + File.separator + fileName));
        assertTrue(fileContent.contains("curl -X GET 'https://example.com/api?param1=value1&param2=value2'"));
        assertTrue(fileContent.contains("-H 'Authorization: Bearer token'"));
    }

    @Test
    public void testPostRequest() throws Exception {
        String url = "https://example.com/api";
        Map<String, String> parameters = Map.of("param1", "value1", "param2", "value2");
        Map<String, String> headers = Map.of("Content-Type", "application/json");
        String body = "{\"key\":\"value\"}";
        String folderPath = "test_output";
        String fileName = "post_request.txt";

        // Build the cURL command
        Curl curl = Curl.builder()
                .httpMethod(HttpMethod.POST)
                .url(url)
                .parameters(parameters)
                .headers(headers)
                .body(body)
                .build();

        // Generate the file
        curl.execute(folderPath, fileName);

        // Verify the file content
        String fileContent = Files.readString(Paths.get(folderPath + File.separator + fileName));
        assertTrue(fileContent.contains("curl -X POST 'https://example.com/api'"));
        assertTrue(fileContent.contains("--data-urlencode 'param1=value1'"));
        assertTrue(fileContent.contains("--data-urlencode 'param2=value2'"));
        assertTrue(fileContent.contains("-H 'Content-Type: application/json'"));
        assertTrue(fileContent.contains("-d '{\"key\":\"value\"}'"));
    }

    @Test
    public void testPutRequest() throws Exception {
        String url = "https://example.com/api";
        Map<String, String> parameters = Map.of("param1", "value1");
        Map<String, String> headers = Map.of("Content-Type", "application/json");
        String body = "{\"key\":\"value\"}";
        String folderPath = "test_output";
        String fileName = "put_request.txt";

        // Build the cURL command
        var curl = Curl.builder()
                .httpMethod(HttpMethod.PUT)
                .url(url)
                .parameters(parameters)
                .headers(headers)
                .body(body)
                .build();

        // Generate the file
        curl.execute(folderPath, fileName);

        // Verify the file content
        String fileContent = Files.readString(Paths.get(folderPath + File.separator + fileName));
        assertTrue(fileContent.contains("curl -X PUT 'https://example.com/api'"));
        assertTrue(fileContent.contains("--data-urlencode 'param1=value1'"));
        assertTrue(fileContent.contains("-H 'Content-Type: application/json'"));
        assertTrue(fileContent.contains("-d '{\"key\":\"value\"}'"));
    }

    @Test
    public void testDeleteRequest() throws Exception {
        String httpMethod = "DELETE";
        String url = "https://example.com/api";
        Map<String, String> parameters = Map.of("param1", "value1");
        Map<String, String> headers = Map.of("Authorization", "Bearer token");
        String body = "";
        String folderPath = "test_output";
        String fileName = "delete_request.txt";

        // Build the cURL command
        var curl = Curl.builder()
                .httpMethod(HttpMethod.DELETE)
                .url(url)
                .parameters(parameters)
                .headers(headers)
                .body(body)
                .build();

        // Generate the file
        curl.execute(folderPath, fileName);

        // Verify the file content
        String fileContent = Files.readString(Paths.get(folderPath + File.separator + fileName));
        assertTrue(fileContent.contains("curl -X DELETE 'https://example.com/api'"));
        assertTrue(fileContent.contains("--data-urlencode 'param1=value1'"));
        assertTrue(fileContent.contains("-H 'Authorization: Bearer token'"));
    }
}