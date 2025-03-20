package com.test.demo.services;

import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AuthService {
    private static final String API_URL = "http://spring-boot-demo-env.eba-p4mintvf.ap-southeast-2.elasticbeanstalk.com/users/login"; // Thay URL API thật

    public static void main(String[] args) throws Exception {
        String response = login("hello", "123");
        System.out.println("Server Response: " + response);
    }

    public static String login(String username, String password) throws Exception {
        // Khởi tạo ObjectMapper của Jackson
        ObjectMapper objectMapper = new ObjectMapper();
        
        // Tạo JSON request body
        ObjectNode jsonNode = objectMapper.createObjectNode();
        jsonNode.put("username", username);
        jsonNode.put("password", password);
        String requestBody = objectMapper.writeValueAsString(jsonNode);
        
        // Tạo HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // Tạo HttpRequest với phương thức POST
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        // Gửi yêu cầu và nhận phản hồi
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Kiểm tra nếu server trả về false thì đăng nhập thất bại
        if (response.body().equals("false")) {
            return null;
        }
        return "true";
        // Phân tích JSON để lấy token
//        JsonNode responseJson = objectMapper.readTree(response.body());
//        return responseJson.has("token") ? responseJson.get("token").asText() : null;
    }
}


