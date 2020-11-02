package com.example.project_20140327;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {

    //서버 URL 설정(PHP 파일 연동)
    final static private String URL = "http://172.30.1.49/FTPtestserver/html/Login.php"; //자기 php 파일이 위치한 경로와 와이파이 주소를 입력한다
    private Map<String,String> map; //문자열 형태로 받겠다는 의미

    public LoginRequest(String userID, String userPassword, Response.Listener<String> listener) {

        super(Method.POST, URL, listener, null); //post 방식으로 전송
        map = new HashMap<>();
        map.put("userID", userID);
        map.put("userPassword", userPassword);
        }
        @Override
        public Map<String,String> getParams() throws AuthFailureError {
            return map;
        }
}
