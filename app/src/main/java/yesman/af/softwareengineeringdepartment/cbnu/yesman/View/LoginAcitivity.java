package yesman.af.softwareengineeringdepartment.cbnu.yesman.View;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;
import java.util.regex.Pattern;

import yesman.af.softwareengineeringdepartment.cbnu.yesman.R;
import yesman.af.softwareengineeringdepartment.cbnu.yesman.SharedPreference.SharedPreference;
import yesman.af.softwareengineeringdepartment.cbnu.yesman.model.User;

public class LoginAcitivity extends Activity {

    private CallbackManager callbackManager;
    /* facebook profile */
    private Profile profile;
    private Button facebookbtn;
    private SharedPreference sharedPreference;
    private String user_Id;
    private String user_password;
    private String user_name;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_facebook__login);
        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
                //
            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                Log.d("FaceBook :::", "id : " + newProfile);
            }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();
        sharedPreference = new SharedPreference(this);

        facebookbtn = (Button) findViewById(R.id.facebookbtn);
//dd
        facebookbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LoginManager - 요청된 읽기 또는 게시 권한으로 로그인 절차를 시작합니다.
                LoginManager.getInstance().logInWithReadPermissions(LoginAcitivity.this,
                        Arrays.asList("public_profile", "user_friends"));
                LoginManager.getInstance().registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                User user = User.getInstance();
                                AccessToken.setCurrentAccessToken(loginResult.getAccessToken());
                                /* 페이스북 프로필을 가져온다*/
                                Handler mHandler = new Handler();
                                mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        profile = Profile.getCurrentProfile();
                                        user_Id = profile.getId();
                                        user_name = profile.getName();
                                        sharedPreference.put(sharedPreference.user_id, user_Id);
                                        sharedPreference.put (sharedPreference.user_name, user_name);
                                        Log.d("FaceBook :::", "id : " + user_Id);
                                        Log.d("FaceBook :::", "name : " + user_name);
                                    }
                                }, 500);
                                user.setUserID(user_Id);
                                /*
                                //사용 디바이스 핸드폰 번호 알아오기
                                TelephonyManager systemService = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                                String user_phone = systemService.getLine1Number();
                                user_phone = user_phone.substring(user_phone.length() - 10, user_phone.length());
                                user_phone = "0" + user_phone;
                                */
                                /*
                                //안드로이드 기기 구글 동기화 이메일 주소 가져오기
                                Pattern emailPattern = Patterns.EMAIL_ADDRESS;
                                AccountManager am = (AccountManager) getSystemService(Context.ACCOUNT_SERVICE);
                                Account[] accounts = am.getAccounts();
                                String user_email = "";
                                for (Account account : accounts) {
                                    if (emailPattern.matcher(account.name).matches()) {
                                        user_email = account.name;
                                    }
                                }
                                */
                                /** SharedPreferences로 Facbook 정보 저장*/
                                /* 로그인될 시 세션을 유지하기 위해 환경 변수에 facebook login session 관리를 저장한다. */
                                sharedPreference.put(sharedPreference.facebook_login, "LOGIN");
                                Intent mainIntent = new Intent(LoginAcitivity.this, MapAcitivity.class);
                                startActivity(mainIntent);
                                finish();
                            }
                            @Override
                            public void onCancel() {
                                Toast.makeText(LoginAcitivity.this, "페이스북 로그인이 취소 되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onError(FacebookException exception) {
                                Toast.makeText(LoginAcitivity.this, "페이스북 로그인이 취소 되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}


