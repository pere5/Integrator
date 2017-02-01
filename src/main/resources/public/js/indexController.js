/**
 * Created by Per Eriksson on 2017-01-31.
 */

integratorApp.controller('indexController', ['$scope', '$http', ($scope, $http) => {
    $scope.categories = {
        fuzzy: [],
        jaroWinkler: []
    };

    $scope.selected = {
        fuzzy: [],
        jaroWinkler: []
    };

    let getChildrenForCategory = (category, callback) => {
        $http({
            method: 'POST',
            data: {category: category},
            url: '/getChildrenForCategory'
        }).then(function successCallback(response) {
            callback(response.data);
        }, function errorCallback(response) {
            console.log("Error in indexController.getChildrenForCategory: " + response)
        });
    };

    $scope.fuzzyClicked = index => {
        let category = $scope.selected.fuzzy[index];
        getChildrenForCategory(category, categories => {
            console.log("**fuzzy** " + category + "=[" + categories + "]");
            $scope.categories.fuzzy.length = index + 1;
            $scope.categories.fuzzy[index + 1] = categories;
        });
    };

    $scope.jaroWinklerClicked = index => {
        let category = $scope.selected.jaroWinkler[0];
        getChildrenForCategory(category, categories => {
            console.log("**jaroWinkler** " + category + "=[" + categories + "]");
            $scope.categories.jaroWinkler[index + 1] = categories;
        });
    };

    $scope.getCategorySuggestions = (flawedCategory) => {
        $http({
            method: 'POST',
            data: {flawedCategory: flawedCategory},
            url: '/getCategorySuggestions'
        }).then(function successCallback(response) {
            $scope.selected.fuzzy[0] = response.data.fuzzyScores[0];
            $scope.selected.jaroWinkler[0] = response.data.jaroWinklerScores[0];
            $scope.categories.fuzzy[0] = response.data.fuzzyScores;
            $scope.categories.jaroWinkler[0] = response.data.jaroWinklerScores;

        }, function errorCallback(response) {
            console.log("Error in indexController.getCategorySuggestions: " + response)
        });
    };


}]);