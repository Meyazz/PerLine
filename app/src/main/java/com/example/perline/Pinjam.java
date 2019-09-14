package com.example.perline;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Pinjam extends AppCompatActivity {

    EditText txtpinjam, txtidbuku, txtbuku, txttglpinjam, txttglbalik;

    Button pinjam;

    RequestQueue requestQueue;

    String peminjaman, idbuku, buku, tgl_pinjam, tgl_balik;

    ProgressDialog progressDialog;

    String HttpUrl = "http://192.168.5.37/perpustakaan/pinjam.php";
    String Http = "http://192.168.5.37/perpustakaan/tampil_buku.php?id_buku=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinjam);

        Intent intent = getIntent();

        idbuku = intent.getStringExtra(Koleksi.EMP_ID_BUKU);

        txtpinjam = (EditText) findViewById(R.id.pinjam);
        txtidbuku = (EditText) findViewById(R.id.txtidbuku);
        txtbuku = (EditText) findViewById(R.id.txtjudul);
        txttglpinjam = (EditText) findViewById(R.id.txttglpinjam);
        txttglbalik = (EditText) findViewById(R.id.txttglkembali);

        pinjam = (Button) findViewById(R.id.pinjambuku);

        txtidbuku.setText(idbuku);
        txtbuku.setText(buku);

        requestQueue = Volley.newRequestQueue(Pinjam.this);

        progressDialog = new ProgressDialog(Pinjam.this);

        getEmployee();

    }

        private void getEmployee(){
            class GetEmployee extends AsyncTask<Void,Void,String> {
                ProgressDialog loading;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading = ProgressDialog.show(Pinjam.this, "Fetching...", "Wait...", false, false);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    loading.dismiss();
                    showEmployee(s);
                }

                @Override
                protected String doInBackground(Void... params) {
                    RequestHandler rh = new RequestHandler();
                    String y = rh.sendGetRequestParam(Http, idbuku);
                    return y;
                }
            }
            GetEmployee ge = new GetEmployee();
            ge.execute();
        }

        private void showEmployee(String json){
            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONArray result = jsonObject.getJSONArray(Koleksi.TAG_JSON_ARRAY);
                JSONObject c = result.getJSONObject(0);
                String idnya = c.getString(Koleksi.TAG_ID_BUKU);
                String judulnya = c.getString(Koleksi.TAG_JUDUL);

                txtidbuku.setText(idnya);
                txtbuku.setText(judulnya);


            } catch (JSONException e) {
                e.printStackTrace();
            }


        pinjam.setOnClickListener(new View.OnClickListener() {
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
                                Toast.makeText(Pinjam.this, ServerResponse, Toast.LENGTH_LONG).show();
                            }
                        },

                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {

                                // Hiding the progress dialog after all task complete.
                                progressDialog.dismiss();

                                // Showing error message if something goes wrong.
                                Toast.makeText(Pinjam.this, volleyError.toString(), Toast.LENGTH_LONG).show();

                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {

                        // Creating Map String Params.
                        Map<String, String> params = new HashMap<String, String>();

                        // Adding All values to Params.
                        params.put("id_pinjam", peminjaman);
                        params.put("id_buku" , idbuku);
                        params.put("buku", buku);
                        params.put("tgl_pinjam", tgl_pinjam);
                        params.put("tgl_balik", tgl_balik);

                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(Pinjam.this);

                requestQueue.add(stringRequest);

            }

        });

    }

    public void GetValue() {

        peminjaman = txtpinjam.getText().toString();
        idbuku = txtidbuku.getText().toString();
        buku = txtbuku.getText().toString();
        tgl_pinjam = txttglpinjam.getText().toString();
        tgl_balik = txttglbalik.getText().toString();

    }
}
