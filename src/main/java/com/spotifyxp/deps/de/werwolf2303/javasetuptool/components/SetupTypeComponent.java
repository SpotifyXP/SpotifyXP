package com.spotifyxp.deps.de.werwolf2303.javasetuptool.components;

import com.spotifyxp.deps.de.werwolf2303.javasetuptool.PublicValues;
import com.spotifyxp.deps.de.werwolf2303.javasetuptool.Setup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

public class SetupTypeComponent extends JPanel implements Component {
    //https://i.stack.imgur.com/845qn.jpg
    final Setup setup;
    JButton next;
    JButton prev;
    final ArrayList<Component> typicalcomponents = new ArrayList<>();
    final ArrayList<Component> customcomponents = new ArrayList<>();
    final ArrayList<Component> completecomponents = new ArrayList<>();
    Setup.SetupBuilder builder;
    JButton custom1;
    JButton custom2;
    JButton cancel;
    Runnable fin;
    int current = 0;
    int ycache = 8;
    final JCheckBox typical = new JCheckBox("Typical");
    final JCheckBox complete = new JCheckBox("Complete");
    final JCheckBox custom = new JCheckBox("Custom");

    public SetupTypeComponent(Setup setup) {
        this.setup = setup;
        setLayout(null);
    }

    public void showComplete() {
        add(complete);
        complete.setFont(fontSize(complete.getFont(), 13));
        complete.setLocation(13, ycache);
        ycache += (PublicValues.setup_height - PublicValues.setup_bar_height) / 3 - 7;
        complete.setSize(new Dimension(PublicValues.setup_width / 3, (PublicValues.setup_height - PublicValues.setup_bar_height) / 3 / 2));
        JTextArea area = new JTextArea("All program features will be installed. (Requires the most disk\nspace.)");
        add(area);
        area.setBackground(getBackground());
        area.setLocation(complete.getX() + 110, complete.getY() + 40);
        area.setSize(new Dimension(PublicValues.setup_width - complete.getWidth() + 110, 66));
        complete.addActionListener(e -> {
            resetBoxes(complete);
            PublicValues.INTERNALType = PublicValues.SetupType.COMPLETE;
        });
    }

    @SuppressWarnings("all")
    Font fontSize(Font f, int size) {
        return new Font(f.getName(), Font.BOLD, size);
    }

    public void showTypical() {
        add(typical);
        typical.setFont(fontSize(typical.getFont(), 13));
        typical.setLocation(13, ycache);
        ycache += (PublicValues.setup_height - PublicValues.setup_bar_height) / 3 - 7;
        typical.setSize(new Dimension(PublicValues.setup_width / 3, (PublicValues.setup_height - PublicValues.setup_bar_height) / 3 / 2));
        JTextArea area = new JTextArea("Common program features will be installed.");
        add(area);
        area.setBackground(getBackground());
        area.setLocation(typical.getX() + 110, typical.getY() + 40);
        area.setSize(new Dimension(PublicValues.setup_width - typical.getWidth() + 110, 66));
        typical.addActionListener(e -> {
            resetBoxes(typical);
            PublicValues.INTERNALType = PublicValues.SetupType.TYPICAL;
        });
    }

    public void showCustom() {
        add(custom);
        custom.setLocation(13, ycache);
        ycache += (PublicValues.setup_height - PublicValues.setup_bar_height) / 3 - 7;
        custom.setSize(new Dimension(PublicValues.setup_width / 3, (PublicValues.setup_height - PublicValues.setup_bar_height) / 3 / 2));
        JTextArea area = new JTextArea("Choose which program features you want installed and where they\nwill be installed. Recommended for advanced users.");
        add(area);
        custom.setFont(fontSize(custom.getFont(), 13));
        area.setBackground(getBackground());
        area.setLocation(custom.getX() + 110, custom.getY() + 40);
        area.setSize(new Dimension(PublicValues.setup_width - custom.getWidth() + 110, 66));
        custom.addActionListener(e -> {
            resetBoxes(custom);
            PublicValues.INTERNALType = PublicValues.SetupType.CUSTOM;
        });
    }

    void resetBoxes(JCheckBox box) {
        if(!box.getText().equals(custom.getText())) {
            custom.setSelected(false);
        }
        if(!box.getText().equals(typical.getText())) {
            typical.setSelected(false);
        }
        if(!box.getText().equals(complete.getText())) {
            complete.setSelected(false);
        }
    }

    public void buildCustom(Component... components) {
        customcomponents.addAll(Arrays.asList(components));
    }

    public void buildTypical(Component... components) {
        typicalcomponents.addAll(Arrays.asList(components));
    }

    public void buildComplete(Component... components) {
        completecomponents.addAll(Arrays.asList(components));
    }

    boolean atSetupTypeComponent = true;
    boolean install = false;

