package com.example.androidlabs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {

    private ArrayList<MessageType> elements = new ArrayList<>();
    private MyListAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        EditText typeField = findViewById(R.id.editTextMessage);

        Button send = findViewById(R.id.buttonSend);
        send.setOnClickListener( click -> {elements.add(new MessageType(typeField.getText().toString(), true));
                                           typeField.setText("");
                                           myAdapter.notifyDataSetChanged();} );

        Button receive = findViewById(R.id.buttonReceive);
        receive.setOnClickListener( click -> {elements.add(new MessageType(typeField.getText().toString(), false));
                                              typeField.setText("");
                                              myAdapter.notifyDataSetChanged();} );


        ListView myList = findViewById(R.id.chat);
        myList.setAdapter(myAdapter = new MyListAdapter());

        myList.setOnItemLongClickListener(((parent, view, position, id) -> {
            new AlertDialog.Builder(ChatRoomActivity.this)
                    .setTitle(getString(R.string.delete_confirmation))
                    .setMessage(getString(R.string.row_info_text) + position +
                            getString(R.string.database_info_text) + id)
                    .setPositiveButton(getString(R.string.yes),(dialog, item) -> {
                        elements.remove(position);
                        myAdapter.notifyDataSetChanged();
                        })
                    .setNegativeButton(getString(R.string.no), (dialog, item) -> {
                        dialog.cancel();
                        })
                    .show();
            return true;
            }));
    }

    class MyListAdapter extends BaseAdapter{
        @Override
        public int getCount() {return elements.size();}

        @Override
        public String getItem(int position) {return elements.get(position).getMessage();}

        @Override
        public long getItemId(int position) {return (long) position;}

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View newView = inflater.inflate(R.layout.row_layout, parent, false);
            TextView theText = newView.findViewById(R.id.textRowLayout);

            if(elements.get(position).getType()){
                theText.setGravity(Gravity.START);
                theText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.row_send, 0, 0, 0);}
            else {
                theText.setGravity(Gravity.END);
                theText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.row_receive, 0);
            }

            theText.setText(getItem(position));
            return newView;
        }
    }
}
