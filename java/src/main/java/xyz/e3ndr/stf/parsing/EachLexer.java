package xyz.e3ndr.stf.parsing;

import xyz.e3ndr.stf.parsing.TokenLexer.BlockLexer;

// [#each arrayName as itemName]
class EachLexer extends BlockLexer {
    private static final char[] START_SEQ = "#each ".toCharArray();

    @Override
    public char[] getStartSequence() {
        return START_SEQ;
    }

    @Override
    protected TokenType intermediateType() {
        return TokenType.EACH_BLOCK;
    }

}
