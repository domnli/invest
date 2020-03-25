package pers.domnli.invest.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.baidu.mobstat.StatService;
import com.qmuiteam.qmui.arch.QMUILatestVisit;

import pers.domnli.invest.MainActivity;


public class LauncherActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        Intent intent = QMUILatestVisit.intentOfLatestVisit(this);
        if (intent == null) {
            intent = new Intent(this, MainActivity.class);
        }
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }
}
