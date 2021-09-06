package com.example.fihhuda.tafseer.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fihhuda.R;
import com.example.fihhuda.quran.fullQuranReadingModels.Ayah;
import com.example.fihhuda.quran.fullQuranReadingModels.Surah;
import com.example.fihhuda.quran.viewsModel.ListeningViewModel;
import com.example.fihhuda.quran.viewsModel.QuranViewModel;
import com.example.fihhuda.tafseer.adapters.TafseerFragmentAdapter;
import com.example.fihhuda.tafseer.tafseerSearchModel.SearchModel;
import com.example.fihhuda.tafseer.tafseerSearchModel.forTafseerReadingInActivity.AyahsItem;

import java.util.List;

public class TafseerFragment extends Fragment implements SearchView.OnQueryTextListener{


    protected SearchView searchBar;
    protected Toolbar toolbar;
    protected RecyclerView searchRecycler;
    private View rootView;
 TafseerFragmentAdapter adapter ;
 LinearLayoutManager layoutManager ;
    private QuranViewModel viewModel;

    public TafseerFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_tafseer, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(rootView);
        viewModel = new ViewModelProvider(getActivity()).get(QuranViewModel.class);
           viewModel.context=getActivity();
    }

    private void initSearchRecycler(List<SearchModel> list) {
        layoutManager  =  new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
        adapter = new TafseerFragmentAdapter(list);
         searchRecycler.setAdapter(adapter);
         searchRecycler.setLayoutManager(layoutManager);
         onItemClick();
    }

    private void onItemClick() {
        adapter.setOnAyahClickListener(new TafseerFragmentAdapter.onAyahClickListener() {
            @Override
            public void onAyahClick(SearchModel model) {
                //start activity for tafseer reading
                //scroll to the position
                Intent intent = new Intent(getActivity(),TafseerDetailsForAllSura.class);
                intent.putExtra("suraName",model.getSurahName());
                startActivity(intent);
              }
        });
    }


    private void initView(View rootView) {
        searchBar = (SearchView) rootView.findViewById(R.id.search_bar);
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        searchRecycler = (RecyclerView) rootView.findViewById(R.id.search_Recycler);
        searchBar.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.e("submit",query);
        List<SearchModel> list= viewModel.getSearchedForByWord(query);
        Log.e("submit",list.get(0).getAyah()+"");

        initSearchRecycler(list);

     return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
       String word_searchFor=newText;
        Log.e("ch",newText);

        return false;
    }


}