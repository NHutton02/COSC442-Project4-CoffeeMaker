package edu.towson.cis.cosc442.project4.coffeemaker;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



class CoffeeMakerTest {

    private CoffeeMaker maker;

    @BeforeEach
    void setUp() {
        // Also resets Inventory's static quantities to 15.
        maker = new CoffeeMaker();
    }

    private Recipe recipe(String name, int price, int coffee, int milk,
                          int sugar, int chocolate) {
        Recipe r = new Recipe();
        r.setName(name);
        r.setPrice(price);
        r.setAmtCoffee(coffee);
        r.setAmtMilk(milk);
        r.setAmtSugar(sugar);
        r.setAmtChocolate(chocolate);
        return r;
    }

    private Recipe simpleRecipe(String name) {
        return recipe(name, 10, 2, 3, 1, 4);
    }

    @Test
    void newCoffeeMakerStartsWithFifteenOfEachIngredient() {
        Inventory inventory = maker.checkInventory();
        assertNotNull(inventory);
        assertAll(
            () -> assertEquals(15, inventory.getCoffee()),
            () -> assertEquals(15, inventory.getMilk()),
            () -> assertEquals(15, inventory.getSugar()),
            () -> assertEquals(15, inventory.getChocolate())
        );
    }

    @Test
    void newCoffeeMakerHasNoNamedRecipes() {
        Recipe[] slots = maker.getRecipes();
        assertNotNull(slots);
        for (Recipe slot : slots) {
            assertNotNull(slot);
            assertNull(slot.getName());
        }
    }

    @Test
    void addRecipeStoresRecipeSoItCanBeFoundByName() {
        Recipe mocha = simpleRecipe("Mocha");

        assertTrue(maker.addRecipe(mocha));
        assertSame(mocha, maker.getRecipeForName("Mocha"));
    }

    @Test
    void addRecipeRejectsDuplicateName() {
        Recipe first = simpleRecipe("Mocha");
        Recipe sameNameDifferentPrice = recipe("Mocha", 20, 1, 1, 1, 1);

        assertTrue(maker.addRecipe(first));
        assertFalse(maker.addRecipe(sameNameDifferentPrice));
        assertSame(first, maker.getRecipeForName("Mocha"));
    }

    @Test
    void addRecipeRejectsFourthDistinctRecipe() {
        assertTrue(maker.addRecipe(simpleRecipe("Mocha")));
        assertTrue(maker.addRecipe(simpleRecipe("Latte")));
        assertTrue(maker.addRecipe(simpleRecipe("Espresso")));

        assertFalse(maker.addRecipe(simpleRecipe("Tea")),
            "The requirements limit the recipe book to three recipes");
        assertNull(maker.getRecipeForName("Tea").getName());
    }

    @Test
    void lookupOfUnknownRecipeReturnsEmptyRecipe() {
        Recipe missing = maker.getRecipeForName("Unknown");
        assertNotNull(missing);
        assertNull(missing.getName());
    }

    @Test
    void deleteRecipeRejectsNull() {
        assertFalse(maker.deleteRecipe(null));
    }

    @Test
    void deleteRecipeRejectsRecipeNotInBook() {
        assertFalse(maker.deleteRecipe(simpleRecipe("Unknown")));
    }

    @Test
    void deleteRecipeActuallyRemovesRecipe() {
        Recipe mocha = simpleRecipe("Mocha");
        assertTrue(maker.addRecipe(mocha));

        assertTrue(maker.deleteRecipe(mocha));
        assertNull(maker.getRecipeForName("Mocha").getName(),
            "A deleted recipe should no longer be available");
        assertTrue(maker.addRecipe(simpleRecipe("Mocha")),
            "The deleted recipe's name should be available for reuse");
    }

    @Test
    void editRecipeRejectsMissingOldRecipe() {
        assertFalse(maker.editRecipe(simpleRecipe("Missing"), simpleRecipe("Missing")));
    }

    @Test
    void editRecipeUpdatesExistingRecipeWithoutChangingItsName() {
        Recipe original = simpleRecipe("Mocha");
        Recipe updated = recipe("Mocha", 20, 5, 4, 3, 2);
        assertTrue(maker.addRecipe(original));

        assertTrue(maker.editRecipe(original, updated));
        Recipe actual = maker.getRecipeForName("Mocha");
        assertAll(
            () -> assertEquals("Mocha", actual.getName()),
            () -> assertEquals(20, actual.getPrice()),
            () -> assertEquals(5, actual.getAmtCoffee()),
            () -> assertEquals(4, actual.getAmtMilk()),
            () -> assertEquals(3, actual.getAmtSugar()),
            () -> assertEquals(2, actual.getAmtChocolate())
        );
    }

    @Test
    void editRecipeStillWorksWhenRecipeBookIsFull() {
        Recipe mocha = simpleRecipe("Mocha");
        assertTrue(maker.addRecipe(mocha));
        assertTrue(maker.addRecipe(simpleRecipe("Latte")));
        assertTrue(maker.addRecipe(simpleRecipe("Espresso")));

        assertTrue(maker.editRecipe(mocha, recipe("Mocha", 25, 2, 2, 2, 2)));
        assertEquals(25, maker.getRecipeForName("Mocha").getPrice());
        assertNotNull(maker.getRecipeForName("Latte").getName());
        assertNotNull(maker.getRecipeForName("Espresso").getName());
    }

    @Test
    void editRecipeCannotRenameOrModifyAnotherRecipe() {
        Recipe mocha = simpleRecipe("Mocha");
        Recipe latte = simpleRecipe("Latte");
        assertTrue(maker.addRecipe(mocha));
        assertTrue(maker.addRecipe(latte));

        Recipe attemptedRename = recipe("Latte", 99, 5, 5, 5, 5);
        assertFalse(maker.editRecipe(mocha, attemptedRename),
            "UC4 prohibits changing the name of a recipe");
        assertSame(mocha, maker.getRecipeForName("Mocha"));
        assertSame(latte, maker.getRecipeForName("Latte"));
    }

    @Test
    void addInventoryAcceptsAllPositiveAmounts() {
        assertTrue(maker.addInventory(2, 3, 4, 5));
        Inventory actual = maker.checkInventory();
        assertAll(
            () -> assertEquals(17, actual.getCoffee()),
            () -> assertEquals(18, actual.getMilk()),
            () -> assertEquals(19, actual.getSugar()),
            () -> assertEquals(20, actual.getChocolate())
        );
    }

    @Test
    void addInventoryAcceptsZeroAmounts() {
        assertTrue(maker.addInventory(0, 0, 0, 0));
        Inventory actual = maker.checkInventory();
        assertAll(
            () -> assertEquals(15, actual.getCoffee()),
            () -> assertEquals(15, actual.getMilk()),
            () -> assertEquals(15, actual.getSugar()),
            () -> assertEquals(15, actual.getChocolate())
        );
    }

    @Test
    void addInventoryAcceptsPositiveAmountsWhenSugarAdditionIsZero() {
        assertTrue(maker.addInventory(2, 3, 0, 5));
        assertAll(
            () -> assertEquals(17, maker.checkInventory().getCoffee()),
            () -> assertEquals(18, maker.checkInventory().getMilk()),
            () -> assertEquals(15, maker.checkInventory().getSugar()),
            () -> assertEquals(20, maker.checkInventory().getChocolate())
        );
    }

    @Test
    void addInventoryRejectsNegativeCoffeeWithoutChangingStock() {
        assertFalse(maker.addInventory(-1, 2, 0, 2));
        assertInventoryIsStillFifteen();
    }

    @Test
    void addInventoryRejectsNegativeMilkWithoutChangingStock() {
        assertFalse(maker.addInventory(2, -1, 0, 2));
        assertInventoryIsStillFifteen();
    }

