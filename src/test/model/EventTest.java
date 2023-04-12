package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the Event class
 */
public class EventTest {
	private Event e;
	private Date d;
    private Recipe newRecipe = new Recipe("A New Recipe", "Whatever category", "No ing",
            "1. Cook");
	
	//NOTE: these tests might fail if time at which line (2) below is executed
	//is different from time that line (1) is executed.  Lines (1) and (2) must
	//run in same millisecond for this test to make sense and pass.
	
	@BeforeEach
	public void runBefore() {
		e = new Event("New Recipe Added: " + newRecipe.getRecipeName());   // (1)
		d = Calendar.getInstance().getTime();   // (2)
	}

    // Add recipe
	@Test
	public void testAddEvent() {
        assertEquals("A New Recipe", newRecipe.getRecipeName());
        assertEquals("Whatever category", newRecipe.getCategory());
        assertEquals("No ing", newRecipe.getIngredients());
        assertEquals("1. Cook", newRecipe.getSteps());

		assertEquals("New Recipe Added: A New Recipe", e.getDescription());
		assertEquals(d, e.getDate());
	}


    // !!! Make a test for hashCode and equals from Event class

	@Test
	public void testToString() {
		assertEquals(d.toString() + "\n" + "New Recipe Added: A New Recipe", e.toString());
	}
}
