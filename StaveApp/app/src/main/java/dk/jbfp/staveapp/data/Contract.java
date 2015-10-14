package dk.jbfp.staveapp.data;

import android.provider.BaseColumns;

public final class Contract {
    public static abstract class User implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SEED = "seed";
        public static final String COLUMN_PHOTO = "photo";
        public static final String CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                    User._ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME + " TEXT NOT NULL," +
                    COLUMN_SEED + " INTEGER NOT NULL," +
                    COLUMN_PHOTO + " BLOB NULL" +
                ")";
    }
}
