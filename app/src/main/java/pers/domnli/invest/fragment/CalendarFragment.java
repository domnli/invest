package pers.domnli.invest.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pers.domnli.invest.R;
import pers.domnli.invest.base.BaseFragment;
import pers.domnli.invest.repository.entity.Bank;

public class CalendarFragment extends BaseFragment implements CalendarView.OnCalendarSelectListener, CalendarView.OnYearChangeListener {

    @BindView(R.id.tv_month_day)
    TextView mTextMonthDay;

    @BindView(R.id.tv_year)
    TextView mTextYear;

    @BindView(R.id.tv_lunar)
    TextView mTextLunar;

    @BindView(R.id.tv_current_day)
    TextView mTextCurrentDay;

    @BindView(R.id.calendarView)
    CalendarView mCalendarView;

    @BindView(R.id.fl_current)
    FrameLayout mFlCurrent;

    private int mYear;
    private int mCurDay;
    private CalendarViewModel mVm;
    private Map<String,Bank> mBankMap;


    @Override
    protected View onCreateView() {
        setStatusBarDarkMode();
        LinearLayout layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.fragment_calendar, null);
        ButterKnife.bind(this, layout);
        mVm = ViewModelProviders.of(this).get(CalendarViewModel.class);
        mBankMap = new HashMap<>();

        initView();
        observe();
        mVm.getAllBank();

        return layout;
    }

    private void initView(){

        mTextMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
        mTextLunar.setText("今日");
        mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));
        mTextMonthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.showYearSelectLayout(mYear);
                mTextLunar.setVisibility(View.GONE);
                mTextYear.setVisibility(View.GONE);
                mTextMonthDay.setText(String.valueOf(mYear));
            }
        });
        mFlCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.scrollToCurrent();
            }
        });
        mYear = mCalendarView.getCurYear();
        mCurDay = mCalendarView.getCurDay();

        mCalendarView.setOnCalendarSelectListener(this);
        mCalendarView.setOnYearChangeListener(this);
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));

    }

    private void observe(){
        mVm.banksLiveData.observe(getViewLifecycleOwner(), banks -> {
            initData(banks);
        });
    }

    private void initData(List<Bank> banks){
            Map<String, Calendar> map = new HashMap<>();
            int startYear = mCalendarView.getCurYear() - 3;
            // 前3年，后3年
            for (Bank bank :banks) {
                mBankMap.put(bank.getBank(),bank);
                for (int i = 0; i < 6; i++) {
                    for (int j = 1; j <= 12; j++) {
                        Calendar billingCal = new Calendar();
                        billingCal.setYear(startYear + i);
                        billingCal.setMonth(j);
                        billingCal.setDay(bank.getBillingDay());
                        if(map.containsKey(billingCal.toString())){
                            billingCal = map.get(billingCal.toString());
                        }else{
                            map.put(billingCal.toString(),billingCal);
                        }
                        billingCal.addScheme(ContextCompat.getColor(getContext(),R.color.billing_color),bank.getBank().length()>4?bank.getBank().replace("银行",""):bank.getBank());

                        Calendar dueCal = new Calendar();
                        dueCal.setYear(startYear + i);
                        dueCal.setMonth(j);
                        dueCal.setDay(bank.getDueDay());
                        if(map.containsKey(dueCal.toString())){
                            dueCal = map.get(dueCal.toString());
                        }else{
                            map.put(dueCal.toString(),dueCal);
                        }
                        dueCal.addScheme(ContextCompat.getColor(getContext(),R.color.due_color),bank.getBank().length()>4?bank.getBank().replace("银行",""):bank.getBank());

                    }
                }
            }

        mCalendarView.setSchemeDate(map);
    }

    @OnClick(R.id.add_bank_fl)
    void onAddBank(View v){
        showTopBarList(v);
    }

    private void showTopBarList(View v) {

        List<String> listItems = new ArrayList<>();

        listItems.add("银行卡");
        listItems.add("记账");
//        listItems.add("设置");

        ArrayAdapter adapter = new ArrayAdapter<>(getContext(), R.layout.simple_list_item, listItems);

        QMUIPopups.listPopup(getContext(),
                QMUIDisplayHelper.dp2px(getContext(), 80),
                QMUIDisplayHelper.dp2px(getContext(), 300),
                adapter, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position){
                            case 0:
                                startFragmentAndDestroyCurrent(new AddBankFragment());
                                break;
                            case 1:
                                startFragment(new RecordFragment());
                                break;
                        }
                    }
                })
                .animStyle(QMUIPopup.ANIM_GROW_FROM_CENTER)
                .preferredDirection(QMUIPopup.DIRECTION_BOTTOM)
                .arrow(false)
                .shadow(true)
                .offsetX(-QMUIDisplayHelper.dp2px(getContext(), 20))
                .offsetYIfBottom(-QMUIDisplayHelper.dp2px(getContext(), 30))
                .show(v);
    }

    @OnClick(R.id.alarm_fl)
    void onAlarm(View v){
        startFragmentAndDestroyCurrent(new BillRemindFragment());
    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        mTextLunar.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mTextLunar.setText(calendar.isCurrentDay()?"今日":"");
        mYear = calendar.getYear();

        if(calendar.hasScheme() && isClick){
            if(calendar.getSchemes().size() == 1){
                toLoan(calendar.getSchemes().get(0).getScheme(),calendar.getYear(),calendar.getMonth(),calendar.getDay());
                return;
            }
            final String[] items = new String[calendar.getSchemes().size()];
            for (int i = 0; i < calendar.getSchemes().size(); i++) {
                items[i] = calendar.getSchemes().get(i).getScheme();
            }

            new QMUIDialog.MenuDialogBuilder(getActivity())
                    .addItems(items, (dialog, which) -> {
                        dialog.dismiss();
                        toLoan(items[which],calendar.getYear(),calendar.getMonth(),calendar.getDay());
                    })
                    .create(R.style.QMUI_Dialog).show();
        }
    }

    private void toLoan(String bank,Integer year,Integer month,Integer day){
        Bank target = mBankMap.get(bank);
        Bundle args = new Bundle();
        args.putString(LoanFragment.ARGS_BANK,bank);
        args.putInt(LoanFragment.ARGS_YEAR,year);
        if(target.getBillingDay() > target.getDueDay() && mCurDay <= target.getBillingDay()){
            args.putInt(LoanFragment.ARGS_MONTH,month-1);
        }else{
            args.putInt(LoanFragment.ARGS_MONTH,month);
        }
        args.putInt(LoanFragment.ARGS_DAY,day);
        LoanFragment fragment = new LoanFragment();
        fragment.setArguments(args);
        startFragment(fragment);
    }

    @Override
    public void onYearChange(int year) {
        mTextMonthDay.setText(String.valueOf(year));
    }
}
