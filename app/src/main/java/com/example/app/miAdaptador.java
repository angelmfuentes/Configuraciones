package com.example.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Angel on 1/04/14.
 */
public class miAdaptador extends BaseAdapter{

    Context context;
    ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;

    public miAdaptador(Context context, ArrayList<HashMap<String, String>> data) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.menu_opcional, null);
        TextView text = (TextView) vi.findViewById(R.id.txt1);
        TextView text2 = (TextView) vi.findViewById(R.id.txt2);
        text.setText(data.get(position).get("id"));
        text2.setText(data.get(position).get("nombre"));
        return vi;
    }
}