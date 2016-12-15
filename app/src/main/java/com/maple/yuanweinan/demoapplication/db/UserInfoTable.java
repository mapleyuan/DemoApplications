package com.maple.yuanweinan.demoapplication.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import com.maple.yuanweinan.demoapplication.pojo.UserInfo;

/**
 * @author yuanweinan
 */
public class UserInfoTable {

    private static final String TABLENAME = "UserInfo";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String SEX = "sex";
    private static final String UPDATE_TIME = "update_time";

    public static final String CREATETABLESQL = "create table USERINFO "
            + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + NAME + " text, " + SEX + " integer, "
            + UPDATE_TIME + " text" + ")";

    public static ContentValues getContentValue(UserInfo info) {
        if (null == info) {
            return null;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, info.mID);
        contentValues.put(NAME, info.mName);
        contentValues.put(SEX, info.mSex);
        contentValues.put(UPDATE_TIME, info.mUpdateTime);
        return contentValues;
    }

    public static void initData(DataBaseHelper dataBaseHelper) {
        UserInfo info = new UserInfo();
        info.mName = "Tom";
        info.mSex = 0;
        insert(dataBaseHelper, info);
        UserInfo info1 = new UserInfo();
        info1.mName = "Mary";
        info1.mSex = 1;
        insert(dataBaseHelper, info1);
    }

    /**
     * 查询消息列表
     *
     * @param dbHelper Dbhelper
     * @return 消息列表
     */
    public static List<UserInfo> queryAll(DataBaseHelper dbHelper) {
        List<UserInfo> datas = new ArrayList<UserInfo>();

        String[] columns = {ID, SEX, NAME, UPDATE_TIME};
        Cursor cursor = dbHelper.query(TABLENAME, columns, null, null, null);

        if (null == cursor) {
            return datas;
        }

        try {
            if (cursor.moveToFirst()) {
                do {
                    UserInfo info = new UserInfo();
                    info.mID = cursor.getLong(0);
                    info.mSex = cursor.getInt(1);
                    info.mName = cursor.getString(2);
                    info.mUpdateTime = cursor.getLong(3);
                    datas.add(info);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }

        return datas;
    }


    /**
     * 插入对话信息
     *
     * @param dbHelper Dbhelper
     * @param info     DoubleChatInfo
     * @return 成功:返回插入行id 失败:返回-1
     */
    public static long insert(DataBaseHelper dbHelper, UserInfo info) {
        if (null == info) {
            return -1;
        }

        try {
            info.mUpdateTime = System.currentTimeMillis();
            info.mID = System.currentTimeMillis();
            long result = dbHelper.insert(TABLENAME, getContentValue(info));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }
}
