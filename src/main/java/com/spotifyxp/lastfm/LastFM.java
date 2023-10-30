package com.spotifyxp.lastfm;

import com.spotifyxp.PublicValues;
import com.spotifyxp.configuration.ConfigValues;

public class LastFM {
    public LastFM() {
        LFMValues.username = PublicValues.config.getString(ConfigValues.lastfmusername.name);
        if(PublicValues.config.getInt(ConfigValues.lastfmartistlimit.name) != 0) LFMValues.artistlimit = PublicValues.config.getInt(ConfigValues.lastfmartistlimit.name);
        if(PublicValues.config.getInt(ConfigValues.lastfmtracklimit.name) != 0) LFMValues.tracklimit = PublicValues.config.getInt(ConfigValues.lastfmtracklimit.name);
    }
}
