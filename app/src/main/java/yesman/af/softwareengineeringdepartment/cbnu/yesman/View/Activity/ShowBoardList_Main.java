package yesman.af.softwareengineeringdepartment.cbnu.yesman.View.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import yesman.af.softwareengineeringdepartment.cbnu.yesman.JSON.DataMakerbyJson;
import yesman.af.softwareengineeringdepartment.cbnu.yesman.JSON.JsonMaker;
import yesman.af.softwareengineeringdepartment.cbnu.yesman.R;
import yesman.af.softwareengineeringdepartment.cbnu.yesman.ServerIDO.ServerManager;
import yesman.af.softwareengineeringdepartment.cbnu.yesman.SharedPreference.SharedPreference;
import yesman.af.softwareengineeringdepartment.cbnu.yesman.View.AdapterAndFragment.Fragment_BoardList_inMain;
import yesman.af.softwareengineeringdepartment.cbnu.yesman.model.Board;
import yesman.af.softwareengineeringdepartment.cbnu.yesman.model.CategoryDomainManager;
import yesman.af.softwareengineeringdepartment.cbnu.yesman.model.User;


public class ShowBoardList_Main extends AppCompatActivity {

    public static int matchingcount = 0;
        public static int seletedtab = 0;  // 0은 재능기부 1은 재능받기
        private SharedPreference sharedPreference;
    FragmentPagerItemAdapter adapter;
    MenuItem item;

        public static Fragment_BoardList_inMain frmainfragmentcontrol = null;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            FacebookSdk.sdkInitialize(getApplicationContext());
            setContentView(R.layout.activity_showboardlist_main_biglayout);
            sharedPreference = new SharedPreference(this);




