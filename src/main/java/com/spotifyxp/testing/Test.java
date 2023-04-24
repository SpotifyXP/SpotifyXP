package com.spotifyxp.testing;


import com.spotifyxp.PublicValues;
import com.spotifyxp.api.GitHubAPI;
import com.spotifyxp.api.SpotifyAPI;
import com.spotifyxp.api.UnofficialSpotifyAPI;
import com.spotifyxp.configuration.Config;
import com.spotifyxp.deps.com.spotify.extendedmetadata.ExtendedMetadata;
import com.spotifyxp.deps.xyz.gianlu.librespot.mercury.MercuryClient;
import com.spotifyxp.exception.ExceptionDialog;
import com.spotifyxp.lib.libLanguage;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.panels.HomePanel;
import com.spotifyxp.swingextension.JImageButton;
import com.spotifyxp.swingextension.JSVGPanel;
import com.spotifyxp.swingextension.JScrollText;
import com.spotifyxp.updater.UpdaterDialog;
import com.spotifyxp.utils.ConnectionUtils;
import com.spotifyxp.utils.PlayerUtils;
import com.spotifyxp.utils.Resources;
import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.apache.xmlgraphics.io.Resource;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.gvt.GVTTreeRendererAdapter;
import org.apache.batik.swing.gvt.GVTTreeRendererEvent;
import org.apache.batik.swing.svg.SVGDocumentLoaderAdapter;
import org.apache.batik.swing.svg.SVGDocumentLoaderEvent;
import org.apache.batik.swing.svg.GVTTreeBuilderAdapter;
import org.apache.batik.swing.svg.GVTTreeBuilderEvent;
import org.w3c.dom.svg.SVGDocument;

public class Test {
    public static void main(String[] args) {
        JFrame frame = new JFrame("SVGTest");
        HomePanel panel = new HomePanel(800, 2400);
        frame.add(panel.getComponent(), BorderLayout.CENTER);
        frame.setPreferredSize(new Dimension(800, 600));
        frame.setVisible(true);
        frame.pack();
    }
}
