package edu.ncsu.csc.selenium;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * Tests Edit Recipe functionality.
 *
 * @author Salvatore Camassa
 */

public class EditRecipeTest extends SeleniumTest {

    /** The URL for CoffeeMaker - change as needed */
    private WebDriver          driver;
    private String             baseUrl;
    private final StringBuffer verificationErrors = new StringBuffer();

    private String             recipeName;

    @Override
    @Before
    protected void setUp () throws Exception {
        super.setUp();

        recipeName = "CoffeeRecipe";
        driver = new HtmlUnitDriver( true );
        baseUrl = "http://localhost:8080";
        driver.manage().timeouts().implicitlyWait( 10, TimeUnit.SECONDS );

    }

    /**
     * Helper method for executing an edit on an existing recipe used to help
     * reduce repeating code.
     * 
     * @param price
     *            price of coffee
     * @param coffee
     *            coffee amount
     * @param milk
     *            amount of milk in coffee
     * @param sugar
     *            amount of sugar in coffee
     * @param chocolate
     *            amount of chocolate in coffee
     * @throws Exception
     */
    public void editHelper ( String price, String coffee, String milk, String sugar, String chocolate )
            throws Exception {
        // Edit the recipe information
        driver.findElement( By.name( "price" ) ).clear();
        driver.findElement( By.name( "price" ) ).sendKeys( price );
        driver.findElement( By.name( "coffee" ) ).clear();
        driver.findElement( By.name( "coffee" ) ).sendKeys( coffee );
        driver.findElement( By.name( "milk" ) ).clear();
        driver.findElement( By.name( "milk" ) ).sendKeys( milk );
        driver.findElement( By.name( "sugar" ) ).clear();
        driver.findElement( By.name( "sugar" ) ).sendKeys( sugar );
        driver.findElement( By.name( "chocolate" ) ).clear();
        driver.findElement( By.name( "chocolate" ) ).sendKeys( chocolate );

        // Submit the recipe.
        driver.findElement( By.cssSelector( "input[type=\"submit\"]" ) ).click();
    }

    /**
     * Sets up editing a recipe
     *
     * @throws Exception
     */
    public void editSetup () throws Exception {
        driver.get( baseUrl + "" );
        driver.findElement( By.linkText( "Edit Recipe" ) ).click();
        // Select the recipe to Edit and Edit it.
        driver.findElement( By.cssSelector( "input[type=\"radio\"]" ) ).click();
    }

    /**
     * Tests adding a recipe to edit.
     *
     * @throws Exception
     */
    public void add () throws Exception {
        driver.get( baseUrl + "" );
        driver.findElement( By.linkText( "Add a Recipe" ) ).click();

        // Enter the recipe information
        driver.findElement( By.name( "name" ) ).clear();
        driver.findElement( By.name( "name" ) ).sendKeys( recipeName );
        driver.findElement( By.name( "price" ) ).clear();
        driver.findElement( By.name( "price" ) ).sendKeys( "50" );
        driver.findElement( By.name( "coffee" ) ).clear();
        driver.findElement( By.name( "coffee" ) ).sendKeys( "2" );
        driver.findElement( By.name( "milk" ) ).clear();
        driver.findElement( By.name( "milk" ) ).sendKeys( "1" );
        driver.findElement( By.name( "sugar" ) ).clear();
        driver.findElement( By.name( "sugar" ) ).sendKeys( "1" );
        driver.findElement( By.name( "chocolate" ) ).clear();
        driver.findElement( By.name( "chocolate" ) ).sendKeys( "0" );
        // Submit the recipe.
        driver.findElement( By.cssSelector( "input[type=\"submit\"]" ) ).click();

        // Make sure the proper message was displayed.
        assertTrue( driver.getPageSource().contains( "Recipe Created" ) );
    }

    /********************************************************************************/

    /**
     * Test to edit an existing, valid recipe. Expect to get a valid success
     * message stating the recipe was edited.
     *
     * @throws Exception
     */
    @Test
    public void testGoodRecipe () throws Exception {
        add();
        editSetup();
        editHelper( "50", "1", "1", "1", "1" );

        // Make sure the proper message was displayed.
        assertTrue( driver.getPageSource().contains( "Recipe edited successfully" ) );
        System.out.println( driver.getPageSource() );
    }

