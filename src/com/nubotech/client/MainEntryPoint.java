/*
 * MainEntryPoint.java
 *
 * Created on February 14, 2009, 8:58 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.nubotech.client;

import com.google.code.gwt.storage.client.Storage;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.resources.client.ResourceCallback;
import com.google.gwt.resources.client.ResourceException;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.nubotech.client.geo.GeoLocation;
import com.nubotech.client.geo.Position;
import com.nubotech.client.geo.PositionCallback;
import com.nubotech.client.geo.PositionError;
import com.nubotech.client.geo.PositionOptions;
import com.nubotech.client.resources.Resources;
import com.nubotech.client.ui.ApplicationContainer;
import com.nubotech.client.ui.GButton;
import com.nubotech.client.ui.GroupListPanel;
import com.nubotech.client.ui.PanelLabel;
import com.nubotech.client.ui.SectionPanel;
import com.nubotech.client.ui.SelectGroup;
import com.nubotech.client.ui.SelectLabel;
import com.nubotech.client.ui.SelectListener;
import com.nubotech.client.ui.SingleField;
import com.nubotech.client.ui.Toggle;
import com.nubotech.client.ui.Transition;
import com.nubotech.client.ui.UnsunkLabel;
import com.nubotech.client.ui.ViewHandler;
import com.nubotech.client.ui.View;


// for non-iphone browsers :
/*div#page-wrap {
margin:0 auto;
padding:0 5px;
width:310px;
}*/


/**
 *
 * @author jonnakkerud
 */
public class MainEntryPoint implements EntryPoint {

    /**
     * The entry point method, called automatically by loading a module
     * that declares an implementing class as an entry-point
     */
    public void onModuleLoad() {
        // ApplicationContainer with header, footer, content, etc

        ApplicationContainer app_container = ApplicationContainer.get();

        registerHandlers(app_container);

        RootPanel.get().add(app_container);

        // Add a HistoryListener to control the application.
        History.addValueChangeHandler(new ValueChangeHandler<String>() {
            // Prevent repeated loads of the same token.
            String lastToken = "";

            public void onValueChange(ValueChangeEvent<String> event) {
                String historyToken = event.getValue();
                if (lastToken.equals(historyToken)) {
                    return;
                }

                if (historyToken != null) {
                    lastToken = historyToken;
                    processHistoryToken(historyToken);
                }
            }
        });

        Resources.INSTANCE.appearanceCss().ensureInjected();

        processHistoryToken(History.getToken());

    }


    /**
     * Change the application's state based on a new history token.
     */
    private void processHistoryToken(String token) {
        if (token != null) {
            token = URL.decodeComponent(token);
        }

        ApplicationContainer.get().handle(token);
    }

