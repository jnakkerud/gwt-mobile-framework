/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nubotech.client.ui;

import com.nubotech.client.LogUtil;
import com.nubotech.client.resources.MobileResources;

/**
 * TODO get working
 *
 * @author jonnakkerud
 */
public class FlipTransition extends Transition {

    @Override
    public void perform(View back, View front, boolean isReverse) {

        //front.addStyleName(Resources.INSTANCE.appearanceCss().FlipPanel());
        if (front == null) return;
        LogUtil.log("front " +front.getElement().getId());
        LogUtil.log("back " + back.getElement().getId());

        // addStyle back front
        //front.addStyleName(Resources.INSTANCE.appearanceCss().front());
        //back.addStyleName(Resources.INSTANCE.appearanceCss().back());

        if (front.getStyleName().indexOf("flip") > -1) {
        front.removeStyleName(MobileResources.INSTANCE.appearanceCss().flip());
        back.removeStyleName(MobileResources.INSTANCE.appearanceCss().flip());

        }
        else {
        front.addStyleName(MobileResources.INSTANCE.appearanceCss().flip());
        back.addStyleName(MobileResources.INSTANCE.appearanceCss().flip());

        }

    }

}
