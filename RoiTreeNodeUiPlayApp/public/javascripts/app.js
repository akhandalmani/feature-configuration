var app = angular.module('plunker', ['ngRoute']);

app.config(function($routeProvider) {
    $routeProvider
    .when('/', {
        controller: 'homeController',
        template: 'asd'
    })
    .when('/about', {
        controller: 'aboutController',
        templateUrl: 'testtempl'
    })
    .otherwise({
        controller: '404Controller',
        templateUrl: 'testtempl'
    });
});

app.controller('appController', function($scope, $location) {
    $scope.isActive = function(path) {
        return $location.path() === path;
    };
});

app.controller('homeController', function($scope) {});

app.controller('aboutController', function($scope) {});

app.controller('404Controller', function($scope, $location) {
    $scope.path = $location.path();
    $scope.back = function() {
        history.back();
    };
});