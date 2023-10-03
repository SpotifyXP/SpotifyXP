package com.spotifyxp.lastfm;

import com.spotifyxp.PublicValues;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.deps.de.umass.lastfm.Authenticator;
import com.spotifyxp.deps.de.umass.lastfm.ImageSize;
import com.spotifyxp.deps.de.umass.lastfm.User;
import com.spotifyxp.swingextension.JFrame2;
import com.spotifyxp.swingextension.JImagePanel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class LastFMUserDialog extends JFrame2 {
    public LastFMUserDialog() {
        User user = User.getInfo(Authenticator.getMobileSession(LFMValues.username, PublicValues.config.get(ConfigValues.lastfmpassword.name), LFMValues.apikey, LFMValues.apisecret));

        setPreferredSize(new Dimension(350, 600));
        setLayout(null);
        JImagePanel userimage = new JImagePanel();
        userimage.setBounds(80, 6, 180, 180);
        add(userimage);
        try {
            userimage.setImage(new URL(user.getImageURL(ImageSize.LARGE)).openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        setTitle("Last.fm - " + LFMValues.username);

        JLabel userusername = new JLabel(PublicValues.language.translate("ui.lastfm.user.username"));
        userusername.setBounds(6, 204, 130, 16);
        add(userusername);
        userusername.setHorizontalAlignment(SwingConstants.RIGHT);

        JLabel userrealname = new JLabel(PublicValues.language.translate("ui.lastfm.user.realname"));
        userrealname.setBounds(6, 232, 130, 16);
        add(userrealname);
        userrealname.setHorizontalAlignment(SwingConstants.RIGHT);

        JLabel usercountry = new JLabel(PublicValues.language.translate("ui.lastfm.user.country"));
        usercountry.setBounds(6, 260, 130, 16);
        add(usercountry);
        usercountry.setHorizontalAlignment(SwingConstants.RIGHT);

        JLabel userage = new JLabel(PublicValues.language.translate("ui.lastfm.user.age"));
        userage.setBounds(6, 288, 130, 16);
        add(userage);
        userage.setHorizontalAlignment(SwingConstants.RIGHT);

        JLabel usergender = new JLabel(PublicValues.language.translate("ui.lastfm.user.gender"));
        usergender.setBounds(6, 316, 130, 16);
        add(usergender);
        usergender.setHorizontalAlignment(SwingConstants.RIGHT);

        JLabel usersubscriber = new JLabel(PublicValues.language.translate("ui.lastfm.user.subscriber"));
        usersubscriber.setBounds(6, 344, 130, 16);
        add(usersubscriber);
        usersubscriber.setHorizontalAlignment(SwingConstants.RIGHT);

        JLabel userplaycount = new JLabel(PublicValues.language.translate("ui.lastfm.user.playcount"));
        userplaycount.setBounds(6, 372, 130, 16);
        add(userplaycount);
        userplaycount.setHorizontalAlignment(SwingConstants.RIGHT);

        JLabel userplaylists = new JLabel(PublicValues.language.translate("ui.lastfm.user.playlists"));
        userplaylists.setBounds(6, 400, 130, 16);
        add(userplaylists);
        userplaylists.setHorizontalAlignment(SwingConstants.RIGHT);

        JLabel userregistered = new JLabel(PublicValues.language.translate("ui.lastfm.user.registered"));
        userregistered.setBounds(6, 428, 130, 16);
        add(userregistered);
        userregistered.setHorizontalAlignment(SwingConstants.RIGHT);

        JLabel userartists = new JLabel(PublicValues.language.translate("ui.lastfm.user.artists"));
        userartists.setBounds(6, 456, 130, 16);
        add(userartists);
        userartists.setHorizontalAlignment(SwingConstants.RIGHT);

        JLabel useralbums = new JLabel(PublicValues.language.translate("ui.lastfm.user.albums"));
        useralbums.setBounds(6, 484, 130, 16);
        add(useralbums);
        useralbums.setHorizontalAlignment(SwingConstants.RIGHT);

        JLabel usertracks = new JLabel(PublicValues.language.translate("ui.lastfm.user.tracks"));
        usertracks.setBounds(6, 512, 130, 16);
        add(usertracks);
        usertracks.setHorizontalAlignment(SwingConstants.RIGHT);

        JLabel userusernamevalue = new JLabel("New label");
        userusernamevalue.setBounds(168, 204, 176, 16);
        add(userusernamevalue);
        userusernamevalue.setText(user.getName());

        JLabel userrealnamevalue = new JLabel("New label");
        userrealnamevalue.setBounds(168, 232, 176, 16);
        add(userrealnamevalue);
        userrealnamevalue.setText(user.getRealname());

        JLabel usercountryvalue = new JLabel("New label");
        usercountryvalue.setBounds(168, 260, 176, 16);
        add(usercountryvalue);
        usercountryvalue.setText(user.getCountry());

        JLabel useragevalue = new JLabel("New label");
        useragevalue.setBounds(168, 288, 176, 16);
        add(useragevalue);
        useragevalue.setText(String.valueOf(user.getAge()));

        JLabel usergendervalue = new JLabel("New label");
        usergendervalue.setBounds(168, 316, 176, 16);
        add(usergendervalue);
        usergendervalue.setText(user.getGender());

        JLabel usersubscribervalue = new JLabel("New label");
        usersubscribervalue.setBounds(168, 344, 176, 16);
        add(usersubscribervalue);
        usersubscribervalue.setText(String.valueOf(user.isSubscriber()));

        JLabel userplaycountvalue = new JLabel("New label");
        userplaycountvalue.setBounds(168, 372, 176, 16);
        add(userplaycountvalue);
        userplaycountvalue.setText(String.valueOf(user.getPlaycount()));

        JLabel userplaylistsvalue = new JLabel("New label");
        userplaylistsvalue.setBounds(168, 400, 176, 16);
        add(userplaylistsvalue);
        userplaylistsvalue.setText(String.valueOf(user.getNumPlaylists()));

        JLabel userregisteredvalue = new JLabel("New label");
        userregisteredvalue.setBounds(168, 428, 176, 16);
        add(userregisteredvalue);
        userregisteredvalue.setText(LastFMDialog.formatDate(user.getRegisteredDate()));

        JLabel userartistsvalue = new JLabel("New label");
        userartistsvalue.setBounds(168, 456, 176, 16);
        add(userartistsvalue);
        userartistsvalue.setText(user.getArtists());

        JLabel useralbumsvalue = new JLabel("New label");
        useralbumsvalue.setBounds(168, 484, 176, 16);
        add(useralbumsvalue);
        useralbumsvalue.setText(user.getAlbums());

        JLabel usertracksvalue = new JLabel("New label");
        usertracksvalue.setBounds(168, 512, 176, 16);
        add(usertracksvalue);
        usertracksvalue.setText(user.getTracks());
    }
}
