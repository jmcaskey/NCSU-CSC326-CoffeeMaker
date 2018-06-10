'use strict';
 
angular.module('myApp').controller('AddRecipeController', ['$scope', 'CoffeeMakerService', function($scope, CoffeeMakerService) {
    var self = this;
    self.recipe={name:'', price:'', coffee:'', milk:'', sugar:'', chocolate:''};
 
    self.submit = submit;
    self.reset = reset;
 
    function addRecipe(recipe){
		$scope.success = false;
		$scope.failure = false;
		CoffeeMakerService.addRecipe(recipe)
        	.then(function() {
        		$scope.success = true;
        		$scope.failure = false;
        	}, function(errResponse) {
        		$scope.failure = true;
        		$scope.success = false;
        		console.error('Error while adding recipe');
        });
    }
 
    function submit() {
    	addRecipe(self.recipe);
        reset();
    }
 
    function reset(){
    	self.recipe={name:'', price:'', coffee:'', milk:'', sugar:'', chocolate:''};
        $scope.addRecipeForm.$setPristine(); //reset Form
    }
 
}]);