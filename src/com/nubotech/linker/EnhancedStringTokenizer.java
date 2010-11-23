/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nubotech.linker;

import java.util.StringTokenizer;

public class EnhancedStringTokenizer {
    private StringTokenizer cst = null;
    String cdelim;
    boolean creturnDelims;

    public EnhancedStringTokenizer(String str) {
        this(str, " \t\n\r\f", false);
    }

    public EnhancedStringTokenizer(String str, String delim) {
        this(str, delim, false);
    }

    public EnhancedStringTokenizer(String str,
                                   String delim, boolean returnDelims) {
        cst = new StringTokenizer(str, delim, true);
        cdelim = delim;
        creturnDelims = returnDelims;
    }

    public boolean hasMoreTokens() {
        return cst.hasMoreTokens();
    }

    String lastToken = null;
    boolean delimLast = true;
    private String internalNextToken() {
        if (lastToken != null) {
            String last = lastToken;
            lastToken = null;
            return last;
        }

        String token = cst.nextToken();
        if (isDelim(token)) {
            if (delimLast) {
                lastToken = token;
                return "";
            } else {
                delimLast = true;
                return token;
            }
        } else {
            delimLast = false;
            return token;
        }
    }

    public String nextToken() {
        String token = internalNextToken();
        if (creturnDelims) return token;
        if (isDelim(token))
            return hasMoreTokens() ? internalNextToken() : "";
        else
            return token;
    }


    private boolean isDelim(String str) {
        if (str.length() == 1) {
            if (cdelim.indexOf(str) >= 0) {
                return true;
            }
        }
        return false;
    }

}
