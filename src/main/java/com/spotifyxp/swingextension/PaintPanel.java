package com.spotifyxp.swingextension;

import javax.swing.*;
import java.awt.*;

public class PaintPanel extends JPanel {

    @FunctionalInterface
    public interface onPaint {
        void run(Graphics g);
    }

    private onPaint onPaint;

    public PaintPanel(onPaint onPaint) {
        this.onPaint = onPaint;
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        onPaint.run(g);
    }
}
