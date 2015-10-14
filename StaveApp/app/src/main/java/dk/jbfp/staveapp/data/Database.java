package dk.jbfp.staveapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import dk.jbfp.staveapp.User;
import dk.jbfp.staveapp.UserRepository;

public class Database extends SQLiteOpenHelper implements UserRepository {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "staveapp.db";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Contract.User.CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<User> getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
            Contract.User._ID,
            Contract.User.COLUMN_NAME,
            Contract.User.COLUMN_SEED,
            Contract.User.COLUMN_PHOTO
        };

        Cursor c = db.query(Contract.User.TABLE_NAME, projection, null, null, null, null, null);
        ArrayList<User> users = new ArrayList<>();

        while (c.moveToNext()) {
            User user = new User();
            user.id = c.getInt(0);
            user.name = c.getString(1);
            user.seed = c.getInt(2);
            user.photo = c.getBlob(3);
            users.add(user);
        }

        return users;
    }

    @Override
    public void addUser(String name, int seed, byte[] photo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Contract.User.COLUMN_NAME, name);
        values.put(Contract.User.COLUMN_SEED, seed);
        values.put(Contract.User.COLUMN_PHOTO, photo);

        db.insert(Contract.User.TABLE_NAME, null, values);

    }
}
