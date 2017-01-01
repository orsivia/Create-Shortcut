package yizx.createshortcut;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yizx on 8/6/15.
 */
public class SimpleQuickDialAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private viewHolder holder;
    private Context c;
    private List<Contact> contactList;

    public SimpleQuickDialAdapter(Context context, List<Contact> list) {
        super();
        this.c = context;
        inflater = LayoutInflater.from(c);
        this.contactList = list;
        Log.i("test", "size="+contactList.size());
    }

    @Override
    public int getCount() {
        return contactList.size();
    }

    @Override
    public Object getItem(int position) {
        return contactList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new viewHolder();
            convertView = inflater.inflate(R.layout.simple_dial_list_item, null);
            holder.contact_name = (TextView)convertView.findViewById(R.id.contact_name_simple);
            holder.contact_icon = (ImageView)convertView.findViewById(R.id.contact_image_simple);
            holder.dial_icon = (ImageView)convertView.findViewById(R.id.dialing);
            holder.contact_name.setText(contactList.get(position).getContact_name());
            Bitmap temp = getBitmap(contactList.get(position).getContact_photo());
            holder.contact_icon.setImageBitmap(temp);
            //Drawable temp0 = Drawable.createFromPath("/Users/yizx/AndroidStudioProjects/CreateShortcut/app/src/main/res/drawable/dialing.png");
            holder.dial_icon.setImageResource(R.drawable.dialing);
        } else {
            holder = (viewHolder) convertView.getTag();
        }
        return convertView;
    }

    public class viewHolder {
        public ImageView contact_icon;
        public TextView contact_name;
        public ImageView dial_icon;
    }

    public static Bitmap getBitmap(byte[] data){
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }
}
