package com.tamimi.sundos.restpos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tamimi.sundos.restpos.Models.UsedItems;

import java.util.ArrayList;

public class LayoutFoodAdapter extends BaseAdapter {
    Context context;
    ArrayList<UsedItems> items;
    int flag;

    public LayoutFoodAdapter(Context context, ArrayList<UsedItems> items, int flag) {
        this.context = context;
        this.items = items;
        this.flag = flag;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position).getitemName();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view1 = inflater.inflate(R.layout.raw_activity2, null);

        LinearLayout background = (LinearLayout) view1.findViewById(R.id.itemLinear);
        TextView name = (TextView) view1.findViewById(R.id.name);
        TextView description = (TextView) view1.findViewById(R.id.description);
        TextView price = (TextView) view1.findViewById(R.id.price);
        ImageView img = (ImageView) view1.findViewById(R.id.imageView);


        if (flag == 0) {
            String itemName = String.valueOf(items.get(position).getPosition());
            name.setText(itemName);
            description.setText("");
            background.setBackgroundColor(context.getResources().getColor(R.color.layer2));
            price.setText("");
        } else {
            name.setText(items.get(position).getitemName());
            background.setBackgroundColor(items.get(position).getBackground());
            name.setTextColor(items.get(position).getTextColor());
            description.setTextColor(items.get(position).getTextColor());
            price.setTextColor(items.get(position).getTextColor());
            price.setText("0$");
            img.setBackgroundResource(R.drawable.focused_table);

        }
        return view1;
    }

}