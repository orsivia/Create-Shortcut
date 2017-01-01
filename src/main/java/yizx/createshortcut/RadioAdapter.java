package yizx.createshortcut;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.util.Log;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class RadioAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<Map<String, Object>> itemList;
    private viewHolder holder;
    private int index = -1;
    private Context c;
    private String mRadioName;
    SharedPreferences mShortcutSP ;

    public RadioAdapter(Context c, List<Map<String, Object>> itemList) {
        super();
        this.c = c;
        this.itemList = itemList;
        inflater = LayoutInflater.from(c);
        mShortcutSP = c.getSharedPreferences("shortcutsp",c.MODE_WORLD_WRITEABLE);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        holder = new viewHolder();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.simple_list_item, null);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            holder.selectBtn = (RadioButton) convertView.findViewById(R.id.radiobtn);
            convertView.setTag(holder);
        } else {
            holder = (viewHolder) convertView.getTag();
        }

        mRadioName = mShortcutSP.getString("packageName","");

        //if(radioName.equals((String)itemList.get(position).get("appName"))){
        //   holder.selectBtn.setChecked(true);
        //}

        holder.name.setText((String)itemList.get(position).get("appName"));
        holder.icon.setImageDrawable((Drawable)itemList.get(position).get("appIcon"));
        holder.selectBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    index = position;
                    notifyDataSetChanged();

                    SharedPreferences.Editor edit = mShortcutSP.edit();
                    edit.putString("packageName",(String)itemList.get(position).get("packageName"));
                    edit.commit();
                }
            }
        });

        if (index == position) {
            holder.selectBtn.setChecked(true);
        } else {
            holder.selectBtn.setChecked(false);
        }

        if(mRadioName.equals((String)itemList.get(position).get("packageName"))){
            holder.selectBtn.setChecked(true);
        }

        return convertView;
    }

    public class viewHolder {
        public ImageView icon;
        public TextView name;
        public RadioButton selectBtn;
    }
}
