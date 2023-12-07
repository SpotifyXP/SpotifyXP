package com.spotifyxp.deps.de.werwolf2303.javasetuptool;

import com.spotifyxp.deps.de.werwolf2303.javasetuptool.components.Component;
import com.spotifyxp.deps.de.werwolf2303.javasetuptool.components.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

@SuppressWarnings("ClassEscapesDefinedScope")
public class Setup {
    final Setup setup = this;
    public static SetupBuilder currentBuilder;
    @SuppressWarnings("ClassEscapesDefinedScope")
    public static class SetupBuilder {
        final SetupBuilder builder = this;
        public String brandname = "";
        public String brandicon = "";
        public String title = "";
        public String progname = "";
        public String progversion = "";
        public Runnable finish;
        public String uxmlat = "";
        public boolean buxml = false;
        public InputStream imageStream = null;
        InstallProgressComponent installProgressComponent = null;
        public final ArrayList<Component> components = new ArrayList<>();
        public SetupBuilder setBrandName(String name) {
            this.brandname = name;
            return this;
        }

        public SetupBuilder setInstallComponent(InstallProgressComponent component) {
            this.installProgressComponent = component;
            return this;
        }

        public SetupBuilder setBrandIcon(String resourcePath) {
            this.brandicon = resourcePath;
            return this;
        }

        public SetupBuilder setProgramImage(InputStream stream) {
            this.imageStream = stream;
            return this;
        }

        public SetupBuilder autoBuildUninstallerXML(String at) {
            buxml = true;
            uxmlat = at;
            return this;
        }

        public SetupBuilder setProgramName(String name) {
            this.progname = name;
            return this;
        }

        public SetupBuilder setOnFinish(Runnable runnable) {
            this.finish = runnable;
            return this;
        }

        public SetupBuilder setProgramVersion(String version) {
            this.progversion = version;
            return this;
        }

        public SetupBuilder setLanguage(String languageCode) {

            return this;
        }

        public SetupBuilder addComponent(Component component) {
            components.add(component);
            return this;
        }

        public Build build() {
            this.title = this.progname + " - " + this.progversion;
            return new Build();
        }

        private class Build {
            public SetupBuilder getSetupBuilder() {
                return builder;
            }
            public Build build() {
                return this;
            }
        }
    }

    private class SetupFrame extends JPanel {
        final ContentManager manager = new ContentManager();
        final JFrame frame;
        public SetupFrame() {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }catch (UnsupportedLookAndFeelException | InstantiationException | IllegalAccessException |
                    ClassNotFoundException ignored) {
            }
            frame = new JFrame(currentBuilder.title);
            frame.getContentPane().add(manager);
            frame.setPreferredSize(new Dimension(PublicValues.setup_width, PublicValues.setup_height));
        }

        public void open() {
            if(com.spotifyxp.PublicValues.isMacOS) {
                frame.setResizable(false);
            }
            frame.setVisible(true);
            frame.pack();
            for(Component component : currentBuilder.components) {
                component.giveComponents(null, null, null, null, null, null, null);
                component.init();
            }
            manager.init();
        }

        public void close() {
            frame.dispose();
        }

        private class ContentManager extends JPanel {
            int at = 0;
            final JPanel navigation;
            final JButton nextinstall;
            final JButton back;
            final JButton cancel;
            final WelcomeComponent welcomeComponent;
            final FinishComponent component;
            final JPanel content = new JPanel();
            final JSeparator separator;
            final JPanel usercontrolablebuttons;
            final JButton custom1;
            final JButton custom2;
            Runnable onExit = () -> System.exit(0);
            final Runnable fin = new Runnable() {
                public void run() {
                    currentBuilder.installProgressComponent.setVisible(false);
                    component.nowVisible();
                    component.setVisible(true);
                    cancel.setVisible(true);
                    nextinstall.setVisible(false);
                    cancel.setText("Finish");
                    for(ActionListener l : cancel.getActionListeners()) {
                        cancel.removeActionListener(l);
                    }
                    cancel.addActionListener(e -> {
                        frame.dispose();
                        onExit.run();
                    });
                }
            };


