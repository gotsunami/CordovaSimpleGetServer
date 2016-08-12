## SimpleGetServer: embedded httpd for cordova ##

Supported platform:
* Android

## Introduction ##

SimpleGetServer is based on CorHttpd by Liming Xie: https://github.com/floatinghotpot/cordova-httpd.

From this plugin, you can browse your android device (locally or remotely) to access files.

But the main goal of this plugin is different, it aims at updating the app behaviour or content reading GET parameters.

## How to use SimpleGetServer? ##

Add the plugin to your cordova project:

    cordova plugin add https://github.com/gotsunami/CordovaSimpleGetServer.git

Quick start, copy the demo files, and just build to play.

    cp -r plugins/com.outofpluto.cordova.simplegetserver/demo/* www/
    
## Javascript APIs ##

```javascript

startServer( options, success_callback, error_callback );

stopServer( success_callback, error_callback );

getURL( success_callback, error_callback );

getLocalPath( success_callback, error_callback );

setListener( success_callback, error_callback );
```

Example code: (read the comments)

```javascript
```
