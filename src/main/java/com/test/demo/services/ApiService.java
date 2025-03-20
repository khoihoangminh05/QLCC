package com.test.demo.services;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.test.demo.models.User;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;


public class ApiService {
	 private static final String BASE_URL = "http://spring-boot-demo-env.eba-p4mintvf.ap-southeast-2.elasticbeanstalk.com";
	   
	 
	 public static String login(String username, String password)  {
		 try {
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
	                .uri(URI.create(BASE_URL + "/users/login"))
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
//	        JsonNode responseJson = objectMapper.readTree(response.body());
//	        return responseJson.has("token") ? responseJson.get("token").asText() : null;
	        
		    } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	    }
	    
	    
	 public static  List<User> getAllUsers()  {
		 try {
		    HttpClient client = HttpClient.newHttpClient();
	        HttpRequest request = HttpRequest.newBuilder()
	                .uri(URI.create(BASE_URL + "/users/all"))
	                .GET()
	                .build();

	        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

	        if (response.statusCode() != 200) {
	            throw new RuntimeException("Failed to fetch users: " + response.statusCode());
	        }

	        ObjectMapper mapper = new ObjectMapper();
	        return mapper.readValue(response.body(), new TypeReference<List<User>>() {});
		    } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	    }
}
