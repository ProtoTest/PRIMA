package com.prototest.prima.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {

   static final String TAG = "Database Helper";
   static final String DB_NAME = "prima.db";
   static final int DB_VERSION = 1;

   public DbHelper(Context context) {
      super(context, DB_NAME, null, DB_VERSION);
      Log.d(TAG, "constructor");
   }

   @Override
   public void onCreate(SQLiteDatabase db) {
      Log.d(TAG, "onCreate()");
      BATTStatsTable.onCreate(db);
      CPUStatsTable.onCreate(db);
      MEMStatsTable.onCreate(db);
   }

   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      Log.d(TAG, String.format("onUpgrade() from %d to %d", oldVersion, newVersion));
      BATTStatsTable.onUpgrade(db, oldVersion, newVersion);
      CPUStatsTable.onUpgrade(db, oldVersion, newVersion);
      MEMStatsTable.onUpgrade(db, oldVersion, newVersion);
   }

   public void dropAllTables(SQLiteDatabase db) {
      BATTStatsTable.dropTable(db);
      CPUStatsTable.dropTable(db);
      MEMStatsTable.dropTable(db);
   }

   public void createAllTables(SQLiteDatabase db) {
      onCreate(db);
   }
}
