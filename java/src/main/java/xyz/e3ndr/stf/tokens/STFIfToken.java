package xyz.e3ndr.stf.tokens;

import java.util.List;

import co.casterlabs.rakurai.json.element.JsonElement;
import co.casterlabs.rakurai.json.element.JsonObject;
import lombok.ToString;
import xyz.e3ndr.stf.JsonQuery;

@ToString
public class STFIfToken extends STFCompositeToken {
    private final JsonQuery query;
    private final boolean invert;

    public STFIfToken(JsonQuery query, boolean invert, List<STFToken> tokens) {
        super(tokens);
        this.query = query;
        this.invert = invert;
    }

    @Override
    public void populate(StringBuilder builder, JsonObject vars) {
        JsonElement element = this.query.resolve(vars);

        if (!element.isJsonBoolean()) {
            throw new IllegalStateException("Expected JsonBoolean, got " + element.getClass().getSimpleName());
        }

        if (element.getAsBoolean() != this.invert) {
            for (STFToken token : this.tokens) {
                token.populate(builder, vars);
            }
        }
    }

}
