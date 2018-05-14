package nsf.esarplab.scchealth;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Locale;


public class DisplayContact extends AppCompatActivity {

    int from_Where_I_Am_Coming = 0;
    private ProfileDbHelper mydb ;

    TextView name ;

    TextView email;
    TextView street;
    TextView place;
    TextView phone;
    int id_To_Update = 0;
    static final int READ_BLOCK_SIZE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_contact);
        // show action bar
        ActionBar myActionBar = getSupportActionBar();
        myActionBar.show();

        name = (TextView) findViewById(R.id.editTextName);

        street = (TextView) findViewById(R.id.editTextStreet);
        email = (TextView) findViewById(R.id.editTextEmail);
        place = (TextView) findViewById(R.id.editTextCity);
        phone = (TextView) findViewById(R.id.editTextPhone);

// display contact

        mydb = new ProfileDbHelper(this);

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            int Value = extras.getInt("id");

            if(Value>0){
                //means this is the view part not the add contact part.
                Cursor rs = mydb.getData(Value);
                id_To_Update = Value;
                rs.moveToFirst();

                String nam = rs.getString(rs.getColumnIndex(ProfileDbHelper.CONTACTS_COLUMN_NAME));

                String emai = rs.getString(rs.getColumnIndex(ProfileDbHelper.CONTACTS_COLUMN_EMAIL));
                String stree = rs.getString(rs.getColumnIndex(ProfileDbHelper.CONTACTS_COLUMN_STREET));
                String plac = rs.getString(rs.getColumnIndex(ProfileDbHelper.CONTACTS_COLUMN_CITY));
                String phon = rs.getString(rs.getColumnIndex(ProfileDbHelper.CONTACTS_COLUMN_PHONE));

                if (!rs.isClosed())  {
                    rs.close();
                }
                Button b = (Button)findViewById(R.id.button1);
                b.setVisibility(View.INVISIBLE);
                /*activate.setVisibility(View.VISIBLE);*/


                name.setText((CharSequence)nam);
                name.setFocusable(false);
                name.setClickable(false);



                email.setText((CharSequence)emai);
                email.setFocusable(false);
                email.setClickable(false);

                street.setText((CharSequence)stree);
                street.setFocusable(false);
                street.setClickable(false);

                place.setText((CharSequence)plac);
                place.setFocusable(false);
                place.setClickable(false);

                phone.setText((CharSequence)phon);
                phone.setFocusable(false);
                phone.setClickable(false);
            }
        }

    }

    // activate contact

    // write text to file
    public void WriteBtn(View v) {
        // add-write text into file
        try {
            FileOutputStream fileout=openFileOutput("mytextfile.txt", MODE_PRIVATE);
            OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
            outputWriter.write(name.getText().toString());
            outputWriter.close();

            //display file saved message
            Toast.makeText(getBaseContext(), "Profile Activated !",
                    Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
// go back to diagnostic
        Intent sleepapneaIntent = new Intent(DisplayContact.this, LabActivity.class);

        // Start the new activity
        startActivity(sleepapneaIntent);
    }
// navigate to Home



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Bundle extras = getIntent().getExtras();

        if(extras !=null) {
            int Value = extras.getInt("id");
            if(Value>0){
                getMenuInflater().inflate(R.menu.display_contact, menu);
            } else{
                getMenuInflater().inflate(R.menu.main_menu, menu);
            }
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()) {
            case R.id.Edit_Contact:
                Button b = (Button)findViewById(R.id.button1);
                b.setVisibility(View.VISIBLE);
                name.setEnabled(true);
                name.setFocusableInTouchMode(true);
                name.setClickable(true);

                email.setEnabled(true);
                email.setFocusableInTouchMode(true);
                email.setClickable(true);

                street.setEnabled(true);
                street.setFocusableInTouchMode(true);
                street.setClickable(true);

                place.setEnabled(true);
                place.setFocusableInTouchMode(true);
                place.setClickable(true);

                phone.setEnabled(true);
                phone.setFocusableInTouchMode(true);
                phone.setClickable(true);

                return true;
            case R.id.Delete_Contact:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.deleteContact)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mydb.deleteContact(id_To_Update);
                                Toast.makeText(getApplicationContext(), "Deleted Successfully",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });

                AlertDialog d = builder.create();
                d.setTitle("Are you sure");
                d.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }



    public void run(View view) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        boolean error=false;
        String stAddress=street.getText().toString();
        try {
            addresses = geocoder.getFromLocationName(stAddress, 1);
        } catch (IOException e) {
            error=true;
        }

        // check information validity
        if((name.getText().toString().trim().length() == 0)||(phone.getText().toString().trim().length() == 0)||(street.getText().toString().trim().length() == 0))
        {
            Toast.makeText(getApplicationContext(), "* Fields Can't be empty", Toast.LENGTH_SHORT).show();
        }

        else if(error){
            Toast.makeText(getApplicationContext(), "Street address not correct", Toast.LENGTH_SHORT).show();
        }

        else {

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                int Value = extras.getInt("id");
                if (Value > 0) {
                    if (mydb.updateContact(id_To_Update, name.getText().toString(),
                            phone.getText().toString(), email.getText().toString(),
                            street.getText().toString(), place.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "not Updated", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (mydb.insertContact(name.getText().toString(), phone.getText().toString(),
                            email.getText().toString(), street.getText().toString(),
                            place.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "done",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "not done",
                                Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivity(intent);
                }
            }
        }
    }
}