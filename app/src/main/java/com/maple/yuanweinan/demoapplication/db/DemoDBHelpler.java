package com.maple.yuanweinan.demoapplication.db;

import java.util.ArrayList;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author yuanweinan
 *
 */
public class DemoDBHelpler extends DataBaseHelper {
	private static DemoDBHelpler sInstance;
	
    public final static int DB_VERSION_MAX = 1; //current version
    private final static String DATABASE_NAME = "demo.db";

    private DemoDBHelpler(Context context) {
        super(context, DATABASE_NAME, DB_VERSION_MAX);
    }
    
    public static DemoDBHelpler getInstance(Context context) {
    	if (sInstance == null) {
    		sInstance = new DemoDBHelpler(context);
            UserInfoTable.initData(sInstance);
    	}
    	return sInstance;
    }

    @Override
    public int getDbCurrentVersion() {
        return DB_VERSION_MAX;
    }
    
    @Override
    public String getDbName() {
        return DATABASE_NAME;
    }

    @Override
    public void onCreateTables(SQLiteDatabase db) {
    	//TODO
        db.execSQL(UserInfoTable.CREATETABLESQL);

    }

    @Override
    public void onAddUpgrades(ArrayList<UpgradeDB> upgrades) {
//    	upgrades.add(new UpgradeDB1To2());
    }

//    class UpgradeDB1To2 extends UpgradeDB {
//        @Override
//        public boolean onUpgradeDB(SQLiteDatabase db) {
//        	try {
//        		db.execSQL(AppAdStateInfoTable.ADD_STATE_UPDATE_TIME);
//        		return true;
//        	} catch (Exception e) {
//        	}
//        	return false;
//        }
//    }

}
