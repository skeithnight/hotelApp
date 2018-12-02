package com.macbook.puritomat.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.macbook.puritomat.R;
import com.macbook.puritomat.model.DataMenu;

import java.util.ArrayList;

public class RecyclerViewAdapterMenu extends RecyclerView.Adapter<RecyclerViewAdapterMenu.MyViewHolder> {
    private ArrayList<DataMenu> dataMenuArrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView namaMenu, hargaMenu;

        public MyViewHolder(View view) {
            super(view);
            namaMenu = (TextView) view.findViewById(R.id.tx_nama_menu);
            hargaMenu = (TextView) view.findViewById(R.id.tx_harga_menu);
        }
    }

    public RecyclerViewAdapterMenu(ArrayList<DataMenu> dataMenuArrayList) {
        this.dataMenuArrayList = dataMenuArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_data_menu, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DataMenu dataMenu = dataMenuArrayList.get(position);
        Log.i("Testing", "onBindViewHolder: "+dataMenu.getNama());

        holder.namaMenu.setText(dataMenu.getNama());
        holder.hargaMenu.setText("Rp. "+String.valueOf(dataMenu.getHarga()));
    }

    @Override
    public int getItemCount() {
        return dataMenuArrayList.size();
    }
}
