package nsf.esarplab.scchealth;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static nsf.esarplab.scchealth.ProfileDbHelper.CONTACTS_COLUMN_CITY;
import static nsf.esarplab.scchealth.ProfileDbHelper.CONTACTS_COLUMN_EMAIL;
import static nsf.esarplab.scchealth.ProfileDbHelper.CONTACTS_COLUMN_ID;
import static nsf.esarplab.scchealth.ProfileDbHelper.CONTACTS_COLUMN_NAME;
import static nsf.esarplab.scchealth.ProfileDbHelper.CONTACTS_COLUMN_PHONE;
import static nsf.esarplab.scchealth.ProfileDbHelper.CONTACTS_COLUMN_STREET;
import static nsf.esarplab.scchealth.ProfileDbHelper.CONTACTS_TABLE_NAME;

public class SignupActivity extends Activity {
    EditText editTextUserName,editTextPassword,editTextConfirmPassword;
    Button btnCreateAccount;
    private ProfileDbHelper profileDB ;
    private Cursor c;
    private boolean valid;

    LoginDataBaseAdapter loginDataBaseAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // get Instance  of Database Adapter
        loginDataBaseAdapter=new LoginDataBaseAdapter(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();

        // Get References of Views
        editTextUserName = (EditText) findViewById(R.id.editTextUserName);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextConfirmPassword = (EditText) findViewById(R.id.editTextConfirmPassword);

        // cross check with profile db
        try {
            profileDB = new ProfileDbHelper(this);
            SQLiteDatabase ourDatabase = profileDB.getReadableDatabase();
            String[] columns = new String[]{CONTACTS_COLUMN_ID, CONTACTS_COLUMN_NAME, CONTACTS_COLUMN_EMAIL, CONTACTS_COLUMN_STREET, CONTACTS_COLUMN_CITY, CONTACTS_COLUMN_PHONE};
            c = ourDatabase.query(CONTACTS_TABLE_NAME, columns, null, null, null, null, null);
        } catch (Exception e) {

        }

        btnCreateAccount = (Button) findViewById(R.id.buttonCreateAccount);
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {


                // TODO Auto-generated method stub

                String userName=editTextUserName.getText().toString().trim();
                String password=editTextPassword.getText().toString().trim();
                String confirmPassword=editTextConfirmPassword.getText().toString();
                c.moveToFirst();
                do {
                    if ((c.getString(1)).equals(userName)) {
                        valid=true;
                    }

                } while (c.moveToNext());


                // check if any of the fields are vaccant

                if (!valid) {
                    Toast.makeText(getApplicationContext(), "No such Profile", Toast.LENGTH_SHORT).show();
                } else if (userName.equals("") || password.equals("") || confirmPassword.equals("")) {
                    Toast.makeText(getApplicationContext(), "Field Vacant", Toast.LENGTH_LONG).show();
                    return;
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(getApplicationContext(), "Password does not match", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    // Save the Data in Database
                    loginDataBaseAdapter.deleteEntry(userName);
                    loginDataBaseAdapter.insertEntry(userName, password);
                    Toast.makeText(getApplicationContext(), "Account Successfully Created ", Toast.LENGTH_SHORT).show();
                    Intent backIntent = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(backIntent);
                }
            }
        });
    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        loginDataBaseAdapter.close();
    }
}
