package com.gertec.exemplosgertec;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.gertec.exemplosgertec.ExemploCodigoBarras1.CodigoBarras1;
import com.gertec.exemplosgertec.ExemploCodigoBarras2.CodigoBarras2;
import com.gertec.exemplosgertec.ExemploNFCIdRW.NfcExemplo;
import com.gertec.exemplosgertec.ExemploImpressora.Impressora;
import com.gertec.exemplosgertec.ExemploSAT.SatPages.MenuSat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String G810 = "Smart G810";
    private static final String version = "v1.0";
    private String[] appPermissions ={
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static final int CODIGO_PERMISSOES_REQUISITADAS=1;

    TextView txtProject;

    ArrayList<Projeto> projetos = new ArrayList<Projeto>();
    ListView lvProjetos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtProject = findViewById(R.id.txtNameProject);
        lvProjetos = findViewById(R.id.lvProjetos);

        txtProject.setText("Android Studio "+ version+" - TSG810");

        projetos.add(new Projeto("Código de Barras", R.drawable.barcode));
        projetos.add(new Projeto("Código de Barras V2",R.drawable.qr_code));
        projetos.add(new Projeto("Impressão",R.drawable.print));
        projetos.add(new Projeto("NFC Leitura/Gravação",R.drawable.nfc2));
        projetos.add(new Projeto("SAT",R.drawable.icon_sat));

        ProjetoAdapter adapter = new ProjetoAdapter(getBaseContext(), R.layout.listprojetos, projetos);
        lvProjetos.setAdapter(adapter);
        lvProjetos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Projeto projeto = (Projeto) lvProjetos.getItemAtPosition(i);

                Intent intent = null;
                switch (projeto.getNome()){
                    case "Código de Barras":
                        if(permissoes()) {
                            intent = new Intent(MainActivity.this, CodigoBarras1.class);
                        }
                        break;
                    case "Código de Barras V2":
                        if(permissoes()) {
                            intent = new Intent(MainActivity.this, CodigoBarras2.class);
                        }
                        break;
                    case "Impressão":
                        intent = new Intent(MainActivity.this, Impressora.class);
                        break;
                    case "NFC Leitura/Gravação":
                        intent = new Intent(MainActivity.this, NfcExemplo.class);
                        break;
                    case "SAT":
                        intent = new Intent(MainActivity.this, MenuSat.class);
                        break;
                }
                if(intent != null){
                    startActivity(intent);
                }
            }
        });
    }

    public boolean permissoes(){
        List<String> permissoesRequeridas = new ArrayList<>();

        for (String permissao : appPermissions){
            if(ContextCompat.checkSelfPermission(this,permissao) != PackageManager.PERMISSION_GRANTED){
                permissoesRequeridas.add(permissao);
            }
        }

        if(!permissoesRequeridas.isEmpty()){
            ActivityCompat.requestPermissions(this,
                    permissoesRequeridas.toArray(new String[permissoesRequeridas.size()]),
                            CODIGO_PERMISSOES_REQUISITADAS);
            return false;
        }
        return true;
    }
}
