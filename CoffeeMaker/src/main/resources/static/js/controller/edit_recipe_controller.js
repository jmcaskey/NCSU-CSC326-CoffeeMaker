'use strict';
 
angular.module('myApp').controller('EditRecipeController', ['$scope', 'CoffeeMakerService', function($scope, CoffeeMakerService) {
	var self = this;

    self.recipe = {name:'', price:'', coffee:'', milk:'', sugar:'', chocolate:''};
    self.submit = submit;
    self.reset = reset;
    self.clearMessages = clearMessages;
    
    $scope.recipes = [];
        
	function updateRecipes() {
		return CoffeeMakerService.getRecipes()
			.then(function(recipes) {
				$scope.recipes = recipes;
			});
	}
 
    function editRecipe(recipe){
    	clearMessages();
		return CoffeeMakerService.editRecipe(recipe)
        	.then(function() {
        		$scope.success = true;
        		$scope.failure = false;
                reset();
                return updateRecipes();
        	}).catch(function(errResponse) {
        		$scope.failure = true;
        		$scope.success = false;
        		console.error('Error while adding recipe');
        	});
    }
    
    function clearMessages() {
    	$scope.failure = false;
    	$scope.success = false;
    }
 
    function submit() {
    	editRecipe(self.recipe);
    }
 
    function reset(){
    	updateRecipes()
    		.then(function() {
    	    	self.recipe = {name:'', price:'', coffee:'', milk:'', sugar:'', chocolate:''};
    	        $scope.editRecipeForm.$setPristine(); //reset Form
    	        $scope.failure = false;
    		});
    }
    
    updateRecipes();
}]);