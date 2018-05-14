package nsf.esarplab.scchealth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;

import dalvik.system.DexClassLoader;

@SuppressLint("NewApi")
public class FluAlgorithm extends AppCompatActivity {
    private long enqueue;
    private DownloadManager dm;
    private TextView tv1;
    private String s;
    Activity context;
    private TextView txtview;
    private String dexResult;
    private int selection=1;
    Button b1;
    ProgressDialog pd;
    DexClassLoader dexClassLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flu_algorithm);

        // show action bar
        ActionBar myActionBar = getSupportActionBar();
        myActionBar.show();


        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rgMethod);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                if(checkedId==R.id.linAlg){
                    selection=1;
                }
                else if(checkedId==R.id.expAlg){
                    selection=2;
                }
            }
        });

        //selection=2;
        //tv1=(TextView) findViewById(R.id.textDownload);
        txtview=(TextView)findViewById(R.id.textview);
        // prepare for dex class loading

        CustomizedDexClassLoader.setContext(this);
        switch(selection){

            case 1: try {
                dexClassLoader = CustomizedDexClassLoader.load("libflu.dex");
            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            }

                break;

            case 2:
                try {
                    dexClassLoader = CustomizedDexClassLoader.load("expflu.dex");
                } catch (RuntimeException e) {
                    throw new RuntimeException(e);
                }
                break;
        }

        // download task

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    long downloadId = intent.getLongExtra(
                            DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                    Query query = new Query();
                    query.setFilterById(enqueue);
                    Cursor c = dm.query(query);
                    if (c.moveToFirst()) {
                        int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {

                            Toast.makeText(getBaseContext(), "Download Finished", Toast.LENGTH_SHORT).show();
                            /*ImageView view = (ImageView) findViewById(R.id.imageView1);
                            String uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                            view.setImageURI(Uri.parse(uriString));*/
                        }
                    }
                }
            }
        };

        registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        /*BackTask bt=new BackTask();
        bt.execute("http://www.textfiles.com/news/bucks.txt");*/


    }

    public void onStart(){
        super.onStart();

        // load dex class
        try {

            Class params[] = { };
            Object paramsObj[] = { };

            Class<?> wordClass = dexClassLoader.loadClass("com.example.Algorithm");
            Object iClass = wordClass.newInstance();
            Method thisMethod = wordClass.getMethod("eoiValue", params);
            dexResult=thisMethod.invoke(iClass, paramsObj).toString();

        } catch (ClassNotFoundException e) {
            Log.i("exception","Class Not Found");
        } catch (NoSuchMethodException e) {
            Log.i("exception","No Such Method");
        } catch (IllegalAccessException e) {
        } catch (IllegalArgumentException e) {
        } catch (InvocationTargetException e) {
        } catch (InstantiationException e) {
        }

        // background task for download

        BackTask bt=new BackTask();

        switch(selection) {
            case 1:
                bt.execute("https://github.com/SCChealth/MEMPHIS/raw/master/libflu.dex");
                break;
            case 2:
                bt.execute("https://github.com/SCChealth/MEMPHIS/raw/master/expflu.dex");
                break;

        }
    }

    //background process to download the file from internet
    private class BackTask extends AsyncTask<String,Integer,Void> {
        String text="";
        protected void onPreExecute(){
            super.onPreExecute();
            //display progress dialog
           /* pd = new ProgressDialog(context);
            pd.setTitle("Reading the text file");
            pd.setMessage("Please wait.");
            pd.setCancelable(true);
            pd.setIndeterminate(false);
            pd.show();*/

        }


        protected Void doInBackground(String...params){
            URL url;
            try {
                //create url object to point to the file location on internet
                //url = new URL(params[0]);
                url = new URL(params[0]);
                //make a request to server
                HttpURLConnection con=(HttpURLConnection)url.openConnection();


                //get InputStream instance
                /*InputStream is=con.getInputStream();
                //create BufferedReader object
                BufferedReader br=new BufferedReader(new InputStreamReader(is));
                String line;
                //read content of the file line by line
                while((line=br.readLine())!=null){
                    text+=line;

                }

                br.close();*/

            }catch (Exception e) {
                e.printStackTrace();
                //close dialog if error occurs
                if(pd!=null) pd.dismiss();
            }

            return null;

        }

        protected void onPostExecute(Void result){
            //close dialog
            if(pd!=null)
                pd.dismiss();
            //display read text in TextVeiw
            //txtview.setText(text);
            //txtview.append("end");

        }

    }

    public void onClick(View view) {
        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        switch(selection) {
            case 1:
                DownloadManager.Request  request = new DownloadManager.Request(
                        Uri.parse("https://github.com/SCChealth/MEMPHIS/raw/master/libflu.dex"));
                enqueue = dm.enqueue(request);
            case 2:
                DownloadManager.Request  request2 = new DownloadManager.Request(
                        Uri.parse("https://github.com/SCChealth/MEMPHIS/raw/master/expflu.dex"));
                enqueue = dm.enqueue(request2);

        }



        //request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, "libflu.dex");
    }

    public void showDownload(View view) {
        Intent i = new Intent();
        i.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
        startActivity(i);
    }

    public void executeDexLoad(View view) {
        Log.i("DexLoad", "Called");
        txtview.append(dexResult);



    }



}