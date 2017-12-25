package br.com.documentsolutions.projetods;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import br.com.documentsolutions.projetods.objetos.Loja;
import br.com.documentsolutions.projetods.util.BitmapUtil;

import static br.com.documentsolutions.projetods.LojasActivity.EXTRA_NOME;

public class DetalhesActivity extends AppCompatActivity {

    //private TextView txtID;
    private TextView txtNome;
    private TextView txtLogradouro;
    private TextView txtBairro;
    private TextView txtComplemento;
    private TextView txtNumero;
    private TextView txtTelefone;
    private AlertDialog alerta;
    private ImageView imageView;
    public static final int REQUEST_GALERIA = 123;
    public static final int REQUEST_CAMERA = 456;
    public static final String EXTRA_LOJA = "LOJA";

    // Storage Permissions
    private final static int REQUEST_CAMERA_PERMISSION = 200;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);

        txtNome = findViewById(R.id.txtNome);
        txtLogradouro = findViewById(R.id.txtLogradouro);
        txtBairro = findViewById(R.id.txtBairro);
        txtComplemento = findViewById(R.id.txtComplemento);
        txtNumero = findViewById(R.id.txtNumero);
        txtTelefone = findViewById(R.id.txtTelefone);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(EXTRA_LOJA)){
            Loja loja = (Loja) bundle.getSerializable(EXTRA_LOJA);
            txtNome.setText(loja.getNome());
            txtLogradouro.setText(loja.getLogradouro());
            txtBairro.setText(loja.getBairro());
            txtComplemento.setText(loja.getComplemento());
            txtNumero.setText(loja.getNumero());
            txtTelefone.setText(loja.getTelefone());
        }

        verifyStoragePermissions(this);
        verificarExistenciaImagem();

        imageView = (ImageView) findViewById(R.id.imgLoja);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Lista de itens
                ArrayList<String> itens = new ArrayList<String>();
                itens.add("Camera");
                itens.add("Galeria");

                //adapter utilizando um layout customizado (TextView)
                ArrayAdapter adapter = new ArrayAdapter(DetalhesActivity.this, R.layout.item_alerta, itens);

                AlertDialog.Builder builder = new AlertDialog.Builder(DetalhesActivity.this);
                builder.setTitle("Selecione a forma de captura:");
                //define o diálogo como uma lista, passa o adapter.
                builder.setSingleChoiceItems(adapter, 0, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        switch (arg1){
                            case 0:
                                ExibirCamera();
                                break;
                            case 1:
                                ExibirGaleria();
                                break;
                        };
                        alerta.dismiss();
                    }
                });

                alerta = builder.create();
                alerta.show();
            }
        });
    }

    private void verificarExistenciaImagem() {
        File imgFileG = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ProjetoDS/Galeria/" + txtNome.getText().toString().trim() + ".jpg");
        ImageView imgLoja = (ImageView) findViewById(R.id.imgLoja);

        if(imgFileG.exists()) {
            //Seta e redefine a qualidade da imagem.
            imgLoja.setImageBitmap(BitmapUtil.carregarBitmap(imgFileG, 100, 100));
        }

        File imgFileC = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ProjetoDS/Camera/" + txtNome.getText().toString().trim() + ".jpg");
        if(imgFileC.exists()) {
            //Seta e redefine a qualidade da imagem.
            imgLoja.setImageBitmap(BitmapUtil.carregarBitmap(imgFileC, 100, 100));
        }
    }

    private void ExibirGaleria() {
        Intent galeria = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(galeria, "Selecione uma imagem"), REQUEST_GALERIA);
    }

    private void ExibirCamera(){
        Intent camera = new Intent(this, CameraActivity.class);
        camera.putExtra(EXTRA_NOME, txtNome.getText().toString());
        startActivityForResult(camera, REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){

            if(requestCode == REQUEST_GALERIA){
                Uri imagemSelecionada = data.getData();
                verifyStoragePermissions(this);
                File imgFile = new File(getRealPathFromURI(imagemSelecionada));
                if (imgFile.exists()) {
                    ImageView imgLoja = (ImageView) findViewById(R.id.imgLoja);
                    File novoFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ProjetoDS/Galeria/" + txtNome.getText().toString().trim() + ".jpg");
                    Toast.makeText(this, "Arquivo salvo em: " + novoFile, Toast.LENGTH_LONG).show();
                    imgFile.renameTo(novoFile);
                     //Seta e redefine a qualidade da imagem.
                    imgLoja.setImageBitmap(BitmapUtil.carregarBitmap(novoFile, 100, 100));
                }
            }

            if(requestCode == REQUEST_CAMERA){
                verificarExistenciaImagem();
            }
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    public static void verifyStoragePermissions(Activity activity) {
        //Verificar em realtime a permissão se rodando em API 23 ou superior
        if(ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, REQUEST_CAMERA_PERMISSION);
            return;
        }
    }

}
