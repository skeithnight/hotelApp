package com.macbook.puritomat.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.macbook.puritomat.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManajemenFragment extends Fragment {

    private View view;
    @BindView(R.id.judul_fragment)
    TextView tvJudulFragment;


    public ManajemenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_manajemen, container, false);
        ButterKnife.bind(this,view);
        tvJudulFragment.setText("Manajemen");

        return view;
    }

}
