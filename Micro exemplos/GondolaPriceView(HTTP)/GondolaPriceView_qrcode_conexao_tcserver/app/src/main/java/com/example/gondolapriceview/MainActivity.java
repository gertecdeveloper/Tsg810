package com.example.gondolapriceview;//package com.example.gondolapriceview;
//
//import android.annotation.SuppressLint;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.util.Log;
//import android.widget.TextView;
//import android.widget.Toast;
//import androidx.appcompat.app.AppCompatActivity;
//
//import java.io.BufferedInputStream;
//import java.io.BufferedOutputStream;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.Socket;
//
//public class MainActivity extends AppCompatActivity {
//    private TextView dataFromServerTextView;
//    public BufferedOutputStream out;
//
//    public  BufferedInputStream in;
////0x59 + tamanho código de barras + string do código de barras
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        // Inicializar referência do TextView
//        dataFromServerTextView = findViewById(R.id.barcodeTextView);
//
//        // Iniciar comunicação com o servidor assim que a atividade for criada
//        iniciarComunicacaoComServidor();
//    }
//
//    private void iniciarComunicacaoComServidor() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    // Estabelecer conexão com o servidor
//                    Socket socket = new Socket("10.0.0.198", 16510);
////                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
//                    out = new BufferedOutputStream(socket.getOutputStream());
//                    in = new BufferedInputStream(socket.getInputStream());
//
//                    // Loop infinito para manter a comunicação contínua
//                    while (true) {
//                        String resposta = lerSocket();
//                        if (resposta != null) {
//                            byte[] respByteArray = resposta.getBytes();
//                            if (respByteArray[0] == 0x02 && respByteArray[1] == 0x13)//ID_W_GET_IDENTIFY 0x13
//                            {
////                                out.write("02140006000000310258313030".getBytes());//R_ID_W_GET_IDENTIFY // 0x14
////                                out.flush();
//                                escreveSocket("02140006000000310258313030");
//                            } else if (respByteArray[0] == 0x02 && respByteArray[1] == 0x15)//ID_CONTINUE 0x15
//                            {
////                                out.write("02160000000000".getBytes());//R_ID_CONTINUE 0x16
////                                out.flush();
//                                escreveSocket("02160000000000");
//                            } else if (respByteArray[0] == 0x02 && respByteArray[1] == 0x1b)//ID_V_GET_UID 0x1b
//                            {
//
////                                out.write("021c0026000000".getBytes());//R_ID_V_GET_UID 0x1c
////                                out.flush();
//                                escreveSocket("021c0026000000");
//                                escreveSocket("00d3091ced8d3031323334353637383941424344454600000000000000000000000000000000");
////                                out.write("00d3091ced8d3031323334353637383941424344454600000000000000000000000000000000".getBytes());//? Talvez deve ser a identificação do dispositivo
////                                //0123456789ABCDEF
////                                out.flush();
//                            } else if (respByteArray[0] == 0x02 && respByteArray[1] == 0x11)//ID_V_LIVE 0x11
//                            {
//                                escreveSocket("02120000000000");
////                                out.write("02120000000000".getBytes());//R_ID_V_LIVE 0x12
////                                out.flush();
//                            }
//                        }
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Log.e("Error", "Erro na comunicação com o servidor: " + e.getMessage());
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }).start();
//    }
//    public String lerSocket() throws Exception {
//
//        int qtdLida;
//        String resposta;
//        //Cria um vetor de bytes de 255 posições
//        byte vetor[] = new byte[1_024];
//        //Lê o fluxo de dados e salva no vetor
//        qtdLida = in.read(vetor);
//        //Recebe a resposta do terminal
//        resposta = new String(vetor);
//        //resposta = (Arrays.toString(vetor));
//        return resposta;
//
//    }
//    public void escreveSocket(String comando) throws Exception {
//        byte[] vetor1;
//        int tamanho = 2, vetorCont = 0;
//        if (comando.length() % 2 == 0) {
//            vetor1 = new byte[comando.length() / 2];
//            for (int i = 0; i < comando.length(); i++) {
//                vetor1[vetorCont] = (byte) (0xFF & (Integer.valueOf(comando.substring(i, tamanho), 16)));
//                i++;
//                vetorCont++;
//                tamanho += 2;
//            }
//            out.write(vetor1);
//            out.flush();
//        }
//    }
//
//
//}


//import android.os.Bundle;
//import android.util.Log;
//import android.widget.TextView;
//import android.widget.Toast;
//import androidx.appcompat.app.AppCompatActivity;
//
//import java.io.BufferedOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.Socket;
//
//public class MainActivity extends AppCompatActivity {
//    private TextView dataFromServerTextView;
//    public BufferedOutputStream out;
//    private static final String TAG = "MainActivity";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        // Inicializar referência do TextView
//        dataFromServerTextView = findViewById(R.id.data_from_server_text_view);
//
//        // Iniciar comunicação com o servidor assim que a atividade for criada
//        iniciarComunicacaoComServidor();
//    }
//
//    private void iniciarComunicacaoComServidor() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    // Estabelecer conexão com o servidor
//                    Log.d(TAG, "Tentando conectar ao servidor...");
//                    Socket socket = new Socket("10.0.0.198", 16510);
//                    Log.d(TAG, "Conexão estabelecida com o servidor.");
//
//                    out = new BufferedOutputStream(socket.getOutputStream());
//                    InputStream in = socket.getInputStream();
//
//                    // Loop infinito para manter a comunicação contínua
//                    while (true) {
//                        byte[] buffer = new byte[1024];
//                        int bytesRead = in.read(buffer);
//                        if (bytesRead != -1) {
//                            Log.d(TAG, "Resposta recebida do servidor: " + new String(buffer, 0, bytesRead));
//                            if (buffer[0] == 0x02 && buffer[1] == 0x13) {
//                                escreveSocket("02140006000000310258313030");
//                            } else if (buffer[0] == 0x02 && buffer[1] == 0x15) {
//                                escreveSocket("02160000000000");
//                            } else if (buffer[0] == 0x02 && buffer[1] == 0x1b) {
//                                escreveSocket("021c0026000000");
//                                escreveSocket("00d3091ced8d3031323334353637383941424344454600000000000000000000000000000000");
//                            } else if (buffer[0] == 0x02 && buffer[1] == 0x11) {
//                                escreveSocket("02120000000000");
//                            }
//                        }
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Log.e(TAG, "Erro na comunicação com o servidor: " + e.getMessage());
//
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }).start();
//    }
//
//    public void escreveSocket(String comando) throws Exception {
//        Log.d(TAG, "Enviando comando para o servidor: " + comando);
//        byte[] vetor1;
//        int tamanho = 2, vetorCont = 0;
//        if (comando.length() % 2 == 0) {
//            vetor1 = new byte[comando.length() / 2];
//            for (int i = 0; i < comando.length(); i++) {
//                vetor1[vetorCont] = (byte) (0xFF & (Integer.valueOf(comando.substring(i, tamanho), 16)));
//                i++;
//                vetorCont++;
//                tamanho += 2;
//            }
//            out.write(vetor1);
//            out.flush();
//        }
//    }
//
//}



//    private void iniciarComunicacaoComServidor() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    // Estabelecer conexão com o servidor
//                    Socket socket = new Socket("192.168.0.88", 16510);
//                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
//                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//
//                    // Loop infinito para manter a comunicação contínua
//                    while (true) {
//
//                        String resposta = in.readLine();
//                        if(resposta.substring(0,4).contains("0213"))//ID_W_GET_IDENTIFY 0x13
//                        {
//                            out.println("02140006000000310258313030");//R_ID_W_GET_IDENTIFY // 0x14
//                            out.flush();
//                        }
//                        else if(resposta.substring(0,4).contains("0215"))//ID_CONTINUE 0x15
//                        {
//                            out.println("02160000000000");//R_ID_CONTINUE 0x16
//                            out.flush();
//                        }
//                        else if(resposta.substring(0,4).contains("021b"))//ID_V_GET_UID 0x1b
//                        {
//                            out.println("021c0026000000");//R_ID_V_GET_UID 0x1c
//                            out.flush();
//                            out.println("00d3091ced8d3031323334353637383941424344454600000000000000000000000000000000");//? Talvez deve ser a identificação do dispositivo
//                            //0123456789ABCDEF
//                            out.flush();
//                        }
//                        else if(resposta.substring(0,4).contains("0211"))//ID_V_LIVE 0x11
//                        {
//                            out.println("02120000000000");//R_ID_V_LIVE 0x12
//                            out.flush();
//                        }
//                        // Aguardar um tempo antes de enviar a próxima solicitação
//                        Thread.sleep(100); // Aguarda 5 segundos antes de enviar a próxima solicitação
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Log.e("Error", "Erro na comunicação com o servidor: " + e.getMessage());
//                    exibirErro("Erro na comunicação com o servidor");
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }

//    private void exibirRespostaDoServidor(final String resposta) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if (resposta != null && !resposta.isEmpty()) {
//                    dataFromServerTextView.setText(resposta);
//                } else {
//                    exibirErro("Resposta vazia do servidor");
//                }
//            }
//        });
//    }
//
//    private void exibirErro(final String mensagem) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(MainActivity.this, mensagem, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }




//import android.os.Bundle;
//import android.util.Log;
//import android.widget.TextView;
//import android.widget.Toast;
//import androidx.appcompat.app.AppCompatActivity;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.Socket;
//
//public class MainActivity extends AppCompatActivity {
//    private TextView dataFromServerTextView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        // Inicializar referência do TextView
//        dataFromServerTextView = findViewById(R.id.data_from_server_text_view);
//
//        // Iniciar comunicação com o servidor assim que a atividade for criada
//        iniciarComunicacaoComServidor();
//    }
//
//    private void iniciarComunicacaoComServidor() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    // Estabelecer conexão com o servidor
//                    Socket socket = new Socket("192.168.137.1", 6500);
//                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
//                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//
//                    // Loop infinito para manter a comunicação contínua
//                    while (true) {
//                        // Enviar o código de barras ao servidor
//                        String barcode = "7896094925144";
//                        out.println(barcode);
//                        out.flush();
//                        Log.d("Debug", "Código de barras enviado: " + barcode);
//
//                        // Receber informações do produto do servidor
//                        String resposta = in.readLine();
//                        Log.d("Debug", "Resposta do servidor: " + resposta);
//
//                        // Exibir informações recebidas do servidor
//                        exibirRespostaDoServidor(resposta);
//
//                        // Aguardar um tempo antes de enviar a próxima solicitação
//                        Thread.sleep(5000); // Aguarda 5 segundos antes de enviar a próxima solicitação
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Log.e("Error", "Erro na comunicação com o servidor: " + e.getMessage());
//                    exibirErro("Erro na comunicação com o servidor");
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }
//
//    private void exibirRespostaDoServidor(final String resposta) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if (resposta != null && !resposta.isEmpty()) {
//                    dataFromServerTextView.setText(resposta);
//                } else {
//                    exibirErro("Resposta vazia do servidor");
//                }
//            }
//        });
//    }

//    private void exibirErro(final String mensagem) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(MainActivity.this, mensagem, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}



//import androidx.appcompat.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//
//import com.example.gondolapriceview.util.ProductService;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.Converter;
//import retrofit2.converter.scalars.ScalarsConverterFactory;
//
//public class MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://10.0.0.198:8080/")
//                .addConverterFactory(ScalarsConverterFactory.create())
//                .build();
//
//        ProductService service = retrofit.create(ProductService.class);
//
//        Call<String> call = service.getProductInfo("7896094925144");
//
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                if (response.isSuccessful()) {
//                    String responseBody = response.body();
//                    // Faça algo com a resposta do servidor
//                    Log.d("DEUS", "Resposta do servidor: " + responseBody);
//                } else {
//                    // Lidar com erro
//                    Log.e("DEUS", "Erro na requisição: " + response.message());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                // Lidar com falha na requisição
//                Log.e("DEUS", "Falha na requisição: " + t.getMessage());
//            }
//        });
//    }
//}



//
//import androidx.appcompat.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.select.Elements;
//
//import com.example.gondolapriceview.util.ProductService;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.scalars.ScalarsConverterFactory;
//
//public class MainActivity extends AppCompatActivity {
//
//    private EditText barcodeEditText;
//    private TextView barcodeTextView;
//    private TextView descriptionTextView;
//    private TextView price1TextView;
//    private TextView price2TextView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        barcodeEditText = findViewById(R.id.barcodeEditText);
//        barcodeTextView = findViewById(R.id.barcodeTextView);
//        descriptionTextView = findViewById(R.id.descriptionTextView);
//        price1TextView = findViewById(R.id.price1TextView);
//        price2TextView = findViewById(R.id.price2TextView);
//
//        Button consultButton = findViewById(R.id.consultButton);
//        consultButton.setOnClickListener(view -> {
//            String barcode = barcodeEditText.getText().toString().trim();
//            if (!barcode.isEmpty()) {
//                fetchDataFromServer(barcode);
//            } else {
//                Toast.makeText(MainActivity.this, "Por favor, insira um código de barras válido", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void fetchDataFromServer(String barcode) {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://10.0.0.198:8080/")
//                .addConverterFactory(ScalarsConverterFactory.create())
//                .build();
//
//        ProductService service = retrofit.create(ProductService.class);
//
//        Call<String> call = service.getProductInfo(barcode);
//
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                if (response.isSuccessful()) {
//                    String responseBody = response.body();
//                    if (responseBody != null && !responseBody.isEmpty()) {
//                        // Parse HTML using Jsoup
//                        Document doc = Jsoup.parse(responseBody);
//
//                        // Extract relevant data
//                        String barcode = "";
//                        String description = "";
//                        String price1 = "";
//                        String price2 = "";
//
//                        Elements elements = doc.select("body").select("br");
//                        if (elements.size() >= 7) {
//                            barcode = elements.get(1).previousSibling().toString();
//                            description = elements.get(2).previousSibling().toString();
//                            price1 = elements.get(4).previousSibling().toString();
//                            price2 = elements.get(6).previousSibling().toString();
//                        } else {
//                            // Produto não encontrado
//                            barcodeTextView.setText("Produto não encontrado");
//                            descriptionTextView.setText("");
//                            price1TextView.setText("");
//                            price2TextView.setText("");
//                            return;
//                        }
//
//                        // Set data to TextViews
//                        barcodeTextView.setText("Código de barras: " + barcode.split(":")[1].trim());
//                        descriptionTextView.setText("Descrição: " + description.split(":")[1].trim());
//                        price1TextView.setText("Preço 1: " + price1.split(":")[1].trim());
//                        price2TextView.setText("Preço 2: " + price2.split(":")[1].trim());
//                    } else {
//                        // Produto não encontrado
//                        barcodeTextView.setText("Produto não encontrado");
//                        descriptionTextView.setText("");
//                        price1TextView.setText("");
//                        price2TextView.setText("");
//                    }
//                } else {
//                    // Lidar com erro
//                    Log.e("DEUS", "Erro na requisição: " + response.message());
//                    Toast.makeText(MainActivity.this, "Erro na requisição. Tente novamente mais tarde.", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                // Lidar com falha na requisição
//                Log.e("DEUS", "Falha na requisição: " + t.getMessage());
//                Toast.makeText(MainActivity.this, "Falha na requisição. Verifique sua conexão com a internet.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}
//
//



//import androidx.appcompat.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.Socket;
//import java.net.SocketTimeoutException;
//
//import androidx.appcompat.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.Socket;
//import java.net.SocketTimeoutException;
//
//public class MainActivity extends AppCompatActivity {
//
//    private EditText barcodeEditText;
//    private EditText hostEditText;
//    private EditText portEditText;
//    private TextView barcodeTextView;
//    private TextView descriptionTextView;
//    private TextView price1TextView;
//    private TextView price2TextView;
//
//    private Socket socket;
//    private BufferedReader input;
//    private PrintWriter output;
//    private final int TIMEOUT_SECONDS = 10; // Timeout de 10 segundos
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        barcodeEditText = findViewById(R.id.barcodeEditText);
//        hostEditText = findViewById(R.id.hostEditText);
//        portEditText = findViewById(R.id.portEditText);
//        barcodeTextView = findViewById(R.id.barcodeTextView);
//        descriptionTextView = findViewById(R.id.descriptionTextView);
//        price1TextView = findViewById(R.id.price1TextView);
//        price2TextView = findViewById(R.id.price2TextView);
//
//        Button consultButton = findViewById(R.id.consultButton);
//        consultButton.setOnClickListener(view -> {
//            String barcode = barcodeEditText.getText().toString().trim();
//            String host = hostEditText.getText().toString().trim();
//            String portStr = portEditText.getText().toString().trim();
//
//            if (!barcode.isEmpty() && !host.isEmpty() && !portStr.isEmpty()) {
//                int port = Integer.parseInt(portStr);
//                sendDataToServer(barcode, host, port);
//            } else {
//                showToast("Por favor, insira um código de barras, host e porta válidos");
//            }
//        });
//    }
//
//    private void startSocketConnection(String host, int port) {
//        try {
//            Log.d("Socket", "Attempting to connect to the server...");
//            socket = new Socket(host, port);
//            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            output = new PrintWriter(socket.getOutputStream(), true);
//            Log.d("Socket", "Connected to the server");
//
//            long startTime = System.currentTimeMillis(); // Tempo inicial
//            long timeout = 5000; // Tempo limite em milissegundos (aqui definido como 5 segundos)
//
//            // Loop para aguardar a resposta do servidor
//            while (!socket.isClosed()) {
//                if (input.ready()) {
//                    // Se houver dados para ler
//                    String response = input.readLine();
//                    Log.d("Socket", "Received response from server: " + response);
//                    parseAndDisplayResponse(response);
//                    break; // Sai do loop quando a resposta é recebida
//                } else {
//                    // Verifica se excedeu o tempo limite
//                    if (System.currentTimeMillis() - startTime > timeout) {
//                        Log.e("Socket", "Timeout waiting for response from server");
//                        showToast("Timeout esperando resposta do servidor");
//                        closeSocket();
//                        break; // Sai do loop quando o tempo limite é excedido
//                    }
//                    // Aguarda um curto período antes de verificar novamente
//                    Thread.sleep(100);
//                }
//            }
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//            Log.e("Socket", "Error connecting to server: " + e.getMessage());
//            showToast("Erro na conexão com o servidor");
//            closeSocket();
//        }
//    }
//
//
//
//    private void sendDataToServer(String barcode, String host, int port) {
//        new Thread(() -> {
//            startSocketConnection(host, port);
//            if (socket != null && output != null) {
//                try {
//                    while (!socket.isClosed()) {
//                        // Send request to server
//                        String request = "/barcode?params=$[" + barcode + "]";
//                        Log.d("Socket", "Sending request to server: " + request);
//                        output.println(request);
//
//                        // Read response from server
//                        String response = input.readLine();
//                        if (response != null) {
//                            Log.d("Socket", "Received response from server: " + response);
//                            parseAndDisplayResponse(response);
//                        } else {
//                            Log.e("Socket", "Timeout waiting for response from server");
//                            showToast("Timeout esperando resposta do servidor");
//                        }
//                    }
//
//                } catch (SocketTimeoutException e) {
//                    e.printStackTrace();
//                    Log.e("Socket", "Timeout waiting for response from server: " + e.getMessage());
//                    showToast("Timeout esperando resposta do servidor");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Log.e("Socket", "Error sending or receiving data: " + e.getMessage());
//                    showToast("Erro na comunicação com o servidor");
//                } finally {
//                    closeSocket();
//                }
//            } else {
//                showToast("Erro na conexão com o servidor");
//                closeSocket();
//            }
//        }).start();
//    }
//
//    private void parseAndDisplayResponse(String response) {
//        // Implemente a lógica para processar a resposta do servidor aqui
//        // Exemplo:
//        runOnUiThread(() -> {
//            barcodeTextView.setText("Código de barras: " + response);
//            descriptionTextView.setText("Descrição: Produto não encontrado");
//            price1TextView.setText("Preço 1: N/A");
//            price2TextView.setText("Preço 2: N/A");
//        });
//    }
//
//    private void showToast(String message) {
//        runOnUiThread(() -> Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show());
//    }
//
//    private void closeSocket() {
//        try {
//            if (input != null) input.close();
//            if (output != null) output.close();
//            if (socket != null) socket.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}



//import android.os.Bundle;
//import android.util.Log;
//import android.widget.TextView;
//import android.widget.Toast;
//import androidx.appcompat.app.AppCompatActivity;
//
//import java.io.BufferedOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.Socket;
//import java.nio.charset.StandardCharsets;
//import java.util.Arrays;


//public class MainActivity extends AppCompatActivity {
//    private TextView dataFromServerTextView;
//    private BufferedOutputStream out;
//    private static final String TAG = "MainActivity";
//    private Handler uiHandler;
//    private Socket socket;
//    private InputStream in;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        dataFromServerTextView = findViewById(R.id.dataFromServerTextView);
//
//        // Iniciar o handler na UI thread
//        uiHandler = new Handler(Looper.getMainLooper());
//
//        // Iniciar comunicação com o servidor assim que a atividade for criada
//        iniciarComunicacaoComServidor();
//    }
//
//    private void iniciarComunicacaoComServidor() {
//        new Thread(() -> {
//            try {
//                // Estabelecer conexão com o servidor
//                Log.d(TAG, "Tentando conectar ao servidor...");
//                socket = new Socket("10.0.0.198", 16510);
//                Log.d(TAG, "Conexão estabelecida com o servidor.");
//
//                out = new BufferedOutputStream(socket.getOutputStream());
//                in = socket.getInputStream();
//
//                // Loop infinito para manter a comunicação contínua
//                while (true) {
//                    byte[] buffer = new byte[1024];
//                    int bytesRead = in.read(buffer);
//                    if (bytesRead != -1) {
//                        Log.d(TAG, "Resposta recebida do servidor (bytes): " + Arrays.toString(Arrays.copyOf(buffer, bytesRead)));
//                        processarResposta(buffer, bytesRead);
//
//                        String data = bytesParaStringLegivel(buffer, bytesRead);
//                        uiHandler.post(() -> {
//                            // Atualizar o TextView com os dados recebidos do servidor
//                            dataFromServerTextView.setText(data);
//                        });
//                    }
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//                Log.e(TAG, "Erro na comunicação com o servidor: " + e.getMessage());
//            } finally {
//                fecharRecursos();
//            }
//        }).start();
//    }
//
//    private void processarResposta(byte[] buffer, int bytesRead) {
//        try {
//            if (buffer[0] == 0x02 && buffer[1] == 0x13) {
//                escreveSocket("02140006000000310258313030");
//            } else if (buffer[0] == 0x02 && buffer[1] == 0x15) {
//                escreveSocket("02160000000000");
//            } else if (buffer[0] == 0x02 && buffer[1] == 0x1b) {
//                escreveSocket("021c0026000000");
//                escreveSocket("00d3091ced8d3031323334353637383941424344454600000000000000000000000000000000");
//            } else if (buffer[0] == 0x02 && buffer[1] == 0x11) {
//                escreveSocket("02120000000000");
//            }
//        } catch (Exception e) {
//            Log.e(TAG, "Erro ao processar a resposta: " + e.getMessage());
//        }
//    }
//
//    public void escreveSocket(String comando) throws Exception {
//        Log.d(TAG, "Enviando comando para o servidor: " + comando);
//        byte[] vetor1;
//        int tamanho = 2, vetorCont = 0;
//        if (comando.length() % 2 == 0) {
//            vetor1 = new byte[comando.length() / 2];
//            for (int i = 0; i < comando.length(); i++) {
//                vetor1[vetorCont] = (byte) (0xFF & (Integer.valueOf(comando.substring(i, tamanho), 16)));
//                i++;
//                vetorCont++;
//                tamanho += 2;
//            }
//            out.write(vetor1);
//            out.flush();
//        }
//    }
//
//    private String bytesParaStringLegivel(byte[] buffer, int length) {
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < length; i++) {
//            if (buffer[i] >= 32 && buffer[i] <= 126) {
//                sb.append((char) buffer[i]);
//            } else {
//                sb.append(String.format("\\x%02X", buffer[i]));
//            }
//        }
//        return sb.toString();
//    }
//
//    private void fecharRecursos() {
//        try {
//            if (out != null) {
//                out.close();
//            }
//            if (in != null) {
//                in.close();
//            }
//            if (socket != null) {
//                socket.close();
//            }
//        } catch (IOException e) {
//            Log.e(TAG, "Erro ao fechar recursos: " + e.getMessage());
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        fecharRecursos();
//    }
//}
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.util.Log;
//import android.widget.TextView;
//import androidx.appcompat.app.AppCompatActivity;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import java.io.BufferedOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.Socket;
//import java.nio.charset.StandardCharsets;
//import java.util.Arrays;

//public class MainActivity extends AppCompatActivity {
//    private TextView dataFromServerTextView;
//    private BufferedOutputStream out;
//    private static final String TAG = "MainActivity";
//    private Handler uiHandler;
//    private Socket socket;
//    private InputStream in;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        dataFromServerTextView = findViewById(R.id.dataFromServerTextView);
//
//        // Iniciar o handler na UI thread
//        uiHandler = new Handler(Looper.getMainLooper());
//
//        // Iniciar comunicação com o servidor assim que a atividade for criada
//        iniciarComunicacaoComServidor();
//
//        // Exemplo: Convertendo um código de barras para bytes e enviando ao servidor
//        String codigoBarras = "#7891150061057"; // Código de barras de exemplo
//      //  enviarCodigoBarras(codigoBarras);
//    }
//
//    private void iniciarComunicacaoComServidor() {
//        new Thread(() -> {
//            try {
//                // Estabelecer conexão com o servidor
//                Log.d(TAG, "Tentando conectar ao servidor...");
//                socket = new Socket("10.0.0.198", 16510);
//                Log.d(TAG, "Conexão estabelecida com o servidor.");
//
//                out = new BufferedOutputStream(socket.getOutputStream());
//                in = socket.getInputStream();
//
//                // Loop infinito para manter a comunicação contínua
//                while (true) {
//                    byte[] buffer = new byte[1024];
//                    int bytesRead = in.read(buffer);
//                    if (bytesRead != -1) {
//                        Log.d(TAG, "Resposta recebida do servidor (bytes): " + Arrays.toString(Arrays.copyOf(buffer, bytesRead)));
//
//                        // Processar a resposta para decidir os próximos comandos
//                        try {
//                            processarDados(buffer, bytesRead);
//                        } catch (Exception e) {
//                            Log.e(TAG, "Erro ao processar a resposta: " + e.getMessage());
//                        }
//                    }
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//                Log.e(TAG, "Erro na comunicação com o servidor: " + e.getMessage());
//            } finally {
//                fecharRecursos();
//            }
//        }).start();
//    }
//
//    private void processarDados(byte[] buffer, int length) {
//        // Verificar se a resposta contém o marcador de início e fim
//        if (buffer[0] == 0x02 && buffer[length - 1] == 0x01) {
//            // Extrair os dados entre os marcadores de início e fim
//            byte[] dataBytes = Arrays.copyOfRange(buffer, 1, length - 1);
//            String data = new String(dataBytes, StandardCharsets.ISO_8859_1);
//            Log.d(TAG, "Dados recebidos do servidor: " + data);
//
//            // Atualizar a interface do usuário com os dados recebidos
//            uiHandler.post(() -> {
//                // Processar o HTML recebido usando o Jsoup
//                processarHTML(data);
//            });
//
//            // Processar a resposta para decidir os próximos comandos
//            try {
//                if (buffer[1] == 0x13) {
//                    escreveSocket("02140006000000310258313030");
//                } else if (buffer[1] == 0x15) {
//                    escreveSocket("02160000000000");
//                } else if (buffer[1] == 0x1b) {
//                    escreveSocket("021c0026000000");
//                    escreveSocket("00d3091ced8d3031323334353637383941424344454600000000000000000000000000000000");
//                } else if (buffer[1] == 0x11) {
//                    escreveSocket("02120000000000");
//                }
//            } catch (Exception e) {
//                Log.e(TAG, "Erro ao processar a resposta: " + e.getMessage());
//            }
//        } else {
//            Log.e(TAG, "Resposta não contém o formato esperado");
//        }
//    }
//
//    public void escreveSocket(String comando) throws IOException {
//        if (out != null) { // Verificar se o BufferedOutputStream está inicializado
//            Log.d(TAG, "Enviando comando para o servidor: " + comando);
//            byte[] vetor1;
//            int tamanho = 0, vetorCont = 0;
//            if (comando.length() % 2 == 0) {
//                vetor1 = new byte[comando.length() / 2];
//                for (int i = 0; i < comando.length(); i += 2) {
//                    String hexDigit = comando.substring(i, i + 2);
//                    try {
//                        int intValue = Integer.parseInt(hexDigit, 16);
//                        vetor1[vetorCont] = (byte) (0xFF & intValue);
//                        vetorCont++;
//                    } catch (NumberFormatException e) {
//                        Log.e(TAG, "Erro ao converter string para inteiro: " + e.getMessage());
//                        return;
//                    }
//                }
//                out.write(vetor1);
//                out.flush();
//            } else {
//                Log.e(TAG, "Comando de escrita não está no formato esperado");
//            }
//        } else {
//            Log.e(TAG, "O objeto BufferedOutputStream não foi inicializado corretamente");
//        }
//    }
//
//    private void fecharRecursos() {
//        try {
//            if (out != null) {
//                out.close();
//            }
//            if (in != null) {
//                in.close();
//            }
//            if (socket != null) {
//                socket.close();
//            }
//        } catch (IOException e) {
//            Log.e(TAG, "Erro ao fechar recursos: " + e.getMessage());
//        }
//    }
//
//    private void enviarCodigoBarras(String codigoBarras) {
//        try {
//            // Converter o código de barras para bytes e enviar ao servidor
//            byte[] codigoBytes = codigoBarras.getBytes(StandardCharsets.ISO_8859_1);
//            String comando = bytesToHexString(codigoBytes);
//            escreveSocket(comando);
//        } catch (IOException e) {
//            Log.e(TAG, "Erro ao enviar o código de barras: " + e.getMessage());
//        }
//    }
//
//    // Método auxiliar para converter bytes em uma string hexadecimal
//    private String bytesToHexString(byte[] bytes) {
//        StringBuilder hexString = new StringBuilder();
//        for (byte b : bytes) {
//            String hex = Integer.toHexString(0xFF & b);
//            if (hex.length() == 1) {
//                // Adicionar um zero à esquerda para garantir dois dígitos hexadecimais
//                hexString.append('0');
//            }
//            hexString.append(hex);
//        }
//        return hexString.toString();
//    }
//
//
//
//
//    private void processarHTML(String html) {
//        // Processar o HTML usando o Jsoup
//        Document doc = Jsoup.parse(html);
//
//        // Exemplo: Exibindo o título do documento HTML no TextView
//        String title = doc.title();
//        dataFromServerTextView.setText(title);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        fecharRecursos();
//    }
//}

//import android.os.Bundle;
//        import android.util.Log;
//        import android.widget.TextView;
//        import androidx.appcompat.app.AppCompatActivity;
//        import java.io.BufferedInputStream;
//        import java.io.BufferedOutputStream;
//        import java.io.IOException;
//        import java.net.Socket;
//
//public class MainActivity extends AppCompatActivity {
//    private TextView dataFromServerTextView;
//    private BufferedOutputStream out;
//    private BufferedInputStream in;
//    private final Object lock = new Object(); // Object for synchronization
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        dataFromServerTextView = findViewById(R.id.barcodeTextView);
//
//        iniciarComunicacaoComServidor();
////        enviarPrimeiroConjunto();
//      enviarSegundoConjunto();
//    }
//
//    private void iniciarComunicacaoComServidor() {
//        new Thread(() -> {
//            try {
//                Socket socket = new Socket("192.168.137.1", 16510);
//                synchronized (lock) {
//                    out = new BufferedOutputStream(socket.getOutputStream());
//                    in = new BufferedInputStream(socket.getInputStream());
//                }
//
//                // Teste de envio de código de barras
//                String codigoDeBarras = "123";
//                enviarCodigoDeBarras(codigoDeBarras);
//
//                // Loop infinito para manter a comunicação contínua
//                while (true) {
//                    String resposta = lerSocket();
//                    if (resposta != null) {
//                        byte[] respByteArray = resposta.getBytes();
//                        if (respByteArray.length > 1 && respByteArray[0] == 0x02) {
//                            switch (respByteArray[1]) {
//                                case 0x13: // ID_W_GET_IDENTIFY 0x13
//                                    escreveSocket("02140006000000310258313030");
//                                    break;
//                                case 0x15: // ID_CONTINUE 0x15
//                                    escreveSocket("02160000000000");
//                                    break;
//                                case 0x1b: // ID_V_GET_UID 0x1b
//                                    escreveSocket("021c0026000000");
//                                    escreveSocket("00d3091ced8d3031323334353637383941424344454600000000000000000000000000000000");
//                                    break;
//                                case 0x11: // ID_V_LIVE 0x11
//                                    escreveSocket("02120000000000");
//                                    break;
//                                default:
//                                    // Handle other cases if necessary
//                                    break;
//                            }
//                        }
//                    }
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//                Log.e("Error", "Erro na comunicação com o servidor: " + e.getMessage());
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }).start();
//    }
//
//    private void enviarDadosAoServidor(byte[] dados) {
//        new Thread(() -> {
//            synchronized (lock) {
//                try {
//                    if (out != null) {
//                        out.write(dados);
//                        out.flush();
//                        Log.d("DADOS", "enviarDadosAoServidor: "+dados);
//                    } else {
//                        Log.e("Error", "BufferedOutputStream 'out' is null");
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Log.e("Error", "Erro ao enviar dados para o servidor: " + e.getMessage());
//                }
//            }
//        }).start();
//    }
//
//    // Método para enviar o primeiro conjunto de bytes fornecido por Ricardo
//    private void enviarPrimeiroConjunto() {
//        byte[] dados = {
//                0x02, 0x59, 0x00, (byte) 0xaa, 0x0e, 0x00, 0x00, 0x31,
//                0x31, 0x31, 0x32, 0x32, 0x32, 0x33, 0x33, 0x33,
//                0x31, 0x31, 0x31, 0x39
//        };
//        enviarDadosAoServidor(dados);
//    }
//
//    // Método para enviar o segundo conjunto de bytes fornecido por Ricardo
//    private void enviarSegundoConjunto() {
//        byte[] dados = {
//                0x02, 0x59, 0x00, (byte) 0xaa, 0x0e, 0x00, 0x00, 0x0d,
//                0x00, 0x31, 0x31, 0x31, 0x32, 0x32, 0x32, 0x33,
//                0x33, 0x33, 0x31, 0x31, 0x31, 0x39
//        };
//        enviarDadosAoServidor(dados);
//    }
//
//    private void enviarCodigoDeBarras(String codigoDeBarras) {
//        byte prefixo = 0x59;
//        int tamanho = codigoDeBarras.length();
//        byte[] codigoDeBarrasBytes = codigoDeBarras.getBytes();
//        byte[] mensagem = new byte[2 + tamanho];
//        mensagem[0] = prefixo;
//        mensagem[1] = (byte) tamanho;
//        System.arraycopy(codigoDeBarrasBytes, 0, mensagem, 2, tamanho);
//
//        enviarDadosAoServidor(mensagem);
//    }
//
//    public String lerSocket() {
//        try {
//            byte[] vetor = new byte[1024];
//            int qtdLida = in.read(vetor);
//            if (qtdLida == -1) {
//                return null;
//            }
//            return new String(vetor, 0, qtdLida);
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.e("Error", "Erro ao ler dados do servidor: " + e.getMessage());
//            return null;
//        }
//    }
//
//    public void escreveSocket(String comando) {
//        byte[] vetor1;
//        int tamanho = 2, vetorCont = 0;
//        if (comando.length() % 2 == 0) {
//            vetor1 = new byte[comando.length() / 2];
//            for (int i = 0; i < comando.length(); i++) {
//                vetor1[vetorCont] = (byte) (0xFF & (Integer.valueOf(comando.substring(i, tamanho), 16)));
//                i++;
//                vetorCont++;
//                tamanho += 2;
//            }
//
//            enviarDadosAoServidor(vetor1);
//        }
//    }
//}
//http://10.0.0.198:8080/barcode?param=7896094925144  deu certo

//import androidx.appcompat.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//
//import com.example.gondolapriceview.util.ProductService;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.scalars.ScalarsConverterFactory;
//
//public class MainActivity extends AppCompatActivity {
//
//    private EditText barcodeEditText;
//    private TextView barcodeTextView;
//    private TextView descriptionTextView;
//    private TextView price1TextView;
//    private TextView price2TextView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        barcodeEditText = findViewById(R.id.barcodeEditText);
//        barcodeTextView = findViewById(R.id.barcodeTextView);
//        descriptionTextView = findViewById(R.id.descriptionTextView);
//        price1TextView = findViewById(R.id.price1TextView);
//        price2TextView = findViewById(R.id.price2TextView);
//
//        Button consultButton = findViewById(R.id.consultButton);
//        consultButton.setOnClickListener(view -> {
//            String barcode = barcodeEditText.getText().toString().trim();
//            if (!barcode.isEmpty()) {
//                fetchDataFromServer(barcode);
//            }
//        });
//    }
//
//    private void fetchDataFromServer(String barcode) {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://172.18.20.117:8080/")
//                .addConverterFactory(ScalarsConverterFactory.create())
//                .build();
//
//        ProductService service = retrofit.create(ProductService.class);
//
//        Call<String> call = service.getProductInfo(barcode);
//
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                if (response.isSuccessful()) {
//                    String responseBody = response.body();
//                    // Parse HTML using Jsoup
//                    Document doc = Jsoup.parse(responseBody);
//
//                    // Extract relevant data
//                    String barcode = doc.select("body").select("br").get(1).previousSibling().toString();
//                    String description = doc.select("body").select("br").get(2).previousSibling().toString();
//                    String price1 = doc.select("body").select("br").get(4).previousSibling().toString();
//                    String price2 = doc.select("body").select("br").get(6).previousSibling().toString();
//
//                    // Set data to TextViews
//                    barcodeTextView.setText("Código de barras: " + barcode.split(":")[1].trim());
//                    descriptionTextView.setText("Descrição: " + description.split(":")[1].trim());
//                    price1TextView.setText("Preço 1: " + price1.split(":")[1].trim());
//                    price2TextView.setText("Preço 2: " + price2.split(":")[1].trim());
//                } else {
//// Lidar com erro
//                    Log.e("DEUS", "Erro na requisição: " + response.message());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                // Lidar com falha na requisição
//                Log.e("DEUS", "Falha na requisição: " + t.getMessage());
//            }
//        });
//    }
//}


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gondolapriceview.canvas.GondolaPriceView;
import com.example.gondolapriceview.util.FTPrntr;
import com.ftpos.library.smartpos.device.Device;
import com.ftpos.library.smartpos.servicemanager.OnServiceConnectCallback;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import com.example.gondolapriceview.util.ProductService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView barcodeTextView;
    private TextView descriptionTextView;
    private TextView price1TextView;
    private TextView price2TextView;
    private TextView erroTextView;
    private Button scanQrCodeButton;
    private Button printButton;
    private Button ipButton;
    private FTPrntr mPrinter = null;
    private Device device;
    private Context mContext;
    private com.ftpos.library.smartpos.servicemanager.ServiceManager service;
    private String currentBarcode;
    private String currentDescription;
    private String currentPrice1;
    private boolean qrCodeScanned = false;
    String ipServidor = "";
    EditText ipEdtxt;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        barcodeTextView = findViewById(R.id.barcodeTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        price1TextView = findViewById(R.id.price1TextView);
        price2TextView = findViewById(R.id.price2TextView);
        erroTextView = findViewById(R.id.erroTextView);
        scanQrCodeButton = findViewById(R.id.scanQrCodeButton);
        printButton = findViewById(R.id.print_button);
        ipEdtxt = findViewById(R.id.ipServEdtxt);
        ipButton = findViewById(R.id.servidorButton);
        scanQrCodeButton.setEnabled(false);
        printButton.setEnabled(false);
        mContext = getApplicationContext();
        mPrinter = new FTPrntr();

        service.bindPosServer(this, new OnServiceConnectCallback() {
            @Override
            public void onSuccess() {
                mPrinter.getInstance(mContext);
                device = Device.getInstance(mContext);
                Log.e("binding", "Success");
                Log.e("printer", "getPaperUsage: " + mPrinter.getUsedPaperLenManage());
            }

            @Override
            public void onFail(int var1) {
                Log.e("binding", "onFail");
            }
        });

        ipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!ipEdtxt.getText().toString().equals("IP do Servidor")){
                    ipServidor = ipEdtxt.getText().toString();
                    ipEdtxt.setVisibility(View.GONE);
                    ipButton.setVisibility(View.GONE);
                    scanQrCodeButton.setEnabled(true);
                }else {
                    Toast.makeText(mContext,"Insira um ip válido", Toast.LENGTH_LONG).show();
                    erroTextView.setText("Por favor, digite um ip válido");
                    handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            erroTextView.setText("");
                        }
                    },3000);
                }
            }
        });

        scanQrCodeButton.setOnClickListener(view -> {
            IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
            integrator.setOrientationLocked(true);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            integrator.setCaptureActivity(CustomScannerActivity.class);
            integrator.initiateScan();
        });

        printButton.setOnClickListener(view -> {
            if (qrCodeScanned) {
                imprimirBitmap();
            } else {
                Toast.makeText(mContext, "Por favor, escaneie um QR Code antes de imprimir", Toast.LENGTH_SHORT).show();
                erroTextView.setText("Por favor, escaneie um QR Code antes de imprimir");
                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        erroTextView.setText("");
                    }
                },3000);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String barcode = result.getContents().trim();
                fetchDataFromServer(barcode);
                qrCodeScanned = true;
            } else {
                Log.e("QR_CODE", "Código QR não encontrado");
                Toast.makeText(mContext,"Código QR não encontrado", Toast.LENGTH_LONG).show();
                erroTextView.setText("Código não encontrado");
                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        erroTextView.setText("");
                    }
                },3000);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void fetchDataFromServer(String barcode) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://"+ipServidor+":8080/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ProductService service = retrofit.create(ProductService.class);

        Call<String> call = service.getProductInfo(barcode);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String responseBody = response.body();
                    // Parse HTML using Jsoup
                    Document doc = Jsoup.parse(responseBody);

                    // Extract relevant data
                    currentBarcode = doc.select("body").select("br").get(1).previousSibling().toString().split(":")[1].trim();
                    currentDescription = doc.select("body").select("br").get(2).previousSibling().toString().split(":")[1].trim();
                    currentPrice1 = doc.select("body").select("br").get(4).previousSibling().toString().split(":")[1].trim();
                    String price2 = doc.select("body").select("br").get(6).previousSibling().toString().split(":")[1].trim();
                    // Set data to TextViews
                    barcodeTextView.setText("Código de barras: " + currentBarcode);
                    descriptionTextView.setText("Descrição: " + currentDescription);
                    price1TextView.setText("Preço 1: " + currentPrice1);
                    if (price2 !=""){
                        price2TextView.setVisibility(View.VISIBLE);
                        price2TextView.setText("Preço 2: " + price2);
                    }else {
                        price2TextView.setVisibility(View.GONE);
                    }
                    printButton.setEnabled(true);
                } else {
                    // Lidar com erro
                    Log.e("Error", "Erro na requisição: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Lidar com falha na requisição
                Log.e("Error", "Falha na requisição: " + t.getMessage());
                Toast.makeText(mContext,"Falha na conexão com servidor!", Toast.LENGTH_LONG).show();
                erroTextView.setText("Falha na conexão com servidor!\n Verifique se o IP está correto");
                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        erroTextView.setText("");
                        ipEdtxt.setVisibility(View.VISIBLE);
                        ipEdtxt.setText(ipServidor);
                        ipButton.setVisibility(View.VISIBLE);
                        scanQrCodeButton.setEnabled(false);
                        printButton.setEnabled(false);
                    }
                },5000);
            }
        });
    }

    private void imprimirBitmap() {
        int ret;
        ret = mPrinter.getStatus();
        if (ret != 0) {
            Log.e("F100_test", "Print getStatus error：0x" + int2HexStr(ret));
            return;
        }
        Log.e("F100_test", "Print getStatus success:");

        boolean bHavePaper = mPrinter.isHavePaper();
        if (!bHavePaper) {
            Log.e("PRNT", "Printer no paper");
            return;
        }
        int temperature = mPrinter.getTemperature();
        Log.e("PRNT", "Temperature is:" + temperature);
        ret = mPrinter.open();
        mPrinter.startCaching();

        GondolaPriceView gondolaPriceView = new GondolaPriceView(MainActivity.this, currentDescription, currentBarcode, Double.parseDouble(currentPrice1));
        ret = mPrinter.printBmp(gondolaPriceView.gondola());
        mPrinter.print();
    }

    public static String int2HexStr(int i) {
        byte[] b = new byte[4];
        b[0] = (byte) ((i >> 24) & 0xFF);
        b[1] = (byte) ((i >> 16) & 0xFF);
        b[2] = (byte) ((i >> 8) & 0xFF);
        b[3] = (byte) (i & 0xFF);
        return byte2HexStr(b, 4);
    }

    public static String byte2HexStr(byte[] src, int len) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        int n = len;
        if (len > src.length) n = src.length;

        for (int i = 0; i < n; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
}

