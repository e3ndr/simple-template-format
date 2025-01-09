package xyz.e3ndr.stf.tokens;

import java.util.List;

import co.casterlabs.rakurai.json.element.JsonElement;
import co.casterlabs.rakurai.json.element.JsonObject;
import lombok.ToString;
import xyz.e3ndr.stf.JsonQuery;

@ToString
public class STFEachToken extends STFCompositeToken {
    private final JsonQuery query;
    private final String keyName;

    public STFEachToken(JsonQuery query, String keyName, List<STFToken> tokens) {
        super(tokens);
        this.query = query;
        this.keyName = keyName;
    }

    @Override
    public void populate(StringBuilder builder, JsonObject vars) {
        JsonElement element = this.query.resolve(vars);

        if (!element.isJsonArray()) {
            throw new IllegalStateException("Expected JsonArray, got " + element.getClass().getSimpleName());
        }

        for (JsonElement item : element.getAsArray()) {
            vars.put(this.keyName, item);
            for (STFToken token : this.tokens) {
                token.populate(builder, vars);
            }
        }

        vars.remove(this.keyName);
    }

}
