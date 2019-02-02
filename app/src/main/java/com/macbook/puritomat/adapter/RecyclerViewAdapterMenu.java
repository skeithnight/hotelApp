package com.macbook.puritomat.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
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
import com.macbook.puritomat.model.DataMenu;

import java.util.ArrayList;

public class RecyclerViewAdapterMenu extends RecyclerView.Adapter<RecyclerViewAdapterMenu.MyViewHolder> {
    private ArrayList<DataMenu> dataMenuArrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView namaMenu, hargaMenu;
        public LinearLayout cdData;
        public View mView;

        public MyViewHolder(View view) {
            super(view);
            mView = view;
            namaMenu = (TextView) view.findViewById(R.id.tx_nama_menu);
            hargaMenu = (TextView) view.findViewById(R.id.tx_harga_menu);
            cdData = (LinearLayout) view.findViewById(R.id.cd_data_menu);
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
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final DataMenu dataMenu = dataMenuArrayList.get(position);
        Log.i("Testing", "onBindViewHolder: "+dataMenu.getNama());

        holder.namaMenu.setText(dataMenu.getNama());
        holder.hargaMenu.setText("Rp. "+String.valueOf(dataMenu.getHarga()));
        holder.cdData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.mView.getContext(), DetailDataActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("menu",holder.mView.getContext().getString(R.string.manajemen_2));
                intent.putExtra("typeDetail","detail");
                Gson gson = new Gson();
                intent.putExtra("data",gson.toJson(dataMenu));
                holder.mView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataMenuArrayList.size();
    }
}
