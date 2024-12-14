package com.ipia.mmarket.View.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.ipia.mmarket.R;

public class ShowItem extends AppCompatActivity {

    TextView tvTitle, tvDescription, tvPrice, tvUser;
    ImageView imageView;
    Button buyButton;
    ImageView cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_item);

        findAndContinue();

        Intent intent = getIntent();
        String itemName = intent.getStringExtra("itemName");
        String itemDescription = intent.getStringExtra("itemDescription");
        double itemPrice = intent.getDoubleExtra("itemPrice", 0);
        String itemImageUrl = intent.getStringExtra("itemImageUrl");
        String userName = intent.getStringExtra("userName");

        tvTitle.setText(itemName);
        tvDescription.setText(itemDescription);
        tvPrice.setText(String.valueOf(itemPrice) + " KM");
        tvUser.setText(userName);

        Glide.with(this)
                .load(itemImageUrl)
                .into(imageView);
    }

    private void findAndContinue() {
        tvTitle = findViewById(R.id.item_title);
        tvDescription = findViewById(R.id.item_description);
        tvPrice = findViewById(R.id.item_price);
        imageView = findViewById(R.id.item_image);
        buyButton = findViewById(R.id.buy_button);
        cancelButton = findViewById(R.id.cancel_button);
        tvUser = findViewById(R.id.item_user);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowItem.this, PaymentActivity.class);
                startActivity(intent);
            }
        });
    }
}
