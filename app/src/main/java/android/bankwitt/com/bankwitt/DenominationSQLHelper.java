package android.bankwitt.com.bankwitt;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by chris on 10/31/2016.
 */

public class DenominationSQLHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "DenominationDB";

    public DenominationSQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table
        String createDenominationTable = "CREATE TABLE " +
                DenominationContract.DenominationEntry.TABLE_NAME + " ( id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DenominationContract.DenominationEntry.COUNT_TITLE + " INTEGER " +
                DenominationContract.DenominationEntry.VALUE_TITLE + " REAL " +
                DenominationContract.DenominationEntry.DISPLAY_AMOUNT_TITLE + " TEXT ) ";
        // create denominations table
        db.execSQL(createDenominationTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older denominations table if existed
        db.execSQL("DROP TABLE IF EXISTS denominations");

        // create fresh books table
        this.onCreate(db);
    }

    public void addDenomination(Denomination inDenomination) {
        Log.d("addBook", inDenomination.toString());

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(DenominationContract.DenominationEntry.COUNT_TITLE, inDenomination.getCount());
        values.put(DenominationContract.DenominationEntry.DISPLAY_AMOUNT_TITLE,
                inDenomination.getDisplayAmount());
        values.put(DenominationContract.DenominationEntry.VALUE_TITLE, inDenomination.getValue());
        //TODO figure out integer display of monies.

        // 3. insert
        db.insert(DenominationContract.DenominationEntry.TABLE_NAME, // table
            null, //nullColumnHack
        values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public void deleteDenomination(Denomination inDenomination) {

    }

    public void updateDenomination(Denomination inDenomination) {

    }

    public ArrayList<Denomination> getAllDenominations() {
        return null;
    }

    public Denomination getDenominationById(String id) {
        return null;
    }

    public void saveAllDenominations(ArrayList<Denomination> allCurrentDatasets) {

    }
}
