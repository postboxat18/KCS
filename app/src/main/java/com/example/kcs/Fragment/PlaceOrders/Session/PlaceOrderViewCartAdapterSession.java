package com.example.kcs.Fragment.PlaceOrders.Session;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kcs.Classes.MyLog;
import com.example.kcs.ViewModel.SharedPreferences_data;
import com.example.kcs.Fragment.Items.CheckedList;
import com.example.kcs.Fragment.PlaceOrders.Header.PlaceOrderViewCartAdapterHeader;
import com.example.kcs.Fragment.PlaceOrders.Header.SelectedHeader;
import com.example.kcs.Fragment.Session.SessionList;
import com.example.kcs.Fragment.Settings.Profile.MyOrders.BottomSheet.OrderDishLists;
import com.example.kcs.Fragment.Settings.Profile.MyOrders.BottomSheet.ViewCartAdapterItem;
import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;
import com.example.kcs.Fragment.Dish.SelectedDishList;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class PlaceOrderViewCartAdapterSession extends RecyclerView.Adapter<PlaceOrderViewCartAdapterSession.ViewHolder> {
    private Context context;
    private List<OrderDishLists> orderDishListsses = new ArrayList<>();
    private ViewCartAdapterItem viewCartAdapterItem;
    private String TAG = "PlaceOrderViewCartAdapterSession";
    private String func_title, phone_number, date,time,count,oldDateTimeCount;
    private List<SelectedSessionList> sessionLists = new ArrayList<>();
    private GetViewModel getViewModel;
    private List<SelectedHeader> selectedHeaders = new ArrayList<>();
    //item map
    private LinkedHashMap<String, List<CheckedList>> itemMap = new LinkedHashMap<>();
    //header map
    private LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>> headerMap = new LinkedHashMap<>();
    //session map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>>> sessionMap = new LinkedHashMap<>();
    //fun map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>>>> funcMap = new LinkedHashMap<>();
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


    public PlaceOrderViewCartAdapterSession(Context context, GetViewModel getViewModel, String s, List<SelectedSessionList> sessionLists, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>>> sessionMap, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedDishList>>>> editSessionMap) {
        this.context = context;
        this.getViewModel = getViewModel;
        this.func_title = s;
        this.sessionMap = sessionMap;
        this.sessionLists = sessionLists;
        this.editSessionMap = editSessionMap;
    }


    @NonNull
    @Override
    public PlaceOrderViewCartAdapterSession.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.place_order_session, parent, false);
        return new PlaceOrderViewCartAdapterSession.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceOrderViewCartAdapterSession.ViewHolder holder, int position) {
        //get user name shared prefernces
        phone_number = new SharedPreferences_data(context).getS_phone_number();

        //get selected session
        //Incase change session breakfast to lunch

        //get date
        getViewModel.getDate_pickerMutable().observe((LifecycleOwner) context, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                date=s;
            }
        });
        //get count
        getViewModel.getS_countLiveData().observe((LifecycleOwner) context, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                count=s;
            }
        });
        //get time
        getViewModel.getTime_pickerMutable().observe((LifecycleOwner) context, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                time=s;
            }
        });

        //get oldDateTimeCount
        getViewModel.getOldDateTimeCountLiveData().observe((LifecycleOwner) context, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                oldDateTimeCount=s;
            }
        });

        if (editSessionMap == null) {
            final SelectedSessionList list = sessionLists.get(position);

            holder.session_title.setText(list.getSession_title());
            holder.session_title.setTextColor(context.getResources().getColor(R.color.colorSecondary));
            holder.count.setText(count);
            holder.count.setTextColor(context.getResources().getColor(R.color.colorSecondary));
            holder.date_time.setText(date+"\t\t"+time);
            holder.date_time.setTextColor(context.getResources().getColor(R.color.colorSecondary));
            String a = list.getSession_title() + "!" + list.getTime()+"/"+list.getS_count();
            MyLog.e(TAG,"count>>if>>"+a);
            headerMap = sessionMap.get(a);
            Set<String> stringSet = headerMap.keySet();
            List<String> aList = new ArrayList<String>(stringSet.size());
            for (String x : stringSet)
                aList.add(x);

            //MyLog.e(TAG,"chs>>list size>> "+ aList.size());
            selectedHeaders.clear();
            for (int i = 0; i < aList.size(); i++) {
                MyLog.e(TAG, "chs>>list header>> " + aList.get(i));
                SelectedHeader list1 = new SelectedHeader(
                        aList.get(i)
                );
                selectedHeaders.add(list1);
            }

            getViewModel.setSelectedHeadersList(selectedHeaders);
            holder.recyclerview_order_item_details.setHasFixedSize(true);
            holder.recyclerview_order_item_details.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            PlaceOrderViewCartAdapterHeader viewCartAdapter = new PlaceOrderViewCartAdapterHeader(context, getViewModel, selectedHeaders, headerMap, null);
            holder.recyclerview_order_item_details.setAdapter(viewCartAdapter);
        }
        else {
            final SelectedSessionList list = sessionLists.get(position);

            holder.session_title.setText(list.getSession_title());
            holder.session_title.setTextColor(context.getResources().getColor(R.color.colorSecondary));
            MyLog.e(TAG,"count>>else>>"+list.getS_count());
            holder.count.setText(count);
            holder.count.setTextColor(context.getResources().getColor(R.color.colorSecondary));
            holder.date_time.setText(date+"\t\t"+time);
            holder.date_time.setTextColor(context.getResources().getColor(R.color.colorSecondary));
            //oldDateTimeCount
            String[]str=oldDateTimeCount.split("_");
            String oldDate=str[0];
            String oldTime=str[1];
            String oldCount=str[2];

            String a = list.getSession_title() + "!" + oldTime+"/"+oldCount;
            MyLog.e(TAG, "editOrders>>sess>>"+list.getSession_title()+" ! "+list.getTime() );

            editHeaderMap = editSessionMap.get(a);
          /*  Set<String> stringSet = editHeaderMap.keySet();
            List<String> aList = new ArrayList<String>(stringSet.size());
            for (String x : stringSet)
                aList.add(x);

            //MyLog.e(TAG,"chs>>list size>> "+ aList.size());
            selectedHeaders.clear();
            for (int i = 0; i < aList.size(); i++) {
                MyLog.e(TAG, "chs>>list header>> " + aList.get(i));
                SelectedHeader list1 = new SelectedHeader(
                        aList.get(i)
                );
                selectedHeaders.add(list1);
            }
            getViewModel.setSelectedHeadersList(selectedHeaders);*/




            Set<String> stringSet = editHeaderMap.keySet();
            List<String> aList = new ArrayList<String>(stringSet.size());
            for (String x : stringSet)
                aList.add(x);

            //MyLog.e(TAG,"chs>>list size>> "+ aList.size());
            selectedHeaders.clear();
            for (int i = 0; i < aList.size(); i++) {
                MyLog.e(TAG, "chs>>list header>> " + aList.get(i));
                SelectedHeader list1 = new SelectedHeader(
                        aList.get(i)
                );
                selectedHeaders.add(list1);
            }

            getViewModel.setSelectedHeadersList(selectedHeaders);
            holder.recyclerview_order_item_details.setHasFixedSize(true);
            holder.recyclerview_order_item_details.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            PlaceOrderViewCartAdapterHeader viewCartAdapter = new PlaceOrderViewCartAdapterHeader(context, getViewModel, selectedHeaders, null, editHeaderMap);
            holder.recyclerview_order_item_details.setAdapter(viewCartAdapter);
        }

    }


    @Override
    public int getItemCount() {
        return sessionLists.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView session_title, date_time,count;
        private RecyclerView recyclerview_order_item_details;


        public ViewHolder(View view) {
            super(view);
            recyclerview_order_item_details = view.findViewById(R.id.recyclerview_order_item_details);
            session_title = view.findViewById(R.id.session_title);
            count = view.findViewById(R.id.count);
            date_time = view.findViewById(R.id.date_time);

        }
    }
}


