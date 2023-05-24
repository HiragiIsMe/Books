package com.example.asynctest;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.progressindicator.CircularProgressIndicator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
    RecyclerView recyclerView;
    EditText search;
    Session session;
    List<Book> book;
    CircularProgressIndicator progressIndicator;
    RecycleHomeAdapter.RecycleViewOnclick listener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        search = v.findViewById(R.id.searchBook);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchBook search = new searchBook();
                search.execute();
            }
        });

        progressIndicator = v.findViewById(R.id.progressHome);
        session = new Session(getContext());
        book = new ArrayList<>();
        getBook getBook = new getBook();
        getBook.execute();

        recyclerView = v.findViewById(R.id.recycleviewHome);

        listener = new RecycleHomeAdapter.RecycleViewOnclick() {


            @Override
            public void onItemClick(View v, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("idBook", book.get(position).getId());

                DetailBookFragment frg = new DetailBookFragment();
                frg.setArguments(bundle);

                getFragmentManager().beginTransaction().replace(R.id.mainframe, frg).commit();
            }
        };
        return v;
    }

    public class getBook extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = RequestTemplate.getData("http://10.0.2.2:5000/Api/Book", session.getToken());

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressIndicator.setVisibility(View.GONE);
            try{
                JSONArray jsonArray = new JSONArray(s);
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject obj = jsonArray.getJSONObject(i);
                    book.add(new Book(obj.getString("id"), obj.getString("name"), obj.getString("authors")));
                }
                JSONObject obj = jsonArray.getJSONObject(1);
                Log.e("datas", obj.getString("name"));

            }catch (Exception e){
                e.printStackTrace();
            }

            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            RecycleHomeAdapter adapter = new RecycleHomeAdapter(book, getContext(), listener);
            recyclerView.setAdapter(adapter);
        }
    }

    public class searchBook extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = RequestTemplate.getData("http://10.0.2.2:5000/Api/Book?searchText="+search.getText().toString(), session.getToken());

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressIndicator.setVisibility(View.GONE);
            Log.e("data", s);
            try{
                book.clear();
                JSONArray jsonArray = new JSONArray(s);
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject obj = jsonArray.getJSONObject(i);
                    book.add(new Book(obj.getString("id"), obj.getString("name"), obj.getString("authors")));
                }

            }catch (Exception e){
                e.printStackTrace();
            }

            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            RecycleHomeAdapter adapter = new RecycleHomeAdapter(book, getContext(), listener);
            recyclerView.setAdapter(adapter);

        }
    }
}