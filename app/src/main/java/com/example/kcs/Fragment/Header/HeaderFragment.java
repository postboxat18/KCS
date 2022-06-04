package com.example.kcs.Fragment.Header;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kcs.Classes.MyLog;
import com.example.kcs.Fragment.Func.FunList;
import com.example.kcs.Fragment.Items.ItemList;

import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

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
    private String funList_title;
    private TextView session_title;
    //Header
    private RecyclerView recyclerview_header;
    private HeaderAdapter headerAdapter;
    private ImageView back_btn;
    //HeaderAdapter.GetHeaderFragment getheaderFragment;
    private List<HeaderList> headerList=new ArrayList<>();
    //private MyViewModel myViewModel;
    private GetViewModel getViewModel;

    private TextView date_picker_actions;
    private String  s_date_picker_actions;
    private DatePickerDialog datePicker;

    private String TAG="HeaderFragment";
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
    public static HeaderFragment newInstance(String param1, String param2){
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
        View view= inflater.inflate(R.layout.fragment_header, container, false);

        date_picker_actions=view.findViewById(R.id.date_picker_actions);
        recyclerview_header=view.findViewById(R.id.recyclerview_header);
        session_title=view.findViewById(R.id.session_title);
        back_btn=view.findViewById(R.id.back_btn);



        // initialising the calendar
        final Calendar calendar = Calendar.getInstance();
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        // initialising the datepickerdialog
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            datePicker = new DatePickerDialog(getContext());
        }

        getViewModel.getDate_pickerMutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s==null)
                {
                    date_picker_actions.setError("Please select the date");
                }
                else
                {
                    date_picker_actions.setText(s);
                }
            }
        });

            //get view model session title
        getViewModel.getSession_titleMutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                session_title.setText(s);
            }
        });


        getViewModel.getHeaderListMutableList().observe(getViewLifecycleOwner(), new Observer<List<HeaderList>>() {
            @Override
            public void onChanged(List<HeaderList> headerLists) {
                headerList=headerLists;
            }
        });




        getViewModel.getS_mapMutable().observe(getViewLifecycleOwner(), new Observer<List<LinkedHashMap<String, List<ItemList>>>>() {
            @Override
            public void onChanged(List<LinkedHashMap<String, List<ItemList>>> linkedHashMaps) {
                //recyclerview_header
                recyclerview_header.setHasFixedSize(true);
                recyclerview_header.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                headerAdapter=new HeaderAdapter(getContext(),headerList,getViewModel,linkedHashMaps);
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

        //on click to select the date
        date_picker_actions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                        // adding the selected date in the edittext
                        date_picker_actions.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                        s_date_picker_actions=date_picker_actions.getText().toString();
                        getViewModel.setDate_picker(s_date_picker_actions);
                    }
                }, year, month, day);

                // set minimum date to be selected as today
                datePicker.getDatePicker().setMinDate(calendar.getTimeInMillis());

                // show the dialog
                datePicker.show();
            }
        });

        return view;
    }

}