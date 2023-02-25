package model;

import java.util.ArrayList;

public class RecipeList {
    private ArrayList<Recipe> recipeList;

    // EFFECTS: creates the main recipe list
    public RecipeList() {
        recipeList = new ArrayList<>();
    }

    // REQUIRES: assume that the recipe list doesn't already contain the same recipe
    // MODIFIES: this
    // EFFECTS: adds recipe into the recipe list
    public void addRecipe(Recipe recipe) {
        recipeList.add(recipe);
    }

    // REQUIRES: the recipe list to not be empty
    // MODIFIES: this
    // EFFECTS: removes recipe from the recipe list
    public void removeRecipe(Recipe recipe) {
        recipeList.remove(recipe);
    }

    // REQUIRES: the recipe list to not be empty
    // EFFECTS: gets the first recipe of the same name in the list, else returns null
    public Recipe getRecipe(String name) {
        for (Recipe recipe : this.recipeList) {
            if (recipe.getRecipeName().equalsIgnoreCase(name.toLowerCase())) {
                return recipe;
            }
        }
        return null;
    }

    // REQUIRES: the recipe list to not be empty
    // EFFECTS: gets a list of recipes of the same category in the list, else return not found
    public RecipeList getRecipeCategory(String category) {
        RecipeList sameCategoryRecipe = new RecipeList();
        for (Recipe recipe : recipeList) {
            if (recipe.getCategory().equalsIgnoreCase(category.toLowerCase())) {
                sameCategoryRecipe.addRecipe(recipe);
            }
        }
        return sameCategoryRecipe;
    }

}
