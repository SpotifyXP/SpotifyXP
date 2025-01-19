package com.spotifyxp.configuration;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.util.ArrayList;

public interface ICustomConfigValue<T>{
    ConfigValueTypes internalType();

    boolean check();

    JComponent getComponent();

    void setOnClickListener(ItemListener listener);

    void writeDefault();

    Object getValue();

    Object getDefaultValue();

    ArrayList<T> getPossibleValues();
}
