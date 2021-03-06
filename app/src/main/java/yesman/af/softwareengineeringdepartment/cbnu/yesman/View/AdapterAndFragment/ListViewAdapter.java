package yesman.af.softwareengineeringdepartment.cbnu.yesman.View.AdapterAndFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.FacebookSdk;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import yesman.af.softwareengineeringdepartment.cbnu.yesman.R;
import yesman.af.softwareengineeringdepartment.cbnu.yesman.model.Board;
import yesman.af.softwareengineeringdepartment.cbnu.yesman.model.CategoryDomainManager;

/**
 * Created by Jo on 2016-06-30.
 */
public class ListViewAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<Board> data;
    private int layout;
    private Context context;




    public ListViewAdapter(Context context, int layout, ArrayList<Board> data){
        this.context= context;
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data=data;
        this.layout=layout;
    }



    @Override
    public int getCount(){return data.size();}
    @Override
    public String getItem(int position){return data.get(position).getTitle();}
    @Override
    public long getItemId(int position){return position;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
        }

        Board listviewitem = data.get(position);
        ImageView icon = (ImageView) convertView.findViewById(R.id.board_image);

        if(listviewitem.getDomain()== CategoryDomainManager.COMPUTER){
            icon.setImageResource(R.drawable.computer);
        }else if(listviewitem.getDomain()== CategoryDomainManager.DOCUMENT){
            icon.setImageResource(R.drawable.document);
        }else if(listviewitem.getDomain()== CategoryDomainManager.TRANSLATE){
            icon.setImageResource(R.drawable.translate);
        }else if(listviewitem.getDomain()== CategoryDomainManager.ENTERTAINMENT){
            icon.setImageResource(R.drawable.entertainment);
        }else if(listviewitem.getDomain()== CategoryDomainManager.DESIGN){
            icon.setImageResource(R.drawable.design);
        }else if(listviewitem.getDomain()== CategoryDomainManager.MOVIE_MUSIC){
            icon.setImageResource(R.drawable.musicvideo);
        }else if(listviewitem.getDomain()== CategoryDomainManager.MARKETING){
            icon.setImageResource(R.drawable.marketing);
        }else{
            icon.setImageResource(R.drawable.lifestyle);
        }


        TextView boardname = (TextView) convertView.findViewById(R.id.title);
        boardname.setText(listviewitem.getTitle());

        TextView content = (TextView) convertView.findViewById(R.id.content);
        content.setText(listviewitem.getContent());

        TextView datetxtview = (TextView) convertView.findViewById(R.id.board_dateinmain);
        Date date = listviewitem.getDate();
        SimpleDateFormat simple = new SimpleDateFormat();
        String dateday = simple.format(date);


        datetxtview.setText(dateday);

        //final TextView nametxtview = (TextView) convertView.findViewById(R.id.user_idornameinmainboard);
        //nametxtview.setText(listviewitem.getUserId());
        FacebookSdk.sdkInitialize(context);





        return convertView;
    }


}