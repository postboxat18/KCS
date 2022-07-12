package com.example.adm.ViewModel;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.adm.Classes.CheckEmail;
import com.example.adm.Classes.MyLog;
import com.example.adm.Classes.SessionList;
import com.example.adm.Classes.SharedPreferences_data;
import com.example.adm.Fragments.Control_Panel.Dish.DishList;
import com.example.adm.Fragments.Control_Panel.Func.FuncList;
import com.example.adm.Fragments.Control_Panel.Header.HeaderList;
import com.example.adm.Fragments.Control_Panel.Item.ItemArrayList;
import com.example.adm.Fragments.Control_Panel.Func.UpdatedList;
import com.example.adm.Fragments.Orders.BottomSheet.Classes.OrderDishLists;
import com.example.adm.Fragments.Orders.Classes.UserItemList;
import com.example.adm.Fragments.Orders.BottomSheet.Classes.OrderLists;
import com.example.adm.Fragments.Users.UserDetailsList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

public class GetViewModel extends AndroidViewModel {
    //check user login
    private MutableLiveData<Boolean> EmailMutable = new MutableLiveData<>();
    private String email;
    private boolean check_email = false;
    //firebase database retrieve
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    //refresh
    private Integer refresh=-1;
    private MutableLiveData<Integer> refreshLiveData=new MutableLiveData<>();

    //Selected Header
    private String header_title;
    private MutableLiveData<String> header_title_Mutable = new MutableLiveData<>();

    //Selected Func
    private String func_title;
    private MutableLiveData<String> func_title_Mutable = new MutableLiveData<>();
    //Selected item
    private String item_title;
    private MutableLiveData<String> item_title_Mutable = new MutableLiveData<>();

    //fragment
    private Integer i_value;
    private MutableLiveData<Integer> value = new MutableLiveData<>();

    //Orders list
    /*private List<OrderLists> orderLists = new ArrayList<>();
    private MutableLiveData<List<OrderLists>> orderListsMutable = new MutableLiveData<>();*/


    //User list
    private List<UserDetailsList> userLists = new ArrayList<>();
    private MutableLiveData<List<UserDetailsList>> UserListMutable = new MutableLiveData<>();


    //user-item list
    private List<UserItemList> userItemLists = new ArrayList<>();

    //Linked HashMap for order deatils
    /*private LinkedHashMap<String, List<UserItemList>> f_map = new LinkedHashMap<>();
    private MutableLiveData<LinkedHashMap<String, List<UserItemList>>> f_mapMutable = new MutableLiveData<>();
    private List<LinkedHashMap<String, List<UserItemList>>> s_map = new ArrayList<>();
    private MutableLiveData<List<LinkedHashMap<String, List<UserItemList>>>> s_mapMutable = new MutableLiveData<>();*/

    //get Data from Firebase database
    //header list
    private List<HeaderList> headerList = new ArrayList<>();
    private MutableLiveData<List<HeaderList>> headerListMutableLiveData = new MutableLiveData<>();
    //func list
    private List<FuncList> funcList = new ArrayList<>();
    private MutableLiveData<List<FuncList>> funcListMutableLiveData = new MutableLiveData<>();
    //item list
    private List<ItemArrayList> itemList = new ArrayList<>();
    private MutableLiveData<List<ItemArrayList>> itemListMutableLiveData = new MutableLiveData<>();
    //dish list
    private List<DishList> dishLists = new ArrayList<>();
    private MutableLiveData<List<DishList>> dishListsMutableLiveData = new MutableLiveData<>();
    //item map
    private LinkedHashMap<String, LinkedHashMap<String, List<DishList>>> itemArrayListMap = new LinkedHashMap<>();
    private MutableLiveData<LinkedHashMap<String, LinkedHashMap<String, List<DishList>>>> itemArrayListMapMutableLiveData = new MutableLiveData<>();
    //dish map
    private LinkedHashMap<String, List<DishList>> dishListMap = new LinkedHashMap<>();
    private MutableLiveData<LinkedHashMap<String, List<DishList>>> dishListMapMutableLiveData = new MutableLiveData<>();
    //session list
    private List<SessionList> sessionLists = new ArrayList<>();
    /*private LinkedHashMap<String, List<SessionList>> ss_f_map = new LinkedHashMap<>();
    private MutableLiveData<LinkedHashMap<String, List<SessionList>>> ss_f_mapMutableLiveData = new MutableLiveData<>();
*/

