package com.example.hossein.tmusic.model;

import android.net.Uri;

public class Album {

    private Long albumID;
    private String AlbumTitle;
    private String AlbumArtsit;
    private Uri AlbumCoverUri;


    public Long getAlbumID() {
        return albumID;
    }

    public void setAlbumID(Long albumID) {
        this.albumID = albumID;
    }

    public String getAlbumTitle() {
        return AlbumTitle;
    }

    public void setAlbumTitle(String albumTitle) {
        AlbumTitle = albumTitle;
    }

    public String getAlbumArtsit() {
        return AlbumArtsit;
    }

    public void setAlbumArtsit(String albumArtsit) {
        AlbumArtsit = albumArtsit;
    }

    public Uri getAlbumCoverUri() {
        return AlbumCoverUri;
    }

    public void setAlbumCoverUri(Uri albumCoverUri) {
        AlbumCoverUri = albumCoverUri;
    }


}
