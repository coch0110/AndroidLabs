package com.example.androidlabs;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;


public class DetailsFragment extends Fragment {

    private Bundle dataFromActivity;
    private long id;
    private String type;
    private AppCompatActivity parentActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFromActivity = getArguments();
        id = dataFromActivity.getLong(ChatRoomActivity.ITEM_ID );
        type = dataFromActivity.getString(ChatRoomActivity.ITEM_TYPE);

        // Inflate the layout for this fragment
        View result =  inflater.inflate(R.layout.fragment_details, container, false);

        //show the message
        TextView message = (TextView)result.findViewById(R.id.textViewFragmentMessage);
        message.setText(dataFromActivity.getString(ChatRoomActivity.ITEM_SELECTED));

        //show the id:
        TextView idView = (TextView)result.findViewById(R.id.textViewFragmentId);
        idView.setText("ID=" + id);

        CheckBox checkBox = (CheckBox)result.findViewById(R.id.checkBoxFragment);
        if(type.equals("send"))
            checkBox.setChecked(true);

        // get the delete button, and add a click listener:
        Button finishButton = (Button)result.findViewById(R.id.buttonHideFragment);
        finishButton.setOnClickListener( clk -> {

            //Tell the parent activity to remove
            parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
            if (!dataFromActivity.getBoolean("IS_PHONE"))
                getActivity().finish();
        });
        return result;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //context will either be FragmentExample for a tablet, or EmptyActivity for phone
        parentActivity = (AppCompatActivity)context;
    }
}
