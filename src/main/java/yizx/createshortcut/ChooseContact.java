package yizx.createshortcut;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yizx on 7/30/15.
 */
public class ChooseContact extends Activity {

    private List<Contact> contactList_ALL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choosecontact);
        contactList_ALL = new ArrayList<>();
        Button btn = (Button)findViewById(R.id.backtoquickdialsetting);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChooseContact.this, QuickDialSetting.class);
                startActivity(i);
            }
        });
        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while(cursor.moveToNext()){
            String contact_name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String contact_number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            Long contact_id = cursor.getLong(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
            Long photo_id = cursor.getLong(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID));
            Bitmap contact_photo;
            if(photo_id>0){
                Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contact_id);
                InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);
                contact_photo = BitmapFactory.decodeStream(input);
            }
            else{
                contact_photo = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
            }
            byte[] temp = getBytes(contact_photo);
            Contact new_contact = new Contact();
            new_contact.setContact_name(contact_name);
            new_contact.setContact_number(contact_number);
            new_contact.setContact_photo(temp);

            contactList_ALL.add(new_contact);
        }
        cursor.close();
        ListView lv = (ListView)findViewById(R.id.lv3);
        final ContactListAdapter cla = new ContactListAdapter(this, contactList_ALL);
        lv.setAdapter(cla);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contact temp = cla.getList().get(position);
                Intent i = new Intent(ChooseContact.this, QuickDialSetting.class);
                Bundle b = new Bundle();
                b.putSerializable("contact", temp);
                i.putExtras(b);
                finish();
                startActivity(i);
            }
        });
    }

    public static byte[] getBytes(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, baos);
        return baos.toByteArray();
    }

}
