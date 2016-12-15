
package com.maple.yuanweinan.demoapplication.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.maple.yuanweinan.demoapplication.utils.LogUtils;

import java.util.ArrayList;

public abstract class DataBaseHelper extends SQLiteOpenHelper {
      private final Context mContext;
    private boolean mIsNewDB = false;
    private SQLiteQueryBuilder msqlQB = null;
    private boolean mUpdateResult = true;

    public DataBaseHelper(Context context, String dataBaseName, int dataBaseVersion) {
        super(context, dataBaseName, (CursorFactory)null, dataBaseVersion);
        this.mContext = context;
        this.msqlQB = new SQLiteQueryBuilder();
        SQLiteDatabase db = null;

        try {
            db = this.getWritableDatabase();
            if(!this.mUpdateResult) {
                if(db != null) {
                    db.close();
                }

                this.mContext.deleteDatabase(this.getDbName());
                this.getWritableDatabase();
            }
        } catch (SQLiteException var6) {
            this.mContext.deleteDatabase(this.getDbName());
        } catch (IllegalStateException var7) {
            ;
        }

    }

    public void onCreate(SQLiteDatabase db) {
        this.mUpdateResult = true;
        this.mIsNewDB = true;
        db.beginTransaction();

        try {
            this.onCreateTables(db);
            db.setTransactionSuccessful();
        } catch (SQLException var6) {
            var6.printStackTrace();
        } finally {
            db.endTransaction();
        }

    }

    public boolean isExistTable(SQLiteDatabase db, String tableName) {
        boolean result = false;
        Cursor cursor = null;
        String where = "type=\'table\' and name=\'" + tableName + "\'";

        try {
            cursor = db.query("sqlite_master", (String[])null, where, (String[])null, (String)null, (String)null, (String)null);
            if(cursor != null && cursor.moveToFirst()) {
                result = true;
            }
        } catch (SQLiteException var10) {
            var10.printStackTrace();
        } finally {
            if(cursor != null) {
                cursor.close();
            }

        }

        return result;
    }

    private boolean isExistColumnInTable(SQLiteDatabase db, String tableName, String columnName) {
        boolean result = false;
        Cursor cursor = null;

        try {
            String[] e = new String[]{columnName};
            cursor = db.query(tableName, e, (String)null, (String[])null, (String)null, (String)null, (String)null);
            if(cursor != null && cursor.getColumnIndex(columnName) >= 0) {
                result = true;
            }
        } catch (Exception var10) {
            LogUtils.i("DatabaseHelper", "isExistColumnInTable has exception");
            result = false;
        } finally {
            if(cursor != null) {
                cursor.close();
            }

        }

        return result;
    }

    public void addColumnToTable(SQLiteDatabase db, String tableName, String columnName, String columnType, String defaultValue) {
        if(!this.isExistColumnInTable(db, tableName, columnName)) {
            db.beginTransaction();

            try {
                String e = "ALTER TABLE " + tableName + " ADD " + columnName + " " + columnType;
                db.execSQL(e);
                if(defaultValue != null) {
                    if(columnType.equals("TEXT")) {
                        defaultValue = "\'" + defaultValue + "\'";
                    }

                    e = "update " + tableName + " set " + columnName + " = " + defaultValue;
                    db.execSQL(e);
                }

                db.setTransactionSuccessful();
            } catch (Exception var10) {
                var10.printStackTrace();
            } finally {
                db.endTransaction();
            }
        }

    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LogUtils.i("DatabaseHelper", "onDowngrade oldVersion=" + oldVersion + ", newVersion=" + newVersion);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion >= 1 && oldVersion <= newVersion && newVersion <= this.getDbCurrentVersion()) {
            ArrayList upgradeDBFuncS = new ArrayList();
            this.onAddUpgrades(upgradeDBFuncS);

            for(int i = oldVersion - 1; i < newVersion - 1; ++i) {
                this.mUpdateResult = ((DataBaseHelper.UpgradeDB)upgradeDBFuncS.get(i)).onUpgradeDB(db);
                if(!this.mUpdateResult) {
                    break;
                }
            }

            upgradeDBFuncS.clear();
        } else {
            LogUtils.i("testDataBase", "onUpgrade() false oldVersion = " + oldVersion + ", newVersion = " + newVersion);
        }
    }

    public void execSql(String sql) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.execSQL(sql);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public Cursor rawQuery(String sql, String[] selectionArgs) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            return db.rawQuery(sql, selectionArgs);
        } catch (Exception var5) {
            var5.printStackTrace();
            return null;
        }
    }

    public Cursor query(String tableName, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return this.query(tableName, projection, selection, selectionArgs, (String)null, (String)null, sortOrder);
    }

    public Cursor query(String tableName, String[] projection, String selection, String[] selectionArgs, String groupBy, String having, String sortOrder) {
        Cursor result = null;

        try {
            SQLiteDatabase e = this.getReadableDatabase();
            result = e.query(tableName, projection, selection, selectionArgs, groupBy, having, sortOrder);
        } catch (SQLException var10) {
            LogUtils.i("data", "SQLException when query in " + tableName + ", " + selection);
        } catch (IllegalStateException var11) {
            LogUtils.i("data", "IllegalStateException when query in " + tableName + ", " + selection);
        }

        return result;
    }

    public Cursor queryCrossTables(String tableName, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor result = null;
        SQLiteQueryBuilder var7 = this.msqlQB;
        synchronized(this.msqlQB) {
            this.msqlQB.setTables(tableName);

            try {
                SQLiteDatabase e = this.getReadableDatabase();
                result = this.msqlQB.query(e, projection, selection, selectionArgs, (String)null, (String)null, sortOrder);
            } catch (SQLException var10) {
                LogUtils.i("data", "SQLException when query in " + tableName + ", " + selection);
            } catch (IllegalStateException var11) {
                LogUtils.i("data", "IllegalStateException when query in " + tableName + ", " + selection);
            }

            return result;
        }
    }

    public long insert(String tableName, ContentValues initialValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        long rowId = 0L;

        try {
            rowId = db.insert(tableName, (String)null, initialValues);
            return rowId;
        } catch (Exception var7) {
            LogUtils.i("data", "Exception when insert in " + tableName);
            return -1;
        }
    }

    public int delete(String tableName, String selection, String[] selectionArgs)  {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean count = false;

        try {
            int count1 = db.delete(tableName, selection, selectionArgs);
            return count1;
        } catch (Exception var7) {
            LogUtils.i("data", "Exception when delete in " + tableName + ", " + selection);
            return -1;
        }
    }

    public int update(String tableName, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean count = false;

        try {
            int count1 = db.update(tableName, values, selection, selectionArgs);
            return count1;
        } catch (Exception var8) {
            LogUtils.i("data", "Exception when update in " + tableName + ", " + selection);
        }
        return -1;
    }

    public int updateOrInsert(String tableName, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean count = false;

        try {
            int count1 = db.update(tableName, values, selection, selectionArgs);
            if(count1 <= 0) {
                db.insert(tableName, (String)null, values);
            }

            return count1;
        } catch (Exception var8) {
            LogUtils.i("data", "Exception when updateOrInsert in " + tableName + ", " + selection);
        }
        return -1;
    }


    public boolean isNewDB() {
        return this.mIsNewDB;
    }

    public boolean openDBWithWorldReadable() {
        try {
            this.close();
            return this.mContext.openOrCreateDatabase(this.getDbName(), 1, (CursorFactory)null) != null;
        } catch (Exception var2) {
            this.close();
            return false;
        }
    }

    public void beginTransaction() {
        try {
            SQLiteDatabase e = this.getWritableDatabase();
            if(null != e) {
                e.beginTransaction();
            }
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public void setTransactionSuccessful() {
        try {
            SQLiteDatabase e = this.getWritableDatabase();
            if(null != e) {
                e.setTransactionSuccessful();
            }
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public void endTransaction() {
        try {
            SQLiteDatabase e = this.getWritableDatabase();
            e.endTransaction();
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public abstract int getDbCurrentVersion();

    public abstract String getDbName();

    public abstract void onCreateTables(SQLiteDatabase var1);

    public abstract void onAddUpgrades(ArrayList<DataBaseHelper.UpgradeDB> var1);

    public abstract class UpgradeDB {
        public UpgradeDB() {
        }

        public abstract boolean onUpgradeDB(SQLiteDatabase var1);
    }
}
