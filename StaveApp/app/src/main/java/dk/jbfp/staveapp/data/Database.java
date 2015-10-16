package dk.jbfp.staveapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import dk.jbfp.staveapp.StepRepository;
import dk.jbfp.staveapp.User;
import dk.jbfp.staveapp.UserRepository;
import dk.jbfp.staveapp.steps.Step;

public class Database extends SQLiteOpenHelper implements UserRepository, StepRepository {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "staveapp.db";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Contract.User.CREATE);
        db.execSQL(Contract.Step.CREATE);
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

    @Override
    public List<Step> getStepsForUser(long userId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                Contract.Step._ID,
                Contract.Step.COLUMN_USER_ID,
                Contract.Step.COLUMN_STATE,
                Contract.Step.COLUMN_LENGTH,
                Contract.Step.COLUMN_OFFSET
        };

        String selection = Contract.Step.COLUMN_USER_ID + " = ?";
        String[] selectionArgs = { String.valueOf(userId) };

        Cursor c = db.query(Contract.Step.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
        ArrayList<Step> steps = new ArrayList<>();
        Step.StepState[] stepStates = Step.StepState.values();

        while (c.moveToNext()) {
            Step step = new Step();
            step.id = c.getInt(0);
            step.userId = c.getInt(1);
            step.state = stepStates[c.getInt(2)];
            step.length = c.getInt(3);
            step.offset = c.getInt(4);
            steps.add(step);
        }

        return steps;
    }

    @Override
    public Step addStep(Step step) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Contract.Step.COLUMN_USER_ID, step.userId);
        values.put(Contract.Step.COLUMN_STATE, step.state.ordinal());
        values.put(Contract.Step.COLUMN_LENGTH, step.length);
        values.put(Contract.Step.COLUMN_OFFSET, step.offset);

        long id = db.insert(Contract.Step.TABLE_NAME, null, values);
        step.id = id;
        return step;
    }

    @Override
    public Step updateStep(Step step) {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(Contract.Step.COLUMN_STATE, step.state.ordinal());

        String selection = Contract.Step._ID + " = ?";
        String[] selectionArgs = { String.valueOf(step.id) };

        db.update(Contract.Step.TABLE_NAME, values, selection, selectionArgs);

        return step;
    }
}
