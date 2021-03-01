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
import android.widget.TextView;
import android.widget.Toast;

import com.fachrizal.niagaelektronik.helper.SqliteHelper;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class PokemonHoohActivity extends AppCompatActivity {
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    SqliteHelper sqliteHelper;
    Cursor cursor;

    TextView textHarga, textSelengkapnya, textNamaProduk, textNamaToko, textnamaKota;
    Button buttonBeliLangsung, buttonKeranjang;
    String stringHarga, stringNamaProduk, stringNamaToko, stringNamaKota, id_akun, nama_pembeli, alamat;
    Integer integerHarga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_hooh);

        sharedPreferences = getSharedPreferences("mainsession", Context.MODE_PRIVATE);

        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

        textHarga = findViewById(R.id.textHarga);
        stringHarga = textHarga.getText().toString();
        integerHarga = Integer.parseInt(stringHarga);
        textHarga.setText(formatRupiah.format(integerHarga));

        textSelengkapnya = findViewById(R.id.textSelengkapnya);
        buttonBeliLangsung = findViewById(R.id.beliLangsungButton);
        buttonKeranjang = findViewById(R.id.keranjangButton);

        textNamaProduk = findViewById(R.id.textNamaProduk);
        stringNamaProduk = textNamaProduk.getText().toString();
        textNamaToko = findViewById(R.id.textNamaToko);
        stringNamaToko = textNamaToko.getText().toString();
        textnamaKota = findViewById(R.id.textNamaKota);
        stringNamaKota = textnamaKota.getText().toString();

        sqliteHelper = new SqliteHelper(this);
        id_akun = sharedPreferences.getString("id_akun","");
        nama_pembeli = sharedPreferences.getString("nama_pembeli","");
        alamat = sharedPreferences.getString("alamat","");
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        textSelengkapnya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = sharedPreferences.edit();
                editor.putString("nama_produk", stringNamaProduk);
                editor.apply();

                Intent intent = new Intent(PokemonHoohActivity.this, DeskripsiLengkapActivity.class);
                startActivity(intent);
            }
        });

        buttonBeliLangsung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = sharedPreferences.edit();
                editor.putString("nama_produk", stringNamaProduk);
                editor.putString("nama_toko", stringNamaToko);
                editor.putString("nama_kota", stringNamaKota);
                editor.putString("harga", stringHarga);
                editor.apply();

                Intent intent = new Intent(PokemonHoohActivity.this, BeliLangsungActivity.class);
                startActivity(intent);
            }
        });

        buttonKeranjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random r = new Random();
                int intInvoice = r.nextInt(999999 - 111111) + 111111;
                String stringInvoice = "INV" + String.valueOf(intInvoice);

                SQLiteDatabase databaseWrite = sqliteHelper.getWritableDatabase();
                databaseWrite.execSQL("INSERT INTO pesanan(invoice, id_akun, nama_pembeli, " +
                        "nama_produk, harga, jumlah, catatan, alamat, status, tanggal) VALUES('" +
                        stringInvoice + "','" +
                        id_akun + "','" +
                        nama_pembeli + "','" +
                        stringNamaProduk + "','" +
                        stringHarga + "','" +
                        "1" + "','" +
                        "" + "','" +
                        alamat + "','" +
                        "Keranjang" + "','" +
                        currentDate + "')");

                Toast.makeText(getApplicationContext(), "Berhasil Menambah Keranjang",
                        Toast.LENGTH_LONG).show();

                Intent intent = new Intent(PokemonHoohActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}