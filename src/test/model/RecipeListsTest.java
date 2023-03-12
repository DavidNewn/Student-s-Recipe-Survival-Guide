package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RecipeListsTest {
    private RecipeList recipeListMain; // name: "Main Recipes"
    private RecipeListFav recipeListFav; // name: "Favourite Recipes"
    private RecipeLists recipeTestLists; // name: "Test"

    @BeforeEach
    void setup() {
        recipeListMain = new RecipeList("Main Recipes");
        recipeListFav = new RecipeListFav("Favourite Recipes");
        recipeTestLists = new RecipeLists("Test", recipeListMain, recipeListFav);
    }

    @Test
    void testConstructor() {
        assertEquals("Test", recipeTestLists.getName());
        assertEquals("Main Recipes", recipeListMain.getName());
        assertEquals("Favourite Recipes", recipeListFav.getName());
    }
}
