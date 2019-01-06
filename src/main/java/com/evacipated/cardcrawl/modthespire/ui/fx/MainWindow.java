package com.evacipated.cardcrawl.modthespire.ui.fx;

import com.evacipated.cardcrawl.modthespire.ui.fx.viewmodels.ViewModel;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainWindow extends Application implements Initializable {

    private ViewModel viewModel;
    private DropShadow launchButtonDropShadow = new DropShadow(10, Color.BLACK);

    //UI Elements
    @FXML private Button buttonLaunch;
    @FXML private Button buttonWorkshop;
    @FXML private Button buttonUpdates;
    @FXML private AnchorPane modListPane;
    @FXML private AnchorPane bottomPane;
    @FXML private AnchorPane modInfoPane;
    @FXML private AnchorPane launchButtonPane;
    @FXML private RadioButton debugButton;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("assets/fx/views/main.fxml"));
        Scene mainScene = new Scene(root);
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(mainScene);
        primaryStage.show();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewModel = new ViewModel();
        initOnClicks();
        initStyles();
    }

    private void initStyles(){
        bottomPane.setEffect(new DropShadow(10, 0, -10, Color.BLACK));
    }

    private void initOnClicks(){
        buttonLaunch.setOnMouseClicked(event -> {
            Boolean selected = debugButton.isSelected();
            viewModel.launchSTS(selected);
        });

        buttonLaunch.setOnMousePressed(event -> {
            launchButtonPane.setEffect(new Blend());
        });

        buttonLaunch.setOnMouseReleased(event -> {
            launchButtonPane.setEffect(launchButtonDropShadow);
        });

        buttonUpdates.setOnMouseClicked(event -> {
            viewModel.searchForUpdates();
        });

        buttonWorkshop.setOnMouseClicked(event -> {
            viewModel.openWorkshopUrl();
        });
    }


}
