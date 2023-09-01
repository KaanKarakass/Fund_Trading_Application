package com.eureka.spring.bean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.donut.DonutChartDataSet;
import org.primefaces.model.charts.donut.DonutChartModel;
import org.primefaces.model.charts.donut.DonutChartOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eureka.spring.model.FundTrading;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped; 

@Named
@ViewScoped
@Component
public class HomePageBean implements Serializable {
    private static final long serialVersionUID = 1L;
    @Autowired
    private FundTradingPageBean fundTradingPageBean;

    private DonutChartModel donutModel;

    @PostConstruct
    public void init() {
        createDonutModel();
    }
    public FundTradingPageBean getFundTradingPageBean() {
        return fundTradingPageBean;
    }

    public void setFundTradingPageBean(FundTradingPageBean fundTradingPageBean) {
        this.fundTradingPageBean = fundTradingPageBean;
    }
    public void createDonutModel() {
        donutModel = new DonutChartModel();
        ChartData data = new ChartData();
        DonutChartOptions options = new DonutChartOptions();
        options.setMaintainAspectRatio(false);
        donutModel.setOptions(options);

        List<FundTrading> dailyTopFunds = fundTradingPageBean.getDailyTopFunds();
        
        
        DonutChartDataSet dataSet = new DonutChartDataSet();
        List<Number> values = dailyTopFunds.stream()
                .map(FundTrading::getPiece)
                .collect(Collectors.toList());
        dataSet.setData(values);

        List<String> labels = dailyTopFunds.stream()
                .map(FundTrading::getFund_name)
                .collect(Collectors.toList());
        dataSet.setBackgroundColor(Arrays.asList("rgb(54, 162, 235)", "rgb(106, 13, 173)", "rgb(255, 205, 86)"));

        data.addChartDataSet(dataSet);
        data.setLabels(labels);

        donutModel.setData(data);
    }

    public DonutChartModel getDonutModel() {
        return donutModel;
    }

    public void setDonutModel(DonutChartModel donutModel) {
        this.donutModel = donutModel;
    }
}
