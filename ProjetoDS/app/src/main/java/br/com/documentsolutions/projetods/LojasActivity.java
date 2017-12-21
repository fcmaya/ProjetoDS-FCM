package br.com.documentsolutions.projetods;

import android.app.ProgressDialog;
import android.app.VoiceInteractor;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.List;


public class LojasActivity extends AppCompatActivity implements MyAdpter.OnItemClickListener {

    private static final String URL_DATA = "https://api.myjson.com/bins/hvcbf";
    public static final String EXTRA_ID = "ID";
    public static final String EXTRA_NOME = "NOME";
    private RecyclerView recyclerView;
    //private RecyclerView.Adapter adapter;
    private MyAdpter adapter;
    private List<ListItem> listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lojas);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItems = new ArrayList<>();

        loadRecyclerViewData();

     }

     private void loadRecyclerViewData(){
         final ProgressDialog progressDialog = new ProgressDialog(this);
         progressDialog.setMessage("Lendo os dados...");
         progressDialog.show();

         StringRequest stringRequest = new StringRequest(Request.Method.GET,
                 URL_DATA,
                 new Response.Listener<String>() {
                     @Override
                     public void onResponse(String s) {
                         progressDialog.dismiss();
                         try {
                             JSONObject jsonObject = new JSONObject(s);
                             JSONArray array = jsonObject.getJSONArray("lojas");

                             for(int i = 0; i < array.length(); i++){
                                JSONObject o = array.getJSONObject(i);
                                ListItem item = new ListItem(
                                        o.getString("id"),
                                        o.getString("nome")
                                );
                                listItems.add(item);
                             }

                             adapter = new MyAdpter(listItems, getApplicationContext());
                             recyclerView.setAdapter(adapter);
                             adapter.setOnItemClickListener(LojasActivity.this);

                         } catch (JSONException e) {
                             e.printStackTrace();
                         }
                     }
                 },
                 new Response.ErrorListener() {
                     @Override
                     public void onErrorResponse(VolleyError error) {
                         progressDialog.dismiss();
                         Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                     }
                 });

         RequestQueue requestQueue = Volley.newRequestQueue(this);
         requestQueue.add(stringRequest);
     }


    @Override
    public void onItemClick(int position) {
        Intent detalhesLoja = new Intent(this, DetalhesActivity.class);
        ListItem itemClicado = listItems.get(position);

        detalhesLoja.putExtra(EXTRA_ID, itemClicado.getID());
        detalhesLoja.putExtra(EXTRA_NOME, itemClicado.getNome());

        startActivity(detalhesLoja);
    }
}
