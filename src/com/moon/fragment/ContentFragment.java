package com.moon.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.moon.Account.R;
import com.moon.adapter.MyListViewAdapter;
import com.moon.helper.MyChartView;
import com.moon.helper.MySQLiteOpenHelper;

import java.util.*;

/**
 * Created with IntelliJ IDEA
 * User: Moon
 * Date: 2015/4/10.
 */
public class ContentFragment extends Fragment {
    private int[] layouts = new int[]{R.layout.fragment_record, R.layout.fragment_detail, R.layout.fragment_count};

    private int tabIndex;
    private int flag = 1;
    private String type;
    private String detail;

    private List<String> types_in = new ArrayList<String>();
    private List<String> types_out = new ArrayList<String>();
    private List<String> types = new ArrayList<String>();
    private ArrayAdapter<String> adapter_type;
    private ArrayAdapter<String> adapter_date_type;
    private List<Map<String, Object>> list_show;
    private MyListViewAdapter adapter_show;

    private RadioGroup radioGroup;
    private TextView textView_date;
    private TextView textView_time;
    private Spinner spinner_detail;
    private EditText editText_money;
    private EditText editText_comment;
    private Button btn;

    private Spinner spinner_date_type;
    private ListView listView_detail;

    private MyChartView chartView;

    private MySQLiteOpenHelper dbHelper;

    private Calendar calendar = Calendar.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabIndex = getArguments().getInt("tabIndex");
        dbHelper = new MySQLiteOpenHelper(getActivity());
        types_in = Arrays.asList(getResources().getStringArray(R.array.types_in));
        types_out = Arrays.asList(getResources().getStringArray(R.array.types_out));
        list_show = new ArrayList<Map<String, Object>>();
        types = types_out;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(layouts[tabIndex], null);
        setView(view);
        return view;
    }

    /**
     * 判断界面内容
     *
     * @param view
     */
    private void setView(View view) {
        switch (tabIndex) {
            case 0:
                setRecordView(view);
                break;
            case 1:
                setDetailView(view);
                break;
            case 2:
                setCountView(view);
                break;
        }
    }

    /**
     * 设置记账界面
     *
     * @param v
     */
    private void setRecordView(View v) {
        radioGroup = (RadioGroup) v.findViewById(R.id.radioGroup);
        spinner_detail = (Spinner) v.findViewById(R.id.spinner_detail);
        textView_date = (TextView) v.findViewById(R.id.textView_date);
        textView_time = (TextView) v.findViewById(R.id.textView_time);
        editText_money = (EditText) v.findViewById(R.id.editText_money);
        editText_comment = (EditText) v.findViewById(R.id.editText_comment);
        btn = (Button) v.findViewById(R.id.btn_save);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(radioGroup.getId() == R.id.radioGroup){
                    switch (i){
                        case R.id.radioBtn_in:
                            types = types_in;
                            flag = 1;
                            break;
                        case R.id.radioBtn_out:
                            types = types_out;
                            flag = 2;
                            break;
                    }
                    adapter_type = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, types);
                    adapter_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_detail.setAdapter(adapter_type);
                }
            }
        });

        adapter_type = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, types);
        adapter_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_detail.setAdapter(adapter_type);

        spinner_detail.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (flag) {
                    case 1:
                        type = types_in.get(i);
                        Log.i("1111", "type" + type);
                        break;
                    case 2:
                        detail = types_out.get(i);
                        Log.i("1111", "detail" + detail);
                        if (i >= 0 && i <= 2) {
                            type = "吃";
                        } else if (i == 3 || i == 4) {
                            type = "穿";
                        } else if (i == 5 || i == 6) {
                            type = "住";
                        } else if (i == 7 || i == 8) {
                            type = "行";
                        } else {
                            type = "用";
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        textView_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = calendar.get(Calendar.YEAR);
                int monthOfYear = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String dateString = year + "-" + getMonth(monthOfYear) + "-" + getDay(dayOfMonth);
                                textView_date.setText(dateString);
                            }
                        }, year, monthOfYear, dayOfMonth);
                dDialog.show();
            }
        });

        textView_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
                final String h;
                if(hourOfDay / 10 == 0){
                    h = "0" + hourOfDay;
                }else{
                    h = "" + hourOfDay;
                }
                int minute = calendar.get(Calendar.MINUTE);
                final String m;
                if(minute / 10 == 0){
                    m = "0" + minute;
                }else{
                    m = "" + minute;
                }
                TimePickerDialog dDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                                String timeString = h + ":" + m;
                                textView_time.setText(timeString);
                            }
                        }, hourOfDay, minute, true);
                dDialog.show();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sql = null;
                String[] args = null;
                switch (flag) {
                    case 1:
                        sql = "insert into tb_income(money,type,detail,dates,time,comments) values(?,?,?,?,?,?)";
                        args = new String[]{editText_money.getText().toString(), "收入", type, textView_date.getText().toString(), textView_time.getText().toString(), editText_comment.getText().toString()};
                        break;
                    case 2:
                        sql = "insert into tb_expand(money,type,detail,dates,time,comments) values(?,?,?,?,?,?)";
                        args = new String[]{editText_money.getText().toString(), type, detail, textView_date.getText().toString(), textView_time.getText().toString(), editText_comment.getText().toString()};
                        break;
                }
                if (dbHelper.execData(sql, args)) {
                    Log.i("1111", "save ok.....");
                }
            }
        });

    }

    /**
     * 设置流水界面
     *
     * @param v
     */
    private void setDetailView(View v) {
        spinner_date_type = (Spinner) v.findViewById(R.id.spinner_date_type);
        listView_detail = (ListView) v.findViewById(R.id.listView_detail);

        adapter_show = new MyListViewAdapter(getActivity(),list_show);
        listView_detail.setAdapter(adapter_show);

        adapter_date_type = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.date_type));
        adapter_date_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_date_type.setAdapter(adapter_date_type);
        spinner_date_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                list_show.clear();
                switch (i) {
                    case 0:
                        String sql1 = "select * from tb_expand where dates = date('now')";
                        String sql2 = "select * from tb_income where dates = date('now')";
                        list_show.addAll(dbHelper.selectList(sql1, null));
                        list_show.addAll(dbHelper.selectList(sql2, null));
                        break;
                    case 1:
                        String where_month = calendar.get(Calendar.YEAR) + "-" + getMonth(calendar.get(Calendar.MONTH)) + "%";
                        String sql3 = "select * from tb_expand where dates like ?";
                        String sql4 = "select * from tb_income where dates like ?";
                        list_show.addAll(dbHelper.selectList(sql3, new String[]{where_month}));
                        list_show.addAll(dbHelper.selectList(sql4, new String[]{where_month}));
                        break;
                    case 2:
                        String where_year = calendar.get(Calendar.YEAR)+"%";
                        String sql5 = "select * from tb_expand where dates like ?";
                        String sql6 = "select * from tb_income where dates like ?";
                        list_show.addAll(dbHelper.selectList(sql5, new String[]{where_year}));
                        list_show.addAll(dbHelper.selectList(sql6, new String[]{where_year}));
                        break;
                }
                adapter_show.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    /**
     * 设置统计界面
     *
     * @param v
     */
    private void setCountView(View v) {
        chartView = (MyChartView) v.findViewById(R.id.chartView);
        String sql1 = "select sum(money) out from tb_expand";
        String sql2 = "select sum(money) inn from tb_income";
        list_show.clear();
        list_show.addAll(dbHelper.selectList(sql1, null));
        list_show.addAll(dbHelper.selectList(sql2, null));
        chartView.setData(list_show);
    }

    /**
     * 获得月份字符串
     * @param monthOfYear
     * @return
     */
    private String getMonth(int monthOfYear){
        String m;
        if ((monthOfYear + 1) / 10 == 0) {
            m = "0" + (monthOfYear + 1);
        } else {
            m = "" + (monthOfYear + 1);
        }
        return m;
    }

    /**
     * 获得"天"的字符串
     * @param dayOfMonth
     * @return
     */
    private String getDay(int dayOfMonth){
        String d;
        if (dayOfMonth / 10 == 0) {
            d = "0" + dayOfMonth;
        } else {
            d = "" + dayOfMonth;
        }
        return d;
    }
}
