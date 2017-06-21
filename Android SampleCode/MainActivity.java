package com.linyuting.luistest;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;

import static android.content.Intent.ACTION_VIEW;

public class MainActivity extends Activity {

    private Handler mHandler = new Handler();

    private static final String LOG_TAG = "AudioRecordTest";
    private String FileName = null;
    private MediaPlayer mPlayer = null;
    private MediaRecorder mRecorder = null;
    ExtAudioRecorder recorder = ExtAudioRecorder.getInstanse(false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                //LUISHelper luisHelper = new LUISHelper("https://westus.api.cognitive.microsoft.com/luis/v2.0/apps/65017861-5d01-45cf-a1f9-0200aac337aa?subscription-key=a97a1502e9c64b44a297a296dfc52acd&verbose=true&q=");
//                //final LUISModel luisModel = luisHelper.Query("你是谁");
//
//                //final String xxx = new BingAutoToTextHelper().recognizeFromBytes(getFromAssets("1.wav"));
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        //TextView tv = (TextView) findViewById(R.id.tv);
//                        //tv.setText(luisModel.getQuery());
//                        //tv.setText(xxx);
//
//                    }
//                });
//
//            }
//        }).start();

        FileName = getApplicationContext().getCacheDir() + "/temp.wav";

        final TextView tv = (TextView) findViewById(R.id.tv);

        final Button action_btn = (Button) findViewById(R.id.action_btn);
        action_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(action_btn.getText().equals("Start")){
                    tv.setText("说出你的问题");
                    recorder = ExtAudioRecorder.getInstanse(false);
                    recorder.recordChat(FileName);
                    action_btn.setText("Stop");
                }
                else{

                    recorder.stopRecord();
                    recorder.release();

                    final byte[] tmp = getFromAssets(FileName);
                    tv.setText("...");

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            final String xxx = new BingAutoToTextHelper().recognizeFromBytes(tmp);

                            //tv.setText(luisModel.getQuery());
                            Bing2TextModel bing2TextModel = Bing2TextModel.fromJsonString(xxx);
                            if (bing2TextModel == null || bing2TextModel.getHeader() == null || bing2TextModel.getHeader().getStatus().equals("error")) {
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv.setText("对不起，我没听清(101)");
                                    }
                                });
                                return;
                            }
                            LUISHelper luisHelper = new LUISHelper();
                            final LUISModel luisModel = luisHelper.Query(bing2TextModel.getHeader().getLexical());

                            if (luisModel == null) {
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv.setText("对不起，我没听清(102)");
                                    }
                                });
                                return;
                            }
                            final String topIntent = luisModel.getTopScoringIntent().getIntent();
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {

                                    switch (topIntent) {
                                        case "greeting":
                                            tv.setText("有什么事快说？");
                                            break;
                                        case "who":
                                            if(luisModel.getEntities()!=null && luisModel.getEntities().size()>0){
                                                String entity = luisModel.getEntities().get(0).getEntity();
                                                if (entity.equals("你")) {
                                                    tv.setText("我就是TechSun机器人啊");
                                                } else {
                                                    tv.setText("我怎么知道" + entity + "是谁？？");
                                                }
                                            }
                                            else{
                                                tv.setText("我就是TechSun机器人啊");
                                            }
                                            break;
                                        case "weather":
                                            Uri uri = null;
                                            if(luisModel.getEntities()!=null && luisModel.getEntities().size()>0){
                                                String entity = luisModel.getEntities().get(0).getEntity();
                                                uri = Uri.parse("https://www.baidu.com/s?wd="+entity+"天气");
                                            }
                                            else{
                                                uri = Uri.parse("https://www.baidu.com/s?wd=天气");
                                            }
                                            android.content.Intent it = new android.content.Intent(android.content.Intent.ACTION_VIEW, uri);
                                            startActivity(it);
                                            tv.setText("Click & Start");
                                            break;
                                        default:
                                            tv.setText("还没学习到这里，试下 你是谁 天气怎么样 之类的吧");
                                            break;
                                    }

                                }
                            });

                        }
                    }).start();
                    action_btn.setText("Start");
                }

            }
        });

//        new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        byte[] s = new Text2AudioHelper().convertToAodio();
//                        MediaPlayer mediaPlayer = new MediaPlayer();
//                        try{
//                            mediaPlayer.prepare();
//                        }
//                        catch (Exception e){
//
//                        }
//
//                    }
//                }).start();


    }

    public byte[] getFromAssets(String fileName) {
        byte[] result = null;
        try {
            File file = new File(fileName);
            FileInputStream in = new FileInputStream(file);
            int lenght = in.available();
            byte[] buffer = new byte[lenght];
            in.read(buffer);
            in.close();
            result = buffer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
