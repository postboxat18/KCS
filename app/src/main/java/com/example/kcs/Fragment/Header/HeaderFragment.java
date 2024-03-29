package com.example.kcs.Fragment.Header;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.kcs.Fragment.Dish.DishList;
import com.example.kcs.Classes.MyLog;

import com.example.kcs.Fragment.PlaceOrders.Header.SelectedHeader;
import com.example.kcs.Fragment.Session.SessionList;
import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;
import com.example.kcs.Classes.TimeList;
import com.example.kcs.Fragment.Dish.SelectedDishList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HeaderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HeaderFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //call from FunAdapter
    private TextView session_title;
    private String s_session_title;
    //Header
    private RecyclerView recyclerview_header;
    private HeaderAdapter headerAdapter;
    private ImageView back_btn;
    //HeaderAdapter.GetHeaderFragment getheaderFragment;
    private List<HeaderList> headerList = new ArrayList<>();
    //private MyViewModel myViewModel;
    private GetViewModel getViewModel;

    //date and time
    private TextView date_picker_actions;
    private TextView time_picker, count;
    private String funcTitle, s_time_picker, s_date_picker_actions, date_time, s_count, oldDateTimeCount;
    private DatePickerDialog datePicker;
    private List<TimeList> timeLists = new ArrayList<>();

    private LinkedHashMap<String, List<TimeList>> stringListLinkedHashMap = new LinkedHashMap<>();
    private List<SessionDateTime> sessionDateTimes = new ArrayList<>();
    private LinkedHashMap<String, List<SessionDateTime>> f_mapsdtMutable = new LinkedHashMap<>();
    private int mYear, mMonth, mDay, mHour, mMinute;
    private String TAG = "HeaderFragment";
    //edit hash map
    //edit hash map list
    private List<SessionList> e_sessionLists = new ArrayList<>();
    private List<SelectedHeader> e_selectedHeaders = new ArrayList<>();

    //Edit HashMap
    //func map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedDishList>>>>>> editFunc_Map = new LinkedHashMap<>();
    //Date map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedDishList>>>>> editDateMap = new LinkedHashMap<>();
    //Session map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedDishList>>>> editSessionMap = new LinkedHashMap<>();
    //Header map
    private LinkedHashMap<String, LinkedHashMap<String, List<SelectedDishList>>> editHeaderMap = new LinkedHashMap<>();
    //Item map
    private LinkedHashMap<String, List<SelectedDishList>> editItemMap = new LinkedHashMap<>();

    //dish map in list
    private LinkedHashMap<String, LinkedHashMap<String, List<DishList>>> d_ItemMap = new LinkedHashMap<>();
    private LinkedHashMap<String, List<DishList>> d_DishMap = new LinkedHashMap<>();

    public HeaderFragment() {


        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HeaderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HeaderFragment newInstance(String param1, String param2) {
        HeaderFragment fragment = new HeaderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  myViewModel = new ViewModelProvider(getActivity()).get(MyViewModel.class);
        getViewModel = new ViewModelProvider(getActivity()).get(GetViewModel.class);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_header, container, false);

        date_picker_actions = view.findViewById(R.id.date_picker_actions);
        time_picker = view.findViewById(R.id.time_picker);
        recyclerview_header = view.findViewById(R.id.recyclerview_header);
        session_title = view.findViewById(R.id.session_title);
        back_btn = view.findViewById(R.id.back_btn);
        count = view.findViewById(R.id.count);


        // initialising the calendar
        final Calendar calendar = Calendar.getInstance();
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        // initialising the datepickerdialog
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            datePicker = new DatePickerDialog(getContext());
        }

        //load time picker
        //getViewModel.GetSessionTime();
        //get fun_title
        getViewModel.getFunc_title_Mutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                funcTitle = s;

            }
        });


        //get time picker
        getViewModel.getTime_pickerMutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                s_time_picker = s;
                if (s == null) {
                    time_picker.setError("please select the time");
                } else {
                    time_picker.setError(null);
                    time_picker.setText(s);
                }

            }
        });

        //get head count
        getViewModel.getS_countLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                s_count = s;
                if (s == null) {
                    count.setError("please select the head count");
                } else {
                    count.setError(null);
                    count.setText(s_count);
                }

            }
        });

        //get date picker
        getViewModel.getDate_pickerMutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                s_date_picker_actions = s;
                if (s == null) {
                    date_picker_actions.setError("Please select the date");
                } else {
                    date_picker_actions.setError(null);
                    date_picker_actions.setText(s);
                }
            }
        });

        //get oldDateTimeCount
        getViewModel.getOldDateTimeCountLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                oldDateTimeCount = s;
            }
        });

        //get edit func map
        getViewModel.getEditFuncMapMutableLiveData().observe(getViewLifecycleOwner(), new Observer<LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedDishList>>>>>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedDishList>>>>>> stringLinkedHashMapLinkedHashMap) {
                editFunc_Map = stringLinkedHashMapLinkedHashMap;
                editDateMap = editFunc_Map.get(funcTitle);
                MyLog.e(TAG, "order>>funcTitle>>" + funcTitle);
                if (editDateMap == null) {
                    editDateMap = new LinkedHashMap<>();
                    MyLog.e(TAG, "edit date map is null");

                } else {
                    MyLog.e(TAG, "edits>>date " + s_date_picker_actions);
                    String date = s_date_picker_actions.replace("/", "-");
                    //oldDateTimeCount
                    String[] arr = oldDateTimeCount.split("_");
                    String oldDate = arr[0];
                    String oldTime = arr[1];
                    String oldCount = arr[2];
                    MyLog.e(TAG, "edits>>oldDate " + oldDate);
                    oldDate = oldDate.replace("/", "-");


                    editSessionMap = editDateMap.get(oldDate);
                    MyLog.e(TAG, "headerCoutn>>\ntime>>" + s_time_picker + "\ncount>>" + s_count + "\nsession>>" + s_session_title);

                    Set<String> stringSet = editSessionMap.keySet();
                    List<String> aList = new ArrayList<String>(stringSet.size());
                    for (String x : stringSet)
                        aList.add(x);
                    for (int i = 0; i < aList.size(); i++) {
                        MyLog.e(TAG, "order>>date>>" + aList.get(i));
                        String[] scb = (aList.get(i)).split("/");
                        String[] str = (scb[0]).split("!");
                        s_session_title = str[0];
                        String time = str[1];
                        if (s_count.equals(scb[1]) && (s_time_picker).equals(time)) {
                            MyLog.e(TAG, "headerCoutn>> if>>"+s_count+">>"+time);
                            s_count = scb[1];
                            count.setText(s_count);
                            getViewModel.setS_count(s_count);
                            break;
                        } else {
                            continue;
                        }
                    }
                    //change 12:12 AM to 24-hrs time
                    SimpleDateFormat date12Format = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
                    SimpleDateFormat date24Format = new SimpleDateFormat("HH:mm");
                    String stime = null;
                    try {
                        stime = date24Format.format(date12Format.parse(s_time_picker));
                    } catch (ParseException e) {
                        e.printStackTrace();
                        MyLog.e(TAG, "time>>error>>" + e.getMessage());
                    }
                    MyLog.e(TAG, "time>>" + stime);
                    String[] hrs = stime.split(":");
                    MyLog.e(TAG, "time>>" + hrs[0]);
                    MyLog.e(TAG, "time>>" + hrs[1]);

                    //check the date and time selection
                    //check condition if lunch or breakfast
                    getViewModel.CheckTime(s_session_title, date, Integer.parseInt(hrs[0]), Integer.parseInt(hrs[1]), funcTitle);
                }
            }
        });
        //get time picker hash map
        getViewModel.getTimeListF_MapMutableLiveData().

                observe(getViewLifecycleOwner(), new Observer<LinkedHashMap<String, List<TimeList>>>() {
                    @Override
                    public void onChanged(LinkedHashMap<String, List<TimeList>> stringListLinkedHashMap1) {
                        stringListLinkedHashMap = stringListLinkedHashMap1;

                    }
                });


        //get alert message
        getViewModel.getAlertMutable().

                observe(getViewLifecycleOwner(), new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {

                        if (integer == 0) {
                            if (s_session_title != null) {
                                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                                alert.setMessage("You Have Selected Time is More Than " + s_session_title + " Session");
                                alert.setTitle("Problem");
                                alert.setCancelable(false);
                                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @SuppressLint("NotifyDataSetChanged")
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                AlertDialog alertDialog = alert.create();
                                alertDialog.show();
                            } else {
                                MyLog.e(TAG, "Session has empty value");
                            }
                        }

                    }
                });


        //get session date and time
        getViewModel.getSessionDateTimesMutableLiveData().

                observe(getViewLifecycleOwner(), new Observer<List<SessionDateTime>>() {
                    @Override
                    public void onChanged(List<SessionDateTime> sessionDateTimes1) {
                        sessionDateTimes = sessionDateTimes1;
                    }
                });
        //get view model session title
        getViewModel.getSession_titleMutable().

                observe(getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        s_session_title = s;
                        session_title.setText(s);
                        MyLog.e(TAG, "dateTime>>session>>" + s_session_title);
                        MyLog.e(TAG, "dateTime>>date>>" + s_date_picker_actions);
                        MyLog.e(TAG, "dateTime>>time>>" + s_time_picker);

                        if ((sessionDateTimes == null || sessionDateTimes.isEmpty()) && ((s_time_picker == null) || (s_time_picker.isEmpty()))) {

                            timeLists = stringListLinkedHashMap.get(s);
                            time_picker.setText(timeLists.get(0).getTimeList());
                            MyLog.e(TAG, "dateTime>>if");


                        } else {
                            //setText Date and Time
                            for (int i = 0; i < sessionDateTimes.size(); i++) {
                                date_picker_actions.setText(sessionDateTimes.get(i).getDate());
                                time_picker.setText(sessionDateTimes.get(i).getTime());
                                //set date and time
                                getViewModel.setDate_picker(sessionDateTimes.get(i).getDate());
                                getViewModel.setTimepicker(sessionDateTimes.get(i).getTime());

                                MyLog.e(TAG, "dateTime>>else");
                            }

                        }

                    }
                });


        //get header list
        getViewModel.getHeaderListMutableList().

                observe(getViewLifecycleOwner(), new Observer<List<HeaderList>>() {
                    @Override
                    public void onChanged(List<HeaderList> headerLists) {
                        headerList = headerLists;
                    }
                });

        //get dish map in list database
        getViewModel.getD_ItemMapMutableLiveData().observe(getViewLifecycleOwner(), new Observer<LinkedHashMap<String, LinkedHashMap<String, List<DishList>>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, LinkedHashMap<String, List<DishList>>> stringLinkedHashMapLinkedHashMap) {
                d_ItemMap = stringLinkedHashMapLinkedHashMap;
                //recyclerview_header
                recyclerview_header.setHasFixedSize(true);
                recyclerview_header.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                headerAdapter = new HeaderAdapter(getContext(), headerList, getViewModel, d_ItemMap, s_session_title);
                getViewModel.setI_fragment(1);
                recyclerview_header.setAdapter(headerAdapter);
            }
        });


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getViewModel.setI_value(6);
            }
        });


        //time picker
        time_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //time picker wehn select time and clik on ok btn alert the time is over lunch or breafast
                if ((date_picker_actions.getText().toString()) != null) {
                    // Get Current Time
                    final Calendar c = Calendar.getInstance();

                    //set selected time to show on alert machine
                    mHour = c.get(Calendar.HOUR_OF_DAY);
                    mMinute = c.get(Calendar.MINUTE);

                    String[] arr = (time_picker.getText().toString()).split("-");
                    try {

                        if ((arr[1]) == null) {
                            String[] str = (time_picker.getText().toString()).split(" ");

                            //change 12:12 AM to 24-hrs time
                            SimpleDateFormat date12Format = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
                            SimpleDateFormat date24Format = new SimpleDateFormat("HH:mm");
                            String stime = null;
                            try {
                                stime = date24Format.format(date12Format.parse(time_picker.getText().toString()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                                MyLog.e(TAG, "time>>error>>" + e.getMessage());
                            }
                            MyLog.e(TAG, "times>>" + stime);
                            MyLog.e(TAG, "times>>\nhour>>" + mHour + "\nmin>>" + mMinute);

                            // Launch Time Picker Dialog
                            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                                    new TimePickerDialog.OnTimeSetListener() {

                                        @Override
                                        public void onTimeSet(TimePicker view, int hourOfDay,
                                                              int minute) {
                                            //String.format("%02d:%02d %s", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute, isPM ? "PM" : "AM"
                                            // txtTime.setText(hourOfDay + ":" + minute);


                                            //check condition if lunch or breakfast
                                            getViewModel.CheckTime(s_session_title, (date_picker_actions.getText().toString()), hourOfDay, minute, funcTitle);

                                            boolean isPM = (hourOfDay >= 12);
                                            time_picker.setText(String.format("%02d:%02d %s", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute, isPM ? "PM" : "AM"));

                                        }
                                    }, mHour, mMinute, false);
                            timePickerDialog.show();

                        } else {
                            // Launch Time Picker Dialog
                            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                                    new TimePickerDialog.OnTimeSetListener() {

                                        @Override
                                        public void onTimeSet(TimePicker view, int hourOfDay,
                                                              int minute) {
                                            //String.format("%02d:%02d %s", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute, isPM ? "PM" : "AM"
                                            // txtTime.setText(hourOfDay + ":" + minute);


                                            //check condition if lunch or breakfast
                                            getViewModel.CheckTime(s_session_title, (date_picker_actions.getText().toString()), hourOfDay, minute, funcTitle);

                                            boolean isPM = (hourOfDay >= 12);
                                            time_picker.setText(String.format("%02d:%02d %s", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute, isPM ? "PM" : "AM"));

                                        }
                                    }, mHour, mMinute, false);
                            timePickerDialog.show();

                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        MyLog.e(TAG, "times>>erroe>>" + e.getMessage());


                        //change 12:12 AM to 24-hrs time
                        SimpleDateFormat date12Format = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
                        SimpleDateFormat date24Format = new SimpleDateFormat("HH:mm");
                        String stime = null;
                        try {
                            stime = date24Format.format(date12Format.parse(time_picker.getText().toString()));
                        } catch (ParseException m) {
                            m.printStackTrace();
                            MyLog.e(TAG, "time>>error>>" + e.getMessage());
                        }
                        MyLog.e(TAG, "times>>" + stime);
                        String[] str = stime.split(":");
                        int mHour = Integer.parseInt(str[0]);
                        int mMinute = Integer.parseInt(str[1]);
                        MyLog.e(TAG, "times>>\nhour>>" + mHour + "\nmin>>" + mMinute);

                        // Launch Time Picker Dialog
                        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                                new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay,
                                                          int minute) {
                                        //String.format("%02d:%02d %s", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute, isPM ? "PM" : "AM"
                                        // txtTime.setText(hourOfDay + ":" + minute);


                                        //check condition if lunch or breakfast
                                        getViewModel.CheckTime(s_session_title, (date_picker_actions.getText().toString()), hourOfDay, minute, funcTitle);

                                        boolean isPM = (hourOfDay >= 12);
                                        time_picker.setText(String.format("%02d:%02d %s", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute, isPM ? "PM" : "AM"));

                                    }
                                }, mHour, mMinute, false);
                        timePickerDialog.show();


                    }

                } else {
                    Toast.makeText(getContext(), "Please select date first", Toast.LENGTH_SHORT).show();
                }

            }
        });


        //on click to select the date
        date_picker_actions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MyLog.e(TAG, "day>>setText>>" + date_picker_actions.getText().toString());

                if ((date_picker_actions.getText().toString()).isEmpty()) {
                    datePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                            // adding the selected date in the edittext
                            date_picker_actions.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                            getViewModel.setDate_picker(date_picker_actions.getText().toString());
                        }
                    }, year, month, day);
                } else {
                    String[] arr = (date_picker_actions.getText().toString()).split("/");
                    int day = Integer.parseInt(arr[0]);
                    int month = Integer.parseInt(arr[1]);
                    int year = Integer.parseInt(arr[2]);
                    MyLog.e(TAG, "day>>" + day + "\nmonth>>" + month + "\nyear>>" + year);
                    datePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                            // adding the selected date in the edittext
                            date_picker_actions.setText(dayOfMonth + "/" + (month) + "/" + year);
                            getViewModel.setDate_picker(date_picker_actions.getText().toString());
                        }
                    }, year, month, day);

                }
                // set minimum date to be selected as today
                datePicker.getDatePicker().setMinDate(calendar.getTimeInMillis());

                // show the dialog
                datePicker.show();
            }
        });
        //onclick head count
        count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Head Count");
                LinearLayout linearLayout = new LinearLayout(getContext());
                final EditText countEdit = new EditText(getContext());

                if (!(count.getText().toString()).isEmpty()) {
                    countEdit.setText(count.getText().toString());
                }
                // write the email using which you registered
                countEdit.setHint("10000");
                countEdit.setMinEms(16);
                countEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
                linearLayout.addView(countEdit);
                linearLayout.setPadding(10, 10, 10, 10);
                builder.setView(linearLayout);

                // Click on Recover and a email will be sent to your registered email id
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String s_count = countEdit.getText().toString().trim();
                        count.setText(s_count);
                        getViewModel.setS_count(s_count);
                        dialog.cancel();
                    }
                });

                builder.create().show();
            }
        });

        return view;
    }


}