package com.example.perline;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void Tambah(View view) {
        Intent intent = new Intent(this, TambahBuku.class);
        startActivity(intent);
    }

    public void Koleksi(View view) {
        Intent intent = new Intent(this, Koleksi.class);
        startActivity(intent);
    }
}
