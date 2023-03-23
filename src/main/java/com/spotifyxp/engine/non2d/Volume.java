package com.spotifyxp.engine.non2d;

import com.spotifyxp.PublicValues;
import com.spotifyxp.custom.StoppableThreadRunnable;
import com.spotifyxp.designs.Theme;
import com.spotifyxp.engine.Non2DElement;
import com.spotifyxp.swingextension.JImagePanel;
import com.spotifyxp.threading.StoppableThread;
import com.spotifyxp.utils.Resources;

import javax.swing.*;

public class Volume extends JPanel implements Non2DElement {
    JImagePanel speakericon = new JImagePanel();
    JPanel panel = this;
    JSlider volumeslider = new JSlider();
    public Volume() {
        //speakericon.setImage();
        volumeslider.setOrientation(SwingConstants.HORIZONTAL);
        volumeslider.setMaximum(10);
        volumeslider.setMinimum(0);
        volumeslider.setValue(10);
        if(PublicValues.theme == Theme.LIGHT) {
            speakericon.setImage(new Resources().readToInputStream("icons/volumefullwhite.png"));
        }else{
            speakericon.setImage(new Resources().readToInputStream("icons/volumefulldark.png"));
        }
        this.add(volumeslider);
        this.add(speakericon);
        StoppableThread thread = new StoppableThread(new StoppableThreadRunnable() {
            @Override
            public void run(int counter) {
                while(!panel.isVisible()) {
                }
                speakericon.setBounds(volumeslider.getX(), volumeslider.getY()+volumeslider.getHeight()+10, speakericon.getWidth(), speakericon.getHeight());
            }
        }, false);
        thread.start();
    }
    public void setLocations() {

    }
    public void setVertical() {
        volumeslider.setOrientation(SwingConstants.VERTICAL);
    }

}
