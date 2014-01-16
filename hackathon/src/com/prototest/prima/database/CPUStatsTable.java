package com.prototest.prima.database;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

public class CPUStatsTable {
   public static final String TAG = "CPU Stats Data";

   public static final String TABLE_CPU = "cpu_stats";
   public static final String COLUMN_ID = BaseColumns._ID;
   public static final String COLUMN_USED = "used";
   public static final String COLUMN_FREE = "free";
   public static final String COLUMN_CREATED_AT = "created_at";
   private static final String NOT_NULL = "NOT NULL";

   private static final String CREATE_TABLE = String
         .format(
               "CREATE TABLE %s "
                     + "(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INT %s, %s INT %s, %s DATETIME default current_timestamp)",
               TABLE_CPU, COLUMN_ID, COLUMN_USED, NOT_NULL, COLUMN_FREE, NOT_NULL,
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
      db.execSQL("DROP TABLE IF EXISTS " + TABLE_CPU);
   }
}
