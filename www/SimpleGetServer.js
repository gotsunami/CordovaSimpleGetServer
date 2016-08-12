
var argscheck = require('cordova/argscheck'),
    exec = require('cordova/exec');

var corhttpd_exports = {};

corhttpd_exports.startServer = function(options, success, error) {
	  var defaults = {
			    'www_root': '',
			    'port': 8888,
			  };
	  
	  // Merge optional settings into defaults.
	  for (var key in defaults) {
	    if (typeof options[key] !== 'undefined') {
	      defaults[key] = options[key];
	    }
	  }
			  
  exec(success, error, "SimpleGetServer", "startServer", [ defaults ]);
};

corhttpd_exports.stopServer = function(success, error) {
	  exec(success, error, "SimpleGetServer", "stopServer", []);
};

corhttpd_exports.getURL = function(success, error) {
	  exec(success, error, "SimpleGetServer", "getURL", []);
};

corhttpd_exports.setListener = function(success, error) {
	  exec(success, error, "SimpleGetServer", "setListener", []);
};

module.exports = corhttpd_exports;

