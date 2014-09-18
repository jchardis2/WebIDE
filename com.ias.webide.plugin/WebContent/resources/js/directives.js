var webIDEInput = app.controller('WebIDEInput', [ '$scope', function($scope) {
	$scope.customer = {
		name : 'Naomi',
		address : '1600 Amphitheatre'
	};

	$scope.newColumn = [ columnTemplate2() ];
	$scope.newColumnKeys = Object.keys($scope.newColumn[0]);
} ]).directive('myInput', function() {
	var template = 1;

	return function(scope, element, attrs) {
		var type = attrs.type;
		if (!type || type == 'text') {
			appendBasicInput(scope, element, attrs);
			return;
		} else if (type == 'dropdown') {
			appendDropDownInput(scope, element, attrs);
			return;
		}

	};
});

function appendBasicInput(scope, element, attrs) {
	// attrs.$set('ngModel2', 'test');
	var newElement = angular.element('<input></input>');
	var keys = Object.keys(attrs.$attr);
	for (key in keys) {
		newElement.attr(attrs.$attr[keys[key]], attrs[keys[key]]);
	}
	newElement.val(element.val());

	// element.replaceWith("<input type='text' ng-model= '" + attrs.ngModel
	// + "'>Hello World</input>");
	element.append(newElement);
};
function appendDropDownInput(scope, element, attrs) {
	var newElement = angular.element('<select></select>');
	var keys = Object.keys(attrs.$attr);
	for (key in keys) {
		newElement.attr(attrs.$attr[keys[key]], attrs[keys[key]]);
	}

	// newElement.attr(attrs.$attr);
	newElement.val(element.val());

	element.append(newElement);
};

var DropDown = {
	appendButton : function() {
		return 'test';

	}

};

function columnTemplate2() {
	return {
		name : {
			val : '',
			title : 'Name',
			type : 'text'
		},
		type : {
			val : '',
			title : 'Type',
			type : 'dropdown',
			uioptions : {
				options : [ 'String', 'Integer', 'Decimal' ]
			}
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
