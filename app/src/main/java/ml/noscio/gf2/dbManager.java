package ml.noscio.gf2;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by jgher on 23.04.2016.
 */
public class dbManager {
    public static String read(String key) {
        Log.i("dbManager","read: "+key);
        String value = "";
        try {
            FeedReaderDbHelper helper = new FeedReaderDbHelper(MainActivity._context);
            SQLiteDatabase db = helper.getWritableDatabase();
            Log.i("dbManager","marke1");
            Cursor c = db.rawQuery("SELECT value FROM data WHERE key='" + key + "'", null);
            Log.i("dbManager","marke2");
            int count = c.getCount();
            Log.i("dbManager","count: "+count);
            Log.i("dbManager","marke3");
            Log.i("count", count + "");
            c.moveToFirst();
            value = c.getString(0);
            c.close();
        }
        catch(Exception e) {
            Log.e("dbManager",e.toString());
            return "";
        }
        return value;
    }
    public static void write(String key, String value) {
        Log.i("dbManager","write: "+key);
        if(key.equals("klasse")) {
            Log.e("dbManager","KLASSE SAVED");
        }
        // Gets the data repository in write mode
        FeedReaderDbHelper helper = new FeedReaderDbHelper(MainActivity._context);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT value FROM data WHERE key='"+key+"'",null);
        c.moveToLast();
        int count = c.getCount();
        Log.i("dbManager","count: "+count);
        while(count!=0) {
            db.delete("data","key='"+key+"'",null);
            c = db.rawQuery("SELECT value FROM data WHERE key='"+key+"'",null);
            count = c.getCount();
            Log.i("dbManager","count: "+count);
            //db.rawQuery("DELETE FROM data WHERE key='"+key+"'",null);
        }
// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FeedReaderDbHelper.FeedEntry.COLUMN_NAME_KEY, key);
        values.put(FeedReaderDbHelper.FeedEntry.COLUMN_NAME_VALUE, value);

// Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                FeedReaderDbHelper.FeedEntry.TABLE_NAME,
                FeedReaderDbHelper.FeedEntry.COLUMN_NAME_NULLABLE,
                values);
        Log.i("dbManager","written to: "+newRowId);
        c = db.rawQuery("SELECT value FROM data WHERE key='"+key+"'",null);
        count = c.getCount();
        c.close();
        Log.i("dbManager","new count: "+count);
    }
    public static void deleteTable() {
        Log.i("dbManager","delete");
        FeedReaderDbHelper helper = new FeedReaderDbHelper(MainActivity._context);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("delete from "+ FeedReaderContract.FeedEntry.TABLE_NAME);
    }
}