            public ContentManager() {
                PublicValues.INTERNALContentManager = content;
                navigation = new JPanel();
                navigation.setLayout(null);
                add(content);

                setLayout(null);
                custom1 = new JButton("Custom 1");
                custom2 = new JButton("Custom 2");

                custom1.setVisible(false);
                custom2.setVisible(false);

                separator = new JSeparator();
                separator.setOrientation(SwingConstants.HORIZONTAL);

                usercontrolablebuttons = new JPanel();

                cancel = new JButton("Cancel");
                nextinstall = new JButton("Next >");
                back = new JButton("< Back");
                cancel.addActionListener(e -> System.exit(0));
                nextinstall.addActionListener(e -> next());
                back.addActionListener(e -> previous());
                back.setVisible(false);
                navigation.add(separator);
                navigation.add(back);
                navigation.add(nextinstall);
                navigation.add(cancel);
                for(int i = 0; i < currentBuilder.components.size(); i++) {
                    content.add(currentBuilder.components.get(i).drawable(), BorderLayout.CENTER);
                    currentBuilder.components.get(i).drawable().setVisible(false);
                }
                if(getProgramImage() != null) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    try {
                        org.apache.commons.io.IOUtils.copy(getProgramImage(), baos);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    byte[] bytes = baos.toByteArray();
                    welcomeComponent = new WelcomeComponent(setup);
                    welcomeComponent.setImage(new ByteArrayInputStream(bytes));
                    welcomeComponent.nowVisible();
                    component = new FinishComponent(setup);
                    component.setImage(new ByteArrayInputStream(bytes));
                    component.setVisible(false);
                }else{
                    welcomeComponent = new WelcomeComponent(setup);
                    welcomeComponent.nowVisible();
                    component = new FinishComponent(setup);
                    component.setVisible(false);
                }
                currentBuilder.components.add(0, new PrivateComponentAdapter(welcomeComponent));
                content.add(welcomeComponent, BorderLayout.CENTER);
                content.add(component, BorderLayout.CENTER);
                if(currentBuilder.installProgressComponent != null) {
                    content.add(currentBuilder.installProgressComponent.drawable(), BorderLayout.CENTER);
                    currentBuilder.installProgressComponent.setVisible(false);
                }
                navigation.add(usercontrolablebuttons);
                usercontrolablebuttons.add(custom1);
                usercontrolablebuttons.add(custom2);
                add(navigation);
                PublicValues.install = this::install;
                PublicValues.resetNext = this::setNormalButtonsVisible;
                PublicValues.changeinstall = () -> nextinstall.setText("Install");
            }

            void setInstallVisible() {
                nextinstall.setText("Install");
            }

            void setNormalButtonsVisible() {
                nextinstall.setText("Next >");
            }

            void internalInstall() {
                back.setVisible(false);
                nextinstall.setVisible(false);
                cancel.setVisible(false);
                custom1.setVisible(false);
                custom2.setVisible(false);
                currentBuilder.components.get(at).drawable().setVisible(false);
                currentBuilder.installProgressComponent.giveComponents(nextinstall, back, cancel, custom1, custom2, fin, currentBuilder);
                currentBuilder.installProgressComponent.setVisible(true);
                currentBuilder.installProgressComponent.init();
                currentBuilder.installProgressComponent.nowVisible();
            }

            void install() {
                internalInstall();
            }

            void resetUserButtons() {
                custom1.setVisible(false);
                custom2.setVisible(false);
            }

            void next() {
                if(PublicValues.INTERNALBlockNextPrev) {
                    return;
                }
                if(nextinstall.getText().equals("Install")) {
                    currentBuilder.components.get(at).onLeave();
                    install();
                    return;
                }
                resetUserButtons();
                currentBuilder.components.get(at).onLeave();
                currentBuilder.components.get(at).drawable().setVisible(false);
                currentBuilder.components.get(at + 1).giveComponents(nextinstall, back, cancel, custom1, custom2, fin, currentBuilder);
                currentBuilder.components.get(at + 1).drawable().setVisible(true);
                currentBuilder.components.get(at + 1).drawable().repaint();
                currentBuilder.components.get(at + 1).nowVisible();
                currentBuilder.components.get(at + 1).init();
                at++;
                if(at != 0) {
                    back.setVisible(true);
                }
                if(!(currentBuilder.components.get(at) instanceof SetupTypeComponent)) {
                    if (at + 2 > currentBuilder.components.size()) {
                        setInstallVisible();
                    }
                }
            }

