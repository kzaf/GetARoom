package com.hotelreservation.hotelreseration.NavigationDrawer;

import com.hotelreservation.hotelreseration.R;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuListAdapter extends BaseAdapter{

    // Fields -----------------------------------------------------------------
    private Context mcontext;
    private String[] titles;
    private int[] icons;
    private LayoutInflater inflater;

    // Constructor ------------------------------------------------------------
    public MenuListAdapter(
            Context context, 
            String[] titles, 
            int[] icons){
        mcontext = context;
        this.titles = titles;
        this.icons = icons;
        inflater = (LayoutInflater)mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // Accessors --------------------------------------------------------------
    @Override
    public int getCount(){
        return titles.length;
    }
    @Override
    public Object getItem(int position){
        return titles[position];
    }
    @Override
    public long getItemId(int position){
        return position;
    }

    // Methods ----------------------------------------------------------------
    public View getView(int position, View convertView, ViewGroup parent){

        ViewHolder viewHolder;

        // Only inflate the view if convertView is null
        if (convertView == null){
            viewHolder = new ViewHolder();
            if(inflater!=null)
            {
            convertView = inflater.inflate(R.layout.drawr_list_item, parent, false);
            viewHolder.txtTitle = (TextView)convertView.findViewById(R.id.title);
            viewHolder.imgIcon = (ImageView)convertView.findViewById(R.id.icon);

            // This is the first time this view has been inflated,
            // so store the view holder in its tag fields
            convertView.setTag(viewHolder);
            }
            else
            {
                Log.i("........",""+null);
            }
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        // Set the views fields as needed
        viewHolder.txtTitle.setText(titles[position]);
        //viewHolder.txtSubtitle.setText(subtitles[position]);
        viewHolder.imgIcon.setImageResource(icons[position]);

        return convertView;
    }

    // Classes ----------------------------------------------------------------
    static class ViewHolder {
        TextView txtTitle;
        //TextView txtSubtitle;
        ImageView imgIcon;
    }
    
    /*
    @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}
	*/
}