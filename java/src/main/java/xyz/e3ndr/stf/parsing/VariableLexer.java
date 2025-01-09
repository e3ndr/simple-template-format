package xyz.e3ndr.stf.parsing;

import xyz.e3ndr.stf.parsing.TokenLexer.BlockLexer;

// [$var]
class VariableLexer extends BlockLexer {
    private static final char[] START_SEQ = "$".toCharArray();

    @Override
    public char[] getStartSequence() {
        return START_SEQ;
    }

    @Override
    protected TokenType intermediateType() {
        return TokenType.VARIABLE;
    }

}
