package com.example.myjavaproject;

import java.util.Scanner;

import Util.DBUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    static Scanner scan = new Scanner(System.in);
    //connect
    @Override
    public void start(Stage stage) throws IOException {


        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("UninaSocialGroup");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
}



    public static void main(String[] args) {
        launch();
    }
}