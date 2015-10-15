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
                    User._ID + " INTEGER PRIMARY KEY NOT NULL," +
                    COLUMN_NAME + " TEXT NOT NULL," +
                    COLUMN_SEED + " INTEGER NOT NULL," +
                    COLUMN_PHOTO + " BLOB NULL" +
                ")";
    }

    public static abstract class Step implements BaseColumns {
        public static final String TABLE_NAME = "step";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_STATE = "state";
        public static final String CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        Step._ID + " INTEGER PRIMARY KEY NOT NULL," +
                        COLUMN_USER_ID + " INTEGER NOT NULL," +
                        COLUMN_STATE + " INTEGER NOT NULL," +
                        "FOREIGN KEY (" + COLUMN_USER_ID + ") " +
                        "REFERENCES " + User.TABLE_NAME +
                ")";
    }
}
