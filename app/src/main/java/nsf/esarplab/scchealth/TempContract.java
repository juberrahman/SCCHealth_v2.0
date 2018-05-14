package nsf.esarplab.scchealth;

import android.provider.BaseColumns;

/**
 * Created by mrahman8 on 1/18/2017.
 */

public class TempContract {

// To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.

    private TempContract() {}

    /**
     * Inner class that defines constant values for the pets database table.
     * Each entry in the table represents a single pet.
     */
    public static final class TempEntry implements BaseColumns {

        /** Name of database table for pets */
        public final static String TABLE_NAME = "temphistory";

        /**
         * Unique ID number for the patient (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the patient.
         *
         * Type: TEXT
         */
        public final static String COLUMN_PATIENT_NAME ="Name";

        /**
         * dATE AND tIME of the test.
         *
         * Type: TEXT
         */
        public final static String COLUMN_DATE_TIME = "Date_and_time";




        /**
         * Value of the temperature.
         *
         * Type: Float
         */
        public final static String COLUMN_TEMP_VALUE = "Temperature";

        /**
         * Possible values for the EOI of temperature.
         * Type TEXT
         */
        public final static String COLUMN_EOI_RATING = "EOI";

        /*public static final int EOI_MINIMUM = 0;
        public static final int EOI_MAXIMUM = 1;
        public static final int EOI_STEP = 11;*/
    }

}
