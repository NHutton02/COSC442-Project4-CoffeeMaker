package edu.towson.cis.cosc442.project4.coffeemaker;

public class CoffeeMaker {
    /** Array of recipes in coffee maker */
    private Recipe[] recipeArray;
    /** Number of recipes in coffee maker */
    private final int NUM_RECIPES = 3;
    /** Array describing if the array is full */
    private boolean[] recipeFull;
    /** Inventory of the coffee maker */
    private Inventory inventory;

    /**
     * Initializes a new CoffeeMaker with default recipes and inventory.
     */
    public CoffeeMaker() {
        recipeArray = new Recipe[NUM_RECIPES];
        recipeFull = new boolean[NUM_RECIPES];
        for (int i = 0; i < NUM_RECIPES; i++) {
            recipeArray[i] = new Recipe();
            recipeFull[i] = false;
        }
        inventory = new Inventory();
    }

    /**
     * Adds a new recipe to the coffee maker if there is space and it does not
     * already exist.
     * 
     * @param r the recipe to be added
     * @return true if the recipe was successfully added, false otherwise
     */
    public boolean addRecipe(Recipe r) {
        boolean canAddRecipe = true;

        // Check if the recipe already exists
        for (int i = 0; i < NUM_RECIPES; i++) {
            if (r.equals(recipeArray[i])) {
                canAddRecipe = false;
            }
        }

        // Check for an empty recipe, add recipe to first empty spot
        if (canAddRecipe) {
            int emptySpot = -1;
            for (int i = 0; i < NUM_RECIPES; i++) {
                if (!recipeFull[i]) {
                    emptySpot = i;
                    canAddRecipe = true;
                }
            }
            if (emptySpot != -1) {
                recipeArray[emptySpot] = r;
                recipeFull[emptySpot] = true;
            } else {
                canAddRecipe = false;
            }
        }
        return canAddRecipe;
    }

    /**
     * Deletes the specified recipe from the coffee maker if it exists.
     * 
     * @param r the recipe to be deleted
     * @return true if the recipe was successfully deleted, false otherwise
     */
    public boolean deleteRecipe(Recipe r) {
        boolean canDeleteRecipe = false;
        if (r != null) {
            for (int i = 0; i < NUM_RECIPES; i++) {
                if (r.equals(recipeArray[i]) && recipeFull[i]) {
                    recipeArray[i] = new Recipe();
                    recipeFull[i]= false;
                    canDeleteRecipe = true;
                }
            }
        }
        return canDeleteRecipe;
    }

    /**
     * Replaces an existing recipe with a new recipe if the new recipe can be added.
     * 
     * @param oldRecipe the recipe to be replaced
     * @param newRecipe the new recipe to replace the old one
     * @return true if the recipe was successfully edited, false otherwise
     */
    public boolean editRecipe(Recipe oldRecipe, Recipe newRecipe) {
        if(oldRecipe == null || newRecipe == null){
            return false;
        }
        //Recipe names cannot be changed
        if (oldRecipe.getName() == null || !oldRecipe.getName().equals(newRecipe.getName())){
            return false;
        }
        //Locate and update the original recipe
        for(int i = 0; i < NUM_RECIPES; i++){
            if (recipeFull[i] && oldRecipe.equals(recipeArray[i])) {
            recipeArray[i] = newRecipe;
            return true;
        }
    }

    return false;
}
        


    /**
     * Adds specified amounts of ingredients to the inventory if all amounts are
     * non-negative.
     * 
     * @param amtCoffee    amount of coffee to add
     * @param amtMilk      amount of milk to add
     * @param amtSugar     amount of sugar to add
     * @param amtChocolate amount of chocolate to add
     * @return true if the inventory was successfully updated, false otherwise
     */
    public boolean addInventory(int amtCoffee, int amtMilk, int amtSugar, int amtChocolate) {
        boolean canAddInventory = true;
        if (amtCoffee < 0 || amtMilk < 0 || amtSugar < 0 || amtChocolate < 0) {
            canAddInventory = false;
        } else {
            inventory.setCoffee(inventory.getCoffee() + amtCoffee);
            inventory.setMilk(inventory.getMilk() + amtMilk);
            inventory.setSugar(inventory.getSugar() + amtSugar);
            inventory.setChocolate(inventory.getChocolate() + amtChocolate);
        }
        return canAddInventory;
    }

    /**
     * Returns the current inventory of the coffee maker.
     * 
     * @return the current Inventory object
     */
    public Inventory checkInventory() {
        return inventory;
    }

    /**
     * Attempts to make coffee with the specified recipe if paid amount is
     * sufficient and ingredients are available.
     * 
     * @param r       the recipe to make coffee from
     * @param amtPaid the amount of money paid
     * @return the change returned to the customer, or the entire amount if the
     *         coffee cannot be made
     */
    public int makeCoffee(Recipe r, int amtPaid) {
       if(r == null){
        return amtPaid;
       }
       //find requested recipe in book
       Recipe selectedRecipe = null;

       for (int i =0; i < NUM_RECIPES; i++){
        if(recipeFull[i] && r.equals(recipeArray[i])){
            selectedRecipe = recipeArray[i];
            break;
        }
       }
       if(selectedRecipe == null){
        return amtPaid;
       }
// Refund if recipe is not registered
       if(amtPaid < selectedRecipe.getPrice() || !inventory.enoughIngredients(selectedRecipe)){
        return amtPaid;
       }
//Consume ingredients
 inventory.setCoffee(inventory.getCoffee() - selectedRecipe.getAmtCoffee());
    inventory.setMilk(inventory.getMilk() - selectedRecipe.getAmtMilk());
    inventory.setSugar(inventory.getSugar() - selectedRecipe.getAmtSugar());
    inventory.setChocolate(inventory.getChocolate() - selectedRecipe.getAmtChocolate());

    return amtPaid - selectedRecipe.getPrice();
    
      }
    /**
     * Returns the array of recipes stored in the coffee maker.
     * 
     * @return an array of all recipes
     */
    public Recipe[] getRecipes() {
        return recipeArray;
    }

    /**
     * Finds and returns the recipe with the given name.
     * 
     * @param name the name of the recipe to find
     * @return the recipe matching the given name, or an empty recipe if none found
     */
    public Recipe getRecipeForName(String name) {
        Recipe r = new Recipe();
        for (int i = 0; i < NUM_RECIPES; i++) {
            if (recipeArray[i].getName() != null) {
                if ((recipeArray[i].getName()).equals(name)) {
                    r = recipeArray[i];
                }
            }
        }
        return r;
    }
}
