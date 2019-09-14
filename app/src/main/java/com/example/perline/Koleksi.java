package com.example.perline;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;

public class Koleksi extends AppCompatActivity implements ListView.OnItemClickListener {

    private ListView listView;

    private String JSON_STRING;

    String HttpUrl = "http://192.168.5.37/perpustakaan/tampil.php";

    public static final String KEY_EMP_ID_BUKU = "id_buku";
    public static final String KEY_EMP_JUDUL = "judul";
    public static final String KEY_EMP_PENULIS = "penulis";

    public static final String TAG_JSON_ARRAY = "result";
    public static final String TAG_ID_BUKU = "id_buku";
    public static final String TAG_JUDUL = "judul";
    public static final String TAG_PENULIS = "penulis";
    public static final String TAG_PENERBIT = "penerbit";

    public static final String EMP_ID_BUKU = "emp_id_buku";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_koleksi);
        listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(this);
        getJSON();

    }

    private void showEmployee(){

    JSONObject jsonObject = null;
    ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
    try {
        jsonObject = new JSONObject(JSON_STRING);
        JSONArray result = jsonObject.getJSONArray(TAG_JSON_ARRAY);

        for(int i = 0; i<result.length(); i++){
            JSONObject jo = result.getJSONObject(i);
            String id = jo.getString(TAG_ID_BUKU);
            String judul = jo.getString(TAG_JUDUL);

            HashMap<String,String> employees = new HashMap<>();
            employees.put(TAG_ID_BUKU,id);
            employees.put(TAG_JUDUL,judul);
            list.add(employees);
        }

    } catch (JSONException e) {
        e.printStackTrace();
    }

    ListAdapter adapter = new SimpleAdapter(
            Koleksi.this, list, R.layout.list_item,
            new String[]{TAG_ID_BUKU,TAG_JUDUL},
            new int[]{R.id.id_buku, R.id.judul});

    listView.setAdapter(adapter);
}

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Koleksi.this,"Mengambil Data","Mohon Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showEmployee();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(HttpUrl);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, TampilBuku.class);
        HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
        String empIdbuku = map.get(TAG_ID_BUKU).toString();
        intent.putExtra(EMP_ID_BUKU,empIdbuku);
        startActivity(intent);
    }
}
