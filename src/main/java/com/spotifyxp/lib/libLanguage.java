package com.spotifyxp.lib;

import com.spotifyxp.PublicValues;
import com.spotifyxp.utils.Resources;
import org.json.JSONObject;

import java.util.function.BiConsumer;

@SuppressWarnings("Convert2Lambda")
public class libLanguage {
    /*
    How to use:

    1. Define if library auto-finds the language or not (when not define the language code yourself look at: https://www.science.co.il/language/Locale-codes.php line 'Language Code' in the table)
    2. Set Language Files folder
    3. Enter translation key z.b hello.world

    INFO: All files must be located in the resources folder


    Language skeleton (Name the file as the language code it has z.b de.json)


    {
       "hello.world" : "Hello World",
       "hello.world2" : "Hello World 2"
     }
     */
    boolean afl = false;
    String languageCode = System.getProperty("user.language");
    String lf = "";
    public void setNoAutoFindLanguage(String code) {
        afl = false;
        languageCode = code;
    }
    boolean langNotFound = false;
    public void setAutoFindLanguage() {
        afl = true;
    }
    public void setLanguageFolder(String path) {
        lf = path;
    }
    public String removeComment(String json) {
        StringBuilder builder = new StringBuilder();
        for(String s : json.split("\n")) {
            if(s.replaceAll(" ", "").startsWith("//")) {
                continue;
            }
            builder.append(s);
        }
        return builder.toString();
    }
    String jsoncache = "";
    public String translateHTML(String htmlfile) {
        StringBuilder cache = new StringBuilder();
        for(String s : htmlfile.split("\n")) {
            if(s.equalsIgnoreCase("")) {
                continue; //Don't waste time here
            }
            if(s.contains("(TRANSLATE)")) {
                s = s.replace(s.split("\\(TRANSLATE\\)")[1].replace("(TRANSLATE)", ""), PublicValues.language.translate(s.split("\\(TRANSLATE\\)")[1].replace("(TRANSLATE)", "")));
                s = s.replace("(TRANSLATE)", "");
            }
            cache.append(s);
        }
        return cache.toString();
    }
    public String translate(String key) {
        final String[] ret = {key};
        if(!jsoncache.equals("")) {
            try {
                JSONObject object = new JSONObject(jsoncache);
                object.toMap().forEach(new BiConsumer<String, Object>() {
                    @Override
                    public void accept(String s, Object o) {
                        if(s.equals(key)) {
                            ret[0] = o.toString();
                        }
                    }
                });
                jsoncache = object.toString();
            } catch (Exception ignored) {
            }
        }
        if(afl) {
            try {
                JSONObject object = new JSONObject(removeComment(new Resources(true).readToString(lf + "/" + languageCode + ".json")));
                object.toMap().forEach(new BiConsumer<String, Object>() {
                    @Override
                    public void accept(String s, Object o) {
                        if(s.equals(key)) {
                            ret[0] = o.toString();
                        }
                    }
                });
                jsoncache = object.toString();
                return ret[0];
            } catch (Exception e) {
                if(langNotFound) {
                    return key;
                }
                languageCode = "en";
                langNotFound = true;
                return translate(key);
            }
        }else{
            if(!jsoncache.equals("")) {
                try {
                    JSONObject object = new JSONObject(jsoncache);
                    object.toMap().forEach(new BiConsumer<String, Object>() {
                        @Override
                        public void accept(String s, Object o) {
                            if(s.equals(key)) {
                                ret[0] = o.toString();
                            }
                        }
                    });
                    jsoncache = object.toString();
                    return ret[0];
                } catch (Exception ignored) {
                }
            }
            try {
                JSONObject object = new JSONObject(removeComment(new Resources(true).readToString(lf + "/" + languageCode + ".json")));
                object.toMap().forEach(new BiConsumer<String, Object>() {
                    @Override
                    public void accept(String s, Object o) {
                        if(s.equals(key)) {
                            ret[0] = o.toString();
                        }
                    }
                });
                jsoncache = object.toString();
            } catch (Exception e) {
                if(langNotFound) {
                    return key;
                }
                langNotFound = true;
                languageCode = "en";
                return translate(key);
            }
        }
        return ret[0];
    }
}
