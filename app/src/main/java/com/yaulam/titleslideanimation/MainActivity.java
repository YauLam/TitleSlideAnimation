package com.yaulam.titleslideanimation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private CustomScrollView svContent;
    private float searchMinMargin, searchMaxMargin, searchMaxWidth, searchMinWidth;
    private float addressMaxWidth, addressMinWidth;
    private TextView tvSearchInput, tvAddress;
    private ViewGroup.MarginLayoutParams searchLayoutParams, addressLayoutParams;
    private LinearLayout llOldView, llNewView, llView;
    private int mOldViewTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        llOldView = (LinearLayout) findViewById(R.id.ll_old_view);
        llNewView = (LinearLayout) findViewById(R.id.ll_new_view);
        llView = (LinearLayout) findViewById(R.id.ll_view);
        svContent = (CustomScrollView) findViewById(R.id.sv_content);
        svContent.setOnCustomScrollListener(mCustomListener);
        tvSearchInput = (TextView) findViewById(R.id.tv_search_input);
        tvAddress = (TextView) findViewById(R.id.tv_address);

        addressLayoutParams = (ViewGroup.MarginLayoutParams) tvAddress.getLayoutParams();
        addressMaxWidth = dip2px(330f);
        addressMinWidth = dip2px(150f);

        searchLayoutParams = (ViewGroup.MarginLayoutParams) tvSearchInput.getLayoutParams();
        //父布局是RelativeLayout,搜索框的顶部据父控件的顶部，布局方式用margin撑开
        searchMinMargin = dip2px(0f);
        searchMaxMargin = dip2px(34f);
        //屏幕宽度 360dp-（布局文件中两边的margin15+15）
        searchMaxWidth = dip2px(330f);
        searchMinWidth = dip2px(170f);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            mOldViewTop = llOldView.getTop();
        }
    }

    CustomScrollView.CustomScrollViewListener mCustomListener = new CustomScrollView.CustomScrollViewListener() {
        @Override
        public void onScrollStatus(int y) {
            if (y > 0){
                //地址部分
                float newAddressWidth = addressMaxWidth - 4 * y;
                if (newAddressWidth < addressMinWidth) {
                    newAddressWidth = addressMinWidth;
                }
                addressLayoutParams.width = (int) newAddressWidth;
                tvAddress.setLayoutParams(addressLayoutParams);

                //搜索框部分
                float newSearchWidth = searchMaxWidth - 4 * y;
                if (newSearchWidth < searchMinWidth) {
                    newSearchWidth = searchMinWidth;
                }
                float newSearchTopMargin = searchMaxMargin - (float) (0.3 * y);
                if (newSearchTopMargin < searchMinMargin) {
                    newSearchTopMargin = searchMinMargin;
                }
                searchLayoutParams.topMargin = (int) newSearchTopMargin;
                searchLayoutParams.width = (int) newSearchWidth;
                tvSearchInput.setLayoutParams(searchLayoutParams);
            }else{
                //地址部分
                addressLayoutParams.width = (int) addressMaxWidth;
                tvAddress.setLayoutParams(addressLayoutParams);

                //搜索框部分
                searchLayoutParams.topMargin = (int) searchMaxMargin;
                searchLayoutParams.width = (int) searchMaxWidth;
                tvSearchInput.setLayoutParams(searchLayoutParams);
            }

            if (y >= mOldViewTop) {
                if (llView.getParent() != llNewView) {
                    llOldView.removeView(llView);
                    llNewView.addView(llView);
                }
            } else {
                if (llView.getParent() != llOldView) {
                    llNewView.removeView(llView);
                    llOldView.addView(llView);
                }
            }
        }
    };

    /**
     * 一般情况，项目中都需要有一个dp和px相互转换的工具类
     * 这个方法可以写在工具类中
     * @param var1
     * @return
     */
    public int dip2px(float var1) {
        float var2 = this.getResources().getDisplayMetrics().density;
        return (int)(var1 * var2 + 0.5F);
    }
}
