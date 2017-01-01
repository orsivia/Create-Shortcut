package yizx.createshortcut;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.content.SharedPreferences;

import yizx.createshortcut.ShortcutService;

public class ShortcutBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.i("miya", "===== shortcut: boot complete ====");
            intent = new Intent(context, ShortcutService.class);
            context.startService(intent);
        } /*else if ("com.hipad.customerkey".equals(intent.getAction())) {
            Log.i("miya", "===== customer key press ====");
            SharedPreferences sp = context.getSharedPreferences("shortcutsp", context.MODE_WORLD_WRITEABLE);
            String name = sp.getString("packageName", "");
            Log.i("miya", "packageName: " + name);
            Intent LaunchIntent = context.getPackageManager().getLaunchIntentForPackage(name);
            context.startActivity(LaunchIntent);
         else if (Intent.ACTION_CAMERA_BUTTON.equals(intent.getAction())) {
            Log.i("miya", "===== customer key long press =====");
            Intent i = new Intent(context, QuickDialSetting.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }*/
    }
}

