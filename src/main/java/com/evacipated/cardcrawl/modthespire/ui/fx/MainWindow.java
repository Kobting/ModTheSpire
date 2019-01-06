package com.evacipated.cardcrawl.modthespire.ui.fx;

import com.evacipated.cardcrawl.modthespire.ui.fx.viewmodels.ViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainWindow extends Application {

    private ViewModel viewModel;

    //UI Elements
    private Button buttonLaunch;
    private Button buttonWorkshop;
    private Button buttonUpdates;

    public MainWindow(ViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("assets/fx/views/main.fxml"));
        Scene mainScene = new Scene(root);
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(mainScene);
        primaryStage.show();

        initUIElements(mainScene);
    }

    private void initUIElements(Scene scene){
        buttonLaunch = (Button) scene.lookup("#buttonLaunch");
        buttonWorkshop = (Button) scene.lookup("#buttonWorkshop");
        buttonUpdates = (Button) scene.lookup("#buttonUpdates");
        System.out.println("initialize");

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
