package com.example.kcs.Fragment.Profile.MyOrders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kcs.Classes.MyLog;
import com.example.kcs.Classes.SharedPreferences_data;
import com.example.kcs.MyViewModel;
import com.example.kcs.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyOrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyOrdersFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageView back_btn;
    private MyViewModel myViewModel;
    private RecyclerView recyclerview_my_orders;
    private List<MyOrdersList> myOrdersList=new ArrayList<>();
    private MyOrdersAdapter myOrdersAdapter;
    private String header,func,s_user_name;
    private String item="";
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String TAG = "MyOrdersFragment";

    public MyOrdersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyOrdersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyOrdersFragment newInstance(String param1, String param2) {
        MyOrdersFragment fragment = new MyOrdersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myViewModel = new ViewModelProvider(getActivity()).get(MyViewModel.class);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_orders, container, false);

        back_btn=view.findViewById(R.id.back_btn);
        recyclerview_my_orders=view.findViewById(R.id.recyclerview_my_orders);


        recyclerview_my_orders.setHasFixedSize(true);
        recyclerview_my_orders.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        myOrdersList=new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        s_user_name=new SharedPreferences_data(getContext()).getS_user_name();

        databaseReference = firebaseDatabase.getReference("Orders").child(s_user_name);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MyLog.e(TAG, "snap>>" + snapshot);
                int size=0;
                for (DataSnapshot datas : snapshot.getChildren()) {
                    MyLog.e(TAG, "snap>>snap childern>>" + snapshot.getValue().toString());
                    MyLog.e(TAG, "snap>>snap childern>>" + snapshot.getKey());
                    MyLog.e(TAG, "snap>>datas>>" + datas);
                    func=datas.getKey().toString();
                    for (DataSnapshot dataSnapshot : datas.getChildren()) {
                        header=dataSnapshot.getKey().toString();
                        MyLog.e(TAG, "snap>>datasnap>>" + dataSnapshot);
                        item+=dataSnapshot.getValue().toString()+" ";
                        size++;

                        MyOrdersList orderLists1 = new MyOrdersList(
                                func,
                                header,
                                item,
                                size
                        );
                        size=0;
                        myOrdersList.add(orderLists1);
                    }

                }
                MyLog.e(TAG,"deta>>\n"+ new GsonBuilder().setPrettyPrinting().create().toJson(myOrdersList));
                myOrdersAdapter=new MyOrdersAdapter(getContext(),myOrdersList);
                recyclerview_my_orders.setAdapter(myOrdersAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });




        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myViewModel.setI_value(3);
               /* Fragment fragment=new HomeFragment();
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.Fragment, fragment).commit();*/
            }
        });
        return view;
    }
}