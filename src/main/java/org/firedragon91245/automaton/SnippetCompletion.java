package org.firedragon91245.automaton;

import org.fife.ui.autocomplete.AbstractCompletion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.ParameterizedCompletion;
import org.fife.ui.autocomplete.ParameterizedCompletionInsertionInfo;
import org.fife.ui.rsyntaxtextarea.RSyntaxUtilities;

import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.Position;
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
    List<Parameter> parameters = new ArrayList<>();

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
                            String name = snippetCode.substring(i + 1, i + consumed);
                            segments.add(new SnippetSegment(SnippetSegmentTypes.TAB_REPLACE, name));
                            parameters.add(new Parameter(null, name));
                            i = i + consumed;
                        }
                        else
                        {
                            if(!sb.isEmpty()) {
                                segments.add(new SnippetSegment(SnippetSegmentTypes.TEXT, sb.toString()));
                                sb.delete(0, sb.length());
                            }
                            segments.add(new SnippetSegment(SnippetSegmentTypes.TAB_POS));
                            parameters.add(new Parameter(null, "Tab " + index));
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
        return snippetName;
    }

    @Override
    public Parameter getParam(int index) {
        return parameters.get(index);
    }

    @Override
    public int getParamCount() {
        return parameters.size();
    }

    @Override
    public ParameterizedCompletionInsertionInfo getInsertionInfo(JTextComponent tc, boolean replaceTabsWithSpaces) {
        ParameterizedCompletionInsertionInfo info = new ParameterizedCompletionInsertionInfo();

        StringBuilder sb = new StringBuilder();

        int dot = tc.getCaretPosition();

        String indent = null;
        try {
            indent = RSyntaxUtilities.getLeadingWhitespace(tc.getDocument(), dot);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }

        List<SnippetSegment> segmentsIncludingIndent = addIndents(indent, segments);

        int currentIndex = 0;
        int firstCursor = -1;
        //int lastCursor = 0;

        int firstRegion = 0;
        int firstRegionLength = 0;

        for(SnippetSegment segment : segmentsIncludingIndent)
        {
            switch (segment.getType())
            {
                case TEXT:
                    sb.append(segment.getText());
                    currentIndex += segment.getTextLength();
                    break;
                case TAB_POS:
                    if(firstCursor == -1)
                    {
                        firstCursor = currentIndex;
                        firstRegion = currentIndex;
                        firstRegionLength = 0;
                    }
                    info.addReplacementLocation(currentIndex + dot, currentIndex + dot);
                    //lastCursor = currentIndex;
                    break;
                case TAB_REPLACE:
                    if(firstCursor == -1)
                    {
                        firstCursor = currentIndex;
                        firstRegion = currentIndex;
                        firstRegionLength = segment.getTextLength();
                    }
                    sb.append(segment.getText());
                    info.addReplacementLocation(currentIndex + dot, currentIndex + segment.getTextLength() + dot);
                    currentIndex += segment.getTextLength();
                    //lastCursor = currentIndex;
                    break;
            }
        }

        try {
            info.setCaretRange(firstCursor + dot, tc.getDocument().createPosition(dot + sb.length()));
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }

        info.setDefaultEndOffs(sb.length());
        info.setInitialSelection(firstRegion + dot, firstRegion + firstRegionLength + dot);
        info.setTextToInsert(sb.toString());

        return info;
    }

    private List<SnippetSegment> addIndents(String indent, List<SnippetSegment> segments) {
        if(indent == null || indent.isEmpty())
        {
            return segments;
        }

        List<SnippetSegment> newSegments = new ArrayList<>();
        for(SnippetSegment segment : segments)
        {
            if(segment.getType() == SnippetSegmentTypes.TEXT)
            {
                newSegments.add(segment.copy().setText(segment.getText().replace("\n", "\n" + indent)));
            }
            else
            {
                newSegments.add(segment);
            }
        }
        return newSegments;
    }

    @Override
    public boolean getShowParameterToolTip() {
        return false;
    }

    @Override
    public String getReplacementText() {
        return null;
    }

    @Override
    public String getSummary() {
        return description;
    }

    @Override
    public String toString() {
        return snippetName;
    }

    @Override
    public String getInputText() {
        return snippetName;
    }
}
