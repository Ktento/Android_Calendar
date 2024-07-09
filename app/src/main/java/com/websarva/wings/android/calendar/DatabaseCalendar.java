package com.websarva.wings.android.calendar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseCalendar extends SQLiteOpenHelper {
    //データベースファイル名の定数フィールド。
    private static final String DATABASE_NAME="calendar.db";
    //バージョン情報の定数フィールド。
    private static final int DATABASE_VERSION=1;

    //コンストラクタ
    public DatabaseCalendar(Context context){
        //親クラスのコンストラクタの呼び出し。
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        //テーブル作成用SQL文字列の作成
        StringBuilder sb=new StringBuilder();
        sb.append("CREATE TABLE calendar(");
        sb.append("id INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb.append("sc_id INTEGER,");
        sb.append("todo_id INTEGER,");
        sb.append("FOREIGN KEY(sc_id) REFERENCES schedule(sc_id),");
        sb.append("FOREIGN KEY(todo_id) REFERENCES todolist(todo_id)");
        sb.append(");");
        String sql=sb.toString();

        //ＳＱＬの実行
        db.execSQL(sql);

        //テーブル作成用SQL文字列の作成
        StringBuilder sb2=new StringBuilder();
        sb2.append("CREATE TABLE schedule(");
        sb2.append("sc_id INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb2.append("sc_title VARCHAR(15),");
        sb2.append("fsc_date DATE,");
        sb2.append("fsc_time TIME,");
        sb2.append("esc_date DATE,");
        sb2.append("esc_time TIME,");
        sb2.append("sc_memo VARCHAR(50)");
        sb2.append(");");
        String sql2=sb2.toString();

        //ＳＱＬの実行
        db.execSQL(sql2);

        //テーブル作成用SQL文字列の作成
        StringBuilder sb3=new StringBuilder();
        sb3.append("CREATE TABLE todolist(");
        sb3.append("todo_id INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb3.append("todo_title VARCHAR(15),");
        sb3.append("todo_date DATE,");
        sb3.append("todo_time TIME,");
        sb3.append("todo_memo VARCHAR(50)");
        sb3.append(");");
        String sql3=sb3.toString();

        //ＳＱＬの実行
        db.execSQL(sql3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

    }
}
