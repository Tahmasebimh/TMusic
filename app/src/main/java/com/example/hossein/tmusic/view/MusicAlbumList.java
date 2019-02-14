package com.example.hossein.tmusic.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hossein.tmusic.AlbumArtistDetailActivity;
import com.example.hossein.tmusic.R;
import com.example.hossein.tmusic.model.Album;
import com.example.hossein.tmusic.model.AlbumLab;
import com.example.hossein.tmusic.utils.ItemDecorationAlbumColumns;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MusicAlbumList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class MusicAlbumList extends Fragment {

    private static final String TAG_DEBUG_LIST = "<<debug>>";
    private static final String TAG_DEBUG_ALBUM_LIST = "<<albumlistdebug>>";
    private OnFragmentInteractionListener mListener;
    private RecyclerView mRecyclerViewAlbumList;
    private ArrayList<Album> mAlbumArrayList;

    public MusicAlbumList() {
        // Required empty public constructor
    }

    public static MusicAlbumList newInstance() {
        
        Bundle args = new Bundle();
        
        MusicAlbumList fragment = new MusicAlbumList();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_music_album_list, container, false);

        mAlbumArrayList = AlbumLab.getInstance().getAlbumList(getActivity());
        mRecyclerViewAlbumList = view.findViewById(R.id.recycler_view_album_list);
        mRecyclerViewAlbumList.setLayoutManager(new GridLayoutManager(getActivity() , 2));
        mAlbumArrayList = AlbumLab.getInstance().getAlbumList(getActivity());
        mRecyclerViewAlbumList.setAdapter(new AlbumListRecyclerAdapter(mAlbumArrayList));

        return view;
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
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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


    private class AlbumRecyclerViewHolder extends RecyclerView.ViewHolder{

        private ImageView mImageView;
        private TextView mTextViewAlbumTitle;
        private TextView mTextViewAlbumArtist;
        public AlbumRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.img_album_cover);
            mTextViewAlbumTitle = itemView.findViewById(R.id.tv_album_title);
            mTextViewAlbumArtist = itemView.findViewById(R.id.tv_album_artist);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Long albumId = mAlbumArrayList.get(getAdapterPosition()).getAlbumID();
                    Intent intent = AlbumArtistDetailActivity.newIntent(albumId , getActivity() , AlbumArtistDetailActivity.DETAIL_ALBUM);
                    startActivity(intent);
                }
            });
        }

        public void bind(Album album) {
            try {
                    Picasso.get().load(album.getAlbumCoverUri()).centerCrop()
                            .resize(mImageView.getWidth() , mImageView.getHeight())
                            .placeholder(R.drawable.music_deffault_icon)
                            .into(mImageView);
            }catch (Exception e){
                Log.i(TAG_DEBUG_LIST , e.getMessage());
            }
            mTextViewAlbumTitle.setText(album.getAlbumTitle());
            mTextViewAlbumArtist.setText(album.getAlbumArtsit());
        }
    }

    private class AlbumListRecyclerAdapter extends RecyclerView.Adapter<AlbumRecyclerViewHolder>{

        private ArrayList<Album> mAlbumArrayList;

        public AlbumListRecyclerAdapter(ArrayList<Album> albumArrayList) {
            mAlbumArrayList = albumArrayList;
        }

        @NonNull
        @Override
        public AlbumRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(getActivity()).inflate(R.layout.album_list_model , parent , false);
            AlbumRecyclerViewHolder albumRecyclerViewHolder = new AlbumRecyclerViewHolder(view);
            return albumRecyclerViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull AlbumRecyclerViewHolder holder, int position) {
            holder.bind(mAlbumArrayList.get(position));
        }

        @Override
        public int getItemCount() {
            return mAlbumArrayList.size();
        }
    }
}
