#Author: Group2
#Author: Sal Camassa, Connor Hall, John-Michael Caskey
#Format taken from existing EditRecipe.feature

Feature: Edit a recipe for acceptance 
	As a user
	I want to be able to edit recipes in the CoffeeMaker to test for acceptance
	So that we can edit the recipes to our liking
	
		
Scenario Outline: Valid edit
	Given the CoffeeMaker already has some <existingRecipes> Recipes
	When I go to enter valid values for name: <recipeName>; cost: <price>; and ingredients: <amtCoffee> coffee, <amtMilk> milk, <amtSugar> sugar, <amtChocolate> chocolate
	And I go to edit that recipe to have cost: <newPrice>; and ingredients: <newAmtCoffee> coffee, <newAmtMilk> milk, <newAmtSugar> sugar, <newAmtChocolate> chocolate
	Then the same recipe is successfully edited
	
Examples:
	| existingRecipes | recipeName | price | amtCoffee | amtMilk | amtSugar | amtChocolate | newPrice | newAmtCoffee | newAmtMilk | newAmtSugar | newAmtChocolate |
	| 0               | Coffee     | 50    | 2         | 1       | 1        | 0            | 49       | 1            | 1          | 1           | 1               |

	
	


Scenario Outline: Invalid Edit
	Given the CoffeeMaker already has <existingRecipes> Recipes
	When I go to enter valid values for name: Coffee; cost: 50; and ingredients: 3 coffee, 1 milk, 1 sugar, 0 chocolate
	And I go to invalidly edit that recipe to have cost: <price>; and ingredients: <amtCoffee> coffee, <amtMilk> milk, <amtSugar> sugar, <amtChocolate> chocolate
	Then the error for an invalid entry of <ingredient> in a recipe occurs
	And the recipe is not at all edited
	
Examples:
	| existingRecipes | price | amtCoffee | amtMilk | amtSugar | amtChocolate | ingredient |
	| 0               | -50   | 1         | 1       | 1        | 1            | price      |
	| 0               | 50    | -1        | 1       | 1        | 1            | coffee     |
	| 0               | 50    | 1         | -1      | 1        | 1            | milk       |
	| 0               | 50    | 1         | 1       | -1       | 1            | sugar      |
	| 0               | 50    | 1         | 1       | 1        | -1           | chocolate  |
	| 0               | a     | 1         | 1       | 1        | 1            | price      |
	| 0               | 50    | a         | 1       | 1        | 1            | coffee     |
	| 0               | 50    | 1         | a       | 1        | 1            | milk       |
	| 0               | 50    | 1         | 1       | a        | 1            | sugar      |
	| 0               | 50    | 1         | 1       | 1        | a            | chocolate  |