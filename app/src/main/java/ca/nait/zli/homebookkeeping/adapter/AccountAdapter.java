package ca.nait.zli.homebookkeeping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.Calendar;
import java.util.List;

import ca.nait.zli.homebookkeeping.R;
import ca.nait.zli.homebookkeeping.db.AccountBean;

public class AccountAdapter extends BaseAdapter {
    Context context;
    List<AccountBean> accountData;
    LayoutInflater inflater;
    int year,month,day;
    public AccountAdapter(Context context, List<AccountBean> accountData) {
        this.context = context;
        this.accountData = accountData;
        inflater = LayoutInflater.from(context);
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public int getCount() {
        return accountData.size();
    }

    @Override
    public Object getItem(int position) {
        return accountData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_view_main,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        AccountBean bean = accountData.get(position);
        holder.typeImageView.setImageResource(bean.getSelectedImageId());
        holder.typeTextView.setText(bean.getTypeName());
        holder.noteTextView.setText(bean.getNote());
        holder.moneyTv.setText("$"+bean.getMoney());
        if (bean.getYear()==year&&bean.getMonth()==month&&bean.getDay()==day) {
            String time = bean.getTime().split(" ")[1];
            holder.timeTextView.setText("Today "+time);
        }else {
            holder.timeTextView.setText(bean.getTime());
        }
        return convertView;
    }

    class ViewHolder{
        ImageView typeImageView;
        TextView typeTextView,noteTextView,timeTextView,moneyTv;
        public ViewHolder(View view){
            typeImageView = view.findViewById(R.id.item_list_view_image_view);
            typeTextView = view.findViewById(R.id.item_list_view_text_view_title);
            timeTextView = view.findViewById(R.id.item_list_view_text_view_time);
            noteTextView = view.findViewById(R.id.item_list_view_text_view_note);
            moneyTv = view.findViewById(R.id.item_list_view_text_view_cost);

        }
    }
}
