package com.example.qatarworldcup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class tournament extends AppCompatActivity {

    private String URL = "https://m.search.naver.com/search.naver?sm=mtp_sly.hst&where=m&query=%EC%B9%B4%ED%83%80%EB%A5%B4%EC%9B%94%EB%93%9C%EC%BB%B5&acr=2";
    //오늘의 영상

    private boolean isRun = false;

    final Bundle bundle = new Bundle();

    TextView[] text = new TextView[4];
    String[] text2 = new String[4];

    ImageView[] video = new ImageView[4]; // 이미지 뷰
    String[] thumb = new String[4]; //썸네일 이미지


    String[] link = new String[4]; // 비디오 링크

    String[] uri = new String[4];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament);

        ImageView tournament= findViewById(R.id.tournament);
        String url="https://search.pstatic.net/common?type=f&size=654x368&quality=75&direct=true&ttype=input&src=https%3A%2F%2Fcsearch-phinf.pstatic.net%2F20221207_220%2F16703796515584btux_PNG%2F6210_29008840_top_banner_img_url_mobile_1670379651485.png";
        Glide.with(this).load(url).apply(RequestOptions.bitmapTransform(new RoundedCorners(100))).into(tournament);


        LinearLayout layout0 = (LinearLayout) findViewById(R.id.layout0);
        LinearLayout layout1 = (LinearLayout) findViewById(R.id.layout1);
        LinearLayout layout2 = (LinearLayout) findViewById(R.id.layout2);
        LinearLayout layout3 = (LinearLayout) findViewById(R.id.layout3);


        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.layout0 :
                        try{
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri[0]));
                            startActivity(intent);
                        }catch (Exception e){
                            //예외처리
                        }
                        break;
                    case R.id.layout1 :
                        try{
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri[1]));
                            startActivity(intent);
                        }catch (Exception e){
                            //예외처리
                        }
                        break;
                    case R.id.layout2 :
                        try{
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri[2]));
                            startActivity(intent);
                        }catch (Exception e){
                            //예외처리
                        }
                        break;
                    case R.id.layout3 :
                        try{
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri[3]));
                            startActivity(intent);
                        }catch (Exception e){
                            //예외처리
                        }
                        break;
                }

            }

        };

        layout0.setOnClickListener(clickListener);
        layout1.setOnClickListener(clickListener);
        layout2.setOnClickListener(clickListener);
        layout3.setOnClickListener(clickListener);

        tournament.BackgroundThread thread = new tournament.BackgroundThread();
        isRun = true;
        thread.start();

        for(int i=0;i<video.length;i++){
            video[i] = findViewById(getResources().getIdentifier("video"+i,"id","com.example.qatarworldcup"));
            text[i] = findViewById(getResources().getIdentifier("text"+i,"id","com.example.qatarworldcup"));
        }
    }

    class BackgroundThread extends Thread{

        @Override
        public void run() {
            while(isRun) { // isRun 변수가 true가 되면
                try {

                    Document doc = Jsoup.connect(URL).get();

                    Elements Url = doc.select(".inner");
                    Elements Thumb = doc.select(".inner > img");
                    Elements Text = doc.select(".video_title.ellip_2");







                    for(int i=0;i< 4;i++){
                        uri[i] = Url.get(i+1).attr("href"); //비디오 링크
                        thumb[i] = Thumb.get(i).attr("src"); //이미지 링크
                        text2[i] = Text.get(i).text();                 // 제목 링크
                        System.out.println(uri[i]);
                    }



                    for(int i=0;i< text.length;i++){
                        bundle.putString("text"+i, text2[i]);
                    }



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


            for(int i = 0; i < video.length; i++){
                Glide.with(getApplicationContext()).load(thumb[i]).into(video[i]);
                text[i].setText(bundle.getString("text"+i));
            }
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