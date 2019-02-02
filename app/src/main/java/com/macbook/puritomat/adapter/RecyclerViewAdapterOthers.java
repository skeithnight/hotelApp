package com.macbook.puritomat.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.macbook.puritomat.R;
import com.macbook.puritomat.activity.DetailDataActivity;
import com.macbook.puritomat.model.DataOthers;
import com.macbook.puritomat.model.DataOthers;

import java.util.ArrayList;

public class RecyclerViewAdapterOthers extends RecyclerView.Adapter<RecyclerViewAdapterOthers.MyViewHolder> {
    private ArrayList<DataOthers> dataOthersArrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView namaOthers, hargaOthers;
        public LinearLayout cdData;
        public View mView;

        public MyViewHolder(View view) {
            super(view);
            mView = view;
            namaOthers = (TextView) view.findViewById(R.id.tx_nama_others);
            hargaOthers = (TextView) view.findViewById(R.id.tx_harga_others);
            cdData = (LinearLayout) view.findViewById(R.id.cd_data_others);
        }
    }

    public RecyclerViewAdapterOthers(ArrayList<DataOthers> dataOthersArrayList) {
        this.dataOthersArrayList = dataOthersArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_data_others, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final DataOthers dataOthers = dataOthersArrayList.get(position);
        Log.i("Testing", "onBindViewHolder: "+dataOthers.getNama());

        holder.namaOthers.setText(dataOthers.getNama());
        holder.hargaOthers.setText("Rp. "+String.valueOf(dataOthers.getHarga()));
        holder.cdData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.mView.getContext(), DetailDataActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("menu",holder.mView.getContext().getString(R.string.manajemen_4));
                intent.putExtra("typeDetail","detail");
                Gson gson = new Gson();
                intent.putExtra("data",gson.toJson(dataOthers));
                holder.mView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataOthersArrayList.size();
    }
}
