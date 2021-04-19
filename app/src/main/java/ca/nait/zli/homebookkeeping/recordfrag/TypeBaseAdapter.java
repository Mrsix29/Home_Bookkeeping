package ca.nait.zli.homebookkeeping.recordfrag;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

import ca.nait.zli.homebookkeeping.R;
import ca.nait.zli.homebookkeeping.db.TypeBean;

public class TypeBaseAdapter extends BaseAdapter {
    Context context;
    List<TypeBean> data;
    int selectPos = 0;  //选中位置
    public TypeBaseAdapter(Context context, List<TypeBean> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_fragment_record_grid_view,parent,false);
        //find related view from layout
        ImageView imageView = convertView.findViewById(R.id.item_fragment_record_image_view);
        TextView textView = convertView.findViewById(R.id.item_fragment_record_text_view);
        //get data from the position
        TypeBean typeBean = data.get(position);
        textView.setText(typeBean.getTypename());
//        check the position if clicked
        if (selectPos == position) {
            imageView.setImageResource(typeBean.getSelectedImageId());
        }else{
            imageView.setImageResource(typeBean.getImageId());
        }
        return convertView;
    }
}
