package com.spotifyxp.testing;


import com.google.protobuf.Message;
import com.google.protobuf.Struct;
import com.google.protobuf.util.JsonFormat;
import com.spotifyxp.PublicValues;
import com.spotifyxp.analytics.Analytics;
import com.spotifyxp.api.SpotifyAPI;
import com.spotifyxp.api.UnofficialSpotifyAPI;
import com.spotifyxp.video.VideoPlayer;
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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TimeZone;

public class Test {
    static String protobufjson = "\"nested\":{\"com\":{\"nested\":{\"spotify\":{\"nested\":{\"canvazcache\":{\"options\":{\"optimize_for\":\"CODE_SIZE\",\"java_package\":\"com.spotify.canvaz\",},\"nested\":{\"Artist\":{\"fields\":{\"uri\":{\"type\":\"string\",\"id\":1},\"name\":{\"type\":\"string\",\"id\":2},\"avatar\":{\"type\":\"string\",\"id\":3},},},\"EntityCanvazResponse\":{\"fields\":{\"canvases\":{\"rule\":\"repeated\",\"type\":\"Canvaz\",\"id\":1},\"ttlInSeconds\":{\"type\":\"int64\",\"id\":2},},\"nested\":{\"Canvaz\":{\"fields\":{\"id\":{\"type\":\"string\",\"id\":1},\"url\":{\"type\":\"string\",\"id\":2},\"fileId\":{\"type\":\"string\",\"id\":3},\"type\":{\"type\":\"canvaz.Type\",\"id\":4},\"entityUri\":{\"type\":\"string\",\"id\":5},\"artist\":{\"type\":\"Artist\",\"id\":6},\"explicit\":{\"type\":\"bool\",\"id\":7},\"uploadedBy\":{\"type\":\"string\",\"id\":8},\"etag\":{\"type\":\"string\",\"id\":9},\"canvasUri\":{\"type\":\"string\",\"id\":11},},},},},\"EntityCanvazRequest\":{\"fields\":{\"entities\":{\"rule\":\"repeated\",\"type\":\"Entity\",\"id\":1},},\"nested\":{\"Entity\":{\"fields\":{\"entityUri\":{\"type\":\"string\",\"id\":1},\"etag\":{\"type\":\"string\",\"id\":2},},},},},},},\"canvaz\":{\"options\":{\"optimize_for\":\"CODE_SIZE\",\"java_package\":\"com.spotify.canvaz\",},\"nested\":{\"Type\":{\"values\":{\"IMAGE\":0,\"VIDEO\":1,\"VIDEO_LOOPING\":2,\"VIDEO_LOOPING_RANDOM\":3,\"GIF\":4,},},},},},},},},},}";
    public static Message fromJson(String json) {
        try {
            Message.Builder structBuilder = Struct.newBuilder();
            JsonFormat.parser().ignoringUnknownFields().merge(json, structBuilder);
            return structBuilder.build();
        }catch (Exception e) {
            return null;
        }
    }
    public static void main(String[] args) throws Exception {
        VideoPlayer player = new VideoPlayer(UnofficialSpotifyAPI.getCanvasURLForTrack("Linkin Park", "Meteora 20th Anniversary Edition", "Hit the Floor"));
        player.show();
        player.play();
    }
}
