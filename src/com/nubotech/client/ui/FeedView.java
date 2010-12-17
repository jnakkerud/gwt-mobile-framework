/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nubotech.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.nubotech.client.RestEncoder;
import com.nubotech.client.Utils;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author jonnakkerud
 */
public class FeedView extends View {

    String twitterQuery = "verbier";
    String[] feedUrls = new String[] {"http://www.google.com/alerts/feeds/05173895962631213926/7161854547244649399"};

    Set<FeedEntry> feeds = new TreeSet<FeedEntry>();

    public FeedView(String title, View parent) {
        super(title, parent);

        Date d = new Date();
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

    private void loadFeeds() {
        toogleBusy();
        String twitter_url = "http://search.twitter.com/search.json?q=" + generateTwitterQuery();

        JsonpRequestBuilder twitter_req = new JsonpRequestBuilder();
        twitter_req.requestObject(twitter_url,
                new AsyncCallback<TwitterFeed>() {
                    public void onFailure(Throwable throwable) {
                        GWT.log("error", throwable);
                        loadAtomFeeds();
                    }
                    public void onSuccess(TwitterFeed feed) {
                        // TODO see http://hootsuite.com/dashboard#/tabs?id=3683313 for idea on feed panel

                        JsArray<Tweet> entries = feed.getEntries();
                        for (int i = 0; i < entries.length(); i++) {
                            Tweet tweet = entries.get(i);
                            feeds.add(new TweetEntry(tweet));
                        }

                        loadAtomFeeds();
                    }
                });
    }

    private void loadAtomFeeds() {
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

        String url = "http://query.yahooapis.com/v1/public/yql?q=" + RestEncoder.encodeUrlString(sb.toString()) + "&format=json";

        JsonpRequestBuilder feed_req = new JsonpRequestBuilder();
        feed_req.requestObject(url,
                new AsyncCallback<AtomFeed>() {

                    public void onFailure(Throwable throwable) {
                        //Log.severe("Error: " + throwable);
                        GWT.log("error", throwable);
                        processFeeds();
                    }

                    public void onSuccess(AtomFeed feed) {
                        // TODO see http://hootsuite.com/dashboard#/tabs?id=3683313 for idea on feed panel

                        JsArray<AtomEntry> entries = feed.getEntries();
                        for (int i = 0; i < entries.length(); i++) {
                            AtomEntry entry = entries.get(i);
                            feeds.add(new AtomFeedEntry(entry));
                        }

                        processFeeds();
                    }
                });
    }

    private void processFeeds() {
        if (feeds.size() > 0) {
            for (FeedEntry entry : feeds) {
                add(new FeedPanel(entry.getDate(), entry.getTitle(), entry.getIcon(), entry.getContent(), entry.getLink()));
            }
        }
        toogleBusy();
    }

    public interface FeedEntry {
        Date getDate();
        String getTitle();
        String getIcon();
        String getContent();
        String getLink();
    }

    public static class AtomFeedEntry implements FeedEntry, Comparable<FeedEntry> {
        AtomEntry entry;
        Date d;

        public AtomFeedEntry(AtomEntry entry) {
            this.entry = entry;
        }

        // "2010-12-13T04:01:43Z"
        // yyyy-MM-ddTHHmmss
        public Date getDate() {
            if (d == null) {
                d = Utils.parseFeedDate(entry.getPublished());
            }
            return d;
        }

        public String getTitle() {
            return entry.getTitle();
        }

        public String getIcon() {
            return FeedPanel.RSS_IMAGE;
        }

        // TODO fixup content font
        public String getContent() {
            return entry.getContent();
        }

        public String getLink() {
            return entry.getLink();
        }

        public int compareTo(FeedEntry e) {
            int results =  getDate().compareTo(e.getDate());
            if(results > 0) {
                return -1;
            }
            else if (results < 0) {
                return 1;
            }
            else {
                return 0;
            }
        }

    }

    public static class TweetEntry implements FeedEntry, Comparable<FeedEntry> {
        Tweet entry;
        Date d;

        public TweetEntry(Tweet entry) {
            this.entry = entry;
        }

        public Date getDate() {
            if (d == null) {
                d = Utils.parseHTTPDate(entry.getCreatedAt());
            }
            return d;
        }

        public String getTitle() {
            return entry.getTitle();
        }

        public String getIcon() {
            return entry.getIcon();
        }

        public final String getContent() {
            return "<p>" + entry.getText() + "</p>";
        }

        public String getLink() {
            return null;
        }

        public int compareTo(FeedEntry e) {
            int results = getDate().compareTo(e.getDate());
            if(results > 0) {
                return -1;
            }
            else if (results < 0) {
                return 1;
            }
            else {
                return 0;
            }
        }
    }

    public static class AtomFeed extends JavaScriptObject {
        protected AtomFeed() {}

        public final native JsArray<AtomEntry> getEntries() /*-{
            return this.query.results.entry;
        }-*/;
    }

    public static class AtomEntry extends JavaScriptObject {
        protected AtomEntry() {}

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
            return this.link.href;
        }-*/;
    }

    public static class TwitterFeed extends JavaScriptObject {

        protected TwitterFeed() {}

        public final native JsArray<Tweet> getEntries() /*-{
            return this.results;
        }-*/;
    }

    public static class Tweet extends JavaScriptObject  {
        protected Tweet() {}

        public final native String getIcon() /*-{
            return this.profile_image_url;
        }-*/;

        public final native String getCreatedAt() /*-{
            return this.created_at;
        }-*/;

        public final native String getTitle() /*-{
            return this.from_user;
        }-*/;

        public final native String getText() /*-{
            return this.text;
        }-*/;

        public final native String getGeo() /*-{
            return this.geo;
        }-*/;
    }

}