    void next() {
        if(!PublicValues.INTERNALBlockNextPrev) {
            return;
        }
        if(!custom.isSelected() && !typical.isSelected() && !complete.isSelected()) {
            return;
        }
        if(atSetupTypeComponent) {
            setVisible(false);
            switch (PublicValues.INTERNALType) {
                case COMPLETE:
                    if(current == completecomponents.size() - 1) {
                        PublicValues.changeinstall.run();
                        install = true;
                    }
                    completecomponents.get(current).giveComponents(this.next, this.prev, this.cancel, this.custom1, this.custom2, this.fin, this.builder);
                    completecomponents.get(current).drawable().setVisible(true);
                    completecomponents.get(current).drawable().repaint();
                    completecomponents.get(current).nowVisible();
                    completecomponents.get(current).init();
                    break;
                case CUSTOM:
                    if(current == customcomponents.size() - 1) {
                        PublicValues.changeinstall.run();
                        install = true;
                    }
                    customcomponents.get(current).giveComponents(this.next, this.prev, this.cancel, this.custom1, this.custom2, this.fin, this.builder);
                    customcomponents.get(current).drawable().setVisible(true);
                    customcomponents.get(current).drawable().repaint();
                    customcomponents.get(current).nowVisible();
                    customcomponents.get(current).init();
                    break;
                case TYPICAL:
                    if(current == typicalcomponents.size() - 1) {
                        PublicValues.changeinstall.run();
                        install = true;
                    }
                    typicalcomponents.get(current).giveComponents(this.next, this.prev, this.cancel, this.custom1, this.custom2, this.fin, this.builder);
                    typicalcomponents.get(current).drawable().setVisible(true);
                    typicalcomponents.get(current).drawable().repaint();
                    typicalcomponents.get(current).nowVisible();
                    typicalcomponents.get(current).init();
                    break;
            }
            atSetupTypeComponent = false;
            return;
        }
        switch (PublicValues.INTERNALType) {
            case COMPLETE:
                if (install) {
                    completecomponents.get(current).onLeave();
                    PublicValues.install.run();
                    return;
                }
                if(completecomponents.size() == current + 2) {
                    install = true;
                    PublicValues.changeinstall.run();
                }
                resetUserButtons();
                completecomponents.get(current).onLeave();
                completecomponents.get(current).drawable().setVisible(false);
                completecomponents.get(current + 1).giveComponents(this.next, this.prev, this.cancel, this.custom1, this.custom2, this.fin, this.builder);
                completecomponents.get(current + 1).drawable().setVisible(true);
                completecomponents.get(current + 1).drawable().repaint();
                completecomponents.get(current + 1).nowVisible();
                completecomponents.get(current + 1).init();
                current++;
                break;
            case CUSTOM:
                if (install) {
                    customcomponents.get(current).onLeave();
                    PublicValues.install.run();
                    return;
                }
                if(customcomponents.size() == current + 2) {
                    install = true;
                    PublicValues.changeinstall.run();
                }
                resetUserButtons();
                customcomponents.get(current).onLeave();
                customcomponents.get(current).drawable().setVisible(false);
                customcomponents.get(current + 1).giveComponents(this.next, this.prev, this.cancel, this.custom1, this.custom2, this.fin, this.builder);
                customcomponents.get(current + 1).drawable().setVisible(true);
                customcomponents.get(current + 1).drawable().repaint();
                customcomponents.get(current + 1).nowVisible();
                customcomponents.get(current + 1).init();
                current++;
                if (current > customcomponents.size()) {
                    PublicValues.changeinstall.run();
                    install = true;
                }
                break;
            case TYPICAL:
                if (install) {
                    typicalcomponents.get(current).onLeave();
                    PublicValues.install.run();
                    return;
                }
                if(typicalcomponents.size() == current + 2) {
                    install = true;
                    PublicValues.changeinstall.run();
                }
                resetUserButtons();
                typicalcomponents.get(current).onLeave();
                typicalcomponents.get(current).drawable().setVisible(false);
                typicalcomponents.get(current + 1).giveComponents(this.next, this.prev, this.cancel, this.custom1, this.custom2, this.fin, this.builder);
                typicalcomponents.get(current + 1).drawable().setVisible(true);
                typicalcomponents.get(current + 1).drawable().repaint();
                typicalcomponents.get(current + 1).nowVisible();
                typicalcomponents.get(current + 1).init();
                current++;
                if (current > typicalcomponents.size()) {
                    PublicValues.changeinstall.run();
                    install = true;
                }
                break;
        }
    }

    void resetUserButtons() {
        custom1.setVisible(false);
        custom2.setVisible(false);
    }

