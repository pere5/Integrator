/**
 * Created by Per Eriksson on 2017-01-31.
 */

integratorApp.controller('indexController', ['$scope', '$http', ($scope, $http) => {
    $scope.categories = {
        fuzzy: [{}],
        jaroWinkler: [{}]
    };

    $scope.selected = {
        fuzzy: [""],
        jaroWinkler: [""]
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

    $scope.fuzzyClicked = () => {
        let category = $scope.selected.fuzzy[0];
        getChildrenForCategory(category, response => {
            console.log("**fuzzy** " + category + "=[" + response + "]");
        });
    };

    $scope.jaroWinklerClicked = () => {
        let category = $scope.selected.jaroWinkler[0];
        getChildrenForCategory(category, response => {
            console.log("**jaroWinkler** " + category + "=[" + response + "]");
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