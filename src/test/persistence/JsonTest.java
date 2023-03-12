package persistence;

import model.Recipe;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkRecipe(Recipe recipe, String recipeName, String category, String ingredients, String steps) {
        assertEquals(recipeName, recipe.getRecipeName());
        assertEquals(category, recipe.getCategory());
        assertEquals(ingredients, recipe.getIngredients());
        assertEquals(steps, recipe.getSteps());
    }
}
