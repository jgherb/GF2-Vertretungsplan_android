package ml.noscio.gf2;

import android.provider.BaseColumns;

/**
 * Created by jgher on 23.04.2016.
 */
public final class FeedReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public FeedReaderContract() {}

    /* Inner class that defines the table contents */
    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "data";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "key";
        public static final String COLUMN_NAME_SUBTITLE = "value";
        public static final String COLUMN_NAME_NULLABLE = "null";
    }
}