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
    private EventLog log = EventLog.getInstance();

    // EFFECTS: recipe with a name, category, ingredients, steps, and an id
    public Recipe(String name, String category, String ingredients, String steps) {
        recipeName = name;
        this.category = category;
        this.ingredients = ingredients;
        this.steps = steps;
        log.logEvent(new Event("Created a New Recipe: " + name)); // First logs 5 default recipes
    }

    // MODIFIES: this, log
    // EFFECTS: changes the name of the recipe
    public String changeRecipeName(String name) {
        if (!recipeName.equals(name)) {
            log.logEvent(new Event("Changed recipe name from \"" + recipeName + "\" to \"" + name + "\""));
        }
        return recipeName = name;
    }

    // MODIFIES: this, log
    // EFFECTS: changes the category of the recipe
    public String changeCategory(String category) {
        if (!this.category.equals(category)) {
            log.logEvent(new Event("Changed category from \"" + this.category + "\" to \""
                    + category + "\""));
        }
        return this.category = category;
    }

    // MODIFIES: this, log
    // EFFECTS: changes the ingredients of the recipe
    public String changeIngredients(String ingredients) {
        if (!this.ingredients.equals(ingredients)) {
            log.logEvent(new Event("Changed ingredients from \"" + this.ingredients + "\" to \""
                    + ingredients + "\""));
        }
        return this.ingredients = ingredients;
    }

    // MODIFIES: this, log
    // EFFECTS: changes the steps of the recipe
    public String changeSteps(String steps) {
        if (!this.steps.equals(steps)) {
            log.logEvent(new Event("Changed recipe steps for: " + recipeName));
        }
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