    //updated list
    private List<UpdatedList> updatedLists = new ArrayList<>();
    private MutableLiveData<List<UpdatedList>> updatedListsMutableLiveData = new MutableLiveData<>();

    //selected Item list
    private List<OrderDishLists> orderDishLists = new ArrayList<>();
    private MutableLiveData<List<OrderDishLists>> orderItemListsMutableLiveData = new MutableLiveData<>();
    /*private LinkedHashMap<String, List<OrderItemLists>> orderItemList_f_map = new LinkedHashMap<>();
    private MutableLiveData<LinkedHashMap<String, List<OrderItemLists>>> orderItemList_f_mapMutableLiveData = new MutableLiveData<>();*/

    //orderlist to get item list view
    private OrderLists orderListsView;
    private MutableLiveData<OrderLists> orderListsViewMutableLiveData = new MutableLiveData<>();

    //selected list
    /*private List<SelectedHeader> selectedHeaders = new ArrayList<>();
    private MutableLiveData<List<SelectedHeader>> selectedHeadersMutableLiveData = new MutableLiveData<>();*/

    //selected session and header in hash map
    /*private LinkedHashMap<String, List<SelectedHeader>> sh_f_map = new LinkedHashMap<>();
    private MutableLiveData<LinkedHashMap<String, List<SelectedHeader>>> sh_f_mapMutableLiveData = new MutableLiveData<>();*/

    private String TAG = "ViewClassModel";
    String s_user_name, func, header, session_title, item, selected;
    //Email check
    private List<CheckEmail> checkEmails = new ArrayList<>();
    private MutableLiveData<List<CheckEmail>> checkEmailsMutableLiveData = new MutableLiveData<>();
    //My Order HashMap
    private List<OrderDishLists> o_orderDishLists = new ArrayList<>();

    //order map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderDishLists>>>>>>> orderMap = new LinkedHashMap<>();
    private MutableLiveData<LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderDishLists>>>>>>>> orderMapMutableLiveData = new MutableLiveData<>();
    //func map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderDishLists>>>>>> orderFunc_Map = new LinkedHashMap<>();
    private MutableLiveData<LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderDishLists>>>>>>> orderFunc_MapMutableLiveData = new MutableLiveData<>();
    //Date map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderDishLists>>>>> orderDateMap = new LinkedHashMap<>();
    private MutableLiveData<LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderDishLists>>>>>> orderDate_MapMutableLiveData = new MutableLiveData<>();
    //Session map
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderDishLists>>>> orderSessionMap = new LinkedHashMap<>();
    private MutableLiveData<LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderDishLists>>>>> orderSessionMapMutableLiveData = new MutableLiveData<>();
    //Header map
    private LinkedHashMap<String, LinkedHashMap<String, List<OrderDishLists>>> orderHeaderMap = new LinkedHashMap<>();
    private MutableLiveData<LinkedHashMap<String, LinkedHashMap<String, List<OrderDishLists>>>> orderHeaderMapMutableLiveData = new MutableLiveData<>();
    //Item map
    private LinkedHashMap<String, List<OrderDishLists>> orderItemMap = new LinkedHashMap<>();
    private MutableLiveData<LinkedHashMap<String, List<OrderDishLists>>> orderItemMapMutableLiveData = new MutableLiveData<>();
    //click on date map
    private String dateString;
    private MutableLiveData<String> dateStringLive = new MutableLiveData<>();

    public GetViewModel(@NonNull Application application) {
        super(application);
        //firebase
        firebaseDatabase = FirebaseDatabase.getInstance();

        CheckUserDetails();

    }

    public MutableLiveData<Integer> getRefreshLiveData() {
        return refreshLiveData;
    }

    public void setRefresh(Integer refresh) {
        this.refresh = refresh;
        this.refreshLiveData.postValue(refresh);
    }

    public MutableLiveData<List<DishList>> getDishListsMutableLiveData() {
        return dishListsMutableLiveData;
    }

    public void setDishLists(List<DishList> dishLists) {
        this.dishLists = dishLists;
        this.dishListsMutableLiveData.postValue(dishLists);
    }

    public void setItem_title(String item_title) {
        this.item_title = item_title;
        this.item_title_Mutable.postValue(item_title);
    }

