package com.example.hossein.tmusic;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MusicListFragment extends Fragment {


    public MusicListFragment() {
        // Required empty public constructor
    }

    public static MusicListFragment newInstance() {
        
        Bundle args = new Bundle();
        
        MusicListFragment fragment = new MusicListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);
        return textView;
    }

}
