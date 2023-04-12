package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the default behaviours for a recipe list
 */
public abstract class RecipeListAbstract {
    private String name;
    private ArrayList<Recipe> recipeList;


    public RecipeListAbstract(String name) {
        this.name = name;
        recipeList = new ArrayList<>();
    }

    // REQUIRES: for the recipe to have a name that is not null
    // MODIFIES: this
    // EFFECTS:  adds the recipe to the recipe list
    public void addRecipe(Recipe recipe) {
        recipeList.add(recipe);
    }

    // REQUIRES: for the recipe to be removed exists in the list
    // MODIFIES: this
    // EFFECTS:  removes the recipe from the recipe list
    public void removeRecipe(Recipe recipe) {
        recipeList.remove(recipe);
    }

    // EFFECTS: searches for the given recipe name in the recipe list and returns it
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
