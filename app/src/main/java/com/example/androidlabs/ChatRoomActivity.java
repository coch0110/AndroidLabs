package com.example.androidlabs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity {

    private ArrayList<MessageType> elements = new ArrayList<>();
    private MyListAdapter myAdapter;
    SQLiteDatabase db;
    public static final String ITEM_SELECTED = "ITEM";
    public static final String ITEM_POSITION = "POSITION";
    public static final String ITEM_ID = "ID";
    public static final String ITEM_TYPE = "TYPE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        boolean isTablet = findViewById(R.id.frameLayout) != null;

        EditText typeField = findViewById(R.id.editTextMessage);
        loadDataFromDatabase();

        Button send = findViewById(R.id.buttonSend);
        send.setOnClickListener( click ->
        {
            ContentValues newRowValues = new ContentValues();
            newRowValues.put(MyOpener.COL_MESSAGE, typeField.getText().toString());
            newRowValues.put(MyOpener.COL_TYPE, "send");
            long newId = db.insert(MyOpener.TABLE_NAME, null, newRowValues);

            elements.add(new MessageType(typeField.getText().toString(), "send", newId));
                                           typeField.setText("");
                                           myAdapter.notifyDataSetChanged();} );

        Button receive = findViewById(R.id.buttonReceive);
        receive.setOnClickListener( click ->
        {
            ContentValues newRowValues = new ContentValues();
            newRowValues.put(MyOpener.COL_MESSAGE, typeField.getText().toString());
            newRowValues.put(MyOpener.COL_TYPE, "receive");
            long newId = db.insert(MyOpener.TABLE_NAME, null, newRowValues);

            elements.add(new MessageType(typeField.getText().toString(), "receive", newId));
                                              typeField.setText("");
                                              myAdapter.notifyDataSetChanged();} );


        ListView myList = findViewById(R.id.chat);
        myList.setAdapter(myAdapter = new MyListAdapter());
        myList.setOnItemClickListener((parent, view, position, id) -> {
            Bundle dataToPass = new Bundle();
            dataToPass.putString(ITEM_SELECTED, elements.get(position).getMessage());
            dataToPass.putString(ITEM_TYPE, elements.get(position).getType());
            dataToPass.putInt(ITEM_POSITION, position);
            dataToPass.putLong(ITEM_ID, id);

            if(isTablet)
            {
                DetailsFragment dFragment = new DetailsFragment(); //add a DetailFragment
                dFragment.setArguments( dataToPass ); //pass it a bundle for information
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, dFragment) //Add the fragment in FrameLayout
                        .commit(); //actually load the fragment.
            }
            else //isPhone
            {
                Intent nextActivity = new Intent(ChatRoomActivity.this, EmptyActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivity(nextActivity); //make the transition
            }
        });

        myList.setOnItemLongClickListener((parent, view, position, id) -> {
            new AlertDialog.Builder(ChatRoomActivity.this)
                    .setTitle(getString(R.string.delete_confirmation))
                    .setMessage(getString(R.string.row_info_text) + position +
                            getString(R.string.database_info_text) + id)
                    .setPositiveButton(getString(R.string.yes),(dialog, item) -> {
                        deleteMessage(elements.get(position));
                        elements.remove(position);
                        List<Fragment> fragments = getSupportFragmentManager().getFragments();
                        if (!fragments.isEmpty())
                            getSupportFragmentManager().beginTransaction().remove(fragments.get(0)).commit();
                        myAdapter.notifyDataSetChanged();
                        })
                    .setNegativeButton(getString(R.string.no), (dialog, item) -> {
                        dialog.cancel();
                        })
                    .show();
            return true;
            });
    }

    private void loadDataFromDatabase()
    {
        //get a database connection:
        MyOpener dbOpener = new MyOpener(this);
        db = dbOpener.getWritableDatabase();


        // We want to get all of the columns. Look at MyOpener.java for the definitions:
        String [] columns = {MyOpener.COL_ID, MyOpener.COL_MESSAGE, MyOpener.COL_TYPE};
        //query all the results from the database:
        Cursor results = db.query(false, MyOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        printCursor(results, db.getVersion());

        //Now the results object has rows of results that match the query.
        //find the column indices:
        int messageColIndex = results.getColumnIndex(MyOpener.COL_MESSAGE);
        int typeColIndex = results.getColumnIndex(MyOpener.COL_TYPE);
        int idColIndex = results.getColumnIndex(MyOpener.COL_ID);

        //iterate over the results, return true if there is a next item:
        while(results.moveToNext())
        {
            String message = results.getString(messageColIndex);
            String type = results.getString(typeColIndex);
            long id = results.getLong(idColIndex);

            //add the new Contact to the array list:
            elements.add(new MessageType(message, type, id));
        }

        //At this point, the contactsList array has loaded every row from the cursor.
    }

    protected void showContact(int position)
    {
        MessageType selectedContact = elements.get(position);

        View contact_view = getLayoutInflater().inflate(R.layout.row_layout, null);
        //get the TextViews
        EditText rowName = contact_view.findViewById(R.id.editTextMessage);
//        EditText rowType = contact_view.findViewById(R.id.row_email);
//        TextView rowId = contact_view.findViewById(R.id.row_id);

        //set the fields for the alert dialog
        rowName.setText(selectedContact.getMessage());
//        rowEmail.setText(selectedContact.getEmail());
//        rowId.setText("id:" + selectedContact.getId());

//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("You clicked on item #" + position)
//                .setMessage("You can update the fields and then click update to save in the database")
//                .setView(contact_view) //add the 3 edit texts showing the contact information
//                .setPositiveButton("Update", (click, b) -> {
//                    selectedContact.update(rowName.getText().toString(), rowEmail.getText().toString());
//                    updateContact(selectedContact);
//                    myAdapter.notifyDataSetChanged(); //the email and name have changed so rebuild the list
//                })
//                .setNegativeButton("Delete", (click, b) -> {
//                    deleteContact(selectedContact); //remove the contact from database
//                    contactsList.remove(position); //remove the contact from contact list
//                    myAdapter.notifyDataSetChanged(); //there is one less item so update the list
//                })
//                .setNeutralButton("dismiss", (click, b) -> { })
//                .create().show();
    }

    protected void updateMessage(MessageType c)
    {
        //Create a ContentValues object to represent a database row:
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(MyOpener.COL_MESSAGE, c.getMessage());
        updatedValues.put(MyOpener.COL_TYPE, c.getType());

        //now call the update function:
        db.update(MyOpener.TABLE_NAME, updatedValues, MyOpener.COL_ID + "= ?", new String[] {Long.toString(c.getId())});
    }

    protected void deleteMessage(MessageType c)
    {
        db.delete(MyOpener.TABLE_NAME, MyOpener.COL_ID + "= ?", new String[] {Long.toString(c.getId())});
    }


    class MyListAdapter extends BaseAdapter{
        @Override
        public int getCount() {return elements.size();}

        @Override
        public MessageType getItem(int position) {return elements.get(position);}

        @Override
        public long getItemId(int position) {return getItem(position).getId();}

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View newView = inflater.inflate(R.layout.row_layout, parent, false);
            TextView theText = newView.findViewById(R.id.textRowLayout);

            MessageType message = getItem(position);

            if(elements.get(position).getType().equals("send")){
                theText.setGravity(Gravity.START);
                theText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.row_send, 0, 0, 0);
            } else {
                theText.setGravity(Gravity.END);
                theText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.row_receive, 0);
            }

//            theText.setText(getItem(position));
            theText.setText(message.getMessage());

            return newView;
        }
    }

    public void printCursor( Cursor c, int version){
        Log.d("DB_DATA", "The database version is: " + version);
        Log.d("DB_DATA", "The number of columns in the database is: " + c.getColumnCount());
        Log.d("DB_DATA", "The names of the columns are: " + Arrays.toString(c.getColumnNames()));
        Log.d("DB_DATA", "The total of rows in the table is: " + c.getCount());
        Log.d("DB_DATA", DatabaseUtils.dumpCursorToString(c));
    }
}
