package xyz.e3ndr.stf;

import co.casterlabs.rakurai.json.Rson;
import co.casterlabs.rakurai.json.element.JsonElement;
import co.casterlabs.rakurai.json.element.JsonObject;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JsonQuery {
    final String[] parts;

    public static JsonQuery parse(String path) {
        return new JsonQuery(path.split("\\."));
    }

    @Override
    public String toString() {
        return String.join(".", this.parts);
    }

    /**
     * @return a null value if the query cannot be resolved, a JsonNull if the value
     *         is actually null, or a JsonElement otherwise.
     */
    public JsonElement resolve(JsonObject obj) {
        // Step through each part of the query, if we encounter string value before the
        // end of the query then return "undefined" Otherwise, get the value of the
        // current part and look up the next part of the query.
        for (int i = 0; i < this.parts.length - 1; i++) {
            String part = this.parts[i];
            JsonElement sub = obj.get(part);

            if (sub == null) {
                return null;
            } else if (sub.isJsonObject()) {
                obj = sub.getAsObject();
            } else {
                return null;
            }
        }

        String finalPart = this.parts[this.parts.length - 1];

        return obj.get(finalPart);
    }

    public void insertInto(JsonObject obj, Object value) {
        for (int i = 0; i < this.parts.length - 1; i++) {
            String part = this.parts[i];
            JsonElement sub = obj.get(part);

            if (sub == null) {
                JsonObject fresh = new JsonObject();
                obj.put(part, fresh);
                obj = fresh;
                continue;
            } else if (sub.isJsonObject()) {
                obj = sub.getAsObject();
                continue;
            } else {
                throw new IllegalStateException(
                    "A non-object value is present at location " + part + " of " + this + ". "
                        + "We expect either no value or a mapping to be present at this location."
                );
            }
        }

        JsonElement valueAsJson = Rson.DEFAULT.toJson(value);

        String finalPart = this.parts[this.parts.length - 1];
        obj.put(finalPart, valueAsJson);
    }

}
