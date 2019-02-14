package com.example.hossein.tmusic.model;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;

public class AlbumLab {


    private static final AlbumLab ourInstance = new AlbumLab();
    private static final String TAG_ALBUM_LAB_ERROR = "<<<<albumlaberror>>>";
    private Cursor mCursor;


    public static AlbumLab getInstance() {
        return ourInstance;
    }

    private AlbumLab() {
    }

    private ArrayList<Album> mAlbumArrayList;

    public ArrayList<Album> getAlbumList(Activity activity){
        mAlbumArrayList = new ArrayList<>();
        try {
            ContentResolver contentResolver = activity.getContentResolver();
            Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
            mCursor = contentResolver.query(uri , null , null , null ,null);
            if(mCursor != null && mCursor.moveToFirst()){
                int albumTitleColumnIndex = mCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM);
                int albumArtistColumnIndex = mCursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST);
                int albumIdcolumnIndex = mCursor.getColumnIndex(MediaStore.Audio.Albums._ID);

                do {
                    Album album = new Album();
                    album.setAlbumID(mCursor.getLong(albumIdcolumnIndex));
                    album.setAlbumTitle(mCursor.getString(albumTitleColumnIndex));
                    album.setAlbumArtsit(mCursor.getString(albumArtistColumnIndex));
                    album.setAlbumCoverUri(setSongCover(mCursor.getLong(albumIdcolumnIndex)));

                   // if(!isRepeatedInAlbumList(album)){
                        mAlbumArrayList.add(album);
                    //}

                }while (mCursor.moveToNext());
                return mAlbumArrayList;
            }
        }catch (Exception e){
            Log.e(TAG_ALBUM_LAB_ERROR , e.getMessage());
        }
        return mAlbumArrayList;
    }

    private boolean isRepeatedInAlbumList(Album album) {
        for (Album album1 : mAlbumArrayList){
            if(album.getAlbumID().equals(album1.getAlbumID())){
                Log.d("<<<aaaaaaa" , "rewpasdfadf");
                return true;
            }
        }
        return false;
    }

    private Uri setSongCover(long anInt) {
        Uri uri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), anInt);
        return uri;
    }
}
