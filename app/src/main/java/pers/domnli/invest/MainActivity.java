package pers.domnli.invest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.arch.QMUIFragmentActivity;
import com.qmuiteam.qmui.arch.annotation.DefaultFirstFragment;
import com.qmuiteam.qmui.arch.annotation.FirstFragments;
import com.qmuiteam.qmui.arch.annotation.LatestVisitRecord;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import pers.domnli.invest.base.BaseFragmentActivity;
import pers.domnli.invest.fragment.BillRemindFragment;
import pers.domnli.invest.fragment.CalendarFragment;
import pers.domnli.invest.fragment.WebExplorerFragment;


@FirstFragments(
        value = {
                CalendarFragment.class,
                BillRemindFragment.class
        })
@DefaultFirstFragment(CalendarFragment.class)
@LatestVisitRecord
public class MainActivity extends BaseFragmentActivity {

    @Override
    protected int getContextViewId() {
        return R.id.invest;
    }


    public static Intent createWebExplorerIntent(Context context, String url, String title) {
        Bundle bundle = new Bundle();
        bundle.putString(WebExplorerFragment.EXTRA_URL, url);
        bundle.putString(WebExplorerFragment.EXTRA_TITLE, title);

        return of(context, WebExplorerFragment.class, bundle);
    }

    public static Intent of(@NonNull Context context,
                            @NonNull Class<? extends QMUIFragment> firstFragment) {
        return QMUIFragmentActivity.intentOf(context, MainActivity.class, firstFragment);
    }

    public static Intent of(@NonNull Context context,
                            @NonNull Class<? extends QMUIFragment> firstFragment,
                            @Nullable Bundle fragmentArgs) {
        return QMUIFragmentActivity.intentOf(context, MainActivity.class, firstFragment, fragmentArgs);
    }
}
