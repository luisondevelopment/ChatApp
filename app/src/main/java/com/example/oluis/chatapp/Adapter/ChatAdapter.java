package com.example.oluis.chatapp.Adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chat.Message;
import com.example.oluis.chatapp.Activity.MainActivity;
import com.example.oluis.chatapp.R;

import org.w3c.dom.Text;

import java.util.List;

public class ChatAdapter extends BaseAdapter {
    private List<Message> messageList;
    private final Activity activity;
    private String nome;

    public ChatAdapter(Activity activity, List<Message> clienteList, String nome){
        this.activity = activity;
        this.messageList = clienteList;
        this.nome = nome;
    }

    public void add(Message cliente){
        messageList.add(cliente);
    }

    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public Object getItem(int position) {

        return messageList.get(position);

    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = activity.getLayoutInflater().inflate(R.layout.list_msg, parent, false);
        Message msg = messageList.get(position);

        TextView txtMsgSent = (TextView) view.findViewById(R.id.txtMessageSent);
        TextView txtMsgReceived = (TextView) view.findViewById(R.id.txtMessageReceived);

        ImageView imgSent = (ImageView) view.findViewById(R.id.imgSent);
        ImageView imgReceived = (ImageView) view.findViewById(R.id.imgReceived);

        if(msg.getType() == Message.MessageType.Photo){
            Bitmap img = BitmapFactory.decodeFile(msg.getMessage());

            if(msg.getNome().equals(nome)){
                imgSent.setVisibility(View.VISIBLE);
                imgSent.setImageBitmap(img);
                imgReceived.setVisibility(View.INVISIBLE);
                txtMsgReceived.setVisibility(View.INVISIBLE);
                txtMsgSent.setVisibility(View.INVISIBLE);
            }else{
                imgReceived.setVisibility(View.VISIBLE);
                imgReceived.setImageBitmap(img);
                imgSent.setVisibility(View.INVISIBLE);
                txtMsgReceived.setVisibility(View.INVISIBLE);
                txtMsgSent.setVisibility(View.INVISIBLE);
            }
        }
        else{
            if(msg.getNome().equals(nome)) {
                txtMsgSent.setText(msg.getMessage());
                txtMsgReceived.setVisibility(View.INVISIBLE);
                imgSent.setVisibility(View.GONE);
                imgReceived.setVisibility(View.GONE);
            }
            else {
                txtMsgReceived.setText(msg.getNome() + ": \n" + msg.getMessage());
                txtMsgSent.setVisibility(View.INVISIBLE);
                imgSent.setVisibility(View.GONE);
                imgReceived.setVisibility(View.GONE);
            }
        }
        return view;
    }



}