            void previous() {
                if(PublicValues.INTERNALBlockNextPrev) {
                    return;
                }
                if(at + 1 > currentBuilder.components.size()) {
                    setNormalButtonsVisible();
                }
                currentBuilder.components.get(at).onLeave();
                currentBuilder.components.get(at).drawable().setVisible(false);
                currentBuilder.components.get(at - 1).giveComponents(nextinstall, back, cancel, custom1, custom2, fin, currentBuilder);
                currentBuilder.components.get(at - 1).drawable().setVisible(true);
                currentBuilder.components.get(at - 1).drawable().repaint();
                currentBuilder.components.get(at - 1).nowVisible();
                currentBuilder.components.get(at - 1).init();
                at--;
                if(nextinstall.getText().equals("Install")) {
                    nextinstall.setText("Next >");
                }
                if(at == 0) {
                    back.setVisible(false);
                }
            }

            void init() {
                separator.setBounds(0, -1, PublicValues.setup_width, 12);
                content.setBounds(0, 0, PublicValues.setup_width, PublicValues.setup_height - PublicValues.setup_bar_height);
                navigation.setBounds(0, PublicValues.setup_height - PublicValues.setup_bar_height, PublicValues.setup_width, PublicValues.setup_bar_height);
                back.setBounds(PublicValues.setup_width - 10 - 351, navigation.getHeight() / 2 - PublicValues.button_height + 9, PublicValues.button_width, PublicValues.button_height);
                nextinstall.setBounds(PublicValues.setup_width - 10 - 234, navigation.getHeight() / 2 - PublicValues.button_height + 9, PublicValues.button_width, PublicValues.button_height);
                cancel.setBounds(PublicValues.setup_width - 10 - 117, navigation.getHeight() / 2 - PublicValues.button_height + 9, PublicValues.button_width, PublicValues.button_height);
                usercontrolablebuttons.setBounds(0, PublicValues.setup_bar_height / 2 - 25, PublicValues.setup_width / 2 - 40, PublicValues.setup_bar_height / 2);
                custom1.setBounds(5, usercontrolablebuttons.getHeight() / 2 - PublicValues.button_height, PublicValues.button_width, PublicValues.button_height);
                custom2.setBounds(PublicValues.button_width + 5, usercontrolablebuttons.getHeight() / 2 - PublicValues.button_height, PublicValues.button_width, PublicValues.button_height);
                welcomeComponent.init();
                component.init();
                if(currentBuilder.finish != null) {
                    onExit = currentBuilder.finish;
                }
            }
        }

        private class PrivateComponentAdapter implements Component {
            final PrivateComponent component;

            public PrivateComponentAdapter(PrivateComponent component) {
                this.component = component;
            }

            public String getName() {
                return this.component.getName();
            }

            public JPanel drawable() {
                return this.component.drawable();
            }

            public void init() {
                this.component.init();
            }

            public void nowVisible() {
                this.component.nowVisible();
            }

            public void onLeave() {
                this.component.onLeave();
            }

            public void giveComponents(JButton next, JButton previous, JButton cancel, JButton custom1, JButton custom2, Runnable fin, SetupBuilder builder) {
                this.component.giveComponents(next, previous, cancel, custom1, custom2, fin, currentBuilder);
            }
        }
    }

    public String getBrandName() {
        return currentBuilder.brandname;
    }

    public String getBrandIcon() {
        return currentBuilder.brandicon;
    }

    public String getProgramName() {
        return currentBuilder.progname;
    }

    public String getProgramVersion() {
        return currentBuilder.progversion;
    }

    public InputStream getProgramImage() {
        return currentBuilder.imageStream;
    }

    public void open(SetupBuilder.Build builder) {
        currentBuilder = builder.getSetupBuilder();
        new SetupFrame().open();
    }
}
