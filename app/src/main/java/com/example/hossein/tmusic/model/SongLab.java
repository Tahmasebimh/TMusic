package com.example.hossein.tmusic.model;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;


public class SongLab {

    private static final SongLab ourInstance = new SongLab();
    public static SongLab getInstance() {

        return ourInstance;
    }

    private SongLab() {
    }

    public ArrayList<Artist> getArtistList(Context context) {
        ArrayList<Artist> artistArrayList = new ArrayList<>();
        ContentResolver contentResolver =  context.getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String[] projection = new String[]{MediaStore.Audio.Media.ARTIST , MediaStore.Audio.Media.ALBUM_ID};

        Cursor cursor = contentResolver.query(songUri , projection , null , null  ,null );
        if (cursor != null && cursor.moveToFirst()){

            int artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songAlbumID = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            do {
                Artist artist = new Artist();
                artist.setArtsitName(cursor.getString(artistColumn));
                artist.setUriSongCover(setArtistCover(cursor.getLong(songAlbumID)));
                artistArrayList.add(artist);
            }while (cursor.moveToNext());
        }
        return artistArrayList;
    }

    private Uri setArtistCover(Long anInt) {
        Uri uri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), anInt);
        return uri;
    }
}
