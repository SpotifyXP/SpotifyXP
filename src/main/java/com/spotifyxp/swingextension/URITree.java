package com.spotifyxp.swingextension;

import com.spotifyxp.graphics.Graphics;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.utils.SVGUtils;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;


public class URITree extends JTree {
    public URITree(DefaultMutableTreeNode node) {
        super(node);
        setCellRenderer(new CustomTreeCellRenderer());
    }

    public enum NodeType {
        ARTIST,
        TRACK,
        ALBUM,
        PLAYLIST,
        SHOW,
        EPISODE,
        LOADMORE
    }

    public static class TreeNodeData {
        private final NodeType nodetype;
        private final String text;
        private final String uri;

        public TreeNodeData(String text, String uri, NodeType nodetype) {
            this.text = text;
            this.uri = uri;
            this.nodetype = nodetype;
        }

        public String getText() {
            return text;
        }

        public String getURI() {
            return uri;
        }

        public NodeType getNodetype() {
            return nodetype;
        }
    }

    private static class CustomTreeModel extends DefaultTreeModel {
        public CustomTreeModel(DefaultMutableTreeNode root) {
            super(root);
        }
    }

    private static class CustomTreeCellRenderer extends DefaultTreeCellRenderer {
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            if (value instanceof DefaultMutableTreeNode) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                if (node.getUserObject() instanceof TreeNodeData) {
                    TreeNodeData data = (TreeNodeData) node.getUserObject();
                    setText(data.getText());
                    try {
                        switch (data.nodetype) {
                            case PLAYLIST:
                                setIcon(SVGUtils.svgToImageIconSameSize(Graphics.PLAYLIST.getInputStream(), getPreferredSize()));
                                break;
                            case TRACK:
                                setIcon(SVGUtils.svgToImageIconSameSize(Graphics.TRACK.getInputStream(), getPreferredSize()));
                                break;
                            case ALBUM:
                                setIcon(SVGUtils.svgToImageIconSameSize(Graphics.ALBUM.getInputStream(), getPreferredSize()));
                                break;
                            case SHOW:
                                setIcon(SVGUtils.svgToImageIconSameSize(Graphics.SHOW.getInputStream(), getPreferredSize()));
                                break;
                            case ARTIST:
                                setIcon(SVGUtils.svgToImageIconSameSize(Graphics.ACCOUNT.getInputStream(), getPreferredSize()));
                                break;
                            case EPISODE:
                                setIcon(SVGUtils.svgToImageIconSameSize(Graphics.MICROPHONE.getInputStream(), getPreferredSize()));
                                break;
                            case LOADMORE:
                                setIcon(SVGUtils.svgToImageIconSameSize(Graphics.DOTS.getInputStream(), getPreferredSize()));
                                break;
                            default:
                                ConsoleLogging.warning("[URITree] Invalid nodeType! Falling back to folder icon");
                        }
                    } catch (NullPointerException ignored) {
                    }
                }
            }
            return this;
        }
    }
}
