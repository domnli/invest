package pers.domnli.invest.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.qmuiteam.qmui.widget.pullLayout.QMUIPullLayout;

import java.util.Timer;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import pers.domnli.invest.R;
import pers.domnli.invest.base.BaseFragment;
import pers.domnli.invest.base.BaseRecyclerAdapter;
import pers.domnli.invest.base.RecyclerViewHolder;
import pers.domnli.invest.repository.entity.BillingSerial;

public class BillingSerialFragment extends BaseFragment {

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;

    @BindView(R.id.pull_layout)
    QMUIPullLayout mPullLayout;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private BaseRecyclerAdapter<BillingSerial> mAdapter;
    private BillingSerialViewModel mVm;

    private String mBank;

    private Integer limit = 20;

    public final static String ARGS_BANK = "args.bank.name";

    @Override
    protected View onCreateView() {
        Bundle args = getArguments();
        if(args != null){
            mBank = args.getString(ARGS_BANK);
        }

        View layout = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_billing_serial, null);
        ButterKnife.bind(this, layout);
        mVm = ViewModelProviders.of(this).get(BillingSerialViewModel.class);

        initTopBar();
        initView();
        observe();

        return layout;
    }

    private void initTopBar() {

        mTopBar.addLeftBackImageButton().setOnClickListener(v->popBackStack());

        mTopBar.setTitle(mBank+"的账单流水");

    }

    private void initView(){
        mPullLayout.setActionListener(new QMUIPullLayout.ActionListener() {
            @Override
            public void onActionTriggered(@NonNull QMUIPullLayout.PullAction pullAction) {
                if(pullAction.getPullEdge() == QMUIPullLayout.PULL_EDGE_TOP){
//                    onRefreshData();
                }else if(pullAction.getPullEdge() == QMUIPullLayout.PULL_EDGE_BOTTOM){
                    mVm.loadMore(mBank,limit,mAdapter.getItemCount());
                }
                mPullLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullLayout.finishActionRun(pullAction);
                    }
                },3000);

            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });

        mAdapter = new BaseRecyclerAdapter<BillingSerial>(getContext(), null) {
            @Override
            public int getItemLayoutId(int viewType) {
                return android.R.layout.simple_list_item_1;
            }

            @Override
            public void bindData(RecyclerViewHolder holder, int position, BillingSerial item) {
                holder.setText(android.R.id.text1, item.getYear() + "." + item.getMonth() + "." + item.getDay() + " : " + item.getMoney() + "元");
            }
        };
        mAdapter.setOnItemClickListener((itemView, pos) -> Toast.makeText(getContext(), "click position=" + pos, Toast.LENGTH_SHORT).show());
        mRecyclerView.setAdapter(mAdapter);
        mVm.loadMore(mBank,limit,mAdapter.getItemCount());
    }

    private void observe(){
        mVm.serialLiveData.observe(getViewLifecycleOwner(),billingSerials -> {
            mAdapter.append(billingSerials);
        });
    }

}
