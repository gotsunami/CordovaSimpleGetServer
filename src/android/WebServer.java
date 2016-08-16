package com.outofpluto.cordova.simplegetserver;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Properties;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;

public class WebServer extends NanoHTTPD
{
    private CallbackContext callbackContext = null;

    public WebServer( int port ) throws IOException {
	super(port);
    }

    public void setCallbackContext(CallbackContext callbackContext) {
	this.callbackContext = callbackContext;
    }
    
    @Override
	public Response serve( String uri, String method, Properties header, Properties parms, Properties files )
    {
	Response response = new Response( HTTP_OK, "application/json", "{\"id\":\"DisMoiAndroid\"}" );
	response.addHeader("Access-Control-Allow-Origin", "*");

	String get = "{";
 	for (Object key : parms.keySet()) {
 	    String value = parms.getProperty((String) key);
 	    get += "\""+(String)key+"\" : \""+value+"\", ";
 	}
	if (get.length() > 1) {
	    get = get.substring(0, get.length()-2);
	}
	get += "}";
	PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, get);
	pluginResult.setKeepCallback(true);
	callbackContext.sendPluginResult(pluginResult);
 	return response;
     }
}
