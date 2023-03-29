package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RecipeTest {
    private Recipe testRecipe1;
    private Recipe testRecipe2;
    private Recipe testRecipe3;

    @BeforeEach
    void setup () {
        testRecipe1 = new Recipe("Pasta Primavera","Vegetarian","Pasta, Veggies",
                "1. Boil pasta. 2. Add veggies. 3. Serve hot");
        testRecipe2 = new Recipe("Minestrone Soup","Soup","Veggies",
                "1. Add chopped veggies to boiling water. 2. When veggies are soft, serve");
        testRecipe3 = new Recipe("Salisbury Steak","Meat","Ground beef, Breadcrumbs, Ketchup",
                "1. Combine ground beef with ingredients into patties. 2. Fry the patties. " +
                        "3. Reduce fond to make sauce");
    }

    @Test
    void testConstructorRecipe1() {
        assertEquals("Pasta Primavera",testRecipe1.getRecipeName());
        assertEquals("Vegetarian",testRecipe1.getCategory());
        assertEquals("Pasta, Veggies",testRecipe1.getIngredients());
        assertEquals("1. Boil pasta. 2. Add veggies. 3. Serve hot",testRecipe1.getSteps());
    }

    @Test
    void testChangeRecipeName() {
        testRecipe1.changeRecipeName("Banana");
        testRecipe2.changeRecipeName("Minestrone Soup"); // same name
        assertEquals("Banana",testRecipe1.getRecipeName());
        assertEquals("Minestrone Soup",testRecipe2.getRecipeName());
    }

    @Test
    void testChangeRecipeCategory() {
        testRecipe2.changeCategory("Meat");
        testRecipe3.changeCategory("Meat"); // same
        assertEquals("Meat", testRecipe2.getCategory());
        assertEquals("Meat",testRecipe3.getCategory());
    }

    @Test
    void testChangeRecipeIngredients() {
        testRecipe1.changeIngredients("Rice, Soup, Wine");
        testRecipe3.changeIngredients("Ground beef, Breadcrumbs, Ketchup"); // same
        assertEquals("Rice, Soup, Wine",testRecipe1.getIngredients());
        assertEquals("Ground beef, Breadcrumbs, Ketchup",testRecipe3.getIngredients());
    }

    @Test
    void testChangeRecipeSteps() {
        testRecipe2.changeSteps("Do nothing");
        testRecipe1.changeSteps("1. Boil pasta. 2. Add veggies. 3. Serve hot"); // same
        assertEquals("Do nothing", testRecipe2.getSteps());
        assertEquals("1. Boil pasta. 2. Add veggies. 3. Serve hot", testRecipe1.getSteps());
    }

    @Test
    void testToString() {
        assertEquals("Pasta Primavera", testRecipe1.toString());
        assertEquals("Minestrone Soup", testRecipe2.toString());
        assertEquals("Salisbury Steak", testRecipe3.toString());
    }
}
