package com.tamimi.sundos.restpos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tamimi.sundos.restpos.Models.UsedItems;

import org.askerov.dynamicgrid.BaseDynamicGridAdapter;

import java.util.List;

public class LayoutItemsAdapter extends BaseDynamicGridAdapter {
    private Context context;
    List<UsedItems> items;

    public LayoutItemsAdapter(Context context, List<UsedItems> items, int columnCount) {
        super(context, items, columnCount);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutItemsAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.raw_activity2, null);
            holder = new LayoutItemsAdapter.ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (LayoutItemsAdapter.ViewHolder) convertView.getTag();
        }
        holder.build((UsedItems) getItem(position));
        return convertView;
    }

    private class ViewHolder {

        LinearLayout background ;
        TextView name;
        TextView description ;
        TextView price;
        ImageView img ;

        private ViewHolder(View view) {
           background = (LinearLayout) view.findViewById(R.id.itemLinear);
            name = (TextView) view.findViewById(R.id.name);
            description = (TextView) view.findViewById(R.id.description);
            price = (TextView) view.findViewById(R.id.price);
           img = (ImageView) view.findViewById(R.id.imageView);
        }

        void build(UsedItems item) {
            name.setText(item.getitemName());
            background.setBackgroundColor(item.getBackground());
            name.setTextColor(item.getTextColor());
            description.setTextColor(item.getTextColor());
            price.setTextColor(item.getTextColor());
            price.setText("0$");

            if(item.getitemName().equals("")){
                description.setText("");
                price.setText("");
            }
            img.setImageBitmap(item.getItemPic());
        }

    }

}