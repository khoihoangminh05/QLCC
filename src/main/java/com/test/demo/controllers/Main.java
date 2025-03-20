package com.test.demo.controllers;

import java.net.URL;

public class Main {
    public static void main(String[] args) {
        URL url = Main.class.getResource("/com/test/demo/views/dashboard.fxml");
        System.out.println("FXML URL: " + url);
        if (url == null) {
            throw new RuntimeException("FXML file not found!");
        }
    }
}
