package com.example.oluis.chatapp.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by oluis on 25/08/2017.
 */

public class DbWraper extends SQLiteOpenHelper{
    public static final String MENSAGEM = "Mensagem";
    public static final String MENSAGEM_ID = "_id";
    public static final String MENSAGEM_NOME = "_nome";
    public static final String MENSAGEM_DATA = "_data";
    public static final String MENSAGEM_CONTEUDO = "_conteudo";

    private static final String DATABASE_NAME = "Chat.db";
    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_CREATE = "create table " + MENSAGEM
            + "(" + MENSAGEM_ID + " integer primary key autoincrement, "
            + MENSAGEM_NOME + " text not null, "
            + MENSAGEM_DATA + " date not null, "
            + MENSAGEM_CONTEUDO + " text not null)";

    public DbWraper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + MENSAGEM);
        onCreate(db);
    }
}
