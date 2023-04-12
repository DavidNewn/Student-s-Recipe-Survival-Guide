package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the Event class
 */
public class EventTest {
	private Event e;
	private int seq = 1;
    private Recipe newRecipe = new Recipe("A New Recipe", "Whatever category", "No ing",
            "1. Cook");
	
	//NOTE: these tests might fail if time at which line (2) below is executed
	//is different from time that line (1) is executed.  Lines (1) and (2) must
	//run in same millisecond for this test to make sense and pass.
	
	@BeforeEach
	public void runBefore() {
		e = new Event("New Recipe Added: " + newRecipe.getRecipeName());   // (1)
		seq = 1;   // (2)
	}

    // Add recipe
	@Test
	public void testEvent() {
        assertEquals("A New Recipe", newRecipe.getRecipeName());
        assertEquals("Whatever category", newRecipe.getCategory());
        assertEquals("No ing", newRecipe.getIngredients());
        assertEquals("1. Cook", newRecipe.getSteps());

		assertEquals("New Recipe Added: A New Recipe", e.getDescription());
		assertEquals(seq, 1);
	}

	@Test
	public void testToString() {
		assertEquals(seq + ": " + "New Recipe Added: A New Recipe", e.toString());
	}
}
