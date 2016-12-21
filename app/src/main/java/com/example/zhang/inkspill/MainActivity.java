package com.example.zhang.inkspill;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Rect[][] rects;
    private int[][] colors;
    private final int N = 20;
    private final int M = 19;
    private MyView view;
    private LinearLayout layout;
    private ProgressBar progressBar;
    private int progress;
    private final int[] dx = new int[]{0, 0, -1, 1};
    private final int[] dy = new int[]{1, -1, 0, 0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(20);
        init();
        layout = (LinearLayout) findViewById(R.id.root);
        view = new MyView(this);
        view.invalidate();
        layout.addView(view);
        layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(MainActivity.this, "重新来过！" ,Toast.LENGTH_SHORT).show();
                init();
                view.invalidate();
                return false;
            }
        });



        findViewById(R.id.btnBlue).setOnClickListener(this);
        findViewById(R.id.btnGreen).setOnClickListener(this);
        findViewById(R.id.btnRed).setOnClickListener(this);
        findViewById(R.id.btnGrey).setOnClickListener(this);
        findViewById(R.id.btnYellow).setOnClickListener(this);


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
            Toast.makeText(MainActivity.this, "胜败乃兵家常事，大侠请重新来过！", Toast.LENGTH_LONG).show();
        }
        changeColor(0, 0, preColor, nowColor);
        view.invalidate();
        if (check(nowColor)){
            Toast.makeText(MainActivity.this,"哇啊偶！你真棒！",Toast.LENGTH_LONG).show();
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
        progress = 20;
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
        menu.add("关于");
        menu.add("帮助");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(MainActivity.this);
        normalDialog.setTitle("关于");
        normalDialog.setMessage("");
        normalDialog.setPositiveButton("确定",null);
        normalDialog.show();
        return super.onOptionsItemSelected(item);
    }

}
