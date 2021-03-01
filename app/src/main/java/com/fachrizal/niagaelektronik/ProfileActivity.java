package com.fachrizal.niagaelektronik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.fachrizal.niagaelektronik.helper.SqliteHelper;


public class ProfileActivity extends AppCompatActivity {
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    SqliteHelper sqliteHelper;
    Cursor cursor;
    TextView textNamaDB, textEmailDB, textTeleponDB, textAlamatDB;
    Button buttonEditProfile;
    String id_akun, nama, email, alamat, telepon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sharedPreferences = getSharedPreferences("mainsession", Context.MODE_PRIVATE);
        sqliteHelper = new SqliteHelper(this);

        textNamaDB = findViewById(R.id.textNamaDB);
        textEmailDB = findViewById(R.id.textEmailDB);
        textTeleponDB = findViewById(R.id.textTeleponDB);
        textAlamatDB = findViewById(R.id.textAlamatDB);
        buttonEditProfile = findViewById(R.id.buttonEditProfile);

        id_akun = sharedPreferences.getString("id_akun", "");
        getAkun();
        textNamaDB.setText(nama);
        textEmailDB.setText(email);
        textTeleponDB.setText(telepon);
        textAlamatDB.setText(alamat);

        buttonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.button_home:
                        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.button_riwayat:
                        intent = new Intent(ProfileActivity.this, RiwayatActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.button_akun:
                        Toast.makeText(ProfileActivity.this, "Akun", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
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
            nama = cursor.getString(1);
            email = cursor.getString(2);
            telepon = cursor.getString(4);
            alamat = cursor.getString(5);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.deleteButton) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Hapus");
            builder.setMessage("Yakin Hapus Akun?");
            builder.setPositiveButton("Iya",
                    (dialog, which) -> {
                        SQLiteDatabase database = sqliteHelper.getWritableDatabase();
                        database.execSQL("DELETE FROM akun WHERE id_akun = '" + id_akun + "'");
                        Toast.makeText(getApplicationContext(), "Akun berhasil dihapus",
                                Toast.LENGTH_LONG).show();
                        dialog.dismiss();

                        editor = sharedPreferences.edit();
                        editor.putString("id_akun", "");
                        editor.putString("nama_pembeli", "");
                        editor.putString("telepon", "");
                        editor.putString("alamat", "");
                        editor.putString("statusLogin", "");
                        editor.apply();

                        Intent intent = new Intent(ProfileActivity.this, WelcomeActivity.class);
                        startActivity(intent);
                        finish();
                    });
            builder.setNegativeButton("Tidak",
                    (dialog, which) -> dialog.dismiss());
            AlertDialog alert1 = builder.create();
            alert1.show();
        }
        return super.onOptionsItemSelected(item);
    }
}