/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nubotech.client.geo;

/**
 *
 * @author jonnakkerud
 */
public interface PositionCallback {

  /**
   * Invoked when asking for the (current) position succeeds.
   *
   * @param position the (current) position of the hosting device
   */
  void onSuccess(Position position);

  /**
   * Invoked when asking for the (current) position fails.
   *
   * @param error the error details encountered.
   */
  void onFailure(PositionError error);

}
