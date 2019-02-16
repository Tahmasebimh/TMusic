package com.example.hossein.tmusic.view;


import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hossein.tmusic.PlayMusicActivity;
import com.example.hossein.tmusic.PlayMusicFragment;
import com.example.hossein.tmusic.R;
import com.example.hossein.tmusic.model.Song;
import com.example.hossein.tmusic.model.SongLab;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumDetailFragment extends Fragment {


    private static final String KEY_ALBUM_ID = "albumid";
    private RecyclerView mRecyclerViewAlbumDetailList;
    private ImageView mImageViewAlbumCover;
    private TextView mTextViewAlbumName;
    private TextView mTextViewArtistName;
    private ArrayList<Song> mAlbumSongs;
    private Long mAlbumId;
    private LinearLayout mLinearLayout;

    public AlbumDetailFragment() {
        // Required empty public constructor
    }

    public static AlbumDetailFragment newInstance(Long albumId) {

        Bundle args = new Bundle();
        args.putLong(KEY_ALBUM_ID, albumId);
        AlbumDetailFragment fragment = new AlbumDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_album_detail, container, false);
        initView(view);
        mAlbumId = getArguments().getLong(KEY_ALBUM_ID);
        mAlbumSongs = SongLab.getInstance().getAlbumSongList(mAlbumId);
        if (getArguments() == null) {
            Toast.makeText(getActivity(), "ridi", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();

        display.getSize(size);
        int width = size.x;
        int height = size.y;

        Picasso.get().load(mAlbumSongs.get(0).getUriAlbumPhoto())
                .centerCrop()
                .resize(width, mImageViewAlbumCover.getHeight())
                .placeholder(R.drawable.music_deffault_icon)
                .into(mImageViewAlbumCover);
        mTextViewAlbumName.setText(mAlbumSongs.get(0).getTitle());
        mTextViewArtistName.setText(mAlbumSongs.get(0).getArtist());


        mRecyclerViewAlbumDetailList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerViewAlbumDetailList.setAdapter(new AlbumSongsListAdapter(mAlbumSongs));
        mRecyclerViewAlbumDetailList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        return view;
    }

    private class AlbumSongsListViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextViewMusicTitle;
        private TextView mTextViewMusicArtistName;
        private ImageView mImageView;
        private ConstraintLayout mConstraintLayoutRoot;

        public AlbumSongsListViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewMusicTitle = itemView.findViewById(R.id.tv_music_name);
            mTextViewMusicArtistName = itemView.findViewById(R.id.tv_music_album_name);
            mImageView = itemView.findViewById(R.id.img_view_song_cover);
            mConstraintLayoutRoot = itemView.findViewById(R.id.music_model_root);
            mTextViewMusicArtistName.setVisibility(View.GONE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long songId = mAlbumSongs.get(getAdapterPosition()).getId();
                    long albumId = mAlbumSongs.get(getAdapterPosition()).getAlbumId();
                    long artistId = mAlbumSongs.get(getAdapterPosition()).getArtistId();
                    Intent intent = PlayMusicActivity.newIntent(getActivity() , PlayMusicFragment.ALBUM_MUSIC_KIND
                            ,songId , artistId , albumId , getAdapterPosition());
                    startActivity(intent);
                }
            });

        }

        public void bind(Song song) {
            Picasso.get().load(song.getUriAlbumPhoto())
                    .resize(200 , 200)
                    .placeholder(R.drawable.music_deffault_icon)
                    .centerCrop()
                    .into(mImageView);
            mTextViewMusicTitle.setText(song.getTitle());
        }
    }

    private class AlbumSongsListAdapter extends RecyclerView.Adapter<AlbumSongsListViewHolder> {

        private ArrayList<Song> mAlbumSongsList;

        public AlbumSongsListAdapter(ArrayList<Song> albumSongsList) {
            mAlbumSongsList = albumSongsList;
        }

        @NonNull
        @Override
        public AlbumSongsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(getActivity()).inflate(R.layout.music_list_model, parent, false);
            AlbumSongsListViewHolder albumSongsListViewHolder = new AlbumSongsListViewHolder(view);
            return albumSongsListViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull AlbumSongsListViewHolder holder, int position) {
            holder.bind(mAlbumSongsList.get(position));
        }

        @Override
        public int getItemCount() {
            return mAlbumSongsList.size();
        }
    }

    private void initView(View view) {
        mImageViewAlbumCover = view.findViewById(R.id.img_detail_album_cover);
        mTextViewAlbumName = view.findViewById(R.id.tv_detail_album_name);
        mTextViewArtistName = view.findViewById(R.id.tv_detail_artist_name);
        mRecyclerViewAlbumDetailList = view.findViewById(R.id.recycler_view_album_detail);
        mLinearLayout = view.findViewById(R.id.linear_album_detail_root);
    }

}
