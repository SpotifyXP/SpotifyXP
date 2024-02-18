package com.spotifyxp.injector;

import com.spotifyxp.PublicValues;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.swingextension.JFrame;
import com.spotifyxp.utils.FileUtils;
import com.spotifyxp.utils.GraphicalMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Objects;

public class InjectorStore {
    public static JFrame main;
    // (Extension Store)
    //Features:
    // Download and Install Extensions
    // Remove Extensions
    static InjectorAPI api;
    static ArrayList<InjectorAPI.ExtensionSimplified> installedExtensions;
    static int exnum = 0;

    public InjectorStore() {
        api = new InjectorAPI();
        installedExtensions = new ArrayList<>();
        if (!new File(PublicValues.appLocation, "extensionstore.store").exists()) {
            try {
                new File(PublicValues.appLocation, "extensionstore.store").createNewFile();
            } catch (Exception e) {
                ConsoleLogging.Throwable(e);
                GraphicalMessage.openException(e);
            }
        }
        try {
            int iter = 0;
            for (String s : Files.readAllLines(new File(PublicValues.appLocation, "extensionstore.store").toPath())) {
                File f = Objects.requireNonNull(new File(PublicValues.appLocation, "Extensions").listFiles())[iter];
                InjectorAPI.ExtensionSimplified e = new InjectorAPI.ExtensionSimplified(
                        "N/A",
                        "N/A",
                        f.getName().split("-")[0].replace(" ", ""),
                        f.getName().split("-")[1].replace(" ", ""),
                        f.getName().split("-")[2].replace(" ", "").replace(".jar", ""),
                        "N/A",
                        "N/A");
                installedExtensions.add(e);
                exnum++;
                iter++;
            }
        } catch (IOException e) {
            ConsoleLogging.Throwable(e);
            GraphicalMessage.openException(e);
        }
    }