    public MutableLiveData<String> getItem_title_Mutable() {
        return item_title_Mutable;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
        this.dateStringLive.postValue(dateString);
    }

    public MutableLiveData<String> getDateStringLive() {
        return dateStringLive;
    }

    public MutableLiveData<LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<OrderDishLists>>>>>>>> getOrderMapMutableLiveData() {
        return orderMapMutableLiveData;
    }

    private void CheckUserDetails() {
        checkEmails = new ArrayList<>();
        databaseReference = firebaseDatabase.getReference("Users-Id");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MyLog.e(TAG, "snap>>" + snapshot);
                for (DataSnapshot datas : snapshot.getChildren()) {
                    MyLog.e(TAG, "error>>at firebase  emails " + datas.child("email").getValue().toString());
                    CheckEmail checkEmails1 = new CheckEmail(
                            datas.child("email").getValue().toString()
                    );
                    checkEmails.add(checkEmails1);
                }
                MyLog.e(TAG, "errors>>at firebase  emails out " + check_email);
                checkEmailsMutableLiveData.postValue(checkEmails);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplication(), "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void setItemArrayListMap(LinkedHashMap<String, LinkedHashMap<String, List<DishList>>> itemArrayListMap) {
        this.itemArrayListMap = itemArrayListMap;
        this.itemArrayListMapMutableLiveData.postValue(itemArrayListMap);
    }

    public MutableLiveData<LinkedHashMap<String, LinkedHashMap<String, List<DishList>>>> getItemArrayListMapMutableLiveData() {
        return itemArrayListMapMutableLiveData;
    }

    /*public MutableLiveData<LinkedHashMap<String, List<SessionList>>> getSs_f_mapMutableLiveData() {
        return ss_f_mapMutableLiveData;
    }*/

    public MutableLiveData<List<CheckEmail>> getCheckEmailsMutableLiveData() {
        return checkEmailsMutableLiveData;
    }

    /*public MutableLiveData<LinkedHashMap<String, List<SelectedHeader>>> getSh_f_mapMutableLiveData() {
        return sh_f_mapMutableLiveData;
    }*/

/*    public MutableLiveData<List<SessionList>> getSessionListsMutableLiveData() {
        return sessionListsMutableLiveData;
    }*/

    /*   public MutableLiveData<List<SelectedHeader>> getSelectedHeadersMutableLiveData() {
           return selectedHeadersMutableLiveData;
       }
   */
    public void setOrderListsView(OrderLists orderListsView) {
        this.orderListsView = orderListsView;
        this.orderListsViewMutableLiveData.postValue(orderListsView);
    }

    public MutableLiveData<OrderLists> getOrderListsViewMutableLiveData() {
        return orderListsViewMutableLiveData;
    }


   /* public MutableLiveData<LinkedHashMap<String, List<OrderItemLists>>> getOrderItemList_f_mapMutableLiveData() {
        return orderItemList_f_mapMutableLiveData;
    }*/

    public MutableLiveData<List<OrderDishLists>> getOrderItemListsMutableLiveData() {
        return orderItemListsMutableLiveData;
    }


    /*public MutableLiveData<List<LinkedHashMap<String, List<UserItemList>>>> getS_mapMutable() {
        return s_mapMutable;
    }*/

    public void setUpdatedLists(List<UpdatedList> updatedLists) {
        this.updatedLists = updatedLists;
        this.updatedListsMutableLiveData.postValue(updatedLists);
    }

    public MutableLiveData<List<UpdatedList>> getUpdatedListsMutableLiveData() {
        return updatedListsMutableLiveData;
    }

    public MutableLiveData<List<HeaderList>> getHeaderListMutableLiveData() {
        return headerListMutableLiveData;
    }

    public MutableLiveData<List<FuncList>> getFuncListMutableLiveData() {
        return funcListMutableLiveData;
    }

    public void setItemArrayList(List<ItemArrayList> itemList) {
        this.itemList = itemList;
        this.itemListMutableLiveData.postValue(itemList);

    }

    public MutableLiveData<LinkedHashMap<String, List<DishList>>> getDishListMapMutableLiveData() {
        return dishListMapMutableLiveData;
    }

    public void setDishListMap(LinkedHashMap<String, List<DishList>> dishListMap) {
        this.dishListMap = dishListMap;
        this.dishListMapMutableLiveData.postValue(dishListMap);
    }

