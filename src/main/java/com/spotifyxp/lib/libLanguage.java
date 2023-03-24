package com.spotifyxp.lib;

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
    public void setAutoFindLanguage() {
        afl = true;
    }
    public void setLanguageFolder(String path) {
        lf = path;
    }
    public String translate(String key) {
        final String[] ret = {key};
        if(afl) {
            try {
                JSONObject object = new JSONObject(new Resources(true).readToString(lf + "/" + languageCode + ".json"));
                object.toMap().forEach(new BiConsumer<String, Object>() {
                    @Override
                    public void accept(String s, Object o) {
                        if(s.equals(key)) {
                            ret[0] = o.toString();
                        }
                    }
                });
            } catch (Exception e) {
                languageCode = "en";
                return translate(key);
            }
        }else{
            try {
                JSONObject object = new JSONObject(new Resources(true).readToString(lf + "/" + languageCode + ".json"));
                object.toMap().forEach(new BiConsumer<String, Object>() {
                    @Override
                    public void accept(String s, Object o) {
                        if(s.equals(key)) {
                            ret[0] = o.toString();
                        }
                    }
                });
            } catch (Exception e) {
                languageCode = "en";
                return translate(key);
            }
        }
        return ret[0];
    }
}
