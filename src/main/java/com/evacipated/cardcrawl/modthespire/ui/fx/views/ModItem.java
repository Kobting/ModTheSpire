package com.evacipated.cardcrawl.modthespire.ui.fx.views;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ModItem extends AnchorPane implements Initializable {

    @FXML private ImageView icon;
    @FXML private Label modName;
    @FXML private Label modVersion;

    private Node view;

    public ModItem() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("assets/fx/views/moditem.fxml"));
        fxmlLoader.setController(this);
        try {
            view = fxmlLoader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        getChildren().add(view);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.icon.setImage(new Image("assets/steam.png"));
    }

    public void setIcon(String iconUrl){
        this.icon.setImage(new Image(iconUrl));
    }

    public void setModName(String modName) {
        this.modName.setText(modName);
    }

    public void setModVersion(String modVersion) {
        this.modVersion.setText(modVersion);
    }
}
