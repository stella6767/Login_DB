package com.example.project_20140327;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {

    //서버 URL 설정(PHP 파일 연동)
    final static private String URL = "http://172.30.1.49/FTPtestserver/html/Register.php";//같은 와이파이 공유!! 휴대폰 와이파이켜져있는지 확인!
    private Map<String,String> map; //자기 php 파일이 위치한 경로를 입력해주면 된다.

    public RegisterRequest(String userID, String userPassword, String userEmail, String userGender,Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        Log.d("로그볼거야이메일",String.valueOf(userEmail));
        Log.d("로그볼거야아이디",String.valueOf(userID));
        Log.d("로그볼거야비번",String.valueOf(userPassword));
        Log.d("로그볼거야성별",String.valueOf(userGender));

        map = new HashMap<>();
        map.put("userID", userID);
        map.put("userPassword", userPassword);
        map.put("userEmail", userEmail);
        map.put("userGender", userGender);
        }
        @Override
        public Map<String,String> getParams() throws AuthFailureError {
            return map;
        }
}
