package ca.nait.zli.homebookkeeping.recordfrag;

import java.util.List;

import ca.nait.zli.homebookkeeping.R;
import ca.nait.zli.homebookkeeping.db.DBManager;
import ca.nait.zli.homebookkeeping.db.TypeBean;

/**
 * 收入记录页面
 */
public class IncomeFragment extends BaseRecordFragment {


    @Override
    public void loadDataToGV() {
        super.loadDataToGV();
        //获取数据库当中的数据源
        List<TypeBean> incomeList = DBManager.getTypeList(1);
        typeList.addAll(incomeList);
        adapter.notifyDataSetChanged();
        typeTextView.setText("Salary");
        typeImageView.setImageResource(R.mipmap.ic_salary_s);

    }

   @Override
    public void saveAccountToDB() {
        accountBean.setCategory(1);
        DBManager.insertItemToRecord(accountBean);
    }
}
