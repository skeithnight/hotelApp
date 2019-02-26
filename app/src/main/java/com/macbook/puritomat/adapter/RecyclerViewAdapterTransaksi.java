package com.macbook.puritomat.adapter;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.macbook.puritomat.R;
import com.macbook.puritomat.activity.CheckOutActivity;
import com.macbook.puritomat.model.Transaksi;

import java.util.ArrayList;

public class RecyclerViewAdapterTransaksi extends RecyclerView.Adapter<RecyclerViewAdapterTransaksi.MyViewHolder> {
    private ArrayList<Transaksi> transaksiArrayList;
    public View mView;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView namaTamu, waktuTransaksi, jumlahKamar;
        public Button status;
        public CardView cvTransaksi;

        public MyViewHolder(View view) {
            super(view);
            mView = view;
            namaTamu = (TextView) view.findViewById(R.id.tv_nama_tamu);
            waktuTransaksi = (TextView) view.findViewById(R.id.tv_check_in);
            jumlahKamar = (TextView) view.findViewById(R.id.tv_jumlah_kamar);
            status = (Button) view.findViewById(R.id.btn_process_transaksi);
            cvTransaksi = (CardView) view.findViewById(R.id.cv_transaksi);
        }
    }

    public RecyclerViewAdapterTransaksi(ArrayList<Transaksi> transaksiArrayList) {
        this.transaksiArrayList = transaksiArrayList;
    }

    @Override
    public RecyclerViewAdapterTransaksi.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_data_transaksi, parent, false);

        return new RecyclerViewAdapterTransaksi.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapterTransaksi.MyViewHolder holder, int position) {
        final Transaksi transaksi = transaksiArrayList.get(position);

        String nama = transaksi.getTamu() == null?"-":transaksi.getTamu().getNama();
        int size = transaksi.getKamar() == null ?0:transaksi.getKamar().size();
        long waktutransaksi = transaksi.getKamar() == null?0:transaksi.getKamar().get(0).getFrom();
        String status = transaksi.getStatus() == null?"-":transaksi.getStatus();
        holder.namaTamu.setText(nama);
        holder.jumlahKamar.setText(String.valueOf(size));
        holder.waktuTransaksi.setText(String.valueOf(waktutransaksi));
        holder.status.setText(status);
        holder.cvTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mView.getContext(), CheckOutActivity.class);
                Gson gson = new Gson();
                intent.putExtra("transaksi",gson.toJson(transaksi));
                mView.getContext().startActivity(intent);

                Toast.makeText(mView.getContext(), transaksi.getId(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return transaksiArrayList.size();
    }
}
