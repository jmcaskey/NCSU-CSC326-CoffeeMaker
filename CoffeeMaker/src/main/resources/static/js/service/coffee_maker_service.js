'use strict';
 
angular.module('myApp').factory('CoffeeMakerService', ['$http', '$q', function($http, $q){
    return {
    	getRecipes: function() {
            var deferred = $q.defer();
    		$http.get("/api/v1/recipes")
				.then(function (response) {
	  				deferred.resolve(response.data);
	  			}, function(errResponse) {
	  				console.error('Error fetching recipes');
                    deferred.reject(errResponse);
	  			});
    		return deferred.promise;
    	},
    	
    	addRecipe: function(recipe) {
            var deferred = $q.defer();
            $http.post('/api/v1/recipes', recipe)
                .then(function (response) {
                    deferred.resolve(response.data);
                }, function(errResponse) {
                    console.error('Error while adding recipe');
                    deferred.reject(errResponse);
                });            
            return deferred.promise;
        },
        
    	editRecipe: function(recipe) {
            var deferred = $q.defer();
            $http.put('/api/v1/recipes', recipe)
                .then(function (response) {
                    deferred.resolve(response.data);
                }, function(errResponse){
                    console.error('Error while adding recipe');
                    deferred.reject(errResponse);
                });            
            return deferred.promise;
        },
        
        deleteRecipe: function(recipe) {
    		var deferred = $q.defer();
            $http.delete("/api/v1/recipes/" + recipe)
                .then(function (response) {
                	console.log(response);
                    deferred.resolve(response.data);
                },
                function(rejection){
                    console.error('Error while deleting recipe');
                    console.log(rejection);
                    deferred.reject(rejection);
                });            
            return deferred.promise;
        },
        
		getInventory: function() {
	        var deferred = $q.defer();
	        $http.get('/api/v1/inventory')
	            .then(function (response) {
	                deferred.resolve(response.data);
	            }, function(errResponse){
	                console.error('Error while getting inventory');
	                deferred.reject(errResponse);
	            });	        
	        return deferred.promise;
	    },
	    
		updateInventory: function(inventory) {
	        var deferred = $q.defer();
	        $http.put('/api/v1/inventory', inventory)
	            .then(function (response) {
	                deferred.resolve(response.data);
	            }, function(errResponse) {
	                console.error('Error while updating Inventory');
	                deferred.reject(errResponse);
	            });	        
	        return deferred.promise;
	    },
	    
		makeCoffee: function(recipe, amtPaid) {        
    		var deferred = $q.defer();
            $http.post("/api/v1/makecoffee/" + recipe, amtPaid)
                .then(function (response) {
                    deferred.resolve(response.data);
                }, function(errResponse){
                    console.error('Error while making recipe');
                    deferred.reject(errResponse);
                    console.log(errResponse);
                });
            return deferred.promise;
        }
    };
}]);