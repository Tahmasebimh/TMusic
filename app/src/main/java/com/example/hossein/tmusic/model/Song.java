package com.example.hossein.tmusic.model;

import android.net.Uri;

public class Song {
    private int id;
    private String title;
    private String artist;
    private String  albumName;
    private Uri mUriAlbumPhoto;
    private Long albumId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public Uri getUriAlbumPhoto() {
        return mUriAlbumPhoto;
    }

    public void setUriAlbumPhoto(Uri uriAlbumPhoto) {
        mUriAlbumPhoto = uriAlbumPhoto;
    }

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }
}
