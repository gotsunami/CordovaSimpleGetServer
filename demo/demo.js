var server = null;

function onLoad() {
    document.addEventListener("deviceready", onDeviceReady, false);
}

function onDeviceReady() {
    server = ( cordova && cordova.plugins && cordova.plugins.SimpleGetServer ) ? cordova.plugins.SimpleGetServer : null;
    
    startServer("htdocs");
}

function updateStatus() {
    if( server ) {
    	server.getURL(function(url){
    	    if(url.length > 0) {
    		document.getElementById('url').innerHTML = "server is up: <a href='" + url + "' target='_blank'>" + url + "</a>";
    	    } else {
    		document.getElementById('url').innerHTML = "server is down.";
    	    }
    	});
    } else {
    	alert("SimpleGetServer plugin not available/ready.");
    }
}

function startServer( wwwroot ) {
    if ( server ) {
    	server.getURL(function(url){
    	    if(url.length > 0) {
    	    	document.getElementById('url').innerHTML = "server is up: <a href='" + url + "' target='_blank'>" + url + "</a>";
	    } else {
    	    	server.startServer({
    	    	    'www_root' : wwwroot,
    	    	    'port' : 8080
    	    	}, function( url ){
		    server.setListener(function( data ) {
			var get = JSON.parse(data);
			for (var key in get) {
			    alert(key+" = "+get[key]);
			}
		    }, function ( error ){
			alert("error: "+error);
		    });
                    updateStatus();
    	    	}, function( error ){
    	    	    document.getElementById('url').innerHTML = 'failed to start server: ' + error;
    	    	});
    	    }
    	    
    	},function(){});
    } else {
    	alert('SimpleGetServer plugin not available/ready.');
    }
}
