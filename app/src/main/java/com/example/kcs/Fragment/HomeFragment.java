package com.example.kcs.Fragment;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.kcs.Fragment.Func.FunAdapter;
import com.example.kcs.Fragment.Func.FunList;
import com.example.kcs.Fragment.Header.HeaderAdapter;
import com.example.kcs.Fragment.Header.HeaderList;
import com.example.kcs.Fragment.Items.ItemList;

import com.example.kcs.R;
import com.example.kcs.ViewModel.GetViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //Header
    private RecyclerView recyclerview_header;
    private HeaderAdapter headerAdapter;
    private List<HeaderList> headerList;
    //Function
    private RecyclerView recyclerview_fun;
    private FunAdapter funAdapter;
    private List<FunList> funLists;
    //Img
    private RecyclerView recyclerview_img;
    private ImageView profile;
    //firebase auth
    private FirebaseAuth mAuth;
    //Lottie anim
    private LottieAnimationView lottie_loading;
    //anim
    private Animation slide_down_anim,slide_up_anim,fade_in_anim;
    private ConstraintLayout bg_banner,head_layout;
    //firebase database retrieve
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private List<ItemList> itemLists=new ArrayList<>();
    private String TAG="HomeFragment";
    //private MyViewModel myViewModel;
    private GetViewModel getViewModel;

    public HomeFragment() {

    }


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //myViewModel = new ViewModelProvider(getActivity()).get(MyViewModel.class);
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
        View view= inflater.inflate(R.layout.fragment_home, container, false);

        //id's
        profile=view.findViewById(R.id.profile);
        recyclerview_header=view.findViewById(R.id.recyclerview_header);
        recyclerview_fun=view.findViewById(R.id.recyclerview_fun);
        recyclerview_img=view.findViewById(R.id.recyclerview_img);



        //recyclerview_img
        //update soon
        /*recyclerview_img.setHasFixedSize(true);
        recyclerview_img.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));*/
        
        //new Array list
        funLists=new ArrayList<>();
        headerList=new ArrayList<>();

        //firebase
        firebaseDatabase = FirebaseDatabase.getInstance();






        //get fun list
        getViewModel.getFunMutableList().observe(getViewLifecycleOwner(), new Observer<List<FunList>>() {
            @Override
            public void onChanged(List<FunList> funList1) {
                funLists=funList1;
                //recyclerview_fun
                recyclerview_fun.setHasFixedSize(true);
                recyclerview_fun.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                funAdapter=new FunAdapter(getContext(),funLists,getViewModel);
                recyclerview_fun.setAdapter(funAdapter);
            }
        });
        
        
        
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getViewModel.setI_value(3);
            }
        });
        return view;
    }



}