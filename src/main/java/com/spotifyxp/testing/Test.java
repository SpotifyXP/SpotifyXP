package com.spotifyxp.testing;


import com.spotifyxp.PublicValues;
import com.spotifyxp.args.CustomSaveDir;
import com.spotifyxp.configuration.Config;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.lastfm.LastFM;
import com.spotifyxp.lastfm.LastFMDialog;
import com.spotifyxp.lastfm.LastFMUserDialog;
import com.spotifyxp.lib.libLanguage;
import com.spotifyxp.support.MacOSSupportModule;
import com.spotifyxp.theming.ThemeLoader;
import com.spotifyxp.utils.URLUtils;
import com.spotifyxp.utils.WebUtils;
import com.spotifyxp.deps.de.umass.lastfm.*;
import com.spotifyxp.lastfm.LFMValues;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.cookie.BasicClientCookie;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.protocol.HttpContext;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class Test {
    public static void main(String[] args) throws Exception {
        new MacOSSupportModule();
        new CustomSaveDir().runArgument(new File("data").getAbsolutePath()).run();
        PublicValues.config = new Config();
        new LastFM();
        PublicValues.language = new libLanguage();
        PublicValues.language.setLanguageFolder("lang");
        PublicValues.language.setNoAutoFindLanguage("en");
        new ThemeLoader().loadTheme("DarkGreen");
        //new LastFMDialog().open();
        new LastFMUserDialog().open();
        //System.out.println(User.getRawInfo(Authenticator.getMobileSession(LFMValues.username, PublicValues.config.get(ConfigValues.lastfmpassword.name), LFMValues.apikey, LFMValues.apisecret)));
    }
}
