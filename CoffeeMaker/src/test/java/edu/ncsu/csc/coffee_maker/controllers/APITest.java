package edu.ncsu.csc.coffee_maker.controllers;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;

import edu.ncsu.csc.coffee_maker.Application;
import edu.ncsu.csc.coffee_maker.models.RecipeBook;
import edu.ncsu.csc.coffee_maker.models.persistent.Recipe;

/**
 * Perform a quick check of one of the API methods to ensure that the API
 * controller is up and receiving requests as it should be
 *
 * @author Kai Presler-Marshall
 *
 */
@RunWith ( SpringRunner.class )
@SpringBootTest ( properties = "logging.level.org.springframework.web=DEBUG" )
@AutoConfigureMockMvc
public class APITest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    /**
     * Sets up the tests.
     */
    @Before
    public void setup () throws Exception {
        final List<Recipe> list = Application.getCoffeeMaker().getRecipeBook().getRecipes();
        for ( final Recipe r : list ) {
            Application.getCoffeeMaker().getRecipeBook().deleteRecipe( r );
        }
        Application.getCoffeeMaker().getRecipeBook().updateRecipes();

        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
    }

    /**
     * Tests that we are able to make a call to the REST API. If such a call
     * cannot be made, throws an exception instead
     *
     * @throws Exception
     */
    @Test
    public void testApi () throws Exception {
        /*
         * Verify that we are able to make a request to the Inventory API
         * endpoint and that we get a 400 (OK) status in return
         */
        mvc.perform( get( "/api/v1/inventory" ) ).andExpect( status().isOk() );
    }

    /**
     * Test edit rest api functions
     *
     * @throws Exception
     */
    public void testAPIEdit () throws Exception {
        final Gson gson = new Gson();

        /*
         * initialize a recipe in recipe book
         */
        final Recipe recipe1 = new Recipe();
        recipe1.setName( "Coffee1" );
        recipe1.setPrice( 5 );
        recipe1.setCoffee( 1 );
        recipe1.setMilk( 1 );
        recipe1.setSugar( 1 );
        recipe1.setChocolate( 1 );
        mvc.perform( post( "/api/v1/recipes" ).content( gson.toJson( recipe1 ) )
                .contentType( MediaType.APPLICATION_JSON ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        recipe1.setCoffee( 2 );

        mvc.perform( put( "/api/v1/recipes" ).content( gson.toJson( recipe1 ) )
                .contentType( MediaType.APPLICATION_JSON ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        mvc.perform( delete( "/api/v1/recipes/Coffee1" ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

    }

    /**
     * Test delete rest api from API test
     *
     * @throws Exception
     */
    @Test
    public void testAPIDelete () throws Exception {
        final Gson gson = new Gson();

        /*
         * initialize a recipe in recipe book
         */
        final Recipe recipe1 = new Recipe();
        recipe1.setName( "Coffee1" );
        recipe1.setPrice( 5 );
        recipe1.setCoffee( 1 );
        recipe1.setMilk( 1 );
        recipe1.setSugar( 1 );
        recipe1.setChocolate( 1 );
        mvc.perform( post( "/api/v1/recipes" ).content( gson.toJson( recipe1 ) )
                .contentType( MediaType.APPLICATION_JSON ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        /*
         * test delete with valid recipe name in book
         */
        mvc.perform( delete( "/api/v1/recipes/Coffee1" ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );
        /*
         * test delete with no recipes in recipe book
         */
        mvc.perform( delete( "/api/v1/recipes/Coffee1" ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isNotFound() );

        /*
         * test delete with invalid name and expect error back
         */
        mvc.perform( post( "/api/v1/recipes" ).content( gson.toJson( recipe1 ) )
                .contentType( MediaType.APPLICATION_JSON ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );
        mvc.perform( delete( "/api/v1/recipes/Coffee2" ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isNotFound() );
        mvc.perform( delete( "/api/v1/recipes/Coffee1" ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

    }

    /**
     * Tests that add is working via http request. If such a call cannot be
     * made, throws an exception instead
     *
     * @throws Exception
     */
    @Test
    public void testAPIAdd () throws Exception {
        final Gson gson = new Gson();

        /*
         * Verify add creates a recipe with valid recipe given as input
         */
        final Recipe recipe1 = new Recipe();
        recipe1.setName( "Coffee1" );
        recipe1.setPrice( 5 );
        recipe1.setCoffee( 1 );
        recipe1.setMilk( 1 );
        recipe1.setSugar( 1 );
        recipe1.setChocolate( 1 );
        mvc.perform( post( "/api/v1/recipes" ).content( gson.toJson( recipe1 ) )
                .contentType( MediaType.APPLICATION_JSON ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        /*
         * Duplicate name recipe should not be added to recipebook
         */
        final Recipe recipe2 = new Recipe();
        recipe2.setName( "Coffee1" );
        recipe2.setPrice( 10 );
        recipe2.setCoffee( 2 );
        recipe2.setMilk( 2 );
        recipe2.setSugar( 2 );
        recipe2.setChocolate( 2 );
        mvc.perform( post( "/api/v1/recipes" ).content( gson.toJson( recipe2 ) )
                .contentType( MediaType.APPLICATION_JSON ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isConflict() );

        /*
         * Add additional recipes to fill book to capacity
         */
        for ( int i = 1; i < RecipeBook.RECIPE_CAPACITY; i++ ) {
            recipe2.setName( "Coffee" + ( i + 1 ) );
            mvc.perform( post( "/api/v1/recipes" ).content( gson.toJson( recipe2 ) )
                    .contentType( MediaType.APPLICATION_JSON ).accept( MediaType.APPLICATION_JSON ) );
        }

        /*
         * An additional recipe should not be added if book is full
         */
        final Recipe recipe3 = new Recipe();
        recipe3.setName( "CoffeeCap" );
        recipe3.setPrice( 10 );
        recipe3.setCoffee( 2 );
        recipe3.setMilk( 2 );
        recipe3.setSugar( 2 );
        recipe3.setChocolate( 2 );
        mvc.perform( post( "/api/v1/recipes" ).content( gson.toJson( recipe3 ) )
                .contentType( MediaType.APPLICATION_JSON ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isInsufficientStorage() );

        /*
         * clean up
         */
        for ( int i = 0; i < RecipeBook.RECIPE_CAPACITY; i++ ) {
            mvc.perform( delete( "/api/v1/recipes/Coffee" + ( i + 1 ) ).contentType( MediaType.APPLICATION_JSON ) );
        }

    }

    /**
     * Tests that add is working via http request. If such a call cannot be
     * made, throws an exception instead
     *
     * @throws Exception
     */
    @Test
    public void testAPIMake () throws Exception {
        final Gson gson = new Gson();

        /*
         * Verify add creates a recipe with valid recipe given as input
         */
        final Recipe recipe1 = new Recipe();
        recipe1.setName( "Coffee1" );
        recipe1.setPrice( 5 );
        recipe1.setCoffee( 1 );
        recipe1.setMilk( 1 );
        recipe1.setSugar( 1 );
        recipe1.setChocolate( 1 );
        mvc.perform( post( "/api/v1/recipes" ).content( gson.toJson( recipe1 ) )
                .contentType( MediaType.APPLICATION_JSON ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        final Recipe recipe2 = new Recipe();
        recipe2.setName( "Coffee2" );
        recipe2.setPrice( 0 );
        recipe2.setCoffee( 2 );
        recipe2.setMilk( 2 );
        recipe2.setSugar( 2 );
        recipe2.setChocolate( 2 );
        mvc.perform( post( "/api/v1/recipes" ).content( gson.toJson( recipe2 ) )
                .contentType( MediaType.APPLICATION_JSON ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        /*
         * test making coffee with exact funds. expect success with 0 change
         */
        mvc.perform( post( "/api/v1/makecoffee/Coffee1" ).content( gson.toJson( 5 ) )
                .contentType( MediaType.APPLICATION_JSON ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andExpect( content().string( "{\"result\":\"success\", \"change\":0}" ) );

        /*
         * test making coffee with excessive funds. expect success with 5 change
         */
        mvc.perform( post( "/api/v1/makecoffee/Coffee1" ).content( gson.toJson( 10 ) )
                .contentType( MediaType.APPLICATION_JSON ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andExpect( content().string( "{\"result\":\"success\", \"change\":5}" ) );

        /*
         * test free coffee with 0 funds as payment. expected success.
         */
        mvc.perform( post( "/api/v1/makecoffee/Coffee2" ).content( gson.toJson( 0 ) )
                .contentType( MediaType.APPLICATION_JSON ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andExpect( content().string( "{\"result\":\"success\", \"change\":0}" ) );

        /*
         * clean up
         */
        for ( int i = 0; i < 2; i++ ) {
            mvc.perform( delete( "/api/v1/recipes/Coffee" + ( i + 1 ) ).contentType( MediaType.APPLICATION_JSON ) )
                    .andExpect( status().isOk() );
        }

    }

    /**
     * Tests that add is working via http request. If such a call cannot be
     * made, throws an exception instead
     *
     * @throws Exception
     */
    @Test
    public void testAPIGet () throws Exception {
        final Gson gson = new Gson();

        final Recipe recipe1 = new Recipe();
        recipe1.setName( "Coffee1" );
        recipe1.setPrice( 5 );
        recipe1.setCoffee( 1 );
        recipe1.setMilk( 1 );
        recipe1.setSugar( 1 );
        recipe1.setChocolate( 1 );
        mvc.perform( post( "/api/v1/recipes" ).content( gson.toJson( recipe1 ) )
                .contentType( MediaType.APPLICATION_JSON ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        final Recipe recipe2 = new Recipe();
        recipe2.setName( "Coffee" );
        recipe2.setPrice( 10 );
        recipe2.setCoffee( 2 );
        recipe2.setMilk( 2 );
        recipe2.setSugar( 2 );
        recipe2.setChocolate( 2 );

        for ( int i = 1; i < RecipeBook.RECIPE_CAPACITY; i++ ) {
            recipe2.setName( "Coffee" + ( i + 1 ) );
            mvc.perform( post( "/api/v1/recipes" ).content( gson.toJson( recipe2 ) )
                    .contentType( MediaType.APPLICATION_JSON ).accept( MediaType.APPLICATION_JSON ) )
                    .andExpect( status().isOk() );
        }

        /*
         * Test get recipe from valid recipe in book
         */
        final ResultActions result = mvc
                .perform( get( "/api/v1/recipes/Coffee1" ).accept( new MediaType( "application", "json" ) ) )
                .andExpect( status().isOk() ).andExpect( content().contentType( "application/json;charset=UTF-8" ) );
        final String json = result.andReturn().getResponse().getContentAsString();
        final Recipe check = gson.fromJson( json, Recipe.class );
        assertEquals( "Coffee1", check.getName() );
        assertEquals( 5, check.getPrice().intValue() );
        assertEquals( 1, check.getCoffee().intValue() );
        assertEquals( 1, check.getMilk().intValue() );
        assertEquals( 1, check.getSugar().intValue() );
        assertEquals( 1, check.getChocolate().intValue() );

        final ResultActions result1 = mvc
                .perform( get( "/api/v1/recipes" ).accept( new MediaType( "application", "json" ) ) )
                .andExpect( status().isOk() ).andExpect( content().contentType( "application/json;charset=UTF-8" ) );
        result1.andReturn().getResponse().getContentAsString();

        /*
         * Test get recipe for recipe not in the recipe book
         */
        mvc.perform( get( "/api/v1/recipes/NA" ) ).andExpect( status().isNotFound() );
        for ( int i = 0; i < RecipeBook.RECIPE_CAPACITY; i++ ) {
            mvc.perform( delete( "/api/v1/recipes/Coffee" + ( i + 1 ) ).contentType( MediaType.APPLICATION_JSON ) );
        }
    }

}
