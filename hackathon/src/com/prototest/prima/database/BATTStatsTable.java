package com.prototest.prima.database;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

public class BATTStatsTable {
   public static final String TAG = "BATTERY Stats Data";

   // can calculate battery percentage from level/scale*100

   public static final String TABLE_BATT = "batt_stats";
   public static final String COLUMN_ID = BaseColumns._ID;
   public static final String COLUMN_SCALE = "scale";
   public static final String COLUMN_LEVEL = "level";
   public static final String COLUMN_VOLTAGE = "voltage";
   public static final String COLUMN_TEMP = "temp";
   private static final String NOT_NULL = "NOT NULL";

   private static final String CREATE_TABLE = String.format("CREATE TABLE %s "
         + "(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INT %s, %s INT %s, %s INT %s)", TABLE_BATT,
         COLUMN_ID, COLUMN_SCALE, NOT_NULL, COLUMN_LEVEL, NOT_NULL, COLUMN_VOLTAGE, NOT_NULL,
         COLUMN_TEMP, NOT_NULL);

   public static void onCreate(SQLiteDatabase db) {
      Log.d(TAG, "onCreate with sql" + CREATE_TABLE);
      db.execSQL(CREATE_TABLE);
   }

   public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      Log.d(TAG, "onUpgrade");

      // Usually an ALTER TABLE statement for migrations

      // below is a hack to just drop the table instead of actually migrating
      dropTable(db);
      onCreate(db);
   }

   public static void dropTable(SQLiteDatabase db) {
      db.execSQL("DROP TABLE IF EXISTS " + TABLE_BATT);
   }
}
