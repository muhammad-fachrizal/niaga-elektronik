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
import android.widget.TextView;
import android.widget.Toast;

import com.fachrizal.niagaelektronik.helper.SqliteHelper;

public class LoginActivity extends AppCompatActivity {

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;

    EditText editLoginEmail, editLoginPassword;
    TextView textPindahRegister, textLupaSandi;
    Button buttonLogin;

    SqliteHelper sqliteHelper;
    Cursor cursor;

    String LoginEmail, LoginPassword, id_akun, nama_pembeli, telepon, alamat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("mainsession", Context.MODE_PRIVATE);

        editLoginEmail = findViewById(R.id.editLoginEmail);
        editLoginPassword = findViewById(R.id.editLoginPassword);

        buttonLogin = findViewById(R.id.buttonLogin);

        textPindahRegister = findViewById(R.id.textPindahRegister);
        textLupaSandi = findViewById(R.id.textLupaSandi);

        sqliteHelper = new SqliteHelper(this);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginEmail = editLoginEmail.getText().toString();
                LoginPassword = editLoginPassword.getText().toString();

                if(LoginEmail.isEmpty() || LoginPassword.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Isi data dengan benar",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    SQLiteDatabase database = sqliteHelper.getReadableDatabase();
                    cursor = database.rawQuery(
                            "SELECT * FROM akun WHERE email ='" + LoginEmail + "'"
                            , null
                    );
                    cursor.moveToFirst();

                    if(cursor.getCount()==1) {
                        if(LoginPassword.equals(cursor.getString(3))) {
                            id_akun = cursor.getString(0);
                            nama_pembeli = cursor.getString(1);
                            telepon = cursor.getString(4);
                            alamat = cursor.getString(5);

                            editor = sharedPreferences.edit();
                            editor.putString("statusLogin", "sudah login");
                            editor.putString("id_akun", id_akun);
                            editor.putString("nama_pembeli", nama_pembeli);
                            editor.putString("telepon", telepon);
                            editor.putString("alamat", alamat);
                            editor.apply();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Email atau Password Salah",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Email atau Password Salah",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        textPindahRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        textLupaSandi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, LupaPasswordActivity.class);
                startActivity(intent);
            }
        });

    }
}