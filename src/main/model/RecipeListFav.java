package model;

/**
 * The favourite list of recipes. Related to main list, so changes to recipes themselves should be reflected in the
 * favourite list
 */
public class RecipeListFav extends RecipeList {

    // EFFECTS: creates a favourite recipe list
    public RecipeListFav(String name) {
        super(name);
    }

    // EFFECTS: adds a recipe to the favourite list. Calls the RecipeList method addRecipe
    public void addRecipe(Recipe recipe) {
        super.addRecipe(recipe);
    }
}
