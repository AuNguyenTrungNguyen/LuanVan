package luanvan.luanvantotnghiep.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import luanvan.luanvantotnghiep.Adapter.ReactSeriesAdapter;
import luanvan.luanvantotnghiep.Database.ChemistryHelper;
import luanvan.luanvantotnghiep.Model.ReactSeries;
import luanvan.luanvantotnghiep.R;
import luanvan.luanvantotnghiep.Util.ChemistrySingle;

public class ReactivitySeriesFragment extends Fragment {

    private ChemistryHelper mChemistryHelper;
    private Context mContext;
    private ReactSeriesAdapter mAdapter;

    private TextView mTvOxidizing;
    private TextView mTvReducing;

    private TextView mTvMemorize;
    private TextView mTvMeaning;

    public ReactivitySeriesFragment() {
    }

    public static ReactivitySeriesFragment newInstance() {
        return new ReactivitySeriesFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reactivity_series, container, false);

        init(view);

        showReactivityList();

        showMemorizeAndMeaning();

        return view;
    }

    private void init(View v) {
        mChemistryHelper = ChemistrySingle.getInstance(mContext);
        List<ReactSeries> mReactSeriesList = mChemistryHelper.getAllReactSeries();

        mTvOxidizing = v.findViewById(R.id.tv_oxidizing_agent);
        mTvReducing = v.findViewById(R.id.tv_reducing_agent);

        mTvMemorize = v.findViewById(R.id.tv_memorize_react_series);
        mTvMeaning = v.findViewById(R.id.tv_meaning_react_series);

        RecyclerView mRvReactSeries = v.findViewById(R.id.rv_react_series);
        mAdapter = new ReactSeriesAdapter(mContext, mReactSeriesList);
        mRvReactSeries.setAdapter(mAdapter);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext,
                LinearLayoutManager.HORIZONTAL,
                false);
        mRvReactSeries.setLayoutManager(mLayoutManager);
        mRvReactSeries.setHasFixedSize(true);
    }

    private void showReactivityList() {
        mAdapter.notifyDataSetChanged();
    }

    private void showMemorizeAndMeaning(){
        mTvOxidizing.setText(Html.fromHtml("Tính chất oxi hóa của ion kim loại tăng dần &rarr"));
        mTvReducing.setText(Html.fromHtml("&larr Tính chất khử của kim loại tăng dần"));

        mTvMemorize.setText("Lúc khác Ba Cần Nên Mang Áo Giáp Có Sắt2/Sắt" +
                " Nên sang Phố Sắt3/Sắt Hỏi cửa Hàng Sắt3/Sắt2 Hiệu Á Phi Âu");

        try {
            InputStream fis = mContext.getAssets().open("reactivity_series");
            BufferedReader reader=new
                    BufferedReader(new InputStreamReader(fis));
            String data="";
            StringBuilder builder=new StringBuilder();
            while((data=reader.readLine())!=null)
            {
                builder.append(data);
                builder.append("\n");
            }
            fis.close();
            mTvMeaning.setText(Html.fromHtml(builder.toString()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
