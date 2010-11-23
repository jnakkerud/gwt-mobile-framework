/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nubotech.client.geo;

import com.google.gwt.core.client.JavaScriptObject;

/**
 *
 * @author jonnakkerud
 */
public class Position extends JavaScriptObject {

  protected Position() {
  }

  /**
   * Returns a set of geographic coordinates together with their associated
   * accuracy, as well as a set of other optional attributes such as altitude
   * and speed.
   *
   * @return the coordinates
   * @see <a href="http://www.w3.org/TR/geolocation-API/#coords">W3C Geolocation
   *      API - Position.coords</a>
   */
  public final native Coordinates getCoords() /*-{
    return this.coords;
  }-*/;

  /**
   * Returns the time when the Position object was acquired.
   *
   * @return the time when the Position object was acquired
   * @see <a href="http://www.w3.org/TR/geolocation-API/#timestamp">W3C
   *      Geolocation API - Position.timestamp</a>
   */
  public final native int getTimestamp() /*-{
    return this.timestamp;
  }-*/;
}

