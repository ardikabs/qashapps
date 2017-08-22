package gravicodev.qash.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import gravicodev.qash.Helper.FirebaseUtils;
import gravicodev.qash.Models.QMaster;
import gravicodev.qash.Models.User;
import gravicodev.qash.R;
import gravicodev.qash.Session.SessionManager;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class ShowQRCodeActivity extends BaseActivity {
    public final static int WIDTH = 1000;

    private TextView qrcodeName, qrcodeBalance;
    private Button btnSave;
    private String name, balance, qrname;
    private ImageView qrcodeView;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_qrcode);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_qrcode);
        toolbar.getNavigationIcon().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
        setSupportActionBar(toolbar);

        qrcodeName = (TextView) findViewById(R.id.qrcode_name);
        qrcodeBalance = (TextView) findViewById(R.id.qrcode_balance);
        qrcodeView = (ImageView) findViewById(R.id.qr_pict);
        btnSave = (Button) findViewById(R.id.btnSaveQR);

        Calendar today = Calendar.getInstance();
        String day = parseWaktu(Integer.toString(today.get(Calendar.DAY_OF_MONTH))) ;
        String month = parseWaktu(Integer.toString(today.get(Calendar.MONTH)+1));
        String year = parseWaktu(Integer.toString(today.get(Calendar.YEAR)));
        String now = year+month+day;


        ArrayList<String> datas = getIntent().getExtras().getStringArrayList("GenerateQR");
        name = datas.get(0);
        balance = datas.get(1);
        qrname = "qash_" + name.trim() + "_" + now;

        qrcodeName.setText(name);
        qrcodeBalance.setText(balance);

        // QRCODE BITMAP
        try {
            Bitmap bitmap = encodeAsBitmap(datas.get(2));
            qrcodeView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ShowQRCodeActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View content = findViewById(R.id.qr_pict);
                content.setDrawingCacheEnabled(true);

                Bitmap bitmap = content.getDrawingCache();
                File folder = new File(Environment.getExternalStorageDirectory(), "Qash");
                if (!folder.exists()){
                    folder.mkdirs();
                }
                File root = Environment.getExternalStorageDirectory();
                File cachePath = new File(root.getAbsolutePath() + "/Qash/" + qrname + ".jpg");
                try {
                    cachePath.createNewFile();
                    FileOutputStream outputStream = new FileOutputStream(cachePath);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Toast.makeText(getApplicationContext(), "Qash QR-Code Saved", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private String parseWaktu(String time) {
        if(time.length() == 1)
        {
            return "0"+time;
        } else {
            return time;
        }
    }


    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, WIDTH, WIDTH, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, WIDTH, 0, 0, w, h);
        return bitmap;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(ShowQRCodeActivity.this,
                            "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_main; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_qrcode, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            View content = findViewById(R.id.qr_pict);
            content.setDrawingCacheEnabled(true);

            Bitmap bitmap = content.getDrawingCache();
            File folder = new File(Environment.getExternalStorageDirectory(), "Qash");
            if (!folder.exists()){
                folder.mkdirs();
            }
            File root = Environment.getExternalStorageDirectory();
            File cachePath = new File(root.getAbsolutePath() + "/Qash/" + qrname + ".jpg");
            try {
                cachePath.createNewFile();
                FileOutputStream outputStream = new FileOutputStream(cachePath);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("image/*");
            //String shareBodyText = "QASH";
            //sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "QASH");
            //sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
            sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(cachePath));
            startActivity(Intent.createChooser(sharingIntent, "Share Qash via"));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
