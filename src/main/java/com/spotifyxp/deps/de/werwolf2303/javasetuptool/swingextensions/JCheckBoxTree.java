package com.spotifyxp.deps.de.werwolf2303.javasetuptool.swingextensions;

/*
https://stackoverflow.com/questions/21847411/java-swing-need-a-good-quality-developed-jtree-with-checkboxes

Posted from: https://stackoverflow.com/users/3322273/somethingsomething
 */

import javax.swing.*;
import javax.swing.event.EventListenerList;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;

public class JCheckBoxTree extends JTree {

    JCheckBoxTree selfPointer = this;
    CheckBoxListener l = new CheckBoxListener() {
        @Override
        public void onTrigger(JCheckBox checkBox) {
            //Default
        }
    };


    public void addCheckActionListener(CheckBoxListener listener) {
        l = listener;
    }



    // Defining data structure that will enable to fast check-indicate the state of each node
    // It totally replaces the "selection" mechanism of the JTree
    private class CheckedNode {
        boolean isSelected = false;
        boolean hasChildren;
        boolean allChildrenSelected;

        public CheckedNode(boolean isSelected_, boolean hasChildren_, boolean allChildrenSelected_) {
            isSelected = isSelected_;
            hasChildren = hasChildren_;
            allChildrenSelected = allChildrenSelected_;
        }
    }
    HashMap<TreePath, CheckedNode> nodesCheckingState;
    HashSet<TreePath> checkedPaths = new HashSet<TreePath>();

    // Defining a new event type for the checking mechanism and preparing event-handling mechanism
    protected EventListenerList listenerList = new EventListenerList();

    public class CheckChangeEvent extends EventObject {
        private static final long serialVersionUID = -8100230309044193368L;

        public CheckChangeEvent(Object source) {
            super(source);
        }
    }

    public interface CheckChangeEventListener extends EventListener {
        public void checkStateChanged(CheckChangeEvent event);
    }

    public void addCheckChangeEventListener(CheckChangeEventListener listener) {
        listenerList.add(CheckChangeEventListener.class, listener);
    }
    public void removeCheckChangeEventListener(CheckChangeEventListener listener) {
        listenerList.remove(CheckChangeEventListener.class, listener);
    }

