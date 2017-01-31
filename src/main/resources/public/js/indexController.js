/**
 * Created by Per Eriksson on 2017-01-31.
 */

integratorApp.controller('indexController', ['$scope', '$http', function ($scope, $http) {
    $scope.categories = {fuzzyScores: [], jaroWinklerScores: []};

    $scope.getCategorySuggestions = (flawedCategory) => {
        $http({
            method: 'POST',
            data: {flawedCategory: flawedCategory},
            url: '/getCategorySuggestions'
        }).then(function successCallback(response) {
            $scope.selectedCategory = response.data.fuzzyScores[0];
            $scope.categories.fuzzyScores = response.data.fuzzyScores;
            $scope.categories.jaroWinklerScores = response.data.jaroWinklerScores;

        }, function errorCallback(response) {
            console.log("Error in indexController.getCategorySuggestions: " + response)
        });
    };


}]);