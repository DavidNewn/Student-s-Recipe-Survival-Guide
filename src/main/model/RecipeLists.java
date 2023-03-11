package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

public class RecipeLists implements Writable {
    private String name;
    private RecipeList recipeListMain; // name: "Main Recipes"
    private RecipeListFav recipeListFav; // name: "Favourite Recipes"

    // EFFECTS: constructs an object with the main recipe list and favourite recipe list
    public RecipeLists(String name, RecipeList rl, RecipeListFav rlf) {
        this.name = name;
        recipeListMain = rl;
        recipeListFav = rlf;
    }

    public RecipeList getRecipeList() {
        return recipeListMain;
    }

    public RecipeListFav getRecipeListFav() {
        return recipeListFav;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put(recipeListMain.getName(), recipesToJson(recipeListMain));
        json.put(recipeListFav.getName(), recipesToJson(recipeListFav));
        return json;
    }

    // EFFECTS: returns recipes in a list as a JSON array
    private JSONArray recipesToJson(RecipeList recipeList) {
        JSONArray jsonArray = new JSONArray();

        for (int i = 0;i < recipeList.size();i++) {
            jsonArray.put(recipeList.atIndex(i).toJson());
        }
        return jsonArray;
    }

}