    /**
     * Testing the rejection of a negative integer in the field for price
     *
     * @throws Exception
     */
    @Test
    public void testNegativePrice () throws Exception {
        editSetup();
        editHelper( "-50", "1", "1", "1", "1" );

        // Make sure the proper message was displayed.
        assertTrue( driver.getPageSource().contains( "Error while editing recipe" ) );
        System.out.println( driver.getPageSource() );
    }

    /**
     * Testing the rejection of a negative integer in the field for coffee
     *
     * @throws Exception
     */
    @Test
    public void testNegativeCoffee () throws Exception {
        editSetup();
        editHelper( "50", "-1", "1", "1", "1" );

        // Make sure the proper message was displayed.
        assertTrue( driver.getPageSource().contains( "Error while editing recipe" ) );
        System.out.println( driver.getPageSource() );
    }

    /**
     * Testing the rejection of a negative integer in the field for milk
     *
     * @throws Exception
     */
    @Test
    public void testNegativeMilk () throws Exception {
        editSetup();
        editHelper( "50", "1", "-1", "1", "1" );

        // Make sure the proper message was displayed.
        assertTrue( driver.getPageSource().contains( "Error while editing recipe" ) );
        System.out.println( driver.getPageSource() );
    }

    /**
     * Testing the rejection of a negative integer in the field for sugar
     *
     * @throws Exception
     */
    @Test
    public void testNegativeSugar () throws Exception {
        editSetup();
        editHelper( "50", "1", "1", "-1", "1" );

        // Make sure the proper message was displayed.
        assertTrue( driver.getPageSource().contains( "Error while editing recipe" ) );
        System.out.println( driver.getPageSource() );
    }

    /**
     * Testing the rejection of a negative integer in the field for chocolate
     *
     * @throws Exception
     */
    @Test
    public void testNegativeChocolate () throws Exception {
        editSetup();
        editHelper( "50", "1", "1", "1", "-1" );

        // Make sure the proper message was displayed.
        assertTrue( driver.getPageSource().contains( "Error while editing recipe" ) );
        System.out.println( driver.getPageSource() );
    }

    /********************************************************************************/

    /**
     * Testing the rejection of a non-integer in the field for price
     *
     * @throws Exception
     */
    @Test
    public void testLetterPrice () throws Exception {
        editSetup();
        editHelper( "-50", "1", "1", "1", "1" );

        // Make sure the proper message was displayed.
        assertTrue( driver.getPageSource().contains( "Error while editing recipe" ) );
        System.out.println( driver.getPageSource() );
    }

    /**
     * Testing the rejection of a non-integer in the field for coffee
     *
     * @throws Exception
     */
    @Test
    public void testLetterCoffee () throws Exception {
        editSetup();
        editHelper( "50", "a", "1", "1", "1" );

        // Make sure the proper message was displayed.
        assertTrue( driver.getPageSource().contains( "Error while editing recipe" ) );
        System.out.println( driver.getPageSource() );
    }

    /**
     * Testing the rejection of a non-integer in the field for milk
     *
     * @throws Exception
     */
    @Test
    public void testLetterMilk () throws Exception {
        editSetup();
        editHelper( "50", "1", "a", "1", "1" );

        // Make sure the proper message was displayed.
        assertTrue( driver.getPageSource().contains( "Error while editing recipe" ) );
        System.out.println( driver.getPageSource() );
    }

    /**
     * Testing the rejection of a non-integer in the field for sugar
     *
     * @throws Exception
     */
    @Test
    public void testLetterSugar () throws Exception {
        editSetup();
        editHelper( "50", "1", "1", "a", "1" );

        // Make sure the proper message was displayed.
        assertTrue( driver.getPageSource().contains( "Error while editing recipe" ) );
        System.out.println( driver.getPageSource() );
    }

    /**
     * Testing the rejection of a non-integer in the field for chocolate
     *
     * @throws Exception
     */
    @Test
    public void testLetterChocolate () throws Exception {
        editSetup();
        editHelper( "50", "1", "1", "1", "a" );

        // Make sure the proper message was displayed.
        assertTrue( driver.getPageSource().contains( "Error while editing recipe" ) );
        System.out.println( driver.getPageSource() );
    }

    /********************************************************************************/

    @Override
    @After
    public void tearDown () throws Exception {
        driver.quit();
        final String verificationErrorString = verificationErrors.toString();
        if ( !"".equals( verificationErrorString ) ) {
            fail( verificationErrorString );
        }
    }

}
