package com.example.oluis.chatapp.Activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chat.Message;
import com.example.oluis.chatapp.Adapter.ChatAdapter;
import com.example.oluis.chatapp.R;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText txtMsg;
    private ListView list;

    private List<Message> listMessage;
    private String IP;
    private int PORT;
    public int Id;
    private Socket socket;
    static ObjectInputStream in;
    static ObjectOutputStream out;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.INTERNET},
                0);

        Intent connectionIntent = getIntent();
        Bundle b = getIntent().getExtras();

        /*if (b == null) {
            Intent i = new Intent(this, ConnectActivity.class);
            startActivity(i);
        } else {
            IP = b.get("IP").toString();
            PORT = Integer.parseInt(b.get("PORT").toString());


        }*/
        listMessage = new ArrayList<Message>();

        Connect();
    }

    private void Connect() {

        txtMsg = (EditText) findViewById(R.id.txtMensagem);

        Connect connect = new Connect(in);
        Thread thread = new Thread(connect);
        thread.start();
    }

    public void btnSendOnClick(View v) {
        new Thread() {
            public void run() {
                Message message = new Message();

                message.setId(Id);
                message.setType(Message.MessageType.Message);
                message.setMessage(txtMsg.getText().toString());

                try
                {
                    out.writeObject(message);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void BuildListView(final List<Message> listMsg) {
        final ChatAdapter produtoAdapter = new ChatAdapter(this, listMsg);
        list = (ListView) findViewById(R.id.listChat);
        list.setAdapter(produtoAdapter);
    }

    class Connect implements Runnable {

        public Socket socket;
        public ObjectInputStream in;

        public Connect(ObjectInputStream in) {
            this.in = in;
        }

        public void run() {
            try {
                socket = new Socket("192.168.100.10", 4445);

                in = new ObjectInputStream(socket.getInputStream());
                out = new ObjectOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            Input output = new Input(in);
            Thread thread = new Thread(output);
            thread.start();
        }
    }

    class Input implements Runnable {

        public Socket socket;
        public ObjectInputStream in;

        public Input(ObjectInputStream in) {
            this.in = in;
        }

        public void run() {
            while (true) {
                try {
                    Message message = (Message) in.readObject();

                    if (message != null) {
                        System.out.println(message.getMessage());

                        if (message.getType() == Message.MessageType.GeneratedId) {
                            Id = Integer.parseInt(message.getMessage());
                        }

                        listMessage.add(message);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                BuildListView(listMessage);
                            }
                        });
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sendPic:
                Toast.makeText(this, "Enviar foto selecionado!", Toast.LENGTH_LONG).show();

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}


