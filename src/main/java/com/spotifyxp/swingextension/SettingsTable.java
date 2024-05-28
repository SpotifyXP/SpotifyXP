package com.spotifyxp.swingextension;

import com.spotifyxp.PublicValues;

import javax.swing.*;
import java.awt.*;

public class SettingsTable extends JPanel {
    int ycache = 10;

    public SettingsTable() {
        setLayout(null);
    }

    public void addSetting(String name, JComponent component) {
        JTextField field = new JTextField(name);
        field.setHorizontalAlignment(SwingConstants.RIGHT);
        field.setEditable(false);
        field.setBorder(BorderFactory.createEmptyBorder());
        field.setForeground(PublicValues.globalFontColor);
        field.setBounds(10, ycache, getWidth() / 2 - 40, 30);
        component.setBounds(10 + field.getWidth(), ycache, getWidth() / 2, 30);
        component.setForeground(PublicValues.globalFontColor);
        add(component);
        add(field);
        ycache+=component.getHeight()+10;
    }

    public void addSetting(String name, boolean nameBold, JComponent component) {
        JTextField field = new JTextField(name);
        if(nameBold) field.setFont(new Font(getFont().getName(), Font.BOLD, getFont().getSize()));
        field.setHorizontalAlignment(SwingConstants.RIGHT);
        field.setEditable(false);
        field.setBorder(BorderFactory.createEmptyBorder());
        field.setForeground(PublicValues.globalFontColor);
        field.setBounds(10, ycache, getWidth() / 2 - 40, 30);
        if(component != null) {
            component.setBounds(10 + field.getWidth(), ycache, getWidth() / 2, 30);
            component.setForeground(PublicValues.globalFontColor);
            add(component);
            ycache+=component.getHeight()+10;
        }else{
            ycache+=40;
        }
        add(field);
    }
}
