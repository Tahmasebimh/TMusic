package com.example.hossein.tmusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.hossein.tmusic.view.AlbumDetailFragment;
import com.example.hossein.tmusic.view.ArtistDetailFragment;

public class AlbumArtistDetailActivity extends AppCompatActivity {

    private static final String KEY_ALBUM_ARTIST_ID = "albumid";
    private static final String KEY_KIND_DETAIL = "intentkind";
    private FrameLayout mFrameLayout;

    public static final int DETAIL_ALBUM = 0;
    public static final int DETAIL_ARTIST = 1;

    public static Intent newIntent(Long albumArtistId , Context context , int kind){
        Intent intent = new Intent(context , AlbumArtistDetailActivity.class);
        intent.putExtra(KEY_ALBUM_ARTIST_ID, albumArtistId);
        intent.putExtra(KEY_KIND_DETAIL , kind);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);
        mFrameLayout = findViewById(R.id.frm_album_detail);
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (getIntent().hasExtra(KEY_ALBUM_ARTIST_ID)  && getIntent().hasExtra(KEY_KIND_DETAIL)){
            if (getIntent().getExtras().getInt(KEY_KIND_DETAIL) == DETAIL_ALBUM) {
                fragmentManager.beginTransaction().replace(R.id.frm_album_detail
                        , AlbumDetailFragment.newInstance(getIntent().getExtras().getLong(KEY_ALBUM_ARTIST_ID)))
                        .commit();
            }else{
                fragmentManager.beginTransaction()
                        .replace(R.id.frm_album_detail
                                , ArtistDetailFragment.newInstance(getIntent().getExtras().getLong(KEY_ALBUM_ARTIST_ID)))
                        .commit();
            }
        }
    }
}
