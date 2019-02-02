package com.macbook.puritomat.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.macbook.puritomat.R;
import com.macbook.puritomat.activity.DetailDataActivity;
import com.macbook.puritomat.model.DataLaundry;
import com.macbook.puritomat.model.DataLaundry;

import java.util.ArrayList;

public class RecyclerViewAdapterLaundry extends RecyclerView.Adapter<RecyclerViewAdapterLaundry.MyViewHolder> {
private ArrayList<DataLaundry> dataLaundryArrayList;

public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView namaLaundry, hargaLaundry;
    public LinearLayout cdData;
    public View mView;

    public MyViewHolder(View view) {
        super(view);
        mView = view;
        namaLaundry = (TextView) view.findViewById(R.id.tx_nama_laundry);
        hargaLaundry = (TextView) view.findViewById(R.id.tx_harga_laundry);
        cdData = (LinearLayout) view.findViewById(R.id.cd_data_laundry);
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
    public void onBindViewHolder(final RecyclerViewAdapterLaundry.MyViewHolder holder, int position) {
        final DataLaundry dataLaundry = dataLaundryArrayList.get(position);
        Log.i("Testing", "onBindViewHolder: "+dataLaundry.getNama());

        holder.namaLaundry.setText(dataLaundry.getNama());
        holder.hargaLaundry.setText("Rp. "+String.valueOf(dataLaundry.getHarga()));

        holder.cdData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.mView.getContext(), DetailDataActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("menu",holder.mView.getContext().getString(R.string.manajemen_3));
                intent.putExtra("typeDetail","detail");
                Gson gson = new Gson();
                intent.putExtra("data",gson.toJson(dataLaundry));
                holder.mView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataLaundryArrayList.size();
    }
}
