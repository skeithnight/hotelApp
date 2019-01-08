package com.macbook.puritomat.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.macbook.puritomat.R;
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

        holder.namaTamu.setText(transaksi.getTamu().getNama());
        holder.jumlahKamar.setText(String.valueOf(transaksi.getKamar().size()));
        holder.waktuTransaksi.setText(String.valueOf(transaksi.getKamar().get(0).getFrom()));
        holder.status.setText(transaksi.getStatus());
        holder.cvTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mView.getContext(), transaksi.getId(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return transaksiArrayList.size();
    }
}
