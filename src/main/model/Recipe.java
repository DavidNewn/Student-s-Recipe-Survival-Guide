package model;

import org.json.JSONObject;
import persistence.Writable;

/**
 * The Recipe class, representing a recipe with a name, category, ingredients, and steps.
 */

// TODO:
//  Change ingredients into either a list of string or ingredients class
//  Change steps into either a list of string or steps class
//  Maybe use a hashmap for ingredients
public class Recipe implements Writable {
    private String recipeName; //name of the recipe
    private String category;   //category of the recipe e.g. vegetarian, vegan, seafood
    private String ingredients;//the ingredients needed for the recipe
    private String steps;      //the steps for the recipe

    // EFFECTS: recipe with a name, category, ingredients, steps, and an id
    public Recipe(String name, String category, String ingredients, String steps) {
        recipeName = name;
        this.category = category;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    // MODIFIES: this
    // EFFECTS: changes the name of the recipe
    public String changeRecipeName(String name) {
        return recipeName = name;
    }

    // MODIFIES: this
    // EFFECTS: changes the category of the recipe
    public String changeCategory(String category) {
        return this.category = category;
    }

    // MODIFIES: this
    // EFFECTS: changes the ingredients of the recipe
    public String changeIngredients(String ingredients) {
        return this.ingredients = ingredients;
    }

    // MODIFIES: this
    // EFFECTS: changes the steps of the recipe
    public String changeSteps(String steps) {
        return this.steps = steps;
    }

    public String getRecipeName() {
        return this.recipeName;
    }

    public String getCategory() {
        return this.category;
    }

    public String getIngredients() {
        return this.ingredients;
    }

    public String getSteps() {
        return this.steps;
    }

    // EFFECTS: creates and returns a JSONObject representing a recipe with its fields
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", recipeName);
        json.put("category", category);
        json.put("ingredients", ingredients);
        json.put("steps", steps);
        return json;
    }

    // EFFECTS: returns recipe name for RecipePanel
    @Override
    public String toString() {
        return recipeName;
    }
}
