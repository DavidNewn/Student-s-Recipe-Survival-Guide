package ui;

import model.Recipe;
import model.RecipeListFav;
import model.RecipeList;

import java.util.Scanner;

public class RecipeApp {
    private RecipeList recipeList;
    private RecipeListFav recipeListFav;
    private Scanner input;

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
        if (command.equals("s")) {
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
        Recipe recipe1 = new Recipe("Pasta Primavera","Vegetarian","Pasta, Veggies",
                "1. Boil pasta. 2. Add veggies. 3. Serve hot");

        recipeList = new RecipeList();
        recipeListFav = new RecipeListFav();
        input = new Scanner(System.in);
        input.useDelimiter("/n");

        recipeList.addRecipe(recipe1);
    }

    private void displayMenu() {
        System.out.println("\nPick your recipe");
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

        boolean run = true;
        String command = null;

        System.out.println("Recipe found: " + recipe.getRecipeName());
        System.out.println("What would you like to do with this recipe?");
        System.out.println("p - Print recipe in full");
        System.out.println("q - Cancel selection and return");

        while (run) {
            command = input.nextLine();
            command = command.toLowerCase();

            if (command.equals("p")) {
                printRecipe(recipe);
                run = false;
            } else if (command.equals("q")) {
                System.out.println("Cancelled selection");
                run = false;
            }
        }
    }


    private Recipe searchRecipeMain() {
        System.out.println("What are you searching for in the recipe list?");
        String recipe = input.nextLine();
        Recipe found = recipeList.getRecipe(recipe);

        if (found == null) {
            System.out.println("Recipe not found.");
        }
        return found;
    }

    // EFFECTS: processes commands for an individual recipe from the favourite list
    // Duplicate code, will refactor later
    private void processSearchRecipeFav() {
        Recipe recipe = searchRecipeFav();
        if (recipe == null) {
            return;
        }

        boolean run = true;
        String command = null;

        System.out.println("Recipe found: " + recipe.getRecipeName());
        System.out.println("What would you like to do with this favourite recipe?");
        System.out.println("p - Print recipe in full");
        System.out.println("q - Cancel selection and return");

        while (run) {
            command = input.nextLine();
            command = command.toLowerCase();

            if (command.equals("p")) {
                printRecipe(recipe);
                run = false;
            } else if (command.equals("q")) {
                System.out.println("Cancelled selection");
                run = false;
            }
        }
    }

    private Recipe searchRecipeFav() {
        System.out.println("What are you searching for in the favourite recipe list?");
        String recipe = input.nextLine();
        Recipe found = recipeListFav.getRecipe(recipe);

        if (found == null) {
            System.out.println("Recipe not found.");
        }
        return found;
    }


    private void printRecipe(Recipe recipe) {
        System.out.println("=".repeat(40));
        System.out.printf("%s%s%n%s%s%n%s%s%n%s%n%s%n",
                "Recipe Name: ", recipe.getRecipeName(),
                "Category: ", recipe.getCategory(),
                "Ingredients: ", recipe.getIngredients(),
                "Steps: ", recipe.getSteps());
        System.out.println("=".repeat(40));
    }

    //!!! Provide option to rename parts of a recipe, add a new one, etc.
    //!!! Print out recipe, recipes from the same category

}
