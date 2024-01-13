package com.spotifyxp.lastfm;

import com.spotifyxp.PublicValues;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.deps.de.umass.lastfm.Authenticator;
import com.spotifyxp.deps.de.umass.lastfm.ImageSize;
import com.spotifyxp.deps.de.umass.lastfm.User;
import com.spotifyxp.swingextension.JFrame;
import com.spotifyxp.swingextension.JImagePanel;
import com.spotifyxp.threading.DefThread;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class LastFMUserDialog extends JFrame {
    public static JImagePanel userimage;
    public static JLabel userusername;
    public static JLabel userrealname;
    public static JLabel usercountry;
    public static JLabel userage;
    public static JLabel usergender;
    public static JLabel usersubscriber;
    public static JLabel userplaylists;
    public static JLabel userregistered;
    public static JLabel userartists;
    public static JLabel useralbums;
    public static JLabel usertracks;
    public static JLabel userplaycount;
    public static JLabel userusernamevalue;
    public static JLabel userrealnamevalue;
    public static JLabel usercountryvalue;
    public static JLabel useragevalue;
    public static JLabel usergendervalue;
    public static JLabel usersubscribervalue;
    public static JLabel userplaylistsvalue;
    public static JLabel userregisteredvalue;
    public static JLabel userartistsvalue;
    public static JLabel useralbumsvalue;
    public static JLabel usertracksvalue;
    public static JLabel userplaycountvalue;
    
    public LastFMUserDialog() {
        setPreferredSize(new Dimension(350, 600));
        setLayout(null);
        userimage = new JImagePanel();
        userimage.setBounds(80, 6, 180, 180);
        add(userimage);

        setTitle("Last.fm - " + LFMValues.username);

        userusername = new JLabel(PublicValues.language.translate("ui.lastfm.user.username"));
        userusername.setBounds(6, 204, 130, 16);
        add(userusername);
        userusername.setHorizontalAlignment(SwingConstants.RIGHT);

        userrealname = new JLabel(PublicValues.language.translate("ui.lastfm.user.realname"));
        userrealname.setBounds(6, 232, 130, 16);
        add(userrealname);
        userrealname.setHorizontalAlignment(SwingConstants.RIGHT);

        usercountry = new JLabel(PublicValues.language.translate("ui.lastfm.user.country"));
        usercountry.setBounds(6, 260, 130, 16);
        add(usercountry);
        usercountry.setHorizontalAlignment(SwingConstants.RIGHT);

        userage = new JLabel(PublicValues.language.translate("ui.lastfm.user.age"));
        userage.setBounds(6, 288, 130, 16);
        add(userage);
        userage.setHorizontalAlignment(SwingConstants.RIGHT);

        usergender = new JLabel(PublicValues.language.translate("ui.lastfm.user.gender"));
        usergender.setBounds(6, 316, 130, 16);
        add(usergender);
        usergender.setHorizontalAlignment(SwingConstants.RIGHT);

        usersubscriber = new JLabel(PublicValues.language.translate("ui.lastfm.user.subscriber"));
        usersubscriber.setBounds(6, 344, 130, 16);
        add(usersubscriber);
        usersubscriber.setHorizontalAlignment(SwingConstants.RIGHT);

        userplaycount = new JLabel(PublicValues.language.translate("ui.lastfm.user.playcount"));
        userplaycount.setBounds(6, 372, 130, 16);
        add(userplaycount);
        userplaycount.setHorizontalAlignment(SwingConstants.RIGHT);

        userplaylists = new JLabel(PublicValues.language.translate("ui.lastfm.user.playlists"));
        userplaylists.setBounds(6, 400, 130, 16);
        add(userplaylists);
        userplaylists.setHorizontalAlignment(SwingConstants.RIGHT);

        userregistered = new JLabel(PublicValues.language.translate("ui.lastfm.user.registered"));
        userregistered.setBounds(6, 428, 130, 16);
        add(userregistered);
        userregistered.setHorizontalAlignment(SwingConstants.RIGHT);

        userartists = new JLabel(PublicValues.language.translate("ui.lastfm.user.artists"));
        userartists.setBounds(6, 456, 130, 16);
        add(userartists);
        userartists.setHorizontalAlignment(SwingConstants.RIGHT);

        useralbums = new JLabel(PublicValues.language.translate("ui.lastfm.user.albums"));
        useralbums.setBounds(6, 484, 130, 16);
        add(useralbums);
        useralbums.setHorizontalAlignment(SwingConstants.RIGHT);

        usertracks = new JLabel(PublicValues.language.translate("ui.lastfm.user.tracks"));
        usertracks.setBounds(6, 512, 130, 16);
        add(usertracks);
        usertracks.setHorizontalAlignment(SwingConstants.RIGHT);

        userusernamevalue = new JLabel("N/A");
        userusernamevalue.setBounds(168, 204, 176, 16);
        add(userusernamevalue);

        userrealnamevalue = new JLabel("N/A");
        userrealnamevalue.setBounds(168, 232, 176, 16);
        add(userrealnamevalue);

        usercountryvalue = new JLabel("N/A");
        usercountryvalue.setBounds(168, 260, 176, 16);
        add(usercountryvalue);

        useragevalue = new JLabel("N/A");
        useragevalue.setBounds(168, 288, 176, 16);
        add(useragevalue);

        usergendervalue = new JLabel("N/A");
        usergendervalue.setBounds(168, 316, 176, 16);
        add(usergendervalue);

        usersubscribervalue = new JLabel("N/A");
        usersubscribervalue.setBounds(168, 344, 176, 16);
        add(usersubscribervalue);

        userplaycountvalue = new JLabel("N/A");
        userplaycountvalue.setBounds(168, 372, 176, 16);
        add(userplaycountvalue);

        userplaylistsvalue = new JLabel("N/A");
        userplaylistsvalue.setBounds(168, 400, 176, 16);
        add(userplaylistsvalue);

        userregisteredvalue = new JLabel("N/A");
        userregisteredvalue.setBounds(168, 428, 176, 16);
        add(userregisteredvalue);

        userartistsvalue = new JLabel("N/A");
        userartistsvalue.setBounds(168, 456, 176, 16);
        add(userartistsvalue);

        useralbumsvalue = new JLabel("N/A");
        useralbumsvalue.setBounds(168, 484, 176, 16);
        add(useralbumsvalue);

        usertracksvalue = new JLabel("N/A");
        usertracksvalue.setBounds(168, 512, 176, 16);
        add(usertracksvalue);

        DefThread thread = new DefThread(new Runnable() {
            @Override
            public void run() {
                loadValues();
            }
        });
        thread.start();
    }
    
    void loadValues() {
        User user = User.getInfo(Authenticator.getMobileSession(LFMValues.username, PublicValues.config.getString(ConfigValues.lastfmpassword.name), LFMValues.apikey, LFMValues.apisecret));
        try {
            userimage.setImage(new URL(user.getImageURL(ImageSize.LARGE)).openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        userusernamevalue.setText(user.getName());
        userrealname.setText(user.getRealname());
        usercountryvalue.setText(user.getCountry());
        useragevalue.setText(String.valueOf(user.getAge()));
        usergendervalue.setText(user.getGender());
        usersubscribervalue.setText(user.isSubscriber() ? PublicValues.language.translate("generic.yes") : PublicValues.language.translate("generic.no"));
        userplaycountvalue.setText(String.valueOf(user.getPlaycount()));
        userplaylistsvalue.setText(String.valueOf(user.getNumPlaylists()));
        userregisteredvalue.setText(user.getRegisteredDate().toString());
        userartistsvalue.setText(user.getArtists());
        useralbumsvalue.setText(user.getAlbums());
        usertracksvalue.setText(user.getTracks());
    }
}
