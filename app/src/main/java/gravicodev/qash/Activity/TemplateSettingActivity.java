package gravicodev.qash.Activity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import gravicodev.qash.Adapter.ListTemplateAdapter;
import gravicodev.qash.R;

public class TemplateSettingActivity extends AppCompatActivity {
    private static final String TAG = "TemplateSettingActivity";
    private ListView listView;
    private AppCompatEditText balanceTemplate, descTemplate;
    private Button btnCreateTemplate;
    private ListTemplateAdapter listTemplateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_setting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_template);
        toolbar.getNavigationIcon().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        balanceTemplate = (AppCompatEditText) findViewById(R.id.template_balance);
        descTemplate = (AppCompatEditText) findViewById(R.id.template_ket);
        btnCreateTemplate = (Button) findViewById(R.id.btnCreateTemplate);
        listView = (ListView) findViewById(R.id.listTemplate);

        // Dummy data balance
        String[] balance = new String[]{
                "10.000", "20.000", "30.000", "40.000", "50.000"
        };

        // Dummy data deskripsi
        String[] ket = new String[]{
                "Jajan 1", "Jajan 2", "Jajan 3", "Jajan 4", "Jajan 5"
        };

        listTemplateAdapter = new ListTemplateAdapter(this, balance, ket);
        listView.setAdapter(listTemplateAdapter);
    }
}
