package com.fachrizal.niagaelektronik;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import com.fachrizal.niagaelektronik.adapter.detail_pesanan_adapter;
import com.fachrizal.niagaelektronik.helper.SqliteHelper;
import com.fachrizal.niagaelektronik.model.detail_pesanan_model;
import java.text.NumberFormat;

import java.util.ArrayList;
import java.util.Locale;

public class DetailPesananActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private com.fachrizal.niagaelektronik.adapter.detail_pesanan_adapter detail_pesanan_adapter;
    private ArrayList<detail_pesanan_model> detail_pesanan_ArrayList;

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    SqliteHelper sqliteHelper;
    Cursor cursor;
    TextView textStatusDB, textTanggalDB, textInvoiceDB, textAlamatDB,
            textInfoJumlahDB, textInfoTotalHargaKanan, textInfoTotalBayarKanan;
    int intGambarProduk;
    Button buttonPesananDiterima;
    String invoiceSP, invoiceDB, id_akun, stringJumlah, stringTanggal, stringNamaPembeli, stringNamaProduk,
            stringAlamat, stringStatus, hargaRupiah, total_harga_rupiah;
    Integer totalHargaKeseluruhan, totalJumlahKeseluruhan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pesanan);

        sharedPreferences = getSharedPreferences("mainsession", Context.MODE_PRIVATE);
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        sqliteHelper = new SqliteHelper(this);
        textStatusDB = findViewById(R.id.textStatusDB);
        textTanggalDB = findViewById(R.id.textTanggalDB);
        textInvoiceDB = findViewById(R.id.textInvoiceDB);
        textAlamatDB = findViewById(R.id.textAlamatDB);
        textInfoJumlahDB = findViewById(R.id.textInfoJumlahDB);
        textInfoTotalHargaKanan = findViewById(R.id.textInfoTotalHargaKanan);
        textInfoTotalBayarKanan = findViewById(R.id.textInfoTotalBayarKanan);

        invoiceSP = sharedPreferences.getString("invoice","");
        id_akun = sharedPreferences.getString("id_akun","");

        detailPesanan();

        recyclerView = (RecyclerView) findViewById(R.id.list_detail_pesanan);

        detail_pesanan_adapter = new detail_pesanan_adapter(detail_pesanan_ArrayList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(DetailPesananActivity.this);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(detail_pesanan_adapter);

        buttonPesananDiterima = findViewById(R.id.buttonPesananDiterima);

        if(stringStatus.equals("Pesanan Diterima")) {
            buttonPesananDiterima.setVisibility(View.GONE);
        }
        buttonPesananDiterima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailPesananActivity.this);
                builder.setTitle("Konfirmasi Paket");
                builder.setMessage("Temukan Kode QR di dalam Paket Sebelum Memindai");
                builder.setPositiveButton("Oke",
                        (dialog, which) -> {
                                Intent intent = new Intent(DetailPesananActivity.this, QRScannerActivity.class);
                                startActivity(intent);
                                  });
                builder.setNegativeButton("Batal",
                        (dialog, which) -> dialog.dismiss());

                AlertDialog alert1 = builder.create();
                alert1.show();
            }
        });
    }

    void detailPesanan() {
        detail_pesanan_ArrayList = new ArrayList<>();

        totalHargaKeseluruhan = 0;
        totalJumlahKeseluruhan = 0;
        SQLiteDatabase database = sqliteHelper.getReadableDatabase();
        cursor = database.rawQuery("SELECT * FROM pesanan WHERE id_akun = '" + id_akun + "' ORDER BY id_pesanan DESC", null);
        cursor.moveToFirst();

        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

        int i;
        for (i=0; i < cursor.getCount(); i++){
            cursor.moveToPosition(i);

            invoiceDB = cursor.getString(1);
            if(invoiceDB.equals(invoiceSP)) {
                stringNamaPembeli = cursor.getString(3);
                stringNamaProduk = cursor.getString(4);

                Integer intHarga = Integer.parseInt(cursor.getString(5));
                hargaRupiah = formatRupiah.format(intHarga);
                stringJumlah = cursor.getString(6);
                Integer intJumlah = Integer.parseInt(stringJumlah);
                totalJumlahKeseluruhan = intJumlah + totalJumlahKeseluruhan;

                Integer intTotalHarga = intHarga * intJumlah;
                totalHargaKeseluruhan = intTotalHarga + totalHargaKeseluruhan;
                total_harga_rupiah = formatRupiah.format(intTotalHarga);

                stringAlamat = cursor.getString(8);
                stringStatus = cursor.getString(9);
                stringTanggal = cursor.getString(10);

                switch (stringNamaProduk) {
                    case "Mushoku Tensei: Jobless Reincarnation, Vol.01 [Light Novel EN] Bekas" :
                        intGambarProduk = R.drawable.novel;
                        break;
                    case "TCG Pokemon Ho-oh GX Lycanroc GX Pembersih Arena" :
                        intGambarProduk = R.drawable.pokemon_hooh;
                        break;
                    case "TCG Pokemon Metagross GX Darkrai GX" :
                        intGambarProduk = R.drawable.pokemon_metagross;
                        break;
                    case "Aksesoris Sony Xperia X Performance" :
                        intGambarProduk = R.drawable.aksesoris_hp;
                        break;
                }

                detail_pesanan_ArrayList.add(new detail_pesanan_model(intGambarProduk,
                        stringNamaProduk, stringJumlah, hargaRupiah, total_harga_rupiah));

            }
        }
        textStatusDB.setText(stringStatus);
        textTanggalDB.setText(stringTanggal);
        textInvoiceDB.setText(invoiceSP);
        textAlamatDB.setText(stringNamaPembeli + "\n" + stringAlamat);
        textInfoJumlahDB.setText("(" + totalJumlahKeseluruhan + " Barang" + ")");
        textInfoTotalHargaKanan.setText(formatRupiah.format(totalHargaKeseluruhan));
        textInfoTotalBayarKanan.setText(formatRupiah.format(totalHargaKeseluruhan));

    }
}