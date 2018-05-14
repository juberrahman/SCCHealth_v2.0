package nsf.esarplab.scchealth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class GeneralSettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_settings);
// show action bar
        ActionBar myActionBar = getSupportActionBar();
        myActionBar.show();

        // Find the View that shows the profile category
        TextView bluetooth = (TextView) findViewById(R.id.bluetooth);

        // Set a click listener on that View
        bluetooth.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the profile category is clicked on.
            @Override
            public void onClick(View view) {
                // Create a new intent to open the {@link ProfileActivity}
                Intent bluetoothIntent = new Intent(GeneralSettingsActivity.this, BluetoothActivity.class);

                // Start the new activity
                startActivity(bluetoothIntent);

            }
        });

        //Find the View that shows the profile category
        TextView wifi = (TextView) findViewById(R.id.wifi);
        // Set a click listener on that View
        wifi.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the profile category is clicked on.
            @Override
            public void onClick(View view) {
                // Create a new intent to open the {@link WifiActivity}
                Intent wifiIntent = new Intent(GeneralSettingsActivity.this, WifiActivity.class);

                // Start the new activity
                startActivity(wifiIntent);

            }
        });


        // Find the View that shows the profile category
        TextView profile = (TextView) findViewById(R.id.profile);

        // Set a click listener on that View
        profile.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the profile category is clicked on.
            @Override
            public void onClick(View view) {
                // Create a new intent to open the {@link ProfileActivity}
                Intent profileIntent = new Intent(GeneralSettingsActivity.this, ProfileActivity.class);

                // Start the new activity
                startActivity(profileIntent);

            }
        });

        // Find the View that shows the profile category
        TextView newuser = (TextView) findViewById(R.id.useraccount);

        // Set a click listener on that View
        newuser.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the profile category is clicked on.
            @Override
            public void onClick(View view) {
                // Create a new intent to open the {@link SignupActivity}
                Intent newuserIntent = new Intent(GeneralSettingsActivity.this, SignupActivity.class);

                // Start the new activity
                startActivity(newuserIntent);

            }
        });

        // Find the View that shows the permission category
        TextView permission = (TextView) findViewById(R.id.permission);

        // Set a click listener on that View
        permission.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the setup permissions view is clicked on.
            @Override
            public void onClick(View view) {
                // open App settings
                Intent permissionIntent = new Intent();
                permissionIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                permissionIntent.setData(uri);
                startActivity(permissionIntent);

            }
        });

    }
}