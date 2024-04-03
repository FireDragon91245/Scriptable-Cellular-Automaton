package org.firedragon91245.automaton;

import org.fife.ui.autocomplete.*;

public class LuaAutocomplete {

    public static CompletionProvider getProvider()
    {
        DefaultCompletionProvider provider = new DefaultCompletionProvider();

        provider.addCompletion(new BasicCompletion(provider, "if"));

        provider.addCompletion(new ShorthandCompletion(provider, "func", "function %1(%2)\n    %3\nend", "function snippet", "function template"));

        TemplateCompletion completion = new TemplateCompletion(provider, "func", "func", "function ${test} ${abc}", "for loop", "for loop template");

        //SnippetCompletion completion = new SnippetCompletion(provider, "func", "function %1{myFunc}(%2)\n    %3\nend", "function snippet");
        completion.setIcon(GameIcons.SNIPPET);

        provider.addCompletion(completion);

        return provider;
    }

}
