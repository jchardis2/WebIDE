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

function columnTemplate() {
	return {
		name : '',
		type : '',
		length : '',
		defaultVal : '',
		collation : '',
		attributes : '',
		isNull : '',
		autoIncr : '',
		comments : '',
		mime : '',
		browserTransform : '',
		transformOptions : ''
	}
};

var nav = {
	url : "nav.html"
};

app.factory('sharedUpdateService', function($rootScope) {

	var sharedUpdateService = {};
	sharedUpdateService.message = '';

	sharedUpdateService.prepForBroadcast = function(broadCastID) {
		this.broadcastItem(broadCastID);
	};

	sharedUpdateService.broadcastItem = function(broadCastID) {
		$rootScope.$broadcast(broadCastID);
	};
	return sharedUpdateService;
});

app.controller('PageCntl', function($scope, $location, sharedUpdateService) {
	$scope.init = function() {
		$scope.nav = nav;
		$scope.url = "projects.html";
		// $scope.$location.html5Mode(false);
		// $scope.$location.hashPrefix('!');
	}

	$scope.setPageUrl = function(url) {
		$scope.url = url;
		$location.path(url);
	}

	$scope.handleUpdate = function(msg) {
		sharedUpdateService.prepForBroadcast(msg);
	};

	$scope.$watch('locationPath', function(path) {
		$location.path(path);
	});
	$scope.$watch(function() {
		return $location.path();
	}, function(path) {
		$scope.locationPath = path;
	});

	$scope.init();
});

app.controller('ProjectCntl', function($scope) {
	$scope.projects = projects;
	$scope.project = projects[0];

	$scope.setProject = function(index) {
		$scope.project = projects[id];
	}
});
var dbCntl = app.controller('DBCntl', function($scope, $http, $location,
		sharedUpdateService) {
	$scope.init = function() {
		$scope.dbs = [];
		$scope.getDbs();
		$scope.updateLocation();
		$scope.showDatabases = true;

		$scope.newDB = {};
		$scope.newTable = {};
		$scope.newColumn = [ columnTemplate() ];
		$scope.newColumnKeys = Object.keys($scope.newColumn[0])
		$scope.selectedDbs = [];
	}

	// getters and setters
	$scope.getDbs = function() {
		$http({
			method : 'GET',
			url : '/db?action=listdbs'
		}).success(function(data, status, headers, config) {
			// this callback will be called asynchronously
			// when the response is available
			$scope.dbs = data.dbs;
			return data;
		}).error(function(data, status, headers, config) {
			// called asynchronously if an error occurs
			// or server returns response with an error status.
		});
	};
	$scope.getTableMetaData = function() {
		$http(
				{
					method : 'GET',
					url : '/db?action=getTableMetaData&db=' + $scope.db.name
							+ '&table=' + $scope.table.name
				}).success(function(data, status, headers, config) {
			// this callback will be called
			// asynchronously
			// when the response is available
			$scope.tableMetaData = data.tableMeta;
			return data;
		}).error(function(data, status, headers, config) {
			// called asynchronously if an error occurs
			// or server returns response with an error
			// status.
		});
	};
	$scope.getTableData = function() {
		$http(
				{
					method : 'GET',
					url : '/db?action=getTableData&db=' + $scope.db.name
							+ '&table=' + $scope.table.name
				}).success(function(data, status, headers, config) {
			// this callback will be called
			// asynchronously
			// when the response is available
			$scope.tableData = data.tableData;
			if (data.tableData.length > 0)
				$scope.tableKeys = Object.keys(data.tableData[0]);
			else
				$scope.tableKeys = "";
			return data;
		}).error(function(data, status, headers, config) {
			// called asynchronously if an error occurs
			// or server returns response with an error
			// status.
		});
	};

	// Updating DB
	$scope.addNewDB = function() {
		$http({
			method : 'GET',
			url : '/db?action=addNewDB&db=' + $scope.newDB.name
		}).success(function(data, status, headers, config) {
			// this callback will be called
			// asynchronously
			// when the response is available
			$scope.returnVal = data;
			$scope.getDbs();
			return data;
		}).error(function(data, status, headers, config) {
			// called asynchronously if an error occurs
			// or server returns response with an error
			// status.
		});
	};
	$scope.deleteDB = function() {
		var i = 0;
		var data = {};
		var names = [];
		data.action = "deleteDB";
		for (i = 0; i < $scope.selectedDbs.length; i++) {
			names[i] = $scope.selectedDbs[i].name;
		}
		data.names = JSON.stringify(names);
		$http.post('/db?', data, {
			params : data
		}).success(function(data, status, headers, config) {
			// this callback will be called
			// asynchronously
			// when the response is available
			$scope.returnVal = data;
			$scope.getDbs();
			return data;
		}).error(function(data, status, headers, config) {
			// called asynchronously if an error occurs
			// or server returns response with an error
			// status.
		});
	};

	$scope.setDb = function(db) {
		$scope.db = db
	};
	$scope.setTable = function(table) {
		$scope.table = table
	};

	$scope.setShownContent = function(contentid) {
		$scope.hideAllContent();
		if (contentid == "showDatabases") {
			$scope.showDatabases = true;
		} else if (contentid == "showDatabaseTables") {
			$scope.showDatabaseTables = true;
		} else if (contentid == "showTableMetadata") {
			$scope.showTableContent = true;
			$scope.showTableMetadata = true;
		} else if (contentid == "showTableData") {
			$scope.showTableContent = true;
			$scope.showTableData = true;
		}
	};

	$scope.setShownActionContent = function(contentid) {
		$scope.hideAllActionContent();
		if (contentid == "showAddDatabases") {
			$scope.showAddDatabases = true;
		} else if (contentid == "showDeleteDatabases") {
			$scope.showDeleteDatabases = true;
		} else if (contentid == "showAddTable") {
			$scope.showAddTable = true;
		}
	};

	$scope.hideAllContent = function() {
		$scope.showDatabases = false;
		$scope.showDatabaseTables = false;
		$scope.showTableContent = false;
		$scope.showTableData = false;
		$scope.showTableMetadata = false;
	};

	$scope.hideAllActionContent = function() {
		$scope.showAddDatabases = false;
		$scope.showDeleteDatabases = false;
		$scope.showAddTable = false;
	};

	// updaters
	$scope.updateLocation = function() {
		$scope.locations = [];
		$scope.locations[0] = {};
		$scope.locations[0].name = "Databases";
		$scope.locations[0].location = '#databases.html';
		$scope.location = "databases.html?";
		if ($scope.db) {
			$scope.locations[1] = {};
			$scope.locations[1].name = $scope.db.name;
			$scope.locations[1].db = $scope.db;
			$scope.locations[1].location = '#databases.html?db='
					+ $scope.db.name;
			$scope.location += 'db=' + $scope.db.name;
			if ($scope.table) {
				$scope.locations[2] = {};
				$scope.locations[2].name = $scope.table.name;
				$scope.locations[2].db = $scope.db;
				$scope.locations[2].table = $scope.table;
				$scope.location += 'table=' + $scope.table.name;
				$scope.setShownContent('showTableMetadata');
			} else {
				$scope.setShownContent('showDatabaseTables');
			}
		} else {
			$scope.setShownContent('showDatabases');
		}
	};

	// Actions
	$scope.deleteSelectedDbs = function() {

	}
	$scope.selectDB = function(db) {
		if (db) {
			var index = $scope.selectedDbs.indexOf(db);
			if (index >= 0) {
				$scope.selectedDbs.splice(index, 1);
			} else {
				$scope.selectedDbs[$scope.selectedDbs.length] = db;
			}
		}
	}
	$scope.deselectDB = new function(db) {
		if (db)
			$scope.selectedDbs[db.name] = "";
	}

	// listeners
	$scope.$on('updateDbs', function() {
		$scope.getDbs();
		$scope.updateLocation();
	});

	// final calls for setup
	$scope.init()
});
