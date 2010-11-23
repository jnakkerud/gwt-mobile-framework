/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nubotech.client.ui;

import com.nubotech.client.LogUtil;
import com.nubotech.client.resources.Resources;

/**
 *
 * @author jonnakkerud
 */
public class SwapTransition extends Transition {

    @Override
    public void perform(View new_view, View current_view, boolean isReverse) {
        if (current_view != null) {
            current_view.removeStyleName(Resources.INSTANCE.appearanceCss().current());
        }
        
        // reverse on parent
        if (new_view.getParentView() != null) {
            new_view.getParentView().addStyleName(Resources.INSTANCE.appearanceCss().reverse());
        }

        new_view.removeStyleName(Resources.INSTANCE.appearanceCss().reverse());
        new_view.addStyleName(Resources.INSTANCE.appearanceCss().current());
    }

}
