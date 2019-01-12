package com.evacipated.cardcrawl.modthespire.ui.fx.views;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ModItem extends AnchorPane implements Initializable {

    @FXML private ImageView icon;
    @FXML private Label modName;
    @FXML private Label modVersion;
    @FXML private AnchorPane activePane;

    private Node view;
    private Color selectedColor = Color.rgb(59, 190, 77);
    private Color deselectedColor = Color.GRAY;
    private boolean active = true;

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
        updateActiveState();
        activePane.setOnMouseClicked(event -> {
            setActive(!this.active);
        });
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

    public void setActive(boolean active) {
        this.active = active;
        updateActiveState();
    }

    private void updateActiveState() {
        if(this.active) {
            activePane.setBackground(new Background(new BackgroundFill(selectedColor, CornerRadii.EMPTY, Insets.EMPTY)));
        } else {
            activePane.setBackground(new Background(new BackgroundFill(deselectedColor, CornerRadii.EMPTY, Insets.EMPTY)));
        }
    }
}
