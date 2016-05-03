package ng.softworks.unorthodox.medstrackr.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by unorthodox on 02/05/16.
 * Alarm service to be used to initiate the message shown to user when it is time to use a
 * particular drug
 */
public class AlarmReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent){
        Toast.makeText(context, "Alarm Started", Toast.LENGTH_LONG).show();
    }
}
