package com.fachrizal.niagaelektronik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.fachrizal.niagaelektronik.helper.SqliteHelper;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

public class KeranjangActivity extends AppCompatActivity {
    ListView list_keranjang;
    ArrayList<HashMap<String, String>> arrayKeranjang = new ArrayList<HashMap<String, String>>();
    SqliteHelper sqliteHelper;
    Cursor cursor;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    ImageView plus, minus, delete;
    TextView textJumlah, textTotalHarga;
    EditText editAlamatDB;
    Button buttonBeli;
    String id_akun, nama_produk, invoice, status, stringHarga, stringTotalJumlah, stringAlamat;
    Integer integerJumlah, totalJumlah, totalHarga, totalHargaKeseluruhan, integerHarga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keranjang);

        sharedPreferences = getSharedPreferences("mainsession", Context.MODE_PRIVATE);
        id_akun = sharedPreferences.getString("id_akun", "");

        textTotalHarga = findViewById(R.id.textTotalHarga);
        editAlamatDB = findViewById(R.id.editAlamatDB);
        buttonBeli = findViewById(R.id.buttonBeli);
        sqliteHelper = new SqliteHelper(this);
        list_keranjang = findViewById(R.id.list_keranjang);
        arrayKeranjang.clear();
        getAkun();
        KeranjangAdapter();

        buttonBeli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editAlamatDB.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Harap Isi Alamat",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    stringAlamat = editAlamatDB.getText().toString();
                    Random r = new Random();
                    int intInvoice = r.nextInt(999999 - 111111) + 111111;
                    String stringInvoice = "INV" + String.valueOf(intInvoice);

                    SQLiteDatabase databaseWrite = sqliteHelper.getWritableDatabase();
                    databaseWrite.execSQL(
                            "UPDATE pesanan SET invoice ='" + stringInvoice + "', " +
                                    "alamat = '" + stringAlamat + "', status = '" +"Menunggu Pembayaran"+"'" +
                                    " WHERE status = '" + "Keranjang" + "'"
                    );

                    editor = sharedPreferences.edit();
                    editor.putString("invoice", stringInvoice);
                    editor.apply();

                    Intent intent = new Intent(KeranjangActivity.this, BayarActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    public void getAkun() {
        SQLiteDatabase database = sqliteHelper.getReadableDatabase();
        cursor = database.rawQuery(
                "SELECT * FROM akun WHERE id_akun ='" + id_akun + "' "
                , null
        );
        cursor.moveToFirst();

        if(cursor.getCount()==1) {
            stringAlamat = cursor.getString(5);
            editAlamatDB.setText(stringAlamat);
        }
    }

    private void KeranjangAdapter(){
        arrayKeranjang.clear();
        list_keranjang.setAdapter(null);

        totalHargaKeseluruhan = 0;
        SQLiteDatabase database = sqliteHelper.getReadableDatabase();
        cursor = database.rawQuery("SELECT * FROM pesanan WHERE id_akun = '" + id_akun + "' ORDER BY id_pesanan DESC", null);
        cursor.moveToFirst();

        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

        int i;
        for (i=0; i < cursor.getCount(); i++){
            cursor.moveToPosition(i);
            status = cursor.getString(9);
            if(status.equals("Keranjang")) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id_pesanan", cursor.getString(0) );
                map.put("invoice",       cursor.getString(1) );
                map.put("nama_produk",      cursor.getString(4) );
                map.put("status",      cursor.getString(9) );
                map.put("tanggal",       cursor.getString(10) );

                Integer intHarga = Integer.parseInt(cursor.getString(5));
                String hargaProduk = intHarga.toString();
                String hargaRupiah = formatRupiah.format(intHarga);
                String stringJumlah = cursor.getString(6);
                map.put("jumlah", stringJumlah);
                Integer intJumlah = Integer.parseInt(stringJumlah);
                Integer intTotalHarga = intHarga * intJumlah;
                totalHargaKeseluruhan = intTotalHarga + totalHargaKeseluruhan;
                map.put("harga_produk", hargaProduk);
                map.put("harga_rupiah", hargaRupiah);

                nama_produk = cursor.getString(4);
                switch (nama_produk) {
                    case "Mushoku Tensei: Jobless Reincarnation, Vol.01 [Light Novel EN] Bekas" :
                        map.put("gambar_produk", R.drawable.novel + "");
                        break;
                    case "TCG Pokemon Ho-oh GX Lycanroc GX Pembersih Arena" :
                        map.put("gambar_produk", R.drawable.pokemon_hooh + "");
                        break;
                    case "TCG Pokemon Metagross GX Darkrai GX" :
                        map.put("gambar_produk", R.drawable.pokemon_metagross + "");
                        break;
                    case "Aksesoris Sony Xperia X Performance" :
                        map.put("gambar_produk", R.drawable.aksesoris_hp + "");
                        break;
                }
                arrayKeranjang.add(map);
            }
        }
        textTotalHarga.setText(formatRupiah.format(totalHargaKeseluruhan));

        if (cursor.getCount() == 0){
            Toast.makeText(getApplicationContext(), "Belum ada keranjang untuk ditampilkan",
                    Toast.LENGTH_LONG).show();
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, arrayKeranjang, R.layout.list_keranjang,
                new String[] { "id_pesanan", "invoice", "nama_produk",
                        "harga_produk", "harga_rupiah", "jumlah", "gambar_produk"},
                new int[] {R.id.id_pesanan, R.id.invoice, R.id.txtNamaProduk,
                        R.id.hargaProduk, R.id.hargaRupiah, R.id.jumlah, R.id.gambarProduk});

        list_keranjang.setAdapter(simpleAdapter);
        list_keranjang.setOnItemClickListener((parent, view, position, id) -> {
            invoice = ((TextView) view.findViewById(R.id.invoice)).getText().toString();
            plus = ((ImageView) view.findViewById(R.id.plus));
            minus = ((ImageView) view.findViewById(R.id.minus));
            delete = ((ImageView) view.findViewById(R.id.delete));

            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    textJumlah = ((TextView) view.findViewById(R.id.jumlah));
                    integerJumlah = Integer.parseInt(textJumlah.getText().toString());
                    totalJumlah = integerJumlah + 1;
                    stringTotalJumlah = totalJumlah.toString();
                    textJumlah.setText(stringTotalJumlah);
                    stringHarga = ((TextView) view.findViewById(R.id.hargaProduk)).getText().toString();
                    integerHarga = Integer.parseInt(stringHarga);
                    totalHarga = integerHarga * totalJumlah;
                    totalHargaKeseluruhan = integerHarga + totalHargaKeseluruhan;
                    textTotalHarga.setText(formatRupiah.format(totalHargaKeseluruhan));

                    SQLiteDatabase databaseWrite = sqliteHelper.getWritableDatabase();
                    databaseWrite.execSQL(
                            "UPDATE pesanan SET jumlah='" + stringTotalJumlah + "'" +
                                    " WHERE invoice = '" + invoice + "'"
                    );
                }
            });

            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    textJumlah = ((TextView) view.findViewById(R.id.jumlah));
                    integerJumlah = Integer.parseInt(textJumlah.getText().toString());
                    if(integerJumlah > 1) {
                        totalJumlah = integerJumlah - 1;
                        stringTotalJumlah = totalJumlah.toString();
                        textJumlah.setText(stringTotalJumlah);
                        stringHarga = ((TextView) view.findViewById(R.id.hargaProduk)).getText().toString();
                        integerHarga = Integer.parseInt(stringHarga);
                        totalHarga = integerHarga * totalJumlah;
                        totalHargaKeseluruhan = totalHargaKeseluruhan - integerHarga;
                        textTotalHarga.setText(formatRupiah.format(totalHargaKeseluruhan));

                        SQLiteDatabase databaseWrite = sqliteHelper.getWritableDatabase();
                        databaseWrite.execSQL(
                                "UPDATE pesanan SET jumlah='" + stringTotalJumlah + "'" +
                                        " WHERE invoice = '" + invoice + "'"
                        );
                    }
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        SQLiteDatabase database = sqliteHelper.getWritableDatabase();
                        database.execSQL("DELETE FROM pesanan WHERE invoice = '" + invoice + "'");
                        arrayKeranjang.clear();
                        KeranjangAdapter();
                }
            });

        });
    }
}