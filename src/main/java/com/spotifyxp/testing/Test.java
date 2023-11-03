package com.spotifyxp.testing;

import com.spotifyxp.PublicValues;
import com.spotifyxp.args.CustomSaveDir;
import com.spotifyxp.history.PlaybackHistory;
import com.spotifyxp.swingextension.JFrame2;
import com.spotifyxp.swingextension.URITree;
import com.spotifyxp.theming.themes.DarkGreen;
import com.spotifyxp.theming.themes.Legacy;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
        PublicValues.theme = new Legacy();
        new CustomSaveDir().runArgument(new File("data").getAbsolutePath()).run();

        JFrame2 frame = new JFrame2("DevTest");
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Playback History");
        URITree tree = new URITree(root);

        PlaybackHistory history = new PlaybackHistory();
        ArrayList<PlaybackHistory.TreeEntry> addedArtists = new ArrayList<>();
        ArrayList<PlaybackHistory.TreeEntry> addedAlbums = new ArrayList<>();

        for(PlaybackHistory.SongEntry entry : history.get15Songs(0)) {
            DefaultMutableTreeNode addedTo = root;
            if(!contains(entry.artistName, addedArtists)) {
                DefaultMutableTreeNode artist = new DefaultMutableTreeNode(new URITree.TreeNodeData(entry.artistName, "todo", URITree.NodeType.ARTIST));
                root.add(artist);
                addedArtists.add(new PlaybackHistory.TreeEntry(entry.artistName, "todo", root));
                addedTo = artist;
            }
            if(!contains(entry.albumName, addedAlbums)) {
                DefaultMutableTreeNode album = new DefaultMutableTreeNode(new URITree.TreeNodeData(entry.albumName, "todo", URITree.NodeType.ALBUM));
                addedTo.add(album);
                DefaultMutableTreeNode track = new DefaultMutableTreeNode(new URITree.TreeNodeData(entry.songName, "todo", URITree.NodeType.TRACK));
                album.add(track);
                addedAlbums.add(new PlaybackHistory.TreeEntry(entry.albumName, "todo", album));
            }
        }

        for(int i = 0; i < root.getChildCount(); i++) {
            if(root.getChildAt(i).getChildCount() == 0) {
                remove(root.getChildAt(i), addedArtists);
                root.remove(i);
            }
        }

        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                try {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionModel().getSelectionPath().getLastPathComponent();
                    URITree.TreeNodeData data = ((URITree.TreeNodeData) node.getUserObject());

                }catch (Exception ignored) {
                }
            }
        });

        frame.add(tree, BorderLayout.CENTER);
        frame.setPreferredSize(new Dimension(800, 600));
        frame.setVisible(true);
        frame.pack();
    }


    static boolean contains(String string, ArrayList<PlaybackHistory.TreeEntry> entries) {
        for(PlaybackHistory.TreeEntry entry : entries) {
            if(entry.name.equals(string)) {
                return true;
            }
        }
        return false;
    }

    static void remove(TreeNode node, ArrayList<PlaybackHistory.TreeEntry> entries) {
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
}
