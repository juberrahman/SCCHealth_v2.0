package nsf.esarplab.scchealth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.common.api.GoogleApiClient;

import nsf.esarplab.bluetoothlibrary.BluetoothSPP;
import nsf.esarplab.bluetoothlibrary.BluetoothState;
import nsf.esarplab.bluetoothlibrary.DeviceList;

public class AsthmaActivity extends AppCompatActivity {
    BluetoothSPP bt;
    Button test;
    TextView hrData, oximetryData, connectionRead;
    EditText etMessage;
    //private GoogleApiClient client;
    private boolean pdReceived = false;
    private boolean hrReceived = false;
    private boolean tenReceived = false;
    private boolean poReceived=false;
    private int sensor=1;
    Menu menu;
    String s = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asthma);

        // show action bar
        ActionBar myActionBar = getSupportActionBar();
        myActionBar.show();
        TextView mNameText = (TextView) findViewById(R.id.display_name);
        bt = new BluetoothSPP(this);
        // receive intent
        test=(Button) findViewById(R.id.test);
        hrData = (TextView) findViewById(R.id.display_bdt);
        oximetryData=(TextView) findViewById(R.id.display_spo2);
        connectionRead = (TextView) findViewById(R.id.textStatus);


        //Show active profile
        SharedPreferences prefs = getSharedPreferences("logindetails",MODE_PRIVATE);
        String Uname =  prefs.getString("loginname","Default");
        mNameText.setText("\t\t"+Uname);

        s = mNameText.getText().toString().trim();
        // Set active profile


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

        // set the bluetooth connection

        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {

                //textReceived.append(message + "\n");
                if (tenReceived == true) {
                    //receive data
                    switch (sensor) {
                        case 1: {
                            hrData.append(message + "\n");
                            break;
                        }
                        case 2: {

                            oximetryData.append(message + "\n");
                            break;
                        }

                    }

                } else {
                    if (message.equals("PD")) {

                        switch (sensor) {
                            case 1: {
                                bt.send("HR", true);
                                pdReceived = true;
                                break;
                            }
                            case 2: {
                                bt.send("PO", true);
                                pdReceived = true;
                                break;
                            }
                        }
                    } else {
                        if (message.equals("HR") && pdReceived) {
                            bt.send("10", true);
                            hrReceived = true;
                        } else if (message.equals("PO") && pdReceived) {
                            bt.send("5", true);
                            poReceived = true;
                        } else {
                            if (message.equals("10") && pdReceived && hrReceived) {
                                bt.send("OK", true);
                                tenReceived = true;
                            }
                            else if (message.equals("5") && pdReceived && poReceived) {
                                bt.send("OK", true);
                                tenReceived = true;
                            } else {
                                hrData.append("Failed Handshake");
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



        // set corresponding display for selected sensor
        final LinearLayout hrlayout = (LinearLayout) findViewById(R.id.hr_result);
        final LinearLayout spo2layout = (LinearLayout) findViewById(R.id.spo2_result);
        RadioButton rbOxygen = (RadioButton) findViewById(R.id.rb_o2);
        RadioButton rbheartRate = (RadioButton) findViewById(R.id.rb_hr);
        hrlayout.setVisibility(View.VISIBLE);
        spo2layout.setVisibility(View.GONE);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rg1);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_o2) {
                    sensor=2;
                    hrlayout.setVisibility(View.GONE);
                    spo2layout.setVisibility(View.VISIBLE);
                } else {
                    hrlayout.setVisibility(View.VISIBLE);
                    spo2layout.setVisibility(View.GONE);
                }
            }
        });

// inflate the screen for info and instruction
        final TextView btnOpenPopup = (TextView) findViewById(R.id.info);
        btnOpenPopup.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                LayoutInflater layoutInflater
                        = (LayoutInflater) getBaseContext()
                        .getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.info_copd, null);
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
                View popupView = layoutInflater.inflate(R.layout.inst_copd, null);
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

    // @Override
  /* public void onStart() {
        super.onStart();
        if (!mBoundService.isBluetoothEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        } else {
            if (!mBoundService.isServiceAvailable()) {
                mBoundService.setupService();
                mBoundService.startService(BluetoothState.DEVICE_ANDROID);
                setup();
            }
        }

    }*/


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
            hrData.setText("");
            oximetryData.setText("");
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
                    bt.send("PD", true);


                }
            }
        });
    }


    @Override
    public void onStop() {
        super.onStop();

    }
}