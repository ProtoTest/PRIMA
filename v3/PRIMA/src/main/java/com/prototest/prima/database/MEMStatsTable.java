package com.prototest.prima.database;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

public class MEMStatsTable {
   public static final String TAG = "MEMORY Stats Data";

   public static final String TABLE_MEM = "mem_stats";
   public static final String COLUMN_ID = BaseColumns._ID;
   public static final String COLUMN_CURRENT = "current";
   public static final String COLUMN_MAX = "max";
   public static final String COLUMN_AVAILABLE = "available";
   private static final String NOT_NULL = "NOT NULL";

   private static final String CREATE_TABLE = String.format("CREATE TABLE %s "
         + "(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INT %s, %s INT %s, %s INT %s)", TABLE_MEM,
         COLUMN_ID, COLUMN_CURRENT, NOT_NULL, COLUMN_MAX, NOT_NULL, COLUMN_AVAILABLE, NOT_NULL);

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
      db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEM);
   }
}
