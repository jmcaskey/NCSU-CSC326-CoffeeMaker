package edu.ncsu.csc.cucumber;

import org.junit.Assert;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.coffee_maker.models.CoffeeMaker;
import edu.ncsu.csc.coffee_maker.models.persistent.Recipe;
import edu.ncsu.csc.test_utils.SharedRecipeData;

/**
 * Step Defs (Cucumber) for interacting with the Recipe model to ensure it is
 * appropriately tested.
 *
 * @author Group 2: Sal Camassa, Connor Hall, John-Michael Caskey
 * 
 *         Format taken from existing RecipeStepDefs.java
 */
public class EditRecipeStepDefs {
    private final CoffeeMaker      coffeeMaker;
    private final SharedRecipeData recipeData;

    /**
     * Constructor for the RecipeStepDefs. Used to keep track of CoffeeMaker's
     * state to ensure that the test completed successfully.
     *
     * @param coffeemaker
     *            CoffeeMaker object; used to properly unit test it without
     *            Spring
     * @param srd
     *            SharedRecipeData; a backup of the recipes to ensure that the
     *            CoffeeMaker is behaving appropriately.
     */
    public EditRecipeStepDefs ( final CoffeeMaker coffeemaker, final SharedRecipeData srd ) {
        this.coffeeMaker = coffeemaker;
        this.recipeData = srd;
    }

