package com.example.hossein.tmusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.example.hossein.tmusic.view.AlbumDetailFragment;

public class AlbumDetailActivity extends AppCompatActivity {

    private static final String KEY_ALBUM_ID = "albumid";
    private static final String TAG_ALBUM_DETAIL = "<<<albumdetail>>>";
    private FrameLayout mFrameLayout;

    public static Intent newIntent(Long albumId , Context context){
        Intent intent = new Intent(context , AlbumDetailActivity.class);
        intent.putExtra(KEY_ALBUM_ID , albumId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);

        if (getIntent().hasExtra(KEY_ALBUM_ID)){
            Log.d(TAG_ALBUM_DETAIL , "is here");
            mFrameLayout = findViewById(R.id.frm_album_detail);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frm_album_detail
                    , AlbumDetailFragment.newInstance(getIntent().getExtras().getLong(KEY_ALBUM_ID)))
            .commit();
        }
    }
}
