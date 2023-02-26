package ui;

import model.Recipe;
import model.RecipeListFav;
import model.RecipeList;

import java.util.Scanner;

/*
 * The interface which users engage with recipes
 * Users can do the following in the RecipeApp:
 * - Add a new recipe in the main list, or add an existing recipe in the main list to the favourite list.
 * - Remove or edit recipes in either list
 * - Search for recipes in either the main or favourite list by name
 * - Print out an individual recipe in detail, or print out the list of names of recipes in both list.
 */
public class RecipeApp {
    private RecipeList recipeList;
    private RecipeListFav recipeListFav;
    private Scanner input;

    // Default recipe list:
    protected Recipe recipe1 = new Recipe("Pasta Primavera","Vegetarian","Pasta, Veggies",
            "1. Boil pasta. 2. Add veggies. 3. Serve hot");
    protected Recipe recipe2 = new Recipe("Minestrone Soup","Soup","Veggies",
            "1. Add chopped veggies to boiling water. 2. When veggies are soft, serve");
    protected Recipe recipe3 = new Recipe("Vichyssoise", "Soup", "Leeks, Potaotes, Heavy Cream",
            "1. Fry chopped leeks and potatoes. Do not brown. 2. Add in broth to just fill chopped veggies. "
                   + "3. When veggies are tender, blend or mash, then add heavy cream");
    protected Recipe recipe4 = new Recipe("Fried Rice", "Rice", "Rice, Leftover Veggies or "
            + "Meats", "1. Add rice in a pan with some oil. Fry for a bit. 2. Add in leftovers. 3. Serve hot");
    protected Recipe recipe5 = new Recipe("Smoked Paprika Curry Sauce","Sauce",
            "Butter, Onions, Paprika, Tomato Paste, Heavy Cream",
            "1. Combine chopped with melted butter in a pot. Fry onions until they're soft "
            + "2.Add in water, let it boil and reduce to half. 3. Turn off heat and add in tomato paste."
            + "4. Add in heavy cream and heat it up to temperature. Mix well.");

    // EFFECTS: runs the recipe application
    public RecipeApp() {
        runRecipeApp();
    }

    // MODIFIES: this
    // EFFECTS: process inputs from user
    // Template from TellerApp
    private void runRecipeApp() {
        boolean run = true;
        String command = null;

        init();

        while (run) {
            displayMenu();
            command = input.nextLine();
            command = command.toLowerCase();

            if (command.equals("q")) {
                run = false;
            } else {
                processCommand(command);
            }
        }

        System.out.println("\nSee You!");
    }

