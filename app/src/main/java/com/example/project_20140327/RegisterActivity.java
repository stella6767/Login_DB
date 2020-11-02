package com.example.project_20140327;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private EditText et_id, et_pass, et_email;
    private String userGender;
    private Button btn_register;


    @Override
    protected void onCreate(Bundle savedInstanceState) { //액티비티 실행 시 처음으로 실행되는 생명주기
        super.onCreate(savedInstanceState);
        Log.d("2","여긴가?");
        setContentView(R.layout.activity_register); //화면 보여주기

        //아이디값 찾아주기
        et_id = findViewById(R.id.et_id);
        et_pass = findViewById(R.id.et_pass);
        et_email = findViewById(R.id.et_email);

        //gender radio 버튼 text 값 string으로 반환
        RadioGroup genderGroup = (RadioGroup) findViewById(R.id.genderGroup);
        int genderGroupID = genderGroup.getCheckedRadioButtonId();
        userGender = ((RadioButton)findViewById(genderGroupID)).getText().toString();
        //현재 선택되어있는 값을 userGender에 대입
        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton genderButton = (RadioButton) findViewById(checkedId);
                userGender = genderButton.getText().toString();
            }
        });



        //회원가입 버튼 클릭 시 수행 버튼을 좀 길게 눌러야 함
        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("3","버튼 클릭 상태");

                //EditText에 현재 입력되어있는 값을 get해온다.
                String userID = et_id.getText().toString();
                String userPassword = et_pass.getText().toString();
                final String userEmail = et_email.getText().toString();

                //가입란 중 하나라도 입력없을 시 오류메시지 출력
                if (userID.equals("") || userPassword.equals("") || userEmail.equals("") || userGender.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    AlertDialog dialog  =builder.setMessage("빈 칸 없이 입력해주세요")
                            .setNegativeButton("확인",null)
                            .create();
                    dialog.show();
                    return;
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response); //결과 값 json 서버로부터 받아온다.
                            boolean success = jsonObject.getBoolean("success");
                            if(success){
                                Toast.makeText(getApplicationContext(),"회원등록에 성공하였습니다.",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }else{//회원 등록에 실패한 경우
                                Toast.makeText(getApplicationContext(),"회원등록에 실패하였습니다.",Toast.LENGTH_SHORT).show();
                                return;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };
                //서버로 volley를 이용해서 요청을 함
                RegisterRequest registerRequest = new RegisterRequest(userID,userPassword,userEmail,userGender,responseListener);

                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);
            }
        });


    }
}
