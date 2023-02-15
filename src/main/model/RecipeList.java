package model;

import java.util.ArrayList;

public class RecipeList {
    private ArrayList<Recipe> recipelist;
    private ArrayList<Recipe> favouritelist;

    // EFFECTS: create two lists of recipes
    public RecipeList() {
        recipelist = new ArrayList<Recipe>();
        favouritelist = new ArrayList<Recipe>();
    }

    // MODIFIES: this
    // EFFECTS: adds recipe into the recipe list
    public void addRecipe() {
        //stub;
    }

    // REQUIRES: the recipe list to not be empty
    // MODIFIES: this
    // EFFECTS: removes recipe from the recipe list
    public void removeRecipe() {
        //stub;
    }

    // REQUIRES: the recipe list to not be empty
    // EFFECTS: gets the recipe of the same name in the list, else returns "not found"
    public void getRecipeName(RecipeList list, String name) {
        //stub;
    }

    // REQUIRES: the recipe list to not be empty
    // EFFECTS: gets a list of recipes of the same category in the list, else returns "not found"
    public void getRecipeCategory(RecipeList list, String category) {
        //stub;
    }

    // MODIFIES: this
    // EFFECTS: adds recipe into the favourite recipe list
    public void addFavouriteRecipe() {
        //stub;
    }

    // REQUIRES: favourite recipe list to not be empty
    // MODIFIES: this
    // EFFECTS: remove recipe from the favourite recipe list
    public void removeFavouriteRecipe() {
        //stub;
    }
}