    void fireCheckChangeEvent(CheckChangeEvent evt) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i++) {
            if (listeners[i] == CheckChangeEventListener.class) {
                ((CheckChangeEventListener) listeners[i + 1]).checkStateChanged(evt);
            }
        }
    }

    @Override
    public void setModel(TreeModel newModel) {
        super.setModel(newModel);
        resetCheckingState();
    }

    // New method that returns only the checked paths (totally ignores original "selection" mechanism)
    public TreePath[] getCheckedPaths() {
        return checkedPaths.toArray(new TreePath[checkedPaths.size()]);
    }

    // Returns true in case that the node is selected, has children but not all of them are selected
    public boolean isSelectedPartially(TreePath path) {
        CheckedNode cn = nodesCheckingState.get(path);
        return cn.isSelected && cn.hasChildren && !cn.allChildrenSelected;
    }

    private void resetCheckingState() {
        nodesCheckingState = new HashMap<TreePath, CheckedNode>();
        checkedPaths = new HashSet<TreePath>();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) getModel().getRoot();
        if (node == null) {
            return;
        }
        boolean mode = false;
        try {
            if (!checkmodes.isEmpty()) {
                for (Boolean b : checkmodes) {
                    if (b) {
                        mode = true;
                        break;
                    }
                }
            }else{
                mode = true;
            }
        }catch (NullPointerException ignored) {
        }
        addSubtreeToCheckingStateTracking(node, mode);
    }

    // Creating data structure of the current model for the checking mechanism
    ArrayList<Boolean> checkmodes = new ArrayList<>();
    private void addSubtreeToCheckingStateTracking(DefaultMutableTreeNode node, boolean selected) {
        TreeNode[] path = node.getPath();
        TreePath tp = new TreePath(path);
        CheckedNode cn = new CheckedNode(selected, node.getChildCount() > 0, false);
        nodesCheckingState.put(tp, cn);
        try {
            if(checkmodes.isEmpty()) {
                for (int i = 0; i < node.getChildCount(); i++) {
                    checkmodes.add(true);
                    addSubtreeToCheckingStateTracking((DefaultMutableTreeNode) tp.pathByAddingChild(node.getChildAt(i)).getLastPathComponent(), true);
                }
            }else {
                for (int i = 0; i < node.getChildCount(); i++) {
                    addSubtreeToCheckingStateTracking((DefaultMutableTreeNode) tp.pathByAddingChild(node.getChildAt(i)).getLastPathComponent(), checkmodes.get(i));
                }
            }
        }catch (NullPointerException e) {
            for (int i = 0; i < node.getChildCount(); i++) {
                addSubtreeToCheckingStateTracking((DefaultMutableTreeNode) tp.pathByAddingChild(node.getChildAt(i)).getLastPathComponent(), true);
            }
        }
    }

    // Overriding cell renderer by a class that ignores the original "selection" mechanism
    // It decides how to show the nodes due to the checking-mechanism
    private class CheckBoxCellRenderer extends JPanel implements TreeCellRenderer {
        private static final long serialVersionUID = -7341833835878991719L;
        JCheckBox checkBox;
        public CheckBoxCellRenderer() {
            super();
            this.setLayout(new BorderLayout());
            checkBox = new JCheckBox();
            setOpaque(false);
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                      boolean selected, boolean expanded, boolean leaf, int row,
                                                      boolean hasFocus) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
            Object obj = node.getUserObject();
            TreePath tp = new TreePath(node.getPath());
            CheckedNode cn = nodesCheckingState.get(tp);
            if (cn == null) {
                return this;
            }
            checkBox.setSelected(cn.isSelected);
            checkBox.setText(obj.toString());
            checkBox.setOpaque(cn.isSelected && cn.hasChildren && ! cn.allChildrenSelected);
            checkBox.setSize(selfPointer.getWidth(), checkBox.getHeight());
            add(checkBox, BorderLayout.CENTER);
            return this;
        }
    }

    public JCheckBoxTree() {
        super();
        // Disabling toggling by double-click
        this.setToggleClickCount(0);
        // Overriding cell renderer by new one defined above
        CheckBoxCellRenderer cellRenderer = new CheckBoxCellRenderer();
        this.setCellRenderer(cellRenderer);

        // Overriding selection model by an empty one
        DefaultTreeSelectionModel dtsm = new DefaultTreeSelectionModel() {
            private static final long serialVersionUID = -8190634240451667286L;
            // Totally disabling the selection mechanism
            public void setSelectionPath(TreePath path) {
            }
            public void addSelectionPath(TreePath path) {
            }
            public void removeSelectionPath(TreePath path) {
            }
            public void setSelectionPaths(TreePath[] pPaths) {
            }
        };
        // Calling checking mechanism on mouse click
        this.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent arg0) {
                TreePath tp = selfPointer.getPathForLocation(arg0.getX(), arg0.getY());
                if (tp == null) {
                    return;
                }
                boolean checkMode = ! nodesCheckingState.get(tp).isSelected;
                for(int i = 0; i < selfPointer.getModel().getChildCount(selfPointer.getModel().getRoot()); i++) {
                    if(selfPointer.getModel().getChild(selfPointer.getModel().getRoot(), i).toString().equals(tp.getLastPathComponent().toString())) {
                        checkmodes.set(i, checkMode);
                    }
                }

                checkSubTree(tp, checkMode);
                updatePredecessorsWithCheckMode(tp, checkMode);
                // Firing the check change event
                fireCheckChangeEvent(new CheckChangeEvent(new Object()));
                JCheckBox box = new JCheckBox();
                box.setText(tp.getLastPathComponent().toString());
                box.setSelected(checkMode);
                if(box.getText().equals(selfPointer.getModel().getRoot().toString())) {
                    for(int i = 0; i < checkmodes.size(); i++) {
                        checkmodes.set(i, checkMode);
                    }
                    for(int i = 0; i < selfPointer.getModel().getChildCount(selfPointer.getModel().getRoot()); i++) {
                        JCheckBox box2 = new JCheckBox();
                        box2.setText(selfPointer.getModel().getChild(selfPointer.getModel().getRoot(), i).toString());
                        box2.setSelected(checkMode);
                        l.onTrigger(box2);
                    }
                }else {
                    l.onTrigger(box);
                }
                // Repainting tree after the data structures were updated
                selfPointer.repaint();
            }
            public void mouseEntered(MouseEvent arg0) {
            }
            public void mouseExited(MouseEvent arg0) {
            }
            public void mousePressed(MouseEvent arg0) {
            }
            public void mouseReleased(MouseEvent arg0) {
            }
        });
        this.setSelectionModel(dtsm);
    }

    // When a node is checked/unchecked, updating the states of the predecessors
    protected void updatePredecessorsWithCheckMode(TreePath tp, boolean check) {
        TreePath parentPath = tp.getParentPath();
        // If it is the root, stop the recursive calls and return
        if (parentPath == null) {
            return;
        }
        CheckedNode parentCheckedNode = nodesCheckingState.get(parentPath);
        DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) parentPath.getLastPathComponent();
        parentCheckedNode.allChildrenSelected = true;
        parentCheckedNode.isSelected = false;
        for (int i = 0 ; i < parentNode.getChildCount() ; i++) {
            TreePath childPath = parentPath.pathByAddingChild(parentNode.getChildAt(i));
            CheckedNode childCheckedNode = nodesCheckingState.get(childPath);
            // It is enough that even one subtree is not fully selected
            // to determine that the parent is not fully selected
            if (! childCheckedNode.allChildrenSelected) {
                parentCheckedNode.allChildrenSelected = false;
            }
            // If at least one child is selected, selecting also the parent
            if (childCheckedNode.isSelected) {
                parentCheckedNode.isSelected = true;
            }
        }
        if (parentCheckedNode.isSelected) {
            checkedPaths.add(parentPath);
        } else {
            checkedPaths.remove(parentPath);
        }
        // Go to upper predecessor
        updatePredecessorsWithCheckMode(parentPath, check);
    }

    // Recursively checks/unchecks a subtree
    protected void checkSubTree(TreePath tp, boolean check) {
        CheckedNode cn = nodesCheckingState.get(tp);
        cn.isSelected = check;
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tp.getLastPathComponent();
        for (int i = 0 ; i < node.getChildCount() ; i++) {
            checkSubTree(tp.pathByAddingChild(node.getChildAt(i)), check);
        }
        cn.allChildrenSelected = check;
        if (check) {
            checkedPaths.add(tp);
        } else {
            checkedPaths.remove(tp);
        }
    }

    public void save() {

    }

    public void load() {

    }

    @FunctionalInterface
    public static interface CheckBoxListener {
        void onTrigger(JCheckBox checkBox);
    }
}