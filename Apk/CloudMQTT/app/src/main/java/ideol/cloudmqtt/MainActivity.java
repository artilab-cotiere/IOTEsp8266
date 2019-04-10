package ideol.cloudmqtt;

import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.widget.ProgressBar;
import android.widget.TextView;

import android.provider.Settings.Secure;

import java.util.Timer;
import java.util.TimerTask;

import helpers.PublishCloudMQTT;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager sensorManager;
    Sensor light;
    float lxValue;

    String PhoneID;
    PublishCloudMQTT myPublish = new PublishCloudMQTT(this);

    TextView myText;
    ProgressBar myProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PhoneID = "sensor_android_" + Secure.getString(getContentResolver(), Secure.ANDROID_ID);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        myText = findViewById(R.id.textValue);
        myProgress = findViewById(R.id.progressValue);
        myProgress.setMax(Math.round(light.getMaximumRange()));

       /* final Handler handler = new Handler();
        Timer timer = new Timer(false);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    // Do whatever you want
                    public void run() {
                        publish(lxValue);
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask,0,5000);*/
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        lxValue = event.values[0];
        int lum = Math.round(event.values[0]);
        int percent = Math.round((event.values[0] * 100) / light.getMaximumRange());

        myProgress.setProgress(lum);
        myText.setText(String.valueOf(lum) + " lx");
        publish(lum);
    }

    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    private void publish(final float value) {
        new Thread(new Runnable() {
            public void run() {
                String Message = "{\"id\":\""+ PhoneID + "\", \"params\":{\"luminosite\":" + value + "}}";
                byte[] payload = Message.getBytes();
                myPublish.publishToTopic("sensors/" + PhoneID, payload , 0, false);
            }
        }).start();
    }
}
