package org.firedragon91245.automaton;

import javax.security.auth.callback.Callback;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class InternalFrame extends JInternalFrame implements ComponentListener {

    private Component parent = null;
    private Runnable menuBarUpdateCallback = null;

    public InternalFrame(String name)
    {
        super(name,  true, true, true, true);
        addComponentListener(this);
    }

    public void useAsParent(Component parent)
    {
        this.parent = parent;
    }

    public void updateMenuBarCallback(Runnable menuBarUpdateCallback)
    {
        this.menuBarUpdateCallback = menuBarUpdateCallback;
    }

    private Point getComponentPositionRelativeToParent(Component com)
    {
        Point result = new Point();
        Component curr = com;

        while(curr != parent)
        {
            result.x += curr.getX();
            result.y += curr.getY();
            curr = curr.getParent();
        }

        return result;
    }

    private void getDefaultParent()
    {
            parent = this.getParent();
            while((!(parent instanceof JDesktopPane)) && parent != null)
            {
                parent = parent.getParent();
            }

            if(parent == null)
            {
                parent = this.getParent();
            }
    }

    @Override
    public void componentResized(ComponentEvent e) {

    }

    @Override
    public void componentMoved(ComponentEvent e) {
        if(parent == null)
            getDefaultParent();

        Component com = e.getComponent();
        Point pos = getComponentPositionRelativeToParent(com);

        if(parent != null) {
            Dimension parentSize = parent.getSize();
            if (pos.x < 0 || pos.y < 0 || pos.x + com.getWidth() > parentSize.width || pos.y + com.getHeight() > parentSize.height) {
                com.setLocation(Math.max(0, Math.min(parentSize.width - com.getWidth(), pos.x)), Math.max(0, Math.min(parentSize.height - com.getHeight(), pos.y)));
                parent.repaint();
                if (menuBarUpdateCallback != null)
                    menuBarUpdateCallback.run();
            }
        }
    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }
}
