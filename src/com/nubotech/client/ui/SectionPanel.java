/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nubotech.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.HasCloseHandlers;
import com.google.gwt.event.logical.shared.HasOpenHandlers;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.nubotech.client.resources.MobileResources;

/**
 *
 * @author jonnakkerud
 */
public class SectionPanel extends Composite implements HasOpenHandlers<SectionPanel>, HasCloseHandlers<SectionPanel> {
    FlowPanel main;
    private HeaderContainer header;
    private final FlowPanel contents = new FlowPanel();
    boolean isOpen = false;

    interface SectionPanelImages extends ClientBundle {
        @Source("com/nubotech/client/resources/down_arrow.png")
        ImageResource disclosurePanelOpen();

        @Source("com/nubotech/client/resources/right_arrow.png")
        ImageResource disclosurePanelClosed();
    }

    public SectionPanel() {
        this(null, false);
    }

    public SectionPanel(String title) {
        this(title, false);
    }

    public SectionPanel(String title, boolean isDisclosure) {
        initWidget(main = new FlowPanel());

        if (title != null) {
            header = new HeaderContainer(isDisclosure);
            header.addStyleName(MobileResources.INSTANCE.appearanceCss().sectionBar());

            if (isDisclosure) {
                contents.setVisible(false);
                SectionPanelImages images = GWT.create(SectionPanelImages.class);
                header.setWidget(new DefaultHeader(images, title));
            } else {
                UnsunkLabel titleLabel = new UnsunkLabel("");
                titleLabel.setText(title);
                titleLabel.addStyleName(MobileResources.INSTANCE.appearanceCss().titleLabel());
                header.setWidget(titleLabel);
            }

            main.add(header);
        }

        // contents
        contents.addStyleName(MobileResources.INSTANCE.appearanceCss().sectionContents());
        main.add(contents);
    }

    public void setOpen(boolean isOpen) {
        if (this.isOpen != isOpen) {
            this.isOpen = isOpen;
            DefaultHeader df = (DefaultHeader) header.getWidget();
            df.setStyle();
            contents.setVisible(this.isOpen);
            fireEvent();
        }
    }

    public void setHtml(String html) {
        HTML h = new HTML(html);
        contents.clear();
        contents.add(h);

    }

    public void add(Widget w) {
        contents.add(w);
    }

    public void clear() {
        contents.clear();
    }

    public HandlerRegistration addOpenHandler(OpenHandler<SectionPanel> handler) {
        return addHandler(handler, OpenEvent.getType());
    }

    public HandlerRegistration addCloseHandler(CloseHandler<SectionPanel> handler) {
        return addHandler(handler, CloseEvent.getType());
    }

    private void fireEvent() {
        if (isOpen) {
            OpenEvent.fire(this, this);
        } else {
            CloseEvent.fire(this, this);
        }
    }

    private final class HeaderContainer extends SimplePanel {

        private HeaderContainer(boolean isDisclosure) {
            // Anchor is used to allow keyboard access.
            //super(DOM.createAnchor());
            Element elem = getElement();
            //DOM.setElementProperty(elem, "href", "javascript:void(0);");
            // Avoids layout problems from having blocks in inlines.
            DOM.setStyleAttribute(elem, "display", "block");
            if (isDisclosure) {
                sinkEvents(Event.ONCLICK);
            }    
            //setStyleName(STYLENAME_HEADER);
        }

        @Override
        public void onBrowserEvent(Event event) {
            // no need to call super.
            switch (DOM.eventGetType(event)) {
                case Event.ONCLICK:
                    // Prevent link default action.
                    DOM.eventPreventDefault(event);
                    setOpen(!isOpen);
            }
        }
    }

    /**
     * The default header widget used within a {@link DisclosurePanel}.
     */
    private class DefaultHeader extends Widget {

        /**
         * imageTD holds the image for the icon, not null. labelTD holds the text
         * for the label.
         */
        //private final Element labelTD;
        private final Image iconImage;
        private final SectionPanelImages images;

        private DefaultHeader(SectionPanelImages images, String text) {
            this.images = images;

            iconImage = isOpen ? new Image(images.disclosurePanelOpen())
                    : new Image(images.disclosurePanelClosed());

            Element root = DOM.createTable();
            Element tbody = DOM.createTBody();
            Element tr = DOM.createTR();
            final Element imageTD = DOM.createTD();
            Element labelTD = DOM.createTD();

            setElement(root);

            DOM.appendChild(root, tbody);
            DOM.appendChild(tbody, tr);
            DOM.appendChild(tr, imageTD);
            DOM.appendChild(tr, labelTD);

            // set image TD to be same width as image.
            DOM.setElementProperty(imageTD, "align", "center");
            DOM.setElementProperty(imageTD, "valign", "middle");
            DOM.setStyleAttribute(imageTD, "width", iconImage.getWidth() + "px");

            DOM.appendChild(imageTD, iconImage.getElement());

            Element textWrapper = DOM.createDiv();
            DOM.setElementProperty(textWrapper.<com.google.gwt.user.client.Element> cast(),
            "className", MobileResources.INSTANCE.appearanceCss().titleLabel());
            DOM.setInnerText(textWrapper, text);
            DOM.appendChild(labelTD, textWrapper);

            setStyle();
        }

        private void setStyle() {
            if (isOpen) {
                AbstractImagePrototype.create(images.disclosurePanelOpen()).applyTo(iconImage);
            } else {
                AbstractImagePrototype.create(images.disclosurePanelClosed()).applyTo(iconImage);
            }
        }
    }

}
