package com.example.hossein.tmusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class PlayMusicActivity extends AppCompatActivity {

    public static final int ALL_MUSIC_KIND = 0;
    public static final int ARTIST_MUSIC_KIND = 1;
    public static final int ALBUM_MUSIC_KIND = 2;

    private static final String KEY_SONG_ID = "songid";
    private static final String KEY_ALBUM_ID = "albumid";
    private static final String KEY_ARTIST_ID = "artistid";
    private static final String KEY_WHAT_KIND = "musickind";
    private static final String KEY_SONG_POSITION = "songPosition";

    public static Intent newIntent(Context context ,int whatKind , long songId , long artistId , long albumId , int position){

        Intent intent = new Intent(context , PlayMusicActivity.class);
        intent.putExtra(KEY_SONG_ID , songId);
        intent.putExtra(KEY_ALBUM_ID , albumId);
        intent.putExtra(KEY_ARTIST_ID , artistId);
        intent.putExtra(KEY_WHAT_KIND , whatKind);
        intent.putExtra(KEY_SONG_POSITION , position);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            finish();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        int kindAlbum = bundle.getInt(KEY_WHAT_KIND);
        long songId = bundle.getLong(KEY_SONG_ID);
        long albumId = bundle.getLong(KEY_ALBUM_ID);
        long artistId = bundle.getLong(KEY_ARTIST_ID);
        int position = bundle.getInt(KEY_SONG_POSITION);

        fragmentManager.beginTransaction().replace(R.id.frm_layout_root_play_music ,
                PlayMusicFragment.newInstance(kindAlbum, songId ,artistId , albumId , position )).commit();
//        switch (kindAlbum){
//            case ALL_MUSIC_KIND :{
//
//                break;
//            }case ARTIST_MUSIC_KIND :{
//                fragmentManager.beginTransaction().replace(R.id.frm_layout_root_play_music ,
//                        PlayMusicFragment.newInstance(ARTIST_MUSIC_KIND, songId ,artistId , albumId , position )).commit();
//                break;
//            }case ALBUM_MUSIC_KIND :{
//                fragmentManager.beginTransaction().replace(R.id.frm_layout_root_play_music ,
//                        PlayMusicFragment.newInstance(ALBUM_MUSIC_KIND, songId ,artistId , albumId , position )).commit();
//                break;
//            }default:
//                finish();
//        }
    }
}
