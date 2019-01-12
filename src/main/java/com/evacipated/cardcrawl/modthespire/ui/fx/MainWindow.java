package com.evacipated.cardcrawl.modthespire.ui.fx;

import com.evacipated.cardcrawl.modthespire.ui.fx.viewmodels.ViewModel;
import com.evacipated.cardcrawl.modthespire.ui.fx.views.ModItem;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.effect.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainWindow extends Application implements Initializable {

    private DropShadow launchButtonDropShadow = new DropShadow(10, Color.BLACK);
    private ViewModel viewModel;
    //UI Elements
    @FXML private Button buttonLaunch;
    @FXML private Button buttonWorkshop;
    @FXML private Button buttonUpdates;
    @FXML private AnchorPane modListPane;
    @FXML private AnchorPane bottomPane;
    @FXML private AnchorPane modInfoPane;
    @FXML private AnchorPane launchButtonPane;
    @FXML private RadioButton debugButton;
    @FXML private ListView<ModItem> modListView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("assets/fx/views/main.fxml"));
        Scene mainScene = new Scene(root);
        primaryStage.setTitle("ModTheSpire");
        primaryStage.setScene(mainScene);
        primaryStage.show();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewModel = new ViewModel();
        initOnClicks();
        initStyles();

        modListView.setItems(viewModel.getModItems());
        modListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            newValue.setEffect(new Glow(.3));
            if(oldValue != null) {
                oldValue.setEffect(new Blend());
            }
        });
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
