package com.fachrizal.niagaelektronik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InputKodeActivity extends AppCompatActivity {
    EditText editKode;
    Button buttonInputKode;
    String kode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_kode);
        editKode = findViewById(R.id.editKode);
        buttonInputKode = findViewById(R.id.buttonInputKode);
        buttonInputKode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kode = editKode.getText().toString();
                if(kode.equals("01050105")) {
                    Intent intent = new Intent(InputKodeActivity.this, EditProfileActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(InputKodeActivity.this, "Kode Salah", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}