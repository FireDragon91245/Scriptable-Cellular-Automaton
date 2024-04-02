package org.firedragon91245.automaton;

import de.milchreis.uibooster.UiBooster;
import de.milchreis.uibooster.model.Form;
import de.milchreis.uibooster.model.FormBuilder;
import de.milchreis.uibooster.model.UiBoosterOptions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AutomatonGame extends JFrame {

    public AutomatonGame() {
        setTitle("Scriptable Automaton - By FireDragon91245");
        Dimension dim = GetMonitorDimensions();
        setSize(dim);
        setVisible(true);

        initUI();
    }

    private JMenuBar generateMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setVisible(true);

        JMenu file = new JMenu("File");
        file.setVisible(true);
        menuBar.add(file);

        JMenuItem fileExit = new JMenuItem("Exit");
        fileExit.setVisible(true);
        JFrame topLevelFrame = this;
        fileExit.addActionListener(e -> topLevelFrame.dispose());
        file.add(fileExit);

        JMenu view = new JMenu("View");
        view.setVisible(true);
        menuBar.add(view);

        JCheckBoxMenuItem viewCodeEditor = new JCheckBoxMenuItem("Code Editor");
        viewCodeEditor.setVisible(true);
        view.add(viewCodeEditor);

        JCheckBoxMenuItem viewCellManager = new JCheckBoxMenuItem("Cell Manager");
        viewCellManager.setVisible(true);
        view.add(viewCellManager);

        JCheckBoxMenuItem viewSimulation = new JCheckBoxMenuItem("Simulation");
        viewSimulation.setVisible(true);
        view.add(viewSimulation);

        view.addSeparator();

        JMenuItem viewReset = new JMenuItem("Reset");
        viewReset.setVisible(true);
        view.add(viewReset);

        view.addSeparator();

        JMenuItem viewOptions = new JMenuItem("Options");
        viewOptions.setVisible(true);
        viewOptions.addActionListener(this::menuViewOptionsAction);
        view.add(viewOptions);

        return menuBar;
    }

    private void menuViewOptionsAction(ActionEvent actionEvent) {
        FormBuilder f = new UiBooster(UiBoosterOptions.Theme.SWING).createForm("Options");

        Form run = f.run();
    }

    private void initUI() {
        JDesktopPane desktop = new JDesktopPane();

        desktop.setSize(getSize());
        desktop.setVisible(true);
        setContentPane(desktop);

        InternalFrame frame = new InternalFrame();
        frame.setSize(200, 100);
        frame.setLocation(new Point(100, 100));
        frame.setVisible(true);

        desktop.add(frame);

        JMenuBar menuBar = generateMenuBar();
        setJMenuBar(menuBar);

        menuBar.updateUI();

    }

    private Dimension GetMonitorDimensions() {
        DisplayMode m = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
        return new Dimension(m.getWidth(), m.getHeight());
    }
}
