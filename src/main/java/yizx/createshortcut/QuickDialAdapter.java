package yizx.createshortcut;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by yizx on 7/29/15.
 */
public class QuickDialAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private viewHolder holder;
    private Context c;
    private List<Contact> contactList;

    public QuickDialAdapter(Context context, List<Contact> cl) {
        super();
        this.c = context;
        inflater = LayoutInflater.from(c);
        this.contactList = cl;
    }

    public List<Contact> getContactList() {
        return contactList;
    }

    @Override
    public int getCount() {
        return 5;
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
    public int getItemViewType(int position) {
        //Log.i("test_size","size="+contactList.size());
        //Log.i("test_pos","position="+position);
        return contactList.size() > position ? 1 : 0;
    }

    public void exchangePos(int from ,int to){
        Contact temp = contactList.get(from);
        contactList.set(from, contactList.get(to));
        contactList.set(to, temp);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new viewHolder();
            if (getItemViewType(position) == 0) {
                convertView = inflater.inflate(R.layout.dial_list_item_empty, null);
                holder.contact_name = (TextView) convertView.findViewById(R.id.emptyprompt);
            } else {
                convertView = inflater.inflate(R.layout.dial_list_item, null);
                holder.contact_name = (TextView) convertView.findViewById(R.id.contact_name);
                holder.contact_icon = (ImageView) convertView.findViewById(R.id.contact_image);
                holder.contact_number = (TextView) convertView.findViewById(R.id.contact_number);
                holder.contact_name.setText(contactList.get(position).getContact_name());
                holder.contact_number.setText(contactList.get(position).getContact_number());
                Bitmap temp = getBitmap(contactList.get(position).getContact_photo());
                holder.contact_icon.setImageBitmap(temp);
                convertView.setTag(holder);
            }
        } else {
            holder = (viewHolder) convertView.getTag();
        }
        return convertView;
    }

    public class viewHolder {
        public ImageView contact_icon;
        public TextView contact_name;
        public TextView contact_number;
    }

    public static Bitmap getBitmap(byte[] data){
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }
}
