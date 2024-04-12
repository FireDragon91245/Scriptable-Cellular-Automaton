package org.firedragon91245.automaton;

import org.fife.ui.autocomplete.Completion;

import javax.swing.*;
import java.awt.*;

public class LuaCellRenderer implements ListCellRenderer<Object> {

    private final JLabel label;
    public LuaCellRenderer()
    {
        label = new JLabel();
        label.setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Completion c = (Completion) value;

        label.setText(c.getInputText());

        Icon icon = c.getIcon();
        if (icon != null) {
            label.setIcon(icon);
        } else {
            label.setIcon(GameIcons.EMPTY_ICON);
        }

        if (isSelected) {
            label.setBackground(list.getSelectionBackground());
            label.setForeground(list.getSelectionForeground());
        } else {
            label.setBackground(list.getBackground());
            label.setForeground(list.getForeground());
        }

        return label;
    }
}
