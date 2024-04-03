package org.firedragon91245.automaton;

import org.fife.ui.autocomplete.AbstractCompletion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.ParameterizedCompletion;
import org.fife.ui.autocomplete.ParameterizedCompletionInsertionInfo;

import javax.swing.text.JTextComponent;
import java.util.ArrayList;
import java.util.IllegalFormatWidthException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SnippetCompletion extends AbstractCompletion implements ParameterizedCompletion {
    private final String snippetName;
    private final String snippetCode;
    private final String description;

    List<SnippetSegment> segments = new ArrayList<>();

    private final Pattern insertionPattern = Pattern.compile("%(\\d+)");

    public SnippetCompletion(CompletionProvider provider, String snippetName, String snippetCode, String description) {
        super(provider);
        this.snippetName = snippetName;
        this.snippetCode = snippetCode;
        this.description = description;
        parse(snippetCode);
    }

    private void parse(String snippetCode) {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < snippetCode.length(); i++)
        {
            char c = snippetCode.charAt(i);
            switch (c){
                case '%':
                    Character next = safeCharAt(snippetCode, i + 1);

                    int consumed = 0;
                    if(next == null) {
                        throw new IllegalArgumentException("Invalid snippet code: " + snippetCode);
                    }
                    else if (next == '%') {
                        sb.append('%');
                    }
                    else if((consumed = consumeNumber(snippetCode, i + 1)) != -1)
                    {
                        i++;
                        int index = 0;
                        try {
                            index = Integer.parseInt(snippetCode.substring(i, i + consumed));
                        } catch (NumberFormatException e) {
                            throw new IllegalArgumentException("[IMPOSSIBLE] Invalid snippet code: " + snippetCode);
                        }
                        Character afterIndex = safeCharAt(snippetCode, i + consumed);
                        boolean isReplacement = false;
                        if(afterIndex != null)
                        {
                            if (afterIndex == '{') {
                                isReplacement = true;
                            }
                        }
                        if(isReplacement)
                        {
                            i += consumed;
                            if((consumed = consumeReplacement(snippetCode, i + 1)) == -1)
                            {
                                throw new IllegalArgumentException("Invalid snippet code: " + snippetCode);
                            }
                            if(!sb.isEmpty()) {
                                segments.add(new SnippetSegment(SnippetSegmentTypes.TEXT, sb.toString()));
                                sb.delete(0, sb.length());
                            }
                            segments.add(new SnippetSegment(SnippetSegmentTypes.TAB_REPLACE, snippetCode.substring(i + 1, i + consumed)));
                            i = i + consumed;
                        }
                        else
                        {
                            if(!sb.isEmpty()) {
                                segments.add(new SnippetSegment(SnippetSegmentTypes.TEXT, sb.toString()));
                                sb.delete(0, sb.length());
                            }
                            segments.add(new SnippetSegment(SnippetSegmentTypes.TAB_POS));
                        }
                    }
                    else
                    {
                        throw new IllegalArgumentException("Invalid snippet code: " + snippetCode);
                    }
                    break;
                    default:
                        sb.append(c);
                        break;
            }
        }

        if(!sb.isEmpty())
        {
            segments.add(new SnippetSegment(SnippetSegmentTypes.TEXT, sb.toString()));
        }
    }

    private int consumeReplacement(String s, int i) {
        int consumed = 0;
        while(Objects.requireNonNullElse(safeCharAt(s, i + consumed), ' ') != '}')
        {
            consumed++;
        }
        return consumed == 0 ? -1 : consumed + 1;
    }

    private int consumeNumber(String s, int i) {
        int consumed = 0;
        while(Character.isDigit(Objects.requireNonNullElse(safeCharAt(s, i + consumed), ' ')))
        {
            consumed++;
        }
        return consumed == 0 ? -1 : consumed;
    }

    private Character safeCharAt(String s, int i) {
        if(i < s.length())
        {
            return s.charAt(i);
        }
        return null;
    }

    @Override
    public String getDefinitionString() {
        return "definition string";
    }

    @Override
    public Parameter getParam(int index) {
        return new Parameter("int", "test", true);
    }

    @Override
    public int getParamCount() {
        return 1;
    }

    @Override
    public ParameterizedCompletionInsertionInfo getInsertionInfo(JTextComponent tc, boolean replaceTabsWithSpaces) {
        ParameterizedCompletionInsertionInfo info = new ParameterizedCompletionInsertionInfo();
        info.addReplacementLocation(5, 8);
        info.setInitialSelection(5,8);
        info.addReplacementCopy("test", 9, 11);
        return info;
    }

    @Override
    public boolean getShowParameterToolTip() {
        return true;
    }

    @Override
    public String getReplacementText() {
        return "test 123 abc";
    }

    @Override
    public String getSummary() {
        return "my summary";
    }
}
