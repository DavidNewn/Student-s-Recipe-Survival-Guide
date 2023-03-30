package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The main list of recipes. Users can add, remove, and search for recipes in this list.
 */

// TODO:
//  CHANGE: only allow one reference of the same recipe name in either list.
//  Warn users of this and give option to change name
public class RecipeList {
    private String name;
    private ArrayList<Recipe> recipeList;

    // EFFECTS: creates the main recipe list with a name and an arrayList
    public RecipeList(String name) {
        this.name = name;
        recipeList = new ArrayList<>();
    }

    // REQUIRES: assume that the recipe list doesn't already contain the same recipe
    // MODIFIES: this
    // EFFECTS: adds recipe into the recipe list
    public void addRecipe(Recipe recipe) {
        recipeList.add(recipe);
    }

    // !!! BUG: deleting reference from main list does not delete from favs
    // REQUIRES: the recipe list to not be empty, assume recipe to be removed != null
    // MODIFIES: this
    // EFFECTS: removes recipe from the recipe list
    public void removeRecipe(Recipe recipe) {
        recipeList.remove(recipe);
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

    // EFFECTS: returns the recipe at index (first recipe at 0)
    public Recipe atIndex(int i) {
        return recipeList.get(i);
    }

    // EFFECTS: returns a modifiable list of recipe list
    public List<Recipe> getRecipeList() {
        return this.recipeList;
    }

    // EFFECTS: returns an unmodifiable list of recipe list. Mainly for JSONWriter to save to JSON file
    public List<Recipe> getRecipeListCollection() {
        return Collections.unmodifiableList(recipeList);
    }

    // EFFECTS: returns size of the recipe list
    public int size() {
        return recipeList.size();
    }

    public String getName() {
        return name;
    }
}