    public MutableLiveData<List<ItemArrayList>> getItemListMutableLiveData() {
        return itemListMutableLiveData;
    }

    public void GetUpdateItem() {
        databaseReference = firebaseDatabase.getReference("Items").child("Selected&UnSelected").child("List");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MyLog.e(TAG, "list>>snap>>" + snapshot);
                int size = 0;
                itemArrayListMap = new LinkedHashMap<>();
                headerList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    //MyLog.e(TAG, "list>>datasnapshot>>" + dataSnapshot);
                    MyLog.e(TAG, "list>>datasnapshot>>key>>" + dataSnapshot.getKey().toString());
                    header_title = dataSnapshot.getKey().toString();
                    dishListMap = new LinkedHashMap<>();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        //MyLog.e(TAG, "list>>data>>" + data);
                        MyLog.e(TAG, "list>>data>>key>>" + data.getKey().toString());
                        String item = data.getKey().toString();
                        dishLists = new ArrayList<>();
                        for (DataSnapshot dishShot : data.getChildren()) {
                            MyLog.e(TAG, "list>>dishShot>>key>>" + dishShot.getKey().toString());
                            MyLog.e(TAG, "list>>dishShot>>value>>" + dishShot.getValue().toString());

                            DishList list = new DishList();
                            list.setDish(dishShot.getKey().toString());
                            list.setBolen(dishShot.getValue().toString());
                            dishLists.add(list);
                        }
                        dishListMap.put(item, dishLists);
                    }
                    itemArrayListMap.put(header_title, dishListMap);
                }

                itemArrayListMapMutableLiveData.postValue(itemArrayListMap);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplication(), "Fail to get data.", Toast.LENGTH_SHORT).show();
                MyLog.e(TAG, "list>>snap>>fun>>Fail to get data.");
            }
        });
    }

    public void GetUpdateFun() {
        databaseReference = firebaseDatabase.getReference("Items").child("Selected&UnSelected").child("Function");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MyLog.e(TAG, "home>>snap>>fun>>" + snapshot);
                int size = 0;
                funcList = new ArrayList<>();

                for (DataSnapshot datas : snapshot.getChildren()) {
                    String path = "" + size;
                    MyLog.e(TAG, "home>>snap>>fun>>" + path);
                    //MyLog.e(TAG, "home>>snap>>fun>>" +  datas.child("0").getValue().toString());
                    FuncList funList1 = new FuncList(
                            datas.getValue().toString());
                    funcList.add(funList1);
                    size++;

                }
                funcListMutableLiveData.postValue(funcList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplication(), "Fail to get data.", Toast.LENGTH_SHORT).show();
                MyLog.e(TAG, "home>>snap>>fun>>Fail to get data.");
            }
        });
    }

    public void GetUpdateHeader() {
        databaseReference = firebaseDatabase.getReference("Items").child("Selected&UnSelected").child("Category");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MyLog.e(TAG, "home>>snap>>" + snapshot);
                int size = 0;
                headerList = new ArrayList<>();
                for (DataSnapshot datas : snapshot.getChildren()) {

                    String path = "" + size;

                    //MyLog.e(TAG, "home>>snap>>" +  datas.child("a").getValue().toString());

                    HeaderList headerList1 = new HeaderList(
                            datas.getValue().toString()
                    );
                    headerList.add(headerList1);
                    size++;

                }

                headerListMutableLiveData.postValue(headerList);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplication(), "Fail to get data.", Toast.LENGTH_SHORT).show();
                MyLog.e(TAG, "home>>snap>>Fail to get data.");
            }
        });
    }


    public MutableLiveData<List<UserDetailsList>> getUserListMutable() {
        return UserListMutable;
    }

  /*  public MutableLiveData<LinkedHashMap<String, List<UserItemList>>> getF_mapMutable() {
        return f_mapMutable;
    }*/

    public void GetUserList() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users-Id");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //MyLog.e(TAG, "snap>>" + snapshot);
                for (DataSnapshot datas : snapshot.getChildren()) {
                  /*  MyLog.e(TAG, "snap>>" + datas.child("username").getValue().toString());
                    MyLog.e(TAG, "snap>>" + datas.child("email").getValue().toString());
                    MyLog.e(TAG, "snap>>" + datas.child("phone_number").getValue().toString());
                  */
                    UserDetailsList userList = new UserDetailsList(
                            datas.child("username").getValue().toString(),
                            datas.child("email").getValue().toString(),
                            datas.child("phone_number").getValue().toString()
                    );
                    userLists.add(userList);
                }
                UserListMutable.postValue(userLists);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplication(), "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*public MutableLiveData<List<OrderLists>> getOrderListsMutable() {
        return orderListsMutable;
    }*/

    public void GetOrdesList() {

        /////////////****************//////////////////////////////////////
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Orders");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot shot1) {
                int size = 0;
                MyLog.e(TAG, "onData>>snapshot>>" + shot1);
                //o_myOrderFuncLists = new ArrayList<>();
                for (DataSnapshot snapshot : shot1.getChildren()) {
                    s_user_name = snapshot.getKey().toString();
                    orderFunc_Map = new LinkedHashMap<>();
                    for (DataSnapshot datas : snapshot.getChildren()) {
                        //o_selectedSessionLists = new ArrayList<>();
                        MyLog.e(TAG, "onData>>datas>>" + datas);
                        MyLog.e(TAG, "MyOrdersAdapter>>func>>" + datas.getKey().toString());
                        func = datas.getKey().toString();
                        orderDateMap = new LinkedHashMap<>();
                        for (DataSnapshot dataTime : datas.getChildren()) {
                            String date = dataTime.getKey().toString();
                            orderSessionMap = new LinkedHashMap<>();
                            for (DataSnapshot ondata : dataTime.getChildren()) {
                                String ss = ondata.getKey().toString();
                                String[] scb = ss.split("-");
                                String count = scb[1];
                                String[] str = (scb[0]).split("_");
                                session_title = str[0];
                                MyLog.e(TAG, "MyOrdersAdapter>>session_title>>" + ondata.getKey().toString());
                                //o_myOrdersList = new ArrayList<>();
                                //o_selectedHeadersList = new ArrayList<>();
                                orderHeaderMap = new LinkedHashMap<>();
                                for (DataSnapshot dataSnapshot : ondata.getChildren()) {
                                    //get item list

                                    header_title = dataSnapshot.getKey().toString();
                                    MyLog.e(TAG, "onData>>dataonData>>" + dataSnapshot);
                                    MyLog.e(TAG, "onData>>header_title>>" + dataSnapshot.getKey().toString());
                                    size = 0;
                                    orderItemMap = new LinkedHashMap<>();
                                    for (DataSnapshot shot : dataSnapshot.getChildren()) {
                                        MyLog.e(TAG, "onData>>shots>>" + shot);
                                        MyLog.e(TAG, "onData>>shots>>" + shot.getKey().toString());
                                        String item = shot.getKey().toString();
                                        o_orderDishLists = new ArrayList<>();
                                        for (DataSnapshot dishShot : shot.getChildren()) {
                                            //get item list
                                            OrderDishLists itemLists = new OrderDishLists(
                                                    dishShot.getValue().toString()
                                            );
                                            o_orderDishLists.add(itemLists);
                                            size++;
                                        }
                                        orderItemMap.put(item, o_orderDishLists);
                                    }

                                    MyLog.e(TAG, "onData>>size>" + size);
                                    orderHeaderMap.put(header_title, orderItemMap);


                                }
                                orderSessionMap.put(ss, orderHeaderMap);

                            }
                            orderDateMap.put(date, orderSessionMap);
                        }
                        orderFunc_Map.put(func, orderDateMap);
                    }
                    orderMap.put(s_user_name, orderFunc_Map);

                }
                orderMapMutableLiveData.postValue(orderMap);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplication(), "Fail to get data.", Toast.LENGTH_SHORT).show();
                MyLog.e(TAG, "fail to get data " + error.getMessage());
            }
        });


    }




    public void setFunc_title(String func_title) {
        this.func_title = func_title;
        func_title_Mutable.postValue(func_title);
    }

    public MutableLiveData<String> getFunc_title_Mutable() {
        return func_title_Mutable;
    }

    public void setI_value(Integer i_value) {
        this.i_value = i_value;
        this.value.postValue(i_value);
    }
    public MutableLiveData<Integer> getValue() {
        return value;
    }

    public void setHeader_title(String header_title) {
        this.header_title = header_title;
        this.header_title_Mutable.postValue(header_title);
    }

    public MutableLiveData<String> getHeader_title_Mutable() {
        return header_title_Mutable;
    }

    public void setEmail(String email) {
        GetUserDeatils(email);
        this.email = email;

    }

    public MutableLiveData<Boolean> getEmailMutable() {
        return EmailMutable;
    }


    public boolean GetUserDeatils(String email) {

        databaseReference = firebaseDatabase.getReference("Admin");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // MyLog.e(TAG, "snap>>" + snapshot);
                for (DataSnapshot datas : snapshot.getChildren()) {
                    MyLog.e(TAG, "error>>at firebase  emails " + datas.child("email").getValue().toString());
                    if (Objects.equals(email, datas.child("email").getValue().toString())) {
                        new SharedPreferences_data(getApplication()).setS_email(datas.child("email").getValue().toString());
                        new SharedPreferences_data(getApplication()).setS_user_name(datas.child("username").getValue().toString());
                        new SharedPreferences_data(getApplication()).setS_phone_number(datas.child("phone_number").getValue().toString());
                        check_email = true;
                        EmailMutable.postValue(check_email);
                        MyLog.e(TAG, "boolean>>at firebase  emails " + check_email);
                        break;
                    } else {
                        continue;

                        //MyLog.e(TAG, "error>>at firebase  emails "+check_email);
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplication(), "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
        MyLog.e(TAG, "boolean>>at return " + check_email);
        return check_email;
    }

    public void getfunFragment(String fun) {
        this.setI_value(1);
        this.func_title_Mutable.postValue(fun);
    }

    public void UpdateListBase() {
        for (int i = 0; i < updatedLists.size(); i++) {
            int n = updatedLists.get(i).getPosition();
            databaseReference = firebaseDatabase.getReference("DUMMY").child(updatedLists.get(i).getTitle());
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    MyLog.e(TAG, "updatesnap>>" + snapshot);
                    MyLog.e(TAG, "updatesnap>>value>>" + snapshot.getValue());
                    databaseReference.setValue("CREAM");

                    Toast.makeText(getApplication(), "Successfully updated data.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplication(), "Fail to update data.", Toast.LENGTH_SHORT).show();
                }
            });

        }


    }


    public void updateItem(String header_title, String item, String dish, String selected, List<DishList> dishLists) {

        MyLog.e(TAG, "switchs>>updateItem");
        MyLog.e(TAG, "switchs>>dishLists>>"+new GsonBuilder().setPrettyPrinting().create().toJson(dishLists));
        MyLog.e(TAG, "switchs>>\nheader>>"+header_title+"\nitem>>"+item+"\ndish>>"+dish+"\nselected>>"+selected);



        if(dish==null) {
            for(int i=0;i<dishLists.size();i++) {
                databaseReference = firebaseDatabase.getReference("Items").child("Selected&UnSelected").child("List");
                String item_b = item + "_" + selected;
                MyLog.e(TAG, "switchs>>commit");
                //remove data list
                String oldData="";
                if(selected.equals("true"))
                {
                    oldData="false";
                }
                else
                {
                    oldData="true";
                }

                //remove data list
                databaseReference.child(header_title).child(item+"_"+oldData).removeValue();

                //add data list
                databaseReference.child(header_title).child(item_b).child(dishLists.get(i).getDish()).setValue(dishLists.get(i).getBolen());
            }
            //get list reload to get item map
            //GetUpdateItem();
            //refreshLiveData.postValue(3);
            setI_value(1);
        }else{
            databaseReference = firebaseDatabase.getReference("Items").child("Selected&UnSelected").child("List");
            String[]str=item.split("_");
            String oldItem=str[0];
            String newBolen=str[1];
            //remove data list
            String oldData="";
            if(newBolen.equals("true"))
            {
                oldData="false";
            }
            else
            {
                oldData="true";
            }

            //remove data list
            databaseReference.child(header_title).child(oldItem+"_"+oldData).removeValue();
            //add data list
            databaseReference.child(header_title).child(item).child(dish).setValue(selected);
        }

    }


    public void DeleteDate(String s_user_name, String funcTitle, String gn_date) {
        //remove old data
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Orders");
        MyLog.e(TAG, "dates>> funcTitle  " + funcTitle);
        MyLog.e(TAG, "dates>> s_user_name  " + s_user_name);
        MyLog.e(TAG, "dates>>date   " + gn_date);

        //remove data
        databaseReference.child(s_user_name).child(funcTitle).child(gn_date).removeValue();
        MyLog.e(TAG, "dates remove commit");
    }
}