package com.example.hossein.tmusic.view;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
public class ArtistDetailFragment extends Fragment {

    private static final String KEY_ARTIST_ID = "artistid";

    public static ArtistDetailFragment newInstance(Long artistId) {

        Bundle args = new Bundle();

        args.putLong(KEY_ARTIST_ID , artistId);
        ArtistDetailFragment fragment = new ArtistDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ArtistDetailFragment() {
        // Required empty public constructor
    }

    private RecyclerView mRecyclerViewArtistDetail;
    private ArrayList<Song> mArtistSongs;
    private TextView mTextViewArtistName;
    private ImageView mImageViewFirstAlbumCover;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_artist_detail, container, false);
        if (getArguments() == null) {
            Toast.makeText(getActivity(), "Artist NOt Found", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }

        mArtistSongs = SongLab.getInstance().getArtistSong(getArguments().getLong(KEY_ARTIST_ID));

        mRecyclerViewArtistDetail = view.findViewById(R.id.recycler_view_artist_detail);
        mImageViewFirstAlbumCover= view.findViewById(R.id.img_first_artist_album);
        mTextViewArtistName = view.findViewById(R.id.tv_artist_name);

        mTextViewArtistName.setText(mArtistSongs.get(0).getArtist());
        Picasso.get().load(mArtistSongs.get(0).getUriAlbumPhoto())
                .resize(200 , 200)
                .placeholder(R.drawable.music_deffault_icon)
                .into(mImageViewFirstAlbumCover);
        mRecyclerViewArtistDetail.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerViewArtistDetail.setAdapter(new ArtistDetailAdapter(mArtistSongs));
        mRecyclerViewArtistDetail.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        return view;
    }

    private class ArtistDetailListHolder extends RecyclerView.ViewHolder{

        private TextView mTextViewSongName ;
        private TextView mTextViewSongAlbumName;
        private ImageView mImageViewSnogAlbumCover;

        public ArtistDetailListHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewSongName = itemView.findViewById(R.id.tv_music_name);
            mTextViewSongAlbumName = itemView.findViewById(R.id.tv_music_album_name);
            mImageViewSnogAlbumCover = itemView.findViewById(R.id.img_view_song_cover);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long songId = mArtistSongs.get(getAdapterPosition()).getId();
                    long albumId = mArtistSongs.get(getAdapterPosition()).getAlbumId();
                    long artistId = mArtistSongs.get(getAdapterPosition()).getArtistId();
                    Intent intent = PlayMusicActivity.newIntent(getActivity() , PlayMusicFragment.ARTIST_MUSIC_KIND
                            ,songId , artistId , albumId , getAdapterPosition());
                    startActivity(intent);
                }
            });
        }

        public void bind(Song song) {
            mTextViewSongName.setText(song.getTitle());
            mTextViewSongAlbumName.setText(song.getAlbumName());
            Picasso.get().load(song.getUriAlbumPhoto())
                    .resize(200 , 200)
                    .placeholder(R.drawable.music_deffault_icon)
                    .into(mImageViewSnogAlbumCover);
        }
    }

    private class ArtistDetailAdapter extends RecyclerView.Adapter<ArtistDetailListHolder>{

        private ArrayList<Song> mArtistSongsArraylist;

        public ArtistDetailAdapter(ArrayList<Song> artistSongsArraylist) {
            mArtistSongsArraylist = artistSongsArraylist;
        }

        @NonNull
        @Override
        public ArtistDetailListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(getActivity()).inflate(R.layout.music_list_model , parent , false);
            ArtistDetailListHolder artistDetailListHolder = new ArtistDetailListHolder(view);
            return artistDetailListHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ArtistDetailListHolder holder, int position) {
            holder.bind(mArtistSongsArraylist.get(position));
        }

        @Override
        public int getItemCount() {
            return mArtistSongsArraylist.size();
        }
    }
}
