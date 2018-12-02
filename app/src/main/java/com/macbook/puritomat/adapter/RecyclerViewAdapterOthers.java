package com.macbook.puritomat.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.macbook.puritomat.R;
import com.macbook.puritomat.model.DataOthers;
import com.macbook.puritomat.model.DataOthers;

import java.util.ArrayList;

public class RecyclerViewAdapterOthers extends RecyclerView.Adapter<RecyclerViewAdapterOthers.MyViewHolder> {
    private ArrayList<DataOthers> dataOthersArrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView namaOthers, hargaOthers;

        public MyViewHolder(View view) {
            super(view);
            namaOthers = (TextView) view.findViewById(R.id.tx_nama_others);
            hargaOthers = (TextView) view.findViewById(R.id.tx_harga_others);
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
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DataOthers dataOthers = dataOthersArrayList.get(position);
        Log.i("Testing", "onBindViewHolder: "+dataOthers.getNama());

        holder.namaOthers.setText(dataOthers.getNama());
        holder.hargaOthers.setText("Rp. "+String.valueOf(dataOthers.getHarga()));
    }

    @Override
    public int getItemCount() {
        return dataOthersArrayList.size();
    }
}
