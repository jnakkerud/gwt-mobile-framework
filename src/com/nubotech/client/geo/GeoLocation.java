/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nubotech.client.geo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.core.client.JavaScriptObject;

/**
 *
 * @author jonnakkerud
 */
public final class GeoLocation extends JavaScriptObject  {

    protected GeoLocation() {
    }

    public static native boolean isSupported() /*-{
        return !!$wnd.navigator.geolocation;
    }-*/;

    public void getCurrentPosition(PositionCallback cb) {
        getCurrentPosition(this, cb);
    }

    public void getCurrentPosition(PositionOptions options, PositionCallback callback) {
        getCurrentPosition(this, options, callback);
    }

    /**
    * Returns the Geolocation instance.
    *
    * @return the Geolocation instance
    */
    public static native GeoLocation getGeolocation() /*-{
        return $wnd.navigator.geolocation;
    }-*/;

    /*
     * Helper method for the getCurrentPosition() / watchPosition() methods
     */
    protected static final void handleSuccess(PositionCallback callback, Position position) {
        UncaughtExceptionHandler ueh = GWT.getUncaughtExceptionHandler();
        if (ueh != null) {
            try {
                callback.onSuccess(position);
            } catch (Throwable t) {
                ueh.onUncaughtException(t);
            }
        } else {
            callback.onSuccess(position);
        }
    }

    /*
     * Helper method for the getCurrentPosition() / watchPosition() methods
     */
    protected static final void handleError(PositionCallback callback, PositionError error) {
        UncaughtExceptionHandler ueh = GWT.getUncaughtExceptionHandler();
        if (ueh != null) {
            try {
                callback.onFailure(error);
            } catch (Throwable t) {
                ueh.onUncaughtException(t);
            }
        } else {
            callback.onFailure(error);
        }
    }

    public void getCurrentPosition(GeoLocation geo, PositionCallback callback) {
        getCurrentPosition(geo, null, callback);
    }

    /**
     * Wraps the specified <code>callback</code> in an
     * {@link AtMostOneCallPositionCallback} instance, and passes that to the
     * getCurrentPosition() call.
     */
    public void getCurrentPosition(GeoLocation geo, PositionOptions options, PositionCallback callback) {
        _getCurrentPosition(geo, new AtMostOneCallPositionCallback(callback), options);
    }

    protected native void _getCurrentPositionTest(GeoLocation geo, PositionCallback callback, PositionOptions options) /*-{
    $wnd.navigator.geolocation.getCurrentPosition(
        function(pos){
            //var lat = pos.coords.latitude;
            //var long = pos.coords.longitude;
            $wnd.console.log(pos);
        },
        function(mess){
           $wnd.console.log(mess);
           // fail
        }
    );
    }-*/;

    protected native void _getCurrentPosition(GeoLocation geo, PositionCallback callback, PositionOptions options) /*-{
        geo.getCurrentPosition(
            function(position) {
                @com.nubotech.client.geo.GeoLocation::handleSuccess(Lcom/nubotech/client/geo/PositionCallback;Lcom/nubotech/client/geo/Position;) (callback, position);
            },
            function(error) {
                @com.nubotech.client.geo.GeoLocation::handleError(Lcom/nubotech/client/geo/PositionCallback;Lcom/nubotech/client/geo/PositionError;) (callback, error);
            },
            options
        );
    }-*/;

}
