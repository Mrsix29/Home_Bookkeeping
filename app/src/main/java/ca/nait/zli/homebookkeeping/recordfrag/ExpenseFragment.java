package ca.nait.zli.homebookkeeping.recordfrag;

import androidx.fragment.app.Fragment;

import java.util.List;

import ca.nait.zli.homebookkeeping.R;
import ca.nait.zli.homebookkeeping.db.DBManager;
import ca.nait.zli.homebookkeeping.db.TypeBean;


public class ExpenseFragment extends BaseRecordFragment {

    @Override
    public void loadDataToGV() {
        super.loadDataToGV();
        List<TypeBean> expenseList = DBManager.getTypeList(0);
        typeList.addAll(expenseList);
        adapter.notifyDataSetChanged();
        typeTextView.setText("Shopping");
        typeImageView.setImageResource(R.mipmap.ic_shopping_s);
    }

    @Override
    public void saveAccountToDB() {
        accountBean.setCategory(0);
        DBManager.insertItemToRecord(accountBean);
    }
}
