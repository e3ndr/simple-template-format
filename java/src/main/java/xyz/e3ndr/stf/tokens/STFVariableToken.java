package xyz.e3ndr.stf.tokens;

import co.casterlabs.rakurai.json.element.JsonElement;
import co.casterlabs.rakurai.json.element.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import xyz.e3ndr.stf.JsonQuery;

@ToString
@RequiredArgsConstructor
public class STFVariableToken implements STFToken {
    private final JsonQuery query;

    @Override
    public void populate(StringBuilder builder, JsonObject vars) {
        builder.append(this.resolve(vars));
    }

    private String resolve(JsonObject vars) {
        JsonElement value = this.query.resolve(vars);

        if (value == null) {
            return "undefined";
        } else if (value.isJsonObject()) {
            return "[object]";
        } else if (value.isJsonArray()) {
            return "[array]";
        } else if (value.isJsonString()) {
            return value.getAsString();
        } else {
            return value.toString();
        }
    }

}
