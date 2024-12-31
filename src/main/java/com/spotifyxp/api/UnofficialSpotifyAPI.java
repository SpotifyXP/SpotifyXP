package com.spotifyxp.api;

import com.spotifyxp.PublicValues;
import com.spotifyxp.deps.com.spotify.canvaz.CanvazOuterClass;
import com.spotifyxp.deps.xyz.gianlu.librespot.mercury.MercuryClient;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.manager.InstanceManager;
import com.spotifyxp.utils.ApplicationUtils;
import com.spotifyxp.utils.ConnectionUtils;
import com.spotifyxp.utils.GraphicalMessage;
import com.spotifyxp.utils.MapUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "unused", "SameParameterValue"})
public class UnofficialSpotifyAPI {
    //This API is used in the Official Spotify Client
    String api;

    public UnofficialSpotifyAPI(String apitoken) {
        api = apitoken;
    }


    /**
     * Refreshes the api token with new given one
     *
     * @param token token in format of a Spotify API token
     */
    public void refresh(String token) {
        api = token;
    }

    /**
     * Holds all the information of the lyrics for a track
     */
    public static class Lyrics {
        public String syncType = "";
        public final ArrayList<LyricsLine> lines = new ArrayList<>();
        public String language = "";
        public String providerDisplayName = "";
        public String provider = "";
        public String providerLyricsId = "";
    }

    /**
     * Holds all the information of a lyrics text line
     */
    public static class LyricsLine {
        public long startTimeMs = 0;
        public String words = "";
        public long endTimeMs = 0; //Usually 0
    }

    /**
     * Returns lyrics for a track
     *
     * @param uri uri of track
     * @return instance of Lyrics
     * @see Lyrics
     */
    public Lyrics getLyrics(String uri) {
        try {
            JSONObject object = new JSONObject(ConnectionUtils.makeGet("https://spclient.wg.spotify.com/color-lyrics/v2/track/" + uri.split(":")[2] + "?format=json&vocalRemoval=false",
                    MapUtils.of("Authorization", "Bearer " + api, "App-Platform", "Win32", "User-Agent", ApplicationUtils.getUserAgent())));
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
        } catch (JSONException | IOException e) {
            return null;
        }
    }

    /**
     * Holds information about an artist
     */
    public static class Artist {
        public String id = "";
        public String uri = "";
        public boolean saved = false;
        public String name = "";
        public String biography = "";
        public String a = "";
    }

    /**
     * Holds information about the HomeTab content
     */
    public static class HomeTab {
        public String greeting = "";
        public final ArrayList<HomeTabSection> sections = new ArrayList<>();
        public HomeTabSectionNoName firstSection = new HomeTabSectionNoName(); //Holds user liked songs thingy and more
    }

    /**
     * Holds information about HomeTab images
     */
    public static class HomeTabImage {
        public final ArrayList<HomeTabImageSource> sources = new ArrayList<>();
    }

    /**
     * Holds information about an HomeTab image
     */
    public static class HomeTabImageSource {
        public String width = "";
        public String height = "";
        public String url = "";
    }

    /**
     * Holds information about an HomeTab playlist
     */
    public static class HomeTabPlaylist {
        public String name = "";
        public String description = "";
        public String uri = "";
        public String ownerName = "";
        public final ArrayList<HomeTabImage> images = new ArrayList<>();
    }

    /**
     * Holds information about an HomeTab artist
     */
    public static class HomeTabArtist {
        public String name = "";
        public String uri = "";
        public final ArrayList<HomeTabImage> images = new ArrayList<>();
    }

    /**
     * Holds information about an HomeTab artist (with image)
     */
    public static class HomeTabArtistNoImage {
        public String name = "";
        public String uri = "";
    }

    /**
     * Holds information about an HomeTab album
     */
    public static class HomeTabAlbum {
        public String name = "";
        public String uri = "";
        public final ArrayList<HomeTabArtistNoImage> artists = new ArrayList<>();
        public final ArrayList<HomeTabImage> images = new ArrayList<>();
    }

    /**
     * Holds information about an HomeTab episode or chapter
     */
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

    /**
     * Holds information:<br><br>
     * Liked songs entry<br>
     * Some other entries<br>
     */
    public static class HomeTabSectionNoName {
        //Has liked songs and some stuff
        public int totalCount = 0;
        public String uri = "";
        public final ArrayList<HomeTabAlbum> albums = new ArrayList<>();
        public final ArrayList<HomeTabArtist> artists = new ArrayList<>();
        public final ArrayList<HomeTabPlaylist> playlists = new ArrayList<>();
        public final ArrayList<HomeTabEpisodeOrChapter> episodeOrChapters = new ArrayList<>();
    }

    /**
     * Holds information about an HomeTab section
     */
    public static class HomeTabSection {
        public int totalCount = 0;
        public String name = "";
        public String uri = "";
        public final ArrayList<HomeTabAlbum> albums = new ArrayList<>();
        public final ArrayList<HomeTabArtist> artists = new ArrayList<>();
        public final ArrayList<HomeTabPlaylist> playlists = new ArrayList<>();
        public final ArrayList<HomeTabEpisodeOrChapter> episodeOrChapters = new ArrayList<>();
    }

    private enum SectionItemTypes {
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
     *
     * @return instance of HomeTab
     * @see HomeTab
     */
    public HomeTab getHomeTab() throws IOException {
        JSONObject root = new JSONObject();
        try {
            //https://api-partner.spotify.com/pathfinder/v1/query?operationName=home&variables=%7B%22timeZone%22%3A%22Europe%2FBerlin%22%7D&extensions=%7B%22persistedQuery%22%3A%7B%22version%22%3A1%2C%22sha256Hash%22%3A%2263c412a34a2071adfd99b804ea2fe1d8e9c5fd7d248e29ca54cc97a7ca06b561%22%7D%7D
            String url = "https://api-partner.spotify.com/pathfinder/v1/query?operationName=home&variables=" + URLEncoder.encode("{\"timeZone\":\"" + ZoneId.systemDefault() + "\"}", Charset.defaultCharset().toString()) + "&extensions=%7B%22persistedQuery%22%3A%7B%22version%22%3A1%2C%22sha256Hash%22%3A%2263c412a34a2071adfd99b804ea2fe1d8e9c5fd7d248e29ca54cc97a7ca06b561%22%7D%7D";
            root = new JSONObject(new JSONObject(ConnectionUtils.makeGet(url,
                    MapUtils.of("Authorization", "Bearer " + api, "App-Platform", "Win32", "User-Agent", ApplicationUtils.getUserAgent()))).getJSONObject("data").getJSONObject("home").toString());
        } catch (JSONException e) {
            if (times > 5) {
                GraphicalMessage.sorryError();
            } else {
                times += 1;
                return getHomeTab();
            }
        } catch (UnsupportedEncodingException e) {
            String url = "https://api-partner.spotify.com/pathfinder/v1/query?operationName=home&extensions=%7B%22persistedQuery%22%3A%7B%22version%22%3A1%2C%22sha256Hash%22%3A%2263c412a34a2071adfd99b804ea2fe1d8e9c5fd7d248e29ca54cc97a7ca06b561%22%7D%7D";
            root = new JSONObject(new JSONObject(ConnectionUtils.makeGet(url,
                    MapUtils.of("Authorization", "Bearer " + api, "App-Platform", "Win32", "User-Agent", ApplicationUtils.getUserAgent()))).getJSONObject("data").getJSONObject("home").toString());
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
                                } catch (JSONException e) {
                                    ConsoleLogging.warning("[HomeTab] Couldnt get name for album");
                                }
                                try {
                                    album.uri = data.getString("uri");
                                } catch (JSONException e) {
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
                                    } catch (JSONException e) {
                                        ConsoleLogging.warning("[HomeTab] Couldnt get name for podcast");
                                    }
                                    try {
                                        eoc.publisherName = podcastV2.getJSONObject("publisher").getString("name");
                                    } catch (JSONException e) {
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
                            } catch (JSONException e) {
                                ConsoleLogging.warning("[HomeTab] Couldnt get name for album");
                            }
                            try {
                                album.uri = data.getString("uri");
                            } catch (JSONException e) {
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
                            } catch (JSONException e) {
                                ConsoleLogging.warning("[HomeTab] Couldnt get total milliseconds for episode/chapter");
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

    /**
     * Gets an url referring to a canvas mp4 file for a track<br><br>
     * <a style="color:yellow;font:bold">!!Deprecated!! </a> Will be removed in a future version
     *
     * @param uri Track URI (spotify:track:xyz)
     * @return String
     */
    @Deprecated
    public static String getCanvasURLForTrack(String uri) {
        try {
            return PublicValues.session.api().getCanvases(CanvazOuterClass.EntityCanvazRequest.newBuilder().addEntities(CanvazOuterClass.EntityCanvazRequest.Entity.newBuilder().setEntityUri(uri).buildPartial()).build()).getCanvases(0).getUrl();
        } catch (ArrayIndexOutOfBoundsException e) {
            return "";
        } catch (IOException | MercuryClient.MercuryException e) {
            throw new RuntimeException(e);
        }
    }

    public static class SpotifyBrowse {
        private final String id;
        private final String title;
        private final ArrayList<SpotifyBrowseEntry> body;

        private SpotifyBrowse(String id, String title, ArrayList<SpotifyBrowseEntry> body) {
            this.id = id;
            this.title = title;
            this.body = body;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public ArrayList<SpotifyBrowseEntry> getBody() {
            return body;
        }

        public static SpotifyBrowse fromJSON(String json) {
            JSONObject jsonObject = new JSONObject(json);
            ArrayList<SpotifyBrowseEntry> entries = new ArrayList<>();
            for(Object object : jsonObject.getJSONArray("body")) {
                entries.add(SpotifyBrowseEntry.fromJSON(object.toString()));
            }
            return new SpotifyBrowse(jsonObject.getString("id"), jsonObject.getString("title"), entries);
        }
    }

    public static class SpotifyBrowseEntry {
        private final String id;
        private final SpotifyBrowseEntryComponent component;
        private final SpotifyBrowseEntryText text;
        private final Optional<SpotifyBrowseEntryCustom> custom;
        private final Optional<SpotifyBrowseEntryMetadata> metadata;
        private final Optional<SpotifyBrowserEntryImages> images;
        private final Optional<ArrayList<SpotifyBrowseEntry>> children;
        private final Optional<SpotifyBrowseEntryEvents> events;

        private SpotifyBrowseEntry(String id, SpotifyBrowseEntryComponent component, SpotifyBrowseEntryText text,
                                   Optional<SpotifyBrowseEntryCustom> custom,
                                   Optional<SpotifyBrowseEntryMetadata> metadata, Optional<SpotifyBrowserEntryImages> images,
                                   Optional<ArrayList<SpotifyBrowseEntry>> children, Optional<SpotifyBrowseEntryEvents> events) {
            this.id = id;
            this.component = component;
            this.text = text;
            this.custom = custom;
            this.metadata = metadata;
            this.images = images;
            this.children = children;
            this.events = events;
        }

        public Optional<SpotifyBrowseEntryEvents> getEvents() {
            return events;
        }

        public String getId() {
            return id;
        }

        public SpotifyBrowseEntryComponent getComponent() {
            return component;
        }

        public SpotifyBrowseEntryText getText() {
            return text;
        }

        public Optional<SpotifyBrowseEntryCustom> getCustom() {
            return custom;
        }

        public Optional<SpotifyBrowseEntryMetadata> getMetadata() {
            return metadata;
        }

        public Optional<SpotifyBrowserEntryImages> getImages() {
            return images;
        }

        public Optional<ArrayList<SpotifyBrowseEntry>> getChildren() {
            return children;
        }

        protected static SpotifyBrowseEntry fromJSON(String json) {
            JSONObject jsonObject = new JSONObject(json);
            Optional<SpotifyBrowserEntryImages> images = Optional.empty();
            if(jsonObject.has("images")) {
                images = Optional.of(SpotifyBrowserEntryImages.fromJSON(jsonObject.getJSONObject("images").toString()));
            }
            Optional<ArrayList<SpotifyBrowseEntry>> children = Optional.empty();
            if(jsonObject.has("children")) {
                ArrayList<SpotifyBrowseEntry> childrenList = new ArrayList<>();
                for(Object object : jsonObject.getJSONArray("children")) {
                    childrenList.add(SpotifyBrowseEntry.fromJSON(object.toString()));
                }
                children = Optional.of(childrenList);
            }
            Optional<SpotifyBrowseEntryCustom> custom = Optional.empty();
            if(jsonObject.has("custom")) {
                custom = Optional.of(SpotifyBrowseEntryCustom.fromJSON(jsonObject.getJSONObject("custom").toString()));
            }
            Optional<SpotifyBrowseEntryMetadata> metadata = Optional.empty();
            if(jsonObject.has("metadata")) {
                metadata = Optional.of(SpotifyBrowseEntryMetadata.fromJSON(jsonObject.getJSONObject("metadata").toString()));
            }
            Optional<SpotifyBrowseEntryEvents> events = Optional.empty();
            if(jsonObject.has("events")) {
                events = Optional.of(SpotifyBrowseEntryEvents.fromJSON(jsonObject.getJSONObject("events").toString()));
            }
            return new SpotifyBrowseEntry(jsonObject.getString("id"),
                    SpotifyBrowseEntryComponent.fromJSON(jsonObject.getJSONObject("component").toString()),
                    SpotifyBrowseEntryText.fromJSON(jsonObject.getJSONObject("text").toString()),
                    custom,
                    metadata,
                    images, children, events);
        }
    }

    public static class SpotifyBrowserEntryImages {
        private final ArrayList<SpotifyBrowseEntryImagesImage> images;

        private SpotifyBrowserEntryImages(ArrayList<SpotifyBrowseEntryImagesImage> images) {
            this.images = images;
        }

        public ArrayList<SpotifyBrowseEntryImagesImage> getImages() {
            return images;
        }

        protected static SpotifyBrowserEntryImages fromJSON(String json) {
            JSONObject jsonObject = new JSONObject(json);
            ArrayList<SpotifyBrowseEntryImagesImage> images = new ArrayList<>();
            for(String key : jsonObject.keySet()) {
                images.add(new SpotifyBrowseEntryImagesImage(jsonObject.getJSONObject(key).getString("uri"),
                        SpotifyBrowseEntryImagesImageTypes.valueOf(key.toUpperCase(Locale.ENGLISH))));
            }
            return new SpotifyBrowserEntryImages(images);
        }
    }

    public static class SpotifyBrowseEntryImagesImage {
        private final String uri;
        private final SpotifyBrowseEntryImagesImageTypes type;

        protected SpotifyBrowseEntryImagesImage(String uri, SpotifyBrowseEntryImagesImageTypes type) {
            this.uri = uri;
            this.type = type;
        }

        public String getUri() {
            return uri;
        }

        public SpotifyBrowseEntryImagesImageTypes getType() {
            return type;
        }
    }

    public enum SpotifyBrowseEntryImagesImageTypes {
        MAIN,
        BACKGROUND
    }

    public static class SpotifyBrowseEntryMetadata {
        private final String sectionId;
        private final Optional<String> uri;
        private final Optional<String> promotion_id;
        private final Optional<String> videoUrl;
        private final Optional<Boolean> isAnimated;
        private final Optional<String> accessibilityText;

        private SpotifyBrowseEntryMetadata(String sectionId, Optional<String> uri, Optional<String> promotion_id,
                                           Optional<String> videoUrl, Optional<Boolean> isAnimated,
                                           Optional<String> accessibilityText) {
            this.sectionId = sectionId;
            this.uri = uri;
            this.promotion_id = promotion_id;
            this.videoUrl = videoUrl;
            this.isAnimated = isAnimated;
            this.accessibilityText = accessibilityText;
        }

        public String getSectionId() {
            return sectionId;
        }

        public Optional<String> getUri() {
            return uri;
        }

        public Optional<String> getPromotion_id() {
            return promotion_id;
        }

        public Optional<String> getVideoUrl() {
            return videoUrl;
        }

        public Optional<Boolean> getIsAnimated() {
            return isAnimated;
        }

        public Optional<String> getAccessibilityText() {
            return accessibilityText;
        }

        protected static SpotifyBrowseEntryMetadata fromJSON(String json) {
            JSONObject jsonObject = new JSONObject(json);
            Optional<String> uri = Optional.empty();
            if(jsonObject.has("uri")) {
                uri = Optional.of(jsonObject.getString("uri"));
            }
            Optional<String> promotion_id = Optional.empty();
            if(jsonObject.has("promotion_id")) {
                promotion_id = Optional.of(jsonObject.getString("promotion_id"));
            }
            Optional<String> videoUrl = Optional.empty();
            if(jsonObject.has("video_url")) {
                videoUrl = Optional.of(jsonObject.getString("video_url"));
            }
            Optional<Boolean> isAnimated = Optional.empty();
            if(jsonObject.has("is_animated")) {
                isAnimated = Optional.of(jsonObject.getBoolean("is_animated"));
            }
            Optional<String> accessibilityText = Optional.empty();
            if(jsonObject.has("accessibility_text")) {
                accessibilityText = Optional.of(jsonObject.getString("accessibility_text"));
            }
            return new SpotifyBrowseEntryMetadata(jsonObject.getString("sectionId"), uri, promotion_id,
                    videoUrl, isAnimated, accessibilityText);
        }
    }

    /**
     * The only one implemented is the 'click' event and in it only the 'uri' data type is implemented
     */
    public static class SpotifyBrowseEntryEvents {
        private final ArrayList<SpotifyBrowseEntryEventsEvent> events;

        private SpotifyBrowseEntryEvents(ArrayList<SpotifyBrowseEntryEventsEvent> events) {
            this.events = events;
        }

        public ArrayList<SpotifyBrowseEntryEventsEvent> getEvents() {
            return events;
        }

        protected static SpotifyBrowseEntryEvents fromJSON(String json) {
            JSONObject jsonObject = new JSONObject(json);
            ArrayList<SpotifyBrowseEntryEventsEvent> events = new ArrayList<>();
            for(String key : jsonObject.keySet()) {
                // Only click is implemented right now
                if(key.equals("click")) {
                    events.add(SpotifyBrowseEntryEventsEvent.fromJSON(jsonObject.getJSONObject(key).toString(), SpotifyBrowseEntryEventsTypes.CLICK));
                    break;
                }
            }
            return new SpotifyBrowseEntryEvents(events);
        }
    }

    public static class SpotifyBrowseEntryEventsEvent {
        private final String name;
        private final SpotifyBrowseEntryEventsTypes type;
        private final Optional<SpotifyBrowseEntryEventsEventDataUri> data_uri;

        private SpotifyBrowseEntryEventsEvent(String name, SpotifyBrowseEntryEventsTypes type,
                                              Optional<SpotifyBrowseEntryEventsEventDataUri> data_uri) {
            this.name = name;
            this.type = type;
            this.data_uri = data_uri;
        }

        public String getName() {
            return name;
        }

        public SpotifyBrowseEntryEventsTypes getType() {
            return type;
        }

        public Optional<SpotifyBrowseEntryEventsEventDataUri> getData_uri() {
            return data_uri;
        }

        protected static SpotifyBrowseEntryEventsEvent fromJSON(String json, SpotifyBrowseEntryEventsTypes type) {
            JSONObject jsonObject = new JSONObject(json);
            Optional<SpotifyBrowseEntryEventsEventDataUri> data_uri = Optional.empty();
            if(jsonObject.getJSONObject("data").has("uri") && jsonObject.getJSONObject("data").keySet().size() == 1) {
                data_uri = Optional.of(SpotifyBrowseEntryEventsEventDataUri.fromJSON(jsonObject.getJSONObject("data").toString()));
            }
            return new SpotifyBrowseEntryEventsEvent(jsonObject.getString("name"), type, data_uri);
        }
    }

    public static class SpotifyBrowseEntryEventsEventDataUri {
        private final String uri;

        private SpotifyBrowseEntryEventsEventDataUri(String uri) {
            this.uri = uri;
        }

        public String getUri() {
            return uri;
        }

        protected static SpotifyBrowseEntryEventsEventDataUri fromJSON(String json) {
            JSONObject jsonObject = new JSONObject(json);
            return new SpotifyBrowseEntryEventsEventDataUri(new JSONObject(json).getString("uri"));
        }
    }

    public enum SpotifyBrowseEntryEventsEventDataTypes {
        URI
    }

    public enum SpotifyBrowseEntryEventsTypes {
        TOGGLEPLAYSTATECLICK,
        CLICK,
        CONTEXTMENUCLICK,
        TOGGLELIKESTATECLICK
    }

    public static class SpotifyBrowseEntryCustom {
        private final Optional<String> style;
        private final Optional<String> backgroundColor;
        private final Optional<String> entityType;

        private SpotifyBrowseEntryCustom(Optional<String> style, Optional<String> backgroundColor,
                                         Optional<String> entityType) {
            this.style = style;
            this.backgroundColor = backgroundColor;
            this.entityType = entityType;
        }

        public Optional<String> getStyle() {
            return style;
        }

        public Optional<String> getBackgroundColor() {
            return backgroundColor;
        }

        public Optional<String> getEntityType() {
            return entityType;
        }

        protected static SpotifyBrowseEntryCustom fromJSON(String json) {
            JSONObject jsonObject = new JSONObject(json);
            Optional<String> style = Optional.empty();
            if(jsonObject.has("style")) {
                style = Optional.of(jsonObject.getString("style"));
            }
            Optional<String> backgroundColor = Optional.empty();
            if(jsonObject.has("backgroundColor")) {
                backgroundColor = Optional.of(jsonObject.getString("backgroundColor"));
            }
            Optional<String> entityType = Optional.empty();
            if(jsonObject.has("entityType")) {
                entityType = Optional.of(jsonObject.getString("entityType"));
            }
            return new SpotifyBrowseEntryCustom(style, backgroundColor, entityType);
        }
    }

    public static class SpotifyBrowseEntryText {
        private final String title;
        private final Optional<String> accessory;
        private final Optional<String> description;
        private final Optional<String> subtitle;

        private SpotifyBrowseEntryText(String title, Optional<String> accessory, Optional<String> description, Optional<String> subtitle) {
            this.title = title;
            this.accessory = accessory;
            this.description = description;
            this.subtitle = subtitle;
        }

        public String getTitle() {
            return title;
        }


        public Optional<String> getAccessory() {
            return accessory;
        }

        public Optional<String> getDescription() {
            return description;
        }

        public Optional<String> getSubtitle() {
            return subtitle;
        }

        protected static SpotifyBrowseEntryText fromJSON(String json) {
            JSONObject jsonObject = new JSONObject(json);
            Optional<String> accessory = Optional.empty();
            if(jsonObject.has("accessory")) {
                accessory = Optional.of(jsonObject.getString("accessory"));
            }
            Optional<String> description = Optional.empty();
            if(jsonObject.has("description")) {
                description = Optional.of(jsonObject.getString("description"));
            }
            Optional<String> subtitle = Optional.empty();
            if(jsonObject.has("subtitle")) {
                subtitle = Optional.of(jsonObject.getString("subtitle"));
            }
            return new SpotifyBrowseEntryText(jsonObject.getString("title"), accessory, description, subtitle);
        }
    }

    public static class SpotifyBrowseEntryComponent {
        private final String id;
        private final String category;

        private SpotifyBrowseEntryComponent(String id, String category) {
            this.id = id;
            this.category = category;
        }

        public String getId() {
            return id;
        }

        public String getCategory() {
            return category;
        }

        protected static SpotifyBrowseEntryComponent fromJSON(String json) {
            JSONObject jsonObject = new JSONObject(json);
            return new SpotifyBrowseEntryComponent(jsonObject.getString("id"), jsonObject.getString("category"));
        }
    }

    public static class SpotifyBrowseSection {
        private final SpotifyBrowseEntry header;
        private final String id;
        private final ArrayList<SpotifyBrowseEntry> body;

        private SpotifyBrowseSection(SpotifyBrowseEntry header, String id,
                                     ArrayList<SpotifyBrowseEntry> body) {
            this.header = header;
            this.id = id;
            this.body = body;
        }

        public SpotifyBrowseEntry getHeader() {
            return header;
        }

        public String getId() {
            return id;
        }

        public ArrayList<SpotifyBrowseEntry> getBody() {
            return body;
        }

        public static SpotifyBrowseSection fromJSON(String json) {
            JSONObject jsonObject = new JSONObject(json);
            ArrayList<SpotifyBrowseEntry> entries = new ArrayList<>();
            for(Object object : jsonObject.getJSONArray("body")) {
                entries.add(SpotifyBrowseEntry.fromJSON(object.toString()));
            }
            return new SpotifyBrowseSection(SpotifyBrowseEntry.fromJSON(jsonObject.getJSONObject("header").toString()),
                    jsonObject.getString("id"), entries);
        }
    }

    public static SpotifyBrowse getSpotifyBrowse() throws IOException {
        String query = "?platform=android&client-timezone=" + URLEncoder.encode("{\"timeZone\":\"" + ZoneId.systemDefault() + "\"}", Charset.defaultCharset().toString()) + "&podcast=true&locale=" + Locale.getDefault();
        CompletableFuture<String> clientTokenFuture = new CompletableFuture<>();
        String clientToken = PublicValues.session.api().getClientToken();
        Thread checkThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!clientTokenFuture.isDone()) {
                    if(PublicValues.session.api().getClientToken() != null) {
                        clientTokenFuture.complete(PublicValues.session.api().getClientToken());
                    }
                    try {
                        Thread.sleep(TimeUnit.SECONDS.toMillis(1));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        if(clientToken == null) {
            checkThread.start();
            try {
                synchronized (clientTokenFuture) {
                    clientTokenFuture.wait();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return SpotifyBrowse.fromJSON(ConnectionUtils.makeGet("https://spclient.wg.spotify.com/hubview-mobile-v1/browse" + query, new HashMap<String, String>() {{
            put("client-token", clientToken);
            put("authorization", "Bearer " + InstanceManager.getPkce().getToken());
        }}));
    }


    // ToDo: Add error handling
    // The endpoint returns an id of 'browse-page-mobile-fallback' when there is something wrong
    public static SpotifyBrowseSection getSpotifyBrowseSection(String sectionUri) throws IOException {
        String query = "?platform=android&client-timezone=" + URLEncoder.encode("{\"timeZone\":\"" + ZoneId.systemDefault() + "\"}", Charset.defaultCharset().toString()) + "&podcast=true&locale=" + Locale.getDefault();
        CompletableFuture<String> clientTokenFuture = new CompletableFuture<>();
        String clientToken = PublicValues.session.api().getClientToken();
        Thread checkThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!clientTokenFuture.isDone()) {
                    if(PublicValues.session.api().getClientToken() != null) {
                        clientTokenFuture.complete(PublicValues.session.api().getClientToken());
                    }
                    try {
                        Thread.sleep(TimeUnit.SECONDS.toMillis(1));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        if(clientToken == null) {
            checkThread.start();
            try {
                clientTokenFuture.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return SpotifyBrowseSection.fromJSON(ConnectionUtils.makeGet("https://spclient.wg.spotify.com/hubview-mobile-v1/browse/" + sectionUri + query, new HashMap<String, String>() {{
            put("client-token", clientToken);
            put("authorization", "Bearer " + InstanceManager.getPkce().getToken());
        }}));
    }
}
