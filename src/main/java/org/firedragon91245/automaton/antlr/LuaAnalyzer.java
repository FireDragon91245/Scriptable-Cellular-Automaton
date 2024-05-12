package org.firedragon91245.automaton.antlr;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.firedragon91245.automaton.antlr.generated.LuaLexer;
import org.firedragon91245.automaton.antlr.generated.LuaParser;

public class LuaAnalyzer {

    public void Analyze(String code)
    {
        LuaLexer lexer = new LuaLexer(CharStreams.fromString(code));
        LuaParser parser = new LuaParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.chunk();

        tree.accept(new LuaAnalyzerVisitor());
    }
}
