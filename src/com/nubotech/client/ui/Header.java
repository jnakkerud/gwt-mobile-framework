/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nubotech.client.ui;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author jonnakkerud
 */
public class Header extends Composite {

    FlowPanel main = new FlowPanel();

    // heading
    String heading;

    public Header() {
        initWidget(main);
        addStyleName("header");
    }

    public void setButton(String title, boolean backButton, ClickHandler handler) {
        StyledButton button = new StyledButton(title, backButton);
        button.addClickHandler(handler);
        if (backButton) {
            main.insert(button, 0);
        }
        else {
            main.add(button);
        }
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        initGui();
    }

    private void initGui() {
        if (heading != null) {
            // <h1>Flip Page Over</h1>
            main.insert(new SimpleHeading(heading), hasBackButton() ? 1 : 0);
        }
    }

    private boolean hasBackButton() {
        boolean retval = false;
        if (main.getWidgetCount() > 0 && main.getWidget(0) != null) {
            retval =  (main.getWidget(0).getStyleName().indexOf("back") != -1);
        }
        return retval;
    }

    // <span class="button back"><span></span>Back</span>
    class StyledButton extends Widget {
        public StyledButton(String title, boolean is_back) {
            setElement(Document.get().createSpanElement());
            getElement().addClassName("button");
            if (is_back) {
                getElement().addClassName("back");
                getElement().setInnerHTML("<span></span>"+title);
            }
            else {
                getElement().setInnerText(title);
            }
        }

        public HandlerRegistration addClickHandler(ClickHandler handler) {
            return addDomHandler(handler, ClickEvent.getType());
        }
    }

    class SimpleHeading extends Widget {
        public SimpleHeading(String heading) {
            setElement(Document.get().createHElement(1));
            getElement().setInnerText(heading);
        }
    }

}
