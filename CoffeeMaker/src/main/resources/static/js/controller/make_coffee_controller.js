'use strict';
 
angular.module('myApp').controller('MakeCoffeeController', ['$scope', 'CoffeeMakerService', function($scope, CoffeeMakerService) {
	this.makeCoffee = makeCoffee;
	
	function makeCoffee() {
		var amtPaid = $scope.amtPaid;
		var name = $scope.name;
		$scope.submissionError = false;
		$scope.submissionFailureInvent = false;
		$scope.submissionFailureFunds = false;
		$scope.submissionSuccess = false;
		
		CoffeeMakerService.makeCoffee(name, amtPaid)
			.then(function(data) {
				$scope.change = data.change;
				$scope.submissionSuccess = true;			
			}, function(response) {
		            	console.error('Error while making recipe: ');
				console.log(response.data);
                                
	                	if (response.data.result === "failureFunds") {
	                		$scope.submissionFailureFunds=true;
	                	} else if (response.data.result === "failureInventory") {
	                		$scope.submissionFailureInvent=true;
	                	} else {
	                		$scope.submissionError=true;	
	                	}
                                $scope.change = response.data.change;
                                if ($scope.change < 0) {
	                                $scope.change = 0;
                                }
                                $scope.error = "Error while making recipe";
			});
	}
	
	CoffeeMakerService.getRecipes()
		.then(function(recipes) {
			$scope.recipes = recipes;
		});
}]);
