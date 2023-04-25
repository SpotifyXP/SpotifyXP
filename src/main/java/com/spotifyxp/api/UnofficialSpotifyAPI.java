package com.spotifyxp.api;


import com.spotifyxp.PublicValues;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class UnofficialSpotifyAPI {
    //This API is used in the Official Spotify Client
    String api = "";
    public UnofficialSpotifyAPI(String apitoken) {
        api = apitoken;
    }

    public void refresh(String token) {
        api = token;
    }
    public static void test(String api) {
        //https://spclient.wg.spotify.com/extended-metadata/v0/extended-metadata
        //Content-Type application/protobuf
        //Authorization: Bearer TOKEN

        HttpClient client = new HttpClient();
        GetMethod post = new GetMethod("https://spclient.wg.spotify.com/color-lyrics/v2/track/0m1F05fy6JmrTrJShFHPm4?format=json&vocalRemoval=false");
        post.setRequestHeader("Authorization", "Bearer " + api);
        post.setRequestHeader("App-Platform", "Win32");
        post.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.5563.65 Spotify/1.2.9.743 Safari/537.36");

        try {
            client.executeMethod(post);
            System.out.println(post.getResponseBodyAsString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("End of function");
    }

    String makeGet(String url, Header... headers) {
        HttpClient client = new HttpClient();
        GetMethod post = new GetMethod(url);
        post.setRequestHeader("Authorization", "Bearer " + api);
        post.setRequestHeader("App-Platform", "Win32");
        post.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.5563.65 Spotify/1.2.9.743 Safari/537.36");
        for(Header header : headers) {
            post.setRequestHeader(header);
        }
        try {
            client.executeMethod(post);
            return post.getResponseBodyAsString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "FAILED";
    }

    String makePost(String url, Header... headers) {
        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod(url);
        post.setRequestHeader("Authorization", "Bearer " + api);
        post.setRequestHeader("App-Platform", "Win32");
        post.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.5563.65 Spotify/1.2.9.743 Safari/537.36");
        for(Header header : headers) {
            post.setRequestHeader(header);
        }
        try {
            client.executeMethod(post);
            return post.getResponseBodyAsString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "FAILED";
    }

    public static class Lyrics {
        public String syncType = "";
        public ArrayList<LyricsLine> lines = new ArrayList<>();
        public String language = "";
        public String providerDisplayName = "";
        public String provider = "";
        public String providerLyricsId = "";
    }
    public static class LyricsLine {
        public long startTimeMs = 0;
        public String words = "";
        public long endTimeMs = 0; //Usually 0
    }

    public Lyrics getLyrics(String uri) {
        JSONObject object = new JSONObject(makeGet("https://spclient.wg.spotify.com/color-lyrics/v2/track/" + uri.split(":")[2] + "?format=json&vocalRemoval=false"));
        JSONObject lyricsroot = new JSONObject(object.getJSONObject("lyrics").toString());
        Lyrics lyrics = new Lyrics();
        lyrics.language = lyricsroot.getString("language");
        lyrics.providerLyricsId = lyricsroot.getString("providerLyricsId");
        lyrics.providerDisplayName = lyricsroot.getString("providerDisplayName");
        lyrics.syncType = lyricsroot.getString("syncType");
        for(Object line : lyricsroot.getJSONArray("lines")) {
            JSONObject l = new JSONObject(line.toString());
            LyricsLine lyricsLine = new LyricsLine();
            lyricsLine.endTimeMs = Long.parseLong(l.getString("endTimeMs"));
            lyricsLine.startTimeMs = Long.parseLong(l.getString("startTimeMs"));
            lyricsLine.words = l.getString("words");
            lyrics.lines.add(lyricsLine);
        }
        return lyrics;
    }

    public static class Artist {
        public String id = "";
        public String uri = "";
        public boolean saved = false;
        public String name = "";
        public String biography = "";
        public String artistImage = "";
        public String artistBackgroundImage = "";
        public String a = "";
    }

    public static class ArtistAlbum {

    }

    public static class ArtistTopTrack {

    }

    public static class ArtistPlaylist {
        public String uri = "";
        public String name = "";
        public String description = "";
        public String imageURL = "";
    }

    //ToDo: Find out for what the 256hash is
    public void getArtist(String uri) {
        JSONObject root = new JSONObject(new JSONObject(makeGet("https://api-partner.spotify.com/pathfinder/v1/query?operationName=queryArtistOverview&variables=%7B%22uri%22:%22" + uri + "%22,%22locale%22:%22%22%7D&extensions=%7B%22persistedQuery%22:%7B%22version%22:1,%22sha256Hash%22:%22b82fd661d09d47afff0d0239b165e01c7b21926923064ecc7e63f0cde2b12f4e%22%7D%7D")).getJSONObject("data").getJSONObject("artistUnion").toString());
        System.out.println(root);
    }

    public static class HomeTab {
        public String greeting = "";

    }


    public HomeTab getHomeTab() {
        JSONObject root = new JSONObject(new JSONObject(makeGet("https://api-partner.spotify.com/pathfinder/v1/query?operationName=home&variables=%7B%22timeZone%22%3A%22Europe%2FBerlin%22%7D&extensions=%7B%22persistedQuery%22%3A%7B%22version%22%3A1%2C%22sha256Hash%22%3A%2263c412a34a2071adfd99b804ea2fe1d8e9c5fd7d248e29ca54cc97a7ca06b561%22%7D%7D")).getJSONObject("data").getJSONObject("home").toString());
        HomeTab tab = new HomeTab();

        System.err.println("Parsing greeting message");
        //Parse greeting text (z.b Good evening)
        tab.greeting = root.getJSONObject("greeting").getString("text");
        //---
        System.err.println("---");

        System.err.println("Parsing sections");
        //Parse sections
        for(Object o : root.getJSONObject("sectionContainer").getJSONObject("sections").getJSONArray("items")) {
            JSONObject section = new JSONObject(o.toString());
            JSONObject sectionItems = new JSONObject(section.getJSONObject("sectionItems").toString());
            int totalCount = sectionItems.getInt("totalCount");
            String typeName = section.getJSONObject("data").getString("__typename");
            System.out.println("TotalCount: " + totalCount);
            System.out.println("TypeName: " + typeName);
            String uri = section.getString("uri");
            for(Object i : sectionItems.getJSONArray("items")) {
                JSONObject item = new JSONObject(i.toString());
                if(item.getJSONObject("content").getString("__typename").equals("UnknownType")) {
                    continue; //Skipping items that have no usable data
                }
                String itemuri = item.getString("uri");
                JSONObject content = new JSONObject(item.getJSONObject("content").getJSONObject("data").toString());
                String itemname = content.getString("name");
                //ToDo: Parse uri and check if its a playlist a track a artist a album or a show or something else
                System.out.println("   "  + itemname);
            }
            System.out.println("EndOfType\n\n");
        }
        System.err.println("---");
        //---

        System.out.println(root);
        return tab;
    }
}
