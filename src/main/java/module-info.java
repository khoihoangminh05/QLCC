module com.test.demo {
    requires java.net.http;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;
    requires com.fasterxml.jackson.databind;
    requires java.base;


    opens com.test.demo.models to com.fasterxml.jackson.databind;
    opens com.test.demo.controllers to javafx.fxml;
    opens com.test.demo to javafx.fxml;
    exports com.test.demo;
}