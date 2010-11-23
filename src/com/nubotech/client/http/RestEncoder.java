/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nubotech.client.http;

/**
 *
 * @author jonnakkerud
 */
public class RestEncoder {
    
    private static String unreserved = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-_.~";
    //private static String reserved = "!*'();:@&=+$,/?%#[]";
    //private static String allowed = unreserved + reserved;
    private static String hexchars = "0123456789ABCDEFabcdef";    

    // This function returns a percent sign followed by two hexadecimal digits.
    // Input is a decimal value not greater than 255.
    private static String gethex(int decimal) {
        return "%" + hexchars.charAt(decimal >> 4) + hexchars.charAt(decimal & 0xF);
    }

    public static String encodeUrlPath(String path, String delimiter) {
        StringBuffer result = new StringBuffer();
        String tokens[] = path.split(delimiter);
        for (int i = 0; i < tokens.length; i++) {
            result.append(encodeUrlString(tokens[i]));
            if (i < tokens.length - 1) {
                result.append(delimiter);
            }
        }
        return result.toString();        
    }
    
    public static String encodeUrlString(String decoded) {
        String encoded = "";
        //char[] decoded = in.toCharArray();

        for (int i = 0; i < decoded.length(); i++) {
            char ch = decoded.charAt(i);
            // Check if character is an unreserved character:
            if (unreserved.indexOf(ch) != -1) {
                encoded = encoded + ch;
            } else {

                // The position in the Unicode table tells us how many bytes are needed.
                // Note that if we talk about first, second, etc. in the following, we are
                // counting from left to right:
                //
                //   Position in   |  Bytes needed   | Binary representation
                //  Unicode table  |   for UTF-8     |       of UTF-8
                // ----------------------------------------------------------
                //     0 -     127 |    1 byte       | 0XXX.XXXX
                //   128 -    2047 |    2 bytes      | 110X.XXXX 10XX.XXXX
                //  2048 -   65535 |    3 bytes      | 1110.XXXX 10XX.XXXX 10XX.XXXX
                // 65536 - 2097151 |    4 bytes      | 1111.0XXX 10XX.XXXX 10XX.XXXX 10XX.XXXX

                int charcode = decoded.codePointAt(i);

                // Position 0 - 127 is equal to percent-encoding with an ASCII character encoding:
                if (charcode < 128) {
                    encoded = encoded + gethex(charcode);
                }

                // Position 128 - 2047: two bytes for UTF-8 character encoding.
                if (charcode > 127 && charcode < 2048) {
                    // First UTF byte: Mask the first five bits of charcode with binary 110X.XXXX:
                    encoded = encoded + gethex((charcode >> 6) | 0xC0);
                    // Second UTF byte: Get last six bits of charcode and mask them with binary 10XX.XXXX:
                    encoded = encoded + gethex((charcode & 0x3F) | 0x80);
                }

                // Position 2048 - 65535: three bytes for UTF-8 character encoding.
                if (charcode > 2047 && charcode < 65536) {
                    // First UTF byte: Mask the first four bits of charcode with binary 1110.XXXX:
                    encoded = encoded + gethex((charcode >> 12) | 0xE0);
                    // Second UTF byte: Get the next six bits of charcode and mask them binary 10XX.XXXX:
                    encoded = encoded + gethex(((charcode >> 6) & 0x3F) | 0x80);
                    // Third UTF byte: Get the last six bits of charcode and mask them binary 10XX.XXXX:
                    encoded = encoded + gethex((charcode & 0x3F) | 0x80);
                }

                // Position 65536 - : four bytes for UTF-8 character encoding.
                if (charcode > 65535) {
                    // First UTF byte: Mask the first three bits of charcode with binary 1111.0XXX:
                    encoded = encoded + gethex((charcode >> 18) | 0xF0);
                    // Second UTF byte: Get the next six bits of charcode and mask them binary 10XX.XXXX:
                    encoded = encoded + gethex(((charcode >> 12) & 0x3F) | 0x80);
                    // Third UTF byte: Get the last six bits of charcode and mask them binary 10XX.XXXX:
                    encoded = encoded + gethex(((charcode >> 6) & 0x3F) | 0x80);
                    // Fourth UTF byte: Get the last six bits of charcode and mask them binary 10XX.XXXX:
                    encoded = encoded + gethex((charcode & 0x3F) | 0x80);
                }

            }
        }
        
        return encoded;
    }
}



