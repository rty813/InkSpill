package com.example.zhang.inkspill;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InterfaceAddress;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Rect[][] rects;
    private int[][] colors;
    private final int N = 20;
    private final int M = 19;
    private MyView view;
    private LinearLayout layout;
    private ProgressBar progressBar;
    private int progress;
    private final int maxProgress = 30;
    private final int[] dx = new int[]{0, 0, -1, 1};
    private final int[] dy = new int[]{1, -1, 0, 0};
    private Button btnBlue;
    private Button btnGray;
    private Button btnYellow;
    private Button btnRed;
    private Button btnGreen;
    private ProgressDialog dialog;
    private Updateinfo updateinfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new updateThread().start();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(maxProgress);
        init();
        layout = (LinearLayout) findViewById(R.id.root);
        view = new MyView(this);
        view.invalidate();
        layout.addView(view);
        layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                init();
                view.invalidate();
                btnBlue.setEnabled(true);
                btnYellow.setEnabled(true);
                btnGray.setEnabled(true);
                btnRed.setEnabled(true);
                btnGreen.setEnabled(true);
                return false;
            }
        });

        btnBlue = (Button) findViewById(R.id.btnBlue);
        btnGreen = (Button) findViewById(R.id.btnGreen);
        btnRed = (Button) findViewById(R.id.btnRed);
        btnGray = (Button) findViewById(R.id.btnGrey);
        btnYellow = (Button) findViewById(R.id.btnYellow);

        btnBlue.setOnClickListener(this);
        btnGreen.setOnClickListener(this);
        btnRed.setOnClickListener(this);
        btnGray.setOnClickListener(this);
        btnYellow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int preColor = colors[0][0];
        switch (v.getId()){
            case R.id.btnBlue:
                colors[0][0] = Color.BLUE;
                break;
            case R.id.btnGreen:
                colors[0][0] = Color.GREEN;
                break;
            case R.id.btnGrey:
                colors[0][0] = Color.GRAY;
                break;
            case R.id.btnRed:
                colors[0][0] = Color.RED;
                break;
            case R.id.btnYellow:
                colors[0][0] = Color.YELLOW;
                break;
        }
        int nowColor = colors[0][0];
        if (nowColor == preColor){
            return;
        }
        progress --;
        progressBar.setProgress(progress);
        if (progress == 0){
            Toast.makeText(MainActivity.this, "胜败乃兵家常事，大侠请重新来过！长按屏幕重置", Toast.LENGTH_LONG).show();
            btnBlue.setEnabled(false);
            btnYellow.setEnabled(false);
            btnGray.setEnabled(false);
            btnRed.setEnabled(false);
            btnGreen.setEnabled(false);
        }
        changeColor(0, 0, preColor, nowColor);
        view.invalidate();
        if (check(nowColor)){
            Toast.makeText(MainActivity.this,"哇啊偶！你真棒！长按屏幕重置",Toast.LENGTH_LONG).show();
            btnBlue.setEnabled(false);
            btnYellow.setEnabled(false);
            btnGray.setEnabled(false);
            btnRed.setEnabled(false);
            btnGreen.setEnabled(false);
        }
    }

    private boolean check(int color){
        for (int i=0; i<N; i++)
            for (int j=0; j<M; j++)
                if (colors[i][j] != color)
                    return false;
        return true;
    }

    private void changeColor(int x, int y, int preColor, int nowColor){
        colors[x][y] = nowColor;
        for (int i=0; i<4; i++){
            int newX = x + dx[i];
            int newY = y + dy[i];
            if ((newX < 0) || (newX >= N) || (newY < 0) || (newY >= M)){
                continue;
            }
            if (colors[newX][newY] == preColor){
                changeColor(newX, newY, preColor, nowColor);
            }
        }
    }

    private void init(){
        findViewById(R.id.btnYellow).setBackgroundColor(Color.YELLOW);
        findViewById(R.id.btnRed).setBackgroundColor(Color.RED);
        findViewById(R.id.btnBlue).setBackgroundColor(Color.BLUE);
        findViewById(R.id.btnGrey).setBackgroundColor(Color.GRAY);
        findViewById(R.id.btnGreen).setBackgroundColor(Color.GREEN);
        progress = maxProgress;
        progressBar.setProgress(progress);

        rects = new Rect[N][M];
        colors = new int[N][M];
        for (int i=0; i<N; i++){
            for (int j=0; j<M; j++){
                rects[i][j] = new Rect(20 + j * 50, 70 + i * 50, 68 + j * 50, 118 + i * 50);
                int color = (int) (Math.random() * 5);
                switch (color){
                    case 0: colors[i][j] = Color.RED;
                        break;
                    case 1: colors[i][j] = Color.BLUE;
                        break;
                    case 2: colors[i][j] = Color.YELLOW;
                        break;
                    case 3: colors[i][j] = Color.GREEN;
                        break;
                    case 4: colors[i][j] = Color.GRAY;
                        break;
                }
            }
        }


    }


    public class MyView extends View{
        public MyView(Context context){
            super(context);
            setWillNotDraw(false);
        }

        @Override
        public void draw(Canvas canvas) {
            System.out.println("绘制啦!!!");
            super.draw(canvas);
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);

            for (int i=0; i<N; i++) {
                for (int j = 0; j < M; j++) {
                    paint.setColor(colors[i][j]);
                    canvas.drawRect(rects[i][j], paint);
                }
            }
//            invalidate();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1,101,1,"关于");
        menu.add(1,102,1,"帮助");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(MainActivity.this);
        switch (item.getItemId()) {
            case 101:
                normalDialog.setTitle("关于");
                normalDialog.setMessage("By 上古豆腐 QQ:523213189");
                normalDialog.setPositiveButton("确定", null);
                normalDialog.show();
                break;
            case 102:
                normalDialog.setTitle("帮助");
                normalDialog.setMessage("这么简单的游戏怎么会有帮助？");
                normalDialog.setPositiveButton("确定", null);
                normalDialog.show();
                break;
        }
        return false;
    }

    public class updateThread extends Thread{
        public void run(){
            updateinfo = new UpdateinfoService().getUpdateinfo();
            if (updateinfo.getVersion().equals("Unknown Version!")){
                return;
            }
            if (updateinfo == null)
                return;
            try{
                PackageManager packageManager = getPackageManager();
                PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
                if (!packageInfo.versionName.equals(updateinfo.getVersion())){
                    handler.sendEmptyMessage(0);
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private Handler handler = new Handler(){
//        @Override
        public void handleMessage(Message msg) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setIcon(android.R.drawable.ic_dialog_info);
            builder.setTitle("请升级InkSpill至版本" + updateinfo.getVersion());
            builder.setMessage(updateinfo.getDescription());
            builder.setCancelable(false);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialog = new ProgressDialog(MainActivity.this);
                    dialog.setTitle("正在下载");
                    dialog.setMessage("请稍后...");
                    dialog.setProgress(0);
                    dialog.show();
                    new DownloadAsynctask().execute(updateinfo.getDownloadUrl());
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            builder.show();
        };
    };

    public class DownloadAsynctask extends AsyncTask<String, Void, Void>{
        @Override
        protected Void doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                while ((len = inputStream.read(buffer)) != -1){
                    bos.write(buffer, 0, len);
                }
                bos.close();
                byte[] getData = bos.toByteArray();
                File saveDir = new File(Environment.getExternalStorageDirectory(),"InkSpill.apk");
                FileOutputStream fos = new FileOutputStream(saveDir);
                fos.write(getData);
                if (fos != null){
                    fos.close();
                }
                if (inputStream != null){
                    inputStream.close();
                }
                System.out.println("Download Success");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.cancel();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"InkSpill.apk")),
                    "application/vnd.android.package-archive");
            startActivity(intent);
        }
    }

}
