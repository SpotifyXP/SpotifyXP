package com.spotifyxp.deps.de.werwolf2303.javasetuptool.components;

import com.spotifyxp.deps.de.werwolf2303.javasetuptool.PublicValues;
import com.spotifyxp.deps.de.werwolf2303.javasetuptool.Setup;
import com.spotifyxp.deps.de.werwolf2303.javasetuptool.uninstaller.Uninstaller;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class InstallProgressComponent extends JPanel implements Component {
    final ArrayList<FileOperation> fileOperations = new ArrayList<>();
    final JProgressBar progress;
    final JTextArea operationlog;
    final JLabel operationpath;
    final JScrollPane pane;
    static FeatureSelectionComponent component;
    Setup.SetupBuilder builder;
    Uninstaller uninstaller;
    final ArrayList<String> files = new ArrayList<>();
    final ArrayList<String> folders = new ArrayList<>();
    boolean bu = false;

    public InstallProgressComponent() {
        setLayout(null);
        progress = new JProgressBar();
        add(progress);
        pane = new JScrollPane();
        operationlog = new JTextArea() {
            @Override
            public void append(String str) {
                super.append(str);
                pane.getVerticalScrollBar().setValue(pane.getVerticalScrollBar().getMaximum());
            }
        };
        operationlog.setEditable(false);
        pane.setViewportView(operationlog);
        add(pane);
        operationpath = new JLabel();
        progress.setMinimum(0);
        progress.setValue(0);
        add(operationpath);
    }

    public InstallProgressComponent(FeatureSelectionComponent component) {
        setLayout(null);
        progress = new JProgressBar();
        add(progress);
        pane = new JScrollPane();
        operationlog = new JTextArea() {
            @Override
            public void append(String str) {
                super.append(str);
                pane.getVerticalScrollBar().setValue(pane.getVerticalScrollBar().getMaximum());
            }
        };
        operationlog.setEditable(false);
        pane.setViewportView(operationlog);
        add(pane);
        operationpath = new JLabel();
        progress.setMinimum(0);
        progress.setValue(0);
        add(operationpath);
        InstallProgressComponent.component = component;
    }

    public static class FileOperationBuilder {
        private final FileOperation internal = new FileOperation();

        public FileOperationBuilder setType(FileOperationTypes type) {
            internal.operationType = type;
            return this;
        }

        public FileOperationBuilder setDownloadURL(String url) {
            if (internal.operationType != FileOperationTypes.DOWNLOAD) {
                throw new UnsupportedOperationException("Download only available when using the DOWNLOAD type");
            }
            internal.url = url;
            return this;
        }

        public FileOperationBuilder setForFeature(FeatureSelectionComponent.Feature feature) {
            if (component == null) {
                throw new UnsupportedOperationException("setForFeature is only supported with the combination of the FeatureSelectionComponent");
            }
            internal.feature = feature;
            return this;
        }

        public FileOperationBuilder setFrom(InputStream stream) {
            internal.fromStream = stream;
            return this;
        }

        public FileOperationBuilder setFrom(String from) {
            internal.from = from;
            return this;
        }

        public FileOperationBuilder setTo(String to) {
            internal.to = to;
            return this;
        }

        public FileOperationBuilder setCustom(Runnable run) {
            internal.customCode = run;
            return this;
        }
    }

    public JPanel drawable() {
        return this;
    }

    public void addFileOperation(FileOperation operation) {
        fileOperations.add(operation);
    }

    public void addFileOperation(FileOperationBuilder builder) {
        fileOperations.add(builder.internal);
    }

    boolean failState = false;

    void doOperations() {
        operationpath.setText("Penis Penis Penis Penis");
        progress.setMaximum(fileOperations.size());
        for (FileOperation operation : fileOperations) {
            switch (operation.operationType) {
                case COPY:
                    if (operation.feature != null) {
                        if (!component.getFeatures().contains(operation.feature)) {
                            break;
                        }
                    }
                    try {
                        operationpath.setText(new File(operation.to).getAbsolutePath());
                        operationlog.append("Copy from=>'" + operation.from + "' to=>'" + operation.to + "'\n");
                        Files.copy(Paths.get(new File(operation.from).getAbsolutePath()), Paths.get(new File(operation.to).getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
                        files.add(operation.to);
                        operation.succeeded = true;
                    } catch (IOException e) {
                        operation.succeeded = false;
                    }
                    break;
                case MOVE:
                    if (operation.feature != null) {
                        if (!component.getFeatures().contains(operation.feature)) {
                            break;
                        }
                    }
                    try {
                        operationpath.setText(new File(operation.to).getAbsolutePath());
                        operationlog.append("Moving from=>'" + operation.from + "' to=>'" + operation.to + "'\n");
                        Files.move(Paths.get(new File(operation.from).getAbsolutePath()), Paths.get(new File(operation.to).getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
                        files.add(operation.to);
                        operation.succeeded = true;
                    } catch (IOException e) {
                        operation.succeeded = false;
                    }
                    break;
                case CREATEFILE:
                    if (operation.feature != null) {
                        if (!component.getFeatures().contains(operation.feature)) {
                            break;
                        }
                    }
                    try {
                        operationpath.setText(new File(operation.from).getAbsolutePath());
                        operationlog.append("Creating file=>'" + operation.from + "'\n");
                        if(new File(operation.from).exists()) { files.add(operation.from); operation.succeeded = true; break; }
                        operation.succeeded = new File(operation.from).createNewFile();
                        files.add(operation.from);
                    } catch (IOException e) {
                        operation.succeeded = false;
                    }
                    break;
                case CREATEDIR:
                    if (operation.feature != null) {
                        if (!component.getFeatures().contains(operation.feature)) {
                            break;
                        }
                    }
                    operationpath.setText(new File(operation.from).getAbsolutePath());
                    operationlog.append("Creating directory=>'" + operation.from + "'\n");
                    if(new File(operation.from).exists()) { files.add(operation.from); operation.succeeded = true; break; }
                    operation.succeeded = new File(operation.from).mkdir();
                    folders.add(operation.from);
                    break;
                case DELETE:
                    if (operation.feature != null) {
                        if (!component.getFeatures().contains(operation.feature)) {
                            break;
                        }
                    }
                    operationpath.setText(new File(operation.from).getAbsolutePath());
                    operationlog.append("Deleting=>'" + operation.from + "'\n");
                    operation.succeeded = new File(operation.from).delete();
                    if(new File(operation.from).isDirectory()) {
                        folders.add(operation.from);
                    }else{
                        files.add(operation.from);
                    }
                    break;
                case COPYSTREAM:
                    if (operation.feature != null) {
                        if (!component.getFeatures().contains(operation.feature)) {
                            break;
                        }
                    }
                    operationpath.setText(new File(operation.to).getAbsolutePath());
                    operationlog.append("Copying stream to=>" + operation.to + "\n");
                    try {
                        Files.copy(operation.fromStream, Paths.get(new File(operation.to).getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
                        files.add(operation.to);
                        operation.succeeded = true;
                    } catch (IOException e) {
                        operation.succeeded = false;
                    }
                    break;
                case CUSTOM:
                    if (operation.feature != null) {
                        if (!component.getFeatures().contains(operation.feature)) {
                            break;
                        }
                    }
                    operationpath.setText("Executing custom Java");
                    operationlog.append("Executing custom java\n");
                    operation.customCode.run();
                    operation.succeeded = true;
                    break;
                case DOWNLOAD:
                    if (operation.feature != null) {
                        if (!component.getFeatures().contains(operation.feature)) {
                            break;
                        }
                    }
                    operationpath.setText("Downloading " + operation.url);
                    operationlog.append("Downloading File from=>" + operation.url + "\n");
                    try {
                        URL url = new URL(operation.url);
                        HttpURLConnection httpConnection = (HttpURLConnection) (url.openConnection());
                        long completeFileSize = httpConnection.getContentLength();
                        BufferedInputStream in = new BufferedInputStream(httpConnection.getInputStream());
                        FileOutputStream fos = new FileOutputStream(
                                operation.to);
                        BufferedOutputStream bout = new BufferedOutputStream(
                                fos, 1024);
                        byte[] data = new byte[1024];
                        long downloadedFileSize = 0;
                        int x;
                        float last = 0;
                        while ((x = in.read(data, 0, 1024)) >= 0) {
                            downloadedFileSize += x;
                            float percentage = ((float) downloadedFileSize) / completeFileSize * 100;
                            if(!String.format("%.0f", percentage).equals(String.format("%.0f", last))) {
                                operationlog.append("Downloading File from=>" + operation.url + " Progress=>" + String.format("%.0f", percentage) + "%\n");
                            }
                            last = percentage;
                            bout.write(data, 0, x);
                        }
                        bout.close();
                        in.close();
                        files.add(operation.to);
                        operation.succeeded = true;
                    } catch (IOException e) {
                        operation.succeeded = false;
                    }
                    break;
            }
            if (!operation.succeeded) {
                failState = true;
                break;
            }
            progress.setValue(progress.getValue() + 1);
        }
        if (failState) {
            progress.setForeground(Color.RED);
            operationlog.append("Installation failed!\n");
            setBackground(Color.red);
        } else {
            if(bu) {
                try {
                    uninstaller.buildUninstaller(files, folders, builder.progname, builder.progversion, builder.uxmlat);
                } catch (ParserConfigurationException e) {
                    operationlog.append("Failed building uninstaller\n");
                    throw new RuntimeException(e);
                }
            }
            operationpath.setText("Install Finished");
            operationsFinished();
        }
    }

    public void init() {
        if(builder.buxml) {
            bu = true;
            uninstaller = new Uninstaller();
        }
        setPreferredSize(new Dimension(PublicValues.setup_width, PublicValues.setup_height - PublicValues.setup_bar_height));
        progress.setBounds(3, 46, PublicValues.setup_width - 6, 25);
        operationpath.setBounds(6, 24, PublicValues.setup_width - 6, 16);
        pane.setBounds(6, 96, PublicValues.setup_width - 6, 185);
    }

    public void nowVisible() {
        Thread t = new Thread(this::doOperations);
        t.start();
    }

    public void onLeave() {

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
        this.builder = builder;
    }

    void operationsFinished() {
        this.next.setText("Next >");
        this.next.setVisible(true);
        for (ActionListener l : this.next.getActionListeners()) {
            this.next.removeActionListener(l);
        }
        this.next.addActionListener(e -> fin.run());
    }

    public enum FileOperationTypes {
        CREATEFILE,
        CREATEDIR,
        MOVE,
        COPY,
        COPYSTREAM,
        DELETE,
        CUSTOM,
        DOWNLOAD
    }

    public static class FileOperation {
        String from = "";
        InputStream fromStream;
        String to = "";
        boolean succeeded = false;
        Runnable customCode;
        String url = "";
        FileOperationTypes operationType;
        FeatureSelectionComponent.Feature feature = null;
    }
}
