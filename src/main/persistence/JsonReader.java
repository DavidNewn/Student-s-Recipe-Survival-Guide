package persistence;

import model.Recipe;
import model.RecipeList;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Represents a reader that reads a recipe and favourite recipe list from JSON data stored in file
// Template from workroom example
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read JSON data
    public JsonReader(String source) {
        this.source = source;
    }

    public RecipeList read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseRecipeList(jsonObject);
    }

    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    private RecipeList parseRecipeList(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        RecipeList rl = new RecipeList(name);
        addRecipes(rl, jsonObject);
        return rl;
    }

    private void addRecipes(RecipeList rl, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("Recipe");
        for (Object json : jsonArray) {
            JSONObject nextRecipe = (JSONObject) json;
            addRecipe(rl, nextRecipe);
        }
    }

    private void addRecipe(RecipeList rl, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        String category = jsonObject.getString("category");
        String ingredients = jsonObject.getString("ingredients");
        String steps = jsonObject.getString("steps");
        Recipe recipe = new Recipe(name, category, ingredients, steps);

        rl.addRecipe(recipe);
    }

}
