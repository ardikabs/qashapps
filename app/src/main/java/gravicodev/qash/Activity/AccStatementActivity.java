package gravicodev.qash.Activity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import gravicodev.qash.Adapter.ListAccStatementAdapter;
import gravicodev.qash.R;

public class AccStatementActivity extends AppCompatActivity {
    private static final String TAG = "AccStatementActivity";
    private ListView listView;
    private TextView period, startBalance, endBalance, qashCreditMutation, qashDebetMutation,
            creditMutation, debetMutation;
    private ListAccStatementAdapter listAccStatementAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accstatement);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_AccStatement);
        toolbar.getNavigationIcon().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        period = (TextView) findViewById(R.id.periodValue);
        startBalance = (TextView) findViewById(R.id.startBalanceValue);
        endBalance = (TextView) findViewById(R.id.endBalanceValue);
        qashCreditMutation = (TextView) findViewById(R.id.qashCreditAccStatementValue);
        qashDebetMutation = (TextView) findViewById(R.id.qashDebetAccStatementValue);
        creditMutation = (TextView) findViewById(R.id.creditAccStatementValue);
        debetMutation = (TextView) findViewById(R.id.debetAccStatementValue);

        // Dummy mutation
        period.setText("29 Jan 2017 - 01 Feb 2017");
        startBalance.setText("Rp " + moneyParserString("1000000"));
        endBalance.setText("Rp " + moneyParserString("2400000"));
        qashCreditMutation.setText("Rp " + moneyParserString("500000"));
        qashDebetMutation.setText("Rp " + moneyParserString("100000"));
        creditMutation.setText("Rp " + moneyParserString("1000000"));
        debetMutation.setText("Rp " + moneyParserString("0"));

        listView = (ListView) findViewById(R.id.listAccStatement);

        // Dummy data date
        String[] date = new String[]{
                "29 January 2017", "01 February 2017", "01 February 2017"
        };

        // Dummy data name
        String[] name = new String[]{
                "BA JASA E-BANKING", "Gajian", "Bayar Makan"
        };

        // Dummy data trailer
        String[] trailer = new String[]{
                "3001/TRCHG/WS95051BIAYA TRANSFER SME", "Accepted with Qash", "Pay with Qash"
        };

        // Dummy data amount
        String[] amount = new String[]{
                "1000000", "500000", "100000"
        };

        // Dummy data type
        String[] type = new String[]{
                "C", "QC", "QD"
        };

        // Dummy data branch
        String[] branch = new String[]{
                "0000", "Qash", "Qash"
        };

        listAccStatementAdapter = new ListAccStatementAdapter(this, name, trailer, amount, type, date, branch);
        listView.setAdapter(listAccStatementAdapter);
    }

    public String moneyParserString(String data){
        ArrayList<String> input = new ArrayList<>();
        for(int i = data.length()-1;i>=0;i--){
            if(!".".equals(String.valueOf(data.charAt(i)))){
                input.add(String.valueOf(data.charAt(i)));
            }
        }

        String strHasil = "";
        int x = 1;
        for(int i=0; i < input.size();i++){
            if(x==3 && i != (input.size()-1)){
                strHasil = "." + input.get(i) + strHasil;
                x = 0;
            }else{
                strHasil = input.get(i) + strHasil;
            }
            x++;
        }

        return strHasil;
    }
}
