package org.firedragon91245.automaton;

import de.milchreis.uibooster.UiBooster;
import de.milchreis.uibooster.model.Form;
import de.milchreis.uibooster.model.FormBuilder;
import de.milchreis.uibooster.model.UiBoosterOptions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AutomatonGame extends JFrame {

    CodeEditor codeEditor = new CodeEditor();

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
        viewCodeEditor.addItemListener(e -> {
            if (viewCodeEditor.isSelected())
            {
                codeEditor.show();
            }
            else
            {
                codeEditor.hide();
            }
        });
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

        JMenuItem viewReload = new JMenuItem("Reload View");
        viewReload.addActionListener(this::menuViewReloadView);
        viewReload.setVisible(true);
        view.add(viewReload);

        view.addSeparator();

        JMenuItem viewOptions = new JMenuItem("Options");
        viewOptions.setVisible(true);
        viewOptions.addActionListener(this::menuViewOptionsAction);
        view.add(viewOptions);


        return menuBar;
    }

    private void menuViewReloadView(ActionEvent event) {
        SwingUtilities.updateComponentTreeUI(this);
        this.validate();
        this.repaint();
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

        codeEditor.setSize(200, 100);
        codeEditor.setLocation(new Point(100, 100));
        //frame.useAsParent(desktop);

        //InternalFrame frame2 = new InternalFrame("test");
        //JList<String> list = new JList<String>(new String[]{"Hello", "World", "Test", "List", "Items"});
        //JLabel lbl = new JLabel();
        //lbl.setText("Hello World");
        //lbl.setIcon(GameIcons.SNIPPET_ICON);
        //lbl.setVisible(true);
        //list.setVisible(true);
        //frame2.add(list);
        //frame2.setVisible(true);
        //frame2.setSize(200, 100);
        //desktop.add(frame2);


        desktop.add(codeEditor);

        JMenuBar menuBar = generateMenuBar();
        setJMenuBar(menuBar);

        codeEditor.useAsParent(desktop);
        codeEditor.updateMenuBarCallback(menuBar::updateUI);

        menuBar.updateUI();

    }

    private Dimension GetMonitorDimensions() {
        DisplayMode m = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
        return new Dimension(m.getWidth(), m.getHeight());
    }
}
