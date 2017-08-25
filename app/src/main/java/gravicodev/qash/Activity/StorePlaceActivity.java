package gravicodev.qash.Activity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import gravicodev.qash.Adapter.ListStoreAdapter;
import gravicodev.qash.R;

public class StorePlaceActivity extends AppCompatActivity {
    private static final String TAG = "StorePlaceActivity";
    private ListView listView;
    private ListStoreAdapter listStoreAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_place);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_store);
        toolbar.getNavigationIcon().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listView = (ListView) findViewById(R.id.listStore);

        // Dummy data title
        String[] title = new String[]{
                "Store 1", "Store 2", "Store 3", "Store 4", "Store 5"
        };

        // Dummy data address
        String[] address = new String[]{
                "Jl. Mana", "Jl. Mini", "Jl. Munu", "Jl. Mene", "Jl. Mono"
        };

        // Dummy data distance
        String[] distance = new String[]{
                "1 KM", "2 KM", "3 KM", "4 KM", "5 KM"
        };

        listStoreAdapter = new ListStoreAdapter(this, title, address, distance);
        listView.setAdapter(listStoreAdapter);
    }
}
