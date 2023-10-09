package com.spotifyxp.testing;


import com.spotifyxp.PublicValues;
import com.spotifyxp.api.RestAPI;
import com.spotifyxp.args.CustomSaveDir;
import com.spotifyxp.configuration.Config;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.deps.com.spotify.connectstate.Connect;
import com.spotifyxp.deps.xyz.gianlu.librespot.core.Session;
import com.spotifyxp.deps.xyz.gianlu.librespot.mercury.MercuryClient;
import com.spotifyxp.dialogs.LoginDialog;
import com.spotifyxp.lastfm.LastFM;
import com.spotifyxp.lastfm.LastFMDialog;
import com.spotifyxp.lastfm.LastFMUserDialog;
import com.spotifyxp.lib.libLanguage;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.logging.ConsoleLoggingModules;
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
import org.checkerframework.checker.units.qual.C;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class Test {
    public static void main(String[] args) throws IOException, GeneralSecurityException, Session.SpotifyAuthenticationException, MercuryClient.MercuryException {

    }
}
