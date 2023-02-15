package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RecipeTest {
    private Recipe testRecipe1;
    private Recipe testRecipe2;
    private Recipe testRecipe3;

    @BeforeEach
    public void setup () {
        testRecipe1 = new Recipe("Pasta Primavera","Vegetarian","Pasta, Veggies",
                "1. Boil pasta. 2. Add veggies. 3. Serve hot",1);
        testRecipe2 = new Recipe("Minestrone Soup","Soup","Veggies",
                "1. Add chopped veggies to boiling water. 2. When veggies are soft, serve",2);
        testRecipe3 = new Recipe("Salisbury Steak","Meat","Ground beef, breadcrumbs, ketchup",
                "1. Combine ground beef with ingredients into patties. 2. Fry the patties. " +
                        "3. Reduce fond to make sauce", 3);
    }

    @Test
    public void testConstructorRecipe1() {
        assertEquals("Pasta Primavera",testRecipe1.getRecipeName());
        assertEquals("Pasta",testRecipe1.getCategory());
        assertEquals("Pasta, Veggies",testRecipe1.getIngredients());
        assertEquals("1. Boil pasta. 2. Add veggies. 3. Serve hot",testRecipe1.getSteps());
        assertTrue(testRecipe1.getId() == 1);
    }

    @Test
    public void testGetRecipeName() {
        //stub;
    }

    @Test
    public void testAddRecipe() {
        //stub;
    }

    @Test
    public void testRemoveRecipe() {
        //stub;
    }

    @Test
    public void testAddFavouriteRecipe() {
        //stub;
    }

    @Test
    public void testRemoveFavouriteRecipe() {
        //stub;
    }

    @Test
    public void changeRecipeName() {
        //stub;
    }

    @Test
    public void changeRecipeCategory() {
        //stub;
    }
}
