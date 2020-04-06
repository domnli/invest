package pers.domnli.invest.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.util.QMUIViewHelper;
import com.qmuiteam.qmui.widget.QMUIProgressBar;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import java.util.Calendar;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import pers.domnli.invest.R;
import pers.domnli.invest.base.BaseFragment;
import pers.domnli.invest.constant.QktMerchant;
import pers.domnli.invest.repository.entity.BillingSerial;

public class RecordFragment extends BaseFragment {

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;

    @BindView(R.id.circleProgressBar)
    QMUIProgressBar mCircleProgressBar;

    @BindView(R.id.current_merchant)
    TextView mCurrentMerchant;

    @BindView(R.id.useLoanTv)
    TextView mUseLoanTv;

    @BindView(R.id.quotaTv)
    TextView mQuotaTv;

    @BindView(R.id.groupListView)
    QMUIGroupListView mGroupList;

    private RecordViewModel mVm;
    private QMUIDialog mBankDialog;

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
        mVm = ViewModelProviders.of(this).get(RecordViewModel.class);

        initTopBar();
        initView();
        observe();

        mVm.getAllBank();
        mVm.getBillProportion(mBank);

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
        if(mBankDialog != null){
            mBankDialog.show();
        }
    }

    private void initView(){
        mCurrentMerchant.setText(QktMerchant.get(calculationId()));
        mCircleProgressBar.setQMUIProgressBarTextGenerator(new QMUIProgressBar.QMUIProgressBarTextGenerator() {
            @Override
            public String generateText(QMUIProgressBar progressBar, int value, int maxValue) {
                return "剩余："+ 100 * (maxValue-value) / maxValue + "%";
            }
        });

        QMUICommonListItemView m1kItem = mGroupList.createItemView("随机700-999金额");
        m1kItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUICommonListItemView m4kItem = mGroupList.createItemView("随机2000-4000金额");
        m4kItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUICommonListItemView m10kItem = mGroupList.createItemView("随机10000-13000金额");
        m10kItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUICommonListItemView mBillItem = mGroupList.createItemView("查看账单");
        mBillItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUIGroupListView.newSection(getContext())
                .addItemView(m1kItem,generateItemClickListener(700,900))
                .addItemView(m4kItem,generateItemClickListener(2000,4000))
                .addItemView(m10kItem,generateItemClickListener(10000,13000))
                .addItemView(mBillItem,null)
                .addTo(mGroupList);
    }

    private View.OnClickListener generateItemClickListener(Integer start,Integer end){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) +1 ;
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                String description ="银行：" + mBank + "\n\n" + "日期："+year+"."+month+"."+day+"\n\n金额：";
                String money = String.valueOf((int)(start + Math.random()*(end-start)));
                SerialDialogBuilder serialDialogBuilder = (SerialDialogBuilder) new SerialDialogBuilder(getActivity(),description,money)
                        .addAction("取消", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        });
                serialDialogBuilder
                        .addAction("确定", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                BillingSerial serial = new BillingSerial();
                                serial.setBank(mBank);
                                serial.setYear(year);
                                serial.setMonth(month);
                                serial.setDay(day);
                                serial.setMoney(Float.valueOf(serialDialogBuilder.getEditText().getText().toString()));
                                mVm.addBillingSerial(serial);
                                mCircleProgressBar.postDelayed(()->mVm.getBillProportion(mBank),1500);

                                dialog.dismiss();
                            }
                        });
                serialDialogBuilder.setTitle("添加银行流水");
                serialDialogBuilder.create(R.style.QMUI_Dialog);
                serialDialogBuilder.show();
            }
        };
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
        mVm.banksLiveData.observe(getViewLifecycleOwner(), banks -> {
            String[] mBanks = new String[banks.size()];
            for (int i = 0; i < banks.size(); i++) {
                mBanks[i]=banks.get(i).getBank();
            }
            mBankDialog = new QMUIDialog.CheckableDialogBuilder(getActivity())
                    .addItems(mBanks, (dialog, which) -> {
                        mBank = mBanks[which];
                        mTopBar.setTitle(mBank);

                        dialog.dismiss();
                    })
                    .create(R.style.QMUI_Dialog);
        });

        mVm.proportionLiveData.observe(getViewLifecycleOwner(), proportion -> {
            mCircleProgressBar.setProgress(proportion.getUseProportion(),true);
            mUseLoanTv.setText(proportion.getNoPaidLoan().toString());
            mQuotaTv.setText(proportion.getQuota().toString());
        });
    }

    class SerialDialogBuilder extends QMUIDialog.AutoResizeDialogBuilder {
        private Context mContext;
        private EditText mEditText;
        private String description;
        private String money;

        public SerialDialogBuilder(Context context,String description,String money) {
            super(context);
            mContext = context;
            this.description = description;
            this.money = money;
        }

        public EditText getEditText() {
            return mEditText;
        }

        @Override
        public View onBuildContent(QMUIDialog dialog) {
            LinearLayout layout = new LinearLayout(mContext);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setLayoutParams(new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            int padding = QMUIDisplayHelper.dp2px(mContext, 20);
            layout.setPadding(padding, padding, padding, padding);

            TextView textView = new TextView(mContext);
            textView.setLineSpacing(QMUIDisplayHelper.dp2px(getContext(), 4), 1.0f);
            textView.setText(description);
            textView.setTextColor(ContextCompat.getColor(getContext(), R.color.app_color_description));
            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layout.addView(textView);

            mEditText = new AppCompatEditText(mContext);
            QMUIViewHelper.setBackgroundKeepingPadding(mEditText, QMUIResHelper.getAttrDrawable(mContext, R.drawable.qmui_divider_bottom_bitmap));
            mEditText.setHint("输入金额");
            mEditText.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
            mEditText.setText(money);
            LinearLayout.LayoutParams editTextLP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, QMUIDisplayHelper.dpToPx(50));
            editTextLP.bottomMargin = QMUIDisplayHelper.dp2px(getContext(), 15);
            mEditText.setLayoutParams(editTextLP);
            layout.addView(mEditText);

            return layout;
        }
    }

}
