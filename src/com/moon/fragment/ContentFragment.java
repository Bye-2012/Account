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
import com.moon.helper.MySQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created with IntelliJ IDEA
 * User: Moon
 * Date: 2015/4/10.
 */
public class ContentFragment extends Fragment {
    private int[] layouts = new int[]{R.layout.fragment_record, R.layout.fragment_detail, R.layout.fragment_count};

    private int tabIndex;
    private int flag = 1;
    private int type;
    private int detail;

    private List<String> types_in = new ArrayList<String>();
    private List<String> types_out = new ArrayList<String>();
    private List<String> types = new ArrayList<String>();
    private ArrayAdapter<String> adapter_type;

    private RadioButton radioBtn_in;
    private TextView textView_date;
    private TextView textView_time;
    private Spinner spinner_detail;
    private EditText editText_money;
    private EditText editText_comment;
    private Button btn;

    private MySQLiteOpenHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabIndex = getArguments().getInt("tabIndex");
        dbHelper = new MySQLiteOpenHelper(getActivity());
        types_in = Arrays.asList(getResources().getStringArray(R.array.types_in));
        types_out = Arrays.asList(getResources().getStringArray(R.array.types_out));
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
        radioBtn_in = (RadioButton) v.findViewById(R.id.radioBtn_in);
        spinner_detail = (Spinner) v.findViewById(R.id.spinner_detail);
        textView_date = (TextView) v.findViewById(R.id.textView_date);
        textView_time = (TextView) v.findViewById(R.id.textView_time);
        editText_money = (EditText) v.findViewById(R.id.editText_money);
        editText_comment = (EditText) v.findViewById(R.id.editText_comment);
        btn = (Button) v.findViewById(R.id.btn_save);

        radioBtn_in.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    types = types_in;
                    flag = 1;
                } else {
                    types = types_out;
                    flag = 2;
                }
                adapter_type = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, types);
                adapter_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_detail.setAdapter(adapter_type);
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
                        type = i;
                        break;
                    case 2:
                        detail = i;
                        if (i >= 0 && i <= 2) {
                            type = 0;
                        } else if (i == 3 || i == 4) {
                            type = 1;
                        } else if (i == 5 || i == 6) {
                            type = 2;
                        } else if (i == 7 || i == 8) {
                            type = 3;
                        } else {
                            type = 4;
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
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int monthOfYear = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String dateString = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
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
                int minute = calendar.get(Calendar.MINUTE);
                TimePickerDialog dDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                                String timeString = hourOfDay + ":" + minute;
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
                        sql = "insert into tb_income(money,type,date,time,comments) values(?,?,?,?,?)";
                        args = new String[]{editText_money.getText().toString(), type + "", textView_date.getText().toString(), textView_time.getText().toString(), editText_comment.getText().toString()};
                        break;
                    case 2:
                        sql = "insert into tb_expand(money,type,detail,date,time,comments) values(?,?,?,?,?,?)";
                        args = new String[]{editText_money.getText().toString(), type + "", detail + "", textView_date.getText().toString(), textView_time.getText().toString(), editText_comment.getText().toString()};
                        break;
                }
                if (dbHelper.execData(sql, args)) {
                    Log.i("1111", "save ok");
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
        Spinner spinner_date_type = (Spinner) v.findViewById(R.id.spinner_date_type);
        ListView listView_detail = (ListView) v.findViewById(R.id.listView_detail);
    }

    /**
     * 设置统计界面
     *
     * @param v
     */
    private void setCountView(View v) {

    }
}
