package nsf.esarplab.scchealth;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class FluMethods extends AppCompatActivity {
    public TextView resultTextView;
    public Button  downloadButton;
    private static int selection=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flu_methods);

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

        // initialize widget members
        resultTextView = (TextView) findViewById(R.id.tvResult);
        //execButton = (Button)findViewById(R.id.btnExcute);
        downloadButton=(Button)findViewById(R.id.btnDownload);

        /*// set onClick listener for Execute button
        execButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //------ call the method from DEX    - fluSeverity(100) -
                String result = dexcallFluSeverity(new Integer(100));

                //------ displaying result
                resultTextView.setText("fluSeverity(100) = " + result);
            }
        });*/


        // set onClick listener for Execute button
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //------ start download background task, since main thread cannot perform network-related tasks
                Toast.makeText(FluMethods.this, "Downloading DEX file...", Toast.LENGTH_LONG).show();//notification to start downloading

                switch(selection) {
                    case 1:   new DownloadFileFromURL().execute("https://github.com/SCChealth/MEMPHIS/raw/master/fludex.dex");//starting execution of async task

                    case 2:   new DownloadFileFromURL().execute("https://github.com/SCChealth/MEMPHIS/raw/master/expflu.dex");//starting execution of async task
                }
            }
        });

    }

    /*
     * AsyncTask class for download a file from a URL
     */
    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            int count;
            try {
                URL url = new URL(params[0]); //url to download
                URLConnection conection = url.openConnection(); //open url connection using url
                conection.connect();//start connection

                String fileName = URLUtil.guessFileName(params[0], null, null);//get filename from url path string
                String dexFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + fileName; //path which the downloaded file will be located

                File dexFile = new File(dexFilePath);//file object to check file existence at above path
                if (dexFile.exists())//delete if it already exists
                    dexFile.delete();

                InputStream input = new BufferedInputStream(url.openStream(), 1024 * 5);//input stream from url
                OutputStream output = new FileOutputStream(dexFilePath);//output stream to write file content

                byte data[] = new byte[1024 * 5];//buffer for reading content
                while ((count = input.read(data)) != -1) {//read data from url connection
                    output.write(data, 0, count);//write data to storage
                }
                output.flush();//flush output stream before close
                output.close();//close stream
                input.close();//close stream
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... progress) { }

        @Override
        protected void onPostExecute(String result) {
            //---- when background task completed, update text & show notification
            resultTextView.setText("fluSeverity(100) = ?");
            Toast.makeText(FluMethods.this, "DEX file download completed", Toast.LENGTH_LONG).show();
        }
    }

    /*
     * method to call fluSeverity() method from DEX file, it accepts same parameter as fluSeverity() method
     */

}

