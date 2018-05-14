package nsf.esarplab.scchealth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import nsf.esarplab.bluetoothlibrary.BluetoothSPP;
import nsf.esarplab.bluetoothlibrary.BluetoothState;
import nsf.esarplab.bluetoothlibrary.DeviceList;

//import com.google.android.gms.common.api.GoogleApiClient;

public class ArrhythmiaActivity extends AppCompatActivity {
    BluetoothSPP bt;
    Button test, csvReader;
    TextView textReceived, connectionRead;
    EditText etMessage;
    //private GoogleApiClient client;
    private boolean saReceived = false;
    private boolean hrReceived = false;
    private boolean tenReceived = false;
    private boolean btdata = true;
    Menu menu;
    String s = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrythmia);
        bt = new BluetoothSPP(this);
        // show actionbar
        ActionBar myActionBar = getSupportActionBar();
        myActionBar.show();
        TextView mNameText = (TextView) findViewById(R.id.display_name);

        //Show active profile
        SharedPreferences prefs = getSharedPreferences("logindetails",MODE_PRIVATE);
        String Uname =  prefs.getString("loginname","Default");
        mNameText.setText("\t\t"+Uname);

        s = mNameText.getText().toString().trim();



        /*//reading profile from file
        try {
            FileInputStream fileIn = openFileInput("mytextfile.txt");
            InputStreamReader InputRead = new InputStreamReader(fileIn);
            char[] inputBuffer = new char[READ_BLOCK_SIZE];
            *//*String s="";*//*
            int charRead;

            while ((charRead = InputRead.read(inputBuffer)) > 0) {
                // char to string conversion
                String readstring = String.copyValueOf(inputBuffer, 0, charRead);
                s += readstring;
            }
            InputRead.close();
            *//*mNameText.setText(s);*//*
            *//*Toast.makeText(getBaseContext(), s,Toast.LENGTH_SHORT).show();*//*

        } catch (Exception e) {
            e.printStackTrace();
        }
        mNameText.setText(s);*/

        // receive intent
        test=(Button) findViewById(R.id.test);
        csvReader=(Button) findViewById(R.id.readCSV);
        textReceived = (TextView) findViewById(R.id.display_name);
        connectionRead = (TextView) findViewById(R.id.textStatus);
        textReceived.setMovementMethod(new ScrollingMovementMethod());


        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {

                textReceived.append(message + "\n");
                if (btdata) {
                    try {
                        writeToCsv(message);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if (tenReceived == true) {
                    //receive data

                    textReceived.append(message + "\n");
                                                 /*try {
                                                     writeToCsv(message);
                                                 } catch (IOException e) {
                                                     // TODO Auto-generated catch block
                                                     e.printStackTrace();
                                                 }*/
                } else {
                    if (message.equals("SA")) {
                        bt.send("HR", true);
                        saReceived = true;
                    } else {
                        if (message.equals("HR") && saReceived) {
                            bt.send("10", true);
                            hrReceived = true;
                        } else {
                            if (message.equals("10") && saReceived && hrReceived) {
                                bt.send("OK", true);
                                tenReceived = true;
                            } else {
                                textReceived.append("Failed Handshake");
                            }
                        }
                    }
                }
            }

        });


        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            public void onDeviceDisconnected() {
                connectionRead.setText("Status : Not connect");
                menu.clear();
                getMenuInflater().inflate(R.menu.menu_connection, menu);
            }

            public void onDeviceConnectionFailed() {
                connectionRead.setText("Status : Connection failed");
            }

            public void onDeviceConnected(String name, String address) {
                connectionRead.setText("Status : Connected to " + name);
                menu.clear();
                getMenuInflater().inflate(R.menu.menu_disconnection, menu);
            }
        });

        /*IntentFilter filter = new IntentFilter();
        filter.addAction("SOME_ACTION");
        filter.addAction("SOME_OTHER_ACTION");

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                TextView display = (TextView) findViewById(R.id.display_name);
                display.append(action);
                *//*Log.i("Receiver", "Broadcast received: " + action);

                if (action.equals("my.action.string")) {
                    String state = intent.getExtras().getString("extra");
                    display.append(state);
                }*//*
            }

        };
        registerReceiver(receiver, filter);*/


        final TextView btnOpenPopup = (TextView) findViewById(R.id.info);
        btnOpenPopup.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                LayoutInflater layoutInflater
                        = (LayoutInflater) getBaseContext()
                        .getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.info_arrhythmia, null);
                final PopupWindow popupWindow = new PopupWindow(
                        popupView,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);

                Button btnDismiss = (Button) popupView.findViewById(R.id.dismiss);
                btnDismiss.setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        popupWindow.dismiss();
                    }
                });

                popupWindow.showAsDropDown(btnOpenPopup, 50, -30);

            }
        });

        final TextView btnOpenInstruction = (TextView) findViewById(R.id.inst);
        btnOpenInstruction.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                LayoutInflater layoutInflater
                        = (LayoutInflater) getBaseContext()
                        .getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.inst_arrhythmia, null);
                final PopupWindow popupWindow = new PopupWindow(
                        popupView,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);

                Button btnDismiss = (Button) popupView.findViewById(R.id.dismiss);
                btnDismiss.setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        popupWindow.dismiss();
                    }
                });

                popupWindow.showAsDropDown(btnOpenPopup, 50, -30);

            }
        });


    }

    public void onDestroy() {
        super.onDestroy();
        bt.stopService();
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
        }

        else if (id == R.id.menu_reinitialize) {
            textReceived.setText("");
        }
        return super.onOptionsItemSelected(item);
    }
    public void onStart() {
        super.onStart();
        if (!bt.isBluetoothEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        } else {
            if (!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_ANDROID);
                setup();
                connectScanner();

            }
        }

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

    public void setup() {

        test.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                {
                    bt.send("SA", true);


                }
            }
        });
    }

    public void connectScanner() {

        csvReader.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


            }
        });
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
            String csv = "/storage/sdcard0/project/btvalue.csv";
            FileWriter file_writer = new FileWriter(csv,true);;




            String s= c.get(Calendar.YEAR)+","+(c.get(Calendar.MONTH)+1)+","+c.get(Calendar.DATE)+","+c.get(Calendar.HOUR)+","+c.get(Calendar.MINUTE)+","+c.get(Calendar.SECOND)+","+ c.get(Calendar.MILLISECOND)+","+x + "\n";

            file_writer.append(s);
            file_writer.close();

        }



    }
    @Override
    public void onStop() {
        super.onStop();

    }
}
