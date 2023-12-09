package com.spotifyxp.testing;

import com.spotifyxp.PublicValues;
import com.spotifyxp.deps.xyz.gianlu.librespot.mercury.MercuryClient;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.video.CanvasPlayer;

import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException, MercuryClient.MercuryException, InterruptedException {
        ConsoleLogging logging = new ConsoleLogging();
        logging.setColored(true);
        PublicValues.debug = true;

        CanvasPlayer player = new CanvasPlayer();
        player.show();
        player.switchMedia("test.mp4");
        player.play();
    }
}
