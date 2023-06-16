package com.spotifyxp.injector;

import com.spotifyxp.PublicValues;
import com.spotifyxp.exception.ExceptionDialog;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.utils.FileUtils;
import com.spotifyxp.utils.GraphicalMessage;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class InjectorStore {
    // (Extension Store)

    //Features:

    // Download and Install Extensions
    // Remove Extensions
    static InjectorAPI api;
    static ArrayList<InjectorAPI.Extension> installedExtensions;

    static int exnum = 0;

    public InjectorStore() {
        api = new InjectorAPI();
        api.parseExtensions();
        installedExtensions = new ArrayList<>();
        if(!new File(PublicValues.appLocation, "extensionstore.store").exists()) {
            try {
                new File(PublicValues.appLocation, "extensionstore.store").createNewFile();
            }catch (Exception e) {
                ConsoleLogging.Throwable(e);
                ExceptionDialog.open(e);
            }
        }
        try {
            int iter = 0;
            for(String s : Files.readAllLines(new File(PublicValues.appLocation, "extensionstore.store").toPath())) {
                File f = Objects.requireNonNull(new File(PublicValues.appLocation, "Extensions").listFiles())[iter];
                InjectorAPI.Extension e = new InjectorAPI.Extension();
                e.location = "N/A";
                e.identifier = s;
                e.name = f.getName().split("-")[0].replace(" ", "");
                e.author = f.getName().split("-")[1].replace(" ", "");
                e.version = f.getName().split("-")[2].replace(" ", "").replace(".jar", "");
                e.description = api.getExtension(e.identifier).description;
                installedExtensions.add(e);
                exnum++;
                iter++;
            }
        } catch (IOException e) {
            ConsoleLogging.Throwable(e);
            ExceptionDialog.open(e);
        }
    }

    static class ContentPanel extends JPanel {
        JScrollPane scrollPane;
        JPanel modulesholder;
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

            for(InjectorAPI.Extension ex : installedExtensions) {
                addModule(ex);
            }

            for(InjectorAPI.Extension e : api.extensions) {
                for(InjectorAPI.Extension ex : installedExtensions) {
                    if(e.identifier.equals(ex.identifier)) {
                        found = true;
                        break;
                    }
                }
                if(!found) {
                    exnum++;
                    addModule(e);
                }
                found = false;
            }
        }

        int ycache = 0;

        public void addModule(InjectorAPI.Extension ex) {

            JButton extensioninstallremovebutton = new JButton("Install");
            extensioninstallremovebutton.setBounds(260, ycache + 57, 89, 23);
            modulesholder.add(extensioninstallremovebutton);

            extensioninstallremovebutton.setForeground(PublicValues.globalFontColor);

            for(InjectorAPI.Extension extension : installedExtensions) {
                if(extension.name.equals(ex.name)) {
                    extensioninstallremovebutton.setText("Remove");
                }
            }

            extensioninstallremovebutton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(extensioninstallremovebutton.getText().equals("Remove")) {
                        //Remove extension
                        extensioninstallremovebutton.setEnabled(false);
                        for(Injector.InjectionEntry entry : PublicValues.injector.injectedJars) {
                            if(entry.filename.equals((ex.name + "-" + ex.author + "-" + ex.version + ".jar").replace(" ", ""))) {
                                try {
                                    entry.loader.close();
                                } catch (IOException exc) {
                                    ConsoleLogging.Throwable(exc);
                                    ExceptionDialog.open(exc);
                                }
                            }
                        }
                        if(new File(PublicValues.appLocation + "/Extensions", (ex.name + "-" + ex.author + "-" + ex.version + ".jar").replace(" ", "")).delete()) {
                            try {
                                StringBuilder builder = new StringBuilder();
                                for(String s : Files.readAllLines(new File(PublicValues.appLocation, "extensionstore.store").toPath())) {
                                    if(!s.replace("\n", "").equals(ex.identifier)) {
                                        builder.append(s);
                                    }
                                }
                                FileWriter w = new FileWriter(new File(PublicValues.appLocation, "extensionstore.store"));
                                w.write(builder.toString());
                                w.close();
                            }catch (Exception e2) {
                                ConsoleLogging.Throwable(e2);
                                ExceptionDialog.open(e2);
                            }
                            extensioninstallremovebutton.setEnabled(true);
                            extensioninstallremovebutton.setText("Install");
                        }
                    }else{
                        //Install extension
                        long size = api.getExtensionSize(ex.location);
                        api.downloadExtension(ex.location, new InjectorAPI.ProgressRunnable() {
                            @Override
                            public void run(long filesizeDownloaded) {
                                if(filesizeDownloaded == size) {
                                    FileUtils.appendToFile(new File(PublicValues.appLocation, "extensionstore.store").getAbsolutePath(), ex.identifier);
                                    extensioninstallremovebutton.setEnabled(true);
                                    extensioninstallremovebutton.setText("Remove");
                                }else{
                                    extensioninstallremovebutton.setEnabled(false);
                                }
                            }
                        });
                        PublicValues.injector.loadJarAt(PublicValues.appLocation + "/Extensions/" + (ex.name + "-" + ex.author + "-" + ex.version + ".jar").replace(" ", ""));
                    }
                }
            });

            JLabel extensiontitle = new JLabel(ex.name + " from " + ex.author + " - " + ex.version);
            extensiontitle.setBounds(10, ycache + 11, 339, 14);
            modulesholder.add(extensiontitle);

            extensiontitle.setForeground(PublicValues.globalFontColor);

            JTextArea extensiondescription = new JTextArea(ex.description);
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
            }catch (Exception e) {
                separator.setBackground(Color.black);
            }
            separator.setSize(361, 2);
            separator.setLocation(0, ycache);
            modulesholder.add(separator);
        }
    }

    public static JFrame main;

    public void open() {
        main = new JFrame("SpotifyXP - Extension Store");
        main.getContentPane().add(new ContentPanel());
        main.setPreferredSize(new Dimension(377, 526));
        main.setVisible(true);
        main.setResizable(false);
        main.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                JOptionPane.showConfirmDialog(null, PublicValues.language.translate("ui.settings.pleaserestart"), PublicValues.language.translate("joptionpane.info"), JOptionPane.OK_CANCEL_OPTION);
            }
        });
        main.pack();
    }
}
