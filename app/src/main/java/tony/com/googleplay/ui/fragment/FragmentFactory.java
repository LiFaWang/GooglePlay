package tony.com.googleplay.ui.fragment;

import android.support.v4.app.Fragment;

import java.util.HashMap;

/**
 * 生成fragment工厂
 * Created by Administrator on 2017/4/3.
 */

public class FragmentFactory {
    private static HashMap<Integer, BaseFragment> sFragmentMap = new HashMap<>();

    /**
     * 根据位置，生产对应的   fragment
     *
     * @param pos
     * @return
     */
    public static Fragment createFragment(int pos) {
        BaseFragment fragment = sFragmentMap.get(pos);
        if (fragment == null) {
            switch (pos) {
                case 0:
                    fragment = new HomeFragment();
                    break;
                case 1:
                    fragment = new AppFragment();
                    break;
                case 2:
                    fragment = new GameFragment();
                    break;
                case 3:
                    fragment = new SubFragment();
                    break;
                case 4:
                    fragment = new RecommendFragment();
                    break;
                case 5:
                    fragment = new CategoryFragment();
                    break;
                case 6:
                    fragment = new HotFragment();
                    break;

                default:
                    break;
            }
            sFragmentMap.put(pos,fragment);
        }
        return fragment;
    }
}
