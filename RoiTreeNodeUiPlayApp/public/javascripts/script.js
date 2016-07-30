var myApp = angular.module('app',[]);

myApp.controller('TestCtrl', function($scope){
  var tabClasses;
  
  function initTabs() {
    tabClasses = ["","","",""];
  }
  
  $scope.getTabClass = function (tabNum) {
    return tabClasses[tabNum];
  };
  
  $scope.getTabPaneClass = function (tabNum) {
    return "tab-pane " + tabClasses[tabNum];
  }
  
  $scope.setActiveTab = function (tabNum) {
    initTabs();
    tabClasses[tabNum] = "active";
  };
  
  $scope.tab1 = "This is first section";
  $scope.tab2 = "This is SECOND section";
  $scope.tab3 = "This is THIRD section";
  $scope.tab4 = "This is FOUTRH section";
  
  //Initialize 
  initTabs();
  $scope.setActiveTab(1);
});
  myApp.controller('MyCtrl',function($scope){
$scope.showChilds = function(index){
	 $scope.items[index].active = !$scope.items[index].active;
     collapseAnother(index);
};

var collapseAnother = function(index){
    for(var i=0; i<$scope.items.length; i++){
        if(i!=index){
            $scope.items[i].active = false;
        }
    }
};

$scope.items = [
                {
                    name: "Item1",
                    subItems: [
                        {name: "SubItem1"},
                        {name: "SubItem2"}
                    ]
                },
                {
                    name: "Item2",
                    subItems: [
                        {name: "SubItem3"},
                        {name: "SubItem4"},
                        {name: "SubItem5"}
                    ]
                },
                {
                    name: "Item3",
                    subItems: [
                        {name: "SubItem6"}
                    ]
                }
            ];


});


