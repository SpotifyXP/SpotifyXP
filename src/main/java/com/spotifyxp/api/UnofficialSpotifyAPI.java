package com.spotifyxp.api;


import com.spotifyxp.PublicValues;
import com.spotifyxp.deps.com.spotify.metadata.Metadata;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.TimeZone;
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

    String encodeURL(String url) {
        try {
            return URLEncoder.encode(url, StandardCharsets.UTF_8.toString()).replace("%3D", "=").replace("%26", "&");
        }catch (UnsupportedEncodingException e) {
            return URLEncoder.encode(url).replace("%3D", "=").replace("%26", "&"); //Encode url without giving him the charset use Fixme: Should check if the charset is the right one
        }
    }

    String sectionToUrl(String section) {
        //https://api-partner.spotify.com/pathfinder/v1/query?operationName=homeSection&variables={"uri":"spotify:section:0JQ5DAIiKWzVFULQfUm85X","timeZone":"Europe/Berlin"}&extensions={"persistedQuery":{"version":1,"sha256Hash":"ca9cf7237e096dbab38e88ef1cb0ecbb65c8039bffd1b415f7ee8bc963607158"}}
        return "https://api-partner.spotify.com/pathfinder/v1/query?" + encodeURL("operationName=homeSection&variables={\"uri\":\"" + section + "\",\"timeZone\" : \"" + TimeZone.getDefault().toZoneId() + "\"}&extensions={\"persistedQuery\":{\"version\":1,\"sha256Hash\":\"ca9cf7237e096dbab38e88ef1cb0ecbb65c8039bffd1b415f7ee8bc963607158\"}}");
    }

    String getPathFinder(String operationName, String uri) {
        if(uri!=null) {
            return "https://api-partner.spotify.com/pathfinder/v1/query?" + encodeURL("operationName=" + operationName + "&variables={\"uri\":\"" + uri +"\",\"timeZone\":\"" + TimeZone.getDefault().toZoneId() + "\"}&extensions={\"persistedQuery\":{\"version\":1,\"sha256Hash\":\"ca9cf7237e096dbab38e88ef1cb0ecbb65c8039bffd1b415f7ee8bc963607158\"}}");
        }else {
            return "https://api-partner.spotify.com/pathfinder/v1/query?" + encodeURL("operationName=" + operationName + "&variables={\"timeZone\":\"" + TimeZone.getDefault().toZoneId() + "\"}&extensions={\"persistedQuery\":{\"version\":1,\"sha256Hash\":\"ca9cf7237e096dbab38e88ef1cb0ecbb65c8039bffd1b415f7ee8bc963607158\"}}");
        }
    }

    public JSONObject getSectionData(String section) {
        if(!section.contains("0JQ5DA")) {
            return new JSONObject(); //Return empty jsonobject for invalid section
        }
        JSONObject root = new JSONObject(makeGet(sectionToUrl(section)));

        return root;
    }

    public static class HomeTab {
        public String greeting = "";
        public ArrayList<HomeTabSection> sections = new ArrayList<>();
        public HomeTabSectionNoName firstSection = new HomeTabSectionNoName(); //Holds user liked songs thingy and more
    }

    public static class HomeTabImage {
        public ArrayList<HomeTabImageSource> sources = new ArrayList<>();
    }

    public static class HomeTabImageSource {
        public String width = "";
        public String height = "";
        public String url = "";
    }

    public static class HomeTabPlaylist {
        public String name = "";
        public String description = "";
        public String uri = "";
        public String ownerName = "";
        public ArrayList<HomeTabImage> images = new ArrayList<>();
    }

    public static class HomeTabArtist {
        public String name = "";
        public String uri = "";
        public ArrayList<HomeTabImage> images = new ArrayList<>();
    }

    public static class HomeTabArtistNoImage {
        public String name = "";
        public String uri = "";
    }

    public static class HomeTabAlbum {
        public String name = "";
        public String uri = "";
        public ArrayList<HomeTabArtistNoImage> artists = new ArrayList<>();
        public ArrayList<HomeTabImage> images = new ArrayList<>();
    }

    public static class HomeTabSectionNoName {
        //Has liked songs and some stuff
        public int totalCount = 0;
        public String uri = "";
        public ArrayList<HomeTabAlbum> albums = new ArrayList<>();
        public ArrayList<HomeTabArtist> artists = new ArrayList<>();
        public ArrayList<HomeTabPlaylist> playlists = new ArrayList<>();
    }

    public static class HomeTabSection {
        public int totalCount = 0;
        public String name = "";
        public String uri = "";
        public ArrayList<HomeTabAlbum> albums = new ArrayList<>();
        public ArrayList<HomeTabArtist> artists = new ArrayList<>();
        public ArrayList<HomeTabPlaylist> playlists = new ArrayList<>();
    }

    static enum SectionTypes {
        HomeShortsSectionData,
        HomeGenericSectionData,
        HomeRecentlyPlayedSectionData,
    }

    static enum SectionItemTypes {
        UnknownType,
        NotFound,
        PlaylistResponseWrapper,
        ArtistResponseWrapper,
        AlbumResponseWrapper,
        EpisodeOrChapterResponseWrapper
    }

    public HomeTab getHomeTab() {
        JSONObject root = new JSONObject(new JSONObject(makeGet("https://api-partner.spotify.com/pathfinder/v1/query?operationName=home&variables=%7B%22timeZone%22%3A%22Europe%2FBerlin%22%7D&extensions=%7B%22persistedQuery%22%3A%7B%22version%22%3A1%2C%22sha256Hash%22%3A%2263c412a34a2071adfd99b804ea2fe1d8e9c5fd7d248e29ca54cc97a7ca06b561%22%7D%7D")).getJSONObject("data").getJSONObject("home").toString());
        HomeTab tab = new HomeTab();
        System.out.println(root);
        //System.err.println("Parsing greeting message");
        //Parse greeting text (z.b Good evening)
        tab.greeting = root.getJSONObject("greeting").getString("text");
        //System.out.println(tab.greeting);
        //---
        //System.err.println("---");

        //System.err.println("Parsing sections");
        //Parse sections
        int counter = 0;
        for(Object o : root.getJSONObject("sectionContainer").getJSONObject("sections").getJSONArray("items")) {
            if(counter == 0) {
                HomeTabSectionNoName userlist = new HomeTabSectionNoName();

                tab.firstSection = userlist;
                counter++;
                continue;
            }
            JSONObject section = new JSONObject(o.toString());
            JSONObject sectionItems = new JSONObject(section.getJSONObject("sectionItems").toString());
            int totalCount = sectionItems.getInt("totalCount");
            String typeName = section.getJSONObject("data").getString("__typename");
            String uri = section.getString("uri");
            HomeTabSection homeTabSection = new HomeTabSection();
            try {
                homeTabSection.name = section.getJSONObject("data").getJSONObject("title").getString("text");
            }catch (JSONException ignored) {
            }
            try {
                homeTabSection.uri = section.getString("uri");
            }catch (JSONException ignored) {
            }
            try {
                homeTabSection.totalCount = section.getJSONObject("sectionItems").getInt("totalCount");
            }catch (JSONException ignored) {
            }
            for(Object i : sectionItems.getJSONArray("items")) {
                JSONObject item = new JSONObject(i.toString());
                try {
                    if (item.getJSONObject("data").getString("__typename").equals("NotFound")) {
                        continue;
                    }
                }catch (JSONException e) {
                    continue;
                }
                try {
                    SectionItemTypes itemTypeName = SectionItemTypes.valueOf(item.getJSONObject("content").getString("__typename"));
                    JSONObject content;
                    JSONObject data;
                    HomeTabImage imageData;
                    switch (itemTypeName) {
                        case UnknownType:
                            continue;
                        case AlbumResponseWrapper:
                            content = new JSONObject(item.getJSONObject("content").toString());
                            data = new JSONObject(content.getJSONObject("data").toString());
                            HomeTabAlbum album = new HomeTabAlbum();
                            album.name = data.getString("name");
                            album.uri = data.getString("uri");
                            imageData = new HomeTabImage();
                            for(Object image : data.getJSONObject("coverArt").getJSONArray("sources")) {
                                JSONObject sourceData = new JSONObject(image.toString());
                                HomeTabImageSource imageSource = new HomeTabImageSource();
                                try {
                                    imageSource.width = sourceData.getString("width");
                                }catch (JSONException ignored) {
                                }
                                try {
                                    imageSource.height = sourceData.getString("height");
                                }catch (JSONException ignored) {
                                }
                                try {
                                    imageSource.url = sourceData.getString("url");
                                }catch (JSONException ignored) {
                                }
                                imageData.sources.add(imageSource);
                            }
                            album.images.add(imageData);
                            for(Object artist : data.getJSONObject("artists").getJSONArray("items")) {
                                JSONObject artistSource = new JSONObject(artist.toString());
                                HomeTabArtistNoImage artistData = new HomeTabArtistNoImage();
                                artistData.name = artistSource.getJSONObject("profile").getString("name");
                                artistData.uri = artistSource.getString("uri");
                                album.artists.add(artistData);
                            }
                            homeTabSection.albums.add(album);
                            break;
                        case ArtistResponseWrapper:
                            content = new JSONObject(item.getJSONObject("content").toString());
                            data = new JSONObject(content.getJSONObject("data").toString());
                            HomeTabArtist artist = new HomeTabArtist();
                            imageData = new HomeTabImage();
                            for(Object image : data.getJSONObject("visuals").getJSONArray("sources")) {
                                JSONObject sourceData = new JSONObject();
                                HomeTabImageSource imageSource = new HomeTabImageSource();
                                try {
                                    imageSource.height = sourceData.getString("height");
                                }catch (JSONException ignored) {
                                }
                                try {
                                    imageSource.width = sourceData.getString("width");
                                }catch (JSONException ignored) {
                                }
                                try {
                                    imageSource.url = sourceData.getString("url");
                                }catch (JSONException ignored) {
                                }
                                imageData.sources.add(imageSource);
                            }
                            artist.images.add(imageData);
                            try {
                                artist.name = data.getJSONObject("profile").getString("name");
                            }catch (JSONException ignored) {
                            }
                            try {
                                artist.uri = data.getString("uri");
                            }catch (JSONException ignored) {
                            }
                            homeTabSection.artists.add(artist);
                            break;
                        case PlaylistResponseWrapper:
                            content = new JSONObject(item.getJSONObject("content").toString());
                            data = new JSONObject(content.getJSONObject("data").toString());
                            HomeTabPlaylist playlist = new HomeTabPlaylist();
                            for (Object image : data.getJSONObject("images").getJSONArray("items")) {
                                HomeTabImage images = new HomeTabImage();
                                for (Object imageSource : new JSONObject(image.toString()).getJSONArray("sources")) {
                                    JSONObject sourceData = new JSONObject(imageSource.toString());
                                    HomeTabImageSource imagesSource = new HomeTabImageSource();
                                    try {
                                        imagesSource.height = sourceData.getString("height");
                                    } catch (JSONException ignored) {
                                        //Image height not available
                                    }
                                    try {
                                        imagesSource.width = sourceData.getString("width");
                                    } catch (JSONException ignored) {
                                        //Image width not available
                                    }
                                    imagesSource.url = sourceData.getString("url");
                                    images.sources.add(imagesSource);
                                }
                                playlist.images.add(images);
                            } //Get images
                            playlist.description = data.getString("description"); //Get description
                            playlist.name = data.getString("name"); //Get Name
                            playlist.ownerName = data.getJSONObject("ownerV2").getJSONObject("data").getString("name"); //Get Artist/Owner name
                            playlist.uri = item.getString("uri"); //Get Uri
                            homeTabSection.playlists.add(playlist);
                            break;
                        case EpisodeOrChapterResponseWrapper:
                            System.out.println("Not implemented: EpisodeOrChapterResponseWrapper");
                            System.out.println("At: " + counter);
                            break;
                    }
                }catch (IllegalArgumentException e) {
                    System.out.println("Found unsupported SectionType: " + item.getJSONObject("content").getString("__typename"));
                }
                tab.sections.add(homeTabSection);
                counter++;
            }
        }
        //---
        for(HomeTabSection section : tab.sections) {
            System.out.println(section.name);
        }
        return tab;
    }
}
