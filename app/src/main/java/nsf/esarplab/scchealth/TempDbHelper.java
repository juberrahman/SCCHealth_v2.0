package nsf.esarplab.scchealth;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mrahman8 on 1/18/2017.
 */

public class TempDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = TempDbHelper.class.getSimpleName();

    /** Name of the database file */
    private static final String DATABASE_NAME = "history.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link TempDbHelper}.
     *
     * @param context of the app
     */
    public TempDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the  table
        String SQL_CREATE_TEMP_TABLE =  "CREATE TABLE " + TempContract.TempEntry.TABLE_NAME + " ("
                + TempContract.TempEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TempContract.TempEntry.COLUMN_PATIENT_NAME + " TEXT NOT NULL, "
                + TempContract.TempEntry.COLUMN_DATE_TIME + " TEXT, "
                + TempContract.TempEntry.COLUMN_TEMP_VALUE + " TEXT NOT NULL, "
                + TempContract.TempEntry.COLUMN_EOI_RATING + " REAL );";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_TEMP_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}