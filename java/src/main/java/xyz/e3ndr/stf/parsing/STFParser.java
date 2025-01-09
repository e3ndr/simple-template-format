package xyz.e3ndr.stf.parsing;

import java.util.LinkedList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.ToString;
import xyz.e3ndr.stf.JsonQuery;
import xyz.e3ndr.stf.SimpleTemplate;
import xyz.e3ndr.stf.parsing.TokenLexer.LexIntermediate;
import xyz.e3ndr.stf.parsing.TokenLexer.TokenType;
import xyz.e3ndr.stf.tokens.STFCompositeToken;
import xyz.e3ndr.stf.tokens.STFEachToken;
import xyz.e3ndr.stf.tokens.STFIfToken;
import xyz.e3ndr.stf.tokens.STFTextToken;
import xyz.e3ndr.stf.tokens.STFToken;
import xyz.e3ndr.stf.tokens.STFVariableToken;

public class STFParser {

    static boolean matchSeq(char[] startSeq, int off, char[] chars) {
        for (int seqIdx = 0; seqIdx < startSeq.length; seqIdx++) {
            if (chars[off + seqIdx] != startSeq[seqIdx]) {
                return false;
            }
        }
        return true;
    }

    private static List<LexIntermediate> parse(int toff, int tlen, char[] tchars) {
        List<LexIntermediate> tokens = new LinkedList<>();

        StringBuilder textBuffer = new StringBuilder();
        char_loop: for (int idx = toff; idx < tlen; idx++) {
            char ch = tchars[idx];

            if (ch != TokenLexer.BLOCK_START_TOKEN) {
                textBuffer.append(ch);
                continue char_loop;
            }

            // Loop through the lexers and try to match by start sequence. Otherwise
            // continue through.
            lex_check: for (TokenLexer lex : TokenLexer.LEXERS) {
                char[] startSeq = lex.getStartSequence();
                if (!matchSeq(startSeq, idx + 1, tchars)) { // +1 to account for the START_TOKEN
                    continue lex_check;
                }

                // It matches! Let's flush the text buffer if it has something in it.
                if (textBuffer.length() > 0) {
                    tokens.add(new LexIntermediate(textBuffer.length(), TokenType.TEXT, textBuffer.toString()));
                    textBuffer.setLength(0);
                }

                // Let the lexer do it's thing.
                LexIntermediate result = lex.lex(idx, tchars);
                idx += result.length;
                tokens.add(result);
                continue char_loop; // Skip the sb append.
            }

            // No matches, append to the stringBuilder.
            textBuffer.append(ch);
        }

        if (textBuffer.length() > 0) {
            tokens.add(new LexIntermediate(textBuffer.length(), TokenType.TEXT, textBuffer.toString()));
        }

        return tokens;
    }

    @ToString
    @AllArgsConstructor
    private static class BlockParseResult {
        int skip;
        List<STFToken> tokens;
    }

    private static BlockParseResult parseBlock(List<LexIntermediate> intermediates, int startAt, int blockLevel) {
        List<STFToken> result = new LinkedList<>();

        for (int idx = startAt; idx < intermediates.size(); idx++) {
            LexIntermediate intermediate = intermediates.get(idx);
            switch (intermediate.type) {
                case BLOCK_CLOSE:
                    if (blockLevel == 0) {
                        throw new IllegalStateException("Unexpected block close token.");
                    }
                    return new BlockParseResult(idx - startAt + 1, result); // +1 to account for the BLOCK_CLOSE

                case EACH_BLOCK: { // #each query as var
                    BlockParseResult blockContents = parseBlock(intermediates, idx + 1, blockLevel + 1);

                    // Split the intermediate content into query and var:
                    String[] split = intermediate.content.split(" as ");
                    JsonQuery query = JsonQuery.parse(split[0]);
                    String var = split[1];

                    STFEachToken eachBlock = new STFEachToken(query, var, blockContents.tokens);
                    result.add(eachBlock);
                    idx += blockContents.skip;
                    break;
                }

                case IFNOT_BLOCK:
                case IF_BLOCK: { // #if query, #ifnot query
                    BlockParseResult blockContents = parseBlock(intermediates, idx + 1, blockLevel + 1);
                    JsonQuery query = JsonQuery.parse(intermediate.content);
                    result.add(new STFIfToken(query, intermediate.type == TokenType.IFNOT_BLOCK, blockContents.tokens));
                    idx += blockContents.skip;
                    break;
                }

                case TEXT:
                    result.add(new STFTextToken(intermediate.content));
                    break;

                case VARIABLE: { // $variable
                    JsonQuery query = JsonQuery.parse(intermediate.content);
                    result.add(new STFVariableToken(query));
                    break;
                }
            }
        }

        if (blockLevel != 0) {
            throw new IllegalStateException("Expected block close token.");
        }

        return new BlockParseResult(intermediates.size() - startAt, result);
    }

    public static SimpleTemplate parse(String template) {
        List<LexIntermediate> intermediates = parse(0, template.length(), template.toCharArray());
        BlockParseResult result = parseBlock(intermediates, 0, 0);

        return new SimpleTemplate(new STFCompositeToken(result.tokens));
    }

}
