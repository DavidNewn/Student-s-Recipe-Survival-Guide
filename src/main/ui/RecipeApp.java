package ui;

import model.Recipe;
import model.RecipeListFav;
import model.RecipeList;

import java.util.Scanner;

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
        recipeListFav.addRecipe(recipe3); // Vichyssoise
        recipeListFav.addRecipe(recipe4); // Fried Rice
        recipeList.addRecipe(recipe5); // Smoked Paprika Curry Sauce
    }

    private void displayMenu() {
        System.out.println("\nSelect your options!");
        System.out.println("\tprintall -> Print names of all recipes in both list");
        System.out.println("\ts -> Search for a recipe in the main list");
        System.out.println("\tf -> Search for a recipe in the favourite list");
        System.out.println("\tq -> quit");
    }

    // EFFECTS: processes commands for an individual recipe from the main recipe list
    private void processSearchRecipeMain() {
        Recipe recipe = searchRecipeMain();
        if (recipe == null) {
            return;
        }
        String name = recipe.getRecipeName();
        System.out.println("Recipe found: " + name);

        boolean run = true;
        String command = null;

        while (run) {
            displaySearchRecipeMain();
            command = input.nextLine();
            command = command.toLowerCase();

            switch (command) {
                case "a":
                    recipeListFav.addRecipe(recipe);
                    System.out.println(name + " added to favourite recipe list!");
                    run = false;
                    break;
                case "p":
                    printRecipe(recipe);
                    run = false;
                    break;
                case "q":
                    System.out.println("Cancelled selection");
                    run = false;
                    break;
                default:
                    System.out.println("Invalid input. Try Again.");
            }
        }
    }

    private Recipe searchRecipeMain() {
        System.out.println("What are you searching for in the recipe list?");
        String recipe = input.nextLine();
        Recipe found = recipeList.searchRecipe(recipe);

        if (found == null) {
            System.out.println("Recipe not found.");
        }
        return found;
    }

    private void displaySearchRecipeMain() {
        System.out.println("\nWhat would you like to do with this recipe?");
        System.out.println("\ta - Add recipe to favourite list");
        System.out.println("\tp - Print recipe in full");
        System.out.println("\tq - Cancel selection and return");
    }

    // EFFECTS: processes commands for an individual recipe from the favourite list
    // Duplicate code, will refactor later
    private void processSearchRecipeFav() {
        Recipe recipe = searchRecipeFav();
        if (recipe == null) {
            return;
        }
        String name = recipe.getRecipeName();
        System.out.println("Recipe found: " + name);

        boolean run = true;
        String command = null;

        while (run) {
            displaySearchRecipeFav();
            command = input.nextLine();
            command = command.toLowerCase();

            switch (command) {
                case "r":
                    recipeListFav.removeRecipe(recipe);
                    System.out.println(name + " removed from the favourite recipe list");
                    run = false;
                    break;
                case "p":
                    printRecipe(recipe);
                    run = false;
                    break;
                case "q":
                    System.out.println("Cancelled selection");
                    run = false;
                    break;
                default:
                    System.out.println("Invalid input. Try Again.");
            }
        }
    }

    private Recipe searchRecipeFav() {
        System.out.println("What are you searching for in the favourite recipe list?");
        String recipe = input.nextLine();
        Recipe found = recipeListFav.searchRecipe(recipe);

        if (found == null) {
            System.out.println("Recipe not found.");
        }
        return found;
    }

    private void displaySearchRecipeFav() {
        System.out.println("\nWhat would you like to do with this favourite recipe?");
        System.out.println("\tr - Remove recipe from favourite list");
        System.out.println("\tp - Print recipe in full");
        System.out.println("\tq - Cancel selection and return");
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


    //!!! Provide option to rename parts of a recipe, add a new one, etc.

}
