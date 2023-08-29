package com.spotifyxp.deps.de.werwolf2303.javasetuptool.components;

import com.spotifyxp.deps.de.werwolf2303.javasetuptool.PublicValues;
import com.spotifyxp.deps.de.werwolf2303.javasetuptool.Setup;
import com.spotifyxp.deps.de.werwolf2303.javasetuptool.utils.StreamUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;

public class AcceptComponent extends JPanel implements Component {
    JScrollPane pane;
    JEditorPane content;
    boolean scrolldown = false;

    @Override
    public String getName() {
        return "AcceptComponent";
    }

    public AcceptComponent() {
        content = new JEditorPane();
        content.setEditable(false);
        pane = new JScrollPane(content);
        add(pane, BorderLayout.CENTER);
    }

    public void load(String html) {
        content.setContentType("text/html");
        content.setText(html);
    }

    public void load(URL url) {
        try {
            content.setContentType("text/html");
            content.setText(StreamUtils.inputStreamToString(url.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setText(String text) {
        content.setText(text);
    }

    public JPanel drawable() {
        return this;
    }

    public void init() {
        pane.setPreferredSize(new Dimension(PublicValues.setup_width, PublicValues.setup_height - PublicValues.setup_bar_height));
        setPreferredSize(new Dimension(PublicValues.setup_width, PublicValues.setup_height - PublicValues.setup_bar_height));
    }

    public void nowVisible() {
        custom1.setText("Accept");
        custom1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                next.setEnabled(true);
                custom1.setVisible(false);
                custom2.setVisible(false);
            }
        });
        custom1.setVisible(true);
        scrolldown = true;
        custom1.setEnabled(false);
        custom2.setText("Decline");
        custom2.setVisible(true);
        custom2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        next.setEnabled(false);
        pane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener(){
            public void adjustmentValueChanged(AdjustmentEvent e) {
                if(!e.getValueIsAdjusting()){
                    JScrollBar scrollBar = (JScrollBar) e.getAdjustable();
                    int extent = scrollBar.getModel().getExtent();
                    int maximum = scrollBar.getModel().getMaximum();
                    if(extent + e.getValue() == maximum){
                        custom1.setEnabled(true);
                    }
                }
            }
        });
    }

    public void onLeave() {
        custom2.setVisible(false);
        next.setEnabled(true);
        custom1.setVisible(false);
    }

    JButton next;
    JButton prev;
    JButton cancel;
    JButton custom1;
    JButton custom2;
    Runnable fin;

    public void giveComponents(JButton next, JButton previous, JButton cancel, JButton custom1, JButton custom2, Runnable finish, Setup.SetupBuilder builder) {
        this.fin = finish;
        this.next = next;
        this.prev = previous;
        this.cancel = cancel;
        this.custom1 = custom1;
        this.custom2 = custom2;
    }
}
