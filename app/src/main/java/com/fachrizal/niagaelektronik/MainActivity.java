package com.fachrizal.niagaelektronik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshBarang;
    CardView novel, pokemon_hooh, pokemon_metagross, aksesoris_hp;
    EditText edtCari;
    Button btnCari;
    String cari;

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("mainsession", Context.MODE_PRIVATE);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.button_home:
                        Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.button_riwayat:
                        Intent intent = new Intent(MainActivity.this, RiwayatActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.button_akun:
                        intent = new Intent(MainActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });

        swipeRefreshBarang = findViewById(R.id.swipeRefreshBarang);
        edtCari = findViewById(R.id.edtCari);
        btnCari = findViewById(R.id.btnCari);
        novel = findViewById(R.id.novel);
        pokemon_hooh = findViewById(R.id.pokemon_hooh);
        pokemon_metagross = findViewById(R.id.pokemon_metagross);
        aksesoris_hp = findViewById(R.id.aksesoris_hp);

        swipeRefreshBarang.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                novel.setVisibility(View.VISIBLE);
                pokemon_hooh.setVisibility(View.VISIBLE);
                pokemon_metagross.setVisibility(View.VISIBLE);
                aksesoris_hp.setVisibility(View.VISIBLE);
                swipeRefreshBarang.setRefreshing(false);
            }
        });

        btnCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cari = edtCari.getText().toString().toLowerCase();
                cariBarang();
            }
        });

        novel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NovelActivity.class);
                startActivity(intent);
            }
        });

        pokemon_hooh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PokemonHoohActivity.class);
                startActivity(intent);
            }
        });

        pokemon_metagross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PokemonMetagrossActivity.class);
                startActivity(intent);
            }
        });

        aksesoris_hp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AksesorisHpActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.cartButton) {
            Intent intent = new Intent(MainActivity.this, KeranjangActivity.class);
            startActivity(intent);
        }

        if (id == R.id.logout) {
            editor = sharedPreferences.edit();
            editor.putString("statusLogin", "");
            editor.putString("id_akun", "");
            editor.putString("nama_pembeli", "");
            editor.putString("alamat", "");
            editor.apply();
            Intent dbmanager = new Intent(MainActivity.this, WelcomeActivity.class);
            startActivity(dbmanager);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void cariBarang() {
        if("Mushoku Tensei: Jobless Reincarnation, Vol.01 [Light Novel EN] Bekas".toLowerCase().contains(cari)) {
            novel.setVisibility(View.VISIBLE);
            pokemon_hooh.setVisibility(View.INVISIBLE);
            pokemon_metagross.setVisibility(View.INVISIBLE);
            aksesoris_hp.setVisibility(View.INVISIBLE);
        }

        if(cari.equals("pokemon")  || cari.equals("tcg") || cari.equals("gx")) {
            novel.setVisibility(View.INVISIBLE);
            pokemon_hooh.setVisibility(View.VISIBLE);
            pokemon_metagross.setVisibility(View.VISIBLE);
            aksesoris_hp.setVisibility(View.INVISIBLE);
        }

        if("Ho-oh Lycanroc Pembersih Arena".toLowerCase().contains(cari)) {
            novel.setVisibility(View.INVISIBLE);
            pokemon_hooh.setVisibility(View.VISIBLE);
            pokemon_metagross.setVisibility(View.INVISIBLE);
            aksesoris_hp.setVisibility(View.INVISIBLE);
        }

        if("Metagross Darkrai".toLowerCase().contains(cari)) {
            novel.setVisibility(View.INVISIBLE);
            pokemon_hooh.setVisibility(View.INVISIBLE);
            pokemon_metagross.setVisibility(View.VISIBLE);
            aksesoris_hp.setVisibility(View.INVISIBLE);
        }

        if("Aksesoris Sony Xperia X Performance".toLowerCase().contains(cari)) {
            novel.setVisibility(View.INVISIBLE);
            pokemon_hooh.setVisibility(View.INVISIBLE);
            pokemon_metagross.setVisibility(View.INVISIBLE);
            aksesoris_hp.setVisibility(View.VISIBLE);
        }
    }
}