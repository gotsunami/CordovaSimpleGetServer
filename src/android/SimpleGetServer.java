package com.outofpluto.cordova.simplegetserver;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.content.Context;
import android.content.res.AssetManager;

/**
 * This class echoes a string called from JavaScript.
 */
public class SimpleGetServer extends CordovaPlugin {

    /** Common tag used for logging statements. */
    private static final String LOGTAG = "SimpleGetServer";
    
    /** Cordova Actions. */
    private static final String ACTION_START_SERVER = "startServer";
    private static final String ACTION_STOP_SERVER = "stopServer";
    private static final String ACTION_GET_URL = "getURL";
    private static final String ACTION_SET_LISTENER = "setListener";
    
    private static final String OPT_PORT = "port";
    private static final String OPT_LOCALHOST_ONLY = "localhost_only";

    private int port = 8888;
    private boolean localhost_only = false;
    
    private WebServer server = null;
    private String url = "";

    @Override
	public boolean execute(String action, JSONArray inputs, CallbackContext callbackContext) throws JSONException {

        PluginResult result = null;
        if (ACTION_START_SERVER.equals(action)) {
            result = startServer(inputs, callbackContext);
            
        } else if (ACTION_STOP_SERVER.equals(action)) {
            result = stopServer(inputs, callbackContext);
            
        } else if (ACTION_GET_URL.equals(action)) {
            result = getURL(inputs, callbackContext);
            
        } else if (ACTION_SET_LISTENER.equals(action)) {
            result = setListener(inputs, callbackContext);
            
        } else {
            Log.d(LOGTAG, String.format("Invalid action passed: %s", action));
            result = new PluginResult(Status.INVALID_ACTION);
        }
        
        if(result != null) callbackContext.sendPluginResult( result );
        
        return true;
    }
    
    private String __getLocalIpAddress() {
    	try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (! inetAddress.isLoopbackAddress()) {
                        if (inetAddress instanceof Inet4Address) {
                            String ip = inetAddress.getHostAddress();
			    Log.w(LOGTAG, "local IP: "+ ip);
			    return ip;
                    	}
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e(LOGTAG, ex.toString());
        }
    	
	return "127.0.0.1";
    }

    private PluginResult startServer(JSONArray inputs, CallbackContext callbackContext) {
	Log.w(LOGTAG, "startServer");

        JSONObject options = inputs.optJSONObject(0);
        if(options == null) return null;
        
        port = options.optInt(OPT_PORT, 8888);
        localhost_only = options.optBoolean(OPT_LOCALHOST_ONLY, false);
        
        final CallbackContext delayCallback = callbackContext;
        cordova.getActivity().runOnUiThread(new Runnable(){
		@Override
		    public void run() {
		    String errmsg = __startServer();
		    if (errmsg.length() > 0) {
			delayCallback.error( errmsg );
		    } else {
			if (localhost_only) {
			    url = "http://127.0.0.1:" + port;
			} else {
			    url = "http://" + __getLocalIpAddress() + ":" + port;
			}
	                delayCallback.success( url );
		    }
		}
	    });
        
        return null;
    }
    
    private String __startServer() {
    	String errmsg = "";
    	try {
	    Context ctx = cordova.getActivity().getApplicationContext();
	    AssetManager am = ctx.getResources().getAssets();

	    server = new WebServer(port);
	} catch (IOException e) {
	    errmsg = String.format("IO Exception: %s", e.getMessage());
	    Log.w(LOGTAG, errmsg);
	}
    	return errmsg;
    }

    private void __stopServer() {
	if (server != null) {
	    server.stop();
	    server = null;
	}
    }
    
    private PluginResult setListener(JSONArray inputs, CallbackContext callbackContext) {
	Log.w(LOGTAG, "setListener");

	if (server != null) {
	    server.setCallbackContext(callbackContext);
	} else {
	    callbackContext.error( "server not running" );
	}

        return null;
    }

    private PluginResult getURL(JSONArray inputs, CallbackContext callbackContext) {
	Log.w(LOGTAG, "getURL");

    	callbackContext.success( this.url );
        return null;
    }

    private PluginResult stopServer(JSONArray inputs, CallbackContext callbackContext) {
	Log.w(LOGTAG, "stopServer");
		
        final CallbackContext delayCallback = callbackContext;
        cordova.getActivity().runOnUiThread(new Runnable(){
		@Override
		    public void run() {
		    __stopServer();
		    url = "";
		    delayCallback.success();
		}
	    });
        
        return null;
    }

    /**
     * Called when the system is about to start resuming a previous activity.
     *
     * @param multitasking		Flag indicating if multitasking is turned on for app
     */
    public void onPause(boolean multitasking) {
    	//if(! multitasking) __stopServer();
    }

    /**
     * Called when the activity will start interacting with the user.
     *
     * @param multitasking		Flag indicating if multitasking is turned on for app
     */
    public void onResume(boolean multitasking) {
    	//if(! multitasking) __startServer();
    }

    /**
     * The final call you receive before your activity is destroyed.
     */
    public void onDestroy() {
    	__stopServer();
    }
}
