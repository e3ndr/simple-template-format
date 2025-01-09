package xyz.e3ndr.stf.parsing;

import xyz.e3ndr.stf.parsing.TokenLexer.BlockLexer;

//[#if var]
class IfLexer extends BlockLexer {
    private static final char[] START_SEQ = "#if ".toCharArray();

    @Override
    public char[] getStartSequence() {
        return START_SEQ;
    }

    @Override
    protected TokenType intermediateType() {
        return TokenType.IF_BLOCK;
    }

}
