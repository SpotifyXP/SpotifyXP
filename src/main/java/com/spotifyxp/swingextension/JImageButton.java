package com.spotifyxp.swingextension;


import com.spotifyxp.PublicValues;
import com.spotifyxp.designs.Theme;
import com.spotifyxp.exception.ExceptionDialog;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.panels.ContentPanel;
import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.svg.SVGDocument;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class JImageButton extends JButton {
    private final BufferedImage image = null;
    public boolean isFilled = false;
    private final String rad = "";
    JSVGCanvas canvas = new JSVGCanvas();
    void refresh() {
        this.repaint();
    }
    public JImageButton() {
        JImageButton button = this;
        //graphics2D.drawImage(image.getScaledInstance(this.getWidth() / 4,this.getHeight() /2, Image.SCALE_SMOOTH), this.getWidth()/3 + 3, this.getHeight()/4, null);
        add(canvas);
        nw = this.getWidth() / 4;
        nh = this.getHeight() / 2;
        nx = this.getWidth() / 3 + 3;
        ny = this.getHeight() / 2;
        //canvas.setBounds(canvas.getWidth() / 2, canvas.getHeight() / 2, canvas.getX() - canvas.getWidth() / 2, canvas.getY() - canvas.getHeight() / 2);
        canvas.setBackground(getBackground());
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);

            }
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);

            }
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                button.getMouseListeners()[0].mouseClicked(e);
                button.setSelected(true);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                button.setSelected(false);
            }
        });
        t.start();
    }
    int nw = 0;
    int nh = 0;
    int nx = 0;
    int ny = 0;
    public void setImage(InputStream inputStream) {
        try {
            String parser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(parser);
            SVGDocument document = factory.createSVGDocument("", inputStream);
            canvas.setSVGDocument(document);
            canvas.setBounds(canvas.getBounds());
        }catch (IOException e) {
            ExceptionDialog.open(e);
            ConsoleLogging.Throwable(e);
        }
        refresh();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Color oldFg = getForeground();
        Color newFg = oldFg;
        if(PublicValues.theme == Theme.LEGACY) {
            ButtonModel mod = getModel();
            if (mod.isPressed()) {
                newFg = getForeground();
                g.setColor(Color.white);
                this.setBorderPainted(true);
            } else {
                this.setBorderPainted(false);
                g.setColor(getBackground());
            }
            g.fillRect(0, 0, getWidth(), getHeight());
            setForeground(newFg);
        }
        super.paintComponent(g);
        if(PublicValues.theme == Theme.LEGACY) {
            setForeground(oldFg);
        }
    }

    Thread t = new Thread(new Runnable() {
        @Override
        public void run() {
            while(!ContentPanel.frame.isVisible()) {
                try {
                    Thread.sleep(99);
                } catch (InterruptedException ignored) {
                }
            }
            canvas.setBounds(canvas.getBounds());
        }
    });
}
