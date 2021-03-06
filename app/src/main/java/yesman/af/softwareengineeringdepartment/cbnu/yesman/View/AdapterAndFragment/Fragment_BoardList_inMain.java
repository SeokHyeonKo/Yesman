package yesman.af.softwareengineeringdepartment.cbnu.yesman.View.AdapterAndFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import yesman.af.softwareengineeringdepartment.cbnu.yesman.R;
import yesman.af.softwareengineeringdepartment.cbnu.yesman.View.Activity.ContentBoard;
import yesman.af.softwareengineeringdepartment.cbnu.yesman.model.Board;
import yesman.af.softwareengineeringdepartment.cbnu.yesman.model.User;

/**
 * Created by Jo on 2016-06-26.
 */
public class Fragment_BoardList_inMain extends Fragment {

    ArrayList<Board> data;
    ListView listView;
    ListViewAdapter adapter;
    FragmentActivity frg;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listview_fragment, container, false);
        listView=(ListView)view.findViewById(R.id.board_list);

        if(User.getInstance().getBoardList()!=null) data = User.getInstance().getBoardList();

        for(int i=0;i<data.size();i++){
            System.out.println(data.get(i).getUserId());
        }
        System.out.println("최초 실행 구간임 왜 데이터가 넘오오지 않을까");
        adapter = new ListViewAdapter(getActivity(), R.layout.content_listview_showboarlist_main, data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(listener);

        return view;
    }

    public void setListViewAdapter(){
        if(User.getInstance().getBoardList()!=null){
        data = User.getInstance().getBoardList();
        for(int i=0;i<data.size();i++){
            System.out.println("데이터 체크 : "+data.get(i).getUserId());
        }
        adapter = new ListViewAdapter(getActivity(), R.layout.content_listview_showboarlist_main, data);
        listView.setAdapter(adapter);
        listView.invalidate();
        }
    }

    AdapterView.OnItemClickListener listener= new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub

            Intent detailBoard = new Intent(getActivity(), ContentBoard.class);
            User.getInstance().setCurrentBoard(data.get(position));
            getContext().startActivity(detailBoard);
        }
    };
}
