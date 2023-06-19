package com.spotifyxp.events;

import javax.swing.*;

public interface HtmlDialogEvents {
    void unsupportedHTMLTag(String tagname);
    void close(JDialog dialog);
    void open(JDialog dialog);
    void resize(JDialog dialog);
}
