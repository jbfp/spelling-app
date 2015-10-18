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
        public static final String COLUMN_LENGTH = "length";
        public static final String COLUMN_OFFSET = "offset";
        public static final String CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        Step._ID + " INTEGER PRIMARY KEY NOT NULL," +
                        COLUMN_USER_ID + " INTEGER NOT NULL," +
                        COLUMN_STATE + " INTEGER NOT NULL," +
                        COLUMN_LENGTH + " INTEGER NOT NULL," +
                        COLUMN_OFFSET + " INTEGER NOT NULL," +
                        "FOREIGN KEY (" + COLUMN_USER_ID + ") " +
                        "REFERENCES " + User.TABLE_NAME +
                ")";
    }

    public static abstract class Stat implements BaseColumns {
        public static final String TABLE_NAME = "stat";
        public static final String COLUMN_WORD = "word";
        public static final String COLUMN_CORRECT = "correct";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_LISTENS = "listens";
        public static final String CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        Stat._ID + " INTEGER PRIMARY KEY NOT NULL," +
                        COLUMN_WORD + " TEXT NOT NULL," +
                        COLUMN_CORRECT + " INTEGER NOT NULL," +
                        COLUMN_TIME + " REAL NOT NULL," +
                        COLUMN_LISTENS + " INTEGER NOT NULL" +
                ")";
    }
}
