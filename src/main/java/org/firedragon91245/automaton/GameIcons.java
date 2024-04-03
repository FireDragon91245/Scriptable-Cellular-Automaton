package org.firedragon91245.automaton;

import javax.swing.*;
import java.net.URL;

public class GameIcons {
    public static final Icon SNIPPET = new ImageIcon(getBundledResourceURL("insert-code.png"));

    public static URL getBundledResourceURL(String resourcePath)
    {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return classLoader.getResource(resourcePath);
    }
}
