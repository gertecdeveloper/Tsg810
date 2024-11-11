using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Text;
using System.Windows.Input;
using Xamarin.Forms;

namespace TSG810Bluetooth
{
    internal class PrintPageViewModel : INotifyPropertyChanged
    {
        private readonly IBlueToothService _blueToothService;

        private IList<string> _deviceList;
        public IList<string> DeviceList
        {
            get
            {
                if (_deviceList == null)
                    _deviceList = new ObservableCollection<string>();
                return _deviceList;
            }
            set
            {
                _deviceList = value;
                OnPropertyChanged(nameof(DeviceList));
            }
        }

        private string _printMessage;
        public string PrintMessage
        {
            get => _printMessage;
            set
            {
                _printMessage = value;
                OnPropertyChanged(nameof(PrintMessage));
            }
        }

        private string _selectDevice;
        public string SelectDevice
        {
            get => _selectDevice;
            set
            {
                _selectDevice = value;
                OnPropertyChanged(nameof(SelectDevice));
            }
        }

        public ICommand PrintCommand => new Command(async () =>
        {
            if (!string.IsNullOrEmpty(PrintMessage))
            {
                // Verifique se o PrintMessage tem o valor correto
                System.Diagnostics.Debug.WriteLine($"Mensagem para imprimir: {PrintMessage}");

                // Enviar para impressão
                await _blueToothService.Print(SelectDevice, PrintMessage);
            }
            else
            {
                System.Diagnostics.Debug.WriteLine("PrintMessage está vazio.");
            }
        });


        public PrintPageViewModel()
        {
            _blueToothService = DependencyService.Get<IBlueToothService>();
            var list = _blueToothService.GetDeviceList();
            DeviceList.Clear();
            foreach (var item in list)
            {
                DeviceList.Add(item);
            }
        }

        public event PropertyChangedEventHandler PropertyChanged;
        protected virtual void OnPropertyChanged(string propertyName)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
        }
    }
}
