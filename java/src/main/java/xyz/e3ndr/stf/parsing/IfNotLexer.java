package xyz.e3ndr.stf.parsing;

import xyz.e3ndr.stf.parsing.TokenLexer.BlockLexer;

// [#ifnot var]
class IfNotLexer extends BlockLexer {
    private static final char[] START_SEQ = "#ifnot ".toCharArray();

    @Override
    public char[] getStartSequence() {
        return START_SEQ;
    }

    @Override
    protected TokenType intermediateType() {
        return TokenType.IFNOT_BLOCK;
    }

}
