/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nubotech.client.http;

import com.google.gwt.core.client.GWT;

/**
 *
 * @author jonnakkerud
 */
public class YahooQuery extends JsonpService {
    public static final String YQL_URL = "http://query.yahooapis.com/v1/yql?";
    public static final String YQL_URL_PUBLIC = "http://query.yahooapis.com/v1/public/yql?";

    String queryStr;

    public static void execute(String queryStr, JsonCallback callback) {
        YahooQuery qry = new YahooQuery();
        qry.queryStr = queryStr;
        qry.executeQuery(callback);
    }

    @Override
    public String generateRequestUrl(String callback) {
        String query = RestEncoder.encodeUrlString(queryStr);
        return YQL_URL_PUBLIC+ "q=" + query + "&format=json" + "&callback=" + callback;
    }

}
