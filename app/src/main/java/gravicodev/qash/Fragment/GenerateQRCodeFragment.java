package gravicodev.qash.Fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.security.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import gravicodev.qash.Helper.FirebaseUtils;
import gravicodev.qash.Models.QMaster;
import gravicodev.qash.R;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

/**
 * Created by Rasyadh A Aziz on 28/07/2017.
 */

public class GenerateQRCodeFragment extends Fragment {
    private static final String TAG = "GenerateQRCodeFragment";
    public final static int WIDTH=500;
    private Button pButton;
    private Button dateButton;


    public GenerateQRCodeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_generateqrcode, container, false);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.qrCodeIV);
        try {
            Bitmap bitmap = encodeAsBitmap("471e7f048fb27452c82a10204cd460b2c51e96d994a2bc9ca643d0d8ccff4d24");
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        pButton = (Button) rootView.findViewById(R.id.pushbutton);

        pButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE,7);
                String key = "QRCODE_1";
                Integer balance = 150000;
                String status = "active";
                Long date = cal.getTime().getTime();
                String title = "TITLE QRCODE";
                String san = "SourceAccountNumber (SOURCE)";

                QMaster qMaster = new QMaster(balance,status,date,title,san);
                FirebaseUtils.getBaseRef().child("qmaster").child(key).setValue(qMaster);

                HashMap<String, Boolean> qrvalue = new HashMap<>();
                qrvalue.put(key,true);
                FirebaseUtils.getBaseRef().child("qcreator").child("user_1").setValue(qrvalue);
            }
        });
        return rootView;
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



    public void showToast(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

}
