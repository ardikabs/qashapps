package gravicodev.qash.Activity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import gravicodev.qash.Adapter.ListPromotionAdapter;
import gravicodev.qash.R;

public class PromotionActivity extends AppCompatActivity {
    private static final String TAG = "PromotionActivity";
    private ListView listView;
    private ListPromotionAdapter listPromotionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_promotion);
        toolbar.getNavigationIcon().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listView = (ListView) findViewById(R.id.listPromotion);

        // Dummy data title
        String[] title = new String[]{
                "Qash Promo 1", "Qash Promo 2", "Qash Promo 3", "Qash Promo 4", "Qash Promo 5"
        };

        // Dummy data deskripsi
        String[] description = new String[]{
                "Bayar dengan qash dapat sepatu", "Bayar dengan qash dapat kaos",
                "Bayar dengan qash dapat topi", "Bayar dengan qash dapat tas",
                "Bayar dengan qash dapat handphone"
        };

        // Dummy data date
        String[] date = new String[]{
                "1 day left", "2 day left", "3 day left", "4 day left", "5 day left"
        };

        listPromotionAdapter = new ListPromotionAdapter(this, title, description, date);
        listView.setAdapter(listPromotionAdapter);
    }
}
