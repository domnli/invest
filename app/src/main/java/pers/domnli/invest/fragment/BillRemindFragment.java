package pers.domnli.invest.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import java.util.Calendar;

import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import pers.domnli.invest.R;
import pers.domnli.invest.base.BaseFragment;
import pers.domnli.invest.repository.entity.Bank;

public class BillRemindFragment extends BaseFragment {

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;

    @BindView(R.id.groupListView)
    QMUIGroupListView mGroupList;

    private BillRemindViewModel mVm;
    private QMUIGroupListView.Section mDueSection;
    private QMUIGroupListView.Section mBillingSection;

    @Override
    protected View onCreateView() {
        FrameLayout layout = (FrameLayout) LayoutInflater.from(getActivity()).inflate(R.layout.fragment_bill_remind, null);
        ButterKnife.bind(this, layout);
        mVm = ViewModelProviders.of(this).get(BillRemindViewModel.class);

        initTopBar();
        initView();
        observe();

        mVm.getRemind();

        return layout;
    }

    private void initTopBar() {

        mTopBar.addLeftBackImageButton().setOnClickListener(v->popBackStack());

        mTopBar.setTitle("账单提醒");
    }

    private void initView(){
        mDueSection = mGroupList.newSection(getContext()).setTitle("临近还款日的银行");
        mBillingSection = mGroupList.newSection(getContext()).setTitle("可记录账单金额的银行");
    }

    private void observe(){
        Calendar calendar = Calendar.getInstance();
        Integer year = calendar.get(Calendar.YEAR);
        Integer month = calendar.get(Calendar.MONTH)+1;
        mVm.remindLiveData.observe(getViewLifecycleOwner(), remind -> {
            for (Bank bank :
                    remind.getDueBanks()) {
                QMUICommonListItemView item = mGroupList.createItemView(bank.getBank());
                item.setDetailText(year + "-"+month+"-"+bank.getDueDay());
                item.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
                mDueSection.addItemView(item, v -> toLoan(bank.getBank(),year,month,bank.getDueDay()));
            }

            for (Bank bank :
                    remind.getBillingBanks()) {
                QMUICommonListItemView item = mGroupList.createItemView(bank.getBank());
                item.setDetailText(year + "-"+month+"-"+bank.getBillingDay());
                item.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
                mBillingSection.addItemView(item, v -> toLoan(bank.getBank(),year,month,bank.getBillingDay()));
            }

            mDueSection.addTo(mGroupList);
            mBillingSection.addTo(mGroupList);

        });
    }

    private void toLoan(String bank,Integer year,Integer month,Integer day){
        Bundle args = new Bundle();
        args.putString(LoanFragment.ARGS_BANK,bank);
        args.putInt(LoanFragment.ARGS_YEAR,year);
        args.putInt(LoanFragment.ARGS_MONTH,month);
        args.putInt(LoanFragment.ARGS_DAY,day);
        LoanFragment fragment = new LoanFragment();
        fragment.setArguments(args);
        startFragment(fragment);
    }

}
