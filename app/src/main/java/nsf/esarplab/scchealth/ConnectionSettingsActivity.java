package nsf.esarplab.scchealth;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ConnectionSettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_settings);


        // Find the View that shows the profile category
        TextView bluetooth = (TextView) findViewById(R.id.bluetooth);

        // Set a click listener on that View
        bluetooth.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the profile category is clicked on.
            @Override
            public void onClick(View view) {
                // Create a new intent to open the {@link ProfileActivity}
                Intent bluetoothIntent = new Intent(ConnectionSettingsActivity.this, BluetoothActivity.class);

                // Start the new activity
                startActivity(bluetoothIntent);

            }
        });

     /*   // Find the View that shows the profile category
        TextView wifi = (TextView) findViewById(R.id.wifi);
        // Set a click listener on that View
        wifi.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the profile category is clicked on.
            @Override
            public void onClick(View view) {
                // Create a new intent to open the {@link ProfileActivity}
                Intent wifiIntent = new Intent(ConnectionSettingsActivity.this, WifiActivity.class);

                // Start the new activity
                startActivity(wifiIntent);

            }
        });*/
    }
}