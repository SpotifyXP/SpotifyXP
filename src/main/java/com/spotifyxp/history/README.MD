## Playback history

- File format




### The file format

Example:

<pre>
SongURI::SongName::ArtistName::ArtistURI::AlbumName::AlbumURI::SongLength
SongURI::Song2Name::ArtistName::ArtistURI::AlbumName::AlbumURI::SongLength
</pre>

<h5>The file consists of 7 variables: The song name, song uri, artist name, artist uri, album name, album uri and song length.<br>

The parser reads 15 songs and stops because this file can be very large (>1Gb)