package ca.nait.zli.homebookkeeping;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import ca.nait.zli.homebookkeeping.adapter.RecordPagerAdapter;
import ca.nait.zli.homebookkeeping.recordfrag.ExpenseFragment;
import ca.nait.zli.homebookkeeping.recordfrag.IncomeFragment;

public class RecordActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        tabLayout = findViewById(R.id.record_tabs);
        viewPager = findViewById(R.id.record_view_pager);
        initPager();
    }

    private void initPager() {
        // initialize fragment list
        List<Fragment> fragmentList = new ArrayList<>();
//        create income and expense fragments
        ExpenseFragment expenseFragment = new ExpenseFragment(); //Expense
        IncomeFragment incomeFragment = new IncomeFragment(); //Income
        fragmentList.add(expenseFragment);
        fragmentList.add(incomeFragment);

//        create new page adapter
        RecordPagerAdapter pagerAdapter = new RecordPagerAdapter(getSupportFragmentManager(), fragmentList);

        viewPager.setAdapter(pagerAdapter);
        // link tab layout and view pager
        tabLayout.setupWithViewPager(viewPager);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.record_image_view:
                finish();
                break;
        }
    }
}