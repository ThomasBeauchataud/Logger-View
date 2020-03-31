package com.github.ffcfalcos.logger.view;

import com.github.ffcfalcos.logger.view.util.ResourcesService;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.util.Arrays;

public class LoggerView extends Application {

    private ConfigManager configManager;
    private RulesManager rulesManager;

    @Override
    public void start(Stage primaryStage) {
        configManager = new ConfigManager();
        rulesManager = new RulesManager(configManager);
        BorderPane root = new BorderPane();

        Label label = new Label();

        Button button = new Button("Change root path");
        button.setOnAction((event) -> {
            configManager.setRoot(new DirectoryChooser().showDialog(primaryStage));
            label.setText("Actual root path : " + configManager.getRootPath());
            configManager.initialize();
            rulesManager.reloadRules();
        });
        root.setTop(button);
        GridPane gridPane = new GridPane();
        GridPane top = new GridPane();
        top.getColumnConstraints().addAll(Arrays.asList(new ColumnConstraints(), new ColumnConstraints(10)));
        top.add(button, 0, 0);
        label.setText("Actual config path : " + configManager.getRootPath());
        top.add(label, 2, 0);
        gridPane.add(top, 0, 0);

        gridPane.add(rulesManager.getActiveRulesView(), 0, 3);
        gridPane.add(rulesManager.getPassiveRulesView(), 0, 6);
        Label activeRulesLabel = new Label("Activated Rules");
        Label passiveRulesLabel = new Label("Deactivated Rules");
        activeRulesLabel.setFont(new Font(18));
        passiveRulesLabel.setFont(new Font(18));
        gridPane.add(activeRulesLabel, 0, 2);
        gridPane.add(passiveRulesLabel, 0, 5);
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setPrefHeight(20);
        gridPane.getRowConstraints().addAll(new RowConstraints(), rowConstraints, new RowConstraints(), new RowConstraints(), rowConstraints);
        root.setPadding(new Insets(30, 50, 30, 50));

        root.setCenter(gridPane);
        primaryStage.setTitle("Logger Manager View");

        try {
            primaryStage.getIcons().add(ResourcesService.getImageFromResourcesName("logo.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        primaryStage.setScene(new Scene(root));
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
