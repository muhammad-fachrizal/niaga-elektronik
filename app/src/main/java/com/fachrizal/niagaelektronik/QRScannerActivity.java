package com.fachrizal.niagaelektronik;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.Result;
import com.fachrizal.niagaelektronik.helper.SqliteHelper;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class QRScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    SqliteHelper sqliteHelper;
    Cursor cursor;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    String invoice;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);

        sqliteHelper = new SqliteHelper(this);
        sharedPreferences = getSharedPreferences("mainsession", Context.MODE_PRIVATE);
        invoice = sharedPreferences.getString("invoice","");
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        Log.v("TAG", rawResult.getText());
        Log.v("TAG", rawResult.getBarcodeFormat().toString());
        String hasilScanQR = rawResult.getText();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        if(hasilScanQR.equals("Paket Niaga-Elektronik")) {
            builder.setMessage(rawResult.getText());
            builder.setPositiveButton("Selesai",
                    (dialog, which) -> {
                        SQLiteDatabase databaseWrite = sqliteHelper.getWritableDatabase();
                        databaseWrite.execSQL(
                                "UPDATE pesanan SET status = '" + "Pesanan Diterima" + "'" +
                                        " WHERE invoice = '" + invoice + "'"
                        );
                        dialog.dismiss();
                        Intent intent = new Intent(QRScannerActivity.this, RiwayatActivity.class);
                        startActivity(intent);
                        finish();
                        });
            Toast.makeText(getApplicationContext(), "Paket Diterima",
                    Toast.LENGTH_LONG).show();
        }
        else {
            builder.setMessage("Kode QR Salah");
            builder.setNegativeButton("Ulang",
                    (dialog, which) -> dialog.dismiss());
            Toast.makeText(getApplicationContext(), "Kode QR Salah",
                    Toast.LENGTH_LONG).show();
        }

        AlertDialog alert1 = builder.create();
        alert1.show();

        mScannerView.resumeCameraPreview(this);
    }
}