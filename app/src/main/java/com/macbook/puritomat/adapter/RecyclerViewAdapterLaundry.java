package com.macbook.puritomat.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.macbook.puritomat.R;
import com.macbook.puritomat.model.DataLaundry;
import com.macbook.puritomat.model.DataLaundry;

import java.util.ArrayList;

public class RecyclerViewAdapterLaundry extends RecyclerView.Adapter<RecyclerViewAdapterLaundry.MyViewHolder> {
private ArrayList<DataLaundry> dataLaundryArrayList;

public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView namaLaundry, hargaLaundry;

    public MyViewHolder(View view) {
        super(view);
        namaLaundry = (TextView) view.findViewById(R.id.tx_nama_laundry);
        hargaLaundry = (TextView) view.findViewById(R.id.tx_harga_laundry);
    }
}

    public RecyclerViewAdapterLaundry(ArrayList<DataLaundry> dataLaundryArrayList) {
        this.dataLaundryArrayList = dataLaundryArrayList;
    }

    @Override
    public RecyclerViewAdapterLaundry.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_data_laundry, parent, false);

        return new RecyclerViewAdapterLaundry.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapterLaundry.MyViewHolder holder, int position) {
        DataLaundry dataLaundry = dataLaundryArrayList.get(position);
        Log.i("Testing", "onBindViewHolder: "+dataLaundry.getNama());

        holder.namaLaundry.setText(dataLaundry.getNama());
        holder.hargaLaundry.setText("Rp. "+String.valueOf(dataLaundry.getHarga()));
    }

    @Override
    public int getItemCount() {
        return dataLaundryArrayList.size();
    }
}
