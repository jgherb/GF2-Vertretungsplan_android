package ml.noscio.gf2;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import ml.noscio.gf2.FeedReaderContract.FeedEntry;

/**
 * Created by jgher on 23.04.2016.
 */
public class DB_utility {
    public DB_utility(String s) {
        mDbHelper = new FeedReaderDbHelper(MainActivity.getContext());
        db = mDbHelper.getWritableDatabase();
    }
    FeedReaderDbHelper mDbHelper;
    SQLiteDatabase db;
    public String getValue(long id) {
        mDbHelper = new FeedReaderDbHelper(MainActivity.getContext());
        db = mDbHelper.getWritableDatabase();
        //TODO: Besserer Datenbankzugriff
        String value = "";
        Cursor c = db.rawQuery("SELECT value FROM data WHERE "+FeedEntry.COLUMN_NAME_ENTRY_ID+"='"+id+"'", null);
        Log.i("DEBUG", "DB_reached Main");

        try {
            c.moveToFirst();
            value = c.getString(0);
            Log.i("DEBUG", "DB_survive try");
        } catch (Exception e) {
            Log.i("exception_DB",e.toString());
            Log.i("DEBUG", "DB_catch");
            prepareDB();
            return "@@ERROR@@";
        }
        Log.i("DEBUG", "DB_close");
        c.close();
        return value;
    }
    public boolean save(long id, String value) {
        mDbHelper = new FeedReaderDbHelper(MainActivity.getContext());
        db = mDbHelper.getWritableDatabase();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

// New value for one column
        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_SUBTITLE, value);

// Which row to update, based on the ID
        String selection = FeedEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(id) };

        int count = db.update(
                FeedEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        return true;
    }
    public void prepareDB() {
        rebuildDB();
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_ENTRY_ID, 0);
        values.put("key", "reg");
        values.put("value", "false");

// Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                "data",
                FeedEntry.COLUMN_NAME_NULLABLE,
                values);

        values = new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_ENTRY_ID, 1);
        values.put("key", "klasse");
        values.put("value", "5a");
        newRowId = db.insert(
                "data",
                FeedEntry.COLUMN_NAME_NULLABLE,
                values);

        values = new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_ENTRY_ID, 2);
        values.put("key", "subjects");
        values.put("value", "");
        newRowId = db.insert(
                "data",
                FeedEntry.COLUMN_NAME_NULLABLE,
                values);

        values = new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_ENTRY_ID, 3);
        values.put("key", "push");
        values.put("value", "");
        newRowId = db.insert(
                "data",
                FeedEntry.COLUMN_NAME_NULLABLE,
                values);
    }
    public void rebuildDB() {
        db.execSQL(FeedReaderDbHelper.SQL_DELETE_ENTRIES);
        db.execSQL(FeedReaderDbHelper.SQL_CREATE_ENTRIES);
    }
}
