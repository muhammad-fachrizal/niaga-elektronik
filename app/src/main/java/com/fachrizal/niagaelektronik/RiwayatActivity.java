package com.fachrizal.niagaelektronik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.fachrizal.niagaelektronik.helper.SqliteHelper;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class RiwayatActivity extends AppCompatActivity {
    String id_akun, nama_produk, status, total_produk,
            invoice, invoiceSekarang, invoiceSebelumnya;
    Integer intTotalProduk, totalHargaKeseluruhan;
    ListView list_riwayat;
    ArrayList<HashMap<String, String>> arrayRiwayat = new ArrayList<HashMap<String, String>>();
    SqliteHelper sqliteHelper;
    Cursor cursor, cursorInvoice;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat);

        sharedPreferences = getSharedPreferences("mainsession", Context.MODE_PRIVATE);
        id_akun = sharedPreferences.getString("id_akun", "");
        sqliteHelper = new SqliteHelper(this);
        list_riwayat = findViewById(R.id.list_riwayat);
        arrayRiwayat.clear();
        RiwayatAdapter();
        invoiceSebelumnya = " ";
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.button_home:
                        Intent intent = new Intent(RiwayatActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.button_riwayat:
                        Toast.makeText(RiwayatActivity.this, "Riwayat", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.button_akun:
                        intent = new Intent(RiwayatActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                }
                return true;
            }
        });
    }

    private void RiwayatAdapter(){
        arrayRiwayat.clear();
        list_riwayat.setAdapter(null);

        SQLiteDatabase database = sqliteHelper.getReadableDatabase();
        cursor = database.rawQuery("SELECT * FROM pesanan WHERE id_akun = '" + id_akun + "' ORDER BY id_pesanan DESC", null);
        cursor.moveToFirst();

        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

        int i;
        for (i=0; i < cursor.getCount(); i++){
            cursor.moveToPosition(i);

            status = cursor.getString(9);
            if(!status.equals("Keranjang")) {

                invoiceSekarang = cursor.getString(1);
                if(!invoiceSekarang.equals(invoiceSebelumnya)) {
                    invoiceSebelumnya = invoiceSekarang;
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("id_pesanan", cursor.getString(0) );
                    map.put("invoice",       cursor.getString(1) );
                    map.put("nama_produk",      cursor.getString(4) );
                    map.put("status",      cursor.getString(9) );
                    map.put("tanggal",       cursor.getString(10) );

                    Integer intHarga = Integer.parseInt(cursor.getString(5));
                    Integer intJumlah = Integer.parseInt(cursor.getString(6));
                    Integer intTotalHarga = intHarga * intJumlah;
                    String total_harga = formatRupiah.format(intTotalHarga);
                    map.put("total_harga", total_harga);

                    nama_produk = cursor.getString(4);
                    switch (nama_produk) {
                        case "Mushoku Tensei: Jobless Reincarnation, Vol.01 [Light Novel EN] Bekas":
                            map.put("gambar_produk", R.drawable.novel + "");
                            break;
                        case "TCG Pokemon Ho-oh GX Lycanroc GX Pembersih Arena":
                            map.put("gambar_produk", R.drawable.pokemon_hooh + "");
                            break;
                        case "TCG Pokemon Metagross GX Darkrai GX":
                            map.put("gambar_produk", R.drawable.pokemon_metagross + "");
                            break;
                        case "Aksesoris Sony Xperia X Performance":
                            map.put("gambar_produk", R.drawable.aksesoris_hp + "");
                            break;
                    }

                    SQLiteDatabase databaseRead = sqliteHelper.getReadableDatabase();
                    cursorInvoice = databaseRead.rawQuery(
                            "SELECT * FROM pesanan WHERE invoice ='" + invoiceSekarang + "' "
                            , null
                    );
                    cursorInvoice.moveToFirst();

                    if(cursorInvoice.getCount()>1) {
                        totalHargaKeseluruhan = 0;
                        intTotalProduk = cursorInvoice.getCount() - 1;
                        total_produk = String.valueOf(intTotalProduk);
                        map.put("total_produk", "+" + total_produk + " produk lainnya");

                        int j;
                        for (j=0; j < cursorInvoice.getCount(); j++) {
                            cursorInvoice.moveToPosition(j);

                            Integer intHarga2 = Integer.parseInt(cursorInvoice.getString(5));
                            Integer intJumlah2 = Integer.parseInt(cursorInvoice.getString(6));
                            Integer intTotalHarga2 = intHarga2 * intJumlah2;
                            totalHargaKeseluruhan = intTotalHarga2 + totalHargaKeseluruhan;
                            String total_harga2 = formatRupiah.format(totalHargaKeseluruhan);
                            map.put("total_harga", total_harga2);

                        }
                    }
                    arrayRiwayat.add(map);
                }

            }

        }

        if (cursor.getCount() == 0){
            Toast.makeText(getApplicationContext(), "Belum ada pesanan untuk ditampilkan",
                    Toast.LENGTH_LONG).show();
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, arrayRiwayat, R.layout.list_riwayat,
                new String[] { "id_pesanan", "invoice", "nama_produk", "status", "tanggal",
                        "total_harga", "gambar_produk", "total_produk"},
                new int[] {R.id.id_pesanan, R.id.invoice, R.id.nama_produk, R.id.status,
                        R.id.tanggal, R.id.total_harga, R.id.gambar_produk, R.id.textTotalProduk});

        list_riwayat.setAdapter(simpleAdapter);
        list_riwayat.setOnItemClickListener((parent, view, position, id) -> {
            invoice = ((TextView) view.findViewById(R.id.invoice)).getText().toString();
            status = ((TextView) view.findViewById(R.id.status)).getText().toString();

            editor = sharedPreferences.edit();
            editor.putString("invoice", invoice);
            editor.apply();

            if(status.equals("Menunggu Pembayaran")) {
                Intent intent = new Intent(RiwayatActivity.this, BayarActivity.class);
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(RiwayatActivity.this, DetailPesananActivity.class);
                startActivity(intent);
            }
        });
    }
}