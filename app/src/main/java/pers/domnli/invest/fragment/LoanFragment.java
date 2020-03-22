package pers.domnli.invest.fragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.qmuiteam.qmui.layout.QMUILinearLayout;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import pers.domnli.invest.InvestApplication;
import pers.domnli.invest.R;
import pers.domnli.invest.base.BaseFragment;
import pers.domnli.invest.repository.entity.LoanMonthly;

public class LoanFragment extends BaseFragment {

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;

    @BindView(R.id.card_layout)
    QMUILinearLayout mCardLayout;

    @BindView(R.id.groupListView)
    QMUIGroupListView mGroupList;

    @BindView(R.id.date_tv)
    TextView mDateTv;

    @BindView(R.id.repaid_cb)
    CompoundButton mRepaidCb;

    @BindView(R.id.loan_tv)
    TextView mLoanTv;

    @BindView((R.id.loan_edit))
    View mEditor;

    @BindView(R.id.img_repaid)
    ImageView mImgRepaid;

    public final static String ARGS_BANK = "args.bank.name";
    public final static String ARGS_YEAR = "args.year";
    public final static String ARGS_MONTH = "args.month";
    public final static String ARGS_DAY = "args.day";

    private LoanViewModel mVm;
    private String mBank;
    private Integer mYear;
    private Integer mMonth;
    private Integer mDay;
    private LoanMonthly mLoanMonthly;

    private QMUICommonListItemView mBillingDayItem;
    private QMUICommonListItemView mDueDayItem;
    private QMUICommonListItemView mQuotaItem;

    @Override
    protected View onCreateView() {
        Bundle args = getArguments();
        if(args != null){
            mBank = args.getString(ARGS_BANK);
            mYear = args.getInt(ARGS_YEAR);
            mMonth = args.getInt(ARGS_MONTH);
            mDay = args.getInt(ARGS_DAY);
        }
        FrameLayout layout = (FrameLayout) LayoutInflater.from(getActivity()).inflate(R.layout.fragment_loan, null);
        ButterKnife.bind(this, layout);
        mVm = ViewModelProviders.of(this).get(LoanViewModel.class);

        initTopBar();
        initView();
        observe();
        return layout;
    }

    @Override
    protected void onViewCreated(@NonNull View rootView) {
        super.onViewCreated(rootView);
        rootView.post(()->{
            if(mBank == null || mYear == null || mMonth == null || mDay == null){
                popBackStack();
            }else {
                mVm.getBankLoanMonthly(mBank,mYear,mMonth);
            }
        });
    }

    private void initTopBar() {

        mTopBar.addLeftBackImageButton().setOnClickListener(v->popBackStack());
        mTopBar.addRightImageButton(R.mipmap.icon_topbar_overflow, R.id.topbar_right_change_button)
                .setOnClickListener(v -> showTopBarList(v));

        mTopBar.setTitle(mBank);
    }

    private void showTopBarList(View v) {
        String[] listItems = new String[]{
                "记帐"
        };
        List<String> data = new ArrayList<>();

        Collections.addAll(data, listItems);

        ArrayAdapter adapter = new ArrayAdapter<>(getContext(), R.layout.simple_list_item, data);

        QMUIPopups.listPopup(getContext(),
                QMUIDisplayHelper.dp2px(getContext(), 80),
                QMUIDisplayHelper.dp2px(getContext(), 300),
                adapter,
                null)
                .animStyle(QMUIPopup.ANIM_GROW_FROM_CENTER)
                .preferredDirection(QMUIPopup.DIRECTION_BOTTOM)
                .arrow(false)
                .shadow(true)
                .offsetX(-QMUIDisplayHelper.dp2px(getContext(), 20))
                .offsetYIfBottom(-QMUIDisplayHelper.dp2px(getContext(), 30))
                .onDismiss(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        Toast.makeText(getContext(), "onDismiss", Toast.LENGTH_SHORT).show();
                    }
                })
                .show(v);
    }

    private void initView(){
        mCardLayout.setRadiusAndShadow(QMUIDisplayHelper.dp2px(getContext(), 15),14,0.5f);
        mDateTv.setText(dateFormat());

        mBillingDayItem = mGroupList.createItemView("账单日");
        mBillingDayItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_NONE);

        mDueDayItem = mGroupList.createItemView("还款日");
        mDueDayItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_NONE);

        QMUICommonListItemView crtDayItem = mGroupList.createItemView("今日");
        crtDayItem.setDetailText(String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)));
        mDueDayItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_NONE);

        mQuotaItem = mGroupList.createItemView("总额度");
        mQuotaItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_NONE);


        QMUIGroupListView.newSection(getContext())
                .addItemView(mBillingDayItem,null)
                .addItemView(mDueDayItem,null)
                .addItemView(crtDayItem,null)
                .addItemView(mQuotaItem,null)
                .addTo(mGroupList);
    }

    @SuppressLint("DefaultLocale")
    private String dateFormat(){
        return String.format("%d年%d月", mYear,mMonth);
    }

    private void observe(){
        mVm.bankLiveData.observe(getViewLifecycleOwner(), bank -> {
            mBillingDayItem.setDetailText(bank.getBillingDay().toString());
            mDueDayItem.setDetailText(bank.getDueDay().toString());
            mQuotaItem.setDetailText(bank.getQuota().toString());
        });

        mVm.loanMonthlyLiveData.observe(getViewLifecycleOwner(),loanMonthly -> {
            mDateTv.setText(dateFormat());
            mLoanMonthly = loanMonthly;
            drawLoanMonthlyView();
        });
    }

    private void drawLoanMonthlyView(){
        if(mLoanMonthly == null){
            mRepaidCb.setChecked(false);
            mCardLayout.setAlpha(1f);
            mImgRepaid.setVisibility(View.GONE);
            mLoanTv.setText("");
        }else{
            mLoanTv.setText(mLoanMonthly.getLoan());
            if(mLoanMonthly.getRepaid()){
                mRepaidCb.setChecked(true);
                mCardLayout.setAlpha(0.5f);
                mEditor.setVisibility(View.GONE);
                mImgRepaid.setVisibility(View.VISIBLE);

            }else {
                mRepaidCb.setChecked(false);
                mCardLayout.setAlpha(1f);
                mEditor.setVisibility(View.VISIBLE);
                mImgRepaid.setVisibility(View.GONE);
            }
        }

    }


    @OnCheckedChanged(R.id.repaid_cb)
    void repaid(CompoundButton cb, boolean checked){
        if(!cb.isPressed()){
            return;
        }
        if(mLoanMonthly == null){
            cb.setChecked(false);
            return;
        }
        if(checked){
            mLoanMonthly.setRepaid(true);
            mVm.updateLoanMonthly(mLoanMonthly);
            drawLoanMonthlyView();
        }else{
            new QMUIDialog.MessageDialogBuilder(getActivity())
                    .setTitle("确认")
                    .setMessage("确定要重置为未还款吗？")
                    .setCanceledOnTouchOutside(false)
                    .addAction("取消", new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            cb.setChecked(true);
                            dialog.dismiss();
                        }
                    })
                    .addAction(0, "确认", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            mLoanMonthly.setRepaid(false);
                            mVm.updateLoanMonthly(mLoanMonthly);
                            drawLoanMonthlyView();
                            dialog.dismiss();
                        }
                    })
                    .create(R.style.QMUI_Dialog).show();
        }
    }

    @OnClick(R.id.prev_month)
    void prevMonth(){
        mLoanMonthly = null;
        Calendar calendar = Calendar.getInstance();
        calendar.set(mYear,mMonth-1,mDay);
        calendar.add(Calendar.MONTH,-1);
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH) + 1;
        mDateTv.setText(dateFormat());

        mVm.getBankLoanMonthly(mBank,mYear,mMonth);
    }

    @OnClick(R.id.next_month)
    void nextMonth(){
        mLoanMonthly = null;
        Calendar calendar = Calendar.getInstance();
        calendar.set(mYear,mMonth-1,mDay);
        calendar.add(Calendar.MONTH,1);
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH) + 1;
        mDateTv.setText(dateFormat());

        mVm.getBankLoanMonthly(mBank,mYear,mMonth);
    }

    @OnClick({R.id.loan_tv,R.id.loan_edit})
    void showDueDialog() {
        if(mLoanMonthly != null && mLoanMonthly.getRepaid()){
            return;
        }
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(getActivity());
        builder.setDefaultText(mLoanTv.getText());
        builder.setTitle("账单金额")
                .setPlaceholder("示例：15000")
                .setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        CharSequence text = builder.getEditText().getText();
                        if (text != null && text.length() > 0) {
                            String due = new BigDecimal(text.toString()).toString();
                            if(mLoanMonthly != null){
                                mLoanMonthly.setLoan(due);
                            }
                            mVm.insertLoanMonthly(mBank,mYear,mMonth,due);
                            mLoanTv.setText(due);
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getActivity(), "请填入金额", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .create(R.style.QMUI_Dialog).show();
    }

}
