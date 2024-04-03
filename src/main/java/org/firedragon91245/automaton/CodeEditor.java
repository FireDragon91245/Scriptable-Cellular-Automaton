package org.firedragon91245.automaton;

import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

public class CodeEditor extends InternalFrame{

    private final ArrayList<Component> components = new ArrayList<>();

    public CodeEditor()
    {
        super("Code Editor");
        initUI();
    }

    private void initUI()
    {
        RSyntaxTextArea codeArea = new RSyntaxTextArea();
        codeArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_LUA);
        codeArea.setCodeFoldingEnabled(true);
        codeArea.setVisible(true);

        RTextScrollPane scrollPane = new RTextScrollPane(codeArea);
        scrollPane.setVisible(true);
        this.add(scrollPane);

        CompletionProvider provider = LuaAutocomplete.getProvider();
        AutoCompletion ac = new AutoCompletion(provider);
        ac.setParameterAssistanceEnabled(true);
        ac.install(codeArea);

        components.add(scrollPane);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        super.componentResized(e);

        for(Component c : components)
        {
            c.setSize(this.getSize());
        }
    }
}
