package org.firedragon91245.automaton.ui;

import javax.swing.*;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.List;
import java.util.function.Consumer;

public class UICallbackWrappers {
    public static class PositionSizeListener implements ComponentListener {
        private final Consumer<Rectangle> f;
        private final Component topLevelParent;

        public PositionSizeListener(Consumer<Rectangle> f, Component topLevelParent) {
            this.f = f;
            this.topLevelParent = topLevelParent;
        }

        @Override
        public void componentResized(ComponentEvent e) {
            f.accept(SwingUtilities.convertRectangle(e.getComponent(), e.getComponent().getBounds(), topLevelParent));
        }

        @Override
        public void componentMoved(ComponentEvent e) {
            f.accept(SwingUtilities.convertRectangle(e.getComponent(), e.getComponent().getBounds(), topLevelParent));
        }

        @Override
        public void componentShown(ComponentEvent e) {

        }

        @Override
        public void componentHidden(ComponentEvent e) {

        }
    }

    public static class InternalFrameCloseOpenListener implements InternalFrameListener {
        private final java.util.List<Consumer<Boolean>> openCloseConsumers;

        public InternalFrameCloseOpenListener(List<Consumer<Boolean>> openCloseConsumers) {
            this.openCloseConsumers = openCloseConsumers;
        }

        @Override
        public void internalFrameOpened(InternalFrameEvent e) {
            openCloseConsumers.forEach(c -> c.accept(true));
        }

        @Override
        public void internalFrameClosing(InternalFrameEvent e) {

        }

        @Override
        public void internalFrameClosed(InternalFrameEvent e) {
            openCloseConsumers.forEach(c -> c.accept(false));
        }

        @Override
        public void internalFrameIconified(InternalFrameEvent e) {

        }

        @Override
        public void internalFrameDeiconified(InternalFrameEvent e) {

        }

        @Override
        public void internalFrameActivated(InternalFrameEvent e) {

        }

        @Override
        public void internalFrameDeactivated(InternalFrameEvent e) {

        }
    }
}
