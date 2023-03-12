package persistence;

import model.Recipe;
import model.RecipeList;
import model.RecipeListFav;
import model.RecipeLists;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Represents a reader that reads a recipe and favourite recipe list from JSON data stored in file
 * Template from workroom example
 */
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read JSON data, with the key for Recipe Lists
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads RecipeLists (main and favourite list) from file and returns it
    // throws IOException if an error occurs reading data from file
    public RecipeLists read(String keyRecipeLists, String keyRecipeMain, String keyRecipeFav) throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseRecipeList(jsonObject, keyRecipeLists, keyRecipeMain, keyRecipeFav);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses RecipeLists (main and favourite list) from JSON object and returns it
    // Notes to self:
    // Recipe Lists / jsonObject name: "Recipe Lists"
    // Main recipe list name: "Main Recipes"
    // Favourite recipe list name: "Favourite Recipes"
    private RecipeLists parseRecipeList(JSONObject jsonObject, String keyRecipeLists,
                                        String keyRecipeMain, String keyRecipeFav) {
        RecipeList rl = new RecipeList(keyRecipeMain);
        RecipeListFav rlf = new RecipeListFav(keyRecipeFav);
        addRecipes(rl, jsonObject);
        addRecipes(rlf, jsonObject);

        return new RecipeLists(keyRecipeLists, rl, rlf);
    }

    // EFFECTS: parses each recipe corresponding to its key in the JSON data and adds the recipes into the recipe list
    private void addRecipes(RecipeList rl, JSONObject jsonObject) {
        String key = rl.getName();
        JSONArray jsonArray = jsonObject.getJSONArray(key);
        for (Object json : jsonArray) {
            JSONObject nextRecipe = (JSONObject) json;
            addRecipe(rl, nextRecipe);
        }
    }

    // EFFECTS: parses the recipe's field from the JSON object data and returns the recipe
    private void addRecipe(RecipeList rl, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        String category = jsonObject.getString("category");
        String ingredients = jsonObject.getString("ingredients");
        String steps = jsonObject.getString("steps");
        Recipe recipe = new Recipe(name, category, ingredients, steps);

        rl.addRecipe(recipe);
    }

}
