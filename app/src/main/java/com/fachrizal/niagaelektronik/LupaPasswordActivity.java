package com.fachrizal.niagaelektronik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fachrizal.niagaelektronik.helper.SqliteHelper;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class LupaPasswordActivity extends AppCompatActivity {
    EditText editEmail;
    Button buttonKirimKode, buttonInputKode;
    String email, subject, message, id_akun;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    SqliteHelper sqliteHelper;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_password);

        sharedPreferences = getSharedPreferences("mainsession", Context.MODE_PRIVATE);
        sqliteHelper = new SqliteHelper(this);

        editEmail = findViewById(R.id.editEmail);
        buttonKirimKode = findViewById(R.id.buttonKirimKode);
        buttonInputKode = findViewById(R.id.buttonInputKode);

        buttonKirimKode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editEmail.getText().toString();
                getAkun();
            }
        });
        buttonInputKode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LupaPasswordActivity.this, InputKodeActivity.class);
                startActivity(intent);
            }
        });
    }

    public void getAkun() {
        SQLiteDatabase database = sqliteHelper.getReadableDatabase();
        cursor = database.rawQuery(
                "SELECT * FROM akun WHERE email ='" + email + "' "
                , null
        );
        cursor.moveToFirst();

        if(cursor.getCount()==1) {
            id_akun = cursor.getString(0);
            email = cursor.getString(2);
            subject = "Ubah Password Niaga-Elektronik";
            message = "Masukkan kode 01050105 pada aplikasi untuk mengubah password";
            SendMail sm = new SendMail(this, email, subject, message);
            sm.execute();

            editor = sharedPreferences.edit();
            editor.putString("id_akun", id_akun);
            editor.putString("email", email);
            editor.putString("statusLogin", "lupa password");
            editor.apply();
        }
        else {
            Toast.makeText(LupaPasswordActivity.this, "Email Tidak Terdaftar", Toast.LENGTH_SHORT).show();
        }
    }

    public class SendMail extends AsyncTask<Void, Void, Void> {

        private Context context;
        private Session session;

        private String email;
        private String subject;
        private String message;

        public SendMail(Context context, String email, String subject, String message) {
            this.context = context;
            this.email = email;
            this.subject = subject;
            this.message = message;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(context, "Periksa Email Anda", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");

            session = Session.getDefaultInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication("mailwebayashi@gmail.com", "kertaspulpen1");
                        }
                    });

            try {
                MimeMessage mm = new MimeMessage(session);
                mm.setFrom(new InternetAddress("mailwebayashi@gmail.com"));
                mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
                mm.setSubject(subject);
                mm.setText(message);
                Transport.send(mm);

            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}