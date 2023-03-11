package model;

/*
 * The favourite list of recipes. Users add and remove recipes they like from the main list to here,
 * and can search recipes in the favourite list.
 */
public class RecipeListFav extends RecipeList {

    // EFFECTS: creates a favourite recipe list
    public RecipeListFav(String name) {
        super(name);
    }
}
