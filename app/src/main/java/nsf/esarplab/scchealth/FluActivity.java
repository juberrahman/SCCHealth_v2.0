package nsf.esarplab.scchealth;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import nsf.esarplab.bluetoothlibrary.BluetoothSPP;
import nsf.esarplab.bluetoothlibrary.BluetoothState;
import nsf.esarplab.bluetoothlibrary.DeviceList;
import dalvik.system.DexClassLoader;

public class FluActivity extends AppCompatActivity {
    BluetoothSPP bt;
    double ratingOfEOI = 0.0;
    Float severityRating;
    String s="";
    Intent mIntent;
    private int fileSeq=1;
    private TextView connectionRead;
    private TextView mNameText;
    private TextView mDateTime;
    private TextView mTemperature;
    private TextView mEoi;
    private Button connectScanner, test, dispResult;
    //private WifiManager wifiManager;
    private boolean diseaseKey = false;
    private boolean sensorKey = false;
    private boolean timeKey = false;
    private boolean connected=false;
    private int sensor = 1;
    private String tempReceived, eoiValue;
    private String currentDateTime = "";
    private TextView Vdatetime, gradient, Textv, Veoi, Vprompt;
    //String mealId = " ";
    private String eoiRating = "0";
    private String prompt = " ";
    private String sEOI="";
    private String sTemperature="";
    private Menu menu;
    private ImageView arrow1, arrow2, arrow3, arrow4, arrow5, arrow6, arrow7, arrow8, arrow9, arrow10, arrow11;
    private LinearLayout mDisplay;
    private ArrayList<String> arr_hex = new ArrayList<String>();
    private ArrayList<Short> arr_received = new ArrayList<Short>();
    private ProgressDialog progressDialog;
    private CountDownTimer Count;
    //temp db finish

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bt = new BluetoothSPP(this);
        // ((cBaseApplication)this.getApplicationContext()).myBlueComms.BluetoothConnectionListener();

       /* if(bt.isBluetoothEnabled())
            Toast.makeText(this,"b on",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this,"b off",Toast.LENGTH_SHORT).show();
*/
        setContentView(R.layout.activity_body_temp);

        // show action bar
        ActionBar myActionBar = getSupportActionBar();
        myActionBar.show();


        /*startService(new Intent(this, BluetoothSPP.class));
        bindService(mIntent, mConnection, BIND_AUTO_CREATE);*/

        // Find all relevant views that we will need to read user input from

        mNameText = (TextView) findViewById(R.id.display_name);
        mDateTime = (TextView) findViewById(R.id.datetime);
        mTemperature = (TextView) findViewById(R.id.display_bdt);
        mEoi = (TextView) findViewById(R.id.display_eoi);
        test = (Button) findViewById(R.id.test);
        dispResult = (Button) findViewById(R.id.displayResult);
        connectScanner = (Button) findViewById(R.id.cScanner);
        Vdatetime = (TextView) findViewById(R.id.datetime);
        Textv = (TextView) findViewById(R.id.display_bdt);
        Veoi = (TextView) findViewById(R.id.display_eoi);
        Vprompt = (TextView) findViewById(R.id.display_prompt);
        connectionRead = (TextView) findViewById(R.id.textStatus);
        mDisplay = (LinearLayout) findViewById(R.id.maindisplay);
        // Set active profile

        //Show active profile
        SharedPreferences prefs = getSharedPreferences("logindetails",MODE_PRIVATE);
        String Uname =  prefs.getString("loginname","Default");
        mNameText.setText("\t\t"+Uname);

        s = mNameText.getText().toString().trim();

        /*
        //reading text from file
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


        // hide main display

        mDisplay.setVisibility(View.INVISIBLE);

// Find the View that shows the save button
        /*Button save = (Button) findViewById(R.id.save);

        // Set a click listener on that View
        save.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers category is clicked on.
            @Override
            public void onClick(View view) {
                // Save record to database
                insertTemp();
                // Exit activity
                finish();

            }
        });*/


        // color line gradient


        gradient = (TextView) findViewById(R.id.active_gradient);


