/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nubotech.client.ui;

import com.nubotech.client.ui.mobile.TouchableFlowPanel;
import com.nubotech.client.ui.mobile.TouchableComposite;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.nubotech.client.Utils;
import com.nubotech.client.ui.mobile.MobileScrollPanel;

/**
 *
 * @author jonnakkerud
 */
public class ContentPanel extends TouchableFlowPanel {

    TouchableComposite header;
    SimplePanel container;
    TouchableComposite footer;

    public void setHeader(Widget header) {
        this.header = new HeaderWrapper(header);
        add(this.header);
    }

    public void setFooter(Widget w_footer) {
        this.footer = new TouchableComposite(w_footer);
        this.footer.addStyleName("footer");
        add(footer);
    }

    public void setContents(Widget contents) {
        this.container = new MobileScrollPanel();
        this.container.setWidget(contents);
        this.container.addStyleName("container");
        contents.addStyleName("contents");
        add(this.container);
    }

    public TouchableComposite getHeader() {
        return header;
    }

    public SimplePanel getContainer() {
        return container;
    }

    public Widget getContents() {
        return container.getWidget();
    }

    @Override
    protected void onLoad() {
        super.onLoad();

        if (Utils.isMobile()) {
            // set height of container for mobile

            //int offset = header.getOffsetHeight() + (footer != null ? footer.getOffsetHeight() : 0);
            //container.setHeight((Window.getClientHeight()-offset)+"px");

            setHeight();            
        }
        else {
            //int offset = header.getOffsetHeight() + (footer != null ? footer.getOffsetHeight() : 0);
            //container.setHeight((Window.getClientHeight()-offset)+"px");

            setHeight();
            container.getElement().getStyle().setOverflow(Style.Overflow.AUTO);

        }
    }

    public void setHeight() {
        int headerH = header.getOffsetHeight();
        int footerH = (footer != null ? footer.getOffsetHeight() : 0);
        int wrapperH = Window.getClientHeight() - headerH - footerH;
        container.setHeight(wrapperH+"px");
    }

    class HeaderWrapper extends TouchableComposite {
        SimplePanel p;
        public HeaderWrapper(Widget wrapped) {
            initWidget(p = new SimplePanel());
            p.setWidget(wrapped);
            addStyleName("headerWrapper");
        }
    }

}
