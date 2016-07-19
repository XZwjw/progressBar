package first.progressbar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import progressBar.CircleProgressBar;

public class MainActivity extends AppCompatActivity {

    private ProgressBar mProgressBar;
    private CircleProgressBar mCircleProgressBar;

    private static final int PROGRESS_LINE_UPDATE = 0x111;
    private static final int PROGRESS_CIRCLE_UPDATE = 0x112;
    private MyHandler mHandler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        mProgressBar = (ProgressBar) findViewById(R.id.progressLine);
        mCircleProgressBar = (CircleProgressBar) findViewById(R.id.progressCircle);
        mHandler = new MyHandler();
        mHandler.sendEmptyMessage(PROGRESS_LINE_UPDATE);
        mHandler.sendEmptyMessage(PROGRESS_CIRCLE_UPDATE);
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == PROGRESS_LINE_UPDATE) {
                int progress = mProgressBar.getProgress();
                mProgressBar.setProgress(++progress);
                if(progress >= 100) {
                    mHandler.removeMessages(PROGRESS_LINE_UPDATE);
                }

                mHandler.sendEmptyMessageDelayed(PROGRESS_LINE_UPDATE, 100);
            }
            if(msg.what == PROGRESS_CIRCLE_UPDATE) {
                int progress = mCircleProgressBar.getProgress();
                mCircleProgressBar.setProgress(++progress);
                if(progress >= 100) {
                    mHandler.removeMessages(PROGRESS_CIRCLE_UPDATE);
                }

                mHandler.sendEmptyMessageDelayed(PROGRESS_CIRCLE_UPDATE, 100);
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
