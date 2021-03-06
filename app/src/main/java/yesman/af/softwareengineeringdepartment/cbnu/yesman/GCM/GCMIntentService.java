package yesman.af.softwareengineeringdepartment.cbnu.yesman.GCM;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import yesman.af.softwareengineeringdepartment.cbnu.yesman.R;
import yesman.af.softwareengineeringdepartment.cbnu.yesman.View.Activity.ContentBoard;
import yesman.af.softwareengineeringdepartment.cbnu.yesman.model.Board;
import yesman.af.softwareengineeringdepartment.cbnu.yesman.model.CategoryDomainManager;
import yesman.af.softwareengineeringdepartment.cbnu.yesman.model.User;
//으범수정
/**
 * Created by seokhyeon on 2016-06-26.
 */
public class GCMIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;

    // web server 에서 받을 extra key (web server 와 동일해야 함)
    static final String TITLE_EXTRA_KEY = "TITLE";
    static final String MSG_EXTRA_KEY = "MSG";
    static final String TYPE_EXTRA_CODE = "TYPE_CODE";


    // web server 에서 받을 extras key
    //다시
    public GCMIntentService() {
        super("");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public GCMIntentService(String name) {
        super(name);
        System.out.println("************************************************* GCMIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);


        System.out.println("************************************************* messageType : " + messageType);

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // This loop represents the service doing some work.
                // 메시지를 받은 후 작업 시작
                System.out.println("************************************************* Working........................... ");

                // Post notification of received message.
                System.out.println("************************************************* 상태바 알림 호출");
                sendNotification(extras);
                System.out.println("************************************************* Received toString : " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);

    }

    // 상태바에 공지
    private void sendNotification(Bundle extras) {
        // 혹시 모를 사용가능한 코드
        String jobjstring = extras.getString(TYPE_EXTRA_CODE);
        System.out.println("넘어온 제이슨 : "+jobjstring);
        int domain = 3;
        String useridfromj = null;
        JSONObject jboj = null;

        try {

            jboj = new JSONObject(jobjstring);
            int boardserial = jboj.getInt("boardserialnumber");
            double x = jboj.getDouble("x");
            double y = jboj.getDouble("y");
            String title = jboj.getString("title");
            String content = jboj.getString("content");
            domain = jboj.getInt("domain");
            String requestID  = jboj.getString("requestID");
            String acceptID = jboj.getString("acceptID");
            int category = jboj.getInt("category");
            int ischeckrequest = jboj.getInt("ischeckrequest");
            int ischeckaccept = jboj.getInt("ischeckaccept");
            int ismatching = jboj.getInt("ismatching");
            System.out.println(ismatching);
            useridfromj = jboj.getString("UserId");

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date  = simpleDateFormat.parse(jboj.getString("date"));

            Board board = new Board(boardserial,x,y,title,content,domain,requestID,acceptID,category,ischeckrequest,ischeckaccept,ismatching,date,useridfromj);
            User.getInstance().setCurrentBoard(board);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        GCMValue.IS_NOTIFICATION = "TRUE";
        Intent intent = new Intent(this,ContentBoard.class);
        intent.putExtra("title",extras.getString(TITLE_EXTRA_KEY));
        intent.putExtra("content",extras.getString(MSG_EXTRA_KEY));
        intent.putExtra("userid", useridfromj);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                intent, 0);

        NotificationCompat.Builder mBuilder =
                null;
        try {
            mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                    //.setSmallIcon(R.mipmap.ic_launcher) // 도메인에 맡게 코딩하면 될듯
                    .setContentTitle(URLDecoder.decode("제목 : "+extras.getString(TITLE_EXTRA_KEY), "UTF-8"))
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(URLDecoder.decode(extras.getString(MSG_EXTRA_KEY), "UTF-8")))
                    .setContentText(URLDecoder.decode("내용 : "+extras.getString(MSG_EXTRA_KEY), "UTF-8"))
                    .setGroup(URLDecoder.decode(useridfromj, "UTF-8"));

            if(domain== CategoryDomainManager.COMPUTER){
                mBuilder.setSmallIcon(R.drawable.computer);
            }else if(domain== CategoryDomainManager.DESIGN){
                mBuilder.setSmallIcon(R.drawable.design);
            }else if(domain== CategoryDomainManager.DOCUMENT){
                mBuilder.setSmallIcon(R.drawable.document);
            }else if(domain== CategoryDomainManager.ENTERTAINMENT){
                mBuilder.setSmallIcon(R.drawable.entertainment);
            }else if(domain== CategoryDomainManager.LIFE){
                mBuilder.setSmallIcon(R.drawable.lifestyle);
            }else if(domain== CategoryDomainManager.MARKETING){
                mBuilder.setSmallIcon(R.drawable.marketing);
            }else if(domain== CategoryDomainManager.MOVIE_MUSIC){
                mBuilder.setSmallIcon(R.drawable.musicvideo);
            }else if(domain== CategoryDomainManager.TRANSLATE){
                mBuilder.setSmallIcon(R.drawable.translate);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        mBuilder.setVibrate(new long[]{0,1500}); // 진동 효과 (퍼미션 필요)
        mBuilder.setAutoCancel(true); // 클릭하면 삭제

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

}

