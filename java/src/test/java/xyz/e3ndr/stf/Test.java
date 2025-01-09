package xyz.e3ndr.stf;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import co.casterlabs.rakurai.json.element.JsonArray;
import co.casterlabs.rakurai.json.element.JsonObject;
import xyz.e3ndr.stf.parsing.STFParser;

public class Test {

    public static void main(String[] args) throws IOException {
        JsonObject vars = new JsonObject()
            .put("someValue", true)
            .put("year", 1234)
            .put(
                "articles",
                JsonArray.of(
                    new JsonObject()
                        .put("title", "Hello world")
                        .put("slug", "hello-world")
                        .put("authors", JsonArray.of("John Doe", "Jane Doe"))
                        .put("summary", "This is a test.")
                )
            );

        String input = Files.readString(new File("../demo.stf").toPath());

        STFParser.parse(input); // Warm-up the JVM.

        long parseStart = System.nanoTime();
        SimpleTemplate template = STFParser.parse(input);
        long parseEnd = System.nanoTime();

        template.render(vars); // Another warm-up.

        long renderStart = System.nanoTime();
        String result = template.render(vars);
        long renderEnd = System.nanoTime();

        System.out.println(result);
        System.out.println();
        System.out.println();
        System.out.printf("Parse took: %fms\n", (parseEnd - parseStart) / 1e+6);
        System.out.printf("Render took: %fms\n", (renderEnd - renderStart) / 1e+6);
    }

}