    void previous() {
        if(!PublicValues.INTERNALBlockNextPrev) {
            return;
        }
        if(atSetupTypeComponent) {
            atSetupTypeComponent = false;
            PublicValues.INTERNALBlockNextPrev = false;
            next.removeActionListener(nextlistener);
            prev.removeActionListener(prevlistener);
            install = false;
            return;
        }
        PublicValues.resetNext.run();
        resetUserButtons();
        switch (PublicValues.INTERNALType) {
            case COMPLETE:
                if (current == 0) {
                    atSetupTypeComponent = true;
                    completecomponents.get(current).onLeave();
                    completecomponents.get(current).drawable().setVisible(false);
                    setVisible(true);
                    install = false;
                } else {
                    if (current + 1 > completecomponents.size()) {
                        PublicValues.resetNext.run();
                    }
                    completecomponents.get(current).onLeave();
                    completecomponents.get(current).drawable().setVisible(false);
                    completecomponents.get(current - 1).giveComponents(this.next, this.prev, this.cancel, this.custom1, this.custom2, this.fin, this.builder);
                    completecomponents.get(current - 1).drawable().setVisible(true);
                    completecomponents.get(current - 1).drawable().repaint();
                    completecomponents.get(current - 1).nowVisible();
                    completecomponents.get(current - 1).init();
                    current--;
                    if (install) {
                        PublicValues.resetNext.run();
                        install = false;
                    }
                }
                break;
            case CUSTOM:
                if (current == 0) {
                    atSetupTypeComponent = true;
                    customcomponents.get(current).onLeave();
                    customcomponents.get(current).drawable().setVisible(false);
                    setVisible(true);
                    install = false;
                } else {
                    if (current + 1 > customcomponents.size()) {
                        PublicValues.resetNext.run();
                        install = false;
                    }
                    customcomponents.get(current).onLeave();
                    customcomponents.get(current).drawable().setVisible(false);
                    customcomponents.get(current - 1).giveComponents(this.next, this.prev, this.cancel, this.custom1, this.custom2, this.fin, this.builder);
                    customcomponents.get(current - 1).drawable().setVisible(true);
                    customcomponents.get(current - 1).drawable().repaint();
                    customcomponents.get(current - 1).nowVisible();
                    customcomponents.get(current - 1).init();
                    current--;
                    if (install) {
                        PublicValues.resetNext.run();
                        install = false;
                    }
                }
                break;
            case TYPICAL:
                if (current == 0) {
                    atSetupTypeComponent = true;
                    typicalcomponents.get(current).onLeave();
                    typicalcomponents.get(current).drawable().setVisible(false);
                    setVisible(true);
                    install = false;
                } else {
                    if (current + 1 > typicalcomponents.size()) {
                        PublicValues.resetNext.run();
                        install = false;
                    }
                    typicalcomponents.get(current).onLeave();
                    typicalcomponents.get(current).drawable().setVisible(false);
                    typicalcomponents.get(current - 1).giveComponents(this.next, this.prev, this.cancel, this.custom1, this.custom2, this.fin, this.builder);
                    typicalcomponents.get(current - 1).drawable().setVisible(true);
                    typicalcomponents.get(current - 1).drawable().repaint();
                    typicalcomponents.get(current - 1).nowVisible();
                    typicalcomponents.get(current - 1).init();
                    current--;
                    if (install) {
                        PublicValues.resetNext.run();
                        install = false;
                    }
                }
                break;
        }
    }

    @Override
    public String getName() {
        return "SetupTypeComponent";
    }

    @Override
    public JPanel drawable() {
        return this;
    }

    final ActionListener nextlistener = e -> next();
    final ActionListener prevlistener = e -> previous();

    boolean first = true;

    @Override
    public void init() {
        if(first) {
            for(Component c : customcomponents) {
                PublicValues.INTERNALContentManager.add(c.drawable(), BorderLayout.CENTER);
                c.drawable().setVisible(false);
            }
            for(Component c : typicalcomponents) {
                PublicValues.INTERNALContentManager.add(c.drawable(), BorderLayout.CENTER);
                c.drawable().setVisible(false);
            }
            for(Component c : completecomponents) {
                PublicValues.INTERNALContentManager.add(c.drawable(), BorderLayout.CENTER);
                c.drawable().setVisible(false);
            }
            first = false;
        }
        if(next == null) {
            return;
        }
        setPreferredSize(new Dimension(PublicValues.setup_width, PublicValues.setup_height - PublicValues.setup_bar_height));
        next.addActionListener(nextlistener);
        prev.addActionListener(prevlistener);
    }

    @Override
    public void nowVisible() {
        atSetupTypeComponent = true;
        PublicValues.INTERNALBlockNextPrev = true;
    }

    @Override
    public void onLeave() {

    }

    @Override
    public void giveComponents(JButton next, JButton previous, JButton cancel, JButton custom1, JButton custom2, Runnable fin, Setup.SetupBuilder builder) {
        this.next = next;
        this.prev = previous;
        this.cancel = cancel;
        this.custom1 = custom1;
        this.custom2 = custom2;
        this.fin = fin;
        this.builder = builder;
    }
}
