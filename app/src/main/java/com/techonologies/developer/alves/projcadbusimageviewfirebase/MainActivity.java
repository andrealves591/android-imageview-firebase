package com.techonologies.developer.alves.projcadbusimageviewfirebase;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.techonologies.developer.alves.projcadbusimageviewfirebase.model.Usuario;

/**
 * @author  ALVESDEVTEC - André Luiz Alves
 * @since 07/03/2019 - 12H40
 * @version 2.0 turboMax
 */

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Button btEscolher;
    private Button btCadastrar;
    private Button btBuscar;
    private EditText etNome;
    private ImageView ivFoto;
    private ProgressBar progressBar;

    private Uri imageUri;

    private StorageReference mStorageRef;
    private DatabaseReference banco;

    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btEscolher = findViewById(R.id.ma_bt_escolher);
        btCadastrar = findViewById(R.id.ma_bt_cadastrar);
        btBuscar = findViewById(R.id.ma_bt_buscar);
        etNome = findViewById(R.id.ma_et_nome);
        ivFoto = findViewById(R.id.ma_iv_foto);
        progressBar = findViewById(R.id.ma_progress_bar);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        banco = FirebaseDatabase.getInstance().getReference("uploads");

        btEscolher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //valida se não há upload em andamento
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(MainActivity.this, "Cadastro em andamento...", Toast.LENGTH_SHORT).show();
                } else {
                    cadastrar();
                }

            }
        });

        btBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirLista();
            }
        });
    }

    private void cadastrar() {
        if (imageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));

            mUploadTask = fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(0);
                                }
                            }, 500);

                            //seta os dados para a classe Usuatios via parâmetro
                            Usuario upload = new Usuario(
                                    etNome.getText().toString().trim(),
                                    taskSnapshot.getDownloadUrl().toString());

                            //Cadastra no firebase
                            String uploadId = banco.push().getKey();
                            banco.child(uploadId).setValue(upload);

                            Toast.makeText(MainActivity.this, "Sucesso!", Toast.LENGTH_LONG).show();
                            limpar();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "Nenhuma imagem selecionada!", Toast.LENGTH_SHORT).show();
        }
    }

    //ABRE A GALERIA
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    //PEGA A EXTENSÃO DO ARQUIVO DE IMAGEM
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    //CARREGA A IMAGEM DENTRO DA IMAGEVIEW
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();

            Picasso.get().load(imageUri).into(ivFoto);
        }
    }
    private void limpar() {
        etNome.setText("");
        ivFoto.setImageDrawable(null);
    }
    //ABRE A BUSCA_ACITIVITY
    private void abrirLista() {
        Intent intent = new Intent(this, BuscaActivity.class);
        startActivity(intent);
    }
}
