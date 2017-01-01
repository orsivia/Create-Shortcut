package yizx.createshortcut;

import android.app.AlertDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

public class ShortcutService extends Service {

    private List<Contact> contactList;
    SharedPreferences mSharedPres;

    @Override
    public void onCreate() {
        Log.i("ShortcutService", "======== ShortcutService ========");
        super.onCreate();

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.hipad.customerkey");
        filter.addAction(Intent.ACTION_CAMERA_BUTTON);
        registerReceiver(myReceiver, filter);

        contactList = new ArrayList();
        mSharedPres = this.getSharedPreferences("shortcutsp", this.MODE_WORLD_WRITEABLE);
        addToContact();
    }

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.hipad.customerkey")) {
                Log.i("ShortcutService","===== customer key press ====");
                SharedPreferences sp = context.getSharedPreferences("shortcutsp",context.MODE_WORLD_WRITEABLE);
                String name = sp.getString("packageName","");
                Log.i("ShortcutService","===== packageName: " +name);

                if(name.equals("yizx.createshortcut") || name.equals("")){
                    Log.i("ShortcutService", "====== create dialog");
                    final AlertDialog.Builder builder = new AlertDialog.Builder(ShortcutService.this);
                    View view = LayoutInflater.from(ShortcutService.this).inflate(R.layout.quick_dial_dialog, null);
                    ListView lv3 = (ListView)view.findViewById(R.id.lv3);
                    lv3.setAdapter(new SimpleQuickDialAdapter(ShortcutService.this, contactList));
                    builder.setView(view);
                    final AlertDialog dialog = builder.create();
                    dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                    dialog.show();
                    lv3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String phoneNum = contactList.get(position).getContact_number();
                            Intent dialIntent = new Intent();
                            dialIntent.setAction(Intent.ACTION_CALL);
                            dialIntent.setData(Uri.parse("tel:" + phoneNum));
                            dialIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(dialIntent);
                            dialog.dismiss();
                        }
                    });

                }else{
                    Intent LaunchIntent = context.getPackageManager().getLaunchIntentForPackage(name);
                    context.startActivity(LaunchIntent);
                }

            }else if(Intent.ACTION_CAMERA_BUTTON.equals(intent.getAction())){
                Log.i("ShortcutService","===== customer key long press =====");
                Intent i = new Intent(context, QuickDialSetting.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }

        }

    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(myReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    public void addToContact() {
        int contactCount = mSharedPres.getInt("count", 0);

        for (int i = 1; i <= contactCount; i++) {
            String key = "contact"+i;
            Contact new_contact = (Contact)getObj(key);
            contactList.add(new_contact);
        }
    }

    public Object getObj(String key){
        Object result = null;
        String obj_base64 = mSharedPres.getString(key, "");
        byte[] base64 = Base64.decode(obj_base64, Base64.DEFAULT);
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        try{
            ObjectInputStream ois = new ObjectInputStream(bais);
            try{
                result = ois.readObject();
            }catch(ClassNotFoundException e){
                e.printStackTrace();
            }
        }catch(StreamCorruptedException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        return result;
    }


}
