package com.websarva.wings.android.calendar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class TodoAdd extends AppCompatActivity {
    //選択されたTODOの主キーを表すフィールド
    private int _todoId = -1;
    //選択されたタイトルを表すフィールド
    private String _todoTitle = "";

    //データベースヘルパーオブジェクト
    private DatabaseCalendar _helper;

    Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_add);

        //Todo用ListView(lvTodo)を取得
        ListView lv_todo = findViewById(R.id.lv_todo);
        //lvTodoにリスナを登録
        lv_todo.setOnItemClickListener(new ListItemClickListener());

        //DBヘルパーオブジェクトを生成
        _helper = new DatabaseCalendar(TodoAdd.this);

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
        while(cursor.moveToNext()) {
            //カラムのインデックス値を取得。
            int idxTitle = cursor.getColumnIndex("todo_title");
            int idxDate = cursor.getColumnIndex("todo_date");
            int idxTime = cursor.getColumnIndex("todo_time");
            int idxMemo = cursor.getColumnIndex("todo_memo");
            //カラムのインデックス値を元にデータを取得。
            dbtitle = cursor.getString(idxTitle);
            dbdate = cursor.getString(idxDate);
            dbtime = cursor.getString(idxTime);
            dbmemo = cursor.getString(idxMemo);

            String result = dbdate + "   " + dbtime + "   " + dbtitle + "   " + dbmemo;
            resultList.add(result);
        }
        ArrayAdapter<String> arrayAdapter=
                new ArrayAdapter<String>(TodoAdd.this, android.R.layout.simple_list_item_1,resultList);
        lv_todo.setAdapter(arrayAdapter);


        btn_back=findViewById(R.id.todo_back);


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //インテントオブジェクトを生成
                Intent intent=new Intent(TodoAdd.this,Todohome.class);
                //第2画面の起動。
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy(){
        //DBヘルパーオブジェクトの解放
        _helper.close();
        super.onDestroy();
    }


    //保存ボタンがタップされたときの処理メソッド
    public void onAddButtonClick(View view){
        //タイトル欄を取得
        EditText et_todo_title = findViewById(R.id.et_todo_title);
        String todo_title = et_todo_title.getText().toString();
        Log.i("Todoadd", "title: " + todo_title);
        //タイトル欄の入力値を消去
        et_todo_title.setText("");
        //日付欄を取得
        EditText et_todo_date = findViewById(R.id.et_todo_date);
        String todo_date = et_todo_date.getText().toString();
        Log.i("Todoadd", "date: " + todo_date);
        //日付欄の入力値を消去
        et_todo_date.setText("");
        //時間欄を取得
        EditText et_todo_time = findViewById(R.id.et_todo_time);
        String todo_time = et_todo_time.getText().toString();
        Log.i("Todoadd", "time: " + todo_time);
        //時間欄の入力値を消去
        et_todo_time.setText("");
        //メモ欄を取得
        EditText et_todo_memo = findViewById(R.id.et_todo_memo);
        String todo_memo = et_todo_memo.getText().toString();
        Log.i("Todoadd", "memo: " + todo_memo);

        if(todo_title.length()!=0&&todo_date.length()!=0&&todo_time.length()!=0&&todo_memo.length()!=0
        &&todo_title.length()<=15&&todo_memo.length()<=50) {

            //データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得
            SQLiteDatabase db = _helper.getWritableDatabase();

            //インサート用SQL文字列の用意
            String sqlInsert = "INSERT INTO todolist (todo_title, todo_date, todo_time, todo_memo) VALUES (?, ?, ?, ?)";
            //SQL文字列を元にプリペアドステートメントを取得
            SQLiteStatement stmt = db.compileStatement(sqlInsert);
            //変数のバインド
            stmt.bindString(1, todo_title);
            stmt.bindString(2, todo_date);
            stmt.bindString(3, todo_time);
            stmt.bindString(4, todo_memo);

            //インサートSQLの実行
            long rowId = stmt.executeInsert();
            Log.i("Todoadd", "Inserted row ID: " + rowId);
            //メモ欄の入力値を消去
            et_todo_memo.setText("");
            new androidx.appcompat.app.AlertDialog.Builder(view.getContext())
                    .setTitle("成功")
                    .setMessage("データが正常に挿入されました")
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
        }
    }


    private class ListItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    }
}