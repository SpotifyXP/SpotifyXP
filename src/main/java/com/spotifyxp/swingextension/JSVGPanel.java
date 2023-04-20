package com.spotifyxp.swingextension;

import com.spotifyxp.exception.ExceptionDialog;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.panels.ContentPanel;
import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.svg.SVGDocument;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class JSVGPanel extends JSVGCanvas {
    String rad = "";
    public boolean isFilled = false;
    public void setImage(InputStream stream) {
        try {
            String parser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(parser);
            SVGDocument document = factory.createSVGDocument("", stream);
            setSVGDocument(document);
        }catch (IOException e) {
            ExceptionDialog.open(e);
            ConsoleLogging.Throwable(e);
        }
    }

    public JSVGPanel() {
        setBackground(ContentPanel.frame.getBackground());
    }

    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g);
        Graphics2D graphics2D = (Graphics2D) g;
        if(!(rad.equals(""))) {
            graphics2D.rotate(Double.parseDouble(rad), (float)this.getWidth() / 2, (float)this.getHeight() / 2);
        }
    }
    public void setRotation(int percent) {
        rad = String.valueOf(((float) 360/100*percent) * 0.01745329252);
        repaint();
    }
}
