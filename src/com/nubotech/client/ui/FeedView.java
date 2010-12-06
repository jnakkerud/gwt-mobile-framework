/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nubotech.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.nubotech.client.LogUtil;
import com.nubotech.client.Utils;
import com.nubotech.client.http.JsonCallback;
import com.nubotech.client.http.TwitterSearch;
import com.nubotech.client.http.YahooQuery;
import java.util.Date;

/**
 *
 * @author jonnakkerud
 */
public class FeedView extends View {

    String twitterQuery = "verbier";
    String[] feedUrls = new String[] {"http://www.google.com/alerts/feeds/05173895962631213926/7161854547244649399"};
    long now;

    public FeedView(String title, View parent) {
        super(title, parent);

        Date d = new Date();
        now = Date.UTC(d.getYear(), d.getMonth(), d.getDate(), d.getHours(), d.getMinutes(), d.getSeconds());
    }

    @Override
    public void onSetCurrent(boolean is_current) {
        super.onSetCurrent(is_current);
        if (is_current) {
            clear();
            loadFeeds();
        }
    }

    private String generateTwitterQuery() {
        StringBuilder sb = new StringBuilder("\""+twitterQuery+"\"");
        return sb.toString();
    }

    private void loadTweets(JSONArray ary) {
        for (int i = 0; i < ary.size(); ++i) {
            JSONObject tweet = ary.get(i).isObject();
            // from_user anchor
            String txt = tweet.get("text").isString().stringValue();
            String user = tweet.get("from_user").isString().stringValue();
            // calc time elapsed
            String elapsed = toElapsed(tweet.get("created_at").isString().stringValue());
            TweetPanel tp = new TweetPanel(txt, user, elapsed);
            // margin-bottom: 5px;
            //DOM.setStyleAttribute(tp.getElement(), "marginBottom", "5px");
            add(tp);
        }
    }

    private String toElapsed(String createdAt) {
        // about X hours ago
        Date createdAtDate = Utils.parseHTTPDate(createdAt);
        long elapsed = now - createdAtDate.getTime();
        long hours = elapsed/(Utils.HOURS);
        StringBuilder sb = new StringBuilder();
        if (hours <= 0) {
            // TODO DEFER formatting bug
            //sb.append("about ").append(elapsed/(Utils.MINUTES)).append(" minutes ago");
            sb.append(createdAt);
        }
        else if (hours > 0 && hours <= 24) {
            sb.append("about ").append(hours).append(" hours ago");
        }
        else {
            sb.append(createdAt);
        }

        return sb.toString();
    }

    private void loadFeeds() {
        /*TwitterSearch.execute(generateTwitterQuery(), new JsonCallback() {
            public void onSuccess(JavaScriptObject jso) {
                if (jso != null) {
                    JSONObject json = new JSONObject(jso);
                    JSONArray ary = json.get("results").isArray();
                    loadTweets(ary);
                }
                else {
                    LogUtil.log("load failed");
                }
            }
        });*/

        //http://developer.yahoo.com/yql/console/?q=select%20title.content%2C%20link.href%2C%20published%2C%20content.content%20from%20atom%20where%20url%3D%27http%3A%2F%2Fwww.google.com%2Falerts%2Ffeeds%2F05173895962631213926%2F7161854547244649399%27%20limit%2010%20%7C%20sort(field%3D%22published%22)%20%7C%20reverse()
        StringBuilder sb = new StringBuilder();
        sb.append("select title.content, link.href, published, content.content from atom where url in (");
        String[] arS = feedUrls;
        for (int i = 0; i < arS.length; i++) {
            sb.append("'").append(arS[i]).append("'");
            if (i+1 < arS.length) {
                sb.append(",");
            }
        }
        sb.append(") limit 10 | sort(field=\"published\") | reverse()");

        String url = YahooQuery.YQL_URL_PUBLIC+ "q=" + sb.toString() + "&format=json";

        JsonpRequestBuilder jsonp = new JsonpRequestBuilder();
        jsonp.requestObject(url,
                new AsyncCallback<Feed>() {

                    public void onFailure(Throwable throwable) {
                        //Log.severe("Error: " + throwable);
                        GWT.log("error", throwable);
                    }

                    public void onSuccess(Feed feed) {
                        // TODO see http://hootsuite.com/dashboard#/tabs?id=3683313 for idea on feed panel

                        JsArray<Entry> entries = feed.getEntries();
                        for (int i = 0; i < entries.length(); i++) {
                            Entry entry = entries.get(i);
                            LogUtil.log(entry.getTitle());
                        }

                    }
                });


    }


    class TweetPanel extends FeedPanel {
        public TweetPanel(String tweet_text, String user, String elapsed) {
        }
    }

    public static class Feed extends JavaScriptObject {

        protected Feed() {
        }

        public final native JsArray<Entry> getEntries() /*-{
            return this.query.results.entry;
        }-*/;
    }

    public static class Entry extends JavaScriptObject {

        protected Entry() {
        }

        public final native String getTitle() /*-{
        return this.title;
        }-*/;

        public final native String getPublished() /*-{
        return this.published;
        }-*/;

        public final native String getContent() /*-{
        return this.content;
        }-*/;

        public final native String getLink() /*-{
        return this.link;
        }-*/;
    }


}
