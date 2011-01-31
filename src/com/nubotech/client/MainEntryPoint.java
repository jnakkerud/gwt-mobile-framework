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
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.resources.client.ResourceCallback;
import com.google.gwt.resources.client.ResourceException;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.nubotech.client.geo.GeoLocation;
import com.nubotech.client.geo.GeoPoint;
import com.nubotech.client.geo.Position;
import com.nubotech.client.geo.PositionCallback;
import com.nubotech.client.geo.PositionError;
import com.nubotech.client.geo.PositionOptions;
import com.nubotech.client.resources.MobileResources;
import com.nubotech.client.ui.ApplicationContainer;
import com.nubotech.client.ui.ContentPanel;
import com.nubotech.client.ui.FeedQuery;
import com.nubotech.client.ui.FeedView;
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

        MobileResources.INSTANCE.appearanceCss().ensureInjected();

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
        bar.addStyleName(MobileResources.INSTANCE.appearanceCss().buttonBar());
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
        FeedQuery feedQuery = new FeedQuery("verbier", new String[] {"http://www.google.com/alerts/feeds/05173895962631213926/7161854547244649399"});
        final FeedView detailPanel = new FeedView("Feeds", parentView, feedQuery);
        final ExamplePanel examplePanel = new ExamplePanel("Grouped Example", parentView);
        final SettingsPanel settingsPanel = new SettingsPanel(parentView);

        final View defaultPanel = new View("Home", null) {
            public String getShortTitle() {
                return "Home";
            }


            @Override
            protected void onLoad() {
                addStyleName(MobileResources.INSTANCE.appearanceCss().SliderPanel());
                add(sectionExample);
                add(detailPanel);
                add(examplePanel);

                // add 10 more with just labels
                int cnt = 4;
                for (int i = 0; i < 35; i++) {
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
                h.addStyleName(MobileResources.INSTANCE.appearanceCss().padding5());
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
            grp.add("Label1", "Description1");
            grp.add("Label2", "Description2");
            grp.add("Label1", "Description1");
            grp.add("Label2", "Description2");
            grp.add("Label1", "Description1");
            grp.add("Label2", "Description2");
            grp.add("Label2", "Description2");
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

            final GroupListPanel forecast_grp = new GroupListPanel("Forecast");
            container.add(forecast_grp);

            GroupListPanel grp3 = new GroupListPanel("The Last Group");
            final DetailPanel detail_panel = new DetailPanel(new String[]{"This is a string"}, "Detail1", ExamplePanel.this);
            PanelLabel label_1 = new PanelLabel("Label 1", true, new Command() {
                public void execute() {
                    detail_panel.enter();
                }
            });
            //grp3.setLabel(label_1);
            grp3.add(label_1);

            final DetailPanel detail_panel_2 = new DetailPanel(new String[]{"This is a string"}, "Detail2", ExamplePanel.this);
            PanelLabel label_2 = new PanelLabel("Label 2", true, new Command() {
                public void execute() {
                    detail_panel_2.enter();
                }
            });
            //grp3.setLabel(label_2);
            grp3.add(label_2);
            container.add(grp3);

            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                public void execute() {
                    loadForecast(forecast_grp);
                }
            });

            add(container);
        }

        public String getShortTitle() {
            return "Grouped";
        }


        private void loadForecast(final GroupListPanel forecast_grp) {
            GeoPoint geo_p = new GeoPoint(46.1,7.216667);
            StringBuilder sb = new StringBuilder();
            sb.append("select * from xml where url='http://www.google.com/ig/api?weather=,,,");
            sb.append(geo_p).append("'");
            String qry = sb.toString();

            String url = "http://query.yahooapis.com/v1/public/yql?q=" + RestEncoder.encodeUrlString(qry) + "&format=json";

            JsonpRequestBuilder forecast_req = new JsonpRequestBuilder();
            forecast_req.requestObject(url,
                    new AsyncCallback<ThisWeatherResult>() {

                        public void onFailure(Throwable throwable) {
                            GWT.log("error", throwable);
                            //toogleBusy(); // TODO spelling
                        }

                        public void onSuccess(ThisWeatherResult result) {
                            JsArray<ThisForecastCondition> entries = result.getForecast();
                            for (int i = 0; i < entries.length(); i++) {
                                ThisForecastCondition forecast = entries.get(i);
                                forecast_grp.add(new PanelLabel(new ForecastWidget(forecast), null));
                            }

                            //toogleBusy(); // TODO spelling
                            content_panel.refresh();
                        }
                    });

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
                        MobileResources.INSTANCE.example1Text().getText(new ResourceCallback<TextResource>() {
                            public void onError(ResourceException e) {
                                //throw new UnsupportedOperationException("Not supported yet.");
                            }

                            public void onSuccess(TextResource resource) {
                                section1.add(new HTML(resource.getText()));
                                content_panel.refresh();
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
                        MobileResources.INSTANCE.example2Text().getText(new ResourceCallback<TextResource>() {

                            public void onError(ResourceException e) {
                                //throw new UnsupportedOperationException("Not supported yet.");
                            }

                            public void onSuccess(TextResource resource) {
                                section2.add(new HTML(resource.getText()));
                                content_panel.refresh();
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

        /*@Override
        protected ContentPanel createContentPanel() {
            ContentPanel cp = new ContentPanel();
            cp.setContainer(new SimplePanel());
            return cp;
        }*/

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

                    content_panel.refresh();
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
            feedsContainer.addStyleName(MobileResources.INSTANCE.appearanceCss().sectionContents());
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
            filtersContainer.addStyleName(MobileResources.INSTANCE.appearanceCss().sectionContents());
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
            fieldsContainer.addStyleName(MobileResources.INSTANCE.appearanceCss().sectionContents());
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
        protected String backButtonLabel() {
            return "Cancel";
        }

    }

    class ForecastWidget extends Composite {
        ThisForecastCondition forecast_condition;
        FlowPanel main;
        public ForecastWidget(ThisForecastCondition condition) {
            this.forecast_condition = condition;
            initWidget(main = new FlowPanel());
            //addStyleName("forecastPanel");
            initGui();
        }

        private void initGui() {
            Image img = new Image("http://www.google.ru"+forecast_condition.getIcon());
            img.getElement().getStyle().setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
            main.add(img);
            String high_low = "H" + Utils.createSpan(forecast_condition.getHigh()+"F", "font-weight: normal;") + "L" + Utils.createSpan(forecast_condition.getLow()+"F", "font-weight: normal;");
            HTML content = new HTML(forecast_condition.getDayOfWeek() + "<br/>" + high_low + " " + forecast_condition.getCondition());
            DOM.setStyleAttribute(content.getElement(), "float", "left");
            DOM.setStyleAttribute(content.getElement(), "paddingLeft", "10px");
            main.add(content);
        }
    }


    public static class ThisWeatherResult extends JavaScriptObject {
        protected ThisWeatherResult() {}

        public final native JsArray<ThisForecastCondition> getForecast() /*-{
            return this.query.results.xml_api_reply.weather.forecast_conditions;
        }-*/;

    }


    public static class ThisForecastCondition extends JsArray {
        protected ThisForecastCondition() {}

        public final native String getDayOfWeek() /*-{
            return this.day_of_week.data;
        }-*/;

        public final native String getLow() /*-{
            return this.low.data;
        }-*/;

        public final native String getHigh() /*-{
            return this.high.data;
        }-*/;

        public final native String getIcon() /*-{
            return this.icon.data;
        }-*/;

        public final native String getCondition() /*-{
            return this.condition.data;
        }-*/;

    }

}
