package de.androidcrypto.nfchcendef1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PackageManager pm = this.getPackageManager();

        if (!pm.hasSystemFeature(PackageManager.FEATURE_NFC_HOST_CARD_EMULATION)) {
            Log.i(TAG, "Missing HCE functionality.");
        }

        Context context = this;
        Timer t = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                Date dt = Calendar.getInstance().getTime();
                Log.d(TAG, "Set time as " + dt.toString());

                TextView t = findViewById(R.id.current_time);

                if (t != null) {

                    runOnUiThread(new Runnable() {
                        public void run() {
                            //t.setText(dt.toString());
                            t.setText(getTimestamp());
                        }
                    });
                }


                if (pm.hasSystemFeature(PackageManager.FEATURE_NFC_HOST_CARD_EMULATION)) {
                    Intent intent = new Intent(context, CardService.class);
                    intent.putExtra("ndefMessage", dt.toString());
                    // Log.d(TAG, intent.toString());

                    startService(intent);
                }
            }

        };

        t.scheduleAtFixedRate(task, 0, 1000);    }

    public static String getTimestamp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return ZonedDateTime
                    .now(ZoneId.systemDefault())
                    .format(DateTimeFormatter.ofPattern("uuuu.MM.dd HH:mm:ss"));
        } else {
            return new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date());
        }
    }
}