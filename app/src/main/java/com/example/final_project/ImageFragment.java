package com.example.final_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class ImageFragment extends Fragment {
    KittensEntity selected;

    public ImageFragment(KittensEntity m) {
        selected = m;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

//        DetailsLayoutBinding binding = DetailsLayoutBinding.inflate(inflater);
//        binding.timeText.setText(selected.message);
//        binding.messageText.setText(selected.timeSent);
//        binding.sendRecieveText.setText( (selected.sendOrReceive == 1) ? "send" : "receive" );
//        //determines left side or right side of text
//        binding.databaseText.setText("Id = " + selected.id);
//        return binding.getRoot();
return container;
    }


}
