package com.example.kcs.Fragment.Dish;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kcs.Classes.MyLog;
import com.example.kcs.Classes.SharedPreferences_data;
import com.example.kcs.Fragment.Header.SessionDateTime;
import com.example.kcs.Fragment.Items.CheckedList;
import com.example.kcs.Fragment.Items.ItemList;
import com.example.kcs.Fragment.PlaceOrders.Session.SelectedSessionList;
import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;
import com.example.kcs.ViewModel.SelectedDishList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.ViewHolder> {
    private Context context;
    private List<DishList> dishLists = new ArrayList<>();
    private GetViewModel getViewModel;
    private String TAG = "DishAdapter";
    private List<CheckedList> checkedLists = new ArrayList<>();
    private List<LinkedHashMap<String, List<CheckedList>>> selected_s_map = new ArrayList<>();

    private LinkedHashMap<String, List<CheckedList>> itemMap = new LinkedHashMap<>();
    private LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>> headerMap = new LinkedHashMap<>();
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>>> sessionMap = new LinkedHashMap<>();
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>>>> funcMap = new LinkedHashMap<>();
    private String funcTitle, sessionTitle, header_title, item_title,s_count,oldDateTimeCount;
    private List<SessionDateTime> sessionDateTimes = new ArrayList<>();
    private List<SelectedSessionList> selectedSessionLists = new ArrayList<>();
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
    //selected dish list
    private List<SelectedDishList> selectedDishLists=new ArrayList<>();
    private List<SelectedDishList> oldSelectedDishLists=new ArrayList<>();


    public DishAdapter(Context context, List<DishList> dishLists, GetViewModel getViewModel, List<LinkedHashMap<String, List<CheckedList>>> selected_s_map, LinkedHashMap<String, List<CheckedList>> itemMap) {
        this.context = context;
        this.dishLists = dishLists;
        this.getViewModel = getViewModel;
        this.selected_s_map = new ArrayList<>(selected_s_map);
        this.itemMap =itemMap;

    }

    @NonNull
    @Override
    public DishAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.dish_checkbox_list, parent, false);
        return new DishAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DishAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if(itemMap==null)
        {
            itemMap=new LinkedHashMap<>();
            MyLog.e(TAG,"itemMap is null");
        }
        //get oldDateTimeCount
        getViewModel.getOldDateTimeCountLiveData().observe((LifecycleOwner) context, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                oldDateTimeCount=s;
            }
        });
        //get head count
        getViewModel.getS_countLiveData().observe((LifecycleOwner) context, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                s_count = s;
            }
        });

        //get func title
        getViewModel.getFunc_title_Mutable().observe((LifecycleOwner) context, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                funcTitle = s;
            }
        });
        //get session title
        getViewModel.getSession_titleMutable().observe((LifecycleOwner) context, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                sessionTitle = s;
            }
        });
        //get header title
        getViewModel.getHeader_title_Mutable().observe((LifecycleOwner) context, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                header_title = s;
            }
        });
        //get item title
        getViewModel.getItem_title_Mutable().observe((LifecycleOwner) context, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                item_title = s;
            }
        });
        //get Session Date Time
        getViewModel.getF_mapsdtMutableLiveData().observe((LifecycleOwner) context, new Observer<LinkedHashMap<String, List<SessionDateTime>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, List<SessionDateTime>> stringListLinkedHashMap) {
                sessionDateTimes = stringListLinkedHashMap.get(funcTitle + "-" + sessionTitle);
                MyLog.e(TAG, "dateTimes>>s_session_title>>title>>" + funcTitle + "-" + sessionTitle);
                MyLog.e(TAG, "dateTimes>>s_session_title>>stringListLinkedHashMap>>" + new GsonBuilder().setPrettyPrinting().create().toJson(stringListLinkedHashMap));
                MyLog.e(TAG, "dateTimes>>s_session_title>>sessionDateTimes>>" + new GsonBuilder().setPrettyPrinting().create().toJson(sessionDateTimes));

            }
        });

        //get header map
        getViewModel.getHeaderMapMutableLiveData().observe((LifecycleOwner) context, new Observer<LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>> stringLinkedHashMapLinkedHashMap) {
                headerMap=new LinkedHashMap<>(stringLinkedHashMapLinkedHashMap);
            }
        });

        //get edit fun map
        getViewModel.getEditFuncMapMutableLiveData().observe((LifecycleOwner) context, new Observer<LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedDishList>>>>>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<SelectedDishList>>>>>> stringLinkedHashMapLinkedHashMap) {
                editFunc_Map=stringLinkedHashMapLinkedHashMap;
            }
        });

        final DishList dishLists1 = dishLists.get(position);

        //check if checked list item already selected
        if (selected_s_map.size() > 0) {
            for (int k = 0; k < selected_s_map.size(); k++) {
                checkedLists = selected_s_map.get(k).get(item_title);
                if (checkedLists != null) {
                    for (int i = 0; i < checkedLists.size(); i++) {
                        final CheckedList checkedLists1 = checkedLists.get(i);
                        MyLog.e(TAG, "checked>>" + checkedLists1.getPosition());
                        MyLog.e(TAG, "checked>>" + position);
                        if (checkedLists1.getItemList().equals(dishLists1.getDish())) {
                            //MyLog.e(TAG, "checked>>" + checkedLists1.getItemList());
                            holder.dish_check.setChecked(true);
                        }/*if (checkedLists1.getPosition() == position) {
                            //MyLog.e(TAG, "checked>>" + checkedLists1.getItemList());
                            holder.dish_check.setChecked(true);
                        }*/
                    }
                } else {
                    checkedLists=new ArrayList<>();
                    MyLog.e(TAG, "checked>> selected size is null>>");
                }
            }
        }
        else {
            MyLog.e(TAG, "checked>>selected  map size is null>>");
        }
        MyLog.e(TAG, "hashmap>>size>>" + selected_s_map.size());



        //final DishList dishLists1 = dishLists.get(position);
        holder.dish_check.setText(dishLists1.getDish());
        if (dishLists1.getBoolens().equals("true")) {
            String[] str = (dishLists1.getDish()).split("-");
            if (str.length > 1) {
                Spannable word = new SpannableString(str[0]);
                word.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorSecondary)), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.dish_check.setText(word);
                Spannable wordTwo = new SpannableString(str[1]);
                wordTwo.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorPrimary)), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.dish_check.append(wordTwo);
            } else {
                holder.dish_check.setText(dishLists1.getDish());
                holder.dish_check.setTextColor(context.getResources().getColor(R.color.colorSecondary));
            }
        } else if (dishLists1.getBoolens().equals("false")) {
            holder.dish_check.setTextColor(context.getResources().getColor(R.color.light_gray));
            holder.dish_check.setEnabled(false);

        }


        holder.dish_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (dishLists1.getDish() != null) {
                    if (checkedLists == null) {
                        MyLog.e(TAG, "placeorders>>date_time checkedLists null");
                        checkedLists = new ArrayList<>();
                    } else {
                        if (holder.dish_check.isChecked()) {

                            CheckedList checkedLists1 = new CheckedList(
                                    dishLists1.getDish(),
                                    position
                            );
                            checkedLists.add(checkedLists1);


                            //notifyDataSetChanged();
                        } else {
                            //unchecked.getUnchecked(dishLists1.getItem());
                            GetUncheckLists(dishLists1.getDish());

                        }
                    }
                    getViewModel.setCheckedLists(checkedLists);
                    itemMap.put(item_title,checkedLists);
                    headerMap.put(header_title, itemMap);
                    String date = (sessionDateTimes.get(0).getDate()).replace("/", "-");
                    String s = sessionTitle + "!" + (date + " " + sessionDateTimes.get(0).getTime()) + "/" + s_count;
                    MyLog.e(TAG, "placeorders>>date_time>>" + s);
                    sessionMap.put(s, headerMap);
                    funcMap.put(funcTitle, sessionMap);
                    getViewModel.setSessionDateTimeCount(s);

                    //set session list
                    Set<String> stringSet = sessionMap.keySet();
                    List<String> aList = new ArrayList<String>(stringSet.size());
                    for (String x : stringSet)
                        aList.add(x);

                    //MyLog.e(TAG,"chs>>list size>> "+ aList.size());
                    selectedSessionLists.clear();
                    for (int i = 0; i < aList.size(); i++) {
                        String[] scb = (aList.get(i)).split("/");
                        String count = scb[1];
                        String[] arr = (scb[0]).split("!");

                        //set selected session list and session date and time
                        MyLog.e(TAG, "chs>>list header>> " + arr[0]);
                        SelectedSessionList list = new SelectedSessionList();
                        list.setBolen(null);
                        list.setSession_title(arr[0]);
                        list.setTime(arr[1]);
                        list.setS_count(count);
                        selectedSessionLists.add(list);
                    }

                   /* //set header map
                    getViewModel.setHeaderMap(headerMap);
                    //set session map
                    getViewModel.setSessionMap(sessionMap);*/
                    //set header map
                    getViewModel.setHeaderMap(headerMap);
                    //set func map
                    getViewModel.setFuncMap(funcMap);
                    MyLog.e(TAG, "selected_s_map>>size>>" + selected_s_map.size());
                    selected_s_map.add(itemMap);
                    getViewModel.setCheck_s_map(selected_s_map);
                    //set selected session
                    getViewModel.setSelectedSessionLists(selectedSessionLists);




                    if(oldDateTimeCount!=null) {
                        MyLog.e(TAG,"oldDateTimeCount is not null");

                        //oldDateTimeCount
                        String[] str = oldDateTimeCount.split("_");
                        String oldDate = str[0];
                        String oldTime = str[1];
                        String oldCount = str[2];
                        //set selected dish into edit item map
                        for (int i = 0; i < checkedLists.size(); i++) {
                            SelectedDishList list = new SelectedDishList(
                                    checkedLists.get(i).getItemList()
                            );
                            selectedDishLists.add(list);
                        }
                        oldDate = oldDate.replace("/", "-");
                        editDateMap = editFunc_Map.get(funcTitle);
                        editSessionMap = editDateMap.get(oldDate);
                        String s1 = sessionTitle + "!" + oldTime + "/" + oldCount;
                        editHeaderMap = editSessionMap.get(s1);
                        editItemMap = editHeaderMap.get(header_title);
                    /*oldSelectedDishLists=new ArrayList<>();
                    oldSelectedDishLists=editItemMap.get(item_title);*/
                        editItemMap.put(item_title, selectedDishLists);
                        getViewModel.setEditItemMap(editItemMap);
                    }
                    else
                    {
                        MyLog.e(TAG,"oldDateTimeCount is null");
                    }


                    Gson gson = new Gson();
                    String json = gson.toJson(checkedLists);
                    new SharedPreferences_data(context).setChecked_item_list(json);



                }
            }

            private void GetUncheckLists(String dish) {
                for (int i = 0; i < checkedLists.size(); i++) {
                    if (dish.equals(checkedLists.get(i).getItemList())) {
                        checkedLists.remove(i);
                        break;
                    }
                }
            }
        });
        /*
        if(headerMap==null)
        {
            headerMap=new LinkedHashMap<>();
            MyLog.e(TAG,"headerMap is null");
        }

        final ItemList itemList1 = itemLists.get(position);

        //img update soon
        //holder.header_img.setText(funList1.getImg());


        //get func title
        getViewModel.getFunc_title_Mutable().observe((LifecycleOwner) context, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                funcTitle=s;
            }
        });
        //get session title
        getViewModel.getSession_titleMutable().observe((LifecycleOwner) context, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                sessionTitle=s;
            }
        });
        //get Session Date Time
        getViewModel.getF_mapsdtMutableLiveData().observe((LifecycleOwner) context, new Observer<LinkedHashMap<String, List<SessionDateTime>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, List<SessionDateTime>> stringListLinkedHashMap) {
                sessionDateTimes = stringListLinkedHashMap.get(funcTitle + "-" + sessionTitle);
            }
        });
        //get session map
        getViewModel.getSessionMapMutableLiveData().observe((LifecycleOwner) context, new Observer<LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>>>() {
            @Override
            public void onChanged(LinkedHashMap<String, LinkedHashMap<String, List<CheckedList>>> stringLinkedHashMapLinkedHashMap) {
                sessionMap=stringLinkedHashMapLinkedHashMap;
            }
        });
        //get head count
        getViewModel.getS_countLiveData().observe((LifecycleOwner) context, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                s_count=s;
            }
        });

        //check if checked list item already selected
        if (selected_s_map.size() > 0) {
            for (int k = 0; k < selected_s_map.size(); k++) {
                checkedLists = selected_s_map.get(k).get(header);
                if (checkedLists != null) {
                    for (int i = 0; i < checkedLists.size(); i++) {
                        final CheckedList checkedLists1 = checkedLists.get(i);
                        MyLog.e(TAG, "checked>>" + checkedLists1.getPosition());
                        MyLog.e(TAG, "checked>>" + position);
                        if (checkedLists1.getPosition() == position) {
                            //MyLog.e(TAG, "checked>>" + checkedLists1.getItemList());
                            holder.item_check.setChecked(true);

                        }
                    }
                } else {
                    checkedLists=new ArrayList<>();
                    MyLog.e(TAG, "checked>> selected size is null>>");
                }
            }
        }
        else {
            MyLog.e(TAG, "checked>>selected  map size is null>>");
        }
        MyLog.e(TAG, "hashmap>>size>>" + selected_s_map.size());


        holder.item_check.setText(itemList1.getItem());
        if(itemList1.getSelected().equals("true"))
        {
            holder.item_check.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }
        else {
            holder.item_check.setTextColor(context.getResources().getColor(R.color.light_gray));
            holder.item_check.setEnabled(false);
        }

        holder.item_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (itemList1.getItem() != null) {
                    if (checkedLists == null) {
                        MyLog.e(TAG, "placeorders>>date_time checkedLists null");
                        checkedLists = new ArrayList<>();
                    }
                    else
                    {
                    if (holder.item_check.isChecked()) {

                            CheckedList checkedLists1 = new CheckedList(
                                    itemList1.getItem(),
                                    position
                            );
                            checkedLists.add(checkedLists1);



                        //notifyDataSetChanged();
                    } else {
                        //unchecked.getUnchecked(itemList1.getItem());
                        GetUncheckList(itemList1.getItem());

                    }
                }
                    getViewModel.setCheckedLists(checkedLists);
                    headerMap.put(header, checkedLists);
                    String date=(sessionDateTimes.get(0).getDate()).replace("/","-");
                    String s=sessionTitle+"!"+(date+" "+sessionDateTimes.get(0).getTime())+"/"+s_count;
                    MyLog.e(TAG, "placeorders>>date_time>>" + s);
                    sessionMap.put(s,headerMap);
                    funcMap.put(funcTitle,sessionMap);

                    //set session list
                    Set<String> stringSet = sessionMap.keySet();
                    List<String> aList = new ArrayList<String>(stringSet.size());
                    for (String x : stringSet)
                        aList.add(x);

                    //MyLog.e(TAG,"chs>>list size>> "+ aList.size());
                    selectedSessionLists.clear();
                    for (int i = 0; i < aList.size(); i++) {
                        String[] scb = (aList.get(i)).split("/");
                        String count=scb[1];
                        String[] arr = (scb[0]).split("!");

                        //set selected session list and session date and time
                        MyLog.e(TAG, "chs>>list header>> " + arr[0]);
                        SelectedSessionList list = new SelectedSessionList();
                        list.setBolen(null);
                        list.setSession_title(arr[0]);
                        list.setTime(arr[1]);
                        list.setS_count(count);
                        selectedSessionLists.add(list);
                    }

                    //set header map
                    getViewModel.setHeaderMap(headerMap);
                    //set session map
                    getViewModel.setSessionMap(sessionMap);
                    //set func map
                    getViewModel.setFuncMap(funcMap);
                    MyLog.e(TAG, "selected_s_map>>size>>" + selected_s_map.size());
                    selected_s_map.add(headerMap);
                    getViewModel.setCheck_s_map(selected_s_map);
                    //set selected session
                    getViewModel.setSelectedSessionLists(selectedSessionLists);

                    Gson gson = new Gson();
                    String json = gson.toJson(checkedLists);
                    new SharedPreferences_data(context).setChecked_item_list(json);


                }
            }
        });


    }

    private void GetUncheckList (String item){
        for (int i = 0; i < checkedLists.size(); i++) {
            if (item.equals(checkedLists.get(i).getItemList())) {
                checkedLists.remove(i);
                break;
            }
        }*/
    }

    @Override
    public int getItemCount() {
        return dishLists.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private CheckBox dish_check;

        public ViewHolder(View view) {
            super(view);
            dish_check = view.findViewById(R.id.dish_check);


        }
    }
}