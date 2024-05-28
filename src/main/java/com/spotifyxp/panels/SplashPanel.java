package com.spotifyxp.panels;

import com.spotifyxp.exception.ElementNotFoundException;
import com.spotifyxp.swingextension.JFrame;
import com.spotifyxp.swingextension.JImagePanel;
import com.spotifyxp.utils.Resources;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.ArrayList;

public class SplashPanel {
    private JPanel contentPanel;
    private JLabel linfo;
    private JImagePanel image;
    private JFrame frame;
    private static ArrayList<Object[]> elements;

    public void show() {
        frame = new JFrame();
        linfo.setText("Please wait...");
        image.setImage(new Resources().readToInputStream("spotifyxp.png"));
        try {
            frame.setIconImage(ImageIO.read(new Resources().readToInputStream("spotifyxp.png")));
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
        frame.getContentPane().add(contentPanel);
        frame.setUndecorated(true);
        frame.setAlwaysOnTop(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.pack();
        frame.setLocation(frame.getLocation().x - frame.getWidth() / 2, frame.getLocation().y - frame.getHeight() / 2);
        elements = new ArrayList<>();
        addAllToElementList();
    }

    public static void hide() {
        getElementByNameAutoThrow("frame", JFrame.class).setVisible(false);
    }

    private void addAllToElementList() {
        addToElementList("contentPanel", contentPanel);
        addToElementList("linfo", linfo);
        addToElementList("image", image);
        addToElementList("frame", frame);
    }

    private void addToElementList(String name, Object instance) {
        elements.add(new Object[]{name, instance});
    }

    public static JPanel getContainer() {
        return getElementByNameAutoThrow("contentPanel", JPanel.class);
    }

    public static <E> E getElementByName(String name, Class<E> elementType) throws ElementNotFoundException {
        for(Object[] element : elements) {
            if(element[0].equals(name)) {
                return elementType.cast(element[1]);
            }
        }
        throw new ElementNotFoundException(elementType);
    }

    public static <E> E getElementByNameAutoThrow(String name, Class<E> elementType) {
        for(Object[] element : elements) {
            if(element[0].equals(name)) {
                return elementType.cast(element[1]);
            }
        }
        throw new RuntimeException(new ElementNotFoundException(elementType));
    }
}
