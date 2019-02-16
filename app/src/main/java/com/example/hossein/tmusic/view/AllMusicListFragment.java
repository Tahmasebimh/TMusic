package com.example.hossein.tmusic.view;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hossein.tmusic.PlayMusicActivity;
import com.example.hossein.tmusic.PlayMusicFragment;
import com.example.hossein.tmusic.R;
import com.example.hossein.tmusic.model.Song;
import com.example.hossein.tmusic.model.SongLab;
import com.squareup.picasso.Picasso;


import java.io.IOException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AllMusicListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class AllMusicListFragment extends Fragment {

    private static final String TAG_DEBUG_LIST = "<>ListSize<>";
    private OnFragmentInteractionListener mListener;

    public AllMusicListFragment() {
        // Required empty public constructor
    }

    public static AllMusicListFragment newInstance() {
        
        Bundle args = new Bundle();
        
        AllMusicListFragment fragment = new AllMusicListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView mRecyclerViewAllMusic;
    ArrayList<Song> mSongArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_music_list, container, false);
       mSongArrayList = SongLab.getInstance().getSongList(getActivity());


        mRecyclerViewAllMusic = view.findViewById(R.id.recycler_view_music_list);
        mRecyclerViewAllMusic.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerViewAllMusic.setAdapter(new AllMusicListAdapter(mSongArrayList));

        return view;
    }

    private void updateUI(){

    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long songId = mSongArrayList.get(getAdapterPosition()).getId();
                    long albumId = mSongArrayList.get(getAdapterPosition()).getAlbumId();
                    long artistId = mSongArrayList.get(getAdapterPosition()).getArtistId();
                    Intent intent = PlayMusicActivity.newIntent(getActivity() , PlayMusicFragment.ALL_MUSIC_KIND
                    ,songId , artistId , albumId , getAdapterPosition());
                    startActivity(intent);
                }
            });
        }

        public void onBind (Song song){
            mTextViewSongTitle.setText(song.getTitle());
            mTextViewSongAlbumName.setText(song.getAlbumName());
            Log.d(TAG_DEBUG_LIST , song.getPath());
            try {

                if(song.getUriAlbumPhoto() != null)
                Picasso.get().load(song.getUriAlbumPhoto()).centerCrop()
                        .resize(200 , 200)
                        .placeholder(R.drawable.music_deffault_icon)
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

    private class AllMusicListAdapter extends RecyclerView.Adapter<AllMusicListViewHolder>{

        private ArrayList<Song> mSongArrayList;
        public AllMusicListAdapter(ArrayList<Song> songs) {
            mSongArrayList = songs;
        }

        @NonNull
        @Override
        public AllMusicListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj,
                null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
