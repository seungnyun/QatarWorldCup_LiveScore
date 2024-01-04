package com.example.qatarworldcup;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment2 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String URL = "https://search.naver.com/search.naver?where=nexearch&sm=tab_etc&mra=bkFF&qvt=0&query=2022%20%EC%B9%B4%ED%83%80%EB%A5%B4%20%EC%9B%94%EB%93%9C%EC%BB%B5%20B%EC%A1%B0";
    //월드컵 B조

    TextView[] match = new TextView[12];
    TextView[] teamScore = new TextView[12];
    ImageView[] flag = new ImageView[12];

    String[] nationA;
    String[] scoreA;
    String[] flagSrc = new String[12];


    private boolean isRun = false; // 스레드제어 변수

    final Bundle bundle = new Bundle();



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment1.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment1 newInstance(String param1, String param2) {
        Fragment1 fragment = new Fragment1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    class BackgroundThread extends Thread{

        @Override
        public void run() {
            while(isRun) { // isRun 변수가 true가 되면
                try {

                    Document doc = Jsoup.connect(URL).get();

                    Elements nation = doc.select(".text");
                    Elements teamScore = doc.select(".team_score");

                    Elements flag = doc.select(".team_thumb > img");


                    for(int i=0;i< flagSrc.length;i++){
                        flagSrc[i] = flag.get(i).attr("src");
                    } // 이미지 주소 가져와서 저장

                    nationA = nation.text().split(" ");
                    scoreA = teamScore.text().split(" ");



                    for(int i=0;i<nationA.length;i++){
                        bundle.putString("match"+i,nationA[i]);
                        bundle.putString("match_teamScore"+i,scoreA[i]);

                    }

                    Message msg = handler.obtainMessage();
                    msg.setData(bundle);
                    handler.sendMessage(msg);


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

            for(int i = 0; i < 12; i++){
                match[i].setText(bundle.getString("match"+i));
                teamScore[i].setText(bundle.getString("match_teamScore"+i));
                Glide.with(getActivity()).load(flagSrc[i]).into(flag[i]);
            }
        }
    };
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_1, container, false);


        for(int i=0;i<match.length;i++){
            match[i] = v.findViewById(getResources().getIdentifier("match"+i,"id","com.example.qatarworldcup"));
            teamScore[i] = v.findViewById(getResources().getIdentifier("match_teamScore"+i,"id","com.example.qatarworldcup"));
            flag[i] = v.findViewById(getResources().getIdentifier("match"+i+"_flag","id","com.example.qatarworldcup"));
        }

        // Inflate the layout for this fragment
        return v;
    }
    @Override
    public void onResume() {
        super.onResume();

        /* 스레드 실행 */
        BackgroundThread thread = new BackgroundThread();
        isRun = true;
        thread.start();
    }

    @Override
    public void onPause() {
        super.onPause();

        /* 스레드 중지 */
        isRun = false;
    }


}



