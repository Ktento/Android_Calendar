package com.websarva.wings.android.calendar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EditCalendar extends AppCompatActivity {
    TextView title;
    TextView fdate;
    TextView edate;
    TextView ftime;
    TextView etime;
    TextView memo;

    Button btn_save;
    Button btn_del;
    Button btn_back;
    private DatabaseCalendar _helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_calendar);

        title=findViewById(R.id.mt_ec_title);
        fdate=findViewById(R.id.mt_ec_fdate);
        edate=findViewById(R.id.mt_ec_edate);
        ftime=findViewById(R.id.mt_ec_ftime);
        etime=findViewById(R.id.mt_ec_etime);
        memo=findViewById(R.id.mt_ec_memo);

        btn_save=findViewById(R.id.btn_ec_save);
        btn_del=findViewById(R.id.btn_ec_delete);
        btn_back=findViewById(R.id.btn_ec_back);

        //DBヘルパーオブジェクトを生成。
        _helper=new DatabaseCalendar(EditCalendar.this);
        //インテントオブジェクトを取得。
        Intent intent=getIntent();
        //リスト画面から渡されたデータを取得。
        int sc_id=intent.getIntExtra("sc_id",-1);
        //データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得。
        SQLiteDatabase db=_helper.getWritableDatabase();
        String sqlSelect="SELECT sc_title,fsc_date,fsc_time,esc_date,esc_time,sc_memo FROM schedule" +
                " WHERE sc_id='"+sc_id+"'";
        //SQLの実行。
        Cursor cursor=db.rawQuery(sqlSelect,null);
        //データベースから取得した値を格納する変数の用意。データがなかった時のための初期値も用意。
        String note="";
        String note2="";
        String note3="";
        String note4="";
        String note5="";
        String note6="";
        //SQL実行の戻り値であるカーソルオブジェクトをループさせてデータベース内のデータを取得。
        while(cursor.moveToNext()){
            //カラムのインデックス値を取得。
            int idxNote=cursor.getColumnIndex("sc_title");
            int idxNote2=cursor.getColumnIndex("fsc_date");
            int idxNote3=cursor.getColumnIndex("fsc_time");
            int idxNote4=cursor.getColumnIndex("esc_date");
            int idxNote5=cursor.getColumnIndex("esc_time");
            int idxNote6=cursor.getColumnIndex("sc_memo");

            //カラムのインデックス値を元にデータを取得。
            note=cursor.getString(idxNote);
            note2=cursor.getString(idxNote2);
            note3=cursor.getString(idxNote3);
            note4=cursor.getString(idxNote4);
            note5=cursor.getString(idxNote5);
            note6=cursor.getString(idxNote6);
        }
        title.setText(note);
        fdate.setText(note2);
        ftime.setText(note3);
        edate.setText(note4);
        etime.setText(note5);
        memo.setText(note6);
        Log.d("memo",note6);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TextViewからテキストを取得してString型に変換
                String uptitle=title.getText().toString();
                String firstdate=fdate.getText().toString();
                String enddate=edate.getText().toString();
                String firsttime=ftime.getText().toString();
                String endtime=etime.getText().toString();
                String upmemo=memo.getText().toString();

                if(uptitle.length()!=0&&firstdate.length()!=0&&enddate.length()!=0&&firsttime.length()!=0
                &&endtime.length()!=0){
                    //データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得。
                    SQLiteDatabase db=_helper.getWritableDatabase();
                    //インサート用のSQL文字列を用意。
                    String sqlInsert="UPDATE schedule SET sc_title=?,fsc_date=?,fsc_time=?,esc_date=?,esc_time=?,sc_memo=? " +
                            "WHERE sc_id=?";
                    //SQL文字列を元にプリペアドステートメントを取得。
                    SQLiteStatement stmt=db.compileStatement(sqlInsert);
                    //変数のバインド
                    stmt.bindString(1,uptitle);
                    stmt.bindString(2,firstdate);
                    stmt.bindString(3,firsttime);
                    stmt.bindString(4,enddate);
                    stmt.bindString(5,endtime);
                    stmt.bindString(6,upmemo);
                    stmt.bindLong(7,sc_id);

                    //インサートSQLの実行。
                    stmt.execute();
                    new androidx.appcompat.app.AlertDialog.Builder(v.getContext())
                            .setTitle("成功")
                            .setMessage("データが正常に更新されました")
                            .setPositiveButton(android.R.string.ok, null)
                            .show();


                }


            }
        });

        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得。
                SQLiteDatabase db=_helper.getWritableDatabase();
                //削除用のSQL文の作成
                String delsql="DELETE FROM schedule WHERE sc_id=?";
                //SQL文字列を元にプリペアドステートメントを取得。
                SQLiteStatement stmt=db.compileStatement(delsql);
                //変数のバインド
                stmt.bindLong(1,sc_id);
                //削除SQLの実行。
                stmt.execute();
                //インテントオブジェクトを生成
                Intent intent=new Intent(EditCalendar.this,MainActivity.class);
                //第2画面の起動。
                startActivity(intent);
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //インテントオブジェクトを生成
                Intent intent=new Intent(EditCalendar.this,MainActivity.class);
                //第2画面の起動。
                startActivity(intent);
            }
        });

    }
}