package org.firedragon91245.automaton;

public class SnippetSegment {


    private final SnippetSegmentTypes snippetSegmentTypes;
    private String s;

    public SnippetSegment(SnippetSegmentTypes snippetSegmentTypes, String s) {
        this.snippetSegmentTypes = snippetSegmentTypes;
        this.s = s;
    }

    public SnippetSegment(SnippetSegmentTypes snippetSegmentTypes) {
        this.snippetSegmentTypes = snippetSegmentTypes;
        this.s = null;
    }

    public SnippetSegmentTypes getType() {
        return snippetSegmentTypes;
    }

    public String getText() {
        return s;
    }

    public int getTextLength() {
        return s != null ? s.length() : 0;
    }

    public SnippetSegment copy() {
        return new SnippetSegment(snippetSegmentTypes, s);
    }

    public SnippetSegment setText(String text) {
        this.s = text;
        return this;
    }
}
