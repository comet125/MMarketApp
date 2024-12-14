package com.ipia.mmarket.View.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.ipia.mmarket.R;

public class PaymentActivity extends AppCompatActivity {

    LinearLayout back_area;
    Button button;
    TextInputLayout prvi, drugi, treci, cetvrti, peti;
    TextView prikaz, transakcija;
    ImageView transakcijaSlika;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        findAndContinue();
    }

    private void findAndContinue() {
        back_area=findViewById(R.id.back_area);
        button=findViewById(R.id.purchaseBtn);
        prvi=findViewById(R.id.ime);
        drugi=findViewById(R.id.adresa);
        treci=findViewById(R.id.brojkartice);
        cetvrti=findViewById(R.id.cvcbroj);
        peti=findViewById(R.id.cc_date);
        prikaz = findViewById(R.id.payment_info_blip);
        transakcija = findViewById(R.id.completedText);
        transakcijaSlika = findViewById(R.id.completedTransaction);

        back_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayUtils.showFieldDialog(PaymentActivity.this, "Payment processed successfully");
                button.setVisibility(View.GONE);
                prvi.setVisibility(View.GONE);
                drugi.setVisibility(View.GONE);
                treci.setVisibility(View.GONE);
                cetvrti.setVisibility(View.GONE);
                peti.setVisibility(View.GONE);
                prikaz.setVisibility(View.GONE);
                transakcija.setVisibility(View.VISIBLE);
                transakcijaSlika.setVisibility(View.VISIBLE);
            }
        });
    }
}