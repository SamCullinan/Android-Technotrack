package edu.phystech.samir.gallery;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ListViewAdapter extends BaseAdapter {

    private ArrayList <Bitmap> img;
    private ArrayList <String> buf;
    private LayoutInflater inflater;

    ListViewAdapter(Context context, ArrayList<String> buf) {
        this.buf=buf;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        img=new ArrayList <Bitmap>();
        for(int i=0; i < buf.size(); i++) {
            img.add(null);
        }
    }

    class ViewHolder {
        ImageView imgView;
    }

    @Override
    public View getView(int position, View view, ViewGroup group)
    {
        ViewHolder holder;

        if(view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.list_item, null);
            holder.imgView = (ImageView)view.findViewById(R.id.item_img);
            view.setTag(holder);
        }

        else {
            holder = (ViewHolder) view.getTag();
        }

        if(img.get(position) == null) {
            holder.imgView.setImageResource(R.drawable.download);
        }
        else {
            holder.imgView.setImageBitmap(img.get(position));
        }
        return view;
    }



    public void setImage (int index, Bitmap bitmap) { img.set(index, bitmap); }
    @Override

    public Bitmap getItem (int position) {
        return img.get(position);
    }
    @Override

    public int getCount() {
        return buf.size();
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

}
