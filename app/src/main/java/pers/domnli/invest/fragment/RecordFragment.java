package pers.domnli.invest.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qmuiteam.qmui.widget.QMUIProgressBar;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import pers.domnli.invest.R;
import pers.domnli.invest.base.BaseFragment;
import pers.domnli.invest.constant.QktMerchant;

public class RecordFragment extends BaseFragment {

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;

    @BindView(R.id.circleProgressBar)
    QMUIProgressBar mCircleProgressBar;

    @BindView(R.id.current_merchant)
    TextView mCurrentMerchant;

    private BillRemindViewModel mVm;
    private QMUIDialog mBankDialog;
    private String[] mBanks = new String[]{"工商银行","农业银行","中国银行","建设银行","交通银行","招商银行","浦发银行","民生银行","中信银行","光大银行","兴业银行","平安银行","广发银行","华夏银行","汇丰银行","恒丰银行","浙商银行","渤海银行","北京银行","上海银行","杭州银行","南京银行","宁波银行","广州银行","邮政储蓄银行","上海农村商业银行","深圳农村商业银行"};

    private String mBank;
    private Timer timer;

    public final static String ARGS_BANK = "args.bank.name";

    @Override
    protected View onCreateView() {
        Bundle args = getArguments();
        if(args != null){
            mBank = args.getString(ARGS_BANK);
        }

        FrameLayout layout = (FrameLayout) LayoutInflater.from(getActivity()).inflate(R.layout.fragment_record, null);
        ButterKnife.bind(this, layout);
        mVm = ViewModelProviders.of(this).get(BillRemindViewModel.class);

        initTopBar();
        initView();
        observe();

        mVm.getRemind();

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(timer == null){
            timer = new Timer();
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mCurrentMerchant.post(()->{
                    mCurrentMerchant.setText(QktMerchant.get(calculationId()));
                });
            }
        },0,30*1000);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(timer != null){
            timer.cancel();
            timer = null;
        }
    }

    private void initTopBar() {

        mTopBar.addLeftBackImageButton().setOnClickListener(v->popBackStack());

        mTopBar.setTitle(mBank);

        mTopBar.addRightImageButton(R.mipmap.icon_topbar_overflow, R.id.topbar_right_change_button)
                .setOnClickListener(v -> showTopBarList(v));
    }

    private void showTopBarList(View v) {
        mBankDialog.show();
    }

    private void initView(){
        mCurrentMerchant.setText(QktMerchant.get(calculationId()));


        mCircleProgressBar.setQMUIProgressBarTextGenerator(new QMUIProgressBar.QMUIProgressBarTextGenerator() {
            @Override
            public String generateText(QMUIProgressBar progressBar, int value, int maxValue) {
                return "剩余："+ 100 * (maxValue-value) / maxValue + "%";
            }
        });

        mCircleProgressBar.setProgress(30);
        mBankDialog = new QMUIDialog.CheckableDialogBuilder(getActivity())
                .addItems(mBanks, (dialog, which) -> {
                    mTopBar.setTitle(mBanks[which]);
                    dialog.dismiss();
                })
                .create(R.style.QMUI_Dialog);
    }

    private String calculationId(){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        String id = "";
        switch (hour) {
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
                id = id + '1';
                break;
            case 13:
            case 14:
            case 15:
                id = id + '2';
                break;
            case 16:
            case 17:
                id = id + '3';
                break;
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
                id = id + '4';
                break;
            case 23:
            case 0:
            case 1:
            case 2:
                id = id + '5';
                break;
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                id = id + '6';
                break;
        }
        if(minute >= 1 && minute <= 10){
            id = id + '1';
        }
        if( minute >= 11 && minute <= 20){
            id = id + '2';
        }
        if( minute >= 21 && minute <= 30){
            id = id + '3';
        }
        if( minute >= 31 && minute <= 40){
            id = id + '4';
        }
        if(minute >= 41 && minute <= 50){
            id = id + '5';
        }
        if(minute >= 51 && minute <= 60){
            id = id + '6';
        }
        return id;
    }

    private void observe(){

    }

}
