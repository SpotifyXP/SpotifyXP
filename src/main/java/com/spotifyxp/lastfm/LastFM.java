package com.spotifyxp.lastfm;

import com.spotifyxp.PublicValues;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.deps.de.umass.lastfm.*;

import java.util.ArrayList;

public class LastFM {
    public LastFM() {
        LFMValues.username = PublicValues.config.get(ConfigValues.lastfmusername.name);
        if(!PublicValues.config.get(ConfigValues.lastfmartistlimit.name).isEmpty()) LFMValues.artistlimit = Integer.parseInt(PublicValues.config.get(ConfigValues.lastfmartistlimit.name));
        if(!PublicValues.config.get(ConfigValues.lastfmtracklimit.name).isEmpty()) LFMValues.tracklimit = Integer.parseInt(PublicValues.config.get(ConfigValues.lastfmtracklimit.name));
    }
}
