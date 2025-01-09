package xyz.e3ndr.stf;

import co.casterlabs.rakurai.json.Rson;
import co.casterlabs.rakurai.json.element.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import xyz.e3ndr.stf.tokens.STFCompositeToken;

@ToString
@RequiredArgsConstructor
public class SimpleTemplate {
    private final STFCompositeToken root;

    public String render(Object vars) {
        JsonObject varsAsJson = (JsonObject) Rson.DEFAULT.toJson(vars);

        StringBuilder builder = new StringBuilder();
        this.root.populate(builder, varsAsJson);
        return builder.toString();
    }

}
