package com.example.hossein.tmusic.view;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hossein.tmusic.AlbumArtistDetailActivity;
import com.example.hossein.tmusic.R;
import com.example.hossein.tmusic.model.Artist;
import com.example.hossein.tmusic.model.Song;
import com.example.hossein.tmusic.model.SongLab;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ArtistListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ArtistListFragment extends Fragment {

    private static final String TAG_DEBUG_ARTIST_LIST = "<> ARTISTLIST <>" ;
    private OnFragmentInteractionListener mListener;

    public ArtistListFragment() {
        // Required empty public constructor
    }

    public static ArtistListFragment newInstance() {
        
        Bundle args = new Bundle();
        
        ArtistListFragment fragment = new ArtistListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView mRecyclerViewMusicList;
    private ArrayList<Artist> artistArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_artist_list, container, false);

        mRecyclerViewMusicList = view.findViewById(R.id.recycler_view_artist_list);
        mRecyclerViewMusicList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerViewMusicList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        artistArrayList = SongLab.getInstance().getArtistList(getActivity());
        Log.d(TAG_DEBUG_ARTIST_LIST , artistArrayList.size() + "");
        mRecyclerViewMusicList.setAdapter(new ArtistListAdapter(artistArrayList));

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

    private class ArtistListViewHolder extends RecyclerView.ViewHolder{

        private TextView mTextViewArtistName;
        private TextView mTextViewMusicName;
        private ImageView mImageViewArtistCaver;

        public ArtistListViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewArtistName = itemView.findViewById(R.id.tv_music_name);
            mTextViewMusicName = itemView.findViewById(R.id.tv_music_album_name);
            mImageViewArtistCaver = itemView.findViewById(R.id.img_view_song_cover);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = AlbumArtistDetailActivity.newIntent(artistArrayList.get(getAdapterPosition()).getArtistId() ,
                            getActivity() , AlbumArtistDetailActivity.DETAIL_ARTIST);
                    startActivity(intent);
                }
            });
        }

        public void bind(Artist artist) {
            mTextViewArtistName.setText(artist.getArtsitName());
           // mTextViewMusicName.setText(artist.getArtistSongList().size());
            try {
                if(artist.getUriSongCover() != null)
                    Picasso.get().load(artist.getUriSongCover()).centerCrop()
                            .resize(200 , 200)
                            .placeholder(R.drawable.music_deffault_icon)
                            .into(mImageViewArtistCaver);
                else {
                    Picasso.get().load(R.drawable.ic_launcher_background).into(mImageViewArtistCaver);
                }
            }catch (Exception e){
                Log.i(TAG_DEBUG_ARTIST_LIST , e.getMessage());
            }
        }
    }

    private class ArtistListAdapter extends RecyclerView.Adapter<ArtistListViewHolder>{

        private ArrayList<Artist> mArtistArrayList;
        public ArtistListAdapter(ArrayList<Artist> artistArrayList) {
            mArtistArrayList = artistArrayList;
        }

        @NonNull
        @Override
        public ArtistListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.music_list_model , parent , false);
            return new ArtistListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ArtistListViewHolder holder, int position) {
            holder.bind(mArtistArrayList.get(position));
        }

        @Override
        public int getItemCount() {
            return mArtistArrayList.size();
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
}
