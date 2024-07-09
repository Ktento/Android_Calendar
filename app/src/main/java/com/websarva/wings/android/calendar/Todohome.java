package com.websarva.wings.android.calendar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class Todohome extends AppCompatActivity {

    Button btn_back;
    Button btn_add;

    //データベースヘルパーオブジェクト
    private DatabaseCalendar _helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todohome);

        ListView lv_todo_home = findViewById(R.id.lv_todo_home);

        btn_back = findViewById(R.id.btn_back);
        btn_add = findViewById(R.id.btn_add);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //インテントオブジェクトを生成
                Intent intent=new Intent(Todohome.this, TodoAdd.class);
                //第2画面の起動。
                startActivity(intent);
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //インテントオブジェクトを生成
                Intent intent=new Intent(Todohome.this,MainActivity.class);
                //第2画面の起動。
                startActivity(intent);
            }
        });

        //DBヘルパーオブジェクトを生成
        _helper = new DatabaseCalendar(Todohome.this);

        //データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得。
        SQLiteDatabase db = _helper.getWritableDatabase();
        //スケジュールが存在するか検索するSQL文の作成
        String sqlSelect="SELECT todo_title,todo_date,todo_time,todo_memo FROM todolist ORDER BY todo_date ASC,todo_time ASC";
        //SQLの実行。
        Cursor cursor=db.rawQuery(sqlSelect,null);
        //データベースから取得した値を格納する変数の用意。データがなかった時のための初期値も用意。
        String dbtitle="";
        String dbdate="";
        String dbtime="";
        String dbmemo="";
        //データベースから取得した値を格納する変数の用意。データがなかった時のための初期値も用意。
        List<String> resultList = new ArrayList<>();
        //SQL実行の戻り値であるカーソルオブジェクトをループさせてデータベース内のデータを取得。
        while(cursor.moveToNext()){
            //カラムのインデックス値を取得。
            int idxTitle=cursor.getColumnIndex("todo_title");
            int idxDate=cursor.getColumnIndex("todo_date");
            int idxTime=cursor.getColumnIndex("todo_time");
            int idxMemo=cursor.getColumnIndex("todo_memo");
            //カラムのインデックス値を元にデータを取得。
            dbtitle=cursor.getString(idxTitle);
            dbdate=cursor.getString(idxDate);
            dbtime=cursor.getString(idxTime);
            dbmemo=cursor.getString(idxMemo);

            String result=dbdate+"   "+dbtime+"   "+dbtitle+"   "+dbmemo;
            resultList.add(result);

        }
        ArrayAdapter<String> arrayAdapter=
                new ArrayAdapter<String>(Todohome.this, android.R.layout.simple_list_item_1,resultList);
        lv_todo_home.setAdapter(arrayAdapter);

    }

    @Override
    protected void onDestroy(){
        //DBヘルパーオブジェクトの解放
        _helper.close();
        super.onDestroy();
    }
}