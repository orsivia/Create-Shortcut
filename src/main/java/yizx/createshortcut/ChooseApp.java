package yizx.createshortcut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;

public class ChooseApp extends Activity {

    private List<AppInfo> allApps = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chooseapp);
        RadioButton radioBtn = (RadioButton)findViewById(R.id.radiobtn);
        //allApps = getAllApps();
        getAppInfo();
        final List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for(int i=0; i<allApps.size(); i++){
            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("appName", allApps.get(i).name);
            listItem.put("appIcon", allApps.get(i).icon);
            listItem.put("appChsn", radioBtn);
            listItem.put("packageName",allApps.get(i).packageName);
            listItems.add(listItem);
        }

        RadioAdapter ra = new RadioAdapter(this, listItems);
        ListView lv = (ListView)findViewById(R.id.lv);
        lv.setAdapter(ra);

        Button back = (Button)findViewById(R.id.backtosetting);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    class AppInfo{
        private String name;
        private Drawable icon;
        private String packageName;
    }

    public void getAppInfo(){
        PackageManager pm = this.getPackageManager();
        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> infoList = pm.queryIntentActivities(i, PackageManager.GET_INTENT_FILTERS);
        Collections.sort(infoList, new ResolveInfo.DisplayNameComparator(pm));
        for(ResolveInfo itemInfo : infoList){
            AppInfo new_item = new AppInfo();
            new_item.name = (String)itemInfo.loadLabel(pm);
            new_item.icon = itemInfo.loadIcon(pm);
            new_item.packageName = itemInfo.activityInfo.packageName;
            allApps.add(new_item);
        }
    }

    /*public static List<ApplicationInfo> getAllApplications(Context context){
        List<ApplicationInfo> result = new ArrayList<ApplicationInfo>();
        PackageManager pm = context.getPackageManager();

        List<ApplicationInfo> applist = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        Collections.sort(applist, new ApplicationInfo.DisplayNameComparator(pm));
        for(int i=0; i<applist.size(); i++){
            ApplicationInfo pi = (ApplicationInfo)applist.get(i);
            if((pi.flags & ApplicationInfo.FLAG_SYSTEM)!= 0)
                result.add(pi);
        }
        return result;
    }

    public List<AppInfo> getAllApps(){
        List<AppInfo> apps = new ArrayList<AppInfo>();
        PackageManager pm = ChooseApp.this.getPackageManager();
        List<ApplicationInfo> appList = getAllApplications(ChooseApp.this);

        for(int i=0; i<appList.size(); i++){
            ApplicationInfo pi = appList.get(i);
            AppInfo ai = new AppInfo();
            ai.name = (String)pi.loadLabel(pm);
            ai.icon = pi.loadIcon(pm);
            //Drawable temp = pm.getApplicationIcon(pi.applicationInfo);
            //if(temp!=null && ai.icon!=null) {
            //    ai.icon.setImageDrawable(temp);
            //}
            apps.add(ai);
        }
        return apps;
    }*/
}