    // MODIFIES: this
    // EFFECTS: process user command
    private void processCommand(String command) {
        if (command.equals("printall")) {
            printAllRecipeNames();
        } else if (command.equals("s")) {
            processSearchRecipeMain();
        } else if (command.equals("f")) {
            processSearchRecipeFav();
        } else if (command.equals("a")) {
            recipeList.addRecipe(makeNewRecipe());
        } else {
            System.out.println("Invalid input, try again");
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes recipe list and favourite recipe list
    private void init() {
        recipeList = new RecipeList();
        recipeListFav = new RecipeListFav();
        input = new Scanner(System.in);
        input.useDelimiter("/n");

        recipeList.addRecipe(recipe1); // Pasta Primavera
        recipeList.addRecipe(recipe2); // Minestrone Soup
        recipeList.addRecipe(recipe3); // Vichyssoise
        recipeList.addRecipe(recipe4); // Fried Rice
        recipeList.addRecipe(recipe5); // Smoked Paprika Curry Sauce

        recipeListFav.addRecipe(recipe3); // Vichyssoise
        recipeListFav.addRecipe(recipe4); // Fried Rice
    }

    // EFFECTS: processes commands for an individual recipe from the main recipe list
    @SuppressWarnings("methodlength")
    private void processSearchRecipeMain() {
        Recipe recipe = searchRecipeMain();
        isFound(recipe);

        boolean run = true;
        while (run) {
            displaySearchRecipeMain();
            String command = input.nextLine();
            command = command.toLowerCase();

            switch (command) {
                case "f":
                    recipeListFav.addRecipe(recipe);
                    System.out.println(recipe.getRecipeName() + " added to favourite recipe list!");
                    run = false;
                    break;
                case "r":
                    recipeList.removeRecipe(recipe);
                    System.out.println(recipe.getRecipeName() + " has been deleted from the main list");
                    run = false;
                    break;
                case "e":
                    editRecipe(recipe);
                    run = false;
                    break;
                case "p":
                    printRecipe(recipe);
                    run = false;
                    break;
                case "q":
                    run = false;
                    break;
                default:
                    System.out.println("Invalid input. Try Again.");
            }
        }
    }

    // REQUIRES: list to not be empty
    // EFFECTS: returns recipe in the main list, else returns null
    private Recipe searchRecipeMain() {
        System.out.println("What are you searching for in the recipe list?");
        String recipe = input.nextLine();
        Recipe found = recipeList.searchRecipe(recipe);

        if (found == null) {
            System.out.println("Recipe not found.");
        }
        return found;
    }

    // EFFECTS: makes a new recipe with user inputs
    // No check for nulls yet
    private Recipe makeNewRecipe() {
        System.out.println("Input your new recipe!");

        System.out.println("Name of the recipe:");
        String name = input.nextLine();
        System.out.println("Category of recipe");
        String category = input.nextLine();
        System.out.println("Ingredients");
        String ingredients = input.nextLine();
        System.out.println("Steps");
        String steps = input.nextLine();

        Recipe recipe = new Recipe(name, category, ingredients, steps);
        System.out.println("Recipe Added");

        return recipe;
    }

    // EFFECTS: process input for editing one of the fields for the recipe
    // Works for now, but will refactor on a later date. Using suppress warnings for now.
    @SuppressWarnings("methodlength")
    private void editRecipe(Recipe recipe) {
        boolean run = true;
        while (run) {
            displayEditMenu();
            String command = input.nextLine();
            command = command.toLowerCase();
            switch (command) {
                case "n":
                    String name = editRecipeInput();
                    recipe.changeRecipeName(name);
                    run = false;
                    break;
                case "c":
                    String category = editRecipeInput();
                    recipe.changeCategory(category);
                    run = false;
                    break;
                case "i":
                    String ing = editRecipeInput();
                    recipe.changeIngredients(ing);
                    run = false;
                    break;
                case "s":
                    String steps = editRecipeInput();
                    recipe.changeSteps(steps);
                    run = false;
                    break;
                default:
                    System.out.println("Invalid input. Try again.");
            }
        }
    }

    // EFFECTS: processes commands for an individual recipe from the favourite list
    @SuppressWarnings("methodlength")
    private void processSearchRecipeFav() {
        Recipe recipe = searchRecipeFav();
        isFound(recipe);

        boolean run = true;

        while (run) {
            displaySearchRecipeFav();
            String command = input.nextLine();
            command = command.toLowerCase();

            switch (command) {
                case "r":
                    recipeListFav.removeRecipe(recipe);
                    System.out.println(recipe.getRecipeName() + " removed from the favourite recipe list");
                    run = false;
                    break;
                case "p":
                    printRecipe(recipe);
                    run = false;
                    break;
                case "q":
                    run = false;
                    break;
                default:
                    System.out.println("Invalid input. Try Again.");
            }
        }
    }

    // REQUIRES: list to not be empty
    // EFFECTS: returns recipe if found in the favourite list, else returns null
    private Recipe searchRecipeFav() {
        System.out.println("What are you searching for in the favourite recipe list?");
        String recipe = input.nextLine();
        Recipe found = recipeListFav.searchRecipe(recipe);

        if (found == null) {
            System.out.println("Recipe not found.");
        }
        return found;
    }

    // Helper function for searchRecipes
    private void isFound(Recipe recipe) {
        if (recipe == null) {
            return;
        }
        System.out.println("Recipe found: " + recipe.getRecipeName());
    }

    // Helper function for editRecipe
    private String editRecipeInput() {
        System.out.println("Type your input");
        return input.nextLine();
    }

    // EFFECTS: displays recipe app menu
    private void displayMenu() {
        System.out.println("\nSelect your options!");
        System.out.println("\tprintall -> Print names of all recipes in both list");
        System.out.println("\ts -> Search for a recipe in the main list");
        System.out.println("\tf -> Search for a recipe in the favourite list");
        System.out.println("\ta -> Add new recipe to the main list");
        System.out.println("\tq -> quit");
    }

    // EFFECTS: displays recipe menu for searching the main list
    private void displaySearchRecipeMain() {
        System.out.println("\nWhat would you like to do with this recipe?");
        System.out.println("\tf - Add recipe to favourite list");
        System.out.println("\tr - Remove recipe from main list");
        System.out.println("\te - Edit recipe");
        System.out.println("\tp - Print recipe in full");
        System.out.println("\tq - Cancel selection and return");
    }

    // EFFECTS: displays recipe menu for searching the fav list
    private void displaySearchRecipeFav() {
        System.out.println("\nWhat would you like to do with this favourite recipe?");
        System.out.println("\tr - Remove recipe from favourite list");
        System.out.println("\tp - Print recipe in full");
        System.out.println("\tq - Cancel selection and return");
    }

    // EFFECTS: displays edit menu for the recipe
    private void displayEditMenu() {
        System.out.println("\nWhat would you like to do with this recipe?");
        System.out.println("\tn - Change name");
        System.out.println("\tc - Change category");
        System.out.println("\ti - Change ingredients");
        System.out.println("\ts - Change steps");
    }

    // EFFECTS: prints recipe
    private void printRecipe(Recipe recipe) {
        repetitionChar();
        System.out.printf("%s%s%n%s%s%n%s%s%n%s%n%s%n",
                "Recipe Name: ", recipe.getRecipeName(),
                "Category: ", recipe.getCategory(),
                "Ingredients: ", recipe.getIngredients(),
                "Steps: ", recipe.getSteps());
        System.out.println(); // for newline
        repetitionChar();
    }

    // EFFECTS: prints all names of recipes in both recipe and recipe favourite lists
    private void printAllRecipeNames() {
        repetitionChar();
        System.out.println("\n**Main Recipe List**");
        for (int i = 0; i < recipeList.size(); i++) {
            Recipe recipe = recipeList.atIndex(i);
            System.out.println((i + 1) + ": " + recipe.getRecipeName());
        }

        System.out.println("\n**Favourite Recipe List**");
        for (int i = 0; i < recipeListFav.size(); i++) {
            Recipe recipe = recipeListFav.atIndex(i);
            System.out.println((i + 1) + ": " + recipe.getRecipeName());
        }
        System.out.println(); // for newline
        repetitionChar();
    }

    // EFFECTS: generates repeating chars for printing recipes. RIP no String.repeat
    private void repetitionChar() {
        char[] charArray = new char[40]; // of 40 length
        for (int i = 0; i < 40; i++) {
            charArray[i] = '=';
        }
        System.out.println(charArray);
    }
}
