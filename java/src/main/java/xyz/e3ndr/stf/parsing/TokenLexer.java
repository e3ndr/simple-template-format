package xyz.e3ndr.stf.parsing;

import lombok.AllArgsConstructor;
import lombok.ToString;

/*
 * STF Syntax
 * 
 * Escaped Character Blocks
 * [##]
 * [$$]
 * 
 * Accessor Blocks
 * [$variable]
 * 
 * Logic Blocks
 * [#if query] ... [/]
 * [#ifnot query] ... [/]
 * [#each query as key] ... [/]
 */
interface TokenLexer {
    static final char BLOCK_START_TOKEN = '[';
    static final char BLOCK_END_TOKEN = ']';

    static final TokenLexer[] LEXERS = { // In order of priority.
            new BlockCloseLexer(),
            new EscapedDollarLexer(),
            new EscapedPoundLexer(),
            new VariableLexer(),
            new IfLexer(),
            new IfNotLexer(),
            new EachLexer()
    };

    /**
     * without the angle brackets.
     */
    char[] getStartSequence();

    /**
     * @param  startAt the index to start at. Including the start of the token.
     * 
     * @return         the length of the block and the resulting token
     */
    LexIntermediate lex(int startAt, char[] template);

    @ToString
    @AllArgsConstructor
    static class LexIntermediate {
        int length;
        TokenType type;
        String content;
    }

    static enum TokenType {
        VARIABLE,
        TEXT,
        EACH_BLOCK,
        IF_BLOCK,
        IFNOT_BLOCK,
        BLOCK_CLOSE
    }

    abstract class BlockLexer implements TokenLexer {
        protected abstract TokenType intermediateType();

        @Override
        public LexIntermediate lex(int startAt, char[] template) {
            for (int idx = startAt; idx < template.length; idx++) {
                if (template[idx] == BLOCK_END_TOKEN) {
                    int contentOffset = startAt + 1 + this.getStartSequence().length; // +1 for the START_TOKEN
                    int contentLen = idx - contentOffset; // END_TOKEN isn't included
                    String contentString = new String(template, contentOffset, contentLen);

                    int blockLen = idx - startAt;

                    return new LexIntermediate(blockLen, this.intermediateType(), contentString);
                }
            }

            throw new ArrayIndexOutOfBoundsException("Ran out of template before the token ended.");
        }

    }

}
