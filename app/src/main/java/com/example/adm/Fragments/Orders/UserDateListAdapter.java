package com.example.adm.Fragments.Orders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adm.Classes.MyLog;
import com.example.adm.Classes.SessionList;
import com.example.adm.Fragments.Orders.BottomSheet.OrderItemLists;
import com.example.adm.Fragments.Orders.BottomSheet.OrderLists;
import com.example.adm.R;
import com.example.adm.ViewModel.GetViewModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class UserDateListAdapter extends RecyclerView.Adapter<UserDateListAdapter.ViewHolder> {
    private Context context;
    private String TAG = "UserDateListAdapter";
    private GetViewModel getViewModel;
    //order hash map
    //date map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderItemLists>>>> orderDateMap = new LinkedHashMap<>();
    //header map
    private LinkedHashMap<String, List<OrderItemLists>> orderHeaderMap = new LinkedHashMap<>();
    //session map
    private LinkedHashMap<String, LinkedHashMap<String, List<OrderItemLists>>> orderSessionMap = new LinkedHashMap<>();
    //selected date list
    private List<SelectedDateList> o_dateLists=new ArrayList<>();
    //selected session list
    private List<SelectedSessionList> o_selectedSessionLists=new ArrayList<>();


    public UserDateListAdapter(Context context, GetViewModel getViewModel, List<SelectedDateList> o_dateLists, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderItemLists>>>> orderDateMap) {
        this.context = context;
        this.getViewModel = getViewModel;
        this.o_dateLists = o_dateLists;
        this.orderDateMap = orderDateMap;
    }

    @NonNull
    @Override
    public UserDateListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.date_cardview, parent, false);
        return new UserDateListAdapter.ViewHolder(view);
        //return view;
    }

    @Override
    public void onBindViewHolder(@NonNull UserDateListAdapter.ViewHolder holder, int position) {

        final SelectedDateList list = o_dateLists.get(position);

        holder.date.setText(list.getDate());

        orderSessionMap=orderDateMap.get(list.getDate());
        //get session
        Set<String> stringSet = orderSessionMap.keySet();
        List<String> aList = new ArrayList<String>(stringSet.size());
        for (String x : stringSet)
            aList.add(x);
        o_selectedSessionLists.clear();
        for(int i=0;i<aList.size();i++)
        {
            SelectedSessionList sessionList = new SelectedSessionList();
            String[] be = (aList.get(i)).split("_");
            String bolen=be[1];
            String[] se=(be[0]).split("!");
            String sess=se[0];
            String time=se[1];
            sessionList.setSession_title(sess);
            sessionList.setTime(time);
            sessionList.setBolen(bolen);
            o_selectedSessionLists.add(sessionList);
        }
        holder.recyclerview_session.setHasFixedSize(true);
        holder.recyclerview_session.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        UserSessionListAdapter userSessionListAdapter=new UserSessionListAdapter(context,getViewModel,o_selectedSessionLists,orderSessionMap);
        holder.recyclerview_session.setAdapter(userSessionListAdapter);




    }


    @Override
    public int getItemCount() {
        MyLog.e(TAG, "sessionLists>>49>>" + o_dateLists.size());
        return o_dateLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView date;
        private RecyclerView recyclerview_session;
        public ViewHolder(View view) {
            super(view);
            date = view.findViewById(R.id.date);
            recyclerview_session = view.findViewById(R.id.recyclerview_session);


        }
    }
}


