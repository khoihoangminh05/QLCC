package com.test.demo.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class DashBoardController {

	@FXML
	private TextField searchField;
	@FXML
	private Button logoutButton;
	
	@FXML
	private Button overviewButton;
	
    @FXML
    private BorderPane mainBorderPane;
    
    @FXML
    private void handleLogout() {
    	try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/test/demo/views/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            Scene scene = new Scene(root,450,450);
            scene.getStylesheets().add(getClass().getResource("/com/test/demo/assets/style.css").toExternalForm());
            stage.setScene(scene);
            stage.setX(500);
            stage.setY(150);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleOverviewButton() {
        loadFXML("/com/test/demo/views/overview.fxml","");
    }

    @FXML
    private void handleAccountButton() {
        loadFXML("/com/test/demo/views/account_management.fxml","/com/test/demo/assets/account_management.css");
    }

    @FXML
    private void handleFeeManagementButton() {
        loadFXML("/com/test/demo/views/fee_management.fxml","");
    }

    // Phương thức để load FXML và đặt vào center
    private void loadFXML(String fxmlFile,String cssFile) {
        try {
        	
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            VBox content = loader.load();
            mainBorderPane.getStylesheets().clear();
            mainBorderPane.getStylesheets().add(getClass().getResource(cssFile).toExternalForm());

            mainBorderPane.setCenter(content); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
