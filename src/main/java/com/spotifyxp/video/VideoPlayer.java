package com.spotifyxp.video;

import com.spotifyxp.PublicValues;
import com.spotifyxp.api.UnofficialSpotifyAPI;
import com.spotifyxp.exception.ExceptionDialog;
import com.spotifyxp.logging.ConsoleLogging;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Screen;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Random;

public class VideoPlayer {
    String playbackURL;
    String file;
    JPanel videoPanel;
    JFrame frame;
    JFXPanel VFXPanel;
    Media m;
    MediaPlayer player;
    MediaView viewer;
    String playURL(String url) {
        Random r = new Random();
        String filename = r.nextInt() + "tmp.mp4";
        file = filename;
        new File(PublicValues.fileslocation, file).deleteOnExit();
        try {
            InputStream in = new URL(url).openStream();
            Files.copy(in, Paths.get(PublicValues.fileslocation + "/" + filename), StandardCopyOption.REPLACE_EXISTING);
        }catch (Exception e) {
            ConsoleLogging.Throwable(e);
            ExceptionDialog.open(e);
            return "";
        }
        return "file:/" + PublicValues.fileslocation.replace("\\", "/") + "/" + filename;
    }
    public VideoPlayer(String url) {
        videoPanel = new JPanel();
        frame = new JFrame();
        frame.getContentPane().add(videoPanel);
        VFXPanel = new JFXPanel();
        m = new Media(playURL(url));
        player = new MediaPlayer(m);
        viewer = new MediaView(player);
        playbackURL = url;
        StackPane root = new StackPane();
        Scene scene = new Scene(root);

        // center video position
        javafx.geometry.Rectangle2D screen = Screen.getPrimary().getVisualBounds();
        viewer.setX((screen.getWidth() - videoPanel.getWidth()) / 2);
        viewer.setY((screen.getHeight() - videoPanel.getHeight()) / 2);

        // resize video based on screen size
        DoubleProperty width = viewer.fitWidthProperty();
        DoubleProperty height = viewer.fitHeightProperty();
        width.bind(Bindings.selectDouble(viewer.sceneProperty(), "width"));
        height.bind(Bindings.selectDouble(viewer.sceneProperty(), "height"));
        viewer.setPreserveRatio(true);

        // add video to stackpane
        root.getChildren().add(viewer);

        VFXPanel.setScene(scene);
        //player.play();
        videoPanel.setLayout(new BorderLayout());
        videoPanel.add(VFXPanel, BorderLayout.CENTER);
        player.setCycleCount(9999); //Loop forever (not really but no one watches it for so long)
    }

    public void play() {
        player.play();
    }

    public void stop() {
        player.stop();
    }

    public void switchMedia(String url) {
        stop();
        new File(PublicValues.fileslocation, file).delete();
        playbackURL = url;
        m = new Media(playURL(url));
        play();
    }

    public void show() {
        frame.setPreferredSize(new Dimension(350, 500));
        frame.setVisible(true);
        frame.pack();
    }

    public void hide() {
        frame.dispose();
    }
}
