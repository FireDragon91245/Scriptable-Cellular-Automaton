package org.firedragon91245.automaton;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.milchreis.uibooster.UiBooster;
import de.milchreis.uibooster.model.Form;
import de.milchreis.uibooster.model.FormBuilder;
import de.milchreis.uibooster.model.UiBoosterOptions;
import org.fife.rsta.ui.search.FindDialog;
import org.firedragon91245.automaton.json.JsonExcludePolicy;
import org.firedragon91245.automaton.ui.CodeEditor;
import org.firedragon91245.automaton.ui.UICallbackWrappers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class AutomatonGame extends JFrame {

    public final GameSettings settings = new GameSettings();
    private final CodeEditor codeEditor = new CodeEditor(this);
    private final UIComponents uiComponents = new UIComponents();
    public final Gson gson = buildGson();

    public AutomatonGame() {
        setTitle("Scriptable Automaton - By FireDragon91245");
        Dimension dim = GetMonitorDimensions();
        setSize(dim);

        initUI();
    }

    private Gson buildGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.setExclusionStrategies(new JsonExcludePolicy());
        return builder.create();
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
        this.uiComponents.viewCodeEditor = viewCodeEditor;
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

    public void menuViewReloadView(ActionEvent event) {
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

        JMenuBar menuBar = generateMenuBar();

        codeEditor.initFindDialogs();
        codeEditor.setSize(200, 100);
        codeEditor.setLocation(new Point(100, 100));
        codeEditor.useAsParent(desktop);
        codeEditor.updateMenuBarCallback(menuBar::updateUI);
        codeEditor.addComponentListener(new UICallbackWrappers.PositionSizeListener(settings::setCodeEditorRectangle, this));
        codeEditor.addInternalFrameListener(new UICallbackWrappers.InternalFrameCloseOpenListener(List.of(settings::setCodeEditorVisible, uiComponents.viewCodeEditor::setSelected)));

        menuBar.add(codeEditor.getSearchMenu());

        setJMenuBar(menuBar);
        menuBar.updateUI();

        desktop.add(codeEditor);
    }

    private Dimension GetMonitorDimensions() {
        DisplayMode m = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
        return new Dimension(m.getWidth(), m.getHeight());
    }

    private class UIComponents {
        public JCheckBoxMenuItem viewCodeEditor;
    }
}
