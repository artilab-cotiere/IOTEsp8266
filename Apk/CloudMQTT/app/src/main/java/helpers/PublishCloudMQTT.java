package helpers;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import java.util.Random;

public class PublishCloudMQTT {

    public MqttAndroidClient mqttAndroidClient;

    final String serverUri = "tcp://m24.cloudmqtt.com:15556";

    public String clientId = "AndroidClient";

    final String username = "xzrswidx";
    final String password = "YyiRI-YdJUtY";

    public PublishCloudMQTT(Context context)
    {
        Random r = new Random();
        int i1 = r.nextInt(1000);
        clientId = clientId + "_" + i1;

        mqttAndroidClient = new MqttAndroidClient(context, serverUri, clientId);
    }

    public void publishToTopic(String topic, byte[] payload, int qos, boolean retained)
    {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(false);
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());

        try {
            mqttAndroidClient.connect(mqttConnectOptions);
        } catch (MqttException e) {
            e.printStackTrace();
        }

        if (mqttAndroidClient.isConnected()) {
            try {
                mqttAndroidClient.publish(topic, payload, qos, retained);
            } catch (MqttPersistenceException e) {
                Log.w("Mqtt", "Failed to publish: " + serverUri + e.toString());
            } catch (MqttException e) {
                e.printStackTrace();
            }
            try {
                mqttAndroidClient.disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }
}
