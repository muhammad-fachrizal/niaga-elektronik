package com.fachrizal.niagaelektronik.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fachrizal.niagaelektronik.R;
import com.fachrizal.niagaelektronik.model.detail_pesanan_model;

import java.util.ArrayList;

public class detail_pesanan_adapter extends RecyclerView.Adapter<detail_pesanan_adapter.detail_pesanan_ViewHolder> {
    private ArrayList<detail_pesanan_model> dataList;

    public detail_pesanan_adapter(ArrayList<detail_pesanan_model> dataList) {
        this.dataList = dataList;
    }

    @Override
    public detail_pesanan_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_detail_pesanan, parent, false);
        return new detail_pesanan_ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(detail_pesanan_ViewHolder holder, int position) {
        holder.gambarProduk.setImageResource(dataList.get(position).getGambar_produk());
        holder.textNamaProdukDB.setText(dataList.get(position).getNama_produk());
        holder.textJumlahDB.setText(dataList.get(position).getJumlah());
        holder.textHargaDB.setText(dataList.get(position).getHarga());
        holder.textTotalHargaMerah.setText(dataList.get(position).getTotal_harga());
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class detail_pesanan_ViewHolder extends RecyclerView.ViewHolder{
        private TextView textNamaProdukDB, textJumlahDB, textHargaDB, textTotalHargaMerah;
        private ImageView gambarProduk;

        public detail_pesanan_ViewHolder(View itemView) {
            super(itemView);
            gambarProduk = (ImageView) itemView.findViewById(R.id.gambarProduk);
            textNamaProdukDB = (TextView) itemView.findViewById(R.id.textNamaProdukDB);
            textJumlahDB = (TextView) itemView.findViewById(R.id.textJumlahDB);
            textHargaDB = (TextView) itemView.findViewById(R.id.textHargaDB);
            textTotalHargaMerah = (TextView) itemView.findViewById(R.id.textTotalHargaMerah);
        }
    }
}