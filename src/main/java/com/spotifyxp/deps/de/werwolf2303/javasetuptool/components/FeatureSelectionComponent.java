package com.spotifyxp.deps.de.werwolf2303.javasetuptool.components;

import com.spotifyxp.deps.de.werwolf2303.javasetuptool.PublicValues;
import com.spotifyxp.deps.de.werwolf2303.javasetuptool.Setup;
import com.spotifyxp.deps.de.werwolf2303.javasetuptool.swingextensions.JCheckBoxTree;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.util.ArrayList;

public class FeatureSelectionComponent extends JPanel implements Component {
    public static class Feature {
        public String name;
        public boolean selected = true;
        public final boolean required = false;
    }

    final JCheckBoxTree tree = new JCheckBoxTree();
    final JScrollPane pane = new JScrollPane();
    final JLabel l = new JLabel("Select features to install");
    Setup.SetupBuilder builder;
    final ArrayList<Feature> features = new ArrayList<>();
    DefaultMutableTreeNode root;
    DefaultTreeModel model;

    public FeatureSelectionComponent() {
        setLayout(null);
        add(l);
        add(pane);
    }

    @Override
    public JPanel drawable() {
        return this;
    }

    public ArrayList<Feature> getFeatures() {
        return features;
    }

    @Override
    public void init() {
        if(builder == null) {
            return;
        }
        root = new DefaultMutableTreeNode(builder.progname);
        setPreferredSize(new Dimension(PublicValues.setup_width, PublicValues.setup_height - PublicValues.setup_bar_height));
        pane.setSize(new Dimension(PublicValues.setup_width, PublicValues.setup_height - PublicValues.setup_bar_height - 50));
        l.setSize(new Dimension(PublicValues.setup_width, 30));
        l.setLocation(5, 2);
        pane.setLocation(0, 30);
        for(Feature feature : features) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(feature.name);
            root.add(node);
        }
        tree.addCheckActionListener(checkBox -> {
            for(Feature feature : features) {
                if(feature.name.equals(checkBox.getText())) {
                    if(feature.required) {
                        if(!checkBox.isSelected()) {
                            next.setEnabled(false);
                            JOptionPane.showMessageDialog(PublicValues.INTERNALContentManager, feature.name + " is required");
                        }else{
                            next.setEnabled(true);
                        }
                    }else {
                        feature.selected = checkBox.isSelected();
                    }
                    break;
                }
            }
        });
        model = new DefaultTreeModel(root);
        tree.setModel(model);
        pane.setViewportView(tree);
    }

    @Override
    public String getName() {
        return "FeatureSelectionComponent";
    }

    public void addFeature(Feature feature) {
        features.add(feature);
    }

    @Override
    public void nowVisible() {

    }

    @Override
    public void onLeave() {
        PublicValues.features = features;
        tree.save();
    }

    JButton next;

    @Override
    public void giveComponents(JButton next, JButton previous, JButton cancel, JButton custom1, JButton custom2, Runnable fin, Setup.SetupBuilder builder) {
        this.builder = builder;
        this.next = next;
    }
}
