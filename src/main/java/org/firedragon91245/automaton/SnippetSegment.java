package org.firedragon91245.automaton;

public class SnippetSegment {


    private final SnippetSegmentTypes snippetSegmentTypes;
    private final String s;

    public SnippetSegment(SnippetSegmentTypes snippetSegmentTypes, String s) {
        this.snippetSegmentTypes = snippetSegmentTypes;
        this.s = s;
    }

    public SnippetSegment(SnippetSegmentTypes snippetSegmentTypes) {
        this.snippetSegmentTypes = snippetSegmentTypes;
        this.s = null;
    }
}
