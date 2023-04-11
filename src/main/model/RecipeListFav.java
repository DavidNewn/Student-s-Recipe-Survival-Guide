package model;

/**
 * The favourite list of recipes. Related to main list, so changes to a recipe should be reflected in both lists
 */
public class RecipeListFav extends RecipeList {
    private EventLog log = EventLog.getInstance();

    // EFFECTS: creates a favourite recipe list
    public RecipeListFav(String name) {
        super(name);
    }

    // EFFECTS: adds a recipe to the favourite list. Calls the RecipeList method addRecipe
    public void addRecipe(Recipe recipe) {
        super.addRecipe(recipe);
    }

    // EFFECTS: removes a recipe from the favourite list. Calls the RecipeList method removeRecipe
    public void removeRecipe(Recipe recipe) {
        super.removeRecipe(recipe);
        log.logEvent(new Event("Removed \"" + recipe.getRecipeName() + "\" from the favourite list!"));
    }
}
