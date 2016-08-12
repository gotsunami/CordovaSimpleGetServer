package com.outofpluto.cordova.simplegetserver;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Properties;
import java.net.URI;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;

public class WebServer extends NanoHTTPD
{
    private CallbackContext callbackContext = null;

    public WebServer( int port , AndroidFile wwwroot) throws IOException {
	super(port, wwwroot);
    }

    public void setCallbackContext(CallbackContext callbackContext) {
	this.callbackContext = callbackContext;
    }
    
    @Override
	public Response serve( String uri, String method, Properties header, Properties parms, Properties files )
    {
 	Response response = super.serve(uri, method, header, parms, files);

	URI obj = URI.create("http://localhost"+uri);
	PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, obj.getQuery());
	pluginResult.setKeepCallback(true);
	callbackContext.sendPluginResult(pluginResult);
 	return response;
     }
}
