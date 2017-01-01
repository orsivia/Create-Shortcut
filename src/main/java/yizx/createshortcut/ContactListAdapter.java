package yizx.createshortcut;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by yizx on 7/30/15.
 */
public class ContactListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private viewHolder holder;
    private Context c;
    private List<Contact> list;

    public ContactListAdapter(Context context, List<Contact> list){
        this.c = context;
        this.inflater = LayoutInflater.from(c);
        this.list = list;
    }

    public List<Contact> getList() {
        return list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            holder = new viewHolder();
            convertView = inflater.inflate(R.layout.dial_list_item, null);
            holder.contact_name = (TextView) convertView.findViewById(R.id.contact_name);
            holder.contact_icon = (ImageView) convertView.findViewById(R.id.contact_image);
            holder.contact_number = (TextView) convertView.findViewById(R.id.contact_number);
            holder.contact_name.setText(list.get(position).getContact_name());
            holder.contact_number.setText(list.get(position).getContact_number());
            Bitmap temp = getBitmap(list.get(position).getContact_photo());
            holder.contact_icon.setImageBitmap(temp);
            convertView.setTag(holder);
        }
        else{
            holder = (viewHolder)convertView.getTag();
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
