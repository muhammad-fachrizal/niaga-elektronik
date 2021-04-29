package com.fachrizal.niagaelektronik;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fachrizal.niagaelektronik.helper.SqliteHelper;

public class RegisterActivity extends AppCompatActivity {

    EditText editRegisterNama, editRegisterTelepon, editRegisterEmail, editRegisterPassword;
    TextView textPindahLogin;
    String registerNama, registerTelepon, registerEmail, registerPassword;
    Button buttonRegister;

    SqliteHelper sqliteHelper;
    Cursor cursor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editRegisterNama = findViewById(R.id.editRegisterNama);
        editRegisterTelepon = findViewById(R.id.editRegisterTelepon);
        editRegisterEmail = findViewById(R.id.editRegisterEmail);
        editRegisterPassword = findViewById(R.id.editRegisterPassword);

        textPindahLogin = findViewById(R.id.textPindahLogin);

        buttonRegister = findViewById(R.id.buttonRegister);

        sqliteHelper = new SqliteHelper(this);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                registerNama = editRegisterNama.getText().toString();
                registerTelepon = editRegisterTelepon.getText().toString();
                registerEmail = editRegisterEmail.getText().toString();
                registerPassword = editRegisterPassword.getText().toString();

                if(registerNama.isEmpty() || registerTelepon.isEmpty() ||
                        registerEmail.isEmpty() || registerPassword.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Harap Isi Semua Data",
                            Toast.LENGTH_LONG).show();
                }

                else {
                    SQLiteDatabase databaseRead = sqliteHelper.getReadableDatabase();
                    cursor = databaseRead.rawQuery(
                            "SELECT * FROM akun WHERE email ='" + registerEmail + "' "
                            , null
                    );
                    cursor.moveToFirst();

                    if(cursor.getCount()>0) {
                        Toast.makeText(getApplicationContext(), "Email Sudah Digunakan",
                                Toast.LENGTH_LONG).show();
                    }

                    else {
                        SQLiteDatabase databaseWrite = sqliteHelper.getWritableDatabase();
                        databaseWrite.execSQL("INSERT INTO akun(nama, email, password, telepon) VALUES('" +
                                registerNama + "','" +
                                registerEmail + "','" +
                                registerPassword + "','" +
                                registerTelepon + "')");
                        Toast.makeText(getApplicationContext(), "Akun berhasil dibuat", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });

        textPindahLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

}