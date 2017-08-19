package com.example.oluis.chatapp.Adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chat.Message;
import com.example.oluis.chatapp.R;

import java.util.List;

public class ChatAdapter extends BaseAdapter{
    private List<Message> messageList;
    private final Activity activity;

    public ChatAdapter(Activity activity, List<Message> clienteList){
        this.activity = activity;
        this.messageList = clienteList;
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

        TextView txtMsgUser = (TextView) view.findViewById(R.id.txtMensagemUser);

        txtMsgUser.setText(msg.getMessage());

        return view;
    }
}
