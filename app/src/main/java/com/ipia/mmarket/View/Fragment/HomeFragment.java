package com.ipia.mmarket.View.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ipia.mmarket.Models.Item;
import com.ipia.mmarket.View.Activity.AddItem;
import com.ipia.mmarket.View.Activity.ShowItem;
import com.ipia.mmarket.View.Adapters.ShopAdapter;
import com.ipia.mmarket.R;

import java.util.ArrayList;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private FirebaseAuth auth;
    private DatabaseReference userDb, itemDb;
    private TextView welcomeText;
    private RecyclerView recyclerView;
    private ShopAdapter shopAdapter;
    private ArrayList<Item> itemList;
    private ImageView accIcon, searchToggle, searchCancel, searchGo;
    private TextInputEditText itemSearch;

    public HomeFragment() {

    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        searchAnim(rootView);

        ExtendedFloatingActionButton fab = rootView.findViewById(R.id.fab);
        welcomeText = rootView.findViewById(R.id.welcome_text);
        recyclerView = rootView.findViewById(R.id.recyclerview);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddItem.class));
                getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });

        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            userDb = FirebaseDatabase.getInstance("https://mmarket-ipia-rma2-default-rtdb.europe-west1.firebasedatabase.app")
                    .getReference("Users")
                    .child(userId);

            userDb.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String userName = snapshot.child("name").getValue(String.class);
                        welcomeText.setText("Welcome, " + userName);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            welcomeText.setText("Welcome, Guest");
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        itemList = new ArrayList<>();
        shopAdapter = new ShopAdapter(getContext(), itemList);
        recyclerView.setAdapter(shopAdapter);

        itemDb = FirebaseDatabase.getInstance("https://mmarket-ipia-rma2-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("Items");

        itemDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Item item = dataSnapshot.getValue(Item.class);
                    itemList.add(item);
                }
                shopAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        searchGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = Objects.requireNonNull(itemSearch.getText()).toString().trim();
                searchItems(query);
                itemSearch.getText().clear();
            }
        });

        shopAdapter.setOnItemClickListener(new ShopAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Item item) {
                Intent intent = new Intent(getActivity(), ShowItem.class);
                intent.putExtra("itemName", item.getItemName());
                intent.putExtra("itemDescription", item.getItemDescription());
                intent.putExtra("itemPrice", item.getItemPrice());
                intent.putExtra("itemImageUrl", item.getImageUrl());
                intent.putExtra("userName", item.getUserName());
                startActivity(intent);
            }
        });

        return rootView;
    }

    private void searchAnim(View rootView) {
        accIcon = rootView.findViewById(R.id.accIcon);
        searchToggle = rootView.findViewById(R.id.search_toggle);
        searchCancel = rootView.findViewById(R.id.search_cancel);
        itemSearch = rootView.findViewById(R.id.item_search);
        welcomeText = rootView.findViewById(R.id.welcome_text);
        searchGo = rootView.findViewById(R.id.search_go);

        searchToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accIcon.setVisibility(View.GONE);
                welcomeText.setVisibility(View.GONE);
                searchToggle.setVisibility(View.GONE);

                itemSearch.setVisibility(View.VISIBLE);
                searchCancel.setVisibility(View.VISIBLE);
                searchGo.setVisibility(View.VISIBLE);
            }
        });

        searchCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accIcon.setVisibility(View.VISIBLE);
                welcomeText.setVisibility(View.VISIBLE);
                searchToggle.setVisibility(View.VISIBLE);
                itemSearch.setVisibility(View.GONE);
                searchCancel.setVisibility(View.GONE);
                searchGo.setVisibility(View.GONE);

                shopAdapter.updateItemList(itemList);
            }
        });
    }

    private void searchItems(String query) {
        ArrayList<Item> filteredList = new ArrayList<>();
        for (Item item : itemList) {
            if (item.getItemName().toLowerCase().contains(query.toLowerCase()) ||
                    item.getItemDescription().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }
        shopAdapter.updateItemList(filteredList);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}


