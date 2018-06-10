package edu.ncsu.csc.coffee_maker.models.persistent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Tests the Inventory model class.
 *
 * Reference: https://spring.io/guides/gs/testing-web/
 *
 * @author John-Michael Caskey
 * @author Sal Camassa
 * @author Connor Hall
 */
public class InventoryTest {

    /**
     * Attempts invalid inventory checks and adds
     * 
     * @param inventory
     *            inventory to change
     * @param input
     *            string to check invalid
     */
    public void tryInvalid ( final Inventory inventory, final String input ) {
        try {
            inventory.checkChocolate( input );
            fail();
        }
        catch ( final IllegalArgumentException e ) {
            // Invalid arguments should throw
        }
        try {
            inventory.checkMilk( input );
            fail();
        }
        catch ( final IllegalArgumentException e ) {
            // Invalid arguments should throw
        }
        try {
            inventory.checkCoffee( input );
            fail();
        }
        catch ( final IllegalArgumentException e ) {
            // Invalid arguments should throw
        }
        try {
            inventory.checkSugar( input );
            fail();
        }
        catch ( final IllegalArgumentException e ) {
            // Invalid arguments should throw
        }
    }

    /**
     * Tests the checkChocolate, checkMilk, and checkSugar methods on CoffeMaker
     */
    @Test
    public void testCheckNumbers () {
        final Inventory inventory = new Inventory( 15, 15, 15, 15 );

        // Test valid numbers
        final String[] validInputs = { "0", "1", "5", "60", "100", "00100", "123456789" };
        final int[] expectedOutput = { 0, 1, 5, 60, 100, 100, 123456789 };
        for ( int i = 0; i < validInputs.length; i++ ) {
            final String input = validInputs[i];
            final int expected = expectedOutput[i];
            assertEquals( expected, inventory.checkCoffee( input ) );
            assertEquals( expected, inventory.checkChocolate( input ) );
            assertEquals( expected, inventory.checkMilk( input ) );
            assertEquals( expected, inventory.checkSugar( input ) );
        }

        final String[] invalidInputs = { "-1", "9.3", "abc", "1 3 4", "" };
        for ( final String input : invalidInputs ) {
            tryInvalid( inventory, input );
        }
    }

}
