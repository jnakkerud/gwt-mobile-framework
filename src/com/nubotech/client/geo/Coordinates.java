/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nubotech.client.geo;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Represents a position Coordinates object.
 *
 * @author bguijt
 * @see <a href="http://www.w3.org/TR/geolocation-API/#coordinates">W3C
 *      Geolocation API - Coordinates interface</a>
 */
public class Coordinates extends JavaScriptObject {

  protected Coordinates() {
  }

  /**
   * Returns the latitude in decimal degrees.
   *
   * @return the latitude in decimal degrees
   * @see <a href="http://www.w3.org/TR/geolocation-API/#lat">W3C Geolocation
   *      API - Coordinates.latitude</a>
   */
  public final native double getLatitude() /*-{
    return this.latitude;
  }-*/;

  /**
   * Returns the longitude in decimal degrees.
   *
   * @return the longitude in decimal degrees
   * @see <a href="http://www.w3.org/TR/geolocation-API/#lon">W3C Geolocation
   *      API - Coordinates.longitude</a>
   */
  public final native double getLongitude() /*-{
    return this.longitude;
  }-*/;

  /**
   * Returns <code>true</code> if this instance has a value for
   * {@link #getAltitude()}.
   */
  public final native boolean hasAltitude() /*-{
    return this.altitude != null;
  }-*/;

  /**
   * Returns the height of the position specified in meters. This is an optional
   * attribute. If the Geolocation API doesn't support this attribute,
   * unexpected results might be returned, or an Exception thrown. Use
   * {@link #hasAltitude()} to check whether this attribute has a value.
   *
   * @return the height of the position specified in meters
   * @see <a href="http://www.w3.org/TR/geolocation-API/#altitude">W3C
   *      Geolocation API - Coordinates.altitude</a>
   */
  public final native double getAltitude() /*-{
    return this.altitude;
  }-*/;

  /**
   * Returns the accuracy level of the position specified in meters.
   *
   * @return the accuracy level of the position specified in meters
   * @see <a href="http://www.w3.org/TR/geolocation-API/#accuracy">W3C
   *      Geolocation API - Coordinates.accuracy</a>
   */
  public final native double getAccuracy() /*-{
    return this.accuracy;
  }-*/;

  /**
   * Returns <code>true</code> if this instance has a value for
   * {@link #getAltitudeAccuracy()}.
   */
  public final native boolean hasAltitudeAccuracy() /*-{
    return this.altitideAccuracy != null;
  }-*/;

  /**
   * Returns the accuracy level of the altitude specified in meters. This is an
   * optional attribute. If the Geolocation API doesn't support this attribute,
   * unexpected results might be returned, or an Exception thrown. Use
   * {@link #hasAltitudeAccuracy()} to check whether this attribute has a value.
   *
   * @return the accuracy level of the altitude specified in meters.
   * @see <a href="http://www.w3.org/TR/geolocation-API/#altitude-accuracy">W3C
   *      Geolocation API - Coordinates.altitudeAccuracy</a>
   */
  public final native double getAltitudeAccuracy() /*-{
    return this.altitudeAccuracy;
  }-*/;

  /**
   * Returns <code>true</code> if this instance has a value for
   * {@link #getHeading()}.
   */
  public final native boolean hasHeading() /*-{
    return this.heading != null;
  }-*/;

  /**
   * Returns the heading specified in degrees counting clockwise relative to the
   * true north. This is an optional attribute. If the Geolocation API doesn't
   * support this attribute, unexpected results might be returned, or an
   * Exception thrown. Use {@link #hasHeading()} to check whether this attribute
   * has a value.
   *
   * @return the heading specified in degrees counting clockwise relative to the
   *         true north.
   * @see <a href="http://www.w3.org/TR/geolocation-API/#heading">W3C
   *      Geolocation API - Coordinates.heading</a>
   */
  public final native double getHeading() /*-{
    return this.heading;
  }-*/;

  /**
   * Returns <code>true</code> if this instance has a value for
   * {@link #getSpeed()}.
   */
  public final native boolean hasSpeed() /*-{
    return this.speed != null;
  }-*/;

  /**
   * Returns the speed of the hosting device in meters per second. This is an
   * optional attribute. If the Geolocation API doesn't support this attribute,
   * unexpected results might be returned, or an Exception thrown. Use
   * {@link #hasSpeed()} to check whether this attribute has a value.
   *
   * @return the speed of the hosting device in meters per second.
   * @see <a href="http://www.w3.org/TR/geolocation-API/#speed">W3C Geolocation
   *      API - Coordinates.speed</a>
   */
  public final native double getSpeed() /*-{
    return this.speed;
  }-*/;
}


