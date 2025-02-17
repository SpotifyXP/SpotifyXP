package com.spotifyxp.swingextension;

import com.spotifyxp.PublicValues;
import com.spotifyxp.utils.AsyncActionListener;
import com.spotifyxp.utils.AsyncMouseListener;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class ContextMenu {
    public interface GlobalContextMenuItem {
        Runnable toRun(JComponent component, @Nullable ArrayList<String> uris);
        String name();
        boolean shouldBeAdded(JComponent component, Class<?> containingClass);
    }

    final JPopupMenu holder = new JPopupMenu();

    public ContextMenu() {
    }

    public ContextMenu(JComponent container, JComponent click, @Nullable ArrayList<String> uris, Class<?> containingClass) {
        click.addMouseListener(new AsyncMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (SwingUtilities.isRightMouseButton(e)) {
                    for (MouseListener listener : container.getMouseListeners()) {
                        listener.mouseClicked(e);
                    }
                }
            }
        }));
        container.addMouseListener(new AsyncMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (SwingUtilities.isRightMouseButton(e)) {
                    holder.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        }));
        for (GlobalContextMenuItem item : PublicValues.globalContextMenuItems) {
            JMenuItem i = new JMenuItem(item.name());
            i.addActionListener(new AsyncActionListener(e -> item.toRun(container, uris).run()));
            if(item.shouldBeAdded(container, containingClass)) holder.add(i);
        }
        PublicValues.contextMenus.add(this);
    }

    public ContextMenu(JPanel panel, @Nullable ArrayList<String> uris, Class<?> containingClass) {
        panel.addMouseListener(new AsyncMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (SwingUtilities.isRightMouseButton(e)) {
                    holder.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        }));
        for (GlobalContextMenuItem item : PublicValues.globalContextMenuItems) {
            JMenuItem i = new JMenuItem(item.name());
            i.addActionListener(new AsyncActionListener(e -> item.toRun(panel, uris).run()));
            if(item.shouldBeAdded(panel, containingClass)) holder.add(i);
        }
        PublicValues.contextMenus.add(this);
    }

    public ContextMenu(JComponent component, @Nullable ArrayList<String> uris, Class<?> containingClass) {
        component.addMouseListener(new AsyncMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (SwingUtilities.isRightMouseButton(e)) {
                    holder.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        }));
        for (GlobalContextMenuItem item : PublicValues.globalContextMenuItems) {
            JMenuItem i = new JMenuItem(item.name());
            i.addActionListener(new AsyncActionListener(e -> item.toRun(component, uris).run()));
            if(item.shouldBeAdded(component, containingClass)) holder.add(i);
        }
        PublicValues.contextMenus.add(this);
    }

    public void addItem(String text, Runnable onClick) {
        JMenuItem item = new JMenuItem(text);
        item.addActionListener(new AsyncActionListener(e -> onClick.run()));
        holder.add(item);
    }

    public void addSeparator() {
        holder.add(new JSeparator());
    }

    /**
     * Show the context menu at a specific location
     * @param invoker - The component to which the location is relative
     * @param x - X location where the context menu should appear
     * @param y - Y location where the context menu should appear
     */
    public void showAt(@Nullable JComponent invoker, int x, int y) {
        if(invoker == null) {
            holder.setLocation(x, y);
            holder.setVisible(true);
        }else{
            holder.show(invoker, x, y);
        }
    }

    /**
     * Show the context menu at a specific location with a specific size
     * @apiNote You have to reset the preferred size manually after invoking this
     * @param invoker - The component to which the location is relative
     * @param x - X location where the context menu should appear
     * @param y - Y location where the context menu should appear
     * @param w - Width of the context menu
     * @param h - Height of the context menu
     */
    public void showAt(@Nullable JComponent invoker, int x, int y, @Nullable Integer w, @Nullable Integer h) {
        Dimension preferredSize = holder.getPreferredSize();
        if(w != null) preferredSize.width = w;
        if(h != null) preferredSize.height = h;
        holder.setPreferredSize(preferredSize);
        showAt(invoker, x, y);
    }
}
