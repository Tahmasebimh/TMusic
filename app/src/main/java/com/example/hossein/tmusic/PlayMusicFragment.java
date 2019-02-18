package com.example.hossein.tmusic;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.AudioRouting;
import android.media.MediaPlayer;
import android.media.TimedMetaData;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hossein.tmusic.model.Song;
import com.example.hossein.tmusic.model.SongLab;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import java.io.IOException;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlayMusicFragment extends Fragment {


    private static final String TAG_PLAY_DEBUG = "<>playMusicList<>";
    private static final String KEY_MEDIA_PLAYER = "mediaplayerkey";
    private static final String KEY_MEDIA_POSITION = "medialistposition";
    private static final String KEY_MEDIA_STATE = "mediastate" ;

    public PlayMusicFragment() {
        // Required empty public constructor
    }

    public static final int ALL_MUSIC_KIND = 0;
    public static final int ARTIST_MUSIC_KIND = 1;
    public static final int ALBUM_MUSIC_KIND = 2;

    private static final String KEY_SONG_ID = "songid";
    private static final String KEY_ALBUM_ID = "albumid";
    private static final String KEY_ARTIST_ID = "artistid";
    private static final String KEY_WHAT_KIND = "musickind";
    private static final String KEY_SONG_POSITION = "position";



    public static PlayMusicFragment newInstance(int whatKind , long songId , long artistId , long albumId , int position) {

        Bundle args = new Bundle();
        args.putInt(KEY_WHAT_KIND , whatKind);
        args.putLong(KEY_SONG_ID , songId);
        args.putLong(KEY_ALBUM_ID , albumId);
        args.putLong(KEY_ARTIST_ID , artistId);
        args.putInt(KEY_SONG_POSITION , position);
        PlayMusicFragment fragment = new PlayMusicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private ArrayList<Song> mSongsToplay;
    private MediaPlayer mMediaPlayer;
    private Handler mHandler;
    private Runnable mRunnable;
    private IndicatorSeekBar mIndicatorSeekBar;
    private MaterialCardView mMaterialCardView;
    private ImageView mImageViewPlayingMusicCover;
    private ImageView mImageViewControll;
    private ImageView mImageViewNext;
    private ImageView mImageViewPre;
    private int curentMusicPosition;
    int position;
    private TextView mTextViewArtistName;
    private TextView mTextViewMusicTitle;
    private TextView mTextViewTimming;
    private TextView mTextViewCurrentTime;
    private Chip mChipShuffle;
    private boolean isShfflePlaying = false;
    private SecureRandom mSecureRandom;
    private int mPreMusicPositionInshuffle;
    private boolean mIsMusicPlaying;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSecureRandom = new SecureRandom();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_play_music, container, false);

        initView(view);

        mSongsToplay = new ArrayList<>();
        mMediaPlayer = new MediaPlayer();
        Bundle bundle = getArguments();
        if(bundle == null){
            getActivity().finish();
        }
        int kindAlbum = bundle.getInt(KEY_WHAT_KIND);
        initMusicList(bundle, kindAlbum);
        initHandler();


        if(savedInstanceState == null) {
            try {
                position = bundle.getInt(KEY_SONG_POSITION);
                playMusic(position);
                mHandler.postDelayed(mRunnable, 0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            curentMusicPosition = savedInstanceState.getInt(KEY_MEDIA_PLAYER) ;
            position = savedInstanceState.getInt(KEY_MEDIA_POSITION);
            mIsMusicPlaying = savedInstanceState.getBoolean(KEY_MEDIA_STATE);
            if(mIsMusicPlaying){
                try {
                    playMusic(position);
                    resumeMusic();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    Log.d(TAG_PLAY_DEBUG , curentMusicPosition +" 1 ");
                    playMusic(position);
                    mMediaPlayer.seekTo(curentMusicPosition);
                    int minute ;
                    int second ;
                    if(curentMusicPosition < 60000){
                        minute = 0;
                        second = curentMusicPosition / 1000;
                    }else{
                        minute = curentMusicPosition / 60000;
                        second = curentMusicPosition / 1000 % 60;
                    }

                    mTextViewCurrentTime.setText(minute + " : " + (second <= 9 ? "0" + second : second ));

                    pauseMusic();
                    Log.d(TAG_PLAY_DEBUG , curentMusicPosition +" 2 ");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mPreMusicPositionInshuffle = position;
                mMediaPlayer.reset();
                goNextMusic();
                mHandler.postDelayed(mRunnable , 0);
            }
        });
        mIndicatorSeekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {

            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                curentMusicPosition = (int) (seekBar.getProgressFloat() / 100 * mMediaPlayer.getDuration());
                mMediaPlayer.seekTo((int) (seekBar.getProgressFloat() / 100 * mMediaPlayer.getDuration()));
            }
        });
        mImageViewControll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaPlayer.isPlaying()){
                    pauseMusic();
                }else{
                    resumeMusic();
                }
            }
        });

        mImageViewNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goNextMusic();
            }
        });

        mImageViewPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPreMusic();
            }
        });

        mChipShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShfflePlaying){
                    isShfflePlaying = false;
                }else{
                    isShfflePlaying = true;
                }
            }
        });

        return view;
    }

    private void goToPreMusic() {
        mIndicatorSeekBar.setProgress(0);
        if (isShfflePlaying && mPreMusicPositionInshuffle != 0){
            try {
                mMediaPlayer.reset();
                playMusic(mPreMusicPositionInshuffle);
                mPreMusicPositionInshuffle = 0;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            if (position == 0) {
                position = mSongsToplay.size() - 1;
                try {
                    playMusic(position);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (position > 0) {
                position--;
                try {
                    playMusic(position);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void goNextMusic() {
        mPreMusicPositionInshuffle = position;
        mIndicatorSeekBar.setProgress(0);
        if (isShfflePlaying){
            position = mSecureRandom.nextInt(mSongsToplay.size()) ;
            try {
                mMediaPlayer.reset();
                playMusic(position);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            if (position < mSongsToplay.size() - 1) {
                position++;
                try {
                    mMediaPlayer.reset();
                    playMusic(position);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (position == mSongsToplay.size() - 1) {
                position = 0;
                try {
                    mMediaPlayer.reset();
                    playMusic(position);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initView(View view) {
        mIndicatorSeekBar = view.findViewById(R.id.music_seek_bar);
        mMaterialCardView = view.findViewById(R.id.music_cover_card_view);
        mImageViewPlayingMusicCover = view.findViewById(R.id.img_playing_music_cover);
        mImageViewControll = view.findViewById(R.id.img_music_controller);
        mImageViewNext = view.findViewById(R.id.img_music_forwrad);
        mImageViewPre = view.findViewById(R.id.img_bck_ward_music);
        mTextViewMusicTitle = view.findViewById(R.id.tv_playin_music_name);
        mTextViewArtistName = view.findViewById(R.id.tv_playing_music_artist);
        mTextViewTimming = view.findViewById(R.id.tv_timming_music);
        mTextViewCurrentTime = view.findViewById(R.id.tv_current_time_music);
        mChipShuffle = view.findViewById(R.id.playing_music_shuffle_chip);
    }

    private void initMusicList(Bundle bundle, int kindAlbum) {
        switch (kindAlbum){
            case ALL_MUSIC_KIND :{
                mSongsToplay = SongLab.getInstance().getAllSongsList(getActivity());
                break;
            }case ARTIST_MUSIC_KIND :{
                mSongsToplay = SongLab.getInstance().getArtistSong(bundle.getLong(KEY_ARTIST_ID));
                break;
            }case ALBUM_MUSIC_KIND :{
                mSongsToplay = SongLab.getInstance().getAlbumSongList(bundle.getLong(KEY_ALBUM_ID));
                break;
            }default:
                getActivity().finish();
        }
    }

    private void setCoverMusic(int position) {
        Picasso.get().load(mSongsToplay.get(position).getUriAlbumPhoto())
                .placeholder(R.drawable.music_deffault_icon)
                .centerCrop()
                .resize(250 , 250)
                .into(mImageViewPlayingMusicCover);
    }

    private void resumeMusic() {
        if (mMediaPlayer.isPlaying()){
            mMediaPlayer.pause();
        }
        Log.d(TAG_PLAY_DEBUG , curentMusicPosition + "");
        mMediaPlayer.seekTo(curentMusicPosition);
        mMediaPlayer.start();
        mHandler.postDelayed(mRunnable , 0);
        mImageViewControll.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_music));
    }

    private void pauseMusic() {
        mMediaPlayer.pause();
        mHandler.removeCallbacks(mRunnable);
        curentMusicPosition = mMediaPlayer.getCurrentPosition();
        mImageViewControll.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_music));
    }

    private void initHandler() {
        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                if(mMediaPlayer != null){
                    int mCurrentPosition = mMediaPlayer.getCurrentPosition();

                    int minute ;
                    int second ;
                    if(mCurrentPosition < 60000){
                        minute = 0;
                        second = mCurrentPosition / 1000;
                    }else{
                        minute = mCurrentPosition / 60000;
                        second = mCurrentPosition / 1000 % 60;
                    }

                    mTextViewCurrentTime.setText(minute + " : " + (second <= 9 ? "0" + second : second ));
                    mIndicatorSeekBar.setProgress((float) mCurrentPosition / mMediaPlayer.getDuration() * 100);
                    mIndicatorSeekBar.setIndicatorTextFormat("${PROGRESS}% " + minute + " : " + (second <= 9 ? "0" + second : second ));

                }
                mHandler.postDelayed(this, 1000);
            }
        };
    }

    private void playMusic(int position) throws IOException {

        mMediaPlayer.reset();
        mMediaPlayer.setDataSource(mSongsToplay.get(position).getPath());
        mMediaPlayer.prepare();
        mMediaPlayer.start();
        setCoverMusic(position);
        mTextViewArtistName.setText(mSongsToplay.get(position).getArtist());
        mTextViewMusicTitle.setText(mSongsToplay.get(position).getTitle());
        int second = ( mMediaPlayer.getDuration() / 1000 % 60 + 1);
        mTextViewTimming.setText(mMediaPlayer.getDuration() / 60000 + " : " + (second < 10 ? "0" + second : second ));
        // mIndicatorSeekBar.setMax(mMediaPlayer.getDuration() / 1000);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mMediaPlayer.isPlaying()){
            mIsMusicPlaying = true;
        }else{
            mIsMusicPlaying = false;
        }
        outState.putInt(KEY_MEDIA_PLAYER , mMediaPlayer.getCurrentPosition());
        outState.putInt(KEY_MEDIA_POSITION , position);
        outState.putBoolean(KEY_MEDIA_STATE , mIsMusicPlaying);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mMediaPlayer.isPlaying()){
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mHandler.removeCallbacks(mRunnable);
        }
    }
}
