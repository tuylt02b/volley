package com.example.tuy.volley;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.tuy.volley.svgandroid.SVG;
import com.example.tuy.volley.svgandroid.SVGParser;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Tuy on 6/16/2015.
 */
public class ContacsAdapter extends BaseAdapter {
    Context context;
    ArrayList<Contacs> arrayList;
    ViewHolder viewHolder;

    //ImageButton image;
    Contacs contact;

    public ContacsAdapter(Context context, ArrayList<Contacs> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Contacs getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public static class ViewHolder {

        TextView id;
        TextView name;
        TextView address;
        TextView gender;
        TextView email;
        ImageView imageView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.show_item, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.id = (TextView) convertView.findViewById(R.id.id);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.address = (TextView) convertView.findViewById(R.id.address);
            viewHolder.gender = (TextView) convertView.findViewById(R.id.gender);
            viewHolder.email = (TextView) convertView.findViewById(R.id.email);
            viewHolder.imageView =(ImageView)convertView.findViewById(R.id.image);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        setValue(viewHolder, position);
        return convertView;
    }


    public void setValue(final ViewHolder holder, final int position) {
        try {
        contact = getItem(position);
        holder.id.setText(contact.getId());
        holder.name.setText(contact.getName());
        holder.address.setText(contact.getAddress());
        holder.email.setText(contact.getEmail());
        holder.gender.setText(contact.getGender());
            final URL url = new URL("http://upload.wikimedia.org/wikipedia/commons/e/e8/Svg_example3.svg");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            SVG svg = SVGParser. getSVGFromInputStream(inputStream);
            Drawable drawable = svg.createPictureDrawable();
            holder.imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            holder.imageView.setImageDrawable(drawable);

        }catch(Exception e){}

    }
}
