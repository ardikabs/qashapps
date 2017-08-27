package gravicodev.qash.Activity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import gravicodev.qash.Adapter.ListAccStatementAdapter;
import gravicodev.qash.Helper.VolleyCallback;
import gravicodev.qash.Models.QMaster;
import gravicodev.qash.R;
import gravicodev.qash.Session.SessionManager;
import gravicodev.qash.Volley.VolleyHelper;

public class AccStatementActivity extends AppCompatActivity {
    private static final String TAG = "AccStatementActivity";
    private ListView listView;
    private TextView period, startBalance, endBalance, qashCreditMutation, qashDebetMutation,
            creditMutation, debetMutation;
    private ListAccStatementAdapter listAccStatementAdapter;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accstatement);

        sessionManager = new SessionManager(this);

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

//        listAccStatementAdapter = new ListAccStatementAdapter(this, name, trailer, amount, type, date, branch);
        List<HashMap<String,String>> emptyList = new ArrayList<>();
        listAccStatementAdapter = new ListAccStatementAdapter(this,emptyList);
        listView.setAdapter(listAccStatementAdapter);

        VolleyHelper vh = new VolleyHelper();
        try {
            vh.getStatement(new VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    HashMap<String,String> dataFromAPI = gson.fromJson(result,HashMap.class);
                    String[] tempTime = dataFromAPI.get("StartDate").split("-");
                    String startdate = tempTime[2]+"-"+tempTime[1]+"-"+tempTime[0];
                    tempTime = dataFromAPI.get("EndDate").split("-");
                    String enddate = tempTime[2]+"-"+tempTime[1]+"-"+tempTime[0];;
                    String firstBalance = dataFromAPI.get("StartBalance");
                    int credit = 0;
                    int debit =0;
                    int creditQ = 0;
                    int debitQ=0;

                    JSONObject obj = null;
                    try {
                        obj = new JSONObject(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JSONArray data = null;
                    try {
                        data = obj.getJSONArray("Data");
                        for(int i = 0; i<data.length();i++){
//                            name, trailer, amount, type, date
                            HashMap<String,String> mydata = new HashMap<>();
                            String trailer = data.getJSONObject(i).getString("Trailer");
                            String amount = data.getJSONObject(i).getString("TransactionAmount");
                            String trxName = data.getJSONObject(i).getString("TransactionName");
                            String date = data.getJSONObject(i).getString("TransactionDate");
                            String type = data.getJSONObject(i).getString("TransactionType");
                            if(trailer.indexOf("Pay By Qash") >=0){
                                if(data.getJSONObject(i).getString("TransactionType").equalsIgnoreCase("C")){
                                    creditQ += Integer.parseInt(amount.split("\\.")[0]);
                                    type = "Q"+type;
                                }
                                else{
                                    debitQ += Integer.parseInt(amount.split("\\.")[0]);
                                    type = "Q"+type;
                                }
                            }
                            if(data.getJSONObject(i).getString("TransactionType").equalsIgnoreCase("C")){
                                credit += Integer.parseInt(amount.split("\\.")[0]);
                            }
                            else{
                                debit += Integer.parseInt(amount.split("\\.")[0]);
                            }
                            mydata.put("amount",amount.split("\\.")[0]);
                            mydata.put("name",trxName);
                            mydata.put("date",date);
                            mydata.put("type",type);
                            mydata.put("trailer",trailer);
                            listAccStatementAdapter.refill(mydata);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    int endbalance = Integer.parseInt(firstBalance.split("\\.")[0]) + credit - debit;
                    period.setText(startdate +" - "+enddate);
                    startBalance.setText("Rp " + moneyParserString(firstBalance.split("\\.")[0]));
                    endBalance.setText("Rp " + moneyParserString(String.valueOf(endbalance)));
                    qashCreditMutation.setText("Rp " + moneyParserString(String.valueOf(creditQ)));
                    qashDebetMutation.setText("Rp " + moneyParserString(String.valueOf(debitQ)));
                    creditMutation.setText("Rp " + moneyParserString(String.valueOf(credit)));
                    debetMutation.setText("Rp " + moneyParserString(String.valueOf(debit)));
                }
            },sessionManager.getUser().accountNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
