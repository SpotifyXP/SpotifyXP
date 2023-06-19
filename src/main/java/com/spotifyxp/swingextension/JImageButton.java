package com.spotifyxp.swingextension;


import com.spotifyxp.PublicValues;
import com.spotifyxp.designs.Theme;
import com.spotifyxp.exception.ExceptionDialog;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.panels.ContentPanel;
import com.spotifyxp.threading.DefThread;
import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.svg.SVGDocument;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class JImageButton extends JSVGCanvas {
    //This will create a new type of jbutton that is not really a jbutton
    //but looks like one and acts like one
    private final BufferedImage image = null;
    public boolean isFilled = false;
    private String rad = "";
    JSVGCanvas canvas = this;
    void refresh() {
        this.repaint();
    }
    boolean highlight = false;
    boolean click = false;
    ActionListener l;
    public JImageButton() {
        JImageButton button = this;
        //graphics2D.drawImage(image.getScaledInstance(this.getWidth() / 4,this.getHeight() /2, Image.SCALE_SMOOTH), this.getWidth()/3 + 3, this.getHeight()/4, null);
        //canvas.setBounds(canvas.getWidth() / 2, canvas.getHeight() / 2, canvas.getX() - canvas.getWidth() / 2, canvas.getY() - canvas.getHeight() / 2);
        canvas.setBackground(getBackground());
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                highlight = false;
                canvas.repaint();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                highlight = true;
                canvas.repaint();
            }
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                click = true;
                canvas.repaint();
                l.actionPerformed(null);
                button.getMouseListeners()[0].mouseClicked(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                click = false;
                canvas.repaint();
            }
        });
        t.start();
        setBorderPainted(true);
        setFocusPainted(true);
        setContentAreaFilled(true);
    }

    boolean first = false;
    public void setImage(InputStream inputStream) {
        try {
            String parser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(parser);
            SVGDocument document = factory.createSVGDocument("", inputStream);
            canvas.setSVGDocument(document);
            if(first) {
                canvas.setBounds(canvas.getX(), canvas.getY(), canvas.getWidth() / 2, canvas.getHeight() / 2);
            }
        }catch (IOException e) {
            ExceptionDialog.open(e);
            ConsoleLogging.Throwable(e);
        }
        refresh();
    }

    Color oldColor = ContentPanel.frame.getBackground();

    public void addActionListener(ActionListener listener) {
        l = listener;
    }

    public void setRotation(int value) {
        rad = String.valueOf(value);
        this.repaint();
    }

    boolean paintBorder = true;
    boolean areaFilled = true;
    boolean focusPainted = true;

    public void setBorderPainted(boolean value) {
        paintBorder = value;
        if(value) {
            repaint();
        }else{
            canvas.setBorder(BorderFactory.createEmptyBorder());
            repaint();
        }
    }

    public void setContentAreaFilled(boolean value) {
        areaFilled = value;
        if(!value) {
            setBackground(oldColor);
        }
    }

    public void setFocusPainted(boolean value) {
        focusPainted = value;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (click) {
            setBackground(getBackground().brighter().brighter());
        } else {
            setBackground(oldColor);
        }
        if(focusPainted) {
            if (highlight) {
                setBackground(getBackground().brighter());
            } else {
                setBackground(oldColor);
            }
        }
        if(paintBorder) {
            g.setColor(oldColor.brighter());
            g.drawRoundRect(getX(), getY(), getWidth(), getHeight(), 15 ,15);
        }
        if(!(rad.equals(""))) {
            ((Graphics2D)g).rotate(Double.parseDouble(rad), (float)this.getWidth() / 2, (float)this.getHeight() / 2);
        }
    }

    DefThread t = new DefThread(new Runnable() {
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
