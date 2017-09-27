package com.example.oluis.chatapp.Activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chat.Message;
import com.example.oluis.chatapp.Adapter.ChatAdapter;
import com.example.oluis.chatapp.Data.MensagemOperations;
import com.example.oluis.chatapp.R;
import com.example.oluis.chatapp.Task.SendPicTask;
import com.example.oluis.chatapp.Utils.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText txtMsg;
    private ListView list;

    private List<Message> listMessage;
    private String IP;
    private int PORT;
    public String LOGIN;
    public int Id;
    private Socket socket;
    static ObjectInputStream in;
    static ObjectOutputStream out;

    private MensagemOperations msgOperations;
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

        LOGIN = b.get("LOGIN").toString();

        /*if (b == null) {
            Intent i = new Intent(this, ConnectActivity.class);
            startActivity(i);
        } else {
            IP = b.get("IP").toString();
            PORT = Integer.parseInt(b.get("PORT").toString());


        }*/
        listMessage = new ArrayList<Message>();

        msgOperations = new MensagemOperations(this);
        msgOperations.open();

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
                message.setNome(LOGIN);
                message.setType(Message.MessageType.Message);
                message.setMessage(txtMsg.getText().toString());
                message.setData(Calendar.getInstance().getTime());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtMsg.setText("");
                    }
                });

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

    public void BuildListView() {
        List<Message> listMessage = msgOperations.GetAll();

        final ChatAdapter produtoAdapter = new ChatAdapter(this, listMessage);
        list = (ListView) findViewById(R.id.listChat);
        list.setAdapter(produtoAdapter);

        list.setSelection(listMessage.size());
    }

    class Connect implements Runnable {

        public Socket socket;
        public ObjectInputStream in;

        public Connect(ObjectInputStream in) {
            this.in = in;
        }

        public void run() {
            try {
                socket = new Socket("10.0.2.2", 4445);

                in = new ObjectInputStream(socket.getInputStream());
                out = new ObjectOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            Input input = new Input(in);
            Thread thread = new Thread(input);
            thread.start();

            Sincronizar();
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

                        if(message.getType() == Message.MessageType.Photo){

                        }

                        msgOperations.addMensagem(message);

                        listMessage.add(message);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                BuildListView();
                            }
                        });
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void Sincronizar(){
        Message message = msgOperations.GetLast();
        message.setId(Id);
        message.setNome(LOGIN);
        message.setType(Message.MessageType.Sync);
        message.setMessage(txtMsg.getText().toString());
        message.setData(Calendar.getInstance().getTime());
        try
        {
            out.writeObject(message);
        }
        catch (IOException e)
        {
            e.printStackTrace();
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
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    try {
                        File photoFile = FileUtils.createFile(this, ".jpg");
                        mCurrentPhotoPath = photoFile.getAbsolutePath();

                        Uri photUri = FileProvider.getUriForFile(this, "com.example.oluis.chatapp.fileprovider", photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photUri);
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
           // Bitmap imgBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);

            File file = new File(mCurrentPhotoPath);
            final String content;
            try {
                Message message = new Message();

                message.setId(Id);
                message.setNome(LOGIN);
                message.setType(Message.MessageType.Photo);
                message.setFoto(FileUtils.readFromFile(file));
                message.setData(Calendar.getInstance().getTime());

                final Message msg = message;

                new Thread() {
                    public void run() {
                        try
                        {
                            out.writeObject(msg);
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


