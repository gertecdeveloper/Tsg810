package com.example.easylayer_tsg810_217.NFC;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.easylayer_tsg810_217.R;

import java.io.IOException;

import br.com.gertec.easylayer.contactless.ContactlessModule;


public class ContactlessActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter;
    private boolean writeOn = false;

    private final static String TAG = "mifare_test";
    private PendingIntent pendingIntent;
    private ContactlessModule contactlessModule;

    private byte[] key = {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};

    private Dialog dialog;
    private Integer idBlock = 0;
    private boolean checkAuthentication = true;

    EditText textData = null;
    EditText textBlock = null;

    TextView response;


    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactless);

        textData = findViewById(R.id.textData);
        textBlock = findViewById(R.id.textBlock);
        response = findViewById(R.id.response);

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.contactless_dialog);
        showDialog("Leitura");

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);


        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC não compatível", Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(this, getClass());
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        contactlessModule = new ContactlessModule(this, this, nfcAdapter);

        idBlock = 12;
        textBlock.setText(idBlock.toString());

        //Botão de escrever
        Button btnWrite = findViewById(R.id.btnWrite);
        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeOn = true;
                showDialog("Escrita");
            }
        });

        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch swich = findViewById(R.id.switchRotation);
        swich.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkAuthentication = true;
                    setLimiteCaracteres(16);
                } else {
                    checkAuthentication = false;
                    setLimiteCaracteres(4);
                }
            }
        });
    }

    private void setLimiteCaracteres(int limite) {
        textData.setFilters(new InputFilter[]{new InputFilter.LengthFilter(limite)});
        textData.setText("");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);



        if (!textData.getText().equals("") || !textBlock.getText().equals("")) {
            idBlock = Integer.parseInt(textBlock.getText().toString());
            if (writeOn) {
                dialog.dismiss();
                contactlessModule.intentReceived(intent);
                writeTag();
                Toast.makeText(this, "Escrito com successo", Toast.LENGTH_SHORT).show();
                writeOn = false;
            } else {
                dialog.dismiss();
                try {
                    contactlessModule.intentReceived(intent);
                    response.setText(detectTagData());
                } catch (Exception e) {
                    response.setText("Cartão não suportado!");
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        contactlessModule.enableForegroundDispatch(pendingIntent, null, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        contactlessModule.dissableForegroundDispatch();
    }

    private String detectTagData() {
        StringBuilder sb = new StringBuilder();
        try {
            contactlessModule.connect(300);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            sb.append("Mifare type: ");
            sb.append(contactlessModule.getType());
            sb.append('\n');

            sb.append("Mifare size: ");
            sb.append(contactlessModule.getSize() + " bytes");
            sb.append('\n');

            sb.append("Mifare sectors: ");
            sb.append(contactlessModule.getSectorCount());
            sb.append('\n');

            sb.append("Mifare blocks: ");
            sb.append(contactlessModule.getBlockCount());
            sb.append('\n');

            sb.append("Mifare Timeout: ");
            sb.append(contactlessModule.getTimeout() + "ms");
            sb.append('\n');

            sb.append("Mifare Transceive Length: ");

            sb.append(contactlessModule.getMaxTransceiveLength());
            sb.append('\n');
            sb.append('\n');

            TextView authenticationKeyText = findViewById(R.id.textKey);
            byte[] authenticationKey = Utils.hexStringToByteArray(authenticationKeyText.getText().toString());


            if (checkAuthentication) {
                if (contactlessModule.authenticateSectorWithKeyB(
                        contactlessModule.blockToSector(idBlock), authenticationKey)) {
                    sb.append(new String(contactlessModule.readBlock(idBlock)));
                } else {
                    sb.append("Erro, não autenticado!");
                }
            } else {
                sb.append(new String(contactlessModule.readBlock(idBlock)));
            }

            sb.append("\n");
            sb.append('\n');

            Log.v(TAG, sb.toString());
            sb.append('\n');
            sb.append('\n');
            contactlessModule.close();
        } catch (Exception e) {
            Log.v(TAG, e.toString());
        }

        return sb.toString();
    }

    public void writeTag() {
        if (!textData.getText().equals("") || !textBlock.getText().equals("")) {
            idBlock = Integer.parseInt(textBlock.getText().toString());
            String data = textData.getText().toString();

            TextView authenticationKeyText = findViewById(R.id.textKey);
            byte[] authenticationKey = Utils.hexStringToByteArray(authenticationKeyText.getText().toString());

            try {
                contactlessModule.connect(300);
                if (checkAuthentication) {
                    contactlessModule.authenticateSectorWithKeyB(
                            contactlessModule.blockToSector(idBlock), authenticationKey);
                }
                if (contactlessModule.isConnected()) {
                    contactlessModule.writeBlock(idBlock, stringToByteArray(data));
                    response.setText("Escrito com sucesso");
                }


            } catch (Exception e) {

            }
        } else {
            Toast.makeText(this, "Dados ou bloco vazios", Toast.LENGTH_SHORT).show();
        }
    }

    public byte[] stringToByteArray(String data) {
        byte[] b;

        if (checkAuthentication) {
            b = new byte[16];
        } else {
            b = new byte[4];
        }

        for (int i = 0; i < data.length(); i++) {
            b[i] = (byte) data.charAt(i);
        }
        return b;
    }

    public void showDialog(String msg) {
        Button btnDismiss = dialog.findViewById(R.id.btnDismiss);
        TextView txtMessage = dialog.findViewById(R.id.txtMessage);
        txtMessage.setText(msg);
        if (!msg.equals("write")) {
            btnDismiss.setVisibility(View.GONE);
        } else {
            btnDismiss.setVisibility(View.VISIBLE);
        }
        dialog.show();
        btnDismiss.setOnClickListener(v -> {
            dialog.dismiss();
            writeOn = false;
        });
    }
}
