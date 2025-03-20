package com.test.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
    	try {
			System.out.println("sssssssssss");
			Parent root = FXMLLoader.load(getClass().getResource("views/login.fxml"));
			System.out.println("sssssssssss");
			Scene scene = new Scene(root,450,450);
			System.out.println("sssssssssss");
			scene.getStylesheets().add(getClass().getResource("assets/style.css").toExternalForm());
			System.out.println("sssssssssss");
			primaryStage.setScene(scene);
			primaryStage.setX(500);
			primaryStage.setY(150);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
    }

    public static void main(String[] args) {
        launch(args);
    }
}
