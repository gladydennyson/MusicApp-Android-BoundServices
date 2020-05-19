package com.example.musiccentral;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public final class SongInfo implements Parcelable {

    String name;
    String artist;
    String songURL;
    Bitmap image;

    public SongInfo(String name, String artist, String songURL, Bitmap image) {
        this.name = name;
        this.artist = artist;
        this.songURL = songURL;
        this.image = image;
    }

    public static final Parcelable.Creator<SongInfo> CREATOR = new Parcelable.Creator<SongInfo>() {
        public SongInfo createFromParcel(Parcel in) {
            return new SongInfo(in);
        }

        public SongInfo[] newArray(int size) {
            return new SongInfo[size];
        }
    };



    private SongInfo(Parcel in) {
        readFromParcel(in);
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeString(artist);
        out.writeString(songURL);
        out.writeParcelable(image,flags);
    }

    public void readFromParcel(Parcel in) {
        name = in.readString();
        artist = in.readString();
        songURL = in.readString();
        image = in.readParcelable(Bitmap.class.getClassLoader());

    }

    public int describeContents() {
        return 0;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public String getSongURL() {
        return songURL;
    }

    public Bitmap getImage() {
        return image;
    }
}
