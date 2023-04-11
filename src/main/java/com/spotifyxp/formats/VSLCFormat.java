package com.spotifyxp.formats;


import com.spotifyxp.utils.EncryptorAesGcmPassword;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.util.Base64;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class VSLCFormat {
    // ############################################
    // #  VSLC = Vorbis Spotify Legacy Container  #
    // ############################################
    // This file format avoids piracy from Spotify music downloaded with SpotifyXP
    //
    // The file format consists of two elements
    //
    // 1. The encrypted music data
    // 2. The Song info in form of JSON
    public static class InvalidVSLCFile extends Exception {
        public enum ExceptionTypes {
            NONE(""),
            EXTENSION("Wrong file extension"),
            READ("Can't read file"),
            NO_DATA("No Song Data and Song Info available"),
            NO_SONG_DATA("No Song Data available"),
            NO_SONG_INFO("No Song Info available"),
            JSON_NO_DATA("Invalid JSON! No data found"),
            JSON_NO_SONG_IMAGE("Invalid JSON! No song info found except an image"),
            JSON("Invalid JSON!"),
            NO_JSON("No JSON found"),
            NO_AUDIO("No Audio Data"),
            DECRYPTION("Wrong decryption key");
            private final String selected;
            ExceptionTypes(String text) {
                selected = text;
            }
            public String getText() {
                return selected;
            }
        }
        String filename;
        public ExceptionTypes exceptionType = ExceptionTypes.NONE;
        public InvalidVSLCFile(String f, ExceptionTypes type) {
            exceptionType = type;
            filename = f;
        }
        public InvalidVSLCFile(String f) {
            filename = f;
        }
        @Override
        public String getMessage() {
            if(exceptionType.getText().equals("")) {
                return "'" + filename + "' is not a valid vslc file!";
            }else{
                return "'" + filename + "' is not a valid vslc file! Additional Information: " + exceptionType.getText();
            }
        }
        @Override
        public String getLocalizedMessage() {
            if(exceptionType.getText().equals("")) {
                return "'" + filename + "' is not a valid vslc file!";
            }else{
                return "'" + filename + "' is not a valid vslc file! Additional Information: " + exceptionType.getText();
            }
        }
        @Override
        public StackTraceElement[] getStackTrace() {
            return new StackTraceElement[] {}; //Not supported but dont't throw an other exception here
        }
    }
    public static class VSLCSongInfo {
        public String title = "";
        public String artist = "";
        public String imageStream;
        public String version = "1.0";
    }
    public static class VSLCFormatDescription {
        public VSLCSongInfo songInfo;
        public byte[] encryptedAudioStream;
    }
    File file;
    String readCache;
    public VSLCFormat(@NotNull String path, @NotNull String filename) throws InvalidVSLCFile {
        if(!filename.toLowerCase().endsWith(".vslc")) {
            throw new InvalidVSLCFile(filename, InvalidVSLCFile.ExceptionTypes.EXTENSION);
        }
        file = new File(path, filename);
    }
    public VSLCFormat(@NotNull File f) throws InvalidVSLCFile {
        if(!f.getName().toLowerCase().endsWith(".vslc")) {
            throw new InvalidVSLCFile(file.getName(), InvalidVSLCFile.ExceptionTypes.EXTENSION);
        }
        file = f;
    }

    public void read() throws InvalidVSLCFile {
        try {
            readCache = FileUtils.readFileToString(file, Charset.defaultCharset());
        } catch (IOException e) {
            throw new InvalidVSLCFile(file.getName(), InvalidVSLCFile.ExceptionTypes.READ);
        }
    }

    boolean wroteSongInfo = false;
    boolean wroteSongData = false;
    VSLCSongInfo songInfo = new VSLCSongInfo();
    VSLCFormatDescription formatDescription = new VSLCFormatDescription();


    public void write() throws InvalidVSLCFile, IOException {
        if(!wroteSongData & !wroteSongInfo) {
            throw new InvalidVSLCFile(file.getName(), InvalidVSLCFile.ExceptionTypes.NO_DATA);
        }
        if(!wroteSongData) {
            throw new InvalidVSLCFile(file.getName(), InvalidVSLCFile.ExceptionTypes.NO_SONG_DATA);
        }
        if(!wroteSongInfo) {
            throw new InvalidVSLCFile(file.getName(), InvalidVSLCFile.ExceptionTypes.NO_SONG_INFO);
        }
        //1. Write Song Data
        byte[] encryptedSongData = formatDescription.encryptedAudioStream;
        //2. Write Song Info
        JSONObject songData = new JSONObject();
        JSONObject songInfoHolder = new JSONObject();
        songInfoHolder.append("title", songInfo.title);
        songInfoHolder.append("artist", songInfo.artist);
        songInfoHolder.append("image", songInfo.imageStream);
        songInfoHolder.append("version", songInfo.version);
        songData.append("filedescription", songInfoHolder);
        //3. Write all of that into the file
        FileUtils.writeByteArrayToFile(file, encryptedSongData);
        com.spotifyxp.utils.FileUtils.appendToFile(file.getAbsolutePath(), "\n" + songData);
    }

    public void writeSongInfo(@NotNull String title, @NotNull String artist, InputStream coverImage) throws IOException {
        try {
            songInfo.artist = artist;
            songInfo.title = title;
            if(!(coverImage ==null)) {
                songInfo.imageStream = Base64.encodeBase64String(IOUtils.toByteArray(coverImage));
            }
            wroteSongInfo = true;
        }catch (IOException e) {
            throw new IOException(e);
        }
    }

    public void writeSongData(@NotNull String encryptionKey, InputStream audioStream) throws Exception {
        try {
            formatDescription.encryptedAudioStream = EncryptorAesGcmPassword.encryptByte(IOUtils.toByteArray(audioStream), encryptionKey);
            wroteSongData = true;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public VSLCSongInfo getSongInfo() throws InvalidVSLCFile {
        boolean foundTitle = false;
        boolean foundArtist = false;
        boolean foundImage = false;
        VSLCSongInfo info = new VSLCSongInfo();
        try {
            JSONObject object = new JSONObject(new JSONObject(readCache.split("\n")[1]).getJSONArray("filedescription").get(0).toString());
            try {
                info.artist = object.getJSONArray("artist").get(0).toString();
                foundTitle = true;
            }catch (JSONException e) {
                //No artist found
            }
            try {
                info.title = object.getJSONArray("title").get(0).toString();
                foundArtist = true;
            }catch (JSONException e) {
                //No title found
            }
            try {
                if(!object.get("image").toString().equals("[null]")) {
                    info.imageStream = object.getJSONArray("image").get(0).toString();
                    foundImage = true;
                }
            }catch (JSONException e) {
                //No image found
            }
            if(!foundArtist & !foundImage & !foundTitle) {
                throw new InvalidVSLCFile(file.getName(), InvalidVSLCFile.ExceptionTypes.JSON_NO_DATA);
            }
            if(!foundArtist & !foundTitle) {
                throw new InvalidVSLCFile(file.getName(), InvalidVSLCFile.ExceptionTypes.JSON_NO_SONG_IMAGE);
            }
        }catch (JSONException e) {
            throw new InvalidVSLCFile(file.getName(), InvalidVSLCFile.ExceptionTypes.JSON);
        }catch (ArrayIndexOutOfBoundsException e) {
            throw new InvalidVSLCFile(file.getName(), InvalidVSLCFile.ExceptionTypes.NO_JSON);
        }
        return info;
    }

    public InputStream getAudioStream(@NotNull String decriptionKey) throws InvalidVSLCFile {
        InputStream stream;
        try {
            stream = new ByteArrayInputStream(EncryptorAesGcmPassword.decryptByte(readCache.split("\n")[0].getBytes(), decriptionKey));
        }catch (ArrayIndexOutOfBoundsException e) {
            throw new InvalidVSLCFile(file.getName(), InvalidVSLCFile.ExceptionTypes.NO_AUDIO);
        } catch (Exception e) {
            throw new InvalidVSLCFile(file.getName(), InvalidVSLCFile.ExceptionTypes.DECRYPTION);
        }
        return stream;
    }
}
