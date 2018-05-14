package nsf.esarplab.scchealth;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class LabActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // show action bar
        ActionBar myActionBar = getSupportActionBar();
        myActionBar.show();

        // Set the content of the activity to use the activity_main.xml layout file
        setContentView(R.layout.activity_lab);

        // Find the View that shows the sleep apnea category
        TextView sleepapnea = (TextView) findViewById(R.id.sleepapnea);

        // Set a click listener on that View
        sleepapnea.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers category is clicked on.
            @Override
            public void onClick(View view) {
                // Create a new intent to open the {@link SleepApnea}
                Intent sleepapneaIntent = new Intent(LabActivity.this, SleepApnea.class);

                // Start the new activity
                startActivity(sleepapneaIntent);
            }
        });

        // Find the View that shows the body temperature category
        TextView bodytemp = (TextView) findViewById(R.id.bodytemp);

        // Set a click listener on that View
        bodytemp.setOnClickListener(new View.OnClickListener() {

            Intent intent = null;
            // The code in this method will be executed when the body temperature category is clicked on.
            @Override
            public void onClick(View view) {


                intent = new Intent(getApplicationContext(), FluActivity.class);
                startActivity(intent);

            }
        });

        // Find the View that shows the Asthma category
        TextView asthma = (TextView) findViewById(R.id.asthma);

        // Set a click listener on that View
        asthma.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the asthma category is clicked on.
            @Override
            public void onClick(View view) {
                // Create a new intent to open the {@link AsthmaActivity}
                Intent colorsIntent = new Intent(LabActivity.this, AsthmaActivity.class);

                // Start the new activity
                startActivity(colorsIntent);
            }
        });

        // Find the View that shows the arrhythmia category
        TextView arrhythmia = (TextView) findViewById(R.id.arrhythmia);

        // Set a click listener on that View
        arrhythmia.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the arrhythmia category is clicked on.
            @Override
            public void onClick(View view) {
                // Create a new intent to open the {@link ArrhythmiaActivity}
                Intent arrhythmiaIntent = new Intent(LabActivity.this, ArrhythmiaActivity.class);

                // Start the new activity
                startActivity(arrhythmiaIntent);
            }
        });

        // Find the View that shows the arrhythmia category
        TextView oneStop = (TextView) findViewById(R.id.oneStop);

        // Set a click listener on that View
        oneStop.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the arrhythmia category is clicked on.
            @Override
            public void onClick(View view) {
                // Create a new intent to open the {@link ArrhythmiaActivity}
                Intent oneStopIntent = new Intent(LabActivity.this, oneStopService.class);

                // Start the new activity
                startActivity(oneStopIntent);
            }
        });
    }
}