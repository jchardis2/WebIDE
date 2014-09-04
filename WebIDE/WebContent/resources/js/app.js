var app = angular.module('webIDE', [ 'ui.bootstrap' ]);

var projects = [ {
	id : "1",
	name : "TestProject1"
}, {
	id : "2",
	name : "TestProject2"
}, {
	id : "3",
	name : "TestProject3"
}, {
	id : "4",
	name : "TestProject4"
} ];


var nav = {
	url : "resources/html/nav.html"
};

app.controller('PageCntl', function($scope) {
	$scope.nav = nav;
	$scope.url = "resources/html/projects.html";

	$scope.setPageUrl = function(url) {
		$scope.url = url;
	}
});

app.controller('ProjectCntl', function($scope) {
	$scope.projects = projects;
	$scope.project = projects[0];

	$scope.setProject = function(index) {
		$scope.project = projects[id];
	}
});

