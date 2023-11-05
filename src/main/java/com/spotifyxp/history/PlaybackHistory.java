package com.spotifyxp.history;

import com.spotifyxp.PublicValues;
import com.spotifyxp.deps.se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.Track;
import com.spotifyxp.graphics.Graphics;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.panels.ContentPanel;
import com.spotifyxp.panels.HomePanel;
import com.spotifyxp.swingextension.JFrame2;
import com.spotifyxp.swingextension.URITree;
import com.spotifyxp.threading.DefThread;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.hc.core5.http.ParseException;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class PlaybackHistory extends JFrame2 {
    public static class SongEntry {
        public String songURI;
        public String songName;
        public String artistName;
        public String artistURI;
        public String albumName;
        public String albumURI;
    }

    public static class TreeEntry {
        public String name;
        public String uri;
        public DefaultMutableTreeNode addedTo;

        public TreeEntry(String name, String uri, DefaultMutableTreeNode addedTo) {
            this.name = name;
            this.uri = uri;
            this.addedTo = addedTo;
        }
    }


    private static String filePath;
    private static String counterFilePath;
    private static URITree tree;
    private static DefaultMutableTreeNode root;
    private static final ArrayList<PlaybackHistory.TreeEntry> addedArtists = new ArrayList<>();
    private static final ArrayList<PlaybackHistory.TreeEntry> addedAlbums = new ArrayList<>();
    private static int offset = 0;

    public PlaybackHistory() {
        filePath = new File(PublicValues.fileslocation, "playback.history").getAbsolutePath();
        counterFilePath = new File(PublicValues.fileslocation, "playback.history.counter").getAbsolutePath();

        if(!new File(filePath).exists()) {
            try {
                new File(filePath).createNewFile();
                FileWriter writer = new FileWriter(filePath, true);
                writer.write("// This file is for the Playback history feature! Dont edit this file unless you know what you are doing");
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if(!new File(counterFilePath).exists()) {
            try {
                new File(counterFilePath).createNewFile();
                FileWriter writer = new FileWriter(counterFilePath, false);
                writer.write("C" + 0);
                writer.close();
            }catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        setPreferredSize(new Dimension(300, 400));
        setTitle("SpotifyXP - Playback History"); //ToDo: Translate

        root = new DefaultMutableTreeNode("Playback History"); //ToDo: Translate
        tree = new URITree(root);

        JScrollPane pane = new JScrollPane(tree);
        add(pane, BorderLayout.CENTER);



        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2) {
                    try {
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionModel().getSelectionPath().getLastPathComponent();
                        URITree.TreeNodeData data = ((URITree.TreeNodeData) node.getUserObject());
                        switch (data.getNodetype()) {
                            case ARTIST:
                                ContentPanel.showArtistPanel(data.getURI());
                                break;
                            case ALBUM:
                                ContentPanel.showAdvancedSongPanel(data.getURI(), HomePanel.ContentTypes.album);
                                break;
                            case TRACK:
                                PublicValues.spotifyplayer.load(data.getURI(), true, false, false);
                                break;
                            case LOADMORE:
                                loadMore();
                                break;
                            default:
                                break;
                        }
                    }catch (Exception ignored) {
                        tree.expandRow(0);
                    }
                }
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                ContentPanel.historybutton.isFilled = false;
                ContentPanel.historybutton.setImage(Graphics.HISTORY.getInputStream());
                PublicValues.history = new PlaybackHistory();
            }
        });
    }

    void loadMore() {
        try {
            try {
                ((DefaultTreeModel) tree.getModel()).removeNodeFromParent(((DefaultMutableTreeNode)root.getChildAt(root.getChildCount() - 1)));
                for (SongEntry entry : get15Songs(offset)) {
                    DefaultMutableTreeNode addedTo;
                    DefaultMutableTreeNode artist = new DefaultMutableTreeNode(new URITree.TreeNodeData(entry.artistName, entry.artistURI, URITree.NodeType.ARTIST));
                    ((DefaultTreeModel) tree.getModel()).insertNodeInto(artist, root, root.getChildCount());
                    root.insert(artist, root.getChildCount() - 1);
                    addedArtists.add(new TreeEntry(entry.artistName, entry.artistURI, root));
                    addedTo = artist;
                    DefaultMutableTreeNode album = new DefaultMutableTreeNode(new URITree.TreeNodeData(entry.albumName, entry.albumURI, URITree.NodeType.ALBUM));
                    addedTo.add(album);
                    addedTo = album;
                    addedAlbums.add(new TreeEntry(entry.albumName, entry.albumURI, addedTo));
                    DefaultMutableTreeNode track = new DefaultMutableTreeNode(new URITree.TreeNodeData(entry.songName, entry.songURI, URITree.NodeType.TRACK));
                    addedTo.add(track);
                    offset++;
                }
                for (int i = 0; i < root.getChildCount(); i++) {
                    if (root.getChildAt(i).getChildCount() == 0) {
                        removeEntry(root.getChildAt(i), addedArtists);
                        root.remove(i);
                    }
                }
                if(getCounter() > offset) {
                    root.add(new DefaultMutableTreeNode(new URITree.TreeNodeData("Load More", "", URITree.NodeType.LOADMORE))); //ToDo: Translate
                }
                ((DefaultTreeModel) tree.getModel()).reload();
            }catch (Exception e) {
                ConsoleLogging.Throwable(e);
            }
        }catch (Exception ignored) {
        }
    }

    @SuppressWarnings("all")
    boolean containsEntry(String string, ArrayList<PlaybackHistory.TreeEntry> entries) {
        for(PlaybackHistory.TreeEntry entry : entries) {
            if(entry.name.equals(string)) {
                return true;
            }
        }
        return false;
    }

    void removeEntry(TreeNode node, ArrayList<PlaybackHistory.TreeEntry> entries) {
        int toRemove = 0;
        boolean found = false;
        for(int i = 0; i < entries.size(); i++) {
            PlaybackHistory.TreeEntry entry = entries.get(i);
            if(entry.addedTo == node) {
                toRemove = i;
                found = true;
                break;
            }
        }
        if(found) {
            entries.remove(toRemove);
        }
    }

    public ArrayList<SongEntry> get15Songs(int offset) {
        ArrayList<SongEntry> songs = new ArrayList<>();
        int counter = 0;
        try (LineIterator it = FileUtils.lineIterator(new File(filePath), "UTF-8")) {
            while (it.hasNext()) {
                String line = it.nextLine();
                if(line.startsWith("//")) {
                    //Skipping because this is the message at the top of the file
                    counter++;
                    continue;
                }
                if(counter == offset + 15) {
                    break;
                }
                if(counter <= offset) {
                    counter++;
                    continue;
                }
                songs.add(parseEntry(line));
                counter++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return songs;
    }


    private SongEntry parseEntry(String line) {
        SongEntry entry = new SongEntry();
        int counter = 0;
        for(String s : line.split("::")) {
            switch (counter) {
                case 0:
                    entry.songURI = s;
                    break;
                case 1:
                    entry.songName = s;
                    break;
                case 2:
                    entry.artistName = s;
                    break;
                case 3:
                    entry.artistURI = s;
                    break;
                case 4:
                    entry.albumName = s;
                    break;
                case 5:
                    entry.albumURI = s;
                    break;
            }
            counter++;
        }
        return entry;
    }

    private void append(String line) {
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write("\n" + line);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("all")
    public void addSong(Track t) throws IOException, ParseException, SpotifyWebApiException {
        StringBuilder builder = new StringBuilder();
        builder.append(t.getUri());
        builder.append("::");
        builder.append(t.getName());
        builder.append("::");
        builder.append(t.getArtists()[0].getName());
        builder.append("::");
        builder.append(t.getArtists()[0].getUri());
        builder.append("::");
        builder.append(t.getAlbum().getName());
        builder.append("::");
        builder.append(t.getAlbum().getUri());
        append(builder.toString());
        addToCounter();
    }

    void addToCounter() {
        try {
            int out = Integer.parseInt(IOUtils.toString(Files.newInputStream(Paths.get(counterFilePath))).replace("C", ""));
            out++;
            FileWriter writer = new FileWriter(counterFilePath, false);
            writer.write("C" + out);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getCounter() {
        try {
            return Integer.parseInt(IOUtils.toString(Files.newInputStream(Paths.get(counterFilePath))).replace("C", ""));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeAllSongs() throws IOException {
        new File(filePath).delete();
        new File(filePath).createNewFile();
        FileWriter writer = new FileWriter(counterFilePath, false);
        writer.write(0);
        writer.close();
    }

    @Override
    public void open() {
        DefThread fetchhistory = new DefThread(() -> {
            try {
                for (SongEntry entry : get15Songs(0)) {
                    DefaultMutableTreeNode addedTo;
                    DefaultMutableTreeNode artist = new DefaultMutableTreeNode(new URITree.TreeNodeData(entry.artistName, entry.artistURI, URITree.NodeType.ARTIST));
                    ((DefaultTreeModel) tree.getModel()).insertNodeInto(artist, root, root.getChildCount());
                    root.insert(artist, root.getChildCount() - 1);
                    addedArtists.add(new TreeEntry(entry.artistName, entry.artistURI, root));
                    addedTo = artist;
                    DefaultMutableTreeNode album = new DefaultMutableTreeNode(new URITree.TreeNodeData(entry.albumName, entry.albumURI, URITree.NodeType.ALBUM));
                    addedTo.add(album);
                    addedTo = album;
                    addedAlbums.add(new TreeEntry(entry.albumName, entry.albumURI, addedTo));
                    DefaultMutableTreeNode track = new DefaultMutableTreeNode(new URITree.TreeNodeData(entry.songName, entry.songURI, URITree.NodeType.TRACK));
                    addedTo.add(track);
                    offset++;
                }
                for (int i = 0; i < root.getChildCount(); i++) {
                    if (root.getChildAt(i).getChildCount() == 0) {
                        removeEntry(root.getChildAt(i), addedArtists);
                        root.remove(i);
                    }
                }
                if(getCounter() > offset) {
                    root.add(new DefaultMutableTreeNode(new URITree.TreeNodeData("Load More", "", URITree.NodeType.LOADMORE))); //ToDo: Translate
                }
                tree.expandRow(0);
            }catch (Exception e) {
                ConsoleLogging.Throwable(e);
            }
        });
        fetchhistory.start();
        super.open();
    }

    DefaultMutableTreeNode getEntry(String name, ArrayList<TreeEntry> entries) {
        for(TreeEntry entry : entries) {
            if(entry.name.equals(name)) {
                return entry.addedTo;
            }
        }
        return null;
    }
}
