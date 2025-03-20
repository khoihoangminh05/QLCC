package com.test.demo.controllers;

import com.test.demo.models.User;
import com.test.demo.services.ApiService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;


public class AccountController {

    @FXML
    private VBox vboxIcon;
    
    @FXML
    private Button registerButton;

    @FXML
    private Button statisticsIcon;

    @FXML
    private VBox statisticsTable;

    @FXML
    private TableView<User> tableView;

    @FXML
    private TableColumn<User, String> usernameColumn;

    @FXML
    private TableColumn<User, String> fullNameColumn;

    @FXML
    private TableColumn<User, String> roleColumn;

    @FXML
    private TableColumn<User, String> activeColumn;

    @FXML
    private TableColumn<User, String> createdAtColumn;

    @FXML
    private TableColumn<User, String> updatedAtColumn;

    //private final ApiService userService = new ApiService();

    @FXML
    public void initialize() {
        statisticsTable.setVisible(false);

        statisticsIcon.setOnAction(event -> showStatistics());

        usernameColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getUsername()));
        fullNameColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getFullName()));
        roleColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getRole()));
        activeColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().isActive() ? "Active" : "Inactive"));
        createdAtColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCreatedAt().toString()));
        updatedAtColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getUpdatedAt().toString()));
    
        usernameColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.1));   
        fullNameColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.1)); 
        roleColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.1));
        activeColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.1));  
        createdAtColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.3)); 
        updatedAtColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.3));
    
    
    }

    private void showStatistics() {
        vboxIcon.setVisible(false);
        vboxIcon.setManaged(false);
        statisticsTable.setVisible(true);
        statisticsTable.setManaged(true);

        new Thread(() -> {
            try {
                List<User> users = ApiService.getAllUsers();
                Platform.runLater(() -> {
                    ObservableList<User> data = FXCollections.observableArrayList(users);
                    tableView.setItems(data);
                });
            } catch (Exception e) {
                Platform.runLater(() -> showError("Lỗi khi lấy dữ liệu: " + e.getMessage()));
            }
        }).start();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    @FXML
    private void openRegisterWindow() {
    	try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/test/demo/views/dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) registerButton.getScene().getWindow();
            Scene scene = new Scene(root,1350,750);
            scene.getStylesheets().add(getClass().getResource("/com/test/demo/assets/dashboard.css").toExternalForm());
            stage.setScene(scene);
            stage.setX(50);
            stage.setY(30);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

