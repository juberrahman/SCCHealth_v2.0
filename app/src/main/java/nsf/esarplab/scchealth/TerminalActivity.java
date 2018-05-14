package nsf.esarplab.scchealth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import nsf.esarplab.bluetoothlibrary.BluetoothSPP;
import nsf.esarplab.bluetoothlibrary.BluetoothSPP.BluetoothConnectionListener;
import nsf.esarplab.bluetoothlibrary.BluetoothSPP.OnDataReceivedListener;
import nsf.esarplab.bluetoothlibrary.BluetoothState;
import nsf.esarplab.bluetoothlibrary.DeviceList;

//import com.google.android.gms.appindexing.Action;
//import com.google.android.gms.appindexing.AppIndex;
//import com.google.android.gms.appindexing.Thing;
//import com.google.android.gms.common.api.GoogleApiClient;

public class TerminalActivity extends AppCompatActivity {
    BluetoothSPP bt;

    TextView textStatus, textRead;
    EditText etMessage;
    private ArrayList<String> arr_hex = new ArrayList<String>();
    Menu menu;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminal);
        Log.i("Check", "onCreate");

        ActionBar myActionBar = getSupportActionBar();
        myActionBar.show();

        textRead = (TextView) findViewById(R.id.textRead);
        textStatus = (TextView) findViewById(R.id.textStatus);
        etMessage = (EditText) findViewById(R.id.etMessage);

        bt = new BluetoothSPP(this);
        //  BluetoothSPP bt = ((cBaseApplication) this.getApplicationContext()).bluetoothSPP.;
        /*((cBaseApplication) this.getApplicationContext()).onCreate();
        BluetoothSPP bt =( (cBaseApplication)this.getApplicationContext()).bluetoothSPP;*/

        if (!bt.isBluetoothAvailable()) {
            Toast.makeText(getApplicationContext()
                    , "BluetoothActivity is not available"
                    , Toast.LENGTH_SHORT).show();
            //finish();
        }


        // cBaseApplication.


        bt.setOnDataReceivedListener(new OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
                int val = 0;
                String readAscii = new String(data);
                //textRead.append(readAscii);
                Log.i("Str@activity", String.valueOf(readAscii));

                arr_hex.add(message);
                if (arr_hex.size() == 2) {
                    String catHex = arr_hex.get(0) + arr_hex.get(1);
                        /*int b0 = (arr_hex.get(0) & 255); // converts to unsigned
                        int b1 = (arr_hex.get(1) & 255); // converts to unsigned
                        int val = b0 << 8 | b1;*/
                    Log.i("val1@final", catHex);
                    val= (int) Long.parseLong(catHex, 16);
                    //val = (short) (Integer.parseInt(catHex, 16));
                    Log.i("val2@final", String.valueOf(val));
                    textRead.append(Integer.toString(val) + "\n");
                    try {
                        writeToCsv(Integer.toString(val));
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    arr_hex.clear();
                }

            }
        });

        bt.setBluetoothConnectionListener(new BluetoothConnectionListener() {
            public void onDeviceDisconnected() {
                textStatus.setText("Status : Not connect");
                menu.clear();
                getMenuInflater().inflate(R.menu.menu_connection, menu);
            }

            public void onDeviceConnectionFailed() {
                textStatus.setText("Status : Connection failed");
            }

            public void onDeviceConnected(String name, String address) {
                textStatus.setText("Status : Connected to " + name);
                menu.clear();
                getMenuInflater().inflate(R.menu.menu_disconnection, menu);
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_connection, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_android_connect) {
            bt.setDeviceTarget(BluetoothState.DEVICE_ANDROID);
            /*
			if(bt.getServiceState() == BluetoothState.STATE_CONNECTED)
    			bt.disconnect();*/
            Intent intent = new Intent(getApplicationContext(), DeviceList.class);
            startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);


        } else if (id == R.id.menu_device_connect) {
            bt.setDeviceTarget(BluetoothState.DEVICE_OTHER);
			/*
			if(bt.getServiceState() == BluetoothState.STATE_CONNECTED)
    			bt.disconnect();*/
            Intent intent = new Intent(getApplicationContext(), DeviceList.class);
            startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
        } else if (id == R.id.menu_disconnect) {
            if (bt.getServiceState() == BluetoothState.STATE_CONNECTED)
                bt.disconnect();
        }else if (id == R.id.menu_reinitialize) {
            textRead.setText("");

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onDestroy() {
        super.onDestroy();
        bt.stopService();
    }

    public void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        if (!bt.isBluetoothEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        } else {
            if (!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_ANDROID);
                setup();
            }
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    public void setup() {
        Button btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (etMessage.getText().length() != 0) {
                    bt.send(etMessage.getText().toString(), true);
                    etMessage.setText("");
                }
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                bt.connect(data);
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_ANDROID);
                setup();
            } else {
                Toast.makeText(getApplicationContext()
                        , "BluetoothActivity was not enabled."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    /**
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Terminal Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }
**/

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    //write to csv file
    public void writeToCsv(String x) throws IOException {
        Calendar c = Calendar.getInstance();
        File folder = new File(Environment.getExternalStorageDirectory() + "/project");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }
        if (success) {
            // Do something on success
            String csv = "/storage/emulated/0/project/btcommon.csv";
            FileWriter file_writer = new FileWriter(csv, true);
            String s = c.get(Calendar.YEAR) + "," + (c.get(Calendar.MONTH) + 1) + "," + c.get(Calendar.DATE) + "," + c.get(Calendar.HOUR) + "," + c.get(Calendar.MINUTE) + "," + c.get(Calendar.SECOND) + "," + c.get(Calendar.MILLISECOND) + "," + x + "\n";

            file_writer.append(s);
            file_writer.close();

        }
    }
}