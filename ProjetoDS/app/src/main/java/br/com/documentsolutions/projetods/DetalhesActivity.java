package br.com.documentsolutions.projetods;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import static br.com.documentsolutions.projetods.LojasActivity.EXTRA_ID;
import static br.com.documentsolutions.projetods.LojasActivity.EXTRA_NOME;

public class DetalhesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);

        Intent intent = getIntent();
        String id = intent.getStringExtra(EXTRA_ID);
        String nome = intent.getStringExtra(EXTRA_NOME);

        TextView txtID = findViewById(R.id.txtID);
        TextView txtNome = findViewById(R.id.txtNome);

        txtID.setText(id);
        txtNome.setText(nome);
    }
}
