/*
 * Copyright 2007 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.nubotech.client.ui;

import com.nubotech.client.ui.mobile.TouchableFlowPanel;
import com.nubotech.client.ui.mobile.TouchableComposite;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.nubotech.client.resources.Resources;

/**
 * This is the base class for all of the major UI panels. It is designed to fill
 * the full width of the client area and to have at most a single instance
 * attached to the DOM. The panel has a concept of having a parent or previous
 * panel that can be accessed by a "back" button, as well as a distinct "edit"
 * or alternate command that can be accessed by a second button in the title
 * bar. The contents of the panel should consist only of PanelLabel widgets.
 */
public abstract class View extends Composite implements HasHTML {

    //private static View activeView;
    protected final ClickHandler parentClickListener = new ClickHandler() {
        public void onClick(ClickEvent event) {
            exit();
        }
    };
    protected View parent;
    private final TouchableFlowPanel contents = new TouchableFlowPanel();
    private final HorizontalPanel header = new HorizontalPanel(); // TODO replace with header tag http://vxjs.org/CSS3/PageFlip/index.html
    private PanelLabel panelLabel;
    private final UnsunkLabel titleLabel = new UnsunkLabel("");
    private Command editCommand;
    private HasText hasText;
    private HasHTML hasHtml;

    protected ContentPanel header_panel;

    Transition.TransitionType transition_type;

    protected View() {
        this.parent = null;
        initWidget(new SimplePanel());
    }

    public View(String title, View parent) {
        this(title, parent, Transition.TransitionType.PAGE_SLIDE);
    }

    public View(String title, View parent, Transition.TransitionType transition_type) {
        this.parent = parent;
        this.transition_type = transition_type;

        panelLabel = new PanelLabel("", new Command() {
            public void execute() {
                if (ApplicationContainer.get().getHandler(getShortTitle()) != null) {
                    History.newItem(getShortTitle());
                }
                else {
                    enter();
                }
            }
        }) {
            public void setText(String title) {
                titleLabel.setText(title);
                super.setText(title);
            }
        };
        panelLabel.setText(title);

        header.setVerticalAlignment(HorizontalPanel.ALIGN_TOP);

        if (parent != null) {
            // add the back button
            header.add(createBackButton());
        }

        titleLabel.addStyleName(Resources.INSTANCE.appearanceCss().titleLabel());
        header.add(titleLabel);
        header.setCellWidth(titleLabel, "100%");
        header.setCellHorizontalAlignment(titleLabel, HorizontalPanel.ALIGN_CENTER);
        header.addStyleName(Resources.INSTANCE.appearanceCss().header());
        header.setHeight("44px");

        header_panel = new ContentPanel();
        header_panel.setHeader(header);

        SimplePanel sp = new SimplePanel();
        sp.setWidget(new TouchableComposite(contents));
        header_panel.setContainer(sp);

        initWidget(header_panel);
        getElement().setId(getShortTitle());
        addStyleName(Resources.INSTANCE.appearanceCss().view());
    }

    protected Label createBackButton() {
        Label l = new Label(parent.getShortTitle());
        l.addClickHandler(parentClickListener);
        l.addStyleName(Resources.INSTANCE.appearanceCss().button());
        l.addStyleName(Resources.INSTANCE.appearanceCss().backButton());
        return l;
    }

    public void setFooter(Widget w) {
        header_panel.setFooter(w);
    }

    public void add(Widget w) {
        if ((hasText != null) || (hasHtml != null)) {
            hasText = hasHtml = null;
            contents.clear();
        }

        contents.add(w);
    }

    public void add(View view) {
        add(view.getLabel());
        view.parent = this;
    }

    public void clear() {
        contents.clear();
    }

    public String getHTML() {
        return hasHtml == null ? null : hasHtml.getHTML();
    }

    public PanelLabel getLabel() {
        return panelLabel;
    }

    public void setLabel(PanelLabel panelLabel) {
        this.panelLabel = panelLabel;
    }

    public String getText() {
        return hasText == null ? null : hasText.getText();
    }

    public void remove(Widget w) {
        contents.remove(w);
    }

    /**
     * Set the command to be executed by a right-justified button in the title
     * bar.
     *
     * @param label the label for the button
     * @param title the title or alt-text for the button
     * @param command the Command to execute when the button is pressed.
     */
    public void setEditCommand(String label, String title, Command command) {
        editCommand = command;
        Label l = new Label(label);
        // add the edit button
        l.addStyleName(Resources.INSTANCE.appearanceCss().button());
        l.addStyleName(Resources.INSTANCE.appearanceCss().goButton());
        l.setTitle(title);
        l.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                editCommand.execute();
            }
        });
        header.add(l);
        header.setCellHorizontalAlignment(l, HorizontalPanel.ALIGN_RIGHT);
    }

    public void setHTML(String html) {
        HTML h;
        hasText = hasHtml = h = new HTML(html);

        contents.clear();
        contents.add(h);
    }

    public void setText(String text) {
        UnsunkLabel l;
        hasText = l = new UnsunkLabel(text);
        hasHtml = null;

        contents.clear();
        contents.add(l);
    }


    com.google.gwt.user.client.Element loading_el = null;
    public void toogleBusy() {
        if (loading_el == null) {
            loading_el = DOM.createDiv();
            loading_el.setInnerText("Loading...");
            loading_el.addClassName(Resources.INSTANCE.appearanceCss().loading());
            com.google.gwt.user.client.Element e = RootPanel.getBodyElement();
            e.appendChild(loading_el);

            // TODO center ... see PopupPanel.center
        } else {
            RootPanel.getBodyElement().removeChild(loading_el);
            loading_el = null;
        }
    }

    public void onSetCurrent(boolean is_current) {
        if (is_current == false) {
            panelLabel.removeStyleName(Resources.INSTANCE.appearanceCss().clicked());
        }
    }

    public void enter() {
        ApplicationContainer.get().setCurrent(this);
    }

    public void exit() {
        // back out to the parent
        if (parent == null) {
            throw new RuntimeException("SliderPanel has no parent");
        }

 
        if (ApplicationContainer.get().getHandler(parent.getShortTitle()) != null) {
            History.newItem(parent.getShortTitle());
        }
        parent.enter();
    }

    /**
     * A short title to be used as the label of the back button.
     */
    public String getShortTitle() {
        return panelLabel.getText();
    }


    public View getParentView() {
        return this.parent;
    }

    public static class Parent extends View {
        String token;
        public Parent(String token) {
            this.token = token;
        }

        @Override
        public void enter() {
            ApplicationContainer.get().handle(token);
        }

        public String getShortTitle() {
            return token;
        }
    }

}
