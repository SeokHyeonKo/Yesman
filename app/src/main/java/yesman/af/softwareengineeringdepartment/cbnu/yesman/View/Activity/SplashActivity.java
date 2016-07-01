package yesman.af.softwareengineeringdepartment.cbnu.yesman.View.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import yesman.af.softwareengineeringdepartment.cbnu.yesman.R;
import yesman.af.softwareengineeringdepartment.cbnu.yesman.SharedPreference.SharedPreference;


public class SplashActivity extends Activity {
    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 3000;
    private SharedPreference sharedPreference;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_slplash_layout);
        sharedPreference = new SharedPreference(this);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            /** SharedPreference 환경 변수 사용 **/
                            /** prefs.getString() return값이 null이라면 2번째 함수를 대입한다. **/
                            String facebook_login = sharedPreference.getValue(sharedPreference.facebook_login,"LOGOUT");
                            Log.d("FACEBOOK_LOGIN::::::", facebook_login);
                            /** 환경 변수 중 로그인 부분을 체킹하여 Activity 전환 */
                            // 페이스북 로그인
                            if(facebook_login.equals("LOGIN")) {
                                Intent mainIntent = new Intent(SplashActivity.this, ShowBoardList_Main.class);
                                SplashActivity.this.startActivity(mainIntent);
                                SplashActivity.this.finish();
                }
                // 페이스북 로그아웃
                else if(facebook_login.equals("LOGOUT")) {
                    Intent loginIntent = new Intent(SplashActivity.this, Login.class);
                    SplashActivity.this.startActivity(loginIntent);
                    SplashActivity.this.finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}