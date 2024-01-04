package com.example.qatarworldcup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class favoriteTeam extends AppCompatActivity {

    private EditText sendTeam;
    //    private TextView receiveMessage;
    private ProgressBar loadingBar;

    String Data;
    String URL = "https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=1&ie=utf8&query=";

    private boolean isRun = false;
    final Bundle bundle = new Bundle();



    String imgSrc;
    ImageView myTeam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_team);

        sendTeam = findViewById(R.id.sendteam);

//        receiveMessage = findViewById(R.id.tv_receive);
        loadingBar = findViewById(R.id.progressBar);

        myTeam =findViewById(R.id.myTeam);


        HttpURLConnection connection;

        Button send = findViewById(R.id.btn_sendData);





        send.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sendTeam.getText().toString().length() == 0){
                    Toast.makeText(getApplicationContext(),"나라이름을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                URL url = null;
                try{
                    String path = getResources().getString(R.string.server_url) + "?msg=";
                    String query = URLEncoder.encode(sendTeam.getText().toString(),"UTF-8");
                    url = new URL (path+query);
                }catch (MalformedURLException | UnsupportedEncodingException e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                new HttpConnection().execute(url);
            }
        });

    }
    private class HttpConnection extends AsyncTask<URL,Integer,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            String data = "";
            if(urls.length==0){
                return "URL is empty";
            }
            try {
                RequestHttpURLConnection connection = new RequestHttpURLConnection();
                data = connection.request(urls[0]);
            }catch (Exception e){
                data = e.getMessage();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);

            loadingBar.setVisibility(View.INVISIBLE);
//            receiveMessage.setText(data);
            if(data.equals("0")){
                Toast.makeText(getApplicationContext(), "나라이름을 다시 입력해주세요", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), data+"을(를) 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                Data = data;

                favoriteTeam.BackgroundThread thread = new favoriteTeam.BackgroundThread();
                isRun = true;
                thread.start();
            }
        }
    }

    class BackgroundThread extends Thread{

        @Override
        public void run() {
            while(isRun) { // isRun 변수가 true가 되면
                try {

                    Document doc = Jsoup.connect(URL+Data).ignoreHttpErrors(true).get();
                    Elements Url = doc.select(".detail_info > a > img");

                    imgSrc = Url.attr("src");

                    Message msg = handler.obtainMessage();
                    msg.setData(bundle);
                    handler.sendMessage(msg);



                    isRun = false;

                } catch (IOException e) {
                    e.printStackTrace();
                }
                try{
                    this.join(); // 스레드를 메인 스레드와 합치기
                }catch(Exception e){
                    e.printStackTrace();
                }
            }



        }

    }


    Handler handler = new Handler(){


        @Override
        public void handleMessage(@NonNull Message msg) {
            Bundle bundle = msg.getData();

            Glide.with(getApplicationContext()).load(imgSrc).override(500, 500).into(myTeam);
        }

    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_option, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu1:

                Intent in = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(in);

                Toast.makeText(this, "조별리그로 이동합니다.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu2:
                in = new Intent(this,tournament.class);
                startActivity(in);

                Toast.makeText(this, "토너먼트로 이동합니다.", Toast.LENGTH_SHORT).show();
                finish();
                break;

            case R.id.menu3:
                in = new Intent(getApplicationContext(),favoriteTeam.class);
                startActivity(in);

                setContentView(R.layout.activity_favorite_team);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

