package tony.com.googleplay.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

import tony.com.googleplay.R;
import tony.com.googleplay.domain.AppInfo;
import tony.com.googleplay.http.protocol.HomeDetailProtocol;
import tony.com.googleplay.ui.holder.DetailAppHolder;
import tony.com.googleplay.ui.holder.DetailDesInfoHolder;
import tony.com.googleplay.ui.holder.DetailDownLoadHolder;
import tony.com.googleplay.ui.holder.DetailPicInfoHolder;
import tony.com.googleplay.ui.holder.DetailSafeInfoHolder;
import tony.com.googleplay.ui.widget.LoadingPage;
import tony.com.googleplay.utils.UIUtils;

/**
 *
 * Created by Administrator on 2017/5/3.
 */

public class HomeDetailActivity extends BaseActivity {

    private AppInfo mData;
    private LoadingPage mLoadingPage;
    private String packageName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLoadingPage = new LoadingPage(UIUtils.getContext()) {
            @Override
            public View onCreateSuccessView() {

                return HomeDetailActivity.this.onCreateSuccessView();
            }

            @Override
            public ResultState onLoad() {
                return HomeDetailActivity.this.onLoad();
            }
        };
        setContentView(mLoadingPage);

        //获取应用的包名
        packageName=getIntent().getStringExtra("packageName");

        mLoadingPage.loadData();

        initActionBar();
    }
    private View onCreateSuccessView(){
        View view = UIUtils.inflat(R.layout.layout_home_detail);
        //动态的添加 应用信息页面
        FrameLayout flDetailAppInfo = (FrameLayout) view.findViewById(R.id.fl_detail_appInfo);
        DetailAppHolder appInfoHolder=new DetailAppHolder();
        flDetailAppInfo.addView(appInfoHolder.getRootView());
        appInfoHolder.setData(mData);//设置数据
        //动态添加应用安全页面
        FrameLayout flDetailAppSafe= (FrameLayout) view.findViewById(R.id.fl_detail_safeInfo);
        DetailSafeInfoHolder detailSafeInfoHolder=new DetailSafeInfoHolder();
        flDetailAppSafe.addView(detailSafeInfoHolder.getRootView());
        detailSafeInfoHolder.setData(mData);
        //动态添加应用截图页面
        HorizontalScrollView horizontalScrollView= (HorizontalScrollView) view.findViewById(R.id.hsv_detail_picInfo);
        DetailPicInfoHolder detailPicInfoHolder=new DetailPicInfoHolder();
        horizontalScrollView.addView(detailPicInfoHolder.getRootView());
        detailPicInfoHolder.setData(mData);
        //动态添加应用描述页面
        FrameLayout flDetailDesInfo= (FrameLayout) view.findViewById(R.id.fl_detail_desInfo);
        DetailDesInfoHolder detailDesInfoHolder=new DetailDesInfoHolder();
        flDetailDesInfo.addView(detailDesInfoHolder.getRootView());
        detailDesInfoHolder.setData(mData);
        //添加下载模块的页面
        FrameLayout flDetailDownLoad= (FrameLayout) view.findViewById(R.id.fl_detail_download);
        DetailDownLoadHolder detailDownLoadHolder=new DetailDownLoadHolder();
        flDetailDownLoad.addView(detailDownLoadHolder.getRootView());
        detailDownLoadHolder.setData(mData);

        return view;
    }
    /**
     * 初始化actionBar
     */
    private void initActionBar() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();


        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true); //设置返回键可用

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private LoadingPage.ResultState onLoad(){
        HomeDetailProtocol protocol=new HomeDetailProtocol(packageName);
        mData = protocol.getData(0);
//        System.out.println("data from protocol=============");
//        System.out.println(mData);
        if(mData!=null){
           return LoadingPage.ResultState.STATE_SUCCESS;
        }else {
            return LoadingPage.ResultState.STATE_ERROR;
        }
    }
}
