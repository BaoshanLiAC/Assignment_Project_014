package com.ac.assignment_project_014.covid19;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ac.assignment_project_014.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.

 */

public class Covid19ChartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    private Bundle dataFromActivity;
    private AppCompatActivity parentActivity;
    private Covid19CountryData country;
    private List<Covid19ProvinceData> dataList;
    private String[] provinces;
    private boolean isTablet;
    private LinearLayout layout;
    private Button hide;

    //chart variables
    private static  int MAX_X_VALUE = 0;
    private static  int MAX_Y_VALUE = 0;
    private static  int MIN_Y_VALUE = 0;
    private static  String STACK_1_LABEL = "Confirmed Case";
    private static  String STACK_2_LABEL = "Daily Case";
    private static  String SET_LABEL = "Default Name";
    private BarChart chart;
    private RelativeLayout.LayoutParams layoutParam;


    public Covid19ChartFragment() {
        // Required empty public constructor
    }
    public Covid19ChartFragment(LinearLayout leftLayout) {
        layout = leftLayout;
    }

    private void setData(Covid19CountryData country){

        List<Covid19ProvinceData> list = country.getDataList();
        SET_LABEL = country.getCountryName() + " on " + country.getSearchDateTime();
        Collections.sort(list, (a,b)->a.getCaseNumber()-b.getCaseNumber());
        if(list.size() > 30){
            dataList = list.subList(0, 30);
        }
        else{
            dataList = list;
        }

        provinces = new String[dataList.size()];
        for(int i = 0; i < dataList.size(); i++) provinces[i] = dataList.get(i).getName();
            MAX_X_VALUE = dataList.size();
        Collections.sort(dataList, (a,b)->a.getCaseNumber()-b.getCaseNumber());
        int max = dataList.get(0).getCaseNumber()+dataList.get(0).getIncrease();
        MAX_Y_VALUE = getCeil(max);
        int min = dataList.get(MAX_X_VALUE - 1).getCaseNumber()+dataList.get(MAX_X_VALUE - 1).getIncrease();
        MIN_Y_VALUE = getCeil(min);
    }


    private int getCeil(int num){
        int lengthMax = String.valueOf(num).length();
        int dv = (int) Math.pow(10, lengthMax-1);
        int ceil = (int) Math.ceil(num/dv);
        return ceil * dv;
    }


    protected void setTablet(boolean isTablet){
        this.isTablet = isTablet;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dataFromActivity = getArguments();
        country = (Covid19CountryData) dataFromActivity.getSerializable("country-data");
        setData(country);

        View view = inflater.inflate(R.layout.covid19_chart_fragment, container, false);

        chart = view.findViewById(R.id.covid_chart);

        BarData data = createChartData();
        configureChartAppearance();
        prepareChartData(data);
        hide = view.findViewById(R.id.covid_chart_btn);
        hide.setOnClickListener(e->{
            parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
            if(!isTablet)parentActivity.finish();
            else layout.setLayoutParams(layoutParam);
        });
        return view;
    }

    private void configureChartAppearance() {

        chart.setDrawGridBackground(false);
        chart.setDrawValueAboveBar(false);
        chart.getDescription().setTextColor(Color.WHITE);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setTextColor(Color.YELLOW);
        chart.getLegend().setTextSize(15f);
        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(provinces.length);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return provinces[(int) value];
            }
        });
        xAxis.setTextColor(Color.WHITE);
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setTextColor(Color.WHITE);
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setTextColor(Color.WHITE);
    }

    private BarData createChartData() {
        ArrayList<BarEntry> values = new ArrayList<>();

        for (int i = 0; i < MAX_X_VALUE; i++) {
            float value1 = dataList.get(i).getCaseNumber();
            float value2 = dataList.get(i).getIncrease();

            values.add(new BarEntry(i, new float[]{value1, value2}));
        }

        BarDataSet set1 = new BarDataSet(values, SET_LABEL);

        set1.setColors(new int[] {ColorTemplate.MATERIAL_COLORS[0], ColorTemplate.MATERIAL_COLORS[1]});
        set1.setValueTextColor(Color.WHITE);
        set1.setStackLabels(new String[] {STACK_1_LABEL, STACK_2_LABEL});

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);

        return data;
    }

    private void prepareChartData(BarData data) {
        data.setValueTextSize(12f);
        chart.setData(data);
        chart.invalidate();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //context will either be FragmentExample for a tablet, or EmptyActivity for phone
        parentActivity = (AppCompatActivity)context;
    }

    public void setLayOutParameter(ViewGroup.LayoutParams para1) {
        this.layoutParam = (RelativeLayout.LayoutParams) para1;
    }
}