    private void registerHandlers(ApplicationContainer app_container) {

        // add the footer
        final FlowPanel bar = new FlowPanel();
        bar.addStyleName(Resources.INSTANCE.appearanceCss().buttonBar());
        GButton settings_btn = new GButton("Settings");
        settings_btn.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                //History.newItem("Settings");
                LogUtil.log("Settings button selected");
            }
        });
        bar.add(settings_btn);
        bar.add(new GButton("Test"));

        View parentView = new View.Parent("Home");
        final SectionPanelExample sectionExample = new SectionPanelExample("Section Example", parentView);
        final DetailPanel detailPanel = new DetailPanel(new String[]{"example details 1", "example details 2"}, "Label 2", parentView);
        final ExamplePanel examplePanel = new ExamplePanel("Grouped Example", parentView);
        final SettingsPanel settingsPanel = new SettingsPanel(parentView);

        final View defaultPanel = new View("Home", null) {
            public String getShortTitle() {
                return "Home";
            }


            @Override
            protected void onLoad() {
                addStyleName(Resources.INSTANCE.appearanceCss().SliderPanel());
                add(sectionExample);
                add(detailPanel);
                add(examplePanel);

                // add 10 more with just labels
                int cnt = 4;
                for (int i = 0; i < 30; i++) {
                    add(new PanelLabel("Label " + cnt++));
                }
            }
        };

        defaultPanel.setFooter(bar);
        defaultPanel.setEditCommand("Edit", "Edit", new Command() {
            public void execute() {
                History.newItem("Settings");
            }
        });


        app_container.registerHandler(new ViewHandler(defaultPanel.getShortTitle(), true) {
            public void execute() {
                defaultPanel.enter();
            }
        });

        app_container.registerHandler(new ViewHandler(sectionExample.getShortTitle(), false) {
            public void execute() {
                sectionExample.enter();
            }
        });

        app_container.registerHandler(new ViewHandler(settingsPanel.getShortTitle(), false) {
            public void execute() {
                settingsPanel.enter();
            }
        });
    }

    class DetailPanel extends View {

        String[] details;

        public DetailPanel(String[] details, String title, View parent) {
            super(title, parent);
            this.details = details;
        }

        @Override
        protected void onLoad() {
            for (String s : details) {
                HTML h = new HTML(s);
                h.addStyleName(Resources.INSTANCE.appearanceCss().padding5());
                DOM.setStyleAttribute(h.getElement(), "margin", "5px");
                add(h);
            }
        }

        public String getShortTitle() {
            return getLabel().getText();
        }

    }

    class ExamplePanel extends View {
        public ExamplePanel(String title, View parent) {
            super(title, parent);
        }

        @Override
        protected void onLoad() {
            // create example
            SectionPanel container = new SectionPanel();

            GroupListPanel grp = new GroupListPanel();
            grp.add("Label1", "Description1");
            grp.add("Label2", "Description2");

            final DetailPanel wp = new DetailPanel(new String[]{"This is a string"}, "Forecast", ExamplePanel.this);
            PanelLabel foreCast = new PanelLabel("Forecast", true, new Command() {
                public void execute() {
                    wp.enter();
                }
            });
            wp.setLabel(foreCast);
            grp.add(foreCast);

            container.add(grp);

            // add a second longer group
            GroupListPanel grp2 = new GroupListPanel("Another Group");
            final View weather_p = new WeatherPanel(ExamplePanel.this);
            PanelLabel weather = new PanelLabel("Current Weather", true, new Command() {
                public void execute() {
                    weather_p.enter();
                }
            });
            weather_p.setLabel(weather);
            grp2.add(weather);

            grp2.add("Label11", "Description11");
            grp2.add("Label22", "Description22");
            grp2.add("Label33", "Description33");
            grp2.add("Label44", "Description44");
            grp2.add("Label55", "Description55");
            grp2.add("Label66", "Description66");
            container.add(grp2);

            add(container);
        }

        public String getShortTitle() {
            return "Grouped";
        }
    }

    class SectionPanelExample extends View {

        public SectionPanelExample(String title, View parent) {
            super(title, parent);
        }

        @Override
        protected void onLoad() {
            final SectionPanel section1 = new SectionPanel("la marseillaise - french", true);
            section1.addOpenHandler(new OpenHandler<SectionPanel>() {
                public void onOpen(OpenEvent<SectionPanel> event) {
                    section1.clear();
                    try {
                        Resources.INSTANCE.example1Text().getText(new ResourceCallback<TextResource>() {
                            public void onError(ResourceException e) {
                                //throw new UnsupportedOperationException("Not supported yet.");
                            }

                            public void onSuccess(TextResource resource) {
                                section1.add(new HTML(resource.getText()));
                            }
                        });
                        //section1.add(new HTML(Resources.INSTANCE.example1Text()));
                    } catch (ResourceException ex) {
                    }
                }
            });
            add(section1);

            final SectionPanel section2 = new SectionPanel("la marseillaise - english", true);
            section2.addOpenHandler(new OpenHandler<SectionPanel>() {
                public void onOpen(OpenEvent<SectionPanel> event) {
                    section2.clear();
                    try {
                        Resources.INSTANCE.example2Text().getText(new ResourceCallback<TextResource>() {

                            public void onError(ResourceException e) {
                                //throw new UnsupportedOperationException("Not supported yet.");
                            }

                            public void onSuccess(TextResource resource) {
                                section2.add(new HTML(resource.getText()));
                            }
                        });
                        //section1.add(new HTML(Resources.INSTANCE.example1Text()));
                    } catch (ResourceException ex) {
                    }
                }
            });
            add(section2);
        }

        public String getShortTitle() {
            return "Section";
        }
    }

    class WeatherPanel extends View {

        public WeatherPanel(View parent) {
            super("Current Weather", parent);
        }

        @Override
        protected void onLoad() {
            GeoLocation geo = GeoLocation.getGeolocation();
            geo.getCurrentPosition(PositionOptions.getPositionOptions(true, 5000, 5000),  new PositionCallback() {
                public void onSuccess(Position position) {

                    LogUtil.log("position " + position);

                    double lat = position.getCoords().getLatitude();
                    double lng = position.getCoords().getLongitude();

                    //throw new UnsupportedOperationException("Not supported yet.");
                    String link = "http://i.wund.com/cgi-bin/findweather/getForecast?brand=iphone&query=" + lat + "," + lng;
                    LogUtil.log(link);

                    Frame f = new Frame(link);
                    f.setWidth("100%");
                    f.setHeight("100%");

                    add(f);

                }

                public void onFailure(PositionError error) {
                    LogUtil.log(error.getMessage());
                    add(new HTML("Failed to load " + error.getMessage()));
                }
            });

        }

        public String getShortTitle() {
            return "Weather";
        }
    }


    class SettingsPanel extends View {
        SelectGroup selectGrp;
        Toggle toggle1;
        Toggle toggle2;
        TextBox name_tb;
        PasswordTextBox pass_tb;
        ListBox list_box;

        public SettingsPanel(View parent) {
            super("Settings", parent, Transition.TransitionType.PAGE_SLIDE);
            setEditCommand("Done", "Done", new Command() {
                public void execute() {
                    // Save settings
                    saveSettings();
                    exit();
                }
            });
        }

        @Override
        protected void onLoad() {
            initGui();
        }

        @Override
        public void onSetCurrent(boolean is_current) {
            super.onSetCurrent(is_current);
            if (is_current) {
                loadSettings();
            }
        }

        // always show when view is set_current
        void loadSettings() {
            final Storage localStorage = Storage.getLocalStorage();
            selectGrp.setSelectedValue(localStorage.getItem("FEED") != null ? localStorage.getItem("FEED") : "ALL");

            toggle1.setSelected(localStorage.getItem("TOOGLE1") != null);
            toggle2.setSelected(localStorage.getItem("TOOGLE2") != null);

            String name = localStorage.getItem("NAME");
            if (name != null) {
                name_tb.setText(name);
            }

            String pass = localStorage.getItem("PASS");
            if (pass != null) {
                pass_tb.setText(pass);
            }

            String index = localStorage.getItem("ITEMS");
            if (index != null) {
                list_box.setSelectedIndex(Integer.parseInt(index));
            }

        }

        void saveSettings() {
            final Storage localStorage = Storage.getLocalStorage();
            localStorage.setItem("FEED", selectGrp.getSelectedValue());

            if (toggle1.isSelected()) {
                localStorage.setItem("TOOGLE1", toggle1.getValue());
            }
            else {
                localStorage.removeItem("TOOGLE1");
            }

            if (toggle2.isSelected()) {
                localStorage.setItem("TOOGLE2", toggle2.getValue());
            }
            else {
                localStorage.removeItem("TOOGLE2");
            }


            localStorage.setItem("NAME", name_tb.getText());
            localStorage.setItem("PASS", pass_tb.getText());
            localStorage.setItem("ITEMS", ""+list_box.getSelectedIndex());
        }


        void initGui() {

            // show primary resort feeds
            SimplePanel feedsContainer = new SimplePanel();
            feedsContainer.addStyleName(Resources.INSTANCE.appearanceCss().sectionContents());
            add(feedsContainer);

            GroupListPanel feeds = new GroupListPanel("Feeds");
            feedsContainer.setWidget(feeds);

            selectGrp = new SelectGroup();
            selectGrp.addSelectListener(new SelectListener() {
                public void onSelect(SelectLabel label) {
                    LogUtil.log("Selected:"+label.getValue());
                }
            });
            SelectLabel l = new SelectLabel("Tahoe", selectGrp);
            l.setValue("TAHOE");
            feeds.add(l);

            // add all
            l = new SelectLabel("All", selectGrp);
            l.setValue("ALL");
            feeds.add(l);

            // show filters
            SimplePanel filtersContainer = new SimplePanel();
            filtersContainer.addStyleName(Resources.INSTANCE.appearanceCss().sectionContents());
            add(filtersContainer);
            GroupListPanel filterGrp = new GroupListPanel("Filters");
            filtersContainer.setWidget(filterGrp);

            toggle1 = new Toggle("Toogle1");
            toggle1.setValue("toogle1");
            filterGrp.add(toggle1);

            toggle2 = new Toggle("Toogle2");
            toggle2.setValue("toogle2");
            filterGrp.add(toggle2);

            SimplePanel fieldsContainer = new SimplePanel();
            fieldsContainer.addStyleName(Resources.INSTANCE.appearanceCss().sectionContents());
            add(fieldsContainer);
            GroupListPanel fieldsGrp = new GroupListPanel("Other");
            fieldsContainer.setWidget(fieldsGrp);

            name_tb = new TextBox();
            fieldsGrp.add(new SingleField("name", "Name", name_tb));

            pass_tb = new PasswordTextBox();
            fieldsGrp.add(new SingleField("pass", "Password", pass_tb));

            list_box = new ListBox(false);
            list_box.addItem("One");
            list_box.addItem("Two");
            list_box.addItem("Three");
            fieldsGrp.add(new SingleField("combo", "Combo", list_box));
        }

        @Override
        protected Label createBackButton() {
            Label l = new Label("Cancel");
            l.addClickHandler(parentClickListener);
            l.addStyleName(Resources.INSTANCE.appearanceCss().button());
            l.addStyleName(Resources.INSTANCE.appearanceCss().goButton());
            return l;
        }


    }

}
