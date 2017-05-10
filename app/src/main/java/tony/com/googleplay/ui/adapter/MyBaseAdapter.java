package tony.com.googleplay.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import tony.com.googleplay.manager.ThreadManager;
import tony.com.googleplay.ui.holder.BaseHolder;
import tony.com.googleplay.ui.holder.MoreHolder;
import tony.com.googleplay.utils.UIUtils;

/**
 *
 * Created by Administrator on 2017/4/9.
 */

public abstract class MyBaseAdapter <T> extends BaseAdapter {
   private ArrayList<T> list;
    public MyBaseAdapter(ArrayList<T> list) {
        this.list=list;
    }
    public static final int TYPE_NORMAL=1;
    public static final int TYPE_MORE=0;

    @Override
    public int getCount() {
        return list.size()+1;//增加一个布局
    }

    @Override
    public T getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    /**
     * 返回普通布局类型, 子类可以重写此方法来返回多种普通布局类型
     * @param position
     * @return
     */

    @Override
    public int getItemViewType(int position) {
        if(position==list.size()){
            return TYPE_MORE;
        }else {
            return getInnerType(position);
        }
    }

    /**
     *
     * @return
     */
    public int getInnerType(int position){
        return TYPE_NORMAL;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseHolder holder=null;
        if(convertView==null){
            if(getItemId(position)!=list.size()){
                holder=getHolder(position);
            }else {
                holder=new MoreHolder(hasMore());
            }

        }else {
            holder=(BaseHolder) convertView.getTag();
        }
        //4.刷新控件数据
        if(getItemId(position)!=list.size()){
            holder.setData(getItem(position));
        }else {
            //加载下一页数据
            MoreHolder loadNext= (MoreHolder) holder;
            if(loadNext.getData()==MoreHolder.TYPE_HAS_MORE){
                loadMore(loadNext);
            }
        }
        return holder.getRootView();
    }

    private void loadMore(final MoreHolder loadNext) {
        ThreadManager.getThreadPoor().excute(new Runnable() {
            @Override
            public void run() {
                {
                    //加载下一页数据
                    final ArrayList<T> moreData = onLoadMore();
                    UIUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            if(moreData!=null){
                                //每页数据有20条
                                //数据不足20条
                                if(moreData.size()<20){
                                    loadNext.setData(MoreHolder.TYPE_NO_MORE);
                                }else {
                                    loadNext.setData(MoreHolder.TYPE_HAS_MORE);
                                }
                                list.addAll(moreData);
                                notifyDataSetChanged();
                            }else {
                                loadNext.setData(MoreHolder.TYPE_MORE_ERROR);
                            }
                        }
                    });

                }
            }
        });
    }

    /**
     *加载更多数据由子类实现
     * @return
     */

    public abstract ArrayList<T> onLoadMore();
    /**
     * 是否可以加载更多，子类重写
     * @return
     */
    public boolean hasMore(){
        return true;
    }
    public abstract BaseHolder<T> getHolder(int position) ;
    public int getSize(){
        return list.size();
    }
}
