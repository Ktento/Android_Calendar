package com.websarva.wings.android.calendar;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    CalendarView cal;
    TextView selectDay;
    Button btn_todo;
    Button btn_add;

    //選択されたスケジュールの主キーIDを表すフィールド。
    private int _scId=-1;
    //現在選択されている日付を表すフィールド。
    private String date="";
    //データベースヘルパーオブジェクト
    private DatabaseCalendar _helper;

    HashMap<Integer, Integer> hashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //DBヘルパーオブジェクトを生成。
        _helper=new DatabaseCalendar(MainActivity.this);
        //ListViewを取得
        ListView sc_list=findViewById(R.id.sc_list);

        cal = findViewById(R.id.calendarView);
        btn_todo=findViewById(R.id.btnTodo);
        btn_add=findViewById(R.id.btnAdd);

        sc_list.setOnItemClickListener(new ListItemClickListener());

        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                //HASHMAPのリセット
                hashMap.clear();
                String day = Integer.toString(year) + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", dayOfMonth);
                date=day;
                //データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得。
                SQLiteDatabase db=_helper.getWritableDatabase();
                //スケジュールが存在するか検索するSQL文の作成
                String sqlSelect="SELECT sc_id,sc_title,fsc_date,fsc_time FROM schedule WHERE fsc_date='"+day+"' ORDER BY fsc_time ASC";
                //SQLの実行。
                Cursor cursor=db.rawQuery(sqlSelect,null);
                //データベースから取得した値を格納する変数の用意。データがなかった時のための初期値も用意。
                String note="";
                String note2="";
                String note3="";
                //データベースから取得した値を格納する変数の用意。データがなかった時のための初期値も用意。
                List<String> resultList = new ArrayList<>();
                int count=0;
                //SQL実行の戻り値であるカーソルオブジェクトをループさせてデータベース内のデータを取得。
                while(cursor.moveToNext()){
                    //カラムのインデックス値を取得。
                    int idxNote=cursor.getColumnIndex("sc_title");
                    //カラムのインデックス値を取得。
                    int idxNote2=cursor.getColumnIndex("fsc_date");
                    //カラムのインデックス値を取得。
                    int idxNote3=cursor.getColumnIndex("fsc_time");
                    //カラムのインデックス値を取得。
                    int idxNote4=cursor.getColumnIndex("sc_id");
                    //カラムのインデックス値を元にデータを取得。
                    note=cursor.getString(idxNote);
                    //カラムのインデックス値を元にデータを取得。
                    note2=cursor.getString(idxNote2);
                    //カラムのインデックス値を元にデータを取得。
                    note3=cursor.getString(idxNote3);
                    //カラムのインデックス値を元にデータを取得。
                    int id=cursor.getInt(idxNote4);
                    String result=note+"   "+note2+"   "+note3;
                    resultList.add(result);
                    //並び替えたsc_idを順番にHashMapに保存
                    hashMap.put(count,id);
                    count++;

                }
                // Cursorをクローズ
                cursor.close();
                ArrayAdapter<String> arrayAdapter=
                        new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,resultList);
                sc_list.setAdapter(arrayAdapter);
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //インテントオブジェクトを生成
                Intent intent=new Intent(MainActivity.this,ScheduleActivity.class);
                //第2画面の起動。
                startActivity(intent);
            }
        });

        btn_todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //インテントオブジェクトを生成
                Intent intent=new Intent(MainActivity.this, Todohome.class);
                //第2画面の起動。
                startActivity(intent);
            }
        });

    }

    //リストがタップされた時の処理が記述されたメンバクラス。
    private class ListItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            // タップされた行番号をフィールドの主キーIDに代入。
            _scId = getScIdFromPosition(position);
            //インテントオブジェクトを生成
            Intent intent=new Intent(MainActivity.this, EditCalendar.class);
            //第2画面に送るデータを格納
            intent.putExtra("sc_id",_scId);
            //第2画面の起動。
            startActivity(intent);

        }

        // リストアダプターから主キーIDを取得するメソッド
        private int getScIdFromPosition(int position) {
            int scId=hashMap.get(position);
            return scId;
        }
    }
    @Override
    protected void onDestroy(){
        //DBヘルパーオブジェクトの解放。
        _helper.close();
        super.onDestroy();
    }
}
