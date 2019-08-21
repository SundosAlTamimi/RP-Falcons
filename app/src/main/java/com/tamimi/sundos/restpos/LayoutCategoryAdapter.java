package com.tamimi.sundos.restpos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tamimi.sundos.restpos.Models.FamilyCategory;
import com.tamimi.sundos.restpos.Models.UsedCategories;
import com.tamimi.sundos.restpos.Models.UsedItems;

import org.askerov.dynamicgrid.BaseDynamicGridAdapter;

import java.util.ArrayList;
import java.util.List;

public class LayoutCategoryAdapter extends BaseDynamicGridAdapter {
    Context context;
//    List<UsedCategories> items;//update 3
List<FamilyCategory> items;
//    public LayoutCategoryAdapter(Context context, List<UsedCategories> items, int columnCount) {
//        super(context, items, columnCount); //update 2
//        this.context = context;
//        this.items = items;
//    }

    public LayoutCategoryAdapter(Context context, List<FamilyCategory> items, int columnCount) {
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
//        holder.build((UsedCategories) getItem(position));//UPDATE 5
        holder.build((FamilyCategory) getItem(position));
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
/*first
//        void build(UsedCategories item) {
//            String itemName = String.valueOf(item.getCategoryName());
//            name.setText(itemName);
//            img.setImageBitmap(item.getCatPic());
//            background.setBackgroundColor(context.getResources().getColor(R.color.layer2));
//        }//update 4
end */
        void build(FamilyCategory item) {
            String itemName = String.valueOf(item.getName());
            name.setText(itemName);
            img.setImageBitmap(StringToBitMap(item.getCatPic()));
            background.setBackgroundColor(context.getResources().getColor(R.color.layer2));
        }

    }

    public Bitmap StringToBitMap(String image) {
        try {
            byte[] encodeByte = Base64.decode(image, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

}