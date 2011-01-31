/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nubotech.client.ui;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.nubotech.client.resources.MobileResources;

/**
 *
 * @author jonnakkerud
 */
public class Toggle extends Composite implements HasClickHandlers {

    FlowPanel main;

    private String value;
    private ToggleWidget tw;

    // http://jqr.github.com/2009/08/05/iphone-toggle-switches.html

    public Toggle(String text) {
        this(text, false);
    }

    public Toggle(String text, boolean selected) {
        initWidget(main = new FlowPanel());

        UnsunkSpan us = new UnsunkSpan(text);
        us.getElement().getStyle().setProperty("verticalAlign", "middle");
        main.add(us);

        tw = new ToggleWidget(selected);
        tw.getElement().getStyle().setProperty("float", "right");
        main.add(tw);
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    public boolean isSelected() {
        return tw.isSelected();
    }

    public void setSelected(boolean selected) {
        tw.setSelected(selected);
    }

    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return tw.addClickHandler(handler);
    }

    class UnsunkSpan extends Widget {
        public UnsunkSpan(String text) {
            setElement(DOM.createSpan());
            setText(text);
        }

        public void setText(String text) {
            DOM.setInnerText(getElement(), text);
        }
    }

    class ToggleWidget extends Widget implements HasClickHandlers {
        Element lOn;
        Element sOn;
        Element lOff;
        Element sOff;
        Element handle;
        private boolean selected;

        public ToggleWidget(boolean sel) {
            setElement(Document.get().createDivElement());
            setStyleName(MobileResources.INSTANCE.appearanceCss().iPhoneCheckContainer());
            getElement().setAttribute("style", "width:83px;");

            sinkEvents(Event.ONCLICK);

            //<input type="checkbox" class="normal" checked="checked"/>

            lOn = Document.get().createLabelElement();
            lOn.setClassName(MobileResources.INSTANCE.appearanceCss().iPhoneCheckLabelOn());
            getElement().appendChild(lOn);
            sOn = Document.get().createSpanElement();
            sOn.setInnerText("ON");
            lOn.appendChild(sOn);

            // <label class="iPhoneCheckLabelOff" id="" style="width: 143px;"><span style="margin-right: 0px;">Male</span></label>
            //<label class="iPhoneCheckLabelOff" id="" style="width: 143px;"><span style="margin-right: -86px;">Male</span></label>
            lOff = Document.get().createLabelElement();
            lOff.setClassName(MobileResources.INSTANCE.appearanceCss().iPhoneCheckLabelOff());
            getElement().appendChild(lOff);
            sOff = Document.get().createSpanElement();
            sOff.setInnerText("OFF");
            lOff.appendChild(sOff);


            //<div class="iPhoneCheckHandle" style="width: 56px; left: 0px;"><div class="iPhoneCheckHandleRight"><div class="iPhoneCheckHandleCenter"/></div></div>
            //<div class="iPhoneCheckHandle" style="width: 56px; left: 86px;"><div class="iPhoneCheckHandleRight"><div class="iPhoneCheckHandleCenter"/></div></div>
            handle = Document.get().createDivElement();
            handle.setClassName(MobileResources.INSTANCE.appearanceCss().iPhoneCheckHandle());
            getElement().appendChild(handle);
            Element handleR = Document.get().createDivElement();
            handleR.setClassName(MobileResources.INSTANCE.appearanceCss().iPhoneCheckHandleRight());
            handle.appendChild(handleR);
            Element handleC = Document.get().createDivElement();
            handleC.setClassName(MobileResources.INSTANCE.appearanceCss().iPhoneCheckHandleCenter());
            handleR.appendChild(handleC);

            setSelected(sel);
        }

        @Override
        public void onBrowserEvent(Event e) {
            switch (DOM.eventGetType(e)) {
                case Event.ONCLICK:
                    // remove selected label
                    setSelected(!selected);

            }
            super.onBrowserEvent(e);
        }

        public HandlerRegistration addClickHandler(ClickHandler handler) {
            return addDomHandler(handler, ClickEvent.getType());
        }

        void setSelected(boolean selected) {
            this.selected = selected;
            if (selected) {
                // <span style="margin-right: -43px;">OFF</span></label><label class="iPhoneCheckLabelOn" style="width: 47px; display: block;"><span style="margin-left: 0px;">ON</span></label><div class="iPhoneCheckHandle" style="width: 34px; left: 43px;"><div class="iPhoneCheckHandleRight"><div class="iPhoneCheckHandleCenter"/></div></div></div>
                lOn.setAttribute("style", "width:47px;");
                sOn.setAttribute("style", "margin-left: 0px;");
                lOff.setAttribute("style", "width:78px;");
                sOff.setAttribute("style", "margin-left: -43px;");
                handle.setAttribute("style", "width:34px; left:43px");
            }
            else {
                // <span style="margin-right: 0px;">OFF</span></label><label class="iPhoneCheckLabelOn" style="width: 4px; display: block;"><span style="margin-left: -43px;">ON</span></label><div class="iPhoneCheckHandle" style="width: 34px; left: 0px;"><div class="iPhoneCheckHandleRight"><div class="iPhoneCheckHandleCenter"/></div></div></div>
                lOn.setAttribute("style", "width:4px;");
                sOn.setAttribute("style", "margin-left: -43px;");
                lOff.setAttribute("style", "width:78px;");
                sOff.setAttribute("style", "margin-left: 0px;");
                handle.setAttribute("style", "width:34px; left:0px");

            }
        }

        public boolean isSelected() {
            return this.selected;
        }

    }


}
