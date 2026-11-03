       package edu.towson.cis.cosc442.project4.coffeemaker;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RecipeTest {

    @Test
    void newRecipeHasDefaultValues() {
        // Arrange / Act
        Recipe recipe = new Recipe();

        // Assert
        assertNull(recipe.getName());
        assertEquals(0, recipe.getPrice());
        assertEquals(0, recipe.getAmtCoffee());
        assertEquals(0, recipe.getAmtMilk());
        assertEquals(0, recipe.getAmtSugar());
        assertEquals(0, recipe.getAmtChocolate());
    }

    @Test
    void settersStoreValidValues() {
        Recipe recipe = new Recipe();

        recipe.setName("Mocha");
        recipe.setPrice(10);
        recipe.setAmtCoffee(2);
        recipe.setAmtMilk(3);
        recipe.setAmtSugar(1);
        recipe.setAmtChocolate(4);

       
       
       
       assertEquals("Mocha", recipe.getName());
        assertEquals(10, recipe.getPrice());
        assertEquals(2, recipe.getAmtCoffee());
        assertEquals(3, recipe.getAmtMilk());
        assertEquals(1, recipe.getAmtSugar());
        assertEquals(4, recipe.getAmtChocolate());
    }

    @Test
    void numericSettersAcceptZero() {
        Recipe recipe = new Recipe();
        recipe.setPrice(5);
        recipe.setAmtCoffee(5);
        recipe.setAmtMilk(5);
        recipe.setAmtSugar(5);
        recipe.setAmtChocolate(5);

        recipe.setPrice(0);
        recipe.setAmtCoffee(0);
        recipe.setAmtMilk(0);
        recipe.setAmtSugar(0);
        recipe.setAmtChocolate(0);

        assertEquals(0, recipe.getPrice());
        assertEquals(0, recipe.getAmtCoffee());
        assertEquals(0, recipe.getAmtMilk());
        assertEquals(0, recipe.getAmtSugar());
        assertEquals(0, recipe.getAmtChocolate());
    }

    @Test
    void negativeNumericValuesResetToZero() {
        Recipe recipe = new Recipe();
        recipe.setPrice(5);
        recipe.setAmtCoffee(5);
        recipe.setAmtMilk(5);
        recipe.setAmtSugar(5);
        recipe.setAmtChocolate(5);

        recipe.setPrice(-1);
        recipe.setAmtCoffee(-1);
        recipe.setAmtMilk(-1);
        recipe.setAmtSugar(-1);
        recipe.setAmtChocolate(-1);

        assertEquals(0, recipe.getPrice());
        assertEquals(0, recipe.getAmtCoffee());
        assertEquals(0, recipe.getAmtMilk());
        assertEquals(0, recipe.getAmtSugar());
        assertEquals(0, recipe.getAmtChocolate());
    }

    @Test
    void nameCanBeChangedAndToStringReturnsName() {
        Recipe recipe = new Recipe();

        recipe.setName("Latte");
        assertEquals("Latte", recipe.getName());
        assertEquals("Latte", recipe.toString());

        recipe.setName("Cappuccino");
        assertEquals("Cappuccino", recipe.getName());
        assertEquals("Cappuccino", recipe.toString());
    }

    @Test
    void recipesWithSameNonNullNameAreEqual() {
        Recipe first = new Recipe();
        Recipe second = new Recipe();
        first.setName("Mocha");
        second.setName("Mocha");
        first.setPrice(5);
        second.setPrice(10);

        assertTrue(first.equals(second));
    }

    @Test
    void recipesWithDifferentNamesAreNotEqual() {
        Recipe first = new Recipe();
        Recipe second = new Recipe();
        first.setName("Mocha");
        second.setName("Latte");

        assertFalse(first.equals(second));
    }

    @Test
    void recipesAreNotEqualWhenOtherNameIsNull() {
        Recipe first = new Recipe();
        Recipe second = new Recipe();
        first.setName("Mocha");

        assertFalse(first.equals(second));
    }

    @Test
    void recipesAreNotEqualWhenThisNameIsNull() {
        Recipe first = new Recipe();
        Recipe second = new Recipe();
        second.setName("Mocha");

        assertFalse(first.equals(second));
    }
}