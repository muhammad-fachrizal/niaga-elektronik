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
import android.widget.TextView;
import android.widget.Toast;

import com.fachrizal.niagaelektronik.helper.SqliteHelper;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class BeliLangsungActivity extends AppCompatActivity {
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    String namaProduk, namaToko, namaKota, stringHarga,
            id_akun, nama_pembeli, alamat, alamatFix, stringCatatan;
    Integer integerJumlah, totalJumlah, totalHarga, integerHarga;
    TextView textNamaToko, textNamaKota, textNamaProduk, textHarga,
            textJumlah, textAlamat, textTotalHarga;
    ImageView gambarProduk, plus, minus;
    EditText editAlamat, editCatatan;
    Button buttonBayar;
    SqliteHelper sqliteHelper;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beli_langsung);

        totalJumlah = 1;
        sharedPreferences = getSharedPreferences("mainsession", Context.MODE_PRIVATE);
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        sqliteHelper = new SqliteHelper(this);
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        gambarProduk = findViewById(R.id.gambarProduk);
        plus = findViewById(R.id.plus);
        textJumlah = findViewById(R.id.jumlah);
        minus = findViewById(R.id.minus);
        editAlamat = findViewById(R.id.editAlamat);
        editCatatan = findViewById(R.id.catatan);
        textNamaToko = findViewById(R.id.namaToko);
        textNamaKota = findViewById(R.id.namaKota);
        textNamaProduk = findViewById(R.id.txtNamaProduk);
        textHarga = findViewById(R.id.hargaProduk);
        textAlamat = findViewById(R.id.textAlamat);
        textTotalHarga = findViewById(R.id.totalHarga);
        buttonBayar = findViewById(R.id.bayarButton);

        namaProduk = sharedPreferences.getString("nama_produk","");
        namaToko = sharedPreferences.getString("nama_toko","");
        namaKota = sharedPreferences.getString("nama_kota","");
        stringHarga = sharedPreferences.getString("harga","");
        integerHarga = Integer.parseInt(stringHarga);

        id_akun = sharedPreferences.getString("id_akun","");
        getAkun();
        editAlamat.setText(alamat);

        switch (namaProduk) {
            case "Mushoku Tensei: Jobless Reincarnation, Vol.01 [Light Novel EN] Bekas":
                gambarProduk.setImageResource(R.drawable.novel);
                textNamaToko.setText(namaToko);
                textNamaKota.setText(namaKota);
                textNamaProduk.setText(namaProduk);
                textHarga.setText(formatRupiah.format(integerHarga));
                textTotalHarga.setText(formatRupiah.format(integerHarga));
                break;
            case "TCG Pokemon Ho-oh GX Lycanroc GX Pembersih Arena":
                gambarProduk.setImageResource(R.drawable.pokemon_hooh);
                textNamaToko.setText(namaToko);
                textNamaKota.setText(namaKota);
                textNamaProduk.setText(namaProduk);
                textHarga.setText(formatRupiah.format(integerHarga));
                textTotalHarga.setText(formatRupiah.format(integerHarga));
                break;
            case "TCG Pokemon Metagross GX Darkrai GX":
                gambarProduk.setImageResource(R.drawable.pokemon_metagross);
                textNamaToko.setText(namaToko);
                textNamaKota.setText(namaKota);
                textNamaProduk.setText(namaProduk);
                textHarga.setText(formatRupiah.format(integerHarga));
                textTotalHarga.setText(formatRupiah.format(integerHarga));
                break;
            case "Aksesoris Sony Xperia X Performance":
                gambarProduk.setImageResource(R.drawable.aksesoris_hp);
                textNamaToko.setText(namaToko);
                textNamaKota.setText(namaKota);
                textNamaProduk.setText(namaProduk);
                textHarga.setText(formatRupiah.format(integerHarga));
                textTotalHarga.setText(formatRupiah.format(integerHarga));
                break;
        }

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                integerJumlah = Integer.parseInt(textJumlah.getText().toString());
                totalJumlah = integerJumlah + 1;
                textJumlah.setText(String.valueOf(totalJumlah));
                totalHarga = integerHarga * totalJumlah;
                textTotalHarga.setText(formatRupiah.format(totalHarga));
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                integerJumlah = Integer.parseInt(textJumlah.getText().toString());
                if(integerJumlah > 1) {
                    totalJumlah = integerJumlah - 1;
                    textJumlah.setText(String.valueOf(totalJumlah));
                    totalHarga = integerHarga * totalJumlah;
                    textTotalHarga.setText(formatRupiah.format(totalHarga));
                }
            }
        });

        buttonBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alamatFix = editAlamat.getText().toString();
                stringCatatan = editCatatan.getText().toString();

                if(alamatFix.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Harap Isi Alamat", Toast.LENGTH_LONG).show();
                }
                else {
                    Random r = new Random();
                    int intInvoice = r.nextInt(999999 - 111111) + 111111;
                    String stringInvoice = "INV" + String.valueOf(intInvoice);
                    editor = sharedPreferences.edit();
                    editor.putString("invoice", stringInvoice);
                    editor.apply();

                    SQLiteDatabase databaseWrite = sqliteHelper.getWritableDatabase();
                    databaseWrite.execSQL("INSERT INTO pesanan(invoice, id_akun, nama_pembeli, " +
                            "nama_produk, harga, jumlah, catatan, alamat, status, tanggal) VALUES('" +
                            stringInvoice + "','" +
                            id_akun + "','" +
                            nama_pembeli + "','" +
                            namaProduk + "','" +
                            stringHarga + "','" +
                            String.valueOf(totalJumlah) + "','" +
                            stringCatatan + "','" +
                            alamatFix + "','" +
                            "Menunggu Pembayaran" + "','" +
                            currentDate + "')");

                    Toast.makeText(getApplicationContext(), "Berhasil Dipesan", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(BeliLangsungActivity.this, BayarActivity.class);
                    startActivity(intent);
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
            nama_pembeli = cursor.getString(1);
            alamat = cursor.getString(5);
        }
    }
}