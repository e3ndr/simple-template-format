package xyz.e3ndr.stf.parsing;

// [$$]
class EscapedDollarLexer implements TokenLexer {
    private static final char[] START_SEQ = ("$$" + BLOCK_END_TOKEN).toCharArray();

    @Override
    public char[] getStartSequence() {
        return START_SEQ;
    }

    @Override
    public LexIntermediate lex(int startAt, char[] template) {
        // 1 for the START_TOKEN
        return new LexIntermediate(1 + START_SEQ.length, TokenType.TEXT, "$");
    }

}
