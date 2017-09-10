package com.example.oluis.chatapp.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.chat.Message;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MensagemOperations {
    private DbWraper dbHelper;
    private String[] MENSAGEM_TABLE_COLUMNS = { DbWraper.MENSAGEM_ID,
                                                DbWraper.MENSAGEM_NOME,
                                                DbWraper.MENSAGEM_DATA,
                                                DbWraper.MENSAGEM_CONTEUDO};
    private SQLiteDatabase database;

    public MensagemOperations(Context context){
        dbHelper = new DbWraper(context);
    }

    public void open() throws SQLException{
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public Message addMensagem(Message msg){
        ContentValues values = new ContentValues();
        values.put(DbWraper.MENSAGEM_NOME, msg.getNome());
        values.put(DbWraper.MENSAGEM_CONTEUDO, msg.getMessage());

        String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(msg.getData());
        values.put(DbWraper.MENSAGEM_DATA, timeStamp);

        long mensagemId = database.insert(DbWraper.MENSAGEM, null, values);

        Cursor cursor = database.query(DbWraper.MENSAGEM,
                MENSAGEM_TABLE_COLUMNS, DbWraper.MENSAGEM_ID + " = "
                        + mensagemId, null, null, null, null );

        cursor.moveToFirst();
        Message msgNew = parseMessage(cursor);
        cursor.close();

        return msgNew;
    }

    public Message GetLast(){

        Message checkout = new Message();
        String queryString = "SELECT * FROM "+ DbWraper.MENSAGEM  + " ORDER BY "+ DbWraper.MENSAGEM_DATA +"  DESC LIMIT 1";

        Cursor cursor = database.rawQuery(queryString, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            checkout = new Message();
            checkout = parseMessage(cursor);
            cursor.moveToNext();
        }

        cursor.close();
        return checkout;
    }

    private Message parseMessage(Cursor cursor){
        Message msg = new Message();
        msg.setId(cursor.getInt(0));
        msg.setNome(cursor.getString(1));

        String data = cursor.getString(2);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = dateFormat.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        msg.setData(d);

        msg.setMessage(cursor.getString(3));

        return msg;
    }
}
