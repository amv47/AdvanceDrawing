package com.example.javafxtest;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class AdvancedDrawingApp extends Application {
    private GraphicsContext gc;
    private Color currentColor = Color.BLACK;
    private double currentLineWidth = 3.0;
    private boolean textMode = false;
    private String textToDraw = "Your Text Here";

    @Override
    public void start(Stage stage) {
        // Canvas and GraphicsContext
        Canvas canvas = new Canvas(800, 600);
        gc = canvas.getGraphicsContext2D();

        // Initial Canvas Setup
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setStroke(currentColor);
        gc.setLineWidth(currentLineWidth);

        // Mouse Events for Drawing
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            if (textMode) {
                gc.fillText(textToDraw, e.getX(), e.getY());
            } else {
                gc.beginPath();
                gc.moveTo(e.getX(), e.getY());
                gc.stroke();
            }
        });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            if (!textMode) {
                gc.lineTo(e.getX(), e.getY());
                gc.stroke();
            }
        });

        // Scroll Event for Zooming
        canvas.addEventHandler(ScrollEvent.SCROLL, e -> {
            double zoomFactor = e.getDeltaY() > 0 ? 1.1 : 0.9;
            canvas.setScaleX(canvas.getScaleX() * zoomFactor);
            canvas.setScaleY(canvas.getScaleY() * zoomFactor);
        });

        // Toolbar
        HBox toolbar = new HBox(10);
        toolbar.setStyle("-fx-padding: 10; -fx-background-color: #f4f4f4;");

        // Color Picker
        ColorPicker colorPicker = new ColorPicker(currentColor);
        colorPicker.setOnAction(e -> {
            currentColor = colorPicker.getValue();
            gc.setStroke(currentColor);
        });

        // Line Width Selector
        Slider lineWidthSlider = new Slider(1, 20, currentLineWidth);
        lineWidthSlider.setShowTickLabels(true);
        lineWidthSlider.setShowTickMarks(true);
        lineWidthSlider.setMajorTickUnit(5);
        lineWidthSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            currentLineWidth = newVal.doubleValue();
            gc.setLineWidth(currentLineWidth);
        });

        // Clear Button
        Button clearButton = new Button("Clear");
        clearButton.setOnAction(e -> {
            gc.setFill(Color.WHITE);
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
            gc.setStroke(currentColor);
            gc.setLineWidth(currentLineWidth);
        });

        // Save Button
        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Files", "*.png"));
            File file = fileChooser.showSaveDialog(stage);
            if (file != null) {
                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(canvas.snapshot(null, null), null), "png", file);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Text Mode Toggle
        ToggleButton textModeButton = new ToggleButton("Text Mode");
        textModeButton.setOnAction(e -> textMode = textModeButton.isSelected());

        // Text Input Field
        TextField textInput = new TextField(textToDraw);
        textInput.setPromptText("Enter text to draw");
        textInput.textProperty().addListener((obs, oldText, newText) -> textToDraw = newText);

        // Add all controls to the toolbar
        toolbar.getChildren().addAll(
                new Label("Color:"), colorPicker,
                new Label("Line Width:"), lineWidthSlider,
                clearButton, saveButton,
                textModeButton, textInput
        );

        // Layout
        BorderPane root = new BorderPane();
        root.setCenter(canvas);
        root.setTop(toolbar);

        // Scene and Stage Setup
        Scene scene = new Scene(root, 800, 600, Color.WHITE);
        stage.setTitle("Advanced Drawing App");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
