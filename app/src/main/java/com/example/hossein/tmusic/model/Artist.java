package com.example.hossein.tmusic.model;

import android.net.Uri;

import java.security.acl.LastOwnerException;
import java.util.ArrayList;

public class Artist {

    private ArrayList<Song> artistSongList;
    private Uri uriSongCover;
    private String artsitName;
    private Long artistId;

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

    public Long getArtistId() {
        return artistId;
    }

    public void setArtistId(Long artistId) {
        this.artistId = artistId;
    }
}
