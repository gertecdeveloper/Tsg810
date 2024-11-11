using Android.Bluetooth;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Xamarin.Forms;
using System.Diagnostics;
using TSG810Bluetooth.Droid;
using Java.Util;



[assembly: Xamarin.Forms.Dependency(typeof(AndroidBlueToothService))]

namespace TSG810Bluetooth.Droid
{
    public class AndroidBlueToothService : IBlueToothService
    {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.DefaultAdapter;

        public IList<string> GetDeviceList()
        {
            if (bluetoothAdapter == null || !bluetoothAdapter.IsEnabled)
            {
                return new List<string>(); // Retorna uma lista vazia se o Bluetooth não estiver disponível
            }

            var btDevice = bluetoothAdapter.BondedDevices.Select(i => i.Name).ToList();
            return btDevice;
        }

        public async Task Print(string deviceName, string text)
        {
            BluetoothDevice device = (from bd in bluetoothAdapter?.BondedDevices
                                      where bd?.Name == deviceName
                                      select bd).FirstOrDefault();

            try
            {
                await Task.Delay(1000);

                BluetoothSocket bluetoothSocket = device?.CreateRfcommSocketToServiceRecord(
                    UUID.FromString("0001101-0000-1000-8000-00805f9b34fb"));

                bluetoothSocket?.Connect();

                // Envia o comando de inicialização, se necessário
                byte[] initCommand = new byte[] { 0x1B, 0x40 }; // ESC/POS inicialização
                bluetoothSocket?.OutputStream.Write(initCommand, 0, initCommand.Length);

                // Envia o texto com uma nova linha no final
                byte[] buffer = Encoding.ASCII.GetBytes(text + "\n");
                bluetoothSocket?.OutputStream.Write(buffer, 0, buffer.Length);

                // Comando de nova linha ESC/POS, se necessário
                byte[] newLine = new byte[] { 0x0A };
                bluetoothSocket?.OutputStream.Write(newLine, 0, newLine.Length);

                bluetoothSocket.Close();
            }
            catch (Exception ex)
            {
                System.Diagnostics.Debug.WriteLine(ex.Message);
                throw;
            }
        }

    }
}