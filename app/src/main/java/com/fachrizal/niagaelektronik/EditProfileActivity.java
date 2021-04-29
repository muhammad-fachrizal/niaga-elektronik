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
import android.widget.Toast;
import com.fachrizal.niagaelektronik.helper.SqliteHelper;

public class EditProfileActivity extends AppCompatActivity {
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    SqliteHelper sqliteHelper;
    Cursor cursor;
    EditText editNama, editEmail, editPassword, editTelepon, editAlamat;
    Button buttonSimpan;
    String id_akun, nama, email, password, telepon, alamat, statusLogin,
            updateNama, updateEmail, updatePassword, updateTelepon, updateAlamat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        sharedPreferences = getSharedPreferences("mainsession", Context.MODE_PRIVATE);
        sqliteHelper = new SqliteHelper(this);

        editNama = findViewById(R.id.editNama);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        editTelepon = findViewById(R.id.editTelepon);
        editAlamat = findViewById(R.id.editAlamat);
        buttonSimpan = findViewById(R.id.buttonSimpan);
        id_akun = sharedPreferences.getString("id_akun", "");
        statusLogin = sharedPreferences.getString("statusLogin", "");

        getAkun();
        editNama.setText(nama);
        editEmail.setText(email);
        editPassword.setText(password);
        editTelepon.setText(telepon);
        editAlamat.setText(alamat);

        buttonSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNama = editNama.getText().toString();
                updateEmail = editEmail.getText().toString();
                updatePassword = editPassword.getText().toString();
                updateTelepon = editTelepon.getText().toString();
                updateAlamat = editAlamat.getText().toString();

                if(updateNama.isEmpty() || updateEmail.isEmpty() ||
                        updatePassword.isEmpty() ||
                        updateTelepon.isEmpty() || updateAlamat.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Harap Isi Semua Data",
                            Toast.LENGTH_LONG).show();
                }

                else {
                    SQLiteDatabase databaseWrite = sqliteHelper.getWritableDatabase();
                    databaseWrite.execSQL(
                            "UPDATE akun SET nama ='" + updateNama + "', " +
                                    "email ='" + updateEmail + "', " +
                                    "password ='" + updatePassword + "', " +
                                    "telepon ='" + updateTelepon + "', " +
                                    "alamat = '" + updateAlamat +"'" +
                                    " WHERE id_akun = '" + id_akun + "'"
                    );
                    editor = sharedPreferences.edit();
                    editor.putString("id_akun", id_akun);
                    editor.putString("nama_pembeli", nama);
                    editor.putString("telepon", telepon);
                    editor.putString("alamat", alamat);
                    editor.apply();

                    Toast.makeText(getApplicationContext(), "Data berhasil diubah", Toast.LENGTH_LONG).show();

                    if(statusLogin.equals("lupa password")) {
                        Intent intent = new Intent(EditProfileActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        finish();
                    }
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
            nama = cursor.getString(1);
            email = cursor.getString(2);
            password = cursor.getString(3);
            telepon = cursor.getString(4);
            alamat = cursor.getString(5);
        }
    }
}