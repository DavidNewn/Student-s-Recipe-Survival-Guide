package model;

/**
 * The favourite list of recipes. Related to main list, so changes to a recipe should be reflected in both lists
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
