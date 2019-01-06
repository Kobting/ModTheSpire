package com.evacipated.cardcrawl.modthespire.ui.fx;

import com.evacipated.cardcrawl.modthespire.ui.fx.models.ModInfo;
import com.evacipated.cardcrawl.modthespire.ui.fx.models.ModItemInfo;
import com.evacipated.cardcrawl.modthespire.ui.fx.viewmodels.ViewModel;
import com.evacipated.cardcrawl.modthespire.ui.fx.views.ModItem;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
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
    @FXML private ListView<ModItem> modListView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("assets/fx/views/main.fxml"));
        Scene mainScene = new Scene(root);
        primaryStage.setTitle("Mod The Spire");
        primaryStage.setScene(mainScene);
        primaryStage.show();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewModel = new ViewModel();
        initOnClicks();
        initStyles();

        //TODO use the info we get from the viewModel to fill the mod items.
        ObservableList<ModItem> modItems = FXCollections.observableArrayList();
        modItems.addAll(makeModItems());
        modListView.setItems(modItems);
    }

    private ArrayList<ModItem> makeModItems() {
        ArrayList<ModItem> modItems = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            ModItem modItem = new ModItem();
            modItem.setModVersion("1.0." + i);
            modItem.setModName("Test Mod " + i);
            modItems.add(modItem);
        }

        return modItems;
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
