package com.macbook.puritomat.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.macbook.puritomat.R;
import com.macbook.puritomat.activity.ListDataManajemenActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManajemenFragment extends Fragment {

    private View mview;
    @BindView(R.id.judul_fragment)
    TextView tvJudulFragment;


    public ManajemenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mview = inflater.inflate(R.layout.fragment_manajemen, container, false);
        ButterKnife.bind(this,mview);
        tvJudulFragment.setText("Manajemen");

        return mview;
    }

    @OnClick({R.id.cdManajemen1,R.id.cdManajemen2,R.id.cdManajemen3,R.id.btn_logout})
    public void clickCV(View view){
        switch (view.getId()) {
            case R.id.cdManajemen1:
                // do something
//                Toast.makeText(view.getContext(), "1", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mview.getContext(), ListDataManajemenActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("menu","kamar");
                startActivity(intent);
                break;
            case R.id.cdManajemen2:
                // do something else
                Toast.makeText(view.getContext(), "2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.cdManajemen3:
                // i'm lazy, do nothing
                Toast.makeText(view.getContext(), "3", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_logout:
                // i'm lazy, do nothing
                Toast.makeText(view.getContext(), "logout", Toast.LENGTH_SHORT).show();
                break;

        }
    }
}
