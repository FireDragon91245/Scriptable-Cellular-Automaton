package org.firedragon91245.automaton.antlr;

import org.firedragon91245.automaton.antlr.generated.LuaParser;
import org.firedragon91245.automaton.antlr.generated.LuaParserBaseVisitor;

public class LuaAnalyzerVisitor extends LuaParserBaseVisitor<Void> {

    @Override
    public Void visitFuncname(LuaParser.FuncnameContext ctx) {
        super.visitFuncname(ctx);
        System.out.println("Function name: " + ctx.getText());
        return Void.TYPE.cast(null);
    }
}
