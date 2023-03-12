package persistence;

import model.Recipe;
import model.RecipeList;
import model.RecipeListFav;
import model.RecipeLists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonWriterTest extends JsonTest {
    private RecipeList recipeList;
    private RecipeListFav recipeListFav;
    private RecipeLists lists;

    private Recipe testRecipe1; // Pasta Primavera
    private Recipe testRecipe2; // Minestrone Soup
    private Recipe testRecipe3; // Test

    @BeforeEach
    void runBefore() {
        recipeList = new RecipeList("Main Recipes");
        recipeListFav = new RecipeListFav("Favourite Recipes");

        testRecipe1 = new Recipe("Pasta Primavera","Vegetarian","Pasta, Veggies",
                "1. Boil pasta. 2. Add veggies. 3. Serve hot");
        testRecipe2 = new Recipe("Minestrone Soup","Soup","Veggies",
                "1. Add chopped veggies to boiling water. 2. When veggies are soft, serve");
        testRecipe3 = new Recipe("Test","Ice Cream",
                "Vanilla Extract, Ginger, Oregano",
                "1. Google the recipe. 2. Mix everything into an ice cream machine. " +
                        "3. Profit (I promise it's a good combination)");
    }

    @Test
    void testWriterInvalidFile() {
        try {
            lists = new RecipeLists("Test", recipeList, recipeListFav);
            JsonWriter writerFail = new JsonWriter("./data/illegal\0:data.json");
            writerFail.open();
            fail("IOException expected");
        } catch (IOException e) {
            System.out.println("Caught IOException for testWriterInvalidFile()");
            }
        }

    @Test
    void testWriterEmptyRecipeLists() {
        try {
            lists = new RecipeLists("Test", recipeList, recipeListFav);
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyRecipeLists.json");
            writer.open();
            writer.write(lists);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyRecipeLists.json");
            lists = reader.read("Test", "Main Recipes", "Favourite Recipes");
            assertEquals("Test", lists.getName());
            assertEquals(0, lists.getRecipeList().size());
            assertEquals(0, lists.getRecipeListFav().size());
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    void testWriterGeneralRecipeLists() {
        try {
            recipeList.addRecipe(testRecipe1);
            recipeList.addRecipe(testRecipe3);
            recipeList.addRecipe(testRecipe2);
            recipeListFav.addRecipe(testRecipe1);

            lists = new RecipeLists("Test", recipeList, recipeListFav);
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralRecipeLists.json");
            writer.open();
            writer.write(lists);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralRecipeLists.json");
            lists = reader.read("Test", "Main Recipes", "Favourite Recipes");
            assertEquals("Test", lists.getName());

            List<Recipe> recipeListMain = lists.getRecipeList().getRecipeListCollection();
            List<Recipe> recipeListFav = lists.getRecipeListFav().getRecipeListCollection();
            assertEquals(3, recipeListMain.size());
            assertEquals(1, recipeListFav.size());

            checkRecipe(recipeListMain.get(0), "Pasta Primavera", "Vegetarian",
                    "Pasta, Veggies", "1. Boil pasta. 2. Add veggies. 3. Serve hot");
            checkRecipe(recipeListMain.get(1), "Test","Ice Cream",
                    "Vanilla Extract, Ginger, Oregano",
                    "1. Google the recipe. 2. Mix everything into an ice cream machine. " +
                            "3. Profit (I promise it's a good combination)");
            checkRecipe(recipeListMain.get(2), "Minestrone Soup","Soup","Veggies",
                    "1. Add chopped veggies to boiling water. 2. When veggies are soft, serve");

            checkRecipe(recipeListFav.get(0), "Pasta Primavera", "Vegetarian",
                    "Pasta, Veggies", "1. Boil pasta. 2. Add veggies. 3. Serve hot");
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }
}

