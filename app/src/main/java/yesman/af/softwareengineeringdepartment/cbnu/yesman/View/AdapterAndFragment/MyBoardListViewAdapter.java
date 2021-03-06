package yesman.af.softwareengineeringdepartment.cbnu.yesman.View.AdapterAndFragment;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import info.hoang8f.widget.FButton;
import yesman.af.softwareengineeringdepartment.cbnu.yesman.R;
import yesman.af.softwareengineeringdepartment.cbnu.yesman.model.Board;
import yesman.af.softwareengineeringdepartment.cbnu.yesman.model.CategoryDomainManager;

/**
 * Created by Jo on 2016-06-30.
 */
public class MyBoardListViewAdapter extends BaseAdapter implements View.OnClickListener{
    private LayoutInflater inflater;
    private ArrayList<Board> data;
    private int layout;
    private Context context;

    public interface ListBtnClickListener {
        void onListBtnClick(int position) ;
    }

    private ListBtnClickListener listBtnClickListener;

    public MyBoardListViewAdapter(Context context, int layout, ArrayList<Board> data,ListBtnClickListener listBtnClickListener){
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data=data;
        this.layout=layout;
        this.context = context;
        this.listBtnClickListener = listBtnClickListener;
    }



    @Override
    public int getCount(){return data.size();}
    @Override
    public String getItem(int position){return data.get(position).getTitle();}
    @Override
    public long getItemId(int position){return position;}


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
        }

        FButton okbtn;
        FButton cancelbtn;
        FButton contactbtn;


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



        okbtn = (FButton)convertView.findViewById(R.id.okbtn_myboardlist);
        okbtn.setTag(position);
        cancelbtn = (FButton)convertView.findViewById(R.id.cancel_btn_myboardlist);
        cancelbtn.setTag(position);
        contactbtn = (FButton)convertView.findViewById(R.id.contact_btn);
        contactbtn.setTag(position);

        Resources resources = context.getResources();



        okbtn.setOnClickListener(this);
        cancelbtn.setOnClickListener(this);
        contactbtn.setOnClickListener(this);

        System.out.println("리스트 아이템 매칭 현황 : "+listviewitem.getContent()+listviewitem.getIsmatching());



        if(listviewitem.getIsmatching()==0){ // 매칭중
            contactbtn.setVisibility(View.VISIBLE);
            okbtn.setVisibility(View.VISIBLE);
            contactbtn.setText("수정");
            okbtn.setText("삭제");
            cancelbtn.setText("매칭중");

            contactbtn.setButtonColor(resources.getColor(R.color.fbutton_color_peter_river));
            okbtn.setButtonColor(resources.getColor(R.color.fbutton_color_peter_river));
            cancelbtn.setButtonColor(resources.getColor(R.color.fbutton_color_peter_river));


            System.out.println("매칭중 실행");

        }

        if(listviewitem.getIsmatching()==1){ // 진행중
            contactbtn.setVisibility(View.VISIBLE);
            okbtn.setVisibility(View.VISIBLE);

            contactbtn.setText("연락");
            okbtn.setText("평가");
            cancelbtn.setText("취소");

            contactbtn.setButtonColor(resources.getColor(R.color.fbutton_color_sun_flower));
            okbtn.setButtonColor(resources.getColor(R.color.fbutton_color_sun_flower));
            cancelbtn.setButtonColor(resources.getColor(R.color.fbutton_color_sun_flower));
            System.out.println("진행중 실행");
        }

        if(listviewitem.getIsmatching()==2){// 완료
            contactbtn.setVisibility(View.GONE);
            okbtn.setVisibility(View.GONE);
            cancelbtn.setText("완료");

            //cancelbtn.setBackgroundColor(Color.GREEN);
            cancelbtn.setButtonColor(resources.getColor(R.color.fbutton_color_green_sea));
            System.out.println("완료 실행");
        }



        TextView name = (TextView) convertView.findViewById(R.id.title);
        name.setText(listviewitem.getTitle());

        TextView content = (TextView) convertView.findViewById(R.id.content);
        content.setText(listviewitem.getContent());

        TextView datetxtview = (TextView) convertView.findViewById(R.id.my_boardlist_ListView_datetxt);
        Date date = listviewitem.getDate();
        SimpleDateFormat simple = new SimpleDateFormat();
        String dateday = simple.format(date);


        datetxtview.setText(dateday);

        return convertView;
    }




    @Override
    public void onClick(View v) {
        if(this.listBtnClickListener!=null){
            if(v.getId()==R.id.okbtn_myboardlist) CategoryDomainManager.isOk = 1;
            else if(v.getId()==R.id.cancel_btn_myboardlist) CategoryDomainManager.isOk = 0;
            else{
                CategoryDomainManager.isOk = 2;
            }

            this.listBtnClickListener.onListBtnClick((int)v.getTag());
        }
    }
}