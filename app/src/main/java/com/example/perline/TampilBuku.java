package com.example.perline;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TampilBuku extends AppCompatActivity  {

    private EditText idbuku, judull, penuliss, penerbitt, pinjam, tgl_pinjam, tgl_balik, btn1, btn2;

    private Button btnpinjam;

    private String id, judul, penulis, penerbit, idpinjam, pinjambuku, balik;

    TextView tanggalpinjam, tanggalbalik;

    Calendar c;
    DatePickerDialog datePickerDialog;

    RequestQueue requestQueue;

    ProgressDialog progressDialog;

    String HttpUrl = "http://192.168.5.37/perpustakaan/tampil_buku.php?id_buku=";
    String Http = "http://192.168.5.37/perpustakaan/pinjam.php";

    /*private void showDateDialog(){
        Calendar newCalender = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                tgl_pinjam.setText(dateFormatter.format(newDate.getTime()));
                tgl_balik.setText(dateFormatter.format(newDate.getTime()));

            }
        }, newCalender.get(Calendar.YEAR), newCalender.get(Calendar.MONTH), newCalender.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();

    }*/

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_buku);

        tgl_pinjam = (EditText) findViewById(R.id.txttglpinjam);
        tgl_balik = (EditText) findViewById(R.id.txttglkembali);
        btn1 = (EditText) findViewById(R.id.txttglpinjam);
        btn2 = (EditText) findViewById(R.id.txttglkembali);

        btn1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        c = Calendar.getInstance();
                                        int day1 = c.get(Calendar.DAY_OF_MONTH);
                                        int month1 = c.get(Calendar.MONTH);
                                        int year1 = c.get(Calendar.YEAR);

                                        datePickerDialog = new DatePickerDialog(TampilBuku.this, new DatePickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                                                tgl_pinjam.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                                            }
                                        }, day1, month1, year1);
                                        datePickerDialog.show();
                                    }
                                });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                c = Calendar.getInstance();
                int day1 = c.get(Calendar.DAY_OF_MONTH);
                int month1 = c.get(Calendar.MONTH);
                int year1 = c.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(TampilBuku.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        tgl_balik.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, day1, month1, year1);
                datePickerDialog.show();
            }
        });

                /*datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMont, int mDay) {

                        tanggalpinjam.setText(mDay + "/" + (mMont+1) + "/" + mYear);

                    }
                }, day, month, year);

                datePickerDialog.show();
            }
        });*/

        Intent intent = getIntent();

        id = intent.getStringExtra(Koleksi.EMP_ID_BUKU);

        idbuku = (EditText) findViewById(R.id.txtidbuku);
        judull = (EditText) findViewById(R.id.txtjudul);
        penuliss = (EditText) findViewById(R.id.txtpenulis);
        penerbitt = (EditText) findViewById(R.id.txtpenerbit);
        pinjam = (EditText) findViewById(R.id.idpinjam);

        btnpinjam = (Button) findViewById(R.id.btnpinjam);


        idbuku.setText(id);

        requestQueue = Volley.newRequestQueue(TampilBuku.this);

        progressDialog = new ProgressDialog(TampilBuku.this);

        getEmployee();

    }

    private void getEmployee(){
        class GetEmployee extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(TampilBuku.this,"Fetching...","Wait...",false,false);
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
                String s = rh.sendGetRequestParam(HttpUrl, id);
                return s;
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
            String judulnya = c.getString(Koleksi.TAG_JUDUL);
            String penulisnya = c.getString(Koleksi.TAG_PENULIS);
            String penerbitnya = c.getString(Koleksi.TAG_PENERBIT);

            judull.setText(judulnya);
            penuliss.setText(penulisnya);
            penerbitt.setText(penerbitnya);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        btnpinjam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.setMessage("Please Wait, We are Inserting Your Data");
                progressDialog.show();

                GetValue();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, Http,
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String ServerResponse) {

                                progressDialog.dismiss();
                                Toast.makeText(TampilBuku.this, ServerResponse, Toast.LENGTH_LONG).show();
                            }
                        },

                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {

                                // Hiding the progress dialog after all task complete.
                                progressDialog.dismiss();

                                // Showing error message if something goes wrong.
                                Toast.makeText(TampilBuku.this, volleyError.toString(), Toast.LENGTH_LONG).show();

                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {

                        // Creating Map String Params.
                        Map<String, String> params = new HashMap<String, String>();

                        // Adding All values to Params.
                        params.put("id_buku" , id);
                        params.put("buku", judul);
                        params.put("penulis", penulis);
                        params.put("penerbit", penerbit);
                        params.put("id_pinjam", idpinjam);
                        params.put("tgl_pinjam", pinjambuku);
                        params.put("tgl_balik", balik);

                        return params;
                    }
                };


                RequestQueue requestQueue = Volley.newRequestQueue(TampilBuku.this);

                requestQueue.add(stringRequest);

            }

        });

    }

    public void GetValue() {

        id = idbuku.getText().toString();
        judul = judull.getText().toString();
        penulis = penuliss.getText().toString();
        penerbit = penerbitt.getText().toString();
        idpinjam = pinjam.getText().toString();
        pinjambuku = tgl_pinjam.getText().toString();
        balik = tgl_balik.getText().toString();

    }
}
