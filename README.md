# Login_DB
로그인 DB 응용





1. 데이터베이스 서버 연동해서 간단한 로그인과 회원가입 기능을 구현함

2. 서버 및 데이터베이스는 XAMPP를 활용 아파치서버와 MYSQL을 이용해 구현함

![image](https://user-images.githubusercontent.com/65489223/97873659-4f06b080-1d5b-11eb-826c-8bb3f185eb8d.png)

3. 네트워크 통신은 와이파이를 통해 서버와 연결하므로 sql과 아파치가 켜져있는 상태에서 서버로 쓰는 본인의 컴퓨터와 앱을 실행하는 스마트폰의 와이파이가 같은 와이파이여만 기능 구현.  (IP4 주소)
![image](https://user-images.githubusercontent.com/65489223/97873666-5201a100-1d5b-11eb-8d14-724b292dd481.png)

4. 데이터베이스 연결은 php 문을 통해서 해결한다. php 파일의 위치는 
C:\xampp\htdocs 아래에 위치한다. 

5. 로그인 후의 화면에서는 구글 맵 API를 활용해 동의대 위치의 구글 지도를 화면에 띄워준다. (간단한 지도 앱)


자세한 내용은 코드의 주석으로 대체하겠습니다.





회원 정보를 등록하기 위해 먼저 phpmyadmin을 활용,  데이터베이스를 구축하였습니다. 

![image](https://user-images.githubusercontent.com/65489223/97873675-54fc9180-1d5b-11eb-8526-2f3c2b82a874.png)
![image](https://user-images.githubusercontent.com/65489223/97873684-57f78200-1d5b-11eb-8465-a5970cffb748.png)

결과  



이렇게 만들어진 user 테이블은 dongeui 데이터베이스 안에 위치시켰습니다.
필드 형식은 전부 문자열로 지정해주었습니다.

다음으로 php 안드로이드와 sql 서버를 연결짓는 php 구문을 작성해 주었습니다.




<Register.php>
```
<?php 
    $con = mysqli_connect("localhost", "root", "", "dongeui");
    mysqli_query($con,'SET NAMES utf8');

    $userID = $_POST["userID"];
    $userPassword = $_POST["userPassword"];
    $userEmail = $_POST["userEmail"];
    $userGender = $_POST["userGender"];

    $statement = mysqli_prepare($con, "INSERT INTO user VALUES (?,?,?,?)");//파라미터를 가지는 조회 
    mysqli_stmt_bind_param($statement, "ssss", $userID, $userPassword, $userEmail,$userGender); //i : integer, d : double, s : string, b : blob
    //string 값 4개를 받고 스트링 형태로 저장하므로 ssss 
    mysqli_stmt_execute($statement);

    $response = array(); //배열 생성
    $response["success"] = true;
 
    echo json_encode($response);//json 포맷으로 변경

?>
```
<Login.php>
```
<?php
    $con = mysqli_connect("localhost", "root", "", "dongeui");
    mysqli_query($con,'SET NAMES utf8');

    $userID = $_POST["userID"];
    $userPassword = $_POST["userPassword"];
    
    $statement = mysqli_prepare($con, "SELECT * FROM user WHERE userID = ? AND userPassword = ?");
    mysqli_stmt_bind_param($statement, "ss", $userID, $userPassword);
    mysqli_stmt_execute($statement);

    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $userID, $userPassword, $userEmail, $userGender); 

    $response = array();
    $response["success"] = false; //로그인 성공여부를 배열 안에 담아둔다
 
    while(mysqli_stmt_fetch($statement)) {//mysql_stmt_fetch() 함수는 mysql_stmt_bind_result()함수로 묶인 버퍼들을 사용하는 로우 데이터를 리턴한다. 
        $response["success"] = true; 
        $response["userID"] = $userID;
        $response["userPassword"] = $userPassword;
        $response["userEmail"] = $userEmail;      
        $response["userGender"] = $userGender;     
    } //변수 하나라도 빼먹으면 htttplog arror 

    echo json_encode($response); 

?>
```
이 후 안드로이드 앱에서 웹 서버와 통신을 하기 위해 volley 라이브러리를 사용했습니다. 
http와 통신하기 <AndroidManifest>파일에 인터넷 권한도 추가해줍니다.

![image](https://user-images.githubusercontent.com/65489223/97873698-5c239f80-1d5b-11eb-802d-8e812994931a.png)
![image](https://user-images.githubusercontent.com/65489223/97873705-5e85f980-1d5b-11eb-97bc-680b8e7f5792.png)



화면 레이아웃은 총 3개로 구성되었으며 로그인 화면, 회원가입 화면, 지도 화면으로 구성됩니다.









<레이아웃 구성>

<div>
<img width="200" src="https://user-images.githubusercontent.com/65489223/97873725-63e34400-1d5b-11eb-897b-ac1baea07c9e.png">
<img width="200" src="https://user-images.githubusercontent.com/65489223/97873732-6776cb00-1d5b-11eb-80aa-039f2aa44636.png">
<img width="200" src="https://user-images.githubusercontent.com/65489223/97873738-69d92500-1d5b-11eb-9f6d-ad43c877d5ce.png">

</div>



	<로그인 화면>                <회원가입 화면>		            <지도>

	    
이 기능을 구현하기 위해 사용한 자바 클래스 파일은 총 4개입니다. 



![image](https://user-images.githubusercontent.com/65489223/97873754-6e9dd900-1d5b-11eb-9627-2c0cbc675fe1.png)

<loginActivity>은 처음 나타나는 화면인 로그인 레이아웃의 위젯들의 기능을 담당하며 주요 기능은 다음과 같습니다.



```
 //회원가입 버튼을 클릭 시 수행
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent); //회원가입 화면으로 넘어감  
            }
        });


  //로그인 버튼 클릭 시
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = et_id.getText().toString();
                String userPassword = et_pass.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response); //json 객체로 response 값을 받아온다.
                            boolean success = jsonObject.getBoolean("success"); //php 구문의 success 값
                            if(success){ //로그인에 성공한 경우
                                String userID = jsonObject.getString("userID");
                                String userPassword = jsonObject.getString("userPassword");
                                Toast.makeText(getApplicationContext(),"로그인에 성공하였습니다.",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, google_map_api.class);
//지도로 넘어간다.
                                intent.putExtra("userID",userID); //로그인 유저의 정보들을 담아둔다. (이후 클래스에 값을 넘겨주기 위해)
                                intent.putExtra("userPassword",userPassword);

                                startActivity(intent);
                            }else{//로그인에 실패한 경우
                                Toast.makeText(getApplicationContext(),"로그인에 실패하였습니다.",Toast.LENGTH_SHORT).show();
                                return;
                            }


```
php 서버에서 json 포맷으로 변경된 값들을 가져와서 검증하는 역할을 하고 있으며 register 구문도 큰 틀에서 비슷하기 때문에 생략하겠습니다.

구글 지도 api를 활용하기 위해
구글 클라우드 플랫폼에서 안드로이드용 맵 api 키를 생성했습니다.
app gradle에 라이브러리를 추가해주었고
<AndroidManifest.xml> 파일에 api 키를 집어넣었습니다.

<div>

![image](https://user-images.githubusercontent.com/65489223/97873783-7493ba00-1d5b-11eb-8d17-0b9168d7f3f4.png)
![image](https://user-images.githubusercontent.com/65489223/97873801-78bfd780-1d5b-11eb-8ce5-2850ca3ba5a6.png)
![image](https://user-images.githubusercontent.com/65489223/97873809-7bbac800-1d5b-11eb-8cc4-ca182701330f.png)

</div>

지도레이아웃의 자바파일 역할은 대략 이렇습니다.
```
  Intent intent = getIntent();
        String userID = intent.getStringExtra("userID"); //이전 엑티비티에서 전달해준 아이디 정보를 인텐트로 받는다
        Toast.makeText(getApplicationContext(),userID+ "님 반갑습니다!",Toast.LENGTH_SHORT).show();

        fragmentManager = getFragmentManager();
        mapFragment = (MapFragment)fragmentManager.findFragmentById(R.id.googleMap);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng location = new LatLng(35.143099, 129.034123);//동의대 위도 경도
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title("동의대학교");
        markerOptions.snippet("대학교");
        markerOptions.position(location);
        googleMap.addMarker(markerOptions);

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 20)); //에니메이션처럼 부드럽게 지도 전환
    }
```
로그인 화면에서 전달해준 로그인 유저의 정보를 받아서 토스트 메시지를 띄우고 설정해둔 위도와 경도로 구글 맵 지도를 에니메이트 형식으로 찍어주는 간단한 앱이었습니다. 

구글 api 키 같은 경우 안 되는 경우 다시 받아와서 설정해야 될 수 있습니다.
그리고 버튼을 눌러 서버와 통신하는 시간이 약간 길거나 여러번 눌러야 될 수 있습니다.









