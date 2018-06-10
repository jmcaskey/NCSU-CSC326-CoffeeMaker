'use strict';

var app = angular.module('myApp').controller('DeleteRecipeController', ['$scope', '$q', 'CoffeeMakerService', function($scope, $q, CoffeeMakerService) {
	
	function updateRecipes() {
		return CoffeeMakerService.getRecipes()
			.then(function(recipes) {
				$scope.recipes = recipes;
			});
	}
	
	this.del = function() {
		var requests = [];
		if ($scope.deleteAll) {
			for (var i = 0, len = $scope.recipes.length; i < len; i++) {
				var recipe = $scope.recipes[i];
				requests.push(CoffeeMakerService.deleteRecipe(recipe.name));
			}
		} else {
			requests.push(CoffeeMakerService.deleteRecipe($scope.name));
		}
		
		$q.all(requests)
			.then(function() {
				$scope.submissionSuccess = true;
		        updateRecipes();
			}, function() {
				$scope.submissionSuccess = false;
				updateRecipes();
			});
	}
	
	updateRecipes();
	
}]);