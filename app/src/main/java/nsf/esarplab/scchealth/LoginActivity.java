package nsf.esarplab.scchealth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends FragmentActivity {
    Button btnSignIn, btnFragment;
    LoginDataBaseAdapter loginDataBaseAdapter;
    EditText ed1, ed2;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button b2=(Button)findViewById(R.id.button2);
        ed1=(EditText) findViewById(R.id.editText);

        // create a instance of SQLite Database
        loginDataBaseAdapter=new LoginDataBaseAdapter(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();

        // Get The Refference Of Buttons
        btnSignIn=(Button)findViewById(R.id.buttonSignIN);
        btnFragment=(Button)findViewById(R.id.help);

        //Set OnClick Listener on SignUp button
        btnFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                HelpdeskFragment f1 = new HelpdeskFragment();
                fragmentTransaction.add(R.id.frag1, f1);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                // hide virtual keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(ed1.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
            }


        });
    }
    // Methos to handleClick Event of Sign In Button
    public void signIn(View V)
    {


        // get the References of views
        final  EditText editTextUserName=(EditText)findViewById(R.id.editText);
        final  EditText editTextPassword=(EditText)findViewById(R.id.editText2);



        // get The User name and Password
        String userName=editTextUserName.getText().toString();
        String password=editTextPassword.getText().toString();

        // fetch the Password form database for respective user name
        String storedPassword=loginDataBaseAdapter.getSinlgeEntry(userName);

        // check if the Stored password matches with  Password entered by user

        if ((userName.equals("admin") && password.equals("hce"))||(password.equals(storedPassword))) {

            // pass login details to shared preferences
            SharedPreferences prefs = getSharedPreferences("logindetails",MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("loginname",ed1.getText().toString()).commit();

            Intent welcomeIntent=new Intent(LoginActivity.this,WelcomeActivity.class);
            startActivity(welcomeIntent);

            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(LoginActivity.this, "User Name or Password does not match", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close The Database
        loginDataBaseAdapter.close();
    }


    public void onClick(View v) {
        finish();
    }



}
