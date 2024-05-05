package org.firedragon91245.automaton;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AutomatonGame().setVisible(true);
        });
    }
}