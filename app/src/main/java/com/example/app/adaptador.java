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
 * Created by Angel on 31/03/14.
 */
public class adaptador extends BaseAdapter {
    Context context;
    ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    public adaptador(Context context, ArrayList<HashMap<String, String>> data){
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater)context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);

    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View vista = view;
        if (vista == null)
            vista = inflater.inflate(R.layout.item_lista, null);
        TextView textView = (TextView)vista.findViewById(R.id.txtLista1);
        TextView textView2 = (TextView)vista.findViewById(R.id.txtLista2);
        textView.setText(data.get(position).get("id"));
        textView2.setText(data.get(position).get("nombre"));
        return vista;
    }
}
