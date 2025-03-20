package com.test.demo.controllers;

import com.test.demo.services.ApiService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField emailField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Button loginButton;
    @FXML
    private Button accountButton;
    
    @FXML
    private void handleLogin(ActionEvent e) throws Exception {
        String username = emailField.getText();
        String password = passwordField.getText();
        
      
        if (ApiService.login(username, password)=="true") {
            openMainScreen();
        } else {
            System.out.println("Login failed");
        }
    }

    private void openMainScreen() {
        try {
            System.out.println("122222222222");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/test/demo/views/dashboard.fxml"));
            System.out.println("122222222222");
            Parent root = loader.load();
            System.out.println("122222222222");
            Stage stage = (Stage) loginButton.getScene().getWindow();
            System.out.println("122222222222");
            Scene scene = new Scene(root,1350,750);
            System.out.println("122222222222");
            scene.getStylesheets().add(getClass().getResource("/com/test/demo/assets/dashboard.css").toExternalForm());
            System.out.println("122222222222");
            stage.setScene(scene);
            stage.setX(50);
            stage.setY(30);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
