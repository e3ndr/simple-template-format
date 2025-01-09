package xyz.e3ndr.stf.tokens;

import java.util.List;

import co.casterlabs.rakurai.json.element.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor
public class STFCompositeToken implements STFToken {
    protected final List<STFToken> tokens;

    @Override
    public void populate(StringBuilder builder, JsonObject vars) {
        for (STFToken token : this.tokens) {
            token.populate(builder, vars);
        }
    }

}
