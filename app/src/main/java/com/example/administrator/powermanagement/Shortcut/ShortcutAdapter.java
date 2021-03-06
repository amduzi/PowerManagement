package com.example.administrator.powermanagement.Shortcut;

import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.powermanagement.Admins.BluetoothAdmin;
import com.example.administrator.powermanagement.Admins.NetworkAdmin;
import com.example.administrator.powermanagement.R;

import java.util.ArrayList;

/**
 * : The adapter for ListView in ShortcutFragment
 */
public class ShortcutAdapter extends BaseAdapter {

    // Variable definition:
    private Context context;
    private String[] list_title;
    private int[] imageContent;
    private String[] list_header;
    private ArrayList<String> list_result;
    private ArrayList<Boolean> list_status;
    private NetworkAdmin networkAdmin;
    private BluetoothAdmin bluetoothAdmin;

    // 0 = Wifi, 1 = GPRS, 2 = Bluetooth, 3 = Airplane, 4 = Hotspot, 5 = GPS, 6 = Network Flow,
    // 7 = Brightness, 8 = Volume, 9 = Sleep Time, 10 = Interaction Time11 = Latest App Usage,
    // 12 = Sync, 13 = system info
    final static int WIFI_NUM=0, GPRS_NUM=1, TOOTH_NUM=2, PLANE_NUM=3,  HOTSPOT_NUM=4, GPS_NUM=5, FLOW_NUM=6,
            LIGHT_NUM=7, VOLUME_NUM=8, SLEEP_NUM=9, ACTION_NUM=10, APP_NUM=11, SYNC_NUM =12, CPU_NUM=13, SENSOR_NUM=14;

    // Constructor: parse the parameters to data in this adapter
    public ShortcutAdapter(Context context, String[] title, int[] image, String[] header,
                           ArrayList<String> result, ArrayList<Boolean> status,
                           NetworkAdmin network, BluetoothAdmin bluetooth){
        this.context = context;
        this.list_title = title;
        this.imageContent = image;
        this.list_header = header;
        this.list_result = result;
        this.list_status = status;
        this.networkAdmin = network;
        this.bluetoothAdmin = bluetooth;
    }

    @Override
    public int getCount(){
        return list_title.length;
    }
    @Override
    public Object getItem(int position){
        return null;
    }
    @Override
    public long getItemId(int position){
        return position;
    }

    //The following 2 methods are for different layout in ListView
    @Override
    public int getViewTypeCount(){
        return 5;
    }
    @Override
    public int getItemViewType(int position){
        switch (position){
            case WIFI_NUM:
                return 0;
            case GPRS_NUM:
            case TOOTH_NUM:
                return 1;
            case LIGHT_NUM:
                return 2;
            case PLANE_NUM:
            case HOTSPOT_NUM:
            case GPS_NUM:
                return 3;
            case FLOW_NUM:
            case VOLUME_NUM:
            case SLEEP_NUM:
            case ACTION_NUM:
            case APP_NUM:
            case SYNC_NUM:
            case CPU_NUM:
            case SENSOR_NUM:
                return 4;
        }
        return 0;
    }

    public View getView(final int position, View convertView,ViewGroup parent){
        View view;
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){

            // Get views
            view = inflater.inflate(R.layout.listview_shortcut,null);
            TextView title = (TextView)view.findViewById(R.id.list_title);
            ImageView icon = (ImageView)view.findViewById(R.id.list_icon);
            TextView header = (TextView)view.findViewById(R.id.list_header_content);
            SwitchCompat switchCompat = (SwitchCompat)view.findViewById(R.id.list_switch);
            TextView result = (TextView)view.findViewById(R.id.list_result);

            if(getItemViewType(position)==0){
                header.setVisibility(View.VISIBLE);
                switchCompat.setVisibility(View.VISIBLE);
                switchCompat.setClickable(true);
                result.setVisibility(View.GONE);
            }
            else if(getItemViewType(position)==1){
                header.setVisibility(View.GONE);
                switchCompat.setVisibility(View.VISIBLE);
                switchCompat.setClickable(true);
                result.setVisibility(View.GONE);
            }
            else if(getItemViewType(position)==2){
                header.setVisibility(View.VISIBLE);
                switchCompat.setVisibility(View.GONE);
                result.setVisibility(View.VISIBLE);
            }
            else if(getItemViewType(position)==3){
                header.setVisibility(View.GONE);
                switchCompat.setVisibility(View.VISIBLE);
                result.setVisibility(View.GONE);
            }
            else{
                header.setVisibility(View.GONE);
                switchCompat.setVisibility(View.GONE);
                result.setVisibility(View.VISIBLE);
            }
            icon.setImageResource(imageContent[position]);
            title.setText(list_title[position]);
            header.setText(list_header[position]);
            result.setText(list_result.get(position));
            Log.d("debug info", position + " " + switchCompat.isChecked() + " " + list_status.get(position));
            if(!switchCompat.isFocused()) {
                switchCompat.setChecked(list_status.get(position));
            }
            switchCompat.setOnCheckedChangeListener(onCheckedChangeListener);

        }
        else{
            TextView title = (TextView)convertView.findViewById(R.id.list_title);
            ImageView icon = (ImageView)convertView.findViewById(R.id.list_icon);
            SwitchCompat switchCompat = (SwitchCompat)convertView.findViewById(R.id.list_switch);
            TextView result = (TextView)convertView.findViewById(R.id.list_result);
            icon.setImageResource(imageContent[position]);
            title.setText(list_title[position]);
            result.setText(list_result.get(position));
            Log.d("debug info", position + " " + switchCompat.isChecked() + " " + list_status.get(position) + "?");
            if(!switchCompat.isFocused()) {
                switchCompat.setChecked(list_status.get(position));
            }
            view = convertView;
        }
        return view;
    }

    /**
     * onCheckedChangeListener: listener for switch in list
     * Still need to be fixed
     */
    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener =
            new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton view, boolean b) {
                    Log.d("debug info", "on checked change listener "+b+"");
                    View parentRow = (View) view.getParent();
                    ListView listView = (ListView) parentRow.getParent().getParent();
                    // listview == null means that this is changed automatically
                    // call sekClicked will call this method
                    if(listView != null) {
                        int position = listView.getPositionForView(parentRow);
                        if (position == WIFI_NUM) {
                            networkAdmin.toggleWiFi(b);
                        } else if (position == GPRS_NUM) {
                            networkAdmin.toggleGPRS(b);
                        } else if (position == TOOTH_NUM) {
                            bluetoothAdmin.toggleBluetooth(b);
                        }
                    }
                }
            };
}
