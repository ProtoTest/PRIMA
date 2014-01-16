package com.prototest.prima.contentprovider;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import com.prototest.prima.database.BATTStatsTable;
import com.prototest.prima.database.CPUStatsTable;
import com.prototest.prima.database.DbHelper;
import com.prototest.prima.database.MEMStatsTable;

public class PrimaContentProvider extends ContentProvider {

   private static DbHelper database;

   private static final String TAG = "Prima Content Provider";

   // TODO: Stick this in a contract. Android dev guide recommends this:
   // http://www.grokkingandroid.com/android-tutorial-writing-your-own-content-provider/
   // https://thenewcircle.com/s/post/1375/android_content_provider_tutorial

   // URI matcher
   private static final int BATTS = 100;
   private static final int BATT_ID = 110;
   private static final int CPUS = 200;
   private static final int CPU_ID = 210;
   private static final int MEMS = 300;
   private static final int MEM_ID = 310;

   private static final String AUTHORITY = "com.prototest.android.prima.contentprovider";

   private static final String BASE_PATH_BATT = "batts";
   private static final String BASE_PATH_CPU = "cpus";
   private static final String BASE_PATH_MEM = "mems";

   private static final String SCHEME = "content://";

   public static final Uri CONTENT_URI_BATT = Uri.parse(SCHEME + AUTHORITY + "/" + BASE_PATH_BATT);
   public static final Uri CONTENT_URI_CPU = Uri.parse(SCHEME + AUTHORITY + "/" + BASE_PATH_CPU);
   public static final Uri CONTENT_URI_MEM = Uri.parse(SCHEME + AUTHORITY + "/" + BASE_PATH_MEM);

   // WTF are these used for??? Looks like the client of this content provider uses
   // these...
   public static final String CONTENT_TYPE_BATT = ContentResolver.CURSOR_DIR_BASE_TYPE
         + BASE_PATH_BATT;
   public static final String CONTENT_TYPE_CPU = ContentResolver.CURSOR_DIR_BASE_TYPE
         + BASE_PATH_CPU;
   public static final String CONTENT_TYPE_MEM = ContentResolver.CURSOR_DIR_BASE_TYPE
         + BASE_PATH_MEM;

   // WTF are these used for???
   public static final String CONTENT_ITEM_TYPE_BATT = ContentResolver.CURSOR_DIR_BASE_TYPE
         + "/batt";
   public static final String CONTENT_ITEM_TYPE_CPU = ContentResolver.CURSOR_DIR_BASE_TYPE + "/cpu";
   public static final String CONTENT_ITEM_TYPE_MEM = ContentResolver.CURSOR_DIR_BASE_TYPE + "/mem";

   private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
   // this static initialization block will be executed by the JVM on class load
   static {
      sURIMatcher.addURI(AUTHORITY, BASE_PATH_BATT, BATTS);
      sURIMatcher.addURI(AUTHORITY, BASE_PATH_CPU, CPUS);
      sURIMatcher.addURI(AUTHORITY, BASE_PATH_MEM, MEMS);

      sURIMatcher.addURI(AUTHORITY, BASE_PATH_BATT + "/#", BATT_ID);
      sURIMatcher.addURI(AUTHORITY, BASE_PATH_CPU + "/#", CPU_ID);
      sURIMatcher.addURI(AUTHORITY, BASE_PATH_MEM + "/#", MEM_ID);
   }

   @Override
   public boolean onCreate() {
      Log.d(TAG, "onCreate()");
      database = new DbHelper(getContext());
      return (database == null) ? false : true;
   }

   public static void dropAllTables() {
      Log.d(TAG, "dropAllTables()");
      database.dropAllTables(database.getWritableDatabase());
   }

   public static void createAllTables() {
      Log.d(TAG, "createAllTables()");
      database.createAllTables(database.getWritableDatabase());
   }

   @SuppressWarnings("unchecked")
   @Override
   public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
         String sortOrder) {

      SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

      switch (sURIMatcher.match(uri)) {
      case BATTS:
         checkColumns(projection, BATTStatsTable.class);
         queryBuilder.setTables(BATTStatsTable.TABLE_BATT);
         break;
      case BATT_ID:
         checkColumns(projection, BATTStatsTable.class);
         queryBuilder.setTables(BATTStatsTable.TABLE_BATT);
         queryBuilder.appendWhere(BATTStatsTable.COLUMN_ID + "=" + uri.getLastPathSegment());
         break;
      case CPUS:
         checkColumns(projection, CPUStatsTable.class);
         queryBuilder.setTables(CPUStatsTable.TABLE_CPU);
         break;
      case CPU_ID:
         checkColumns(projection, CPUStatsTable.class);
         queryBuilder.setTables(CPUStatsTable.TABLE_CPU);
         queryBuilder.appendWhere(CPUStatsTable.COLUMN_ID + "=" + uri.getLastPathSegment());
         break;
      case MEMS:
         checkColumns(projection, MEMStatsTable.class);
         queryBuilder.setTables(MEMStatsTable.TABLE_MEM);
         break;
      case MEM_ID:
         checkColumns(projection, MEMStatsTable.class);
         queryBuilder.setTables(MEMStatsTable.TABLE_MEM);
         queryBuilder.appendWhere(MEMStatsTable.COLUMN_ID + "=" + uri.getLastPathSegment());
         break;
      default:
         throw new IllegalArgumentException("Failed to query " + uri + ": Unknown uri");
      }

      SQLiteDatabase db = database.getReadableDatabase();
      Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null,
            sortOrder);
      // make sure that potential listeners are getting notified
      cursor.setNotificationUri(getContext().getContentResolver(), uri);

      return cursor;
   }

   @Override
   public String getType(Uri uri) {
      final String multiple_prefix = "vnd.android.cursor.dir/vnd.com.prototest.android.prima.contentprovider.";
      final String single_prefix = "vnd.android.cursor.item/vnd.com.prototest.android.prima.contentprovider.";

      switch (sURIMatcher.match(uri)) {
      case BATTS:
         return multiple_prefix + BASE_PATH_BATT;
      case BATT_ID:
         return single_prefix + BASE_PATH_BATT;
      case CPUS:
         return multiple_prefix + BASE_PATH_CPU;
      case CPU_ID:
         return single_prefix + BASE_PATH_CPU;
      case MEMS:
         return multiple_prefix + BASE_PATH_MEM;
      case MEM_ID:
         return single_prefix + BASE_PATH_MEM;
      default:
         Log.e(TAG, "unknown uri");
         return null;
      }
   }

   @Override
   public Uri insert(Uri uri, ContentValues values) {
      String basePath;

      long id = 0;
      SQLiteDatabase db = database.getWritableDatabase();

      switch (sURIMatcher.match(uri)) {
      case BATTS:
         id = db.insertWithOnConflict(BATTStatsTable.TABLE_BATT, null, values,
               SQLiteDatabase.CONFLICT_IGNORE);
         break;
      case CPUS:
         id = db.insertWithOnConflict(CPUStatsTable.TABLE_CPU, null, values,
               SQLiteDatabase.CONFLICT_IGNORE);
         break;
      case MEMS:
         id = db.insertWithOnConflict(MEMStatsTable.TABLE_MEM, null, values,
               SQLiteDatabase.CONFLICT_IGNORE);
         break;
      default:
         throw new IllegalArgumentException("Failed to insert " + uri + ": Unknown uri");
      }

      getContext().getContentResolver().notifyChange(uri, null);

      // insert returns -1 on failure
      // append the id of the record on success to the uri
      if (id != -1) {
         uri = Uri.withAppendedPath(uri, Long.toString(id));
      }

      return uri;
   }

   // Initialize the available columns for each table
   private static final String[] columnsAvailBATT;
   private static final String[] columnsAvailCPU;
   private static final String[] columnsAvailMEM;

   static {
      columnsAvailBATT = new String[] { BATTStatsTable.COLUMN_ID, BATTStatsTable.COLUMN_LEVEL,
            BATTStatsTable.COLUMN_SCALE, BATTStatsTable.COLUMN_TEMP, BATTStatsTable.COLUMN_VOLTAGE,
            BATTStatsTable.COLUMN_CREATED_AT };

      columnsAvailCPU = new String[] { CPUStatsTable.COLUMN_ID, CPUStatsTable.COLUMN_FREE,
            CPUStatsTable.COLUMN_USED, CPUStatsTable.COLUMN_CREATED_AT };

      columnsAvailMEM = new String[] { MEMStatsTable.COLUMN_ID, MEMStatsTable.COLUMN_AVAILABLE,
            MEMStatsTable.COLUMN_CURRENT, MEMStatsTable.COLUMN_CREATED_AT };
   }

   /**
    * Check to ensure the columns requested for database access exist
    * 
    * @param projection
    *           Array of database columns that should be included for each row retrieved
    * @param table
    *           A generic class table representation
    */
   private <T> void checkColumns(String[] projection, Class<T> table) {
      if (projection != null) {
         HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
         HashSet<String> availableColumns = null;

         if (table.getName().equals(BATTStatsTable.class.getName())) {
            availableColumns = new HashSet<String>(
                  Arrays.asList(PrimaContentProvider.columnsAvailBATT));
         } else if (table.getName().equals(CPUStatsTable.class.getName())) {
            availableColumns = new HashSet<String>(
                  Arrays.asList(PrimaContentProvider.columnsAvailCPU));
         } else if (table.getName().equals(MEMStatsTable.class.getName())) {
            availableColumns = new HashSet<String>(
                  Arrays.asList(PrimaContentProvider.columnsAvailMEM));
         } else {
            throw new IllegalArgumentException(table.getName() + ": is not supported");
         }

         // check if all columns which are requested are available
         if (!availableColumns.containsAll(requestedColumns)) {
            throw new IllegalArgumentException("Unknown columns in projection");
         }
      }
   }

   @Override
   public int delete(Uri uri, String selection, String[] selectionArgs) {
      Log.i(TAG, "NOT IMPLEMENTED");
      return 0;
   }

   @Override
   public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
      Log.i(TAG, "NOT IMPLEMENTED");
      return 0;
   }
}
