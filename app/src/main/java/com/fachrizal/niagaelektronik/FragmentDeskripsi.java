package com.fachrizal.niagaelektronik;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentDeskripsi extends Fragment {
    View view;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    TextView textNamaProduk, textDeskripsiLengkap;
    String namaProduk;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_deskripsi_lengkap, container, false);

        sharedPreferences = this.getActivity().getSharedPreferences("mainsession", Context.MODE_PRIVATE);
        textNamaProduk = view.findViewById(R.id.textNamaProduk);
        textDeskripsiLengkap = view.findViewById(R.id.textDeskripsiLengkap);
        namaProduk = sharedPreferences.getString("nama_produk","");
        textNamaProduk.setText(namaProduk);
        switch (namaProduk) {
            case "Mushoku Tensei: Jobless Reincarnation, Vol.01 [Light Novel EN] Bekas":
                textDeskripsiLengkap.setText(R.string.deskripsiNovel);
                break;
            case "TCG Pokemon Ho-oh GX Lycanroc GX Pembersih Arena":
                textDeskripsiLengkap.setText(R.string.deskripsiPokemonHooh);
                break;
            case "TCG Pokemon Metagross GX Darkrai GX":
                textDeskripsiLengkap.setText(R.string.deskripsiPokemonMetagross);
                break;
            case "Aksesoris Sony Xperia X Performance":
                textDeskripsiLengkap.setText(R.string.deskripsiAksesorisHp);
                break;
        }
        return view;
    }
}
