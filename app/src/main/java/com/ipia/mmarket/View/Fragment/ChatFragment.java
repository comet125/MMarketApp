package com.ipia.mmarket.View.Fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ipia.mmarket.R;
import com.ipia.mmarket.View.Activity.ChatSystem;

public class ChatFragment extends Fragment {

    CardView cardView;

    public ChatFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        cardView = rootView.findViewById(R.id.card_lock);
        openChatSystem();
        return rootView;
    }



    private void openChatSystem(){

        cardView = cardView.findViewById(R.id.card_lock);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChatSystem.class);
                startActivity(intent);
            }
        });

    }
}