    /**
     * Opens the extension store
     */
    public void open() {
        main = new JFrame(PublicValues.language.translate("extension.title"));
        main.getContentPane().add(new ContentPanel());
        main.setPreferredSize(new Dimension(377, 526));
        main.setVisible(true);
        main.setResizable(false);
        main.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                JOptionPane.showConfirmDialog(com.spotifyxp.panels.ContentPanel.frame, PublicValues.language.translate("ui.settings.pleaserestart"), PublicValues.language.translate("joptionpane.info"), JOptionPane.OK_CANCEL_OPTION);
            }
        });
        main.pack();
    }

    static class ContentPanel extends JPanel {
        final JScrollPane scrollPane;
        final JPanel modulesholder;
        int ycache = 0;

        public ContentPanel() {
            setLayout(null);
            scrollPane = new JScrollPane();
            scrollPane.setBounds(0, 0, 361, 487);
            add(scrollPane);
            modulesholder = new JPanel();
            modulesholder.setSize(361, 487 * exnum);
            scrollPane.setViewportView(modulesholder);
            modulesholder.setLayout(null);
            boolean found = false;
            for(InjectorAPI.APIInjectorRepository repository : api.getRepositories()) {
                for (InjectorAPI.ExtensionSimplified ex : installedExtensions) {
                    addModule(repository, ex);
                }
                for (InjectorAPI.ExtensionSimplified e : api.getExtensions(repository)) {
                    for (InjectorAPI.ExtensionSimplified ex : installedExtensions) {
                        if (e.getName().equals(ex.getName()) && e.getAuthor().equals(ex.getAuthor()) && e.getVersion().equals(ex.getVersion())) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        if (!Objects.equals(e.getMinVersion(), InjectorAPI.compatibleMinVersion)) {
                            exnum++;
                            addModuleNotCompatible(e);
                            continue;
                        }
                        exnum++;
                        addModule(repository, e);
                    }
                    found = false;
                }
            }
        }

        public void addModuleNotCompatible(InjectorAPI.ExtensionSimplified ex) {
            JButton extensioninstallremovebutton = new JButton(PublicValues.language.translate("extension.install.incompatible"));
            extensioninstallremovebutton.setBounds(260, ycache + 41, 105, 23);
            modulesholder.add(extensioninstallremovebutton);
            extensioninstallremovebutton.setForeground(PublicValues.globalFontColor);
            for (InjectorAPI.ExtensionSimplified extension : installedExtensions) {
                if (extension.getIdentifier().equals(ex.getIdentifier())) {
                    extensioninstallremovebutton.setText(PublicValues.language.translate("extension.remove"));
                }
            }
            extensioninstallremovebutton.addActionListener(e -> {
                if(!extensioninstallremovebutton.getText().equals(PublicValues.language.translate("extension.remove"))) {
                    return;
                }
                //Remove extension
                extensioninstallremovebutton.setEnabled(false);
                for (Injector.InjectionEntry entry : PublicValues.injector.injectedJars) {
                    if (entry.filename.equals((ex.getName() + "-" + ex.getAuthor()+ "-" + ex.getVersion() + ".jar").replace(" ", ""))) {
                        try {
                            entry.loader.close();
                        } catch (IOException exc) {
                            ConsoleLogging.Throwable(exc);
                            GraphicalMessage.openException(exc);
                        }
                    }
                }
                if (new File(PublicValues.appLocation + "/Extensions", (ex.getName() + "-" + ex.getAuthor() + "-" + ex.getVersion() + ".jar").replace(" ", "")).delete()) {
                    try {
                        StringBuilder builder = new StringBuilder();
                        for (String s : Files.readAllLines(new File(PublicValues.appLocation, "extensionstore.store").toPath())) {
                            if (!s.replace("\n", "").equals(ex.getName())) {
                                builder.append(s);
                            }
                        }
                        FileWriter w = new FileWriter(new File(PublicValues.appLocation, "extensionstore.store"));
                        w.write(builder.toString());
                        w.close();
                    } catch (Exception e2) {
                        ConsoleLogging.Throwable(e2);
                        GraphicalMessage.openException(e2);
                    }
                    extensioninstallremovebutton.setEnabled(true);
                    extensioninstallremovebutton.setText(PublicValues.language.translate("extension.install.incompatible"));
                }
            });
            JLabel extensiontitle = new JLabel(ex.getName() + " from " + ex.getAuthor() + " - " + ex.getVersion());
            extensiontitle.setBounds(10, ycache + 11, 339, 14);
            modulesholder.add(extensiontitle);
            extensiontitle.setForeground(PublicValues.globalFontColor);
            JTextArea extensiondescription = new JTextArea(ex.getDescription());
            extensiondescription.setBackground(main.getBackground());
            extensiondescription.setBounds(10, ycache + 36, 240, 64);
            modulesholder.add(extensiondescription);
            extensiondescription.setEditable(false);
            extensiondescription.setLineWrap(true);
            extensiondescription.setWrapStyleWord(true);
            extensiondescription.setForeground(PublicValues.globalFontColor);
            ycache += 112;
            JSeparator separator = new JSeparator();
            try {
                if (PublicValues.theme.isLight()) {
                    separator.setBackground(Color.black);
                } else {
                    separator.setBackground(Color.white);
                }
            } catch (Exception e) {
                separator.setBackground(Color.black);
            }
            separator.setSize(361, 2);
            separator.setLocation(0, ycache);
            modulesholder.add(separator);
        }

        public void addModule(InjectorAPI.APIInjectorRepository repository, InjectorAPI.ExtensionSimplified ex) {
            JButton extensioninstallremovebutton = new JButton(PublicValues.language.translate("extension.install"));
            extensioninstallremovebutton.setBounds(260, ycache + 41, 105, 23);
            modulesholder.add(extensioninstallremovebutton);
            extensioninstallremovebutton.setForeground(PublicValues.globalFontColor);
            for (InjectorAPI.ExtensionSimplified extension : installedExtensions) {
                if (extension.getIdentifier().equals(ex.getIdentifier())) {
                    extensioninstallremovebutton.setText(PublicValues.language.translate("extension.remove"));
                }
            }
            extensioninstallremovebutton.addActionListener(e -> {
                if (extensioninstallremovebutton.getText().equals(PublicValues.language.translate("extension.remove"))) {
                    //Remove extension
                    extensioninstallremovebutton.setEnabled(false);
                    for (Injector.InjectionEntry entry : PublicValues.injector.injectedJars) {
                        if (entry.filename.equals((ex.getName() + "-" + ex.getAuthor() + "-" + ex.getVersion() + ".jar").replace(" ", ""))) {
                            try {
                                entry.loader.close();
                            } catch (IOException exc) {
                                ConsoleLogging.Throwable(exc);
                                GraphicalMessage.openException(exc);
                            }
                        }
                    }
                    if (new File(PublicValues.appLocation + "/Extensions", (ex.getName() + "-" + ex.getAuthor() + "-" + ex.getVersion() + ".jar").replace(" ", "")).delete()) {
                        try {
                            StringBuilder builder = new StringBuilder();
                            for (String s : Files.readAllLines(new File(PublicValues.appLocation, "extensionstore.store").toPath())) {
                                if (!s.replace("\n", "").equals(ex.getIdentifier())) {
                                    builder.append(s);
                                }
                            }
                            FileWriter w = new FileWriter(new File(PublicValues.appLocation, "extensionstore.store"));
                            w.write(builder.toString());
                            w.close();
                        } catch (Exception e2) {
                            ConsoleLogging.Throwable(e2);
                            GraphicalMessage.openException(e2);
                        }
                        extensioninstallremovebutton.setEnabled(true);
                        extensioninstallremovebutton.setText(PublicValues.language.translate("extension.install"));
                    }
                } else {
                    //Install extension
                    InjectorAPI.Extension extension = api.getExtensionFromSimplified(ex, repository);
                    try {
                        long size = api.getSizeOfExtension(repository, extension);
                        for(InjectorAPI.ExtensionSimplified dependency : extension.getDependencies()) {
                            api.downloadExtension(extension, repository, filesizeDownloaded -> {
                                if (filesizeDownloaded == size) {
                                    FileUtils.appendToFile(new File(PublicValues.appLocation, "extensionstore.store").getAbsolutePath(), dependency.getIdentifier());
                                    extensioninstallremovebutton.setEnabled(true);
                                    extensioninstallremovebutton.setText(PublicValues.language.translate("extension.remove"));
                                } else {
                                    extensioninstallremovebutton.setEnabled(false);
                                }
                            });
                        }
                        api.downloadExtension(extension, repository, filesizeDownloaded -> {
                            if (filesizeDownloaded == size) {
                                FileUtils.appendToFile(new File(PublicValues.appLocation, "extensionstore.store").getAbsolutePath(), ex.getIdentifier());
                                extensioninstallremovebutton.setEnabled(true);
                                extensioninstallremovebutton.setText(PublicValues.language.translate("extension.remove"));
                            } else {
                                extensioninstallremovebutton.setEnabled(false);
                            }
                        });
                    } catch (IOException exc) {
                        throw new RuntimeException(exc);
                    }
                    GraphicalMessage.pleaseRestart();
                }
            });
            JLabel extensiontitle = new JLabel(ex.getName() + " from " + ex.getAuthor() + " - " + ex.getVersion());
            extensiontitle.setBounds(10, ycache + 11, 339, 14);
            modulesholder.add(extensiontitle);
            extensiontitle.setForeground(PublicValues.globalFontColor);
            JTextArea extensiondescription = new JTextArea(ex.getDescription());
            extensiondescription.setBackground(main.getBackground());
            extensiondescription.setBounds(10, ycache + 36, 240, 64);
            modulesholder.add(extensiondescription);
            extensiondescription.setEditable(false);
            extensiondescription.setLineWrap(true);
            extensiondescription.setWrapStyleWord(true);
            extensiondescription.setForeground(PublicValues.globalFontColor);
            ycache += 112;
            JSeparator separator = new JSeparator();
            try {
                if (PublicValues.theme.isLight()) {
                    separator.setBackground(Color.black);
                } else {
                    separator.setBackground(Color.white);
                }
            } catch (Exception e) {
                separator.setBackground(Color.black);
            }
            separator.setSize(361, 2);
            separator.setLocation(0, ycache);
            modulesholder.add(separator);
        }
    }
}
