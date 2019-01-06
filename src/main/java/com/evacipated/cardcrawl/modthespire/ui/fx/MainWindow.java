package com.evacipated.cardcrawl.modthespire.ui.fx;

import com.evacipated.cardcrawl.modthespire.ui.fx.viewmodels.ViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainWindow extends Application {

    private ViewModel viewModel;

    //UI Elements
    private Button buttonLaunch;
    private Button buttonWorkshop;
    private Button buttonUpdates;
    private AnchorPane modListPane;
    private AnchorPane bottomPane;
    private AnchorPane modInfoPane;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        viewModel = new ViewModel();

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("assets/fx/views/main.fxml"));
        Scene mainScene = new Scene(root);
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(mainScene);
        primaryStage.show();

        initUIElements(mainScene);
        initOnClicks();
        initStyles();
    }

    private void initUIElements(Scene scene){
        buttonLaunch = (Button) scene.lookup("#buttonLaunch");
        buttonWorkshop = (Button) scene.lookup("#buttonWorkshop");
        buttonUpdates = (Button) scene.lookup("#buttonUpdates");
        modListPane = (AnchorPane) scene.lookup("#leftPane");
        modInfoPane = (AnchorPane) scene.lookup("#rightPane");
        bottomPane = (AnchorPane) scene.lookup("#bottomPane");
    }

    private void initStyles(){
        bottomPane.setEffect(new DropShadow(10, 0, -10, Color.BLACK));
    }

    private void initOnClicks(){
        buttonLaunch.setOnMouseClicked(event -> {
            //TODO check debug checkbox
            viewModel.launchSTS(false);
        });

        buttonUpdates.setOnMouseClicked(event -> {
            viewModel.searchForUpdates();
        });

        buttonWorkshop.setOnMouseClicked(event -> {
            viewModel.openWorkshopUrl();
        });
    }

}
