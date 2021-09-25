package tool.mariam.fihhuda.quran.viewsModel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tool.mariam.fihhuda.quran.fullQuranReadingModels.Ayah;
import tool.mariam.fihhuda.quran.fullQuranReadingModels.FullQuran;
import tool.mariam.fihhuda.quran.fullQuranReadingModels.Surah;
import tool.mariam.fihhuda.tafseer.tafseerSearchModel.SearchModel;
import tool.mariam.fihhuda.tafseer.tafseerSearchModel.forSearchInFragment.AyahsSearchItem;
import tool.mariam.fihhuda.tafseer.tafseerSearchModel.forSearchInFragment.SearchForAyah;
import tool.mariam.fihhuda.tafseer.tafseerSearchModel.forSearchInFragment.SurahsSearchItem;
import tool.mariam.fihhuda.tafseer.tafseerSearchModel.forTafseerReadingInActivity.AllTafseerReading;
import tool.mariam.fihhuda.tafseer.tafseerSearchModel.forTafseerReadingInActivity.SurahsItem;


public class QuranViewModel extends ViewModel {
    public Context context;

    public MutableLiveData<FullQuran> fullQuran = new MutableLiveData<>();
    public MutableLiveData<String> message = new MutableLiveData<>();
    FullQuran full = new FullQuran();

    public QuranViewModel() {
    }

    public QuranViewModel(Context context) {
        this.context = context;
    }


    //this function too get all quran from jason as aclass called fullquraan
    //  have a class called data .. thia data class have list of suras
    public FullQuran getAllQuranFromJson() {

        InputStream fileIn = null;
        try {
            fileIn = context.getAssets().open("quran.json");
            BufferedInputStream bufferedIn = new BufferedInputStream(fileIn);
            Reader reader = new InputStreamReader(bufferedIn, StandardCharsets.UTF_8);
            full = new Gson().fromJson(reader, FullQuran.class);
            fullQuran.setValue(full);
            message.setValue("سور القران كاملاً");

        } catch (IOException e) {
            message.setValue(e.getLocalizedMessage());
        }

        return full;
    }

    //return surah as pages each page as list of ayah
    public List<List<Ayah>> getSurahAyahs(int position) {
        List<Surah> allSouar = full.getData().getSurahs();
        if (position == 0) {
            //ayat surah
            List<Ayah> currentSurah = new ArrayList<Ayah>();
            currentSurah = allSouar.get(position).getAyahs();
            List<List<Ayah>> fatiha = new ArrayList<>();
            fatiha.add(currentSurah);
            return fatiha;
        }

        //ayat surah
        List<Ayah> currentSurah = new ArrayList<Ayah>();
        currentSurah = allSouar.get(position).getAyahs();

        // pages
        int firstPageinSurah = currentSurah.get(0).getPage();//page=2  baqra
        int lastPageInSurah = currentSurah.get(currentSurah.size() - 1).getPage();//49 baqara
        List<List<Ayah>> listOfPages = new ArrayList<>();
        int lastayah = 0;
        int index = 0;

        for (int i = firstPageinSurah; i <= lastPageInSurah; i = i + 1) {
            List<Ayah> pageNumber_i = new ArrayList<>();
            for (int j = lastayah; j < currentSurah.size(); j = j + 1) {
                index = j;
                if (currentSurah.get(j).getPage() == i) {
                    String s = currentSurah.get(j).getText();
                    pageNumber_i.add(currentSurah.get(j));

                } else {
                    listOfPages.add(pageNumber_i);
                    lastayah = index; //lastaya
                    break;
                }
            }
        }
        // return  clean( listOfPages );
        List<List<Ayah>> listOfPages2 = listOfPages;
        listOfPages2.removeAll(Collections.singleton(""));
        listOfPages2.removeAll(Collections.singleton(null));

        return listOfPages2;


    }


    public List<SearchModel> getSearchedForByWord(String word) {
        InputStream fileIn = null;
        try {
            fileIn = context.getAssets().open("quran_clean.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedInputStream bufferedIn = new BufferedInputStream(fileIn);
        Reader reader = new InputStreamReader(bufferedIn, StandardCharsets.UTF_8);
        SearchForAyah full = new Gson().fromJson(reader, SearchForAyah.class);
        List<SurahsSearchItem> suras = full.getSurahs();
        List<SearchModel> allSearchedAyat = new ArrayList<>();
        List<AyahsSearchItem> ayahs;
        for (int i = 0; i < suras.size(); i++) {
            SurahsSearchItem surah = suras.get(i);
            ayahs = surah.getAyahs();
            for (int k = 0; k < ayahs.size(); k++) {
                if (ayahs.get(k).getText().contains(word)) {
                    allSearchedAyat.add(new SearchModel(surah.getName(), ayahs.get(k)));
                }
            }
        }
        return allSearchedAyat;
    }


    //get Tafseer OF surah

    public SurahsItem getTafseerForSura(String suraName) {
        SurahsItem suraitem = null;
        InputStream fileIn = null;
        try {
            fileIn = context.getAssets().open("tafseer.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedInputStream bufferedIn = new BufferedInputStream(fileIn);
        Reader reader = new InputStreamReader(bufferedIn, StandardCharsets.UTF_8);
        AllTafseerReading allTafseerReading = new Gson().fromJson(reader, AllTafseerReading.class);
        List<SurahsItem> list = allTafseerReading.getData().getSurahs();
        for (SurahsItem item : list) {
            if (item.getName().contains(suraName)) {
                suraitem = item;
                break;
            }
        }
        return suraitem;
    }

    //get Surah Ayat For Tafseer
    public Surah getSuaraAyatForTafseer(String name) {
        Surah surahitem = null;
        FullQuran fullQuran = getAllQuranFromJson();
        List<Surah> list = fullQuran.getData().getSurahs();
        for (Surah item : list) {
            if (item.getName().contains(name)) {
                surahitem = item;
                break;
            }
        }
        return surahitem;
    }

}






