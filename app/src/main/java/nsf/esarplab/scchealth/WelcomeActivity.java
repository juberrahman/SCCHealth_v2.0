package nsf.esarplab.scchealth;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;

//git@gitlab.com:mjrahman/smartandconnected.git

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        /*ActionBar myActionBar = getSupportActionBar();
        myActionBar.show();*/

        /*Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);*/
        // receive user name and display in a text view
       /* Intent userIntent = getIntent();
        String userName=userIntent.getStringExtra("user");*/
        TextView displayUsername = (TextView) findViewById(R.id.uName);



        //Get Login details
        SharedPreferences prefs = getSharedPreferences("logindetails",MODE_PRIVATE);
        String Uname =  prefs.getString("loginname","Default");
        displayUsername.setText("\t\tHello "+Uname);

        // set notification
        Calendar calender =Calendar.getInstance();
        calender.set(Calendar.HOUR_OF_DAY,7);
        calender.set(Calendar.MINUTE,59);
        calender.set(Calendar.SECOND,1);
        Intent intent=new Intent(getApplicationContext(), Notification_receiver.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager=(AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calender.getTimeInMillis(),1000*60*60*12,pendingIntent);


        // Find the View that shows the explorer
        TextView explorer = (TextView) findViewById(R.id.explore);

        // Set a click listener on that View
        explorer.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers category is clicked on.
            @Override
            public void onClick(View view) {

                // Create a new intent to open the {@link SleepApnea}
                Intent homeIntent = new Intent(WelcomeActivity.this, HomeActivity.class);

                // Start the new activity
                startActivity(homeIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}