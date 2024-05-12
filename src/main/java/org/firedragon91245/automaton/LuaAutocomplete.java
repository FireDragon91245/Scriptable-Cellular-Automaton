package org.firedragon91245.automaton;

import org.fife.ui.autocomplete.*;
import org.firedragon91245.automaton.ui.GameIcons;
import org.firedragon91245.automaton.ui.LuaCellRenderer;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.List;

public class LuaAutocomplete {

    public static CompletionProvider getProvider()
    {
        DefaultCompletionProvider provider = new DefaultCompletionProvider();

        SnippetCompletion snippetFunc = new SnippetCompletion(provider, "func", "function %1{myFunc}(%2)\n    %3\nend", "function snippet");
        snippetFunc.setIcon(GameIcons.SNIPPET_ICON);
        provider.addCompletion(snippetFunc);

        FunctionCompletion completion = new FunctionCompletion(provider, "print", "void");
        completion.setParams(List.of(new ParameterizedCompletion.Parameter("text", "string"), new ParameterizedCompletion.Parameter("...", "any")));
        provider.addCompletion(completion);

        //FunctionCompletion completion = new FunctionCompletion(provider, "print", "void");
        //completion.setParams(List.of(new ParameterizedCompletion.Parameter("text", "string"), new ParameterizedCompletion.Parameter("...", "any")));
        //BasicCompletion completion = new BasicCompletion(provider, "if", "if");
        //provider.addCompletion(completion);

        provider.setListCellRenderer(new LuaCellRenderer());

        return provider;
    }

}
