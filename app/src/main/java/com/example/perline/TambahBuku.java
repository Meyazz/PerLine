package com.example.perline;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class TambahBuku extends AppCompatActivity {

    EditText edtid, edtjudul, edtpenulis, edtpenerbit;

    Button tambah;

    RequestQueue requestQueue;

    String txtid, txtjudul, txtpenulis, txtpenerbit;

    ProgressDialog progressDialog;

    String HttpUrl = "http://192.168.5.37/perpustakaan/buku.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_buku);
        edtid = (EditText) findViewById(R.id.id_buku);
        edtjudul = (EditText) findViewById(R.id.judul);
        edtpenulis = (EditText) findViewById(R.id.penulis);
        edtpenerbit = (EditText) findViewById(R.id.penerbit);

        tambah = (Button) findViewById(R.id.tambah);

        requestQueue = Volley.newRequestQueue(TambahBuku.this);

        progressDialog = new ProgressDialog(TambahBuku.this);

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.setMessage("Please Wait, We are Inserting Your Data");
                progressDialog.show();

                GetValue();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpUrl,
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String ServerResponse) {

                                progressDialog.dismiss();
                                Toast.makeText(TambahBuku.this, ServerResponse, Toast.LENGTH_LONG).show();
                            }
                        },

                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {

                                // Hiding the progress dialog after all task complete.
                                progressDialog.dismiss();

                                // Showing error message if something goes wrong.
                                Toast.makeText(TambahBuku.this, volleyError.toString(), Toast.LENGTH_LONG).show();

                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {

                        // Creating Map String Params.
                        Map<String, String> params = new HashMap<String, String>();

                        // Adding All values to Params.
                        params.put("id_buku", txtid);
                        params.put("judul", txtjudul);
                        params.put("penulis", txtpenulis);
                        params.put("penerbit", txtpenerbit);

                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(TambahBuku.this);

                requestQueue.add(stringRequest);

            }

        });

    }

    public void GetValue() {

        txtid = edtid.getText().toString();
        txtjudul = edtjudul.getText().toString();
        txtpenulis = edtpenulis.getText().toString();
        txtpenerbit = edtpenerbit.getText().toString();

    }
}
