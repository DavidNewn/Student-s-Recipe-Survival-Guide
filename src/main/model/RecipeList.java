package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The main list of recipes. Users can add, remove, and search for recipes in this list.
 */
public class RecipeList implements Writable {
    private String name;
    private ArrayList<Recipe> recipeList;

    // EFFECTS: creates the main recipe list
    public RecipeList(String name) {
        this.name = name;
        recipeList = new ArrayList<>();
    }

    // !!! CHANGE: only allow one reference of the same name in either list.
    // !!! Warn users of this and give option to change name
    // REQUIRES: assume that the recipe list doesn't already contain the same recipe
    // MODIFIES: this
    // EFFECTS: adds recipe into the recipe list

    public void addRecipe(Recipe recipe) {
        recipeList.add(recipe);
    }

    // !!! BUG: deleting reference from main list does not delete from favs
    // REQUIRES: the recipe list to not be empty
    // MODIFIES: this
    // EFFECTS: removes recipe from the recipe list
    public void removeRecipe(Recipe recipe) {
        recipeList.remove(recipe);
        System.out.println(recipe.getRecipeName() + " has been deleted from the main list");
    }

    // REQUIRES: the recipe list to not be empty
    // EFFECTS: searches the first recipe of the same name in the list, else returns null
    public Recipe searchRecipe(String name) {
        for (Recipe recipe : this.recipeList) {
            if (recipe.getRecipeName().equalsIgnoreCase(name.toLowerCase())) {
                return recipe;
            }
        }
        return null;
    }

    public int size() {
        return recipeList.size();
    }

    public Recipe atIndex(int i) {
        return recipeList.get(i);
    }

    // EFFECTS: returns an unmodifiable list of recipe list
    public List<Recipe> getRecipeListCollection() {
        return Collections.unmodifiableList(recipeList);
    }

    public String getName() {
        return name;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("Recipe", thingiesToJson());
        return json;
    }

    // EFFECTS: returns things in this workroom as a JSON array
    private JSONArray thingiesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Recipe r : recipeList) {
            jsonArray.put(r.toJson());
        }

        return jsonArray;
    }

}
