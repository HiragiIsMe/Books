package com.example.asynctest;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.progressindicator.CircularProgressIndicator;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailBookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailBookFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String idBook;
    ImageView image;
    TextView name, authors, isbn, publisher, available, description;
    CircularProgressIndicator progress;
    Bundle bundle;
    Session session;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DetailBookFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailBookFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailBookFragment newInstance(String param1, String param2) {
        DetailBookFragment fragment = new DetailBookFragment();
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
        View v = inflater.inflate(R.layout.fragment_detail_book, container, false);

        session = new Session(getContext());
        Bundle bundle = getArguments();
        idBook = bundle.getString("idBook");
        name = v.findViewById(R.id.bookName);
        authors = v.findViewById(R.id.bookAuthors);
        isbn = v.findViewById(R.id.bookIsbn);
        publisher = v.findViewById(R.id.bookPublisher);
        available = v.findViewById(R.id.bookAvb);
        description = v.findViewById(R.id.bookDesc);
        progress = v.findViewById(R.id.detailProgress);
        image = v.findViewById(R.id.bookImage);

        setBook setBook = new setBook();
        setBook.execute();


        return v;
    }

    public class setBook extends AsyncTask<String, String, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progress.setVisibility(View.GONE);
            try{
                JSONObject obj = new JSONObject(s);
                name.setText("Name : " + obj.getString("name"));
                authors.setText("Author : " + obj.getString("authors"));
                isbn.setText("isbn : " + obj.getString("isbn"));
                publisher.setText("Publisher : " + obj.getString("publisher"));
                available.setText("Available : " + String.valueOf(obj.getInt("available")));
                description.setText(obj.getString("description"));
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = RequestTemplate.getData("http://10.0.2.2:5000/Api/Book/" + idBook, session.getToken());
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
        }
    }
}