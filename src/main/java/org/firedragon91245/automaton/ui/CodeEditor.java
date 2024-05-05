package org.firedragon91245.automaton.ui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.fife.rsta.ui.CollapsibleSectionPanel;
import org.fife.rsta.ui.GoToDialog;
import org.fife.rsta.ui.SizeGripIcon;
import org.fife.rsta.ui.search.*;
import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.fife.ui.rtextarea.SearchContext;
import org.firedragon91245.automaton.AutomatonGame;
import org.firedragon91245.automaton.LuaAutocomplete;
import org.firedragon91245.automaton.json.JsonExcludePolicy;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class CodeEditor extends InternalFrame implements SearchListener {

    private final ArrayList<Component> components = new ArrayList<>();

    private RSyntaxTextArea te;
    private FindToolBar findToolBar;
    private ReplaceToolBar replaceToolBar;
    private FindDialog findDialog;
    private ReplaceDialog replaceDialog;
    private CollapsibleSectionPanel csp;
    private AutomatonGame topLevelComponent;
    private JPanel findAndReplacePanel;
    private JSplitPane splitPane;



    public CodeEditor(AutomatonGame topLevelComponent)
    {
        super("Code Editor");
        initUI();
        this.topLevelComponent = topLevelComponent;
    }

    private void initUI()
    {
        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        setContentPane(splitPane);
        findAndReplacePanel = new JPanel();
        findAndReplacePanel.setVisible(true);
        splitPane.setBottomComponent(findAndReplacePanel);


        te = new RSyntaxTextArea();
        te.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_LUA);
        te.setCodeFoldingEnabled(true);
        te.setMarkOccurrences(true);
        te.setVisible(true);

        RTextScrollPane scrollPane = new RTextScrollPane(te);
        scrollPane.setBounds(0, 0, 200, 200);
        scrollPane.setVisible(true);
        splitPane.setTopComponent(scrollPane);

        CompletionProvider provider = LuaAutocomplete.getProvider();
        AutoCompletion ac = new AutoCompletion(provider);
        ac.setShowDescWindow(true);
        ac.setParameterAssistanceEnabled(true);

        ac.setAutoCompleteEnabled(true);
        ac.setAutoActivationEnabled(true);
        ac.setAutoCompleteSingleChoices(true);
        ac.setAutoActivationDelay(800);
        ac.setParameterAssistanceEnabled(true);
        ac.install(te);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        super.componentResized(e);

        for(Component c : components)
        {
            c.setSize(this.getSize());
        }
    }

    public void initFindDialogs() {
        findDialog = new FindDialog(topLevelComponent, this);
        replaceDialog = new ReplaceDialog(topLevelComponent, this);

        SearchContext context = findDialog.getSearchContext();
        replaceDialog.setSearchContext(context);

        findToolBar = new FindToolBar(this);
        findToolBar.setSearchContext(context);
        replaceToolBar = new ReplaceToolBar(this);
        replaceToolBar.setSearchContext(context);

        findToolBar.setVisible(false);
        replaceToolBar.setVisible(false);
        findAndReplacePanel.add(findToolBar);
        findAndReplacePanel.add(replaceToolBar);
    }

    public JMenu getSearchMenu()
    {
        JMenu search = new JMenu("Search");
        search.add(new JMenuItem(new ShowFindDialogAction()));
        search.add(new JMenuItem(new ShowReplaceDialogAction()));
        search.add(new JMenuItem(new GoToLineAction()));
        search.addSeparator();

        JMenuItem showFindAdnReplaceToolBar = new JMenuItem("Show Find and Replace Toolbar");
        search.add(showFindAdnReplaceToolBar);
        showFindAdnReplaceToolBar.addActionListener(e -> {
            if(findToolBar.isVisible())
            {
                findToolBar.setVisible(false);
                replaceToolBar.setVisible(true);
            }
            else {
                replaceToolBar.setVisible(!replaceToolBar.isVisible());
            }
        });

        JMenuItem showFindToolBar = new JMenuItem("Show Find Toolbar");
        search.add(showFindToolBar);
        showFindToolBar.addActionListener(e -> {
            if(replaceToolBar.isVisible())
            {
                replaceToolBar.setVisible(false);
                findToolBar.setVisible(true);
            }
            else
            {
                findToolBar.setVisible(!findToolBar.isVisible());
            }
        });

        return search;
    }

    @Override
    public void searchEvent(SearchEvent searchEvent) {

    }

    @Override
    public String getSelectedText() {
        return null;
    }

    private class ShowFindDialogAction extends AbstractAction {

        ShowFindDialogAction() {
            super("Find...");
            int c = getToolkit().getMenuShortcutKeyMaskEx();
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F, c));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (replaceDialog.isVisible()) {
                replaceDialog.setVisible(false);
            }
            findDialog.setVisible(true);
        }

    }

    private class ShowReplaceDialogAction extends AbstractAction {

        ShowReplaceDialogAction() {
            super("Replace...");
            int c = getToolkit().getMenuShortcutKeyMaskEx();
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_H, c));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (findDialog.isVisible()) {
                findDialog.setVisible(false);
            }
            replaceDialog.setVisible(true);
        }

    }

    private class GoToLineAction extends AbstractAction {

        GoToLineAction() {
            super("Go To Line...");
            int c = getToolkit().getMenuShortcutKeyMaskEx();
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_L, c));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (findDialog.isVisible()) {
                findDialog.setVisible(false);
            }
            if (replaceDialog.isVisible()) {
                replaceDialog.setVisible(false);
            }
            GoToDialog dialog = new GoToDialog(topLevelComponent);
            dialog.setMaxLineNumberAllowed(te.getLineCount());
            dialog.setVisible(true);
            int line = dialog.getLineNumber();
            if (line>0) {
                try {
                    te.setCaretPosition(te.getLineStartOffset(line-1));
                } catch (BadLocationException ble) { // Never happens
                    UIManager.getLookAndFeel().provideErrorFeedback(te);
                    ble.printStackTrace();
                }
            }
        }

    }
}
