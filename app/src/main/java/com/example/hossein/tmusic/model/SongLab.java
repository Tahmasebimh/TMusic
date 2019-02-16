package com.example.hossein.tmusic.model;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;



public class SongLab {

    private static final String TAG_CATCH_ERROR = "<><>listerror<><>";
    private static final String TAG_CURSOR = "<><>cursor<><>" ;
    private static final String TAG_SONG_LIST = "<<<songlistsize>>>";
    private Cursor mCursor;
    private ArrayList<Song> mSongArrayList;
    private ArrayList<Artist> mArtistArrayList;
    private ArrayList<Song> mAlbumSongList;
    private ArrayList<Song> mArtistSongs;
    private static final SongLab ourInstance = new SongLab();
    public static SongLab getInstance() {
        return ourInstance;
    }

    private SongLab() {
    }

    private Uri setArtistCover(Long anInt) {
        return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), anInt);
    }

    public ArrayList<Song> getSongList(Activity activity) {
        mSongArrayList = new ArrayList<>();
        mArtistArrayList = new ArrayList<>();
        try {
            ContentResolver contentResolver =  activity.getContentResolver();
            Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            mCursor = contentResolver.query(songUri , null , null , null  ,null  );

            if(mCursor != null && mCursor.moveToFirst()) {
                int songId = mCursor.getColumnIndex(MediaStore.Audio.Media._ID);
                int songTitle = mCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
                int songArtist = mCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
                int songAlbum = mCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
                int songAlbumID = mCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
                int songArtistId = mCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID);
                int songPathColumnIndex = mCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
                do {
                    Song song = new Song();
                    song.setId(mCursor.getLong(songId));
                    song.setTitle(mCursor.getString(songTitle));
                    song.setArtist(mCursor.getString(songArtist));
                    song.setAlbumName(mCursor.getString(songAlbum));
                    song.setUriAlbumPhoto(setSongCover(mCursor.getLong(songAlbumID)));
                    song.setAlbumId(mCursor.getLong(songAlbumID));
                    song.setArtistId(mCursor.getLong(songArtistId));
                    song.setPath(mCursor.getString(songPathColumnIndex));
                    mSongArrayList.add(song);
                } while (mCursor.moveToNext());
                generateArtistList();
                return mSongArrayList;
            }
        }catch (Exception e){
            Log.e(TAG_CATCH_ERROR , "list error");
        }finally {
            mCursor.close();
        }
        return mSongArrayList;
    }

    public ArrayList<Song> getAllSongsList(Activity activity){
        if(mSongArrayList.size() > 0 ){
            return mSongArrayList;
        }else{
            getSongList(activity);
            return mSongArrayList;
        }
    }

    private void generateArtistList() {

        try {
            if (mCursor != null && mCursor.moveToFirst()){

                int artistColumn = mCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
                int songAlbumID = mCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
                int artistID = mCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID);
                do {
                    Artist artist = new Artist();
                    artist.setArtsitName(mCursor.getString(artistColumn));
                    artist.setUriSongCover(setArtistCover(mCursor.getLong(songAlbumID)));
                    artist.setArtistId(mCursor.getLong(artistID));
                    if(!repeatedInArtistList(artist)){
                        mArtistArrayList.add(artist);
                    }
                }while (mCursor.moveToNext());
            }
        }catch (Exception e){
            Log.e(TAG_CATCH_ERROR , "artist List error");
        }

    }

    private boolean repeatedInArtistList(Artist artist){

        for (Artist artist1 : mArtistArrayList){
            if(artist1.getArtistId().equals(artist.getArtistId())){
                return true;
            }
        }
        return false;
    }

    private Uri setSongCover(long anInt) {
        Uri uri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), anInt);
        return uri;
    }

    public ArrayList<Artist> getArtistList(Activity activity) {
        getSongList(activity);
        Log.d("<><><>" , mArtistArrayList.size() + "");
        return mArtistArrayList;
    }

    public ArrayList<Song> getAlbumSongList (Long albumId){

        mAlbumSongList = new ArrayList<>();
        Log.d(TAG_SONG_LIST , albumId.toString());
        for (Song song : mSongArrayList){
            Log.d(TAG_SONG_LIST , song.getAlbumId().toString() + "song album id");
            if (song.getAlbumId().equals(albumId)){
                mAlbumSongList.add(song);
            }
        }
        Log.d(TAG_SONG_LIST , mAlbumSongList.size() + "");
        return mAlbumSongList;
    }

    public ArrayList<Song> getArtistSong(Long artistId){
        mArtistSongs = new ArrayList<>();
        for (Song song : mSongArrayList){
            if (song.getArtistId().equals(artistId)){
                mArtistSongs.add(song);
            }
        }
        return mArtistSongs;
    }
    public Uri getLastArtistCoverUri(Long artistId){
        ArrayList<Song> songs = getArtistSong(artistId);
        return songs.get(songs.size() - 1).getUriAlbumPhoto();
    }

    public int getArtistSongsCount(Long artistId){
        ArrayList<Song> songs = getArtistSong(artistId);
        return songs.size();
    }

    public int getAlbumSongsCount(long albumId){
        int counter = 0;
        for (Song song : mSongArrayList){
            if(song.getAlbumId().equals(albumId)){
                counter++;
            }
        }
        return counter;
    }
}
