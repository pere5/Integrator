/**
 * Created by Per Eriksson on 2017-01-31.
 */

integratorApp.controller('indexController', ['$scope', '$http', ($scope, $http) => {
    $scope.categories = {
        fuzzy: [],
        jaroWinkler: [],
        levenshtein: []
    };

    $scope.selected = {
        fuzzy: [],
        jaroWinkler: [],
        levenshtein: []
    };

    $scope.language = "English";

    let getChildrenForCategory = (category, language, callback) => {
        $http({
            method: 'POST',
            data: {category: category, language: language},
            url: '/getChildrenForCategory'
        }).then(function successCallback(response) {
            callback(response.data);
        }, function errorCallback(response) {
            console.log("Error in indexController.getChildrenForCategory: " + response)
        });
    };

    $scope.levenshteinClicked = (index, language) => {
        let category = $scope.selected.levenshtein[index];
        getChildrenForCategory(category, language, categories => {
            console.log("**levenshtein** " + category + "=[" + categories + "]");
            $scope.categories.levenshtein.length = index + 1;
            $scope.categories.levenshtein[index + 1] = categories;
        });
    };

    $scope.fuzzyClicked = (index, language) => {
        let category = $scope.selected.fuzzy[index];
        getChildrenForCategory(category, language, categories => {
            console.log("**fuzzy** " + category + "=[" + categories + "]");
            $scope.categories.fuzzy.length = index + 1;
            $scope.categories.fuzzy[index + 1] = categories;
        });
    };

    $scope.jaroWinklerClicked = (index, language) => {
        let category = $scope.selected.jaroWinkler[index];
        getChildrenForCategory(category, language, categories => {
            console.log("**jaroWinkler** " + category + "=[" + categories + "]");
            $scope.categories.jaroWinkler.length = index + 1;
            $scope.categories.jaroWinkler[index + 1] = categories;
        });
    };

    $scope.getCategorySuggestions = (flawedCategory, language) => {
        $http({
            method: 'POST',
            data: {flawedCategory: flawedCategory, language: language},
            url: '/getCategorySuggestions'
        }).then(function successCallback(response) {
            $scope.selected.fuzzy[0] = response.data.fuzzyScores[0];
            $scope.selected.jaroWinkler[0] = response.data.jaroWinklerScores[0];
            $scope.selected.levenshtein[0] = response.data.levenshteinScores[0];
            $scope.categories.fuzzy[0] = response.data.fuzzyScores;
            $scope.categories.jaroWinkler[0] = response.data.jaroWinklerScores;
            $scope.categories.levenshtein[0] = response.data.levenshteinScores;
            $scope.jaroWinklerClicked(0, language);
            $scope.fuzzyClicked(0, language);
            $scope.levenshteinClicked(0, language);
        }, function errorCallback(response) {
            console.log("Error in indexController.getCategorySuggestions: " + response)
        });
    };


}]);