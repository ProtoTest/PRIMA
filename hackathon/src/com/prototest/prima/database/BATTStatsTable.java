package com.prototest.prima.database;

import android.database.SQLException;
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
   public static final String COLUMN_CREATED_AT = "created_at";

   private static final String CREATE_TABLE = String
         .format(
               "CREATE TABLE %s "
                     + "(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INT NOT NULL, %s INT NOT NULL, %s INT NOT NULL, %s INT NOT NULL, %s DATETIME default current_timestamp)",
               TABLE_BATT, COLUMN_ID, COLUMN_SCALE, COLUMN_LEVEL, COLUMN_VOLTAGE, COLUMN_TEMP,
               COLUMN_CREATED_AT);

   public static void onCreate(SQLiteDatabase db) {
      Log.d(TAG, "onCreate with sql: " + CREATE_TABLE);

      try {
         db.execSQL(CREATE_TABLE);
      } catch (SQLException e) {
         Log.e(TAG, e.getMessage());
      }
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
