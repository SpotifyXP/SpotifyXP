package com.spotifyxp.api;

import com.spotifyxp.PublicValues;
import com.spotifyxp.deps.com.spotify.canvaz.CanvazOuterClass;
import com.spotifyxp.deps.xyz.gianlu.librespot.mercury.MercuryClient;
import com.spotifyxp.factory.Factory;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.utils.GraphicalMessage;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.TimeZone;

public class UnofficialSpotifyAPI {
    //This API is used in the Official Spotify Client
    String api;

    public UnofficialSpotifyAPI(String apitoken) {
        api = apitoken;
    }


    /**
     * Refreshes the api token with new given one
     * @param token token in format of a Spotify API token
     */
    public void refresh(String token) {
        api = token;
    }

    private static String makeGetStatic(String url, Header... headers) {
        HttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        get.setHeader("Authorization", "Bearer " + Factory.getUnofficialSpotifyApi().api);
        get.setHeader("App-Platform", "Win32");
        get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.5563.65 Spotify/1.2.9.743 Safari/537.36");
        for (Header header : headers) {
            get.setHeader(header);
        }
        try {
            return EntityUtils.toString(client.execute(get).getEntity());
        } catch (IOException e) {
            ConsoleLogging.Throwable(e);
        }
        return "FAILED";
    }

    private String makeGet(String url, Header... headers) {
        HttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        get.setHeader("Authorization", "Bearer " + Factory.getUnofficialSpotifyApi().api);
        get.setHeader("App-Platform", "Win32");
        get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.5563.65 Spotify/1.2.9.743 Safari/537.36");
        for (Header header : headers) {
            get.setHeader(header);
        }
        try {
            return EntityUtils.toString(client.execute(get).getEntity());
        } catch (IOException e) {
            ConsoleLogging.Throwable(e);
        }
        return "FAILED";
    }

    private String makePost(String url, Header... headers) {
        HttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        post.setHeader("Authorization", "Bearer " + Factory.getUnofficialSpotifyApi().api);
        post.setHeader("App-Platform", "Win32");
        post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.5563.65 Spotify/1.2.9.743 Safari/537.36");
        for (Header header : headers) {
            post.setHeader(header);
        }
        try {
            return EntityUtils.toString(client.execute(post).getEntity());
        } catch (IOException e) {
            ConsoleLogging.Throwable(e);
        }
        return "FAILED";
    }


    public static class Lyrics {
        public String syncType = "";
        public final ArrayList<LyricsLine> lines = new ArrayList<>();
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

    /**
     * Returns lyrics for a track
     * @param uri uri of track
     * @return instance of Lyrics
     * @see Lyrics
     */
    public Lyrics getLyrics(String uri) {
        JSONObject object = new JSONObject(makeGet("https://spclient.wg.spotify.com/color-lyrics/v2/track/" + uri.split(":")[2] + "?format=json&vocalRemoval=false"));
        JSONObject lyricsroot = new JSONObject(object.getJSONObject("lyrics").toString());
        Lyrics lyrics = new Lyrics();
        lyrics.language = lyricsroot.getString("language");
        lyrics.providerLyricsId = lyricsroot.getString("providerLyricsId");
        lyrics.providerDisplayName = lyricsroot.getString("providerDisplayName");
        lyrics.syncType = lyricsroot.getString("syncType");
        for (Object line : lyricsroot.getJSONArray("lines")) {
            JSONObject l = new JSONObject(line.toString());
            LyricsLine lyricsLine = new LyricsLine();
            lyricsLine.endTimeMs = Long.parseLong(l.getString("endTimeMs"));
            lyricsLine.startTimeMs = Long.parseLong(l.getString("startTimeMs"));
            lyricsLine.words = l.getString("words");
            lyrics.lines.add(lyricsLine);
        }
        return lyrics;
    }

    public static String getHomeTabJSON() {
        String url;
        try {
            url = "https://api-partner.spotify.com/pathfinder/v1/query?operationName=home&variables=" + URLEncoder.encode("{\"timeZone\":\"" + ZoneId.systemDefault() + "\"}", Charset.defaultCharset().toString()) + "&extensions=%7B%22persistedQuery%22%3A%7B%22version%22%3A1%2C%22sha256Hash%22%3A%2263c412a34a2071adfd99b804ea2fe1d8e9c5fd7d248e29ca54cc97a7ca06b561%22%7D%7D";
        } catch (UnsupportedEncodingException e) {
            url = "https://api-partner.spotify.com/pathfinder/v1/query?operationName=home&extensions=%7B%22persistedQuery%22%3A%7B%22version%22%3A1%2C%22sha256Hash%22%3A%2263c412a34a2071adfd99b804ea2fe1d8e9c5fd7d248e29ca54cc97a7ca06b561%22%7D%7D";
        }
        return makeGetStatic(url);
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

    public static class ArtistPlaylist {
        public String uri = "";
        public String name = "";
        public String description = "";
        public String imageURL = "";
    }

    public void getArtist(String uri) {
        //b82fd661d09d47afff0d0239b165e01c7b21926923064ecc7e63f0cde2b12f4e
        JSONObject root = new JSONObject(new JSONObject(makeGet("https://api-partner.spotify.com/pathfinder/v1/query?" + encodeURL("operationName=queryArtistOverview&variables={\"uri\":\"" + uri + "\",\"locale\":\"\"}&extensions={\"persistedQuery\":{\"version\":1,\"sha256Hash\":\"\"}}"))).toString());
    }

    String encodeURL(String url) {
        try {
            return URLEncoder.encode(url, StandardCharsets.UTF_8.toString()).replace("%3D", "=").replace("%26", "&");
        } catch (UnsupportedEncodingException e) {
            ConsoleLogging.Throwable(e);
            GraphicalMessage.openException(e);
            return "";
        }
    }

    String sectionToUrl(String section) {
        //https://api-partner.spotify.com/pathfinder/v1/query?operationName=homeSection&variables={"uri":"spotify:section:0JQ5DAIiKWzVFULQfUm85X","timeZone":"Europe/Berlin"}&extensions={"persistedQuery":{"version":1,"sha256Hash":"ca9cf7237e096dbab38e88ef1cb0ecbb65c8039bffd1b415f7ee8bc963607158"}}
        return "https://api-partner.spotify.com/pathfinder/v1/query?" + encodeURL("operationName=homeSection&variables={\"uri\":\"" + section + "\",\"timeZone\" : \"" + TimeZone.getDefault().toZoneId() + "\"}&extensions={\"persistedQuery\":{\"version\":1,\"sha256Hash\":\"ca9cf7237e096dbab38e88ef1cb0ecbb65c8039bffd1b415f7ee8bc963607158\"}}");
    }

    String getPathFinder(String operationName, String uri) {
        if (uri != null) {
            return "https://api-partner.spotify.com/pathfinder/v1/query?" + encodeURL("operationName=" + operationName + "&variables={\"uri\":\"" + uri + "\",\"timeZone\":\"" + TimeZone.getDefault().toZoneId() + "\"}&extensions={\"persistedQuery\":{\"version\":1,\"sha256Hash\":\"ca9cf7237e096dbab38e88ef1cb0ecbb65c8039bffd1b415f7ee8bc963607158\"}}");
        } else {
            return "https://api-partner.spotify.com/pathfinder/v1/query?" + encodeURL("operationName=" + operationName + "&variables={\"timeZone\":\"" + TimeZone.getDefault().toZoneId() + "\"}&extensions={\"persistedQuery\":{\"version\":1,\"sha256Hash\":\"ca9cf7237e096dbab38e88ef1cb0ecbb65c8039bffd1b415f7ee8bc963607158\"}}");
        }
    }

    public static class HomeTab {
        public String greeting = "";
        public final ArrayList<HomeTabSection> sections = new ArrayList<>();
        public HomeTabSectionNoName firstSection = new HomeTabSectionNoName(); //Holds user liked songs thingy and more
    }

    public static class HomeTabImage {
        public final ArrayList<HomeTabImageSource> sources = new ArrayList<>();
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
        public final ArrayList<HomeTabImage> images = new ArrayList<>();
    }

    public static class HomeTabArtist {
        public String name = "";
        public String uri = "";
        public final ArrayList<HomeTabImage> images = new ArrayList<>();
    }

    public static class HomeTabArtistNoImage {
        public String name = "";
        public String uri = "";
    }

    public static class HomeTabAlbum {
        public String name = "";
        public String uri = "";
        public final ArrayList<HomeTabArtistNoImage> artists = new ArrayList<>();
        public final ArrayList<HomeTabImage> images = new ArrayList<>();
    }

    public static class HomeTabEpisodeOrChapter {
        public long totalMilliseconds = 0;
        public String isoDate = "";
        public long playPositionMilliseconds = 0;
        public String EpisodeOrChapterName = "";
        public String description = "";
        //public String contentRating = ""; Don't know what the possible values are
        public String uri = "";
        public final ArrayList<HomeTabImage> EpisodeOrChapterImages = new ArrayList<>();
        public String name = "";
        public String publisherName = "";
        public final ArrayList<HomeTabImage> coverImages = new ArrayList<>();
    }

    public static class HomeTabSectionNoName {
        //Has liked songs and some stuff
        public int totalCount = 0;
        public String uri = "";
        public final ArrayList<HomeTabAlbum> albums = new ArrayList<>();
        public final ArrayList<HomeTabArtist> artists = new ArrayList<>();
        public final ArrayList<HomeTabPlaylist> playlists = new ArrayList<>();
        public final ArrayList<HomeTabEpisodeOrChapter> episodeOrChapters = new ArrayList<>();
    }

    public static class HomeTabSection {
        public int totalCount = 0;
        public String name = "";
        public String uri = "";
        public final ArrayList<HomeTabAlbum> albums = new ArrayList<>();
        public final ArrayList<HomeTabArtist> artists = new ArrayList<>();
        public final ArrayList<HomeTabPlaylist> playlists = new ArrayList<>();
        public final ArrayList<HomeTabEpisodeOrChapter> episodeOrChapters = new ArrayList<>();
    }

    enum SectionTypes {
        HomeShortsSectionData,
        HomeGenericSectionData,
        HomeRecentlyPlayedSectionData,
    }

    enum SectionItemTypes {
        UnknownType,
        PlaylistResponseWrapper,
        ArtistResponseWrapper,
        AlbumResponseWrapper,
        EpisodeOrChapterResponseWrapper,
        PodcastOrAudiobookResponseWrapper
    }

    int times = 0;

    /**
     * Gets the complete HomeTab (Used in the tab Home)
     * @return instance of HomeTab
     * @see HomeTab
     */
    public HomeTab getHomeTab() {
        JSONObject root = new JSONObject();
        try {
            //https://api-partner.spotify.com/pathfinder/v1/query?operationName=home&variables=%7B%22timeZone%22%3A%22Europe%2FBerlin%22%7D&extensions=%7B%22persistedQuery%22%3A%7B%22version%22%3A1%2C%22sha256Hash%22%3A%2263c412a34a2071adfd99b804ea2fe1d8e9c5fd7d248e29ca54cc97a7ca06b561%22%7D%7D
            String url = "https://api-partner.spotify.com/pathfinder/v1/query?operationName=home&variables=" + URLEncoder.encode("{\"timeZone\":\"" + ZoneId.systemDefault() + "\"}", Charset.defaultCharset().toString()) + "&extensions=%7B%22persistedQuery%22%3A%7B%22version%22%3A1%2C%22sha256Hash%22%3A%2263c412a34a2071adfd99b804ea2fe1d8e9c5fd7d248e29ca54cc97a7ca06b561%22%7D%7D";
            root = new JSONObject(new JSONObject(makeGet(url)).getJSONObject("data").getJSONObject("home").toString());
        }catch (JSONException e) {
            if(times>5) {
                GraphicalMessage.sorryError();
            }else{
                times+=1;
                return getHomeTab();
            }
        } catch (UnsupportedEncodingException e) {
            String url = "https://api-partner.spotify.com/pathfinder/v1/query?operationName=home&extensions=%7B%22persistedQuery%22%3A%7B%22version%22%3A1%2C%22sha256Hash%22%3A%2263c412a34a2071adfd99b804ea2fe1d8e9c5fd7d248e29ca54cc97a7ca06b561%22%7D%7D";
            root = new JSONObject(new JSONObject(makeGet(url)).getJSONObject("data").getJSONObject("home").toString());
        }
        HomeTab tab = new HomeTab();
        tab.greeting = root.getJSONObject("greeting").getString("text");
        int counter = 0;
        for (Object o : root.getJSONObject("sectionContainer").getJSONObject("sections").getJSONArray("items")) {
            if (counter == 0) {
                HomeTabSectionNoName userlist = new HomeTabSectionNoName();
                JSONObject section = new JSONObject(o.toString());
                try {
                    userlist.uri = section.getString("uri");
                } catch (JSONException e) {
                    ConsoleLogging.error("[HomeTab] Couldnt get uri for section");
                }
                try {
                    userlist.totalCount = section.getJSONObject("sectionItems").getInt("totalCount");
                } catch (JSONException e) {
                    ConsoleLogging.error("[HomeTab] Couldnt get totalcount for section");
                }
                int ic = 0;
                try {
                    for (Object i : new JSONObject(o.toString()).getJSONObject("sectionItems").getJSONArray("items")) {
                        if (ic == 0) {
                            ic++;
                            continue; //Guess: The first item always contains nothing
                        }
                        JSONObject item = new JSONObject(i.toString());
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
                                try {
                                    album.name = data.getString("name");
                                }catch (JSONException e) {
                                    ConsoleLogging.warning("[HomeTab] Couldnt get name for album");
                                }
                                try {
                                    album.uri = data.getString("uri");
                                }catch (JSONException e) {
                                    ConsoleLogging.warning("[HomeTab] Couldnt get uri for album");
                                }
                                imageData = new HomeTabImage();
                                for (Object image : data.getJSONObject("coverArt").getJSONArray("sources")) {
                                    JSONObject sourceData = new JSONObject(image.toString());
                                    HomeTabImageSource imageSource = new HomeTabImageSource();
                                    try {
                                        imageSource.url = sourceData.getString("url");
                                    } catch (JSONException ignored) {
                                        ConsoleLogging.warning("[HomeTab] Couldnt get url for image");
                                    }
                                    imageData.sources.add(imageSource);
                                }
                                album.images.add(imageData);
                                for (Object artist : data.getJSONObject("artists").getJSONArray("items")) {
                                    JSONObject artistSource = new JSONObject(artist.toString());
                                    HomeTabArtistNoImage artistData = new HomeTabArtistNoImage();
                                    try {
                                        artistData.name = artistSource.getJSONObject("profile").getString("name");
                                    } catch (JSONException e) {
                                        ConsoleLogging.warning("[HomeTab] Couldnt get name for artist");
                                    }
                                    try {
                                        artistData.uri = artistSource.getString("uri");
                                    } catch (JSONException e) {
                                        ConsoleLogging.warning("[HomeTab] Couldnt get uri for artist");
                                    }
                                    album.artists.add(artistData);
                                }
                                userlist.albums.add(album);
                                break;
                            case ArtistResponseWrapper:
                                content = new JSONObject(item.getJSONObject("content").toString());
                                data = new JSONObject(content.getJSONObject("data").toString());
                                HomeTabArtist artist = new HomeTabArtist();
                                imageData = new HomeTabImage();
                                try {
                                    for (Object image : data.getJSONObject("visuals").getJSONArray("sources")) {
                                        JSONObject sourceData = new JSONObject(image);
                                        HomeTabImageSource imageSource = new HomeTabImageSource();
                                        try {
                                            imageSource.url = sourceData.getString("url");
                                        } catch (JSONException ignored) {
                                            ConsoleLogging.warning("[HomeTab] Couldnt get url for image");
                                        }
                                        imageData.sources.add(imageSource);
                                    }
                                } catch (JSONException ignored) {
                                    //No images
                                }
                                artist.images.add(imageData);
                                try {
                                    artist.name = data.getJSONObject("profile").getString("name");
                                } catch (JSONException ignored) {
                                    ConsoleLogging.warning("[HomeTab] Couldnt get name for artist");
                                }
                                try {
                                    artist.uri = data.getString("uri");
                                } catch (JSONException ignored) {
                                    ConsoleLogging.warning("[HomeTab] Couldnt get uri for artist");
                                }
                                userlist.artists.add(artist);
                                break;
                            case PlaylistResponseWrapper:
                                content = new JSONObject(item.getJSONObject("content").toString());
                                data = new JSONObject(content.getJSONObject("data").toString());
                                HomeTabPlaylist playlist = new HomeTabPlaylist();
                                try {
                                    for (Object image : data.getJSONObject("images").getJSONArray("items")) {
                                        HomeTabImage images = new HomeTabImage();
                                        for (Object imageSource : new JSONObject(image.toString()).getJSONArray("sources")) {
                                            JSONObject sourceData = new JSONObject(imageSource.toString());
                                            HomeTabImageSource imagesSource = new HomeTabImageSource();
                                            try {
                                                imagesSource.url = sourceData.getString("url");
                                            } catch (JSONException e) {
                                                ConsoleLogging.warning("[HomeTab] Couldnt get url for image");
                                            }
                                            images.sources.add(imagesSource);
                                        }
                                        playlist.images.add(images);
                                    } //Get images
                                } catch (JSONException e) {
                                    //No images
                                }
                                try {
                                    playlist.description = data.getString("description"); //Get description
                                } catch (JSONException e) {
                                    ConsoleLogging.warning("[HomeTab] Couldnt get description for playlist");
                                }
                                try {
                                    playlist.name = data.getString("name"); //Get Name
                                } catch (JSONException e) {
                                    ConsoleLogging.warning("[HomeTab] Couldnt get name for playlist");
                                }
                                try {
                                    playlist.ownerName = data.getJSONObject("ownerV2").getJSONObject("data").getString("name"); //Get Artist/Owner name
                                } catch (JSONException e) {
                                    ConsoleLogging.warning("[HomeTab] Couldnt get owner of playlist");
                                }
                                try {
                                    playlist.uri = item.getString("uri"); //Get Uri
                                } catch (JSONException e) {
                                    ConsoleLogging.warning("[HomeTab] Couldnt get uri for playlist");
                                }
                                userlist.playlists.add(playlist);
                                break;
                            case EpisodeOrChapterResponseWrapper:
                                content = new JSONObject(item.getJSONObject("content").toString());
                                data = new JSONObject(content.getJSONObject("data").toString());
                                HomeTabEpisodeOrChapter eoc = new HomeTabEpisodeOrChapter();
                                if (data.getString("__typename").equals("GenericError")) {
                                    break;
                                }
                                try {
                                    eoc.totalMilliseconds = data.getJSONObject("duration").getLong("totalMilliseconds");
                                } catch (JSONException exception) {
                                    ConsoleLogging.warning("[HomeTab] Couldnt get duration for episode/chapter");
                                }
                                try {
                                    eoc.uri = item.getString("uri");
                                } catch (JSONException e) {
                                    ConsoleLogging.warning("[HomeTab] Couldnt get uri for episode/chapter");
                                }
                                try {
                                    eoc.isoDate = data.getJSONObject("releaseDate").getString("isoString");
                                } catch (JSONException e) {
                                    ConsoleLogging.warning("[HomeTab] Couldnt get isodate for episode/chapter");
                                }
                                try {
                                    eoc.playPositionMilliseconds = data.getJSONObject("playedState").getLong("playPositionMilliseconds");
                                } catch (JSONException e) {
                                    ConsoleLogging.warning("[HomeTab] Couldnt get play position for episode/chapter");
                                }
                                try {
                                    eoc.EpisodeOrChapterName = data.getString("name");
                                } catch (JSONException e) {
                                    ConsoleLogging.warning("[HomeTab] Couldnt get name for episode/chapter");
                                }
                                try {
                                    eoc.description = data.getString("description");
                                } catch (JSONException e) {
                                    ConsoleLogging.warning("[HomeTab] Couldnt get description for episode/chapter");
                                }
                                for (Object source : data.getJSONObject("coverArt").getJSONArray("sources")) {
                                    JSONObject coverSource = new JSONObject(source.toString());
                                    HomeTabImage coverImage = new HomeTabImage();
                                    HomeTabImageSource coverImageSource = new HomeTabImageSource();
                                    try {
                                        coverImageSource.url = coverSource.getString("url");
                                    } catch (JSONException e) {
                                        ConsoleLogging.warning("[HomeTab] Couldnt get url for image");
                                    }
                                    coverImage.sources.add(coverImageSource);
                                    eoc.EpisodeOrChapterImages.add(coverImage);
                                }
                                try {
                                    JSONObject podcastV2 = data.getJSONObject("podcastV2").getJSONObject("data");
                                    try {
                                        eoc.name = podcastV2.getString("name");
                                    }catch (JSONException e) {
                                        ConsoleLogging.warning("[HomeTab] Couldnt get name for podcast");
                                    }
                                    try {
                                        eoc.publisherName = podcastV2.getJSONObject("publisher").getString("name");
                                    }catch (JSONException e) {
                                        ConsoleLogging.warning("[HomeTab] Couldnt get name of publisher for podcast");
                                    }
                                    for (Object source : podcastV2.getJSONObject("coverArt").getJSONArray("sources")) {
                                        JSONObject coverSource = new JSONObject(source.toString());
                                        HomeTabImage coverImage = new HomeTabImage();
                                        HomeTabImageSource coverImageSource = new HomeTabImageSource();
                                        try {
                                            coverImageSource.url = coverSource.getString("url");
                                        } catch (JSONException e) {
                                            ConsoleLogging.warning("[HomeTab] Couldnt get url for image");
                                        }
                                        coverImage.sources.add(coverImageSource);
                                        eoc.coverImages.add(coverImage);
                                    }
                                } catch (JSONException e) {
                                    ConsoleLogging.error("HomeTab -> Can't parse podcastV2 element (JSONData)-> " + data);
                                }
                                userlist.episodeOrChapters.add(eoc);
                                break;
                        }
                    }
                    tab.firstSection = userlist;
                    counter++;
                    continue;
                } catch (JSONException e) {
                    ConsoleLogging.warning("[HomeTab] Couldnt parse section");
                }
            }
            JSONObject section = new JSONObject(o.toString());
            JSONObject sectionItems = new JSONObject(section.getJSONObject("sectionItems").toString());
            HomeTabSection homeTabSection = new HomeTabSection();
            try {
                homeTabSection.name = section.getJSONObject("data").getJSONObject("title").getString("text");
            } catch (JSONException ignored) {
                ConsoleLogging.warning("[HomeTab] Couldnt get name of section");
            }
            try {
                homeTabSection.uri = section.getString("uri");
            } catch (JSONException ignored) {
                ConsoleLogging.warning("[HomeTab] Couldnt get uri of section");
            }
            try {
                homeTabSection.totalCount = section.getJSONObject("sectionItems").getInt("totalCount");
            } catch (JSONException ignored) {
                ConsoleLogging.warning("[HomeTab] Couldnt get totalcount of section");
            }
            for (Object i : sectionItems.getJSONArray("items")) {
                JSONObject item = new JSONObject(i.toString());
                try {
                    if (item.getJSONObject("content").getJSONObject("data").getString("__typename").equals("NotFound")) {
                        continue;
                    }
                } catch (JSONException ignored) {
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
                            try {
                                album.name = data.getString("name");
                            }catch (JSONException e) {
                                ConsoleLogging.warning("[HomeTab] Couldnt get name for album");
                            }
                            try {
                                album.uri = data.getString("uri");
                            }catch (JSONException e) {
                                ConsoleLogging.warning("[HomeTab] Couldnt get uri for album");
                            }
                            imageData = new HomeTabImage();
                            for (Object image : data.getJSONObject("coverArt").getJSONArray("sources")) {
                                JSONObject sourceData = new JSONObject(image.toString());
                                HomeTabImageSource imageSource = new HomeTabImageSource();
                                try {
                                    imageSource.url = sourceData.getString("url");
                                } catch (JSONException ignored) {
                                    ConsoleLogging.warning("[HomeTab] Couldnt get url for image");
                                }
                                imageData.sources.add(imageSource);
                            }
                            album.images.add(imageData);
                            for (Object artist : data.getJSONObject("artists").getJSONArray("items")) {
                                JSONObject artistSource = new JSONObject(artist.toString());
                                HomeTabArtistNoImage artistData = new HomeTabArtistNoImage();
                                try {
                                    artistData.name = artistSource.getJSONObject("profile").getString("name");
                                }catch (JSONException e) {
                                    ConsoleLogging.warning("[HomeTab] Couldnt get name for artist");
                                }
                                try {
                                    artistData.uri = artistSource.getString("uri");
                                }catch (JSONException e) {
                                    ConsoleLogging.warning("[HomeTab] Couldnt get uri for artist");
                                }
                                album.artists.add(artistData);
                            }
                            homeTabSection.albums.add(album);
                            break;
                        case ArtistResponseWrapper:
                            content = new JSONObject(item.getJSONObject("content").toString());
                            data = new JSONObject(content.getJSONObject("data").toString());
                            HomeTabArtist artist = new HomeTabArtist();
                            imageData = new HomeTabImage();
                            try {
                                for (Object image : data.getJSONObject("visuals").getJSONArray("sources")) {
                                    JSONObject sourceData = new JSONObject(image.toString());
                                    HomeTabImageSource imageSource = new HomeTabImageSource();
                                    try {
                                        imageSource.url = sourceData.getString("url");
                                    } catch (JSONException ignored) {
                                        ConsoleLogging.warning("[HomeTab] Couldnt get url for image");
                                    }
                                    imageData.sources.add(imageSource);
                                }
                            } catch (JSONException ignored) {
                                //No images
                            }
                            artist.images.add(imageData);
                            try {
                                artist.name = data.getJSONObject("profile").getString("name");
                            } catch (JSONException ignored) {
                                ConsoleLogging.warning("[HomeTab] Couldnt get name for artist");
                            }
                            try {
                                artist.uri = data.getString("uri");
                            } catch (JSONException ignored) {
                                ConsoleLogging.warning("[HomeTab] Couldnt get uri for artist");
                            }
                            homeTabSection.artists.add(artist);
                            break;
                        case PlaylistResponseWrapper:
                            content = new JSONObject(item.getJSONObject("content").toString());
                            data = new JSONObject(content.getJSONObject("data").toString());
                            HomeTabPlaylist playlist = new HomeTabPlaylist();
                            try {
                                for (Object image : data.getJSONObject("images").getJSONArray("items")) {
                                    HomeTabImage images = new HomeTabImage();
                                    for (Object imageSource : new JSONObject(image.toString()).getJSONArray("sources")) {
                                        JSONObject sourceData = new JSONObject(imageSource.toString());
                                        HomeTabImageSource imagesSource = new HomeTabImageSource();
                                        try {
                                            imagesSource.url = sourceData.getString("url");
                                        }catch (JSONException e) {
                                            ConsoleLogging.warning("[HomeTab] Couldnt get url for image");
                                        }
                                        images.sources.add(imagesSource);
                                    }
                                    playlist.images.add(images);
                                } //Get images
                            } catch (JSONException e) {
                                //No images
                            }
                            try {
                                playlist.description = data.getString("description"); //Get description
                            }catch (JSONException e) {
                                ConsoleLogging.warning("[HomeTab] Couldnt get description for playlist");
                            }
                            try {
                                playlist.name = data.getString("name"); //Get Name
                            }catch (JSONException e) {
                                ConsoleLogging.warning("[HomeTab] Couldnt get name for playlist");
                            }
                            try {
                                playlist.ownerName = data.getJSONObject("ownerV2").getJSONObject("data").getString("name"); //Get Artist/Owner name
                            }catch (JSONException e) {
                                ConsoleLogging.warning("[HomeTab] Couldnt get owner of playlist");
                            }
                            try {
                                playlist.uri = item.getString("uri"); //Get Uri
                            }catch (JSONException e) {
                                ConsoleLogging.warning("[HomeTab] Couldnt get uri of playlist");
                            }
                            homeTabSection.playlists.add(playlist);
                            break;
                        case EpisodeOrChapterResponseWrapper:
                            content = new JSONObject(item.getJSONObject("content").toString());
                            data = new JSONObject(content.getJSONObject("data").toString());
                            HomeTabEpisodeOrChapter eoc = new HomeTabEpisodeOrChapter();
                            if (data.getString("__typename").equals("GenericError") || data.getString("__typename").equals("RestrictedContent")) {
                                break;
                            }
                            try {
                                eoc.totalMilliseconds = data.getJSONObject("duration").getLong("totalMilliseconds");
                            }catch (JSONException e) {
                                ConsoleLogging.warning("[HomeTab] Couldnt get total milliseconds for episode/chapter");
                            }
                            try {
                                eoc.uri = item.getString("uri");
                            }catch (JSONException e) {
                                ConsoleLogging.warning("[HomeTab] Couldnt get uri for episode/chapter");
                            }
                            try {
                                eoc.isoDate = data.getJSONObject("releaseDate").getString("isoString");
                            }catch (JSONException e) {
                                ConsoleLogging.warning("[HomeTab] Couldnt get isodate for episode/chapter");
                            }
                            try {
                                eoc.playPositionMilliseconds = data.getJSONObject("playedState").getLong("playPositionMilliseconds");
                            }catch (JSONException e) {
                                ConsoleLogging.warning("[HomeTab] Couldnt get play position for episode/chapter");
                            }
                            try {
                                eoc.EpisodeOrChapterName = data.getString("name");
                            }catch (JSONException e) {
                                ConsoleLogging.warning("[HomeTab] Couldnt get name for episode/chapter");
                            }
                            try {
                                eoc.description = data.getString("description");
                            }catch (JSONException e) {
                                ConsoleLogging.warning("[HomeTab] Couldnt get description for episode/chapter");
                            }
                            for (Object source : data.getJSONObject("coverArt").getJSONArray("sources")) {
                                JSONObject coverSource = new JSONObject(source.toString());
                                HomeTabImage coverImage = new HomeTabImage();
                                HomeTabImageSource coverImageSource = new HomeTabImageSource();
                                try {
                                    coverImageSource.url = coverSource.getString("url");
                                } catch (JSONException e) {
                                    ConsoleLogging.warning("[HomeTab] Couldnt get url for image");
                                }
                                coverImage.sources.add(coverImageSource);
                                eoc.EpisodeOrChapterImages.add(coverImage);
                            }
                            try {
                                JSONObject podcastV2 = data.getJSONObject("podcastV2").getJSONObject("data");
                                eoc.name = podcastV2.getString("name");
                                eoc.publisherName = podcastV2.getJSONObject("publisher").getString("name");
                                for (Object source : podcastV2.getJSONObject("coverArt").getJSONArray("sources")) {
                                    JSONObject coverSource = new JSONObject(source.toString());
                                    HomeTabImage coverImage = new HomeTabImage();
                                    HomeTabImageSource coverImageSource = new HomeTabImageSource();
                                    try {
                                        coverImageSource.url = coverSource.getString("url");
                                    } catch (JSONException e) {
                                        ConsoleLogging.warning("[HomeTab] Couldnt get url for image");
                                    }
                                    coverImage.sources.add(coverImageSource);
                                    eoc.coverImages.add(coverImage);
                                }
                            } catch (JSONException e) {
                                ConsoleLogging.error("HomeTab -> Can't parse podcastV2 element (JSONData)-> " + data);
                            }
                            homeTabSection.episodeOrChapters.add(eoc);
                            break;
                    }
                } catch (IllegalArgumentException e) {
                    ConsoleLogging.error("Found unsupported SectionType: " + item.getJSONObject("content").getString("__typename"));
                }
            }
            counter++;
            tab.sections.add(homeTabSection);
        }
        //---
        return tab;
    }


    public static String getCanvasURLForTrack(String uri) {
        try {
            return PublicValues.session.api().getCanvases(CanvazOuterClass.EntityCanvazRequest.newBuilder().addEntities(CanvazOuterClass.EntityCanvazRequest.Entity.newBuilder().setEntityUri(uri).buildPartial()).build()).getCanvases(0).getUrl();
        } catch (ArrayIndexOutOfBoundsException e) {
            return "";
        } catch (IOException | MercuryClient.MercuryException e) {
            throw new RuntimeException(e);
        }
    }


}
