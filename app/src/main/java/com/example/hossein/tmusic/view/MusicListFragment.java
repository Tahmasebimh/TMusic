package com.example.hossein.tmusic.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hossein.tmusic.R;
import com.example.hossein.tmusic.model.Song;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class MusicListFragment extends Fragment {

    private static final String TAG_DEBUG_LIST = "<> ListSize <>";
    private RecyclerView mRecyclerViewMusicList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_music_list, container, false);
        ArrayList<Song> songArrayList = getSongList();
        Log.d(TAG_DEBUG_LIST , songArrayList.size() + "");

        mRecyclerViewMusicList = view.findViewById(R.id.recycler_view_music_list);
        mRecyclerViewMusicList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerViewMusicList.setAdapter(new AllMusicListAdapter(songArrayList));

        return view;
    }

    private class AllMusicListViewHolder extends RecyclerView.ViewHolder{

        private TextView mTextViewSongTitle;
        private TextView mTextViewSongAlbumName;
        private ImageView mImageViewSongCover;

        public AllMusicListViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewSongTitle = itemView.findViewById(R.id.tv_music_name);
            mTextViewSongAlbumName = itemView.findViewById(R.id.tv_music_album_name);
            mImageViewSongCover = itemView.findViewById(R.id.img_view_song_cover);
        }

        public void onBind (Song song){
            mTextViewSongTitle.setText(song.getTitle());
            mTextViewSongAlbumName.setText(song.getAlbumName());
            try {
                Log.d(TAG_DEBUG_LIST , song.getTitle() + song.getUriAlbumPhoto().toString());
                if(song.getUriAlbumPhoto() != null)
                    Picasso.get().load(song.getUriAlbumPhoto()).centerCrop()
                            .resize(200 , 200)
                            .into(mImageViewSongCover);
                else {
                    Picasso.get().load(R.drawable.ic_launcher_background).into(mImageViewSongCover);
                }
//                mImageViewSongCover.setImageBitmap(PictureUtils
//                        .getScalledBitmap(getRealPathFromURI(song.getUriAlbumPhoto()) , 70 , 70));
            }catch (Exception e){
                Log.i(TAG_DEBUG_LIST , e.getMessage());
            }
        }
    }

    private class AllMusicListAdapter extends RecyclerView.Adapter<MusicListFragment.AllMusicListViewHolder>{

        private ArrayList<Song> mSongArrayList;
        public AllMusicListAdapter(ArrayList<Song> songs) {
            mSongArrayList = songs;
        }

        @NonNull
        @Override
        public MusicListFragment.AllMusicListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(getActivity()).inflate(R.layout.music_list_model , parent , false);
            return new AllMusicListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AllMusicListViewHolder holder, int position) {
            holder.onBind(mSongArrayList.get(position));
        }

        @Override
        public int getItemCount() {
            return mSongArrayList.size();
        }
    }


    public abstract ArrayList<Song> getSongList();
}
