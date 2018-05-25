package nsf.esarplab.scchealth;


import android.annotation.SuppressLint;
import android.app.Activity;

@SuppressLint("SetJavaScriptEnabled")
public class ShowWebChart extends Activity {
    /*private TempDbHelper mydb;
    WebView webView;
    int num1, num2, num3, num4, num5, num6;
    LineGraphSeries<DataPoint>series;
    GraphView graph;
    SQLiteDatabase ourDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_web_chart);

        graph = (GraphView) findViewById(R.id.graph);


        mydb = new TempDbHelper(this);
        ourDatabase = mydb.getReadableDatabase();

        //graph.addSeries(series);



        String[] columns = new String[]{_ID, COLUMN_PATIENT_NAME, COLUMN_DATE_TIME, COLUMN_TEMP_VALUE, COLUMN_EOI_RATING};
        Cursor c = ourDatabase.query(TempContract.TempEntry.TABLE_NAME, columns, null, null, null, null, null);
        DataPoint[] dp = new DataPoint[c.getCount()];
        int iRow = c.getColumnIndex(COLUMN_TEMP_VALUE);

        for (int i = 0;i<c.getCount();i++) {
            c.moveToNext();
            dp[i]=new DataPoint(c.getInt(2),c.getInt(3));

            Log.i("ValueFetched",""+dp[i]);

        }

        series = new LineGraphSeries <>(dp);
// set manual X bounds
        *//*graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(-150);
        graph.getViewport().setMaxY(150);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(4);
        graph.getViewport().setMaxX(80);

        // enable scaling and scrolling
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);
*//*

        graph.addSeries(series);

        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(ShowWebChart.this));
        graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

        series.setDrawDataPoints(true);
        graph.getGridLabelRenderer().setHumanRounding(false);

    }
        *//*Intent intent = getIntent();
        num1 = intent.getIntExtra("NUM1", 0);
        num2 = intent.getIntExtra("NUM2", 0);
        num3 = intent.getIntExtra("NUM3", 0);
        num4 = intent.getIntExtra("NUM4", 0);
        num5 = intent.getIntExtra("NUM5", 0);


        webView = (WebView)findViewById(R.id.web);
        webView.addJavascriptInterface(new WebAppInterface(), "Android");

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/line_chart.html");
    }

    public class WebAppInterface {

        @JavascriptInterface
        public int getNum1() {
            return num1;
        }

        @JavascriptInterface
        public int getNum2() {
            return num2;
        }

        @JavascriptInterface
        public int getNum3() {
            return num3;
        }

        @JavascriptInterface
        public int getNum4() {
            return num4;
        }

        @JavascriptInterface
        public int getNum5() {
            return num5;
        }
    }*/

}