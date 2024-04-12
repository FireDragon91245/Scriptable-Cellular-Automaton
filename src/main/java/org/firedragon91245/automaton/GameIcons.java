package org.firedragon91245.automaton;

import org.jdesktop.swingx.icon.EmptyIcon;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class GameIcons {
    public static final int ICON_SIZE = 16;
    public static final ImageIcon SNIPPET = loadIcon("insert-code.png");
    public static final ImageIcon SNIPPET_ICON = scale(SNIPPET, ICON_SIZE, ICON_SIZE);
    public static final EmptyIcon EMPTY_ICON = new EmptyIcon(ICON_SIZE, ICON_SIZE);

    public static ImageIcon loadIcon(String resourcePath)
    {
        return new ImageIcon(getBundledResourceURL(resourcePath));
    }

    public static ImageIcon loadIcon(String resourcePath, int width, int height)
    {
        return scale(loadIcon(resourcePath), width, height);
    }

    public static ImageIcon scale(ImageIcon icon, int width, int height)
    {
        return new ImageIcon(icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }

    public static URL getBundledResourceURL(String resourcePath)
    {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return classLoader.getResource(resourcePath);
    }
}
