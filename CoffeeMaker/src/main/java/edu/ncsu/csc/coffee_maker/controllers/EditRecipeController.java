package edu.ncsu.csc.coffee_maker.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller class for making coffee. The controller returns editrecipe.html
 * from the /src/main/resources/templates folder.
 *
 * @author Connor Hall
 */
@Controller
public class EditRecipeController {

    /**
     * On a GET request to /editrecipe, the MakeCoffeeController will return
     * /src/main/resources/templates/editrecipe.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( "/editrecipe" )
    public String recipeForm ( final Model model ) {
        return "editrecipe";
    }

}