            if(getIntent().hasExtra("exist")==false){ // 유저가 존재하지 않고 가입한 경우
                initUser();
                sharedPreference = new SharedPreference(this);
                sharedPreference.put(sharedPreference.PUSH_OPTION,"ON");
            }else { // 기존에 존재 하였으나 로그아웃한후 다시 들어온경우
                sharedPreference = new SharedPreference(this);
                sharedPreference.put(sharedPreference.PUSH_OPTION,"ON");
                ServerManager serverManager = ServerManager.getInstance();
                serverManager.getAllUserInfo();
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        setPreferenceWithUser();
                        CategoryDomainManager.x = User.getInstance().getX();
                        CategoryDomainManager.y = User.getInstance().getY();
                    }
                }, 300);
            }

            if(getIntent().hasExtra("put")==true){ //기존 유저 접속
                sharedPreference = new SharedPreference(this);

            }

            ServerManager serverManager = ServerManager.getInstance();
            serverManager.getDonation_BoardList();








        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Write.class);
                startActivity(intent);
            }
        });
        fab.setBackgroundResource(R.drawable.messenger_button_white_bg_round);




        adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add(R.string.titleA, Fragment_BoardList_inMain.class)
                .add(R.string.titleB, Fragment_BoardList_inMain.class)
                .create());


        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);

            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    Fragment_BoardList_inMain fr = (Fragment_BoardList_inMain)adapter.getPage(0);
                    fr.setListViewAdapter();

                }
            }, 600);


        getSupportActionBar().setTitle("YesMan");
        // ActionBar의 배경색 변경
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF339999));


        final LinearLayout lyTabs = (LinearLayout) viewPagerTab.getChildAt(0);
        changeTabsTitleTypeFace(lyTabs, 0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                System.out.println("각 탭 포지션 : "+position);
                seletedtab = position;
                ServerManager serverManager = ServerManager.getInstance();
                User.getInstance().setBoardList(null);
                if(seletedtab==0) serverManager.getDonation_BoardList();
                else serverManager.getRequest_BoardList();

                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if(seletedtab==0){
                            System.out.println("실행?");
                            Fragment_BoardList_inMain fr = (Fragment_BoardList_inMain)adapter.getPage(0);
                            frmainfragmentcontrol = fr;
                            fr.setListViewAdapter();


                        }else{
                            System.out.println("실행?");
                            Fragment_BoardList_inMain fr = (Fragment_BoardList_inMain)adapter.getPage(1);
                            frmainfragmentcontrol = fr;
                            fr.setListViewAdapter();
                        }
                    }
                }, 400);


                //adapter.notifyDataSetChanged()
                changeTabsTitleTypeFace(lyTabs, position); // 글자 굵게 하기
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                { // 매칭이 되었다면 알려줌
                    ServerManager.getInstance().checkMatching();
                }
            }, 300);

            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                { // 매칭이 되었다면 알려줌YH
                    item.setIcon(buildCounterDrawable(matchingcount, R.drawable.file));
                }
            }, 1000);



        

    }


    public void changeTabsTitleTypeFace(LinearLayout ly, int position) {
        for (int j = 0; j < ly.getChildCount(); j++) {
            TextView tvTabTitle = (TextView) ly.getChildAt(j);
            tvTabTitle.setTypeface(null, Typeface.NORMAL);
            if (j == position) tvTabTitle.setTypeface(null, Typeface.BOLD);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_actiontab, menu);

        item = menu.findItem(R.id.myBoardList_btn);


        return true;
    }

    // matchingcount값에 따라 알림표시 그려주는 함수
    private Drawable buildCounterDrawable(int count, int backgroundImageId) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.custom_badge, null);
        view.setBackgroundResource(backgroundImageId);

        if (count == 0) {
            View counterTextPanel = view.findViewById(R.id.counterValuePanel);
            counterTextPanel.setVisibility(View.GONE);
        } else {
            TextView textView = (TextView) view.findViewById(R.id.count);
            textView.setText("" + count);
        }

        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        return new BitmapDrawable(getResources(), bitmap);
    }

    // 알림 숫자 증가 함수
    private void doIncrease() {
        matchingcount++;
        invalidateOptionsMenu();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mypage_btn) {
            Intent intent = new Intent(this,MyPage.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.myBoardList_btn) {
            Intent intent = new Intent(this,MyBoardList.class);
            startActivity(intent);
            matchingcount = 0;
            buildCounterDrawable(matchingcount,R.drawable.file);
            //알림 숫자증가 테스트
            //doIncrease();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setPreferenceWithUser(){
        User user = User.getInstance();
        sharedPreference.put(sharedPreference.user_id,user.getUserID());
        sharedPreference.put(sharedPreference.user_name,user.getUserName());
        sharedPreference.put(sharedPreference.user_x,String.valueOf(user.getX()));
        sharedPreference.put(sharedPreference.user_y,String.valueOf(user.getY()));
        sharedPreference.put(sharedPreference.domain0,String.valueOf(user.getDomain_dsign()));
        sharedPreference.put(sharedPreference.domain1,String.valueOf(user.getDomain_translate()));
        sharedPreference.put(sharedPreference.domain2,String.valueOf(user.getDomain_document()));
        sharedPreference.put(sharedPreference.domain3,String.valueOf(user.getDomain_marketing()));
        sharedPreference.put(sharedPreference.domain4,String.valueOf(user.getDomain_computer()));
        sharedPreference.put(sharedPreference.domain5,String.valueOf(user.getDomain_music()));
        sharedPreference.put(sharedPreference.domain6,String.valueOf(user.getDomain_service()));
        sharedPreference.put(sharedPreference.domain7,String.valueOf(user.getDomain_play()));
        sharedPreference.put(sharedPreference.reg_id,user.getRegID());
    }



    private void initUser(){



        String uxs =  sharedPreference.getValue(sharedPreference.user_x,"userx");
        String uys = sharedPreference.getValue(sharedPreference.user_y,"usery");
        System.out.println("ux "+uxs);
        System.out.println("uy" +uys);

        User.getInstance().setUserID(sharedPreference.getValue(sharedPreference.user_id,"userId"));
        User.getInstance().setUserName(sharedPreference.getValue(sharedPreference.user_name,"username"));
        User.getInstance().setX(Double.parseDouble(uxs));
        User.getInstance().setY(Double.parseDouble(uys));
        User.getInstance().setDomain_computer(sharedPreference.getValue(sharedPreference.domain4,0));
        User.getInstance().setDomain_document(sharedPreference.getValue(sharedPreference.domain2,0));
        User.getInstance().setDomain_dsign(sharedPreference.getValue(sharedPreference.domain0,0));
        User.getInstance().setDomain_marketing(sharedPreference.getValue(sharedPreference.domain3,0));
        User.getInstance().setDomain_music(sharedPreference.getValue(sharedPreference.domain5,0));
        User.getInstance().setDomain_play(sharedPreference.getValue(sharedPreference.domain7,0));
        User.getInstance().setDomain_service(sharedPreference.getValue(sharedPreference.domain6,0));
        User.getInstance().setDomain_translate(sharedPreference.getValue(sharedPreference.domain1,0));
    }

    public void onResume(){
        super.onResume();
        User.getInstance().setBoardList(new ArrayList<Board>());
        ServerManager serverManager = ServerManager.getInstance();
        if(seletedtab==0) serverManager.getDonation_BoardList();
        else serverManager.getRequest_BoardList();

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if(seletedtab==0){
                    System.out.println("실행?");
                    Fragment_BoardList_inMain fr = (Fragment_BoardList_inMain)adapter.getPage(0);
                    fr.setListViewAdapter();


                }else{
                    System.out.println("실행?");
                    Fragment_BoardList_inMain fr = (Fragment_BoardList_inMain)adapter.getPage(1);
                    fr.setListViewAdapter();
                }
            }
        }, 600);
    }

    class getMyBoardListConnectionPre extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String...url) {
            // URL 연결이 구현될 부분
            URL url1;
            String response = null;
            Log.w("Dd","dd");

            try {
                System.out.println("보냄?");
                URL object = new URL(url[0]);
                HttpURLConnection con = (HttpURLConnection) object.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Accept", "*/*");
                con.setRequestProperty("X-Requested-With", "XMLHttpRequest");
                con.setRequestMethod("POST");

                JSONObject obj = JsonMaker.getInstance().makeJson();

                OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                System.out.println(obj.toString());
                if(obj!=null) wr.write("data=" + obj.toString());

                wr.flush();
                wr.close();


                StringBuilder sb = new StringBuilder();
                int HttpResult = con.getResponseCode();
                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            con.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    //System.out.println("" + sb.toString());
                    response = sb.toString();
                } else {
                    System.out.println(con.getResponseMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }



            return response;
        }
        @Override
        protected void onPostExecute(String result) {
            // UI 업데이트가 구현될 부분
            System.out.println("result값 : "+result);

            if(result!=null && (JsonMaker.getInstance().getSeleted()==JsonMaker.GET_REQUSET_LIST
                    || JsonMaker.getInstance().getSeleted()==JsonMaker.GET_DONATION_LIST
                    || JsonMaker.getInstance().getSeleted()==JsonMaker.CHECK_MYBOARDLIST)){

                boolean isbe = true;

                try {
                    JSONObject checkobj = new JSONObject(result);

                    System.out.println("1"+checkobj.getString("data")+"1");

                    if(checkobj.getString("data").equals("null")) isbe = false;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //ㅇ
                if(isbe){
                    User.getInstance().setBoardList(DataMakerbyJson.getDataMaker().getBoardList(result));

                    if(JsonMaker.getInstance().getSeleted()==JsonMaker.GET_REQUSET_LIST
                            || JsonMaker.getInstance().getSeleted()==JsonMaker.GET_DONATION_LIST){
                        if(ShowBoardList_Main.frmainfragmentcontrol!=null && User.getInstance().getBoardList()!=null) ShowBoardList_Main.frmainfragmentcontrol.setListViewAdapter();
                    }

                    if(JsonMaker.getInstance().getSeleted()==JsonMaker.CHECK_MYBOARDLIST){

                        if(MyBoardList.adapter!=null){
                            //MyBoardList.adapter = new MyBoardListViewAdapter(MyBoardList.context, R.layout.content_listview_inmyboardlist, User.getInstance().getBoardList(), MyBoardList.context.getCon);
                            // MyBoardList.listView.setAdapter(MyBoardList.adapter);
                            //MyBoardList.adapter.notifyDataSetChanged();
                        }
                    }
                    ArrayList<Board> arr = User.getInstance().getBoardList();
                    for(int i=0;i<arr.size();i++){
                        System.out.println(arr.get(i).getContent());
                        System.out.println(arr.get(i).getAcceptID());
                        System.out.println(arr.get(i).getDomain());
                        System.out.println(arr.get(i).getX());

                    }
                }
            }

            if(JsonMaker.getInstance().getSeleted()==JsonMaker.CHECK_USER){
                System.out.println("유저 확인");
                System.out.println(result);
                User.getInstance().setExist_already(DataMakerbyJson.getDataMaker().chekUser(result));

            }

            if(JsonMaker.getInstance().getSeleted()==JsonMaker.GET_MY_INFORMATION){
                User.getInstance().setPoint(DataMakerbyJson.getDataMaker().getPoint(result));
                User.getInstance().setReliability(DataMakerbyJson.getDataMaker().getReliability(result));
            }

            if(JsonMaker.getInstance().getSeleted()==JsonMaker.CHECK_MATCHING){
                ShowBoardList_Main.matchingcount = DataMakerbyJson.getDataMaker().chekMatchingCount(result);
            }

            if(JsonMaker.getInstance().getSeleted()==JsonMaker.GETALLINFO){

            }

            if(JsonMaker.getInstance().getSeleted()==JsonMaker.REGISTER_RELIABILITY){

            }



        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.v("d", "WaitAndDrawAsyncTask on Cancelled");

        }


    }



}