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
		name : {
			val : '',
			title : 'Name',
			type : 'text'
		},
		type : {
			val : '',
			title : 'Type',
			type : 'text'
		},
		length : {
			val : '',
			title : 'Length/Size',
			type : 'text'
		},
		defaultVal : {
			val : '',
			title : 'Default Value',
			type : 'text'
		},
		collation : {
			val : '',
			title : 'Collation',
			type : 'text'
		},
		attributes : {
			val : '',
			title : 'Attributes',
			type : 'text'
		},
		isNull : {
			val : '',
			title : 'Null',
			type : 'text'
		},
		autoIncr : {
			val : '',
			title : 'Auto Increment',
			type : 'text'
		},
		comments : {
			val : '',
			title : 'Comment',
			type : 'text'
		},
		mime : {
			val : '',
			title : 'mime',
			type : 'text'
		},
		browserTransform : {
			val : '',
			title : 'Browser Transform',
			type : 'text'
		},
		transformOptions : {
			val : '',
			title : 'Transform Options',
			type : 'text'
		}
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
		if ($location.path()) {
			$scope.url = $location.path();
		} else {
			$scope.url = "projects.html";
		}
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
		$filter, sharedUpdateService) {

	$scope.$filter = $filter;

	$scope.$location = $location;
	$scope.init = function() {
		$scope.dbs = [];
		$scope.getDbs($scope.initFromSearch);
		$scope.showDatabases = true;

		// Modifying db
		$scope.newDB = {};
		$scope.newTable = {};
		$scope.newColumn = [ columnTemplate() ];
		$scope.newColumnKeys = Object.keys($scope.newColumn[0]);
		$scope.selectedDbs = [];

		// extras
		$scope.alerts = [];
	}

	$scope.initFromSearch = function() {
		if ($scope.$location.search().db) {
			$scope.db = $scope.$filter('filter')($scope.dbs,
					$scope.$location.search().db, true)[0];
			if ($scope.$location.search().table) {
				var table = $scope.$filter('filter')($scope.db.tables,
						$scope.$location.search().table, true);
				if (table) {
					$scope.table = table[0];
					$scope.getTableMetaData();
				}
			}
		}
		$scope.updateLocation();
	};

	// getters and setters
	$scope.getDbs = function(callback) {
		var data = {};
		data.db = $scope.db;
		$http.post('/rest/db/getDbs', {
			params : data
		}).success(function(data, status, headers, config) {
			// this callback will be called asynchronously
			// when the response is available
			$scope.dbs = data.dbs;
			if (callback) {
				callback();
			}
			return data;
		}).error(function(data, status, headers, config) {
			$scope.alerts = data.as;
		});
	};
	$scope.getTableMetaData = function() {
		var data = {};
		data.db = $scope.db.name;
		data.table = $scope.table.name;
		$http.post('/rest/db/getTableMetaData', data, {
			params : data
		}).success(function(data, status, headers, config) {
			// this callback will be called
			// asynchronously
			// when the response is available
			$scope.tableMetaData = data.tableMeta;
			return data;
		}).error(function(data, status, headers, config) {
			$scope.alerts = data.as;
		});
	};
	$scope.getTableData = function() {
		var data = {};
		data.db = $scope.db.name;
		data.table = $scope.table.name;
		$http.post('/rest/db/getTableData', data, {
			params : data
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
			$scope.alerts = data.as;
		});
	};

	// Updating DB
	$scope.addNewDB = function() {

		var data = {};
		data.db = $scope.newDB.name;
		$http.post('/rest/db/addNewDB', data, {
			params : data
		}).success(function(data, status, headers, config) {
			// this callback will be called
			// asynchronously
			// when the response is available
			$scope.returnVal = data;
			$scope.getDbs();
			return data;
		}).error(function(data, status, headers, config) {
			$scope.alerts = data.as;
		});
	};
	$scope.deleteDB = function() {
		var i = 0;
		var data = {};
		var names = [];
		for (i = 0; i < $scope.selectedDbs.length; i++) {
			names[i] = $scope.selectedDbs[i].name;
		}
		data.names = JSON.stringify(names);
		$http.post('/rest/db/deleteDB', data, {
			params : data
		}).success(function(data, status, headers, config) {
			// this callback will be called
			// asynchronously
			// when the response is available
			$scope.returnVal = data;
			$scope.getDbs();
			return data;
		}).error(function(data, status, headers, config) {
			$scope.alerts = data.as;
		});
	};

	$scope.addNewTable = function() {

		var data = {};
		$scope.newTable.cols = $scope.newColumn;
		data.table = $scope.newTable;
		$http.post('/rest/db/addNewTable', data, {
			params : data
		}).success(function(data, status, headers, config) {
			// this callback will be called
			// asynchronously
			// when the response is available
			$scope.returnVal = data;
			$scope.getDbs();
			return data;
		}).error(function(data, status, headers, config) {
			$scope.alerts = data.as;
		});
	};

	$scope.addNewColumn = function() {
		$scope.newColumn[$scope.newColumn.length] = columnTemplate();
	}

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
			$scope.$location.search('db', $scope.db.name);
			if ($scope.table) {
				$scope.locations[2] = {};
				$scope.locations[2].name = $scope.table.name;
				$scope.locations[2].db = $scope.db;
				$scope.locations[2].table = $scope.table;
				$scope.location += 'table=' + $scope.table.name;
				$scope.$location.search('table', $scope.table.name);
				$scope.setShownContent('showTableMetadata');
			} else {
				delete $scope.$location.search().table;
				$scope.setShownContent('showDatabaseTables');
			}
		} else {
			delete $scope.$location.search().db;
			delete $scope.$location.search().table;
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

	// alerts
	$scope.closeAlert = function(index) {
		$scope.alerts.splice(index, 1);
		if ($scope.alerts.length == 0) {
			$scope.alerts = [];
		}
	};
	$scope.removeAlerts = function() {
		$scope.alerts = [];
	};

	// final calls for setup
	$scope.init();
});
