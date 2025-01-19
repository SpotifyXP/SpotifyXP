package com.spotifyxp.configuration;

import com.spotifyxp.PublicValues;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemListener;
import java.util.ArrayList;

public class CustomConfigValue<T> implements ICustomConfigValue<T>{
    private final String name;
    private final Object defaultValue;
    private final ArrayList<String> displayValues;
    private final ArrayList<T> possibleValues;
    private JComboBox<String> component;
    private final ConfigValueTypes internalTyp;
    private final boolean fromSpotifyXP;

    @SuppressWarnings("unchecked")
    CustomConfigValue(String name, Object defaultValue, ArrayList<String> displayValues, ArrayList<T> possibleValues, ConfigValueTypes internalType) {
        this.name = name;
        this.displayValues = displayValues;
        this.fromSpotifyXP = true;
        this.defaultValue = defaultValue;
        if(possibleValues == null) {
            this.possibleValues = (ArrayList<T>) displayValues;
        }else {
            this.possibleValues = possibleValues;
        }
        this.internalTyp = internalType;
    }

    @SuppressWarnings("unchecked")
    public CustomConfigValue(String name, Object defaultValue, ArrayList<String> displayValues, ArrayList<T> possibleValues, ConfigValueTypes internalType, boolean p) {
        this.name = name;
        this.fromSpotifyXP = false;
        this.displayValues = displayValues;
        this.defaultValue = defaultValue;
        if(possibleValues == null) {
            this.possibleValues = (ArrayList<T>) displayValues;
        }else {
            this.possibleValues = possibleValues;
        }
        this.internalTyp = internalType;
    }

    private static class ColoredComboBoxRenderer implements ListCellRenderer<String> {
        private final DefaultListCellRenderer defaultRenderer;

        public ColoredComboBoxRenderer() {
            defaultRenderer = new DefaultListCellRenderer();
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
            Component defaultComponent = defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            defaultComponent.setForeground(PublicValues.globalFontColor);
            return defaultComponent;
        }
    }

    @Override
    public ConfigValueTypes internalType() {
        return internalTyp;
    }

    @Override
    @SuppressWarnings("SuspiciousMethodCalls")
    public boolean check() {
        if(!fromSpotifyXP) throw new UnsupportedOperationException("check() is only available for SpotifyXP settings");
        return possibleValues.contains(PublicValues.config.getObject(name));
    }

    @Override
    public JComponent getComponent() {
        if(component == null) {
            if(fromSpotifyXP) {
                this.component = new JComboBox<>(displayValues.toArray(new String[0]));
                this.component.setRenderer(new ColoredComboBoxRenderer());
                this.component.setSelectedItem(PublicValues.config.getObject(name));
            }else {
                this.component = new JComboBox<>(displayValues.toArray(new String[0]));
                this.component.setRenderer(new ColoredComboBoxRenderer());
                this.component.setSelectedItem(name);
            }
        }
        return component;
    }

    @Override
    public void setOnClickListener(ItemListener l) {
        component.addItemListener(l);
    }

    @Override
    public void writeDefault() {
        if(!fromSpotifyXP) throw new UnsupportedOperationException("writeDefault() is only available for SpotifyXP settings");
        PublicValues.config.getProperties().put(name, defaultValue);
    }

    @Override
    public Object getValue() {
        return possibleValues.get(component.getSelectedIndex());
    }

    @Override
    public Object getDefaultValue() {
        return defaultValue;
    }

    @Override
    public ArrayList<T> getPossibleValues() {
        return possibleValues;
    }
}
