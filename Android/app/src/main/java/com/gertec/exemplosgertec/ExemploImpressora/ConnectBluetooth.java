package com.gertec.exemplosgertec.ExemploImpressora;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class ConnectBluetooth {

    private BluetoothAdapter mBluetoothAdapter;
    private static BluetoothSocket mmSocket;
    private BluetoothDevice mmDevice;

    private Thread workerThread;
    volatile boolean stopWorker;
    byte[] readBuffer;
    int readBufferPosition;

    private static OutputStream mmOutputStream;
    private InputStream mmInputStream;



    private String sName;

    public BluetoothSocket getMmSocket() {
        return mmSocket;
    }

    public String findBT() {

        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (mBluetoothAdapter == null) {
                return "No bluetooth adapter available";
            }

            if (!mBluetoothAdapter.isEnabled()) {
//                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                startActivityForResult(enableBluetooth, 0);
            }

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            sName=sName+pairedDevices.size();
            if (pairedDevices.size() > 0) {

                for (BluetoothDevice device : pairedDevices) {

                    // RPP300 is the name of the bluetooth printer device
                    // we got this name from the list of paired devices
                    sName = sName +"<"+device.getName()+">";

                    String deviceAddress = new String(device.getAddress());
                    String deviceName = new String(device.getName());

                    //if (device.getName().equals("Inner printer")) {
                    if(
                            (deviceName.equals("BluetoothPrinter"))||
                                    (deviceAddress.equals("00:12:12:12:33:33"))||
                                    (deviceAddress.equals("00:11:22:33:44:55"))||
                                    (deviceName.equals("Inner printer"))||
                                    (deviceName.equals("InnerPrinter"))||
                                    (deviceName.equals("POS_Printer"))||
                                    (deviceName.equals("SimulatePrinter"))||
                                    (deviceName).equals("Virtual BT Printer")
                    ){
                        mmDevice = device;
                        sName=sName+" Found";
                        break;
                    }
                }
            }

            //voltar myLabel.setText("Bluetooth device found."+sName);
            sName = "Bluetooth device found.";

        } catch (Exception e) {
            sName = sName+" >>>"+e.getMessage();
            e.printStackTrace();
        }

        return sName;
    }

    public String openBT() throws IOException {
        try {

//             AlignmentSpan.Standard SerialPortService ID;
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            beginListenForData();

            return("Bluetooth Opened");

        } catch (Exception e) {
            e.printStackTrace();
            Log.w("openBT",e.getMessage());
            sName = sName + e.getMessage();

        }
        return(sName);
    }

    // close the connection to bluetooth printer.
    public String closeBT() throws IOException {
        try {
            stopWorker = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
            return ("Bluetooth Closed");
        } catch (Exception e) {
            e.printStackTrace();
            return ("Error bluetooth not Closed!");
        }
    }

    void beginListenForData() {
        try {
            final Handler handler = new Handler();

            // this is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {

                        try {

                            int bytesAvailable = mmInputStream.available();

                            if (bytesAvailable > 0) {

                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);

                                for (int i = 0; i < bytesAvailable; i++) {

                                    byte b = packetBytes[i];
                                    if (b == delimiter) {

                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length
                                        );

                                        // specify US-ASCII encoding
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        // tell the user data were sent to bluetooth printer device
                                        handler.post(new Runnable() {
                                            public void run() {
                                                sName=data;/////////////////////////////////////////////////////////////////////////////////
                                            }
                                        });

                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }
                    }
                }
            });

            workerThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public OutputStream getOutputStream(){
        return mmOutputStream;
    }
    public InputStream getInputStream(){
        return mmInputStream;
    }
}