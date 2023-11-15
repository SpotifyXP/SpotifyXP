package com.spotifyxp.history;

import com.spotifyxp.PublicValues;
import com.spotifyxp.deps.de.werwolf2303.sql.*;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.Track;
import com.spotifyxp.graphics.Graphics;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.panels.ContentPanel;
import com.spotifyxp.panels.HomePanel;
import com.spotifyxp.swingextension.JFrame2;
import com.spotifyxp.swingextension.URITree;
import com.spotifyxp.threading.DefThread;
import com.spotifyxp.utils.GraphicalMessage;

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
import java.sql.SQLException;
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

    private static URITree tree;
    private static DefaultMutableTreeNode root;
    private static ArrayList<PlaybackHistory.TreeEntry> addedArtists = new ArrayList<>();
    private static int offset = 0;
    private static String databasePath;
    private static SQLSession sqlSession;
    private static SQLTable sqlTable;

    public PlaybackHistory() {
        databasePath = new File(PublicValues.fileslocation, "playbackhistory.db").getAbsolutePath();

        sqlSession = new SQLSession(databasePath);
        sqlSession.loadDriver("org.sqlite.JDBC", "jdbc", "sqlite");

        try {
            sqlSession.connect();
        }catch (SQLException e) {
            e.printStackTrace();
            ConsoleLogging.error("Can't establish a connection to the playback history database");
            PublicValues.history = null;
            return;
        }

        sqlTable = new SQLTable("history");

        sqlSession.initSQLElement(sqlTable);
        try {
            if(!sqlTable.exists()) {
                try {
                    sqlTable.create(new SQLEntryPair("track_uri", false, SQLEntryTypes.STRING),
                            new SQLEntryPair("track_name", false, SQLEntryTypes.STRING),
                            new SQLEntryPair("artist_uri", false, SQLEntryTypes.STRING),
                            new SQLEntryPair("artist_name", false, SQLEntryTypes.STRING),
                            new SQLEntryPair("album_uri", false, SQLEntryTypes.STRING),
                            new SQLEntryPair("album_name", false, SQLEntryTypes.STRING),
                            new SQLEntryPair("count", false, SQLEntryTypes.INTEGER));
                }catch (SQLException e) {
                    PublicValues.history = null;
                    return;
                }
            }
        } catch (SQLException e) {
            PublicValues.history = null;
            return;
        }
        setPreferredSize(new Dimension(300, 400));
        setTitle(PublicValues.language.translate("ui.history.title"));

        root = new DefaultMutableTreeNode(PublicValues.language.translate("ui.history.tree.root"));
        tree = new URITree(root);

        JScrollPane pane = new JScrollPane(tree);
        add(pane, BorderLayout.CENTER);

        JButton removeall = new JButton(PublicValues.language.translate("ui.history.removeall"));

        removeall.addActionListener(e -> {
            try {
                removeAllSongs();
                dispose();
                ContentPanel.historybutton.isFilled = false;
                ContentPanel.historybutton.setImage(com.spotifyxp.graphics.Graphics.HISTORY.getInputStream());
                PublicValues.history = new PlaybackHistory();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        });

        add(removeall, BorderLayout.SOUTH);



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
            }
        });
    }

    void loadMore() {
        try {
            try {
                ((DefaultTreeModel) tree.getModel()).removeNodeFromParent(((DefaultMutableTreeNode)root.getChildAt(root.getChildCount() - 1)));
                for (PlaybackHistory.SongEntry entry : get15Songs(offset)) {
                    DefaultMutableTreeNode addedTo;
                    DefaultMutableTreeNode artist = new DefaultMutableTreeNode(new URITree.TreeNodeData(entry.artistName, entry.artistURI, URITree.NodeType.ARTIST));
                    ((DefaultTreeModel) tree.getModel()).insertNodeInto(artist, root, root.getChildCount());
                    root.insert(artist, root.getChildCount() - 1);
                    addedArtists.add(new PlaybackHistory.TreeEntry(entry.artistName, entry.artistURI, root));
                    addedTo = artist;
                    DefaultMutableTreeNode album = new DefaultMutableTreeNode(new URITree.TreeNodeData(entry.albumName, entry.albumURI, URITree.NodeType.ALBUM));
                    addedTo.add(album);
                    addedTo = album;
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
                if(sqlTable.tryGetRowCount() - 1 > offset) {
                    root.add(new DefaultMutableTreeNode(new URITree.TreeNodeData(PublicValues.language.translate("ui.general.loadmore"), "", URITree.NodeType.LOADMORE)));
                }
                ((DefaultTreeModel) tree.getModel()).reload();
            }catch (Exception e) {
                ConsoleLogging.Throwable(e);
            }
        }catch (Exception ignored) {
        }
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

    public ArrayList<PlaybackHistory.SongEntry> get15Songs(int offset) {
        ArrayList<PlaybackHistory.SongEntry> songs = new ArrayList<>();
        int counter = 0;
        try {
            PlaybackHistory.SongEntry entry = new PlaybackHistory.SongEntry();
            for(SQLEntryPair pair : sqlTable.parseTableBackwards(15, offset, "count")) {
                switch (counter) {
                    case 0:
                        entry.songURI = pair.getValueString();
                        break;
                    case 1:
                        entry.songName = pair.getValueString();
                        break;
                    case 2:
                        entry.artistURI = pair.getValueString();
                        break;
                    case 3:
                        entry.artistName = pair.getValueString();
                        break;
                    case 4:
                        entry.albumName = pair.getValueString();
                        break;
                    case 5:
                        entry.albumURI = pair.getValueString();
                        songs.add(entry);
                        break;
                    case 6:
                        entry = new PlaybackHistory.SongEntry();
                        counter = 0;
                        continue;
                }
                counter++;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassCastException ignored) {
        }
        return songs;
    }

    public void removeAllSongs() throws SQLException {
        sqlTable.clearTable();
        root.removeAllChildren();
        ((DefaultTreeModel) tree.getModel()).reload();
    }

    @Override
    public void open() {
        offset = 0;
        addedArtists = new ArrayList<>();
        root.removeAllChildren();
        ((DefaultTreeModel) tree.getModel()).reload();

        DefThread fetchhistory = new DefThread(() -> {
            try {
                for (PlaybackHistory.SongEntry entry : get15Songs(0)) {
                    DefaultMutableTreeNode addedTo;
                    DefaultMutableTreeNode artist = new DefaultMutableTreeNode(new URITree.TreeNodeData(entry.artistName, entry.artistURI, URITree.NodeType.ARTIST));
                    ((DefaultTreeModel) tree.getModel()).insertNodeInto(artist, root, root.getChildCount());
                    root.insert(artist, root.getChildCount() - 1);
                    addedArtists.add(new PlaybackHistory.TreeEntry(entry.artistName, entry.artistURI, root));
                    addedTo = artist;
                    DefaultMutableTreeNode album = new DefaultMutableTreeNode(new URITree.TreeNodeData(entry.albumName, entry.albumURI, URITree.NodeType.ALBUM));
                    addedTo.add(album);
                    addedTo = album;
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
                if(sqlTable.tryGetRowCount() - 1 > offset) {
                    root.add(new DefaultMutableTreeNode(new URITree.TreeNodeData(PublicValues.language.translate("ui.general.loadmore"), "", URITree.NodeType.LOADMORE)));
                }
                tree.expandRow(0);
            }catch (Exception e) {
                ConsoleLogging.Throwable(e);
            }
        });
        fetchhistory.start();
        super.open();
    }

    DefaultMutableTreeNode getEntry(String name, ArrayList<PlaybackHistory.TreeEntry> entries) {
        for(PlaybackHistory.TreeEntry entry : entries) {
            if(entry.name.equals(name)) {
                return entry.addedTo;
            }
        }
        return null;
    }

    public void addSong(Track t) throws SQLException {
        sqlTable.insertIntoTable(new SQLInsert(t.getUri(), SQLEntryTypes.STRING),
                new SQLInsert(t.getName(), SQLEntryTypes.STRING),
                new SQLInsert(t.getArtists()[0].getUri(), SQLEntryTypes.STRING),
                new SQLInsert(t.getArtists()[0].getName(), SQLEntryTypes.STRING),
                new SQLInsert(t.getAlbum().getName(), SQLEntryTypes.STRING),
                new SQLInsert(t.getAlbum().getUri(), SQLEntryTypes.STRING),
                new SQLInsert(sqlTable.getRowCount(), SQLEntryTypes.INTEGER));
    }
}
