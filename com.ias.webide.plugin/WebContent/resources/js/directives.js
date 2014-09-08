app.directive('breadcrumb', function() {

	function link(scope, element, attrs) {
		var locations;

		function updateTime() {
			var i;
			for (i = 0; i < locations.length; i++) {
			}
			element.text("TEst");
		}

		scope.$watch(attrs.locations, function(value) {
			format = value;
			updateTime();
		});

		// element.on('$destroy', function() {
		// $interval.cancel(timeoutId);
		// });

		// start the UI update process; save the timeoutId for canceling
		// timeoutId = $interval(function() {
		// updateTime(); // update DOM
		// }, 1000);
	}

	return {
		link : link
	};
});