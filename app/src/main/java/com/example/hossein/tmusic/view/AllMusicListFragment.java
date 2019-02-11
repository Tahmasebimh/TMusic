package com.example.hossein.tmusic.view;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
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

import com.example.hossein.tmusic.R;
import com.example.hossein.tmusic.model.Song;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AllMusicListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class AllMusicListFragment extends Fragment {

    private static final String TAG_DEBUG_LIST = "<> ListSize <>";
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_music_list, container, false);
        ArrayList<Song> songArrayList = getSongList();
        Log.d(TAG_DEBUG_LIST , songArrayList.size() + "");

        mRecyclerViewAllMusic = view.findViewById(R.id.recycler_view_music_list);
        mRecyclerViewAllMusic.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerViewAllMusic.setAdapter(new AllMusicListAdapter(songArrayList));

        return view;
    }

    private void upDateUi(){

    }

    private ArrayList<Song> getSongList() {
        ArrayList<Song> songArrayList = new ArrayList<>();
        ContentResolver contentResolver =  getActivity().getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(songUri , null , null , null  ,null );
        if(cursor != null && cursor.moveToFirst()){
            int songTitle = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songAlbum = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int songAlbumID = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            do {
                Song song = new Song();
                song.setTitle(cursor.getString(songTitle));
                song.setArtist(cursor.getString(songArtist));
                song.setAlbumName(cursor.getString(songAlbum));
                setSongCover(song , cursor.getLong(songAlbumID));
                songArrayList.add(song);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return songArrayList;
    }

    private void setSongCover(Song song, long anInt) {
        Uri uri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), anInt);
        song.setUriAlbumPhoto(uri);
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
