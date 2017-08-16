package gravicodev.qash.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import gravicodev.qash.R;

public class ShowQRCodeActivity extends AppCompatActivity {
    private TextView qrcodeName, qrcodeBalance;
    private Button btnSave;
    private String name, balance, qrname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_qrcode);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_qrcode);
        toolbar.getNavigationIcon().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
        setSupportActionBar(toolbar);

        qrcodeName = (TextView) findViewById(R.id.qrcode_name);
        qrcodeBalance = (TextView) findViewById(R.id.qrcode_balance);
        btnSave = (Button) findViewById(R.id.btnSaveQR);

        String[] datas = getIntent().getExtras().getStringArray("GenerateQR");
        name = datas[0];
        balance = "Rp " + datas[1];
        qrname = "qash_" + name.trim() + "_" + datas[1].trim();

        qrcodeName.setText(name);
        qrcodeBalance.setText(balance);

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
}
