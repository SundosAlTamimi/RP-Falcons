package com.tamimi.sundos.restpos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tamimi.sundos.restpos.Models.UsedCategories;
import com.tamimi.sundos.restpos.Models.UsedItems;

import org.askerov.dynamicgrid.BaseDynamicGridAdapter;

import java.util.ArrayList;
import java.util.List;

public class LayoutCategoryAdapter extends BaseDynamicGridAdapter {
    Context context;
    List<UsedCategories> items;

    public LayoutCategoryAdapter(Context context, List<UsedCategories> items, int columnCount) {
        super(context, items, columnCount);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.raw_activity, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.build((UsedCategories) getItem(position));
        return convertView;
    }

    private class ViewHolder {
        private LinearLayout background;
        private TextView name;
        private ImageView img;

        private ViewHolder(View view) {
            background = (LinearLayout) view.findViewById(R.id.itemLinear);
            name = (TextView) view.findViewById(R.id.name);
            img = (ImageView) view.findViewById(R.id.imageView);
        }

        void build(UsedCategories item) {
            String itemName = String.valueOf(item.getCategoryName());
            name.setText(itemName);
//            holder.img.setBackgroundDrawable(items.get(position).get);
            background.setBackgroundColor(context.getResources().getColor(R.color.layer2));
        }

    }
}