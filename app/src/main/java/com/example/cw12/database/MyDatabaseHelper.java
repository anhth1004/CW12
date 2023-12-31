package com.example.cw12.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private final Context context;
    private static final String DATABASE_NAME = "App_Hiking.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "hike";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_LOCATION = "location";
    private static final String COLUMN_DATE_OF_HIKE = "date_of_hike";
    private static final String COLUMN_HIKE_LENGTH = "hike_length";
    private static final String COLUMN_PARKING_AVAILABLE = "parking_available";
    private static final String COLUMN_DIFFICULTY_LEVEL = "difficulty_level";

    private static final String COLUMN_DESCRIPTION = "description";

    private static final String OBSERVATION_TABLE_NAME = "observation";
    private static final String COLUMN_OBSERVATION_ID = "observation_id";
    private static final String COLUMN_HIKE_ID = "hike_id";
    private static final String COLUMN_OBSERVATION_TYPE = "observation_type";
    private static final String COLUMN_OBSERVATION_TIME = "date_of_observation";
    private static final String COLUMN_ADDITIONAL_COMMENT = "additional_comment";

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_LOCATION + " TEXT, " +
                COLUMN_DATE_OF_HIKE + " TEXT, " +
                COLUMN_HIKE_LENGTH + " REAL, " + // Cập nhật tên cột và kiểu dữ liệu
                COLUMN_PARKING_AVAILABLE + " TEXT, " +
                COLUMN_DIFFICULTY_LEVEL + " TEXT," +
                COLUMN_DESCRIPTION + " TEXT);";

        String query1 = "CREATE TABLE " + OBSERVATION_TABLE_NAME +
                " (" + COLUMN_OBSERVATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_HIKE_ID + " TEXT REFERENCES hike(_id), " +
                COLUMN_OBSERVATION_TYPE + " TEXT, " +
                COLUMN_ADDITIONAL_COMMENT + " TEXT, " +
                COLUMN_OBSERVATION_TIME + " TEXT);";
        db.execSQL(query);
        db.execSQL(query1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + OBSERVATION_TABLE_NAME);
        if (oldVersion < 2) {
            // Thực hiện cập nhật cho phiên bản 2
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_HIKE_LENGTH + " REAL");
        }
        onCreate(db);
    }

    public void addHike(String name, String location, String date, String length, String parkingAvailable, String difficultyLevel, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_LOCATION, location);
        contentValues.put(COLUMN_DATE_OF_HIKE, date);
        contentValues.put(COLUMN_HIKE_LENGTH, length); // Cập nhật tên cột cho độ dài chuyến đi (nếu bạn đã tạo cột này)
        contentValues.put(COLUMN_PARKING_AVAILABLE, parkingAvailable);
        contentValues.put(COLUMN_DIFFICULTY_LEVEL, difficultyLevel);

        contentValues.put(COLUMN_DESCRIPTION, description);

        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            Toast.makeText(context, "Please fill all required fields", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully added!", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor readAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public void updateHike(String id, String name, String location, String date, String parkingAvailable, String difficultyLevel, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_LOCATION, location);
        contentValues.put(COLUMN_DATE_OF_HIKE, date);
        contentValues.put(COLUMN_PARKING_AVAILABLE, parkingAvailable);
        contentValues.put(COLUMN_DIFFICULTY_LEVEL, difficultyLevel);
        contentValues.put(COLUMN_DESCRIPTION, description);

        long result = db.update(
                TABLE_NAME,
                contentValues,
                "_id = ?",
                new String[]{id});

        if (result == -1) {
            Toast.makeText(context, "UPDATE: FAILED ...", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "UPDATE: DONE ...", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteOneRow(String rowId) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id = ?", new String[]{rowId});
        if (result == -1) {
            Toast.makeText(context, "Delete failed.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Deleted.", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }

    public void addObservation(String hike_id, String observation_type, String observation_time, String additional_comment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_HIKE_ID, hike_id);
        contentValues.put(COLUMN_OBSERVATION_TYPE, observation_type);
        contentValues.put(COLUMN_ADDITIONAL_COMMENT, additional_comment);
        contentValues.put(COLUMN_OBSERVATION_TIME, observation_time);

        long result = db.insert(OBSERVATION_TABLE_NAME, null, contentValues);

        if (result == -1) {
            Toast.makeText(context, "Please fill all required fields", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully added!", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor readAllObservations(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + OBSERVATION_TABLE_NAME + " WHERE " + COLUMN_HIKE_ID + " = '" + id + "'";
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor searchHike(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME + " LIKE '%" + name + "%'";
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
    public static String getColumnId() {
        return COLUMN_ID;
    }
    public Cursor readAllExpenses(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + OBSERVATION_TABLE_NAME + " WHERE " + COLUMN_HIKE_ID + " = '" + id + "'";
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
}
