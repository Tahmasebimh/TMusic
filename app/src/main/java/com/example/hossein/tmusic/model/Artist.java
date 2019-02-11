package com.example.hossein.tmusic.model;

import android.net.Uri;

import java.util.ArrayList;

public class Artist {

    private ArrayList<Song> artistSongList;
    private Uri uriSongCover;
    private String artsitName;

    public ArrayList<Song> getArtistSongList() {
        return artistSongList;
    }

    public void setArtistSongList(ArrayList<Song> artistSongList) {
        this.artistSongList = artistSongList;
    }

    public Uri getUriSongCover() {
        return uriSongCover;
    }

    public void setUriSongCover(Uri uriSongCover) {
        this.uriSongCover = uriSongCover;
    }

    public String getArtsitName() {
        return artsitName;
    }

    public void setArtsitName(String artsitName) {
        this.artsitName = artsitName;
    }
}
