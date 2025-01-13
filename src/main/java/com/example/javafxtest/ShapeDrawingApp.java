package com.example.javafxtest;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;



public class ShapeDrawingApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AdvanceDrawing.fxml"));
        Scene scene = new Scene(loader.load(), 1000, 700);

        // Enable Undo/Redo shortcuts
        scene.setOnKeyPressed(e -> {
            ShapeDrawingAppController controller = loader.getController();
            if (e.isControlDown() && e.getCode().toString().equals("Z")) controller.undo();
            if (e.isControlDown() && e.getCode().toString().equals("Y")) controller.redo();
            if (e.isControlDown() && e.getCode().toString().equals("S")) controller.saveCanvas();
        });

        Image icon = new Image("81ILaTyMxWL._AC_UF1000,1000_QL80_.jpg");
        stage.getIcons().add(icon);

        stage.setTitle("Shape Drawing App (FXML Version)");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}