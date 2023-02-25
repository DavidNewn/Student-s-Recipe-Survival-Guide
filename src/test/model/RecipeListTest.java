package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class RecipeListTest {
    private RecipeList recipeListTest;
    private Recipe testRecipe1;
    private Recipe testRecipe2;
    private Recipe testRecipe3;

    @BeforeEach
    public void setup() {
        recipeListTest = new RecipeList();

        testRecipe1 = new Recipe("Pasta Primavera","Vegetarian","Pasta, Veggies",
                "1. Boil pasta. 2. Add veggies. 3. Serve hot");
        testRecipe2 = new Recipe("Minestrone Soup","Soup","Veggies",
                "1. Add chopped veggies to boiling water. 2. When veggies are soft, serve");
        testRecipe3 = new Recipe("Smoked Paprika Curry Sauce","Sauce",
                "Butter, Onions, Paprika, Tomato Paste, Heavy Cream",
                "1. Combine chopped with melted butter in a pot. Fry onions until they're soft " +
                        "2.Add in water, let it boil and reduce to half. 3. Turn off heat and add in tomato paste." +
                        "4. Add in heavy cream and heat it up to temperature. Mix well.");
    }

    @Test
    public void testAddOneRecipeToList() {
        recipeListTest.addRecipe(testRecipe1);
        assertEquals(1, recipeListTest.size());
    }

    @Test
    public void testRemoveOneRecipeFromList() {
        recipeListTest.addRecipe(testRecipe3);
        recipeListTest.addRecipe(testRecipe2);
        recipeListTest.addRecipe(testRecipe2);
        assertEquals(3, recipeListTest.size());

        recipeListTest.removeRecipe(testRecipe2);
        assertEquals(2, recipeListTest.size());
    }

    @Test
    public void testSearchForRecipeInList() {
        recipeListTest.addRecipe(testRecipe1); // "Pasta Primavera"
        recipeListTest.addRecipe(testRecipe2); // "Minestrone Soup"
        recipeListTest.addRecipe(testRecipe3); // "Smoked Paprika Curry Sauce"
        assertEquals(3, recipeListTest.size());

        Recipe found = recipeListTest.searchRecipe("Smoked Paprika Curry Sauce"); // normal
        Recipe found2 = recipeListTest.searchRecipe("minesTRone SOUP"); // testing ignore case
        Recipe notfound = recipeListTest.searchRecipe("notintherecipe"); // fail case
        assertEquals("Smoked Paprika Curry Sauce", found.getRecipeName());
        assertEquals("Minestrone Soup", found2.getRecipeName());
        assertEquals(null, notfound);
    }

    @Test
    public void testAtIndexInList () {
        recipeListTest.addRecipe(testRecipe1); // "Pasta Primavera"
        recipeListTest.addRecipe(testRecipe2); // "Minestrone Soup"
        assertEquals(2, recipeListTest.size());

        Recipe recipe1 = recipeListTest.atIndex(0);
        Recipe recipe2 = recipeListTest.atIndex(1);

        assertEquals("Pasta Primavera", recipe1.getRecipeName());
        assertEquals("Minestrone Soup", recipe2.getRecipeName());
    }
}
