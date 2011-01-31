/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nubotech.client.ui;

import com.nubotech.client.LogUtil;
import com.nubotech.client.resources.MobileResources;

/**
 *
 * @author jonnakkerud
 */
public class SwapTransition extends Transition {

    @Override
    public void perform(View new_view, View current_view, boolean isReverse) {
        if (current_view != null) {
            current_view.removeStyleName(MobileResources.INSTANCE.appearanceCss().current());
        }
        
        // reverse on parent
        if (new_view.getParentView() != null) {
            new_view.getParentView().addStyleName(MobileResources.INSTANCE.appearanceCss().reverse());
        }

        new_view.removeStyleName(MobileResources.INSTANCE.appearanceCss().reverse());
        new_view.addStyleName(MobileResources.INSTANCE.appearanceCss().current());
    }

}
