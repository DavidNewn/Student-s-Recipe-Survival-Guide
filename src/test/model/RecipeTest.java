package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RecipeTest {
    private Recipe testRecipe1;

    @BeforeEach
    void setup () {
        testRecipe1 = new Recipe("Pasta Primavera","Pasta","Vegetarian",1);
    }
    @Test
    void testConstructorRecipe1() {
        assertEquals("Pasta Primavera",testRecipe1.getRecipeName());
        assertEquals("Pasta",testRecipe1.getCategory());
        assertEquals("Vegetarian",testRecipe1.getDietType());
        assertTrue(testRecipe1.getId() == 1);
    }

}
