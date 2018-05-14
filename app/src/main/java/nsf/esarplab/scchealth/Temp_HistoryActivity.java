package nsf.esarplab.scchealth;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class Temp_HistoryActivity extends AppCompatActivity {
    /** Database helper that will provide us access to the database */
    private TempDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_history);

        // show action bar
        ActionBar myActionBar = getSupportActionBar();
        myActionBar.show();


        mDbHelper = new TempDbHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the temperature database.
     */
    private void displayDatabaseInfo() {
        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                TempContract.TempEntry._ID,
                TempContract.TempEntry.COLUMN_PATIENT_NAME,
                TempContract.TempEntry.COLUMN_DATE_TIME,
                TempContract.TempEntry.COLUMN_TEMP_VALUE,
                TempContract.TempEntry.COLUMN_EOI_RATING};

        // Perform a query on the temperatures table
        Cursor cursor = db.query(
                TempContract.TempEntry.TABLE_NAME,   // The table to query
                projection,            // The columns to return
                null,                  // The columns for the WHERE clause
                null,                  // The values for the WHERE clause
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // The sort order

        TextView displayView = (TextView) findViewById(R.id.text_view_temp);

        try {
            // Create a header in the Text View that looks like this:
            //
            // The pets table contains <number of rows in Cursor> pets.
            // _id - name - datetime - temperature - EOI
            //
            // In the while loop below, iterate through the rows of the cursor and display
            // the information from each column in this order.
            displayView.setText("The database contains " + cursor.getCount() + " records.\n\n");
            displayView.append(TempContract.TempEntry._ID + " - " +
                    TempContract.TempEntry.COLUMN_PATIENT_NAME + " - " +
                    TempContract.TempEntry.COLUMN_DATE_TIME + " - " +
                    TempContract.TempEntry.COLUMN_TEMP_VALUE + " - " +
                    TempContract.TempEntry.COLUMN_EOI_RATING + "\n");

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(TempContract.TempEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(TempContract.TempEntry.COLUMN_PATIENT_NAME);
            int datetimeColumnIndex = cursor.getColumnIndex(TempContract.TempEntry.COLUMN_DATE_TIME);
            int tempColumnIndex = cursor.getColumnIndex(TempContract.TempEntry.COLUMN_TEMP_VALUE);
            int eoiColumnIndex = cursor.getColumnIndex(TempContract.TempEntry.COLUMN_EOI_RATING);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentDatetime = cursor.getString(datetimeColumnIndex);
                Double currentTemperature = cursor.getDouble(tempColumnIndex);
                String currentValue = cursor.getString(eoiColumnIndex);
                // Display the values from each column of the current row in the cursor in the TextView
                displayView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentDatetime + " - " +
                        currentTemperature + " - " +
                        currentValue));
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }

    /**
     * Helper method to insert hardcoded data into the database. For debugging purposes only.
     */
    private void insertTemp() {
        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and  attributes are the values.
        ContentValues values = new ContentValues();
        values.put(TempContract.TempEntry.COLUMN_PATIENT_NAME, "Anonymous");
        values.put(TempContract.TempEntry.COLUMN_DATE_TIME, " null");
        values.put(TempContract.TempEntry.COLUMN_TEMP_VALUE, "98");
        values.put(TempContract.TempEntry.COLUMN_EOI_RATING, " ");

        // Insert a new row for Toto in the database, returning the ID of that new row.
        // The first argument for db.insert() is the pets table name.
        // The second argument provides the name of a column in which the framework
        // can insert NULL in the event that the ContentValues is empty (if
        // this is set to "null", then the framework will not insert a row when
        // there are no values).
        // The third argument is the ContentValues object containing the info for Toto.
        long newRowId = db.insert(TempContract.TempEntry.TABLE_NAME, null, values);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option

            case R.id.action_insert_dummy_data:
                //insertTemp();
                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
            case R.id.action_trends:
                Intent lineGraph = new Intent(Temp_HistoryActivity.this, ShowWebChart.class);
                startActivity(lineGraph);

        }
        return super.onOptionsItemSelected(item);
    }
}