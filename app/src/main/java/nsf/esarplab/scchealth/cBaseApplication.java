package nsf.esarplab.scchealth;

import android.app.Application;

import nsf.esarplab.bluetoothlibrary.BluetoothSPP;

/**
 * Created by mrahman8 on 2/7/2017.
 */

public class cBaseApplication extends Application {
    public BluetoothSPP bluetoothSPP;
    //public static volatile int fileSeq=1;

    @Override
    public void onCreate()
    {
        super.onCreate();
        bluetoothSPP = new BluetoothSPP(this);


    }
}