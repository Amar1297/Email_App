package com.example.myapplication.ui.mail;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.EmailAdapter;
import com.example.myapplication.EmailData;
import com.example.myapplication.MainActivity2;
import com.example.myapplication.R;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MailFragment extends Fragment{

    private SearchView searchView=null;
    private SearchView.OnQueryTextListener queryTextListener;
    ShimmerFrameLayout frameLayout;
    Handler handler;
    private final String Mails_URL = "https://api2.funedulearn.com/init/demo-mails";
    private MailViewModel homeViewModel;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EmailAdapter adapter;
    private List<EmailData> emaildatalist;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(MailViewModel.class);
        View root = inflater.inflate(R.layout.fragment_mail, container, false);

        frameLayout=root.findViewById(R.id.shinmr1);
        handler=new Handler();
        swipeRefreshLayout = root.findViewById(R.id.refresh);
        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        swipeRefreshLayout.setColorSchemeColors(Color.RED);
        emaildatalist = new ArrayList<>();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                frameLayout.stopShimmer();
                frameLayout.setVisibility(View.GONE);
            }
        },2000);

        loadData();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                emaildatalist.clear();
                loadData();
                adapter.notifyDataSetChanged();

                swipeRefreshLayout.setRefreshing(false);
            }
        });
        return root;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }
    private void loadData() {



        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                Mails_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray =jsonObject.getJSONArray("data");

                        for(int i=0; i<jsonArray.length(); i++){

                              JSONObject o = jsonArray.getJSONObject(i);
                              int id=o.getInt("id");
                              String from =o.getString("from");
                              String subje=o.getString("subject");
                              JSONObject body=o.getJSONObject("body");

                                 Boolean attc=body.getBoolean("attachments");
                                 String message=body.getString("message");


                       EmailData modelActivity = new EmailData(i,from,subje,false,from);
            emaildatalist.add(modelActivity);
        }

        adapter = new EmailAdapter(emaildatalist, getActivity().getApplicationContext());

        recyclerView.setAdapter(adapter);


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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);


        MenuItem searchitem = menu.findItem(R.id.searchmail);
        SearchView searchView = new SearchView(((MainActivity2) getContext()).getSupportActionBar().getThemedContext());

        searchitem.setActionView(searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });


        MenuItem delete = menu.findItem(R.id.deletemail);
        int i=0;
        for(EmailData e:emaildatalist) {
            if (e.isSelected())
                i++;
        }
        if(i!=0)
        { delete.setVisible(true);
            } else {
                delete.setVisible(false);
           }



        super.onCreateOptionsMenu(menu, inflater);
    }
}