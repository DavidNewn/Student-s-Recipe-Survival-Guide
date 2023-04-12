package model;

/**
 * The main list of recipes.
 * Users can add, remove, and search for recipes in this list.
 */

// TODO:
//  Change so removal of recipe in main list reflects on favourite list if recipe is also there.
public class RecipeList extends RecipeListAbstract {
    private EventLog log = EventLog.getInstance();

    // EFFECTS: creates the main recipe list
    public RecipeList(String name) {
        super(name);
    }

    @Override
    // REQUIRES: the recipe list doesn't already contain the same recipe
    // MODIFIES: this
    // EFFECTS:  adds recipe into the recipe list
    public void addRecipe(Recipe recipe) {
        super.addRecipe(recipe);
        log.logEvent(new Event("Added \"" + recipe.getRecipeName() + "\" into the main list!"));
    }

    @Override
    // !!! BUG: deleting recipe from main list does not delete from favs
    // REQUIRES: the recipe list to not be empty, assume recipe to be removed != null
    // MODIFIES: this
    // EFFECTS:  removes recipe from the recipe list
    public void removeRecipe(Recipe recipe) {
        super.removeRecipe(recipe);
        log.logEvent(new Event("Removed \"" + recipe.getRecipeName() + "\" from the main list!"));
    }
}
