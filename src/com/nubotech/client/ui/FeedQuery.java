/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nubotech.client.ui;

/**
 *
 * @author jonnakkerud
 */
public class FeedQuery {
    String twitterQuery;
    String[] feedUrls;

    public FeedQuery() {}

    public FeedQuery(String twitterQuery, String[] feedUrls) {
        this.twitterQuery = twitterQuery;
        this.feedUrls = feedUrls;
    }
}