    @Test
    void addInventoryRejectsNegativeSugarWithoutChangingStock() {
        assertFalse(maker.addInventory(2, 2, -1, 2));
        assertInventoryIsStillFifteen();
    }

    @Test
    void addInventoryRejectsNegativeChocolateWithoutChangingStock() {
        assertFalse(maker.addInventory(2, 2, 0, -1));
        assertInventoryIsStillFifteen();
    }

    private void assertInventoryIsStillFifteen() {
        assertAll(
            () -> assertEquals(15, maker.checkInventory().getCoffee()),
            () -> assertEquals(15, maker.checkInventory().getMilk()),
            () -> assertEquals(15, maker.checkInventory().getSugar()),
            () -> assertEquals(15, maker.checkInventory().getChocolate())
        );
    }

    @Test
    void makeCoffeeReturnsChangeAndConsumesIngredients() {
        Recipe mocha = simpleRecipe("Mocha");
        assertTrue(maker.addRecipe(mocha));

        assertEquals(5, maker.makeCoffee(mocha, 15));
        Inventory actual = maker.checkInventory();
        assertAll(
            () -> assertEquals(13, actual.getCoffee(), "Coffee should be consumed"),
            () -> assertEquals(12, actual.getMilk()),
            () -> assertEquals(14, actual.getSugar()),
            () -> assertEquals(11, actual.getChocolate())
        );
    }

    @Test
    void makeCoffeeAcceptsExactPaymentAndReturnsNoChange() {
        Recipe mocha = simpleRecipe("Mocha");
        maker.addRecipe(mocha);
        assertEquals(0, maker.makeCoffee(mocha, 10));
    }

    @Test
    void makeCoffeeRefundsInsufficientPaymentWithoutUsingIngredients() {
        Recipe mocha = simpleRecipe("Mocha");
        maker.addRecipe(mocha);

        assertEquals(9, maker.makeCoffee(mocha, 9));
        assertInventoryIsStillFifteen();
    }

    @Test
    void makeCoffeeRefundsWhenCoffeeIngredientIsInsufficient() {
        assertCannotMakeRecipe(recipe("Mocha", 10, 16, 1, 1, 1));
    }

    @Test
    void makeCoffeeRefundsWhenMilkIngredientIsInsufficient() {
        assertCannotMakeRecipe(recipe("Mocha", 10, 1, 16, 1, 1));
    }

    @Test
    void makeCoffeeRefundsWhenSugarIngredientIsInsufficient() {
        assertCannotMakeRecipe(recipe("Mocha", 10, 1, 1, 16, 1));
    }

    @Test
    void makeCoffeeRefundsWhenChocolateIngredientIsInsufficient() {
        assertCannotMakeRecipe(recipe("Mocha", 10, 1, 1, 1, 16));
    }

    private void assertCannotMakeRecipe(Recipe r) {
        maker.addRecipe(r);
        assertEquals(15, maker.makeCoffee(r, 15));
        assertInventoryIsStillFifteen();
    }

    @Test
    void makeCoffeeSucceedsWhenStockExactlyEqualsIngredientRequirements() {
        Recipe usesAll = recipe("Everything", 10, 15, 15, 15, 15);
        maker.addRecipe(usesAll);

        assertEquals(0, maker.makeCoffee(usesAll, 10));
        assertAll(
            () -> assertEquals(0, maker.checkInventory().getCoffee()),
            () -> assertEquals(0, maker.checkInventory().getMilk()),
            () -> assertEquals(0, maker.checkInventory().getSugar()),
            () -> assertEquals(0, maker.checkInventory().getChocolate())
        );
    }

    @Test
    void makeCoffeeRefundsWhenRecipeIsNotInRecipeBook() {
        Recipe unregistered = simpleRecipe("NotAdded");

        assertEquals(15, maker.makeCoffee(unregistered, 15),
            "UC7 requires purchasing a beverage listed in the RecipeBook");
        assertInventoryIsStillFifteen();
    }
}
