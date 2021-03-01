package com.fachrizal.niagaelektronik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fachrizal.niagaelektronik.helper.SqliteHelper;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BayarActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 100;
    private ImageView imageView;
    String invoice, stringDateDatabase, stringBatasBayar, telepon, nomorVA,
            stringJumlah, stringHarga;
    Integer intJumlah, intHarga, intTotalHarga, totalHargaKeseluruhan;
    Date dateDatabase, dateBatasBayar;
    Button pickImageButton, simpan_bukti_bayar;
    TextView textBatasBayar, textNomorVA, textTotalHarga;
    SqliteHelper sqliteHelper;
    Cursor cursor;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bayar);

        sharedPreferences = getSharedPreferences("mainsession",
                Context.MODE_PRIVATE);
        sqliteHelper = new SqliteHelper(this);
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

        imageView = findViewById(R.id.image_view);
        pickImageButton = findViewById(R.id.pick_image_button);
        simpan_bukti_bayar = findViewById(R.id.simpan_bukti_bayar);
        textBatasBayar = findViewById(R.id.textBatasBayar);
        textNomorVA = findViewById(R.id.textNomorVA);
        textTotalHarga = findViewById(R.id.textTotalHarga);

        telepon = sharedPreferences.getString("telepon", "");
        invoice = sharedPreferences.getString("invoice","");

        getPesanan();
        textBatasBayar.setText(stringBatasBayar);
        nomorVA = "80777" + telepon;
        textNomorVA.setText(nomorVA);
        textTotalHarga.setText(formatRupiah.format(totalHargaKeseluruhan));

        pickImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
                simpan_bukti_bayar.setVisibility(View.VISIBLE);
            }
        });

        simpan_bukti_bayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BayarActivity.this, RiwayatActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void getPesanan() {
        totalHargaKeseluruhan = 0;

        SQLiteDatabase database = sqliteHelper.getReadableDatabase();
        cursor = database.rawQuery(
                "SELECT * FROM pesanan WHERE invoice ='" + invoice + "' "
                , null
        );
        cursor.moveToFirst();

        int i;
        for (i=0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            stringHarga = cursor.getString(5);
            intHarga = Integer.parseInt(stringHarga);
            stringJumlah = cursor.getString(6);
            intJumlah = Integer.parseInt(stringJumlah);
            intTotalHarga = intHarga * intJumlah;
            totalHargaKeseluruhan = totalHargaKeseluruhan + intTotalHarga;

            stringDateDatabase = cursor.getString(10);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            try {
                dateDatabase = simpleDateFormat.parse(stringDateDatabase);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateDatabase);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            dateBatasBayar = calendar.getTime();
            stringBatasBayar = simpleDateFormat.format(dateBatasBayar);
        }
    }

    private void openGallery() {
        Intent gallery =
                new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            Uri imageUri = data.getData();
            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(imageUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap imageBitmap = BitmapFactory.decodeStream(imageStream);
            imageView.setImageURI(imageUri);
            byte[] imageByte = getBitmapAsByteArray(imageBitmap);

            SQLiteDatabase databaseWrite = sqliteHelper.getWritableDatabase();
            databaseWrite.execSQL(
                    "UPDATE pesanan SET gmbBuktiBayar='" + imageByte + "', status = '" + "Pesanan Dikirim" +"'" +
                            " WHERE invoice = '" + invoice + "'"
            );
        }
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

}