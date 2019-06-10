package com.nakiha.event.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class eventDatabaseHelper extends SQLiteOpenHelper {
    public static final String BOOK_NAME = "Event";
    public static final String KEY_NAME = "my_key";
    /* 数据库中每行除了 key 就有一个json对象转化为的 String */
    private static final String CREATE_BOOK = "create table Event ("
            + "my_key text primary key,"
            + "event_json text)";

    private Context mContext;

    public eventDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 数据库更新之后丢掉原来的库
        db.execSQL("drop table if exists Event");
        // 重建库
        onCreate(db);
    }
}
