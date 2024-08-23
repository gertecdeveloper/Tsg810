package com.example.easylayer_tsg810_217;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.easylayer_tsg810_217.CodigoDeBarras.CodigoDeBarras;
import com.example.easylayer_tsg810_217.Impressora.Impressora;
import com.example.easylayer_tsg810_217.NFC.ContactlessActivity;
import com.example.easylayer_tsg810_217.Projeto.Projeto;
import com.example.easylayer_tsg810_217.Projeto.ProjetoAdapter;
import com.example.easylayer_tsg810_217.Projeto.Sobre;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Projeto> projetos = new ArrayList<Projeto>();
    ListView lvProjetos;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //Informações
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button informaçoes = findViewById(R.id.infor);
        informaçoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        //Lista das funções
        lvProjetos = findViewById(R.id.lvProjetos);
        projetos.add(new Projeto("Código De Barras", R.drawable.qr_code));
        projetos.add(new Projeto("Impressão", R.drawable.print));
        projetos.add(new Projeto("NFC Leitura", R.drawable.nfc2));

        //Intent para as activitys das funções
        ProjetoAdapter adapter = new ProjetoAdapter(getBaseContext(), R.layout.listprojetos, projetos);
        lvProjetos.setAdapter(adapter);
        lvProjetos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Projeto projeto = (Projeto) lvProjetos.getItemAtPosition(i);

                Intent intent = null;
                switch (projeto.getNome()) {
                    case "Código De Barras":
                        intent = new Intent(MainActivity.this, CodigoDeBarras.class);
                        break;
                    case "Impressão":
                        intent = new Intent(MainActivity.this, Impressora.class);
                        break;
                    case "NFC Leitura":
                        intent = new Intent(MainActivity.this, ContactlessActivity.class);
                        break;
                }
                if (intent != null) {
                    startActivity(intent);
                }
            }
        });

    }

    //Função do dialog de informações
    public void showDialog() {
        int mStackLevel = 1;
        mStackLevel++;

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        Sobre newFragment = Sobre.newInstance(mStackLevel);
        newFragment.show(ft, "dialog");
    }
}