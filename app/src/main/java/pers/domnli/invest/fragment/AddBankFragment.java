package pers.domnli.invest.fragment;

import android.os.AsyncTask;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pers.domnli.invest.InvestApplication;
import pers.domnli.invest.R;
import pers.domnli.invest.base.BaseFragment;
import pers.domnli.invest.repository.entity.Bank;

public class AddBankFragment extends BaseFragment {

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;

    @BindView(R.id.groupListView)
    QMUIGroupListView mGroupList;

    private String[] mBanks = new String[]{"工商银行","农业银行","中国银行","建设银行","交通银行","招商银行","浦发银行","民生银行","中信银行","光大银行","兴业银行","平安银行","广发银行","华夏银行","汇丰银行","恒丰银行","浙商银行","渤海银行","北京银行","上海银行","杭州银行","南京银行","宁波银行","广州银行","邮政储蓄银行","上海农村商业银行","深圳农村商业银行"};
    private String[] mDays = new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};

    private QMUICommonListItemView mBankItem;
    private QMUICommonListItemView mTailItem;
    private QMUICommonListItemView mBillingDayItem;
    private QMUICommonListItemView mDueDayItem;
    private QMUICommonListItemView mQuotaItem;
    private QMUICommonListItemView mCurrentDaySelectItem;
    private QMUIDialog mBankDialog;
    private QMUIDialog mDayDialog;

    @Override
    protected View onCreateView() {
        FrameLayout layout = (FrameLayout) LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_bank, null);
        ButterKnife.bind(this, layout);

        initTopBar();
        initView();
        return layout;
    }

    private void initTopBar() {

        mTopBar.addLeftBackImageButton().setOnClickListener(v->popBackStack());

        mTopBar.addRightTextButton("保存",R.id.topbar_right_change_button).setOnClickListener(v->save());

        mTopBar.setTitle("新增银行信用卡");
    }

    private void initView(){

        mBankItem = mGroupList.createItemView("银行");
        mBankItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        mTailItem = mGroupList.createItemView("卡尾号");
        mTailItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        mBillingDayItem = mGroupList.createItemView("账单日");
        mBillingDayItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        mDueDayItem = mGroupList.createItemView("还款日");
        mDueDayItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        mQuotaItem = mGroupList.createItemView("总额度");
        mQuotaItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);


        QMUIGroupListView.newSection(getContext())
                .setDescription("如果选择已经添加的银行，则视为修改;如果银行存在多张卡，并且每张卡需要独立还款，则卡尾号必填")
                .addItemView(mBankItem,v->mBankDialog.show())
                .addItemView(mTailItem,v->showTailDialog())
                .addTo(mGroupList);

        QMUIGroupListView.newSection(getContext())
                .addItemView(mBillingDayItem,v->{
                    mCurrentDaySelectItem = (QMUICommonListItemView)v;
                    mDayDialog.show();
                })
                .addItemView(mDueDayItem,v->{
                    mCurrentDaySelectItem = (QMUICommonListItemView)v;
                    mDayDialog.show();
                })
                .addTo(mGroupList);

        QMUIGroupListView.newSection(getContext())
                .setDescription("填写该银行所有卡的总额度")
                .addItemView(mQuotaItem,view -> showQuotaDialog())
                .addTo(mGroupList);

        mBankDialog = new QMUIDialog.CheckableDialogBuilder(getActivity())
                .addItems(mBanks, (dialog, which) -> {
                    mBankItem.setDetailText(mBanks[which]);
                    dialog.dismiss();
                })
                .create(R.style.QMUI_Dialog);

        mDayDialog = new QMUIDialog.CheckableDialogBuilder(getActivity())
                .addItems(mDays, (dialog, which) -> {
                    mCurrentDaySelectItem.setDetailText(mDays[which]);
                    dialog.dismiss();
                })
                .create(R.style.QMUI_Dialog);

    }

    private void showTailDialog(){
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(getActivity());
        builder.setTitle("卡尾号")
                .setPlaceholder("非必填；示例：2053")
                .setInputType(InputType.TYPE_CLASS_NUMBER)
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
                            mTailItem.setDetailText(text);
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getActivity(), "请填入卡号", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .create(R.style.QMUI_Dialog).show();
    }

    private void showQuotaDialog() {
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(getActivity());
        builder.setTitle("总额度")
                .setPlaceholder("示例：15000")
                .setInputType(InputType.TYPE_CLASS_NUMBER)
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
                            mQuotaItem.setDetailText(text);
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getActivity(), "请填入金额", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .create(R.style.QMUI_Dialog).show();
    }

    private void save() {
        AsyncTask.execute(()->{
            try {
                Bank bank = new Bank();
                if(mTailItem.getDetailText().toString().isEmpty()){
                    bank.setBank(mBankItem.getDetailText().toString());
                }else{
                    String name = mBankItem.getDetailText().toString() + mTailItem.getDetailText().toString();
                    bank.setBank(name);
                }
                bank.setBillingDay(Integer.valueOf(mBillingDayItem.getDetailText().toString()));
                bank.setDueDay(Integer.valueOf(mDueDayItem.getDetailText().toString()));
                bank.setQuota(Integer.valueOf(mQuotaItem.getDetailText().toString()));
                InvestApplication.getAppDataBase().dao().insert(bank);
                mTopBar.post(()->{
                    Toast.makeText(getContext(),"已添加",Toast.LENGTH_LONG).show();
                    mBankItem.setDetailText("");
                    mTailItem.setDetailText("");
                    mBillingDayItem.setDetailText("");
                    mDueDayItem.setDetailText("");
                    mQuotaItem.setDetailText("");
                });
            }catch (Exception e){
                mTopBar.post(()-> Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show());
            }
        });

    }

}
