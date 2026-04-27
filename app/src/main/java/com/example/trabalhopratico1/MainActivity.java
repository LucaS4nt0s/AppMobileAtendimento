package com.example.trabalhopratico1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnCadastrarDemanda, btnListarChamados, btnFiltrarChamados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnCadastrarDemanda = findViewById(R.id.btnCadastrarDemanda);
        btnListarChamados = findViewById(R.id.btnListarChamados);
        btnFiltrarChamados = findViewById(R.id.btnFiltrarChamados);

        btnCadastrarDemanda.setOnClickListener(this);
        btnListarChamados.setOnClickListener(this);
        btnFiltrarChamados.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == btnCadastrarDemanda){
            startActivity(new Intent(this, CadastrarDemanda.class));
        }
        if(v == btnListarChamados){
            startActivity(new Intent(this, ListarChamados.class));
        }
        if(v == btnFiltrarChamados){
            startActivity(new Intent(this, FiltrarChamados.class));
        }
    }
}