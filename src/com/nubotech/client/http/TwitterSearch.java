/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nubotech.client.http;

/**
 *
 * @author jonnakkerud
 */
public class TwitterSearch extends JsonpService {
    public static final String TWITTER_PUBLIC_URL = "http://search.twitter.com/search.json?";

    String queryStr;

    public static void execute(String queryStr, JsonCallback cb) {
        TwitterSearch qry = new TwitterSearch();
        qry.queryStr = queryStr;
        qry.executeQuery(cb);
    }

    @Override
    public String generateRequestUrl(String callback) {
        String query = RestEncoder.encodeUrlString(queryStr);
        return TWITTER_PUBLIC_URL+ "&callback=" + callback + "&q=" + query;
    }
}
