package com.yuukidach.ucount;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private List<IOItem> mDatas;
    private LayoutInflater inflater;

    // 页数下标,从0开始(当前是第几页)
    private int curIndex;

    // 每一页显示的个数
    private int pageSize;

    public GridViewAdapter(Context context, List<IOItem> mDatas, int curIndex, int pageSize) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.mDatas = mDatas;
        this.curIndex = curIndex;
        this.pageSize = pageSize;
    }

    /**
     * 先判断数据集的大小是否足够显示满本页 mDatas.size() > (curIndex+1)*pageSize,
     * 如果够，则直接返回每一页显示的最大条目个数pageSize,
     * 如果不够，则有几项返回几,(mDatas.size() - curIndex * pageSize);(也就是最后一页的时候就显示剩余item)
     */
    @Override
    public int getCount() {
        return mDatas.size() > (curIndex + 1) * pageSize ? pageSize : (mDatas.size() - curIndex * pageSize);

    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position + curIndex * pageSize);
    }

    @Override
    public long getItemId(int position) {
        return position + curIndex * pageSize;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.chose_io_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tv = (TextView) convertView.findViewById(R.id.item_grid_title);
            viewHolder.iv = (ImageView) convertView.findViewById(R.id.item_grid_icon);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        /**
         * 为GridView中的每个item设置高度
         */
        int height = parent.getHeight();
        int width = parent.getWidth();
        //得到GridView每一项的高度与宽度
        GridView.LayoutParams params = new GridView.LayoutParams(width / 6,
                height / 4 + 20);
        //设置每一行的高度和宽度
        convertView.setLayoutParams(params);

        /**
         * 重新确定position.
         * 在给View绑定显示的数据时，计算正确的position = position + curIndex * pageSize，
         * （因为拿到的是总的数据源，数据源是分页加载到每页的GridView上的，为了确保能正确的点对不同页上的item)
         */
        int pos = position + curIndex * pageSize;
        viewHolder.tv.setText(mDatas.get(pos).getName());
        viewHolder.iv.setImageResource(mDatas.get(pos).getSrcId());

        return convertView;
    }


    class ViewHolder {
        public TextView tv;
        public ImageView iv;
    }
}