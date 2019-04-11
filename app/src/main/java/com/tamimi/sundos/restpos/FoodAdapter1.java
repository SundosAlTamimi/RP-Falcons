package com.tamimi.sundos.restpos;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tamimi.sundos.restpos.Models.Items;

import java.util.ArrayList;

public class FoodAdapter1 extends BaseAdapter {
    Context context;
    ArrayList<Items> items;

    FoodAdapter1(Context context, ArrayList<Items> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position).getMenuName();
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

        name.setText(items.get(position).getMenuName());
        description.setText(items.get(position).getDescription());
        img.setImageDrawable(new BitmapDrawable(context.getResources(), items.get(position).getPic()));
        if (items.get(position).getPrice() != 0)
            price.setText("$" + items.get(position).getPrice());

        background.setBackgroundColor(items.get(position).getBackground());
        name.setTextColor(items.get(position).getTextColor());
        description.setTextColor(items.get(position).getTextColor());

        return view1;
    }

}