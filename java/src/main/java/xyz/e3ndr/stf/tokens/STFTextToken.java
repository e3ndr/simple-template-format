package xyz.e3ndr.stf.tokens;

import co.casterlabs.rakurai.json.element.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor
public class STFTextToken implements STFToken {
    private final String value;

    @Override
    public void populate(StringBuilder builder, JsonObject vars) {
        builder.append(this.value);
    }

}
