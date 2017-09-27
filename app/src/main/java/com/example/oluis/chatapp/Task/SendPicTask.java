package com.example.oluis.chatapp.Task;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import com.chat.Message;
import com.example.oluis.chatapp.Utils.FileUtils;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Calendar;

/**
 * Created by Usuario on 02/09/2017.
 */

public class SendPicTask extends AsyncTask<String, Void, Void>{

    private Context context;
    private ImageView imgView;
    private ProgressBar progress;
    private ObjectOutputStream out;
    private String Nome;

    public int Id;

    public SendPicTask(Context context, ObjectOutputStream out, String Nome){
        this.context = context;
        this.Nome = Nome;
    }

    @Override
    protected void onPreExecute(){
       // progress.setVisibility(View.VISIBLE);  Como acessar a progressBar daqui para setar ela como visível?
    }

   /* @Override
    protected void onPostExecute(Bitmap bitmap){  //verificar o porque do Override não sobrescrever o método
       progress.setVisibility(View.INVISIBLE);
    }*/

    @Override
    protected Void doInBackground(String... files) {
        try {
            File file = new File(files[0]);

            String msg = new String(FileUtils.readFromFile(file));

            Message message = new Message();

            message.setId(Id);
            message.setNome(Nome);
            message.setType(Message.MessageType.Photo);
            message.setMessage(msg);
            message.setData(Calendar.getInstance().getTime());

            out.writeObject(message);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
