package com.bookkeeping.utilities;

import com.google.gson.Gson;

import java.util.Locale;

/**
 * Created by chandanm on 11/20/16.
 */
public class LocaleUtility {
    public static void main(String[] args) {

        Locale locales[] = Locale.getAvailableLocales();
        Locale locale = new Locale("hi","IN");

        System.out.println(locale.getDisplayLanguage());
        /*
        Gson gson = new Gson();
        for(int i=0;i<locales.length;i++) {
            System.out.println(locales[i].getDisplayCountry() +":" + locales[i].toString() + "::" + locales[i].getCountry() + "::"
                    + locales[i].getDisplayCountry() + "::" + locales[i].getDisplayLanguage() + "::"
                    + locales[i].getDisplayName() + "::" + locales[i].getDisplayVariant() + "::"
                    + locales[i].getISO3Country() + "::" + locales[i].getISO3Language() + "::"
                    + locales[i].getLanguage() + "::" + locales[i].getVariant() + "");
        }
        */

    }
}
