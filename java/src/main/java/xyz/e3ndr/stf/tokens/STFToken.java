package xyz.e3ndr.stf.tokens;

import co.casterlabs.rakurai.json.element.JsonObject;

public interface STFToken {

    public void populate(StringBuilder builder, JsonObject vars);

}
