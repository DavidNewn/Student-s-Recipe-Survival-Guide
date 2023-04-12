package model;

/**
 * The favourite list of recipes.
 * Related to main list, so changes to a recipe should be reflected in both lists
 * (intent; not fully implemented yet)
 */
public class RecipeListFav extends RecipeListAbstract {
    private EventLog log = EventLog.getInstance();

    // EFFECTS: creates the favourite recipe list
    public RecipeListFav(String name) {
        super(name);
    }

    @Override
    // REQUIRES: recipe to have a name that is not null
    // MODIFIES: this
    // EFFECTS:  adds a recipe to the favourite list
    public void addRecipe(Recipe recipe) {
        super.addRecipe(recipe);
        log.logEvent(new Event("Added \"" + recipe.getRecipeName() + "\" into the favourite list!"));
    }

    @Override
    // REQUIRES: recipe to be in the recipe list
    // MODIFIES: this
    // EFFECTS:  removes a recipe from the favourite list
    public void removeRecipe(Recipe recipe) {
        super.removeRecipe(recipe);
        log.logEvent(new Event("Removed \"" + recipe.getRecipeName() + "\" from the favourite list!"));
    }
}
