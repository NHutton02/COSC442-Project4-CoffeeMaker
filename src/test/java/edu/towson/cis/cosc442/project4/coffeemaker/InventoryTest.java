           package edu.towson.cis.cosc442.project4.coffeemaker;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Regression tests derived from the Inventory Javadocs and CoffeeMaker requirements.
 * Inventory's fields are static, so construction in @BeforeEach resets shared state.
 */
class InventoryTest {

    private Inventory inventory;

    @BeforeEach
    void setUp() {
        inventory = new Inventory();
    }

    private Recipe recipeWithIngredients(int coffee, int milk, int sugar, int chocolate) {
        Recipe recipe = new Recipe();
        recipe.setAmtCoffee(coffee);
        recipe.setAmtMilk(milk);
        recipe.setAmtSugar(sugar);
        recipe.setAmtChocolate(chocolate);
        return recipe;
    }

    @Test
    void newInventoryStartsWithFifteenOfEachIngredient() {
        assertAll(

           () -> assertEquals(15, inventory.getCoffee()),
            () -> assertEquals(15, inventory.getMilk()),
            () -> assertEquals(15, inventory.getSugar()),
            () -> assertEquals(15, inventory.getChocolate())
        );
    }

    @Test
    void settersStorePositiveQuantities() {
        inventory.setCoffee(20);
        inventory.setMilk(21);
        inventory.setSugar(22);
        inventory.setChocolate(23);

        assertAll(
            () -> assertEquals(20, inventory.getCoffee()),
            () -> assertEquals(21, inventory.getMilk()),
            () -> assertEquals(22, inventory.getSugar()),
            () -> assertEquals(23, inventory.getChocolate())
        );
    }

    @Test
    void settersAcceptZeroAtLowerBoundary() {
        inventory.setCoffee(0);
        inventory.setMilk(0);
        inventory.setSugar(0);
        inventory.setChocolate(0);

        assertAll(
            () -> assertEquals(0, inventory.getCoffee()),
            () -> assertEquals(0, inventory.getMilk()),
            () -> assertEquals(0, inventory.getSugar()),
            () -> assertEquals(0, inventory.getChocolate())
        );
    }

    @Test
    void settersClampNegativeQuantitiesToZero() {
        inventory.setCoffee(-1);
        inventory.setMilk(-1);
        inventory.setSugar(-1);
        inventory.setChocolate(-1);

        assertAll(
            () -> assertEquals(0, inventory.getCoffee()),
            () -> assertEquals(0, inventory.getMilk()),
            () -> assertEquals(0, inventory.getSugar()),
            () -> assertEquals(0, inventory.getChocolate())
        );
    }

    @Test
    void enoughIngredientsReturnsTrueWhenStockExceedsRequirements() {
        assertTrue(inventory.enoughIngredients(recipeWithIngredients(1, 2, 3, 4)));
    }

    @Test
    void enoughIngredientsReturnsTrueAtExactStockBoundary() {
        assertTrue(inventory.enoughIngredients(recipeWithIngredients(15, 15, 15, 15)));
    }

    @Test
    void enoughIngredientsReturnsFalseWhenCoffeeIsInsufficient() {
        assertFalse(inventory.enoughIngredients(recipeWithIngredients(16, 1, 1, 1)));
    }

    @Test
    void enoughIngredientsReturnsFalseWhenMilkIsInsufficient() {
        assertFalse(inventory.enoughIngredients(recipeWithIngredients(1, 16, 1, 1)));
    }

    @Test
    void enoughIngredientsReturnsFalseWhenSugarIsInsufficient() {
        assertFalse(inventory.enoughIngredients(recipeWithIngredients(1, 1, 16, 1)));
    }

    @Test
    void enoughIngredientsReturnsFalseWhenChocolateIsInsufficient() {
        assertFalse(inventory.enoughIngredients(recipeWithIngredients(1, 1, 1, 16)));
    }

    @Test
    void enoughIngredientsReturnsTrueForRecipeUsingNoIngredients() {
        inventory.setCoffee(0);
        inventory.setMilk(0);
        inventory.setSugar(0);
        inventory.setChocolate(0);
        assertTrue(inventory.enoughIngredients(recipeWithIngredients(0, 0, 0, 0)));
    }

    @Test
    void toStringListsCurrentAmountsInDocumentedOrder() {
        inventory.setCoffee(3);
        inventory.setMilk(4);
        inventory.setSugar(5);
        inventory.setChocolate(6);

        assertEquals("Coffee: 3\nMilk: 4\nSugar: 5\nChocolate: 6\n", inventory.toString());
    }
}
