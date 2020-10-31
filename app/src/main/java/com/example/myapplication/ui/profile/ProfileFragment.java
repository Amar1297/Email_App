package com.example.myapplication.ui.profile;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.myapplication.EmailAdapter;
import com.example.myapplication.EmailData;
import com.example.myapplication.R;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileFragment extends Fragment {

    private final String Profile_URL="https://api2.funedulearn.com/init/demo-profile";
    private ProfileViewModel profileViewModel;
    private SwipeRefreshLayout layout;
    private ShimmerFrameLayout frameLayout;
    Handler handler;
    private ImageView imageView;
    private TextView name,emailid;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        imageView=root.findViewById(R.id.imageViewprof);
        name=root.findViewById(R.id.nameprof);
        emailid=root.findViewById(R.id.emailidprof);
        layout=root.findViewById(R.id.swipeforprofile);
        frameLayout=root.findViewById(R.id.shinpro);
        handler=new Handler();

        layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loaddata();
                layout.setRefreshing(false);
            }
        });

        loaddata();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                frameLayout.stopShimmer();
                frameLayout.setVisibility(View.GONE);
                layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        loaddata();
                        layout.setRefreshing(false);
                    }
                });
            }
        },2000);




        return root;
    }

    private void loaddata() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                Profile_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONObject profi=jsonObject.getJSONObject("profile");
                            String myname=profi.getString("name");
                            String imgurl=profi.getString("image");

                            name.setText(myname);
                            emailid.setText(myname.toLowerCase().replace(" ","")+"@gmail.com");

                            Glide.with(getContext().getApplicationContext())
                                    .load(imgurl)
                                    .into(imageView);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);
    }
}