package com.spotifyxp.performance;

import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.Track;
import com.spotifyxp.factory.Factory;
import com.spotifyxp.guielements.DefTable;

import java.util.ArrayList;

public class ListUtils {
    public static enum URITypes {
        PLAYLIST,
        TRACK,
        ALBUM,
        SHOW,
        EPISODE
    }

    public static void addAllTracksToList(URITypes type, ArrayList<String> uricache, DefTable table, String uri) throws Exception {
        switch (type) {
            case SHOW:
                //Name, Filesize, Bitrate, HHMMSS of Track

                break;
            case ALBUM:
                //Name, Filesize, Bitrate, HHMMSS of Track

                break;
            case PLAYLIST:
                //Name, Filesize, Bitrate, HHMMSS of Track

                break;
            default:
                throw new RuntimeException("Invalid URIType: " + type.toString());
        }
    }
}