        int[] colors = {Color.parseColor("#008000"), Color.parseColor("#FFFF00"), Color.parseColor("#FFA500"), Color.parseColor("#ff0000"), Color.parseColor("#800000")};
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT, colors);
        gradient.setBackground(gd);

        // pop up window for info and instruction

        final TextView btnOpenPopup = (TextView) findViewById(R.id.info);
        btnOpenPopup.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                LayoutInflater layoutInflater
                        = (LayoutInflater) getBaseContext()
                        .getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.info_bodytemp, null);
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
                View popupView = layoutInflater.inflate(R.layout.inst_bodytemp, null);
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

        // connect scanner by bluetooth

        connectScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bt.setDeviceTarget(BluetoothState.DEVICE_OTHER);
            /*
			if(bt.getServiceState() == BluetoothState.STATE_CONNECTED)
    			bt.disconnect();*/
                Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);

            }
        });

        //EOI color ranking
        //LinearLayout mainDisplay=(LinearLayout) findViewById(R.id.maindisplay);
        arrow1 = (ImageView) findViewById(R.id.arrow1);
        arrow2 = (ImageView) findViewById(R.id.arrow2);
        arrow3 = (ImageView) findViewById(R.id.arrow3);
        arrow4 = (ImageView) findViewById(R.id.arrow4);
        arrow5 = (ImageView) findViewById(R.id.arrow5);
        arrow6 = (ImageView) findViewById(R.id.arrow6);
        arrow7 = (ImageView) findViewById(R.id.arrow7);
        arrow8 = (ImageView) findViewById(R.id.arrow8);
        arrow9 = (ImageView) findViewById(R.id.arrow9);
        arrow10 = (ImageView) findViewById(R.id.arrow10);
        arrow11 = (ImageView) findViewById(R.id.arrow11);

        //mainDisplay.setVisibility(View.INVISIBLE);


        // set the bluetooth connection

        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
                short val = 0;
                String readAscii = new String(data);
                //Log.i("Str@activity", readAscii);
                //textReceived.append(message + "\n");
                if (timeKey) {
                    //receive data

                    arr_hex.add(message);
                    Log.i("size_arr_hex",""+arr_hex.size());


                    if (arr_hex.size() == 2) {
                        String catHex = arr_hex.get(0) + arr_hex.get(1);
                        /*int b0 = (arr_hex.get(0) & 255); // converts to unsigned
                        int b1 = (arr_hex.get(1) & 255); // converts to unsigned
                        int val = b0 << 8 | b1;*/
                        Log.i("val1@final", catHex);
                        val = (short) (Integer.parseInt(catHex, 16));
                        Log.i("val2@final", String.valueOf(val));
                        arr_received.add(val);
                        //Textv.append(Integer.toString(val) + "\n");
                        try {
                            writeToCsv(Integer.toString(val));
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        arr_hex.clear();
                        Log.i("sizearr_received", "" + arr_received.size());
                    }


                } else {
                    if (readAscii.equals("BT")) {

                        bt.send("TP", true);
                        diseaseKey = true;

                    } else {
                        if (readAscii.equals("TP") && diseaseKey) {
                            bt.send("05", true);
                            sensorKey = true;
                        } else {
                            if (readAscii.equals("05") && diseaseKey && sensorKey) {
                                bt.send("OK", true);
                                timeKey = true;

                            } else {
                                mDisplay.setVisibility(View.VISIBLE);
                                arrow1.setVisibility(View.INVISIBLE);
                                arrow2.setVisibility(View.INVISIBLE);
                                arrow3.setVisibility(View.INVISIBLE);
                                arrow4.setVisibility(View.INVISIBLE);
                                arrow5.setVisibility(View.INVISIBLE);
                                arrow6.setVisibility(View.INVISIBLE);
                                arrow7.setVisibility(View.INVISIBLE);
                                arrow8.setVisibility(View.INVISIBLE);
                                arrow9.setVisibility(View.INVISIBLE);
                                arrow10.setVisibility(View.INVISIBLE);
                                arrow11.setVisibility(View.INVISIBLE);
                                Textv.setText("Failed Handshake");
                                Count.cancel();
                                progressDialog.dismiss();

                            }
                        }
                    }
                }
            }

        });

        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            public void onDeviceDisconnected() {
                connectionRead.setText("Status : Not connect");
                connected=false;
                menu.clear();
                getMenuInflater().inflate(R.menu.menu_connection, menu);
            }

            public void onDeviceConnectionFailed() {
                connectionRead.setText("Status : Connection failed");
            }

            public void onDeviceConnected(String name, String address) {
                connectionRead.setText("Status : Connected to " + name);
                connected=true;
                menu.clear();
                getMenuInflater().inflate(R.menu.menu_disconnection, menu);
            }
        });
    }

    /**
     * save new entry  into database.
     */
    private void insertTemp() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = mNameText.getText().toString().trim();
        String dateString = mDateTime.getText().toString().trim();
        String valueString = mTemperature.getText().toString().trim();
        String eoiString = eoiValue;


        // Create database helper
        TempDbHelper mDbHelper = new TempDbHelper(this);

        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(TempContract.TempEntry.COLUMN_PATIENT_NAME, nameString);
        values.put(TempContract.TempEntry.COLUMN_DATE_TIME, dateString);
        values.put(TempContract.TempEntry.COLUMN_TEMP_VALUE, sTemperature);
        values.put(TempContract.TempEntry.COLUMN_EOI_RATING, sEOI);

        // Insert a new row  in the database, returning the ID of that new row.
        long newRowId = db.insert(TempContract.TempEntry.TABLE_NAME, null, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, "saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }


    public void onDestroy() {
        super.onDestroy();
        bt.stopService();
    }


    // menu options

    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_connection, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_android_connect:
                bt.setDeviceTarget(BluetoothState.DEVICE_ANDROID);
            /*
            if(bt.getServiceState() == BluetoothState.STATE_CONNECTED)
    			bt.disconnect();*/
                Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                return true;


            case R.id.menu_device_connect:
                bt.setDeviceTarget(BluetoothState.DEVICE_OTHER);
			/*
			if(bt.getServiceState() == BluetoothState.STATE_CONNECTED)
    			bt.disconnect();*/
                Intent intent2 = new Intent(getApplicationContext(), DeviceList.class);
                startActivityForResult(intent2, BluetoothState.REQUEST_CONNECT_DEVICE);
                return true;

            case R.id.menu_disconnect:
                if (bt.getServiceState() == BluetoothState.STATE_CONNECTED)
                    bt.disconnect();


                return true;


            case R.id.menu_reinitialize:
                Textv.setText("");
                Vdatetime.setText("");
                Vprompt.setText("");
                Veoi.setText("");
                mDisplay.setVisibility(View.INVISIBLE);
                diseaseKey = false;
                sensorKey = false;
                timeKey = false;
                return true;

            case R.id.action_save:
                // Save record to database
                insertTemp();
                // Exit activity
                //finish();
                return true;
            // Respond to a click on the "Share to SCC" menu option
            case R.id.action_share:


                if(s.matches("")) {
                    Toast.makeText(getApplicationContext(), "Create Profile First ", Toast.LENGTH_LONG).show();

                }else{

                    // Go to cloud activity
                    Intent shareIntent = new Intent(FluActivity.this, CloudActivity.class);
                    //Bundle extras = new Bundle();
                    shareIntent.putExtra("DT", "BT");
                    shareIntent.putExtra("profile", s);
                    shareIntent.putExtra("EOI", eoiValue);
                    shareIntent.putExtra("Time", currentDateTime);
                    shareIntent.putExtra("Algorithm", "BT1");
                    startActivity(shareIntent);
                    return true;

                }
            case R.id.action_sms:
                String messageToSend = "EOI:" + eoiValue;
                String number = "9015157371";

                SmsManager.getDefault().sendTextMessage(number, null, messageToSend, null, null);
                //finish();
                return true;
            case R.id.action_history:

                // Create a new intent to open the {@link Temperature History}
                Intent temperatureHistoryIntent = new Intent(FluActivity.this, Temp_HistoryActivity.class);

                // Start the new activity
                startActivity(temperatureHistoryIntent);
                //finish();
                return true;

            case R.id.action_algorithm:
                Intent algIntent = new Intent(FluActivity.this, FluMethods.class);
                startActivity(algIntent);
                return true;
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
                    if (connected) {
                        bt.send("BT", true);
                        // progress indicator
                        progressDialog = new ProgressDialog(FluActivity.this,
                                R.style.AppTheme_Dark_Dialog);
                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("Collecting data...");

                        Count=new CountDownTimer(10000, 1000) {

                            public void onTick(long millisecondsUntilDone) {

                                progressDialog.show();
                            }

                            @Override
                            public void onFinish() {
                                Log.i("Done", "Count Down Timer Finished");
                                progressDialog.dismiss();
                                AlertDialog alertDialog = new AlertDialog.Builder(FluActivity.this).create();
                                alertDialog.setTitle("Instruction");
                                alertDialog.setMessage("Click on COMPUTE to See Test Result");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                            }
                        }.start();

                    } else {
                        Toast.makeText(getApplicationContext(), "Get Connected First", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    @Override
    public void onStop() {
        super.onStop();


    }


    public void tempAlgorithm(View v) {

        ArrayList<Short> arr_trans = new ArrayList<Short>();
        ArrayList<Short> arr_processed1 = new ArrayList<Short>();
        ArrayList<Short> arr_processed2 = new ArrayList<Short>();
        float sum = 0.0f;
        float sum1 = 0.0f;
        float sum2 = 0.0f;
        float avgValue = 0.0f;
        float avgValue1 = 0.0f;
        float avgValue2 = 0.0f;
        float resultVoltage=0.0f;
        float temperature=0.0f;

        // make main display visible and hide arrows
        mDisplay.setVisibility(View.VISIBLE);
        arrow1.setVisibility(View.INVISIBLE);
        arrow2.setVisibility(View.INVISIBLE);
        arrow3.setVisibility(View.INVISIBLE);
        arrow4.setVisibility(View.INVISIBLE);
        arrow5.setVisibility(View.INVISIBLE);
        arrow6.setVisibility(View.INVISIBLE);
        arrow7.setVisibility(View.INVISIBLE);
        arrow8.setVisibility(View.INVISIBLE);
        arrow9.setVisibility(View.INVISIBLE);
        arrow10.setVisibility(View.INVISIBLE);
        arrow11.setVisibility(View.INVISIBLE);

        // display when there is no data
        if (arr_received.size() == 0) {
            mDisplay.setVisibility(View.VISIBLE);
            Textv.setText("No Data");
            Veoi.setText("No Data");
        } else {

            // get date and time

            currentDateTime = DateFormat.getDateTimeInstance().format(new Date());
            // initialize the screen
            Vdatetime.setText("");
            Textv.setText("");
            Vprompt.setText("");
            Veoi.setText("");

            // temperature processing begin
            for (int i = 0; i < arr_received.size(); i++) {
                if ((arr_received.get(i)>1000)&&(arr_received.get(i)<9000)) {
                    arr_trans.add(arr_received.get(i));

                    Log.i("transferrred", "" + arr_received.get(i));

                }
            }
            Log.i("sizer", "" + arr_received.size());
            Log.i("sizet", "" + arr_trans.size());
            for (int j = 0; j < arr_trans.size(); j++) {
                sum += arr_trans.get(j);
            }
            avgValue = sum / arr_trans.size();

            Log.i("Avg", "" + avgValue);

            for (int k = 0; k < arr_trans.size(); k++) {
                if (arr_trans.get(k) >= avgValue) {
                    arr_processed1.add(arr_trans.get(k));

                } else {
                    arr_processed2.add(arr_trans.get(k));
                }
            }

            Log.i("size1", "" + arr_processed1.size());
            Log.i("size2", "" + arr_processed2.size());
            for (int j = 0; j < arr_processed1.size(); j++) {
                sum1 += arr_processed1.get(j);
            }
            avgValue1 = sum1 / arr_processed1.size();

            for (int j = 0; j < arr_processed2.size(); j++) {
                sum2 += arr_processed2.get(j);
            }
            avgValue2 = sum2 / arr_processed2.size();

            resultVoltage = avgValue1 - avgValue2;
            Log.i("Result", "" + resultVoltage);

            temperature=(resultVoltage/350)*75;

            String sSeverity="";
            //String result = dexcallFluSeverity(new Integer(100));
            String result = dexcallFluSeverity(new Integer(Math.round(resultVoltage)));


            try {
                sTemperature=String.valueOf(new DecimalFormat("###.##").format(temperature));
                ratingOfEOI = (temperature - 97) / 10;
                sEOI = new DecimalFormat("##.##").format(ratingOfEOI);
                sSeverity = new DecimalFormat("##.##").format(100 * ratingOfEOI);

                if (temperature<= 97.5) {
                    prompt = "Normal Temperature";
                    arrow1.setVisibility(View.VISIBLE);
                    sEOI="0.0";
                    sSeverity="0.0";
                } else if (temperature <= 98.5) {
                    prompt = "Normal Temperature";
                    arrow2.setVisibility(View.VISIBLE);
                } else if (temperature <= 99.5) {
                    prompt = "Normal Temperature";
                    arrow3.setVisibility(View.VISIBLE);
                } else if (temperature <= 100.5) {
                    prompt = "Normal Temperature";
                    arrow4.setVisibility(View.VISIBLE);
                } else if (temperature <= 101.5) {
                    prompt = "Low Fever,\nconsider consulting your doctor";
                    arrow5.setVisibility(View.VISIBLE);
                } else if (temperature <= 102.5) {
                    prompt = "Medium Fever,\nConsult your doctor";
                    arrow6.setVisibility(View.VISIBLE);
                } else if (temperature <= 103.5) {
                    prompt = "High Fever,\nConsult your doctor";
                    arrow7.setVisibility(View.VISIBLE);
                } else if (temperature <= 104.5) {
                    prompt = "High Fever,\nConsult your doctor";
                    arrow8.setVisibility(View.VISIBLE);
                } else if (temperature <= 105.5) {
                    prompt = "Very High Fever,\nConsult your doctor immediately";
                    arrow9.setVisibility(View.VISIBLE);
                } else if (temperature <= 106.5) {
                    prompt = "Very High Fever,\nConsult your doctor immediately";
                    arrow10.setVisibility(View.VISIBLE);
                } else if (temperature >= 106.5) {
                    prompt = "Extremely High Fever,\nConsult your doctor immediately";
                    arrow11.setVisibility(View.VISIBLE);
                }

            } catch (NumberFormatException e) {

                prompt = "Invalid Data";
                gradient.setVisibility(View.INVISIBLE);
            }



//------ displaying result

            Vdatetime.setText(currentDateTime);

            //Textv.append(sTemperature+"°F");
            Textv.append(resultVoltage+"°F");

            Vprompt.append(prompt );
            //Veoi.append("fluSeverity(100) = " + result);

            Veoi.append( result);
        }

        // end temperature processing
        arr_received.clear();
        Log.i("sizearr_received", "" + arr_received.size());
        arr_trans.clear();
        arr_processed1.clear();
        arr_processed2.clear();
        fileSeq++;
        timeKey = false;
        diseaseKey = false;
        sensorKey = false;
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
            String fileName = "flu" + String.valueOf(fileSeq) + ".csv";
            String csv = "/storage/emulated/0/project/"+fileName;
            FileWriter file_writer = new FileWriter(csv, true);
            String s = c.get(Calendar.YEAR) + "," + (c.get(Calendar.MONTH) + 1) + "," + c.get(Calendar.DATE) + "," + c.get(Calendar.HOUR) + "," + c.get(Calendar.MINUTE) + "," + c.get(Calendar.SECOND) + "," + c.get(Calendar.MILLISECOND) + "," + x + "\n";

            file_writer.append(s);
            file_writer.close();


        }
    }

    public String dexcallFluSeverity(Integer temp) {
        try {
            final String libPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS ) + "/fludex.dex";//path to DEX file to load
            final File tmpDir = getDir("dex", 0);//temp directory optimized dex files should be written
            final DexClassLoader classloader = new DexClassLoader(libPath, tmpDir.getAbsolutePath(), null, this.getClass().getClassLoader());//create DexClassLoader object
            final Class<Object> classToLoad = (Class<Object>) classloader.loadClass("com.example.eoiValue");//load class with class name - eoiValue
            final Object myInstance  = classToLoad.newInstance();//create a instance of the class loaded above
            final Method doSomething = classToLoad.getMethod("fluSeverity", Integer.class);//get method of the class loaded above

            String result  = (String) doSomething.invoke(myInstance, temp);//finally, invoke the method of the instance, while passing parameter value
            return result;//return the method invocation result
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
