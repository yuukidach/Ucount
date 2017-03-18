package com.yuukidach.ucount;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuukidach on 17-3-12.
 */

public class CostFragment extends Fragment {

    private String[] titles = {"一般", "用餐", "零食", "交通", "充值", "购物", "娱乐", "住房", "饮料", "网购",
            "鞋帽", "护肤", "化妆", "电影", "转账", "浪费", "健身", "医疗", "旅游", "教育", "香烟", "酒水", "数码", "捐献",
            "家庭", "宠物", "服装", "日用", "水果", "母婴", "信用卡", "股票", "工作", "家具", "通信"};
    private ViewPager mPager;
    private List<View> mPagerList;
    private List<IOItem> mDatas;
    private LinearLayout mLlDot;
    private LayoutInflater inflater;
    private ImageView itemImage;
    private TextView itemTitle;
    private LinearLayout itemLayout;

    // 总的页数
    private int pageCount;

    // 每一页显示的个数
    private int pageSize = 18;

    // 当前显示的是第几页
    private int curIndex = 0;

    private static final String TAG = "CostFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // 获得AddItemActivity对应的控件，用来提示已选择的项目类型
        getBannerId();

        View view = inflater.inflate(R.layout.cost_fragment, container, false);

        mPager = (ViewPager) view.findViewById(R.id.viewpager_1);
        mLlDot = (LinearLayout) view.findViewById(R.id.ll_dot_1);

        // 初始化数据源
        initDatas();

        // 初始化上方banner
        changeBanner(mDatas.get(0));

        // 总的页数=总数/每页数量，并取整
        pageCount = (int) Math.ceil(mDatas.size() * 1.0 / pageSize);
        mPagerList = new ArrayList<View>();
        for (int i = 0; i < pageCount; i++) {
            // 每个页面都是inflate出一个新实例
            GridView gridView = (GridView) inflater.inflate(R.layout.item_grid, mPager, false);
            gridView.setAdapter(new GridViewAdapter(getActivity(), mDatas, i, pageSize));

            mPagerList.add(gridView);

            // 对gridView添加item监听
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    changeBanner(mDatas.get( (int)id ));
                }
            });

        }
        // 设置圆点
        setOvalLayout(inflater, mLlDot);
        // 设置适配器
        mPager.setAdapter(new ViewPagerAdapter(mPagerList));

        return view;
    }

    /**
     * 初始化数据源
     */
    private void initDatas() {
        mDatas = new ArrayList<IOItem>();
        for (int i = 1; i <= titles.length; i++) {
            // 动态获取资源ID，第一个参数是资源名，第二个参数是资源类型例如drawable，string等，第三个参数包名
            int imageId = getResources().getIdentifier("type_big_" + i, "drawable", MainActivity.PACKAGE_NAME);
            mDatas.add(new IOItem(imageId, titles[i-1]));
        }
    }

    /**
     * 设置圆点
     */
    public void setOvalLayout(LayoutInflater inflater, ViewGroup container) {
        for (int i = 0; i < pageCount; i++) {
            mLlDot.addView(inflater.inflate(R.layout.dot, container, false));
        }
        // 默认显示第一页
        mLlDot.getChildAt(0).findViewById(R.id.v_dot)
                .setBackgroundResource(R.drawable.dot_selected);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageSelected(int position) {
                // 取消圆点选中
                mLlDot.getChildAt(curIndex)
                        .findViewById(R.id.v_dot)
                        .setBackgroundResource(R.drawable.dot_normal);
                // 圆点选中
                mLlDot.getChildAt(position)
                        .findViewById(R.id.v_dot)
                        .setBackgroundResource(R.drawable.dot_selected);
                curIndex = position;
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    // 获得AddItemActivity对应的控件，用来提示已选择的项目类型
    public void getBannerId() {
        itemImage = (ImageView) getActivity().findViewById(R.id.chosen_image);
        itemTitle = (TextView) getActivity().findViewById(R.id.chosen_title);
        itemLayout = (LinearLayout) getActivity().findViewById(R.id.have_chosen);
    }

    // 改变banner状态
    public void changeBanner(IOItem tmpItem) {
        Bitmap bm = BitmapFactory.decodeResource(getResources(), tmpItem.getSrcId());
        Palette.Builder pb = new Palette.Builder(bm);
        pb.maximumColorCount(1);

        itemImage.setImageResource(tmpItem.getSrcId());
        itemTitle.setTag(-tmpItem.getSrcId());      // 保留图片资源id作为标签，方便以后调用
        itemTitle.setText(tmpItem.getName());

        // 获取图片颜色并改变上方banner的背景色
        pb.generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch swatch = palette.getSwatches().get(0);
                if (swatch != null) {
                    itemLayout.setBackgroundColor(swatch.getRgb());
                } else {
                    Log.d(TAG, "changeBanner: ");
                }
            }
        });
    }
}