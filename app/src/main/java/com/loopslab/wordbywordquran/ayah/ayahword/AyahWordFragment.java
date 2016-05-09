package com.loopslab.wordbywordquran.ayah.ayahword;


import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import com.loopslab.wordbywordquran.R;
import com.loopslab.wordbywordquran.surah.SurahDataSource;
import com.loopslab.wordbywordquran.various.settings.Config;

import java.util.ArrayList;

import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;


/**
 * A simple {@link Fragment} subclass.
 */
public class AyahWordFragment extends Fragment {


    long surah_id;
    long ayah_number;
    String lang;
    private ArrayList<AyahWord> ayahWordArrayList;
    private RecyclerView mRecyclerView;
    private AyahWordAdapter ayahWordAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public AyahWordFragment() {
        // Required empty public constructor
    }

    public static AyahWordFragment newInstance(Bundle bundle) {
        AyahWordFragment ayahWordFragment = new AyahWordFragment();
        ayahWordFragment.setArguments(bundle);
        return ayahWordFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        lang = sp.getString(Config.LANG, Config.defaultLang);
        surah_id = getArguments().getLong(SurahDataSource.SURAH_ID_TAG);
        ayah_number = getArguments().getLong(SurahDataSource.SURAH_AYAH_NUMBER);
        ayahWordArrayList = getAyahWordsBySurah(surah_id, ayah_number);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ayah_word, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_ayah_word_view);

        //for fast scroll
        VerticalRecyclerViewFastScroller fastScroller = (VerticalRecyclerViewFastScroller) view.findViewById(R.id.fast_scroller);

        // Connect the recycler to the scroller (to let the scroller scroll the list)
        fastScroller.setRecyclerView(mRecyclerView);

        // Connect the scroller to the recycler (to let the recycler scroll the scroller's handle)
        mRecyclerView.setOnScrollListener(fastScroller.getOnScrollListener());

        ayahWordAdapter = new AyahWordAdapter(ayahWordArrayList, getActivity(), surah_id);


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        //set Adapter with Animation
      //  ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(ayahWordAdapter);
      //  scaleInAnimationAdapter.setInterpolator(new OvershootInterpolator());
        mRecyclerView.setAdapter(ayahWordAdapter);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setVerticalScrollBarEnabled(true);
        

        //set headerview
        RecyclerViewHeader recyclerViewHeader = (RecyclerViewHeader) view.findViewById(R.id.header);
        TextView headerTextView = (TextView) recyclerViewHeader.findViewById(R.id.headerTextView);
        headerTextView.setText(getString(R.string.bismillah));
        recyclerViewHeader.attachTo(mRecyclerView, true);

    }

    public ArrayList<AyahWord> getAyahWordsBySurah(long surah_id, long ayah_number) {
        ArrayList<AyahWord> ayahWordArrayList = new ArrayList<AyahWord>();
        AyahWordDataSource ayahWordDataSource = new AyahWordDataSource(getActivity());
        //ayahWordArrayList = ayahWordDataSource.getEnglishAyahWordsBySurahVerse(surah_id, ayah_number);

        switch (lang) {
            case Config.LANG_BN:
                ayahWordArrayList = ayahWordDataSource.getBanglaAyahWordsBySurah(surah_id, ayah_number);
                break;
            case Config.LANG_INDO:
                ayahWordArrayList = ayahWordDataSource.getIndonesianAyahWordsBySurah(surah_id, ayah_number);
                break;
            case Config.LANG_EN:
                ayahWordArrayList = ayahWordDataSource.getEnglishAyahWordsBySurah(surah_id, ayah_number);
                break;
        }
    /*
        if(lang.equals(Config.LANG_BN))
        {
            ayahWordArrayList = ayahWordDataSource.getBanglaAyahWordsBySurah(surah_id, ayah_number);
            Log.d("AyahwordFragment",lang);
        }else if(lang.equals(Config.LANG_INDO))
        {
            ayahWordArrayList = ayahWordDataSource.getIndonesianAyahWordsBySurah(surah_id, ayah_number);
            Log.d("AyahwordFragment",lang);
        }else {
            Log.d("AyahwordFragment",lang);
            ayahWordArrayList = ayahWordDataSource.getEnglishAyahWordsBySurah(surah_id, ayah_number);
        }*/


/*
        for(AyahWord ayahWord:ayahWordArrayList) {

            for (Word word : ayahWord.getWord()) {
                //ayahWordString += word.getAyahWord_words_ar()+"\n"+word.getAyahWord_translate();

                Log.d("Word: ", word.getAyahWord_verse_id() + " " + word.getAyahWord_translate());

            }
        }*/

        return ayahWordArrayList;
    }

/*
    private class AsyncLoadData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            surah_id = getArguments().getLong(SurahDataSource.SURAH_ID_TAG);
            ayah_number = getArguments().getLong(SurahDataSource.SURAH_AYAH_NUMBER);
        }

        @Override
        protected Void doInBackground(Void... params) {

            ayahWordArrayList = getAyahWordsBySurah(surah_id, ayah_number);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            mRecyclerView.setVisibility(View.VISIBLE);
            ayahWordAdapter = new AyahWordAdapter(ayahWordArrayList, getActivity());
            mRecyclerView.setAdapter(ayahWordAdapter);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        }
    }
*/


}