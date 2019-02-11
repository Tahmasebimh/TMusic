package com.example.hossein.tmusic.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.example.hossein.tmusic.R;
import com.google.android.material.tabs.TabLayout;

public class MusicListPagerActivity extends AppCompatActivity implements AllMusicListFragment.OnFragmentInteractionListener
 , ArtistListFragment.OnFragmentInteractionListener{

    private static final int REQ_CODE_READ_EXTERNAL = 0 ;
    TabLayout mTabLayout;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(MusicListPagerActivity.this , Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MusicListPagerActivity.this ,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE} ,
                    REQ_CODE_READ_EXTERNAL);
        }

        mViewPager = findViewById(R.id.music_list_view_pager);
        mTabLayout = findViewById(R.id.music_list_tab_layout);

        mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position){
                    case 0 : {
                        return AllMusicListFragment.newInstance();
                    }case 1: {
                        return ArtistListFragment.newInstance();
                    }default:
                        return AllMusicListFragment.newInstance();
                }
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {

                switch (position){
                    case 0 :{
                        return getResources().getString(R.string.tab_all_music);
                    }case 1 :{
                        return getResources().getString(R.string.tab_artist);
                    }case 2 : {
                        return getResources().getString(R.string.tab_album);
                    }default:
                        return super.getPageTitle(position);
                }
            }
        });

        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQ_CODE_READ_EXTERNAL){
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, R.string.permission_blocked , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
