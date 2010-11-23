/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nubotech.client.geo;

/**
 * {@link PositionCallback} wrapper which makes sure that a call to either
 * {@link #onSuccess(Position)} or {@link #onFailure(PositionError)} is executed
 * at most once.
 *
 * @see GeolocationImplHtml5AndFirefox
 *
 * @author bguijt
 */
public class AtMostOneCallPositionCallback implements PositionCallback {

  private PositionCallback wrappedCallback;
  private int callCount = 0;

  public AtMostOneCallPositionCallback(PositionCallback wrappedCallback) {
    this.wrappedCallback = wrappedCallback;
  }

  /**
   * Calls the wrapped <code>onFailure()</code> only if it is the first call
   */
  public void onFailure(PositionError error) {
    if (callCount == 0) {
      callCount++;
      wrappedCallback.onFailure(error);
    }
  }

  /**
   * Calls the wrapped <code>onSuccess()</code> only if it is the first call
   */
  public void onSuccess(Position position) {
    if (callCount == 0) {
      callCount++;
      wrappedCallback.onSuccess(position);
    }
  }
}

