package org.firedragon91245.automaton;

import org.fife.ui.autocomplete.*;

public class LuaAutocomplete {

    public static CompletionProvider getProvider()
    {
        DefaultCompletionProvider provider = new DefaultCompletionProvider();

        provider.addCompletion(new BasicCompletion(provider, "if"));

        provider.addCompletion(new FunctionCompletion(provider, "test", "int"));

        return provider;
    }

}
