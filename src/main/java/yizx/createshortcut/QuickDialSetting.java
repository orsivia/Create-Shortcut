package yizx.createshortcut;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.content.SharedPreferences;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yizx on 7/29/15.
 */
public class QuickDialSetting extends Activity {

    private List<Contact> contactList = new ArrayList();
    SharedPreferences mSharedPres;
    SharedPreferences.Editor mEdit;
    QuickDialAdapter mAdapter;
    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quickdialsetting);

        mSharedPres = this.getSharedPreferences("shortcutsp", this.MODE_WORLD_WRITEABLE);
        mEdit = mSharedPres.edit();

        Button back_to_smart_setting = (Button) findViewById(R.id.back_to_smart_setting);
        back_to_smart_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(QuickDialSetting.this, ChooseApp.class);
                startActivity(i);
            }
        });

        mAdapter = new QuickDialAdapter(this, contactList);
        ListView lv2 = (ListView) findViewById(R.id.lv2);
        lv2.setAdapter(mAdapter);

        mode = mSharedPres.getInt("mode", 0);
        Log.i("test","mode="+mode);
        switch (mode){
            case 1:
                Intent grab_data = getIntent();
                Contact get_contact = (Contact) grab_data.getSerializableExtra("contact");
                if (get_contact != null) {
                    storeContact(get_contact);
                }
                addToContact();
                mAdapter.notifyDataSetChanged();
                break;
            case 2:
                Intent update_data = getIntent();
                Contact update_contact = (Contact) update_data.getSerializableExtra("contact");
                if (update_contact != null) {
                    updateContact(update_contact);
                }
                addToContact();
                mAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }

        lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (contactList.size() <= position) {
                    mEdit.putInt("mode", 1);
                    mEdit.commit();
                    Intent i = new Intent(QuickDialSetting.this, ChooseContact.class);
                    startActivity(i);
                    finish();
                } else {
                    mEdit.putInt("mode", 2);
                    mEdit.putInt("pos", position+1);
                    mEdit.commit();
                    Intent i = new Intent(QuickDialSetting.this, ChooseContact.class);
                    startActivity(i);
                    finish();
                }
            }
        });

    }

    public void storeContact(Contact contact) {
        Log.i("test","====STORE====");
        int contactCount = mSharedPres.getInt("count", 0);

        switch (contactCount) {
            case 0:
                storeObj(contact, "contact1");
                mEdit.putInt("count", 1);
                mEdit.commit();
                break;
            case 1:
                storeObj(contact, "contact2");
                mEdit.putInt("count", 2);
                mEdit.commit();
                break;
            case 2:
                storeObj(contact, "contact3");
                mEdit.putInt("count", 3);
                mEdit.commit();
                break;
            case 3:
                storeObj(contact, "contact4");
                mEdit.putInt("count", 4);
                mEdit.commit();
                break;
            case 4:
                storeObj(contact, "contact5");
                mEdit.putInt("count", 5);
                mEdit.commit();
                break;
            default:
                break;
        }

    }

    public void addToContact() {
        int contactCount = mSharedPres.getInt("count", 0);

        for (int i = 1; i <= contactCount; i++) {
            String key = "contact"+i;
            Contact new_contact = (Contact)getObj(key);
            contactList.add(new_contact);
        }
    }

    public void updateContact(Object object){
        int pos = mSharedPres.getInt("pos", -1);
        Log.i("test","====UPDATE====");
        Log.i("test","pos="+pos);
        String key = "contact"+pos;
        storeObj(object, key);
    }

    public void storeObj(Object obj, String key) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            String obj_base64 = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            mEdit.putString(key, obj_base64);
            mEdit.commit();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (oos != null) {
                    oos.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
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


