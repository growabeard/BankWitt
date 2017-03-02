package android.bankwitt.com.bankwitt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.bankwitt.com.bankwitt.DenominationContract.DenominationEntry.COUNT_TITLE;
import static android.bankwitt.com.bankwitt.DenominationContract.DenominationEntry.DISPLAY_AMOUNT_TITLE;
import static android.bankwitt.com.bankwitt.DenominationContract.DenominationEntry.ID;
import static android.bankwitt.com.bankwitt.DenominationContract.DenominationEntry.TABLE_NAME;
import static android.bankwitt.com.bankwitt.DenominationContract.DenominationEntry.VALUE_TITLE;

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
                TABLE_NAME + " ( id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COUNT_TITLE + " INTEGER NOT NULL, " +
                VALUE_TITLE + " INTEGER NOT NULL, " +
                DISPLAY_AMOUNT_TITLE + " TEXT NOT NULL ); ";
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
        Log.d("addDenomination", inDenomination.toString());

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(COUNT_TITLE, inDenomination.getCount());
        values.put(DISPLAY_AMOUNT_TITLE,
                inDenomination.getDisplayAmount());
        values.put(VALUE_TITLE, inDenomination.getValue());
        //TODO figure out integer display of monies.

        Log.d("addDenomination", values.toString());
        // 3. insert
        db.insert(TABLE_NAME, null, values);

        // 4. close
        db.close();
    }

    public void deleteDenomination(Denomination inDenomination) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(inDenomination.getId())};

        db.delete(TABLE_NAME, selection, selectionArgs);

        db.close();
    }

    public void updateDenomination(Denomination inDenomination) {

    }

    public ArrayList<Denomination> getAllDenominations() {
        Log.d("getAllDenominations", "Getting all denominations..");
        ArrayList<Denomination> resultingDenomList = new ArrayList<Denomination>();

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String[] columns = new String[]{ID, DISPLAY_AMOUNT_TITLE, COUNT_TITLE, VALUE_TITLE};
            Cursor resultFromQuery = db.query(TABLE_NAME, columns, null, null, null, null, VALUE_TITLE, null);

            if (resultFromQuery.getCount() > 0) {
                return populateDenominationFromQuery(resultFromQuery);
            }
        }catch(SQLiteException e) {

        }
        finally{
            db.close();
        }
        return resultingDenomList;
    }

    private ArrayList<Denomination> populateDenominationFromQuery(Cursor resultFromQuery) {
        ArrayList<Denomination> resultingDenomList = new ArrayList<Denomination>();
        resultFromQuery.moveToFirst();
        Denomination firstDenom = new Denomination();
        firstDenom.setId(resultFromQuery.getInt(resultFromQuery.getColumnIndex(ID)));
        firstDenom.setCount(resultFromQuery.getInt(resultFromQuery.getColumnIndex(COUNT_TITLE)));
        firstDenom.setValue(resultFromQuery.getInt(resultFromQuery.getColumnIndex(VALUE_TITLE)));
        firstDenom.setDisplayAmount(resultFromQuery.getString(resultFromQuery.getColumnIndex(DISPLAY_AMOUNT_TITLE)));
        firstDenom.setChanged(false);
        resultingDenomList.add(firstDenom);
        while(resultFromQuery.moveToNext()) {
            Denomination nextDenom = new Denomination();
            nextDenom.setId(resultFromQuery.getInt(resultFromQuery.getColumnIndex(ID)));
            nextDenom.setCount(resultFromQuery.getInt(resultFromQuery.getColumnIndex(COUNT_TITLE)));
            nextDenom.setValue(resultFromQuery.getInt(resultFromQuery.getColumnIndex(VALUE_TITLE)));
            nextDenom.setDisplayAmount(resultFromQuery.getString(resultFromQuery.getColumnIndex(DISPLAY_AMOUNT_TITLE)));
            nextDenom.setChanged(false);
            resultingDenomList.add(nextDenom);
        }
        return resultingDenomList;
    }

    public Denomination getDenominationById(String id) {
        return null;
    }

    public void saveAllDenominations(List<Denomination> changedDatasets) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = new String[]{DISPLAY_AMOUNT_TITLE, COUNT_TITLE, VALUE_TITLE};

        for(Denomination currDataset : changedDatasets) {
            ContentValues val = new ContentValues();
            val.put(COUNT_TITLE, currDataset.getCount());
            val.put(DISPLAY_AMOUNT_TITLE, currDataset.getDisplayAmount());
            val.put(VALUE_TITLE, currDataset.getValue());

            String selection = ID + " LIKE ?";
            String[] selectionArgs = {String.valueOf(currDataset.getId())};

            int resultFromQuery = db.update(TABLE_NAME, val, selection, selectionArgs);
        }
        db.close();
    }

    public void deleteDenominations(List<Denomination> allDatasetsPendingDeletion) {
        for (Denomination denomToDelete:allDatasetsPendingDeletion) {
            deleteDenomination(denomToDelete);
        }
    }
}
