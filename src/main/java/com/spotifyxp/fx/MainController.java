package com.spotifyxp.fx;

import com.spotifyxp.PublicValues;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class MainController {
    @FXML
    private Label uititle;
    @FXML
    private Button uiclose;
    @FXML
    private Button uiscreencontrol;
    @FXML
    private Button uiminimize;
    @FXML
    private TextField uisearch;
    @FXML
    private ImageView uiforward;
    @FXML
    private ImageView uiback;
    @FXML
    private ImageView userdropdown;
    @FXML
    private ImageView userimage;
    @FXML
    private Label uisearchclear;
    @FXML
    private ImageView uiaddplaylist;
    @FXML
    private Label playertitle;
    @FXML
    private Label playerartist;
    @FXML
    private ImageView playericon;
    @FXML
    private Pane playerprevious;
    @FXML
    private Pane playerplaypause;
    @FXML
    private Pane playernext;
    @FXML
    private Label playertimecurrent;
    @FXML
    private Slider playertime;
    @FXML
    private Label playertimetotal;
    @FXML
    private Button playerlyrics;
    @FXML
    private Slider playervolume;
    @FXML
    private Pane playervolumeicon;

    public Label getUititle() {
        return uititle;
    }

    public Button getUiclose() {
        return uiclose;
    }

    public Button getUiscreencontrol() {
        return uiscreencontrol;
    }

    public Button getUiminimize() {
        return uiminimize;
    }

    public TextField getUisearch() {
        return uisearch;
    }

    public ImageView getUiforward() {
        return uiforward;
    }

    public ImageView getUiback() {
        return uiback;
    }

    public ImageView getUserdropdown() {
        return userdropdown;
    }

    public ImageView getUserimage() {
        return userimage;
    }

    public Label getUisearchclear() {
        return uisearchclear;
    }

    public ImageView getUiaddplaylist() {
        return uiaddplaylist;
    }

    public Label getPlayertitle() {
        return playertitle;
    }

    public Label getPlayerartist() {
        return playerartist;
    }

    public ImageView getPlayericon() {
        return playericon;
    }

    public Pane getPlayerprevious() {
        return playerprevious;
    }

    public Pane getPlayerplaypause() {
        return playerplaypause;
    }

    public Pane getPlayernext() {
        return playernext;
    }

    public Label getPlayertimecurrent() {
        return playertimecurrent;
    }

    public Label getPlayertimetotal() {
        return playertimetotal;
    }

    public Button getPlayerlyrics() {
        return playerlyrics;
    }

    public Slider getPlayervolume() {
        return playervolume;
    }

    public Pane getPlayervolumeicon() {
        return playervolumeicon;
    }



    public void initialize() {
        PublicValues.newController = this;
    }

    public void closeClicked(MouseEvent mouseEvent) {
        //Highlight color #BD3000
        System.exit(0);
    }
}
