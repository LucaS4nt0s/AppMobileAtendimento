package com.example.trabalhopratico1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.chip.ChipGroup;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CadastrarDemanda extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 100;

    private com.google.android.material.textfield.TextInputEditText editTextTitulo, editTextDescricao, editTextLocal;
    private Button btnCancelar, btnEnviarDemanda, btnCapturarFoto;
    private ChipGroup chipGroupStatus;
    private ImageView imageViewPreview;
    private String statusSelecionado = "";
    private String currentPhotoPath = "";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastrar_demanda);

        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editTextTitulo = findViewById(R.id.editTextTitulo);
        editTextDescricao = findViewById(R.id.editTextDescricao);
        editTextLocal = findViewById(R.id.editTextLocal);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnEnviarDemanda = findViewById(R.id.btnEnviarDemanda);
        btnCapturarFoto = findViewById(R.id.btnCapturarFoto);
        chipGroupStatus = findViewById(R.id.chipGroupStatus);
        imageViewPreview = findViewById(R.id.imageViewPreview);

        btnCancelar.setOnClickListener(this);
        btnEnviarDemanda.setOnClickListener(this);
        btnCapturarFoto.setOnClickListener(this);

        chipGroupStatus.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                if (checkedIds.contains(R.id.chipAberto)) {
                    statusSelecionado = "Aberto";
                } else if (checkedIds.contains(R.id.chipAndamento)) {
                    statusSelecionado = "Em andamento";
                } else if (checkedIds.contains(R.id.chipConcluido)) {
                    statusSelecionado = "Conclu\u00EDdo";
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnCancelar) {
            finish();
        } else if (v.getId() == R.id.btnEnviarDemanda) {
            enviarDemanda();
        } else if (v.getId() == R.id.btnCapturarFoto) {
            dispatchTakePictureIntent();
        }
    }

    private void dispatchTakePictureIntent() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
            return;
        }
        abrirCamera();
    }

    private void abrirCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            Toast.makeText(this, "Câmera não disponível", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                abrirCamera();
            } else {
                Toast.makeText(this, "Permissão de câmera negada", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            Bitmap fotoCapturada = (Bitmap) extras.get("data");
            if (fotoCapturada != null) {
                imageViewPreview.setVisibility(View.VISIBLE);
                imageViewPreview.setImageBitmap(fotoCapturada);
                currentPhotoPath = salvarBitmap(fotoCapturada);
            }
        }
    }

    private String salvarBitmap(Bitmap bitmap) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (storageDir == null) {
            storageDir = getFilesDir();
        }
        File imageFile = new File(storageDir, imageFileName);
        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fos);
            return imageFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private void enviarDemanda() {
        if (editTextTitulo.getText().toString().isEmpty()) {
            editTextTitulo.setError("Campo obrigatório");
            return;
        }
        if (editTextDescricao.getText().toString().isEmpty()) {
            editTextDescricao.setError("Campo obrigatório");
            return;
        }
        if (editTextLocal.getText().toString().isEmpty()) {
            editTextLocal.setError("Campo obrigatório");
            return;
        }
        if (statusSelecionado.isEmpty()) {
            Toast.makeText(this, "Selecione um status", Toast.LENGTH_SHORT).show();
            return;
        }

        String titulo = editTextTitulo.getText().toString();
        String descricao = editTextDescricao.getText().toString();
        String local = editTextLocal.getText().toString();

        BD bd = new BD(this);
        long id = bd.salvarDados(titulo, descricao, local, statusSelecionado, currentPhotoPath);
        bd.close();

        salvarNoBack4App(titulo, descricao, local, statusSelecionado, currentPhotoPath, id);

        Toast.makeText(this, "Chamado cadastrado com sucesso", Toast.LENGTH_SHORT).show();

        finish();
    }

    private void salvarNoBack4App(String titulo, String descricao, String local, String estado, String imagePath, long idLocal) {
        com.parse.ParseObject chamado = new com.parse.ParseObject("Chamado");
        chamado.put("titulo", titulo);
        chamado.put("descricao", descricao);
        chamado.put("local", local);
        chamado.put("estado", estado);
        chamado.put("dataCadastro", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
        chamado.put("idLocal", idLocal);

        if (!imagePath.isEmpty()) {
            chamado.put("nomeImagem", new File(imagePath).getName());
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            if (bitmap != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                byte[] bytesFoto = stream.toByteArray();
                String fotoBase64 = android.util.Base64.encodeToString(bytesFoto, android.util.Base64.DEFAULT);
                chamado.put("fotoString", fotoBase64);
            }
        }

        chamado.saveInBackground(e -> {
            if (e == null) {
                android.util.Log.i("Back4App", "Chamado salvo na nuvem com sucesso");
            } else {
                android.util.Log.e("Back4App", "Erro ao salvar na nuvem", e);
            }
        });
    }
}