    /**
     * Deletes the existing batch of recipies in the CoffeeMaker and then
     * populates it with a new batch of Recipies.
     *
     * @param numRecipes
     *            The number of recipies that should be in the RecipeBook
     * @throws Exception
     *             If the number of recipies to add is greater than the max
     *             allowed
     */
    @Given ( "^the CoffeeMaker already has some (\\d+) Recipes$" )
    public void existingRecipes ( final int numRecipes ) throws Exception {
        // Check current number of recipes in Recipe Book
        final int total = this.coffeeMaker.getRecipes().size();
        if ( total != 0 ) {
            int count = 0;
            while ( count < total ) {
                this.coffeeMaker.deleteRecipe( 0 );
                count++;
            }
            Assert.assertEquals( 0, this.coffeeMaker.getRecipes().size() );
        }

        if ( numRecipes == 0 ) {
            return; // no need to execute the rest of the code
        }
        if ( numRecipes > 3 ) {
            throw new IllegalArgumentException( "Number of Recipes cannot be greater than 3" );
        }
        else {

            for ( int i = 0; i < numRecipes; i++ ) {
                try {
                    final Recipe testR = new Recipe();
                    try {
                        testR.setName( "Recipe" + i );
                        final Integer pr = new Integer( i * 10 );
                        testR.setPrice( pr );
                        testR.setCoffee( new Integer( i ) );
                        testR.setMilk( 1 );
                        testR.setSugar( 1 );
                        testR.setChocolate( 1 );
                    }
                    catch ( final Exception e ) {
                        Assert.fail( "Error in creating recipes" );
                    }
                    recipeData.latestRecipeAdded = this.coffeeMaker.addRecipe( testR );

                }
                catch ( final Exception e ) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Create a recipe from the valid parameters specified.
     *
     * @param name
     *            Name of the new Recipe
     * @param cost
     *            Cost of the new Recipe
     * @param coffeeAmt
     *            Amount of Coffee needed for the new Recipe
     * @param milkAmt
     *            Amount of Milk needed for the new Recipe
     * @param sugarAmt
     *            Amount of Sugar needed for the new Recipe
     * @param chocolateAmt
     *            Amount of Chocolate needed for the new Recipe
     */
    @When ( "^I go to enter valid values for name: (.+); cost: (\\d+); and ingredients: (\\d+) coffee, (\\d+) milk, (\\d+) sugar, (\\d+) chocolate$" )
    public void addSpecificRecipe ( final String name, final int cost, final int coffeeAmt, final int milkAmt,
            final int sugarAmt, final int chocolateAmt ) {
        recipeData.recipeError = "";
        recipeData.latestRecipeAdded = false;
        final Recipe newR = new Recipe();
        try {

            newR.setName( name );
            newR.setPrice( cost );
            newR.setCoffee( coffeeAmt );
            newR.setMilk( milkAmt );
            newR.setSugar( sugarAmt );
            newR.setChocolate( chocolateAmt );
        }
        catch ( final Exception e ) {
            recipeData.recipeError = e.getMessage();
            Assert.fail( "Unable to create new Recipe" );
        }
        recipeData.currentRecipe = newR;
        final boolean tempCheck = this.coffeeMaker.addRecipe( newR );
        recipeData.latestRecipeAdded = tempCheck;
    }

    /**
     * Unsuccessfully attempt to add a new Recipe to the system.
     *
     * @param name
     *            Name of the new Recipe
     * @param cost
     *            Cost of the new Recipe
     * @param coffeeAmt
     *            Amount of Coffee needed for the new Recipe
     * @param milkAmt
     *            Amount of Milk needed for the new Recipe
     * @param sugarAmt
     *            Amount of Sugar needed for the new Recipe
     * @param chocolateAmt
     *            Amount of Chocolate needed for the new Recipe
     */
    @When ( "^I do attempt to submit the following recipe values - name: (.+); cost: (.+); and ingredients: (.+) coffee, (.+) milk, (.+) sugar, (.+) chocolate$" )
    public void invalidRecipe ( final String name, final String cost, final String coffeeAmt, final String milkAmt,
            final String sugarAmt, final String chocolateAmt ) {
        recipeData.recipeError = "";
        recipeData.latestRecipeAdded = false;
        final Recipe newR = new Recipe();
        try {
            newR.setName( name );
            try {
                newR.setPrice( Integer.parseInt( cost ) );
            }
            catch ( final NumberFormatException nfe ) {
                throw new NumberFormatException( "Price must be a positive integer" );
            }
            try {
                newR.setCoffee( Integer.parseInt( coffeeAmt ) );
                newR.setMilk( Integer.parseInt( milkAmt ) );
                newR.setSugar( Integer.parseInt( sugarAmt ) );
                newR.setChocolate( Integer.parseInt( chocolateAmt ) );
            }
            catch ( final Exception e ) {
                throw new NumberFormatException( "Units of must be a positive integer" );
            }

            recipeData.currentRecipe = newR;
            final boolean tempCheck = this.coffeeMaker.addRecipe( newR );
            recipeData.latestRecipeAdded = tempCheck;

        }
        catch ( final Exception e ) {
            recipeData.recipeError = e.getMessage();
        }

    }

    /**
     * Edit the most recently referenced recipe and give it the new parameters
     * provided
     *
     * @param cost
     *            New cost of the Recipe
     * @param coffeeAmt
     *            New amount of Coffee needed for the Recipe
     * @param milkAmt
     *            New amount of Milk needed for the Recipe
     * @param sugarAmt
     *            New amount of Sugar needed for the Recipe
     * @param chocolateAmt
     *            New amount of Chocolate needed for the Recipe
     */
    @When ( "^I go to edit that recipe to have cost: (\\d+); and ingredients: (\\d+) coffee, (\\d+) milk, (\\d+) sugar, (\\d+) chocolate$" )
    public void validEditRecipe ( final int cost, final int coffeeAmt, final int milkAmt, final int sugarAmt,
            final int chocolateAmt ) {

        recipeData.recipeError = "";
        final Recipe newR = recipeData.currentRecipe;
        recipeData.index = coffeeMaker.getRecipes().indexOf( newR );

        try {
            newR.setPrice( cost );
            newR.setCoffee( coffeeAmt );
            newR.setMilk( milkAmt );
            newR.setSugar( sugarAmt );
            newR.setChocolate( chocolateAmt );
        }
        catch ( final Exception e ) {
            recipeData.recipeError = e.getMessage();
            Assert.fail( "Unable to edit Recipe" );
        }
        recipeData.currentRecipe = newR;

        coffeeMaker.editRecipe( recipeData.index, newR );
    }

    /**
     * Unsuccessfully attempt to edit the most recently referenced recipe with
     * the new parameters provided.
     *
     * @param cost
     *            New cost of the Recipe
     * @param coffeeAmt
     *            New amount of Coffee needed for the Recipe
     * @param milkAmt
     *            New amount of Milk needed for the Recipe
     * @param sugarAmt
     *            New amount of Sugar needed for the Recipe
     * @param chocolateAmt
     *            New amount of Chocolate needed for the Recipe
     */
    @When ( "^I go to invalidly edit that recipe to have cost: (.+); and ingredients: (.+) coffee, (.+) milk, (.+) sugar, (.+) chocolate$" )
    public void invalidEditRecipe ( final String cost, final String coffeeAmt, final String milkAmt,
            final String sugarAmt, final String chocolateAmt ) {
        // TODO
        recipeData.recipeError = "";
        final Recipe newR = new Recipe();
        recipeData.index = coffeeMaker.getRecipes().indexOf( recipeData.currentRecipe );
        try {
            try {
                newR.setPrice( Integer.parseInt( cost ) );
            }
            catch ( final NumberFormatException nfe ) {
                throw new NumberFormatException( "Price must be a positive integer" );
            }
            try {
                newR.setCoffee( Integer.parseInt( coffeeAmt ) );
                newR.setMilk( Integer.parseInt( milkAmt ) );
                newR.setSugar( Integer.parseInt( sugarAmt ) );
                newR.setChocolate( Integer.parseInt( chocolateAmt ) );
            }
            catch ( final Exception e ) {
                throw new NumberFormatException( "Units of must be a positive integer" );
            }
        }
        catch ( final Exception e ) {
            recipeData.recipeError = e.getMessage();
        }
        recipeData.currentRecipe = newR;
    }

    /**
     * Ensure that a recipe was edited as expected by comparing the actual
     * results in the CoffeeMaker to what is stored in the backup
     */
    @Then ( "^the same recipe is successfully edited$" )
    public void recipeEditedSuccessfully () {
        Assert.assertEquals( "Recipe was not edited correctly", recipeData.currentRecipe,
                coffeeMaker.getRecipes().get( recipeData.index ) );
    }

    /**
     * Ensure that a recipe was _not_ edited as expected by comparing the actual
     * results in the CoffeeMaker to what is stored in the backup
     */
    @Then ( "^the recipe is not at all edited$" )
    public void recipeEditedUnsuccessfully () {
        final Recipe current = recipeData.currentRecipe;
        final Recipe other = coffeeMaker.getRecipes().get( recipeData.index );
        Assert.assertNotEquals( "Recipe was edited when it shouldn't have been", current, other );
    }

    /**
     * Ensure that an error occurred for adding the inventory specified
     *
     * @param ingredient
     *            The ingredient that should trigger the error
     */
    @Then ( "^the error for an invalid entry of (.+) in a recipe occurs$" )
    public void invalidIngredientError ( final String ingredient ) {
        Assert.assertTrue( !ingredient.isEmpty() );

    }

}
