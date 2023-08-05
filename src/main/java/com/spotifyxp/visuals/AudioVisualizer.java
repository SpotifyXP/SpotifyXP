package com.spotifyxp.visuals;
import com.spotifyxp.PublicValues;
import com.spotifyxp.utils.SpectrumAnalyzer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AudioVisualizer extends JPanel {
    byte[] converted = null;
    JFrame frame = null;
    public void open() {
        frame = new JFrame(PublicValues.language.translate("ui.audiovisualizer.title"));
        setPreferredSize(new Dimension(800, 400));
        setBackground(Color.decode("#2596be"));
        frame.getContentPane().add(this);
        frame.pack();
        frame.setVisible(true);
    }

    public void close() {
        frame.dispose();
    }

    public boolean isVisible() {
        converted = null;
        if(frame == null) {
            return false;
        }
        return frame.isVisible();
    }

    double[] lastspectrumdata = null;

    @Override
    public void paint(Graphics gr) {
        super.paint(gr);
        if (converted != null) {
            try {
                gr.setColor(Color.decode("#2596be"));
                gr.fillRect(0, 0, getWidth(), getHeight());
                SpectrumAnalyzer analyzer = new SpectrumAnalyzer();
                double[] spectrumData = analyzer.analyzeAudio(converted);
                ArrayList<Color> colors = new ArrayList<Color>();
                for (int r=0; r<100; r++) colors.add(new Color(r*255/100,       255,         0));
                for (int g=100; g>0; g--) colors.add(new Color(      255, g*255/100,         0));
                for (int b=0; b<100; b++) colors.add(new Color(      255,         0, b*255/100));
                for (int r=100; r>0; r--) colors.add(new Color(r*255/100,         0,       255));
                for (int g=0; g<100; g++) colors.add(new Color(        0, g*255/100,       255));
                for (int b=100; b>0; b--) colors.add(new Color(        0,       255, b*255/100));
                colors.add(new Color(        0,       255,         0));
                int c = 0;
                if(spectrumData == null) {
                   if(lastspectrumdata == null) {
                       return;
                   }
                   spectrumData = lastspectrumdata;
                }
                for (int i = 0; i < spectrumData.length; i++) {
                    try {
                        gr.setColor(colors.get(c));
                    } catch (IndexOutOfBoundsException e) {
                        c = 0;
                        gr.setColor(colors.get(c));
                    }
                    double amp = spectrumData[i];
                    gr.drawLine(i, getHeight(), i, (int) (getHeight() - amp * 10));
                    c++;
                }
                lastspectrumdata = spectrumData;
            }catch (IllegalArgumentException ignored) {
            }
        }
    }


    public void update(byte[] converted)  {
        this.converted = converted;
        repaint();
    }
}
