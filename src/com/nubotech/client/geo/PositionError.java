/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nubotech.client.geo;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Represents an error encountered when asking the (current) {@link Position}.
 *
 * @author bguijt
 * @see <a href="http://www.w3.org/TR/geolocation-API/#position-error">W3C
 *      Geolocation API - PositionError interface</a>
 */
public class PositionError extends JavaScriptObject {

  public static final int UNKNOWN_ERROR = 0;
  public static final int PERMISSION_DENIED = 1;
  public static final int POSITION_UNAVAILABLE = 2;
  public static final int TIMEOUT = 3;

  protected PositionError() {
  }

  /**
   * Creates a new PositionError with the specified code and message.
   *
   * @param code any PositionError constant value (0-3)
   * @param message the error message
   */
  public static native PositionError create(int code, String message) /*-{
    return {code: code, message: message};
  }-*/;

  /**
   * Returns the error code. Must be one of {@link #UNKNOWN_ERROR},
   * {@link #PERMISSION_DENIED}, {@link #POSITION_UNAVAILABLE} or
   * {@link #TIMEOUT}.
   *
   * @return the error code
   * @see <a href="http://www.w3.org/TR/geolocation-API/#code">W3C Geolocation
   *      API - PositionError.code</a>
   */
  public final native int getCode() /*-{
    return (typeof this.code == "undefined") ? 0 : this.code;
  }-*/;

  /**
   * Returns the message describing the details of the error encountered.
   *
   * @return the error message
   * @see <a href="http://www.w3.org/TR/geolocation-API/#message">W3C
   *      Geolocation API - PositionError.message</a>
   */
  public final native String getMessage() /*-{
    return this.message;
  }-*/;
}


