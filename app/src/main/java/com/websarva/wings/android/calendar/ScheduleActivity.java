package com.websarva.wings.android.calendar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity {

    Button btn_back;
    Button btn_add;
    Button btn_todohome;
    CalendarView cal;

    TextView sc_title;
    TextView fsc_date;
    TextView esc_date;
    TextView fsc_time;
    TextView esc_time;
    TextView sc_memo;
    private DatabaseCalendar _helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        //インテントオブジェクトを取得。
        Intent intent=getIntent();


        cal = findViewById(R.id.calendarView);

        btn_back=findViewById(R.id.sc_back);
        btn_add=findViewById(R.id.sc_add);
        btn_todohome=findViewById(R.id.btnTodo);

        sc_title=findViewById(R.id.et_sc_title);
        fsc_date=findViewById(R.id.et_fsc_date);
        esc_date=findViewById(R.id.et_esc_date);
        fsc_time=findViewById(R.id.et_fsc_time);
        esc_time=findViewById(R.id.et_esc_time);
        sc_memo=findViewById(R.id.et_sc_memo);
        //DBヘルパーオブジェクトを生成。
        _helper=new DatabaseCalendar(ScheduleActivity.this);

        //戻るボタンをタップした時の処理。
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //インテントオブジェクトを生成
                Intent intent=new Intent(ScheduleActivity.this, MainActivity.class);
                //第2画面の起動。
                startActivity(intent);
            }
        });

        btn_todohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //インテントオブジェクトを生成
                Intent intent=new Intent(ScheduleActivity.this, Todohome.class);
                //第2画面の起動。
                startActivity(intent);
            }
        });


        //追加ボタンを押したときの処理
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TextViewからテキストを取得してString型に変換
                String title=sc_title.getText().toString();
                String first_date=fsc_date.getText().toString();
                String end_date=esc_date.getText().toString();
                String first_time=fsc_time.getText().toString();
                String end_time=esc_time.getText().toString();
                String memo=sc_memo.getText().toString();

                if(title.length()!=0&&first_date.length()!=0&&end_date.length()!=0
                        &&first_time.length()!=0&&end_time.length()!=0){
                    //データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得。
                    SQLiteDatabase db=_helper.getWritableDatabase();
                    //インサート用のSQL文字列を用意。
                    String sqlInsert="INSERT INTO schedule(sc_title,fsc_date,fsc_time,esc_date,esc_time,sc_memo)VALUES(?,?,?,?,?,?)";
                    //SQL文字列を元にプリペアドステートメントを取得。
                    SQLiteStatement stmt=db.compileStatement(sqlInsert);
                    //変数のバインド
                    stmt.bindString(1,title);
                    stmt.bindString(2,first_date);
                    stmt.bindString(3,first_time);
                    stmt.bindString(4,end_date);
                    stmt.bindString(5,end_time);
                    stmt.bindString(6,memo);
                    //インサートSQLの実行。
                    stmt.executeInsert();
                    Log.d("memo",memo);
                    new androidx.appcompat.app.AlertDialog.Builder(v.getContext())
                            .setTitle("成功")
                            .setMessage("データが正常に挿入されました")
                            .setPositiveButton(android.R.string.ok, null)
                            .show();

                }
            }
        });
        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String day = Integer.toString(year) + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", dayOfMonth);
                fsc_date.setText(day);
            }
        });



    }
    /*
    // データを取得してログに出力するメソッド
    private void logDatabaseContents() {
        SQLiteDatabase db = _helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM schedule", null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    // 各列のインデックスを取得
                    int idIndex = cursor.getColumnIndex("sc_id");
                    int titleIndex = cursor.getColumnIndex("sc_title");
                    int firstDateIndex = cursor.getColumnIndex("fsc_date");
                    int firstTimeIndex = cursor.getColumnIndex("fsc_time");
                    int endDateIndex = cursor.getColumnIndex("esc_date");
                    int endTimeIndex = cursor.getColumnIndex("esc_time");
                    int memoIndex = cursor.getColumnIndex("sc_memo");

                    // カラムの値を取得してログに出力
                    int id = cursor.getInt(idIndex);
                    String title = cursor.getString(titleIndex);
                    String firstDate = cursor.getString(firstDateIndex);
                    String firstTime = cursor.getString(firstTimeIndex);
                    String endDate = cursor.getString(endDateIndex);
                    String endTime = cursor.getString(endTimeIndex);
                    String memo = cursor.getString(memoIndex);

                    Log.d("DatabaseInfo", "ID: " + id +
                            ", Title: " + title +
                            ", First Date: " + firstDate +
                            ", First Time: " + firstTime +
                            ", End Date: " + endDate +
                            ", End Time: " + endTime +
                            ", Memo: " + memo);
                } while (cursor.moveToNext());
            } else {
                Log.d("DatabaseInfo", "No data found in schedule table.");
            }
        } finally {
            cursor.close();
        }
    }*/



    @Override
    protected void onDestroy(){
        //DBヘルパーオブジェクトの解放。
        _helper.close();
        super.onDestroy();
    }
}