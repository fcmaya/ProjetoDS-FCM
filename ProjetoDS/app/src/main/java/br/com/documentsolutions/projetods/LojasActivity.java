package br.com.documentsolutions.projetods;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.com.documentsolutions.projetods.objetos.Loja;


public class LojasActivity extends AppCompatActivity implements MyAdpter.OnItemClickListener {

    private static final String URL_DATA = "https://api.myjson.com/bins/hvcbf";
    public static final String EXTRA_ID = "ID";
    public static final String EXTRA_NOME = "NOME";
    public static final String EXTRA_LOJA = "LOJA";
    private RecyclerView recyclerView;
    private MyAdpter adapter;
    private List<ListItem> listItems;
    private Loja loja;
    private ArrayList<Loja> arrayLojas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lojas);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        criarPastasApp();

        listItems = new ArrayList<>();
        arrayLojas = new ArrayList<>();

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

                                loja = new Loja();
                                loja.setId(Long.valueOf(o.getString("id")));
                                loja.setNome(o.getString("nome"));

                                JSONObject p = o.getJSONObject("endereco");

                                loja.setLogradouro(p.getString("logradouro"));
                                loja.setBairro(p.getString("bairro"));
                                loja.setComplemento(p.getString("complemento"));
                                loja.setNumero(p.getString("numero"));

                                loja.setTelefone(o.getString("telefone"));

                                Log.v("ObjetoLoja", loja.getLogradouro().toString());

                                arrayLojas.add(i, loja);
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

        Loja lojaDetalhe = arrayLojas.get(position);
        Toast.makeText(this, "Loja: " + lojaDetalhe.getNome().toString(), Toast.LENGTH_SHORT).show();

        detalhesLoja.putExtra(EXTRA_LOJA, lojaDetalhe);
        startActivity(detalhesLoja);
    }

    private void criarPastasApp() {

        String state = Environment.getExternalStorageState();
        Log.d("Media State", state);

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File appDirectory = new File(
                    Environment.getExternalStorageDirectory().getAbsolutePath() + "/ProjetoDS/");

            Log.d("appDirectroyExist", appDirectory.exists() + "");
            if (!appDirectory.exists())
                Log.d("appDir created: ", appDirectory.mkdir() + "");

            File dbDirectory = new File(
                    Environment.getExternalStorageDirectory().getAbsolutePath() + "/ProjetoDS/Camera/");

            Log.d("dbDirectroyExist", dbDirectory.exists() + "");
            if (!dbDirectory.exists())
                Log.d("MissaoDir created: ", dbDirectory.mkdirs() + "");


            File themesDirectory = new File(
                    Environment.getExternalStorageDirectory().getAbsolutePath() + "/ProjetoDS/Galeria/");
            Log.d("backupDirectroyExist", themesDirectory.exists() + "");
            if (!themesDirectory.exists())
                Log.d("backupDir created: ", themesDirectory.mkdirs() + "");

        }
    }

}
