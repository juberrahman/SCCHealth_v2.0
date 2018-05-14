package nsf.esarplab.scchealth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class BluetoothActivity extends AppCompatActivity implements OnClickListener {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth);

        // show action bar
        ActionBar myActionBar = getSupportActionBar();
        myActionBar.show();

        /*Button btnSimple = (Button) findViewById(R.id.btnSimple);
        btnSimple.setOnClickListener(this);

        Button btnListener = (Button) findViewById(R.id.btnListener);
        btnListener.setOnClickListener(this);

        Button btnAutoConnect = (Button) findViewById(R.id.btnAutoConnect);
        btnAutoConnect.setOnClickListener(this);*/

        Button btnDeviceList = (Button) findViewById(R.id.btnDeviceList);
        btnDeviceList.setOnClickListener(this);

        Button btnTerminal = (Button) findViewById(R.id.btnTerminal);
        btnTerminal.setOnClickListener(this);

        Button btnPair = (Button) findViewById(R.id.btnPair);
        btnPair.setOnClickListener(this);

    }

    public void onClick(View v) {
        int id = v.getId();
        Intent intent = null;
        switch (id) {
            /*case R.id.btnSimple:
                intent = new Intent(getApplicationContext(), SimpleActivity.class);
                startActivity(intent);
                break;
            case R.id.btnListener:
                intent = new Intent(getApplicationContext(), ListenerActivity.class);
                startActivity(intent);
                break;
            case R.id.btnAutoConnect:
                intent = new Intent(getApplicationContext(), AutoConnectActivity.class);
                startActivity(intent);
                break;*/
            case R.id.btnDeviceList:
                intent = new Intent(getApplicationContext(), DeviceListActivity.class);
                startActivity(intent);
                break;
            case R.id.btnTerminal:
                intent = new Intent(getApplicationContext(), TerminalActivity.class);
                startActivity(intent);
                break;
            case R.id.btnPair:
                intent = new Intent(getApplicationContext(), BluetoothPair.class);
                startActivity(intent);
                break;
        }
    }
}
