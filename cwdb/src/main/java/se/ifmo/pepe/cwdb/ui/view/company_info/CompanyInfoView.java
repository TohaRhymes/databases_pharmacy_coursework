package se.ifmo.pepe.cwdb.ui.view.company_info;

import com.github.appreciated.card.Card;
import com.github.appreciated.card.action.ActionButton;
import com.github.appreciated.card.action.Actions;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import se.ifmo.pepe.cwdb.backend.auth.User;
import se.ifmo.pepe.cwdb.backend.model.Companies;
import se.ifmo.pepe.cwdb.backend.model.CompanyInfo;
import se.ifmo.pepe.cwdb.backend.service.CompanyService;
import se.ifmo.pepe.cwdb.ui.MainLayout;

import java.util.ArrayList;
import java.util.List;


@Route(value = "company-info", layout = MainLayout.class)
@PageTitle("Company Info | Coursage")
public class CompanyInfoView extends VerticalLayout {

    private final CompanyService companyService;
    private final User currentUser = VaadinSession.getCurrent().getAttribute(User.class);
    private Companies currentCompany;
    private ListSeries npmPctAnnualSeries;
    private ListSeries marketCapSeries;
    private List<CompanyInfo> info;
    private List<Number> marketCap;
    private List<Number> npmPctAnnual;
    private Configuration configuration;
    private Chart chart;
    public CompanyInfoView(CompanyService companyService) {
        setSizeFull();
        setHeightFull();
        this.companyService = companyService;
        this.currentCompany = companyService.findById(currentUser.getCompanyID());
        info = companyService.findAllInfoByCompanyId(currentCompany.getId());
        marketCap = new ArrayList<>();
        npmPctAnnual = new ArrayList<>();
        info.forEach(i -> {
            marketCap.add(i.getMarketCap());
            npmPctAnnual.add(i.getNetProfitMarginPctAnnual());
        });

        addClassName("company-info-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        chart = new Chart(ChartType.SPLINE);
        configuration = chart.getConfiguration();

        Navigator navigator = configuration.getNavigator();
        navigator.setEnabled(true);
        navigator.setMargin(75);

        RangeSelector rangeSelector = new RangeSelector();
        rangeSelector.setSelected(4);
        configuration.setRangeSelector(rangeSelector);

        PlotOptionsSeries plotOptions = new PlotOptionsSeries();
        plotOptions.setMarker(new Marker(false));

        configuration.getTooltip().setPointFormatter(
                "function() { " +
                        "var category = this.x; " +
                        "var tipTxt = this.series.name + ': <b>' + this.y.toFixed(3) + '</b><br>'; " +
                        "return tipTxt; " +
                        "}"
        );
        configuration.setPlotOptions(plotOptions);

        configuration.setTitle(String.format("Analysis of '%s'", currentCompany.getName()));
        configuration.setSubTitle(currentCompany.getSpecialization());

        marketCapSeries = new ListSeries("Market Cap");
        marketCapSeries.setData(marketCap);

        npmPctAnnualSeries = new ListSeries("Net profit margin annual (%)");
        npmPctAnnualSeries.setData(npmPctAnnual);

        configuration.setSeries(marketCapSeries, npmPctAnnualSeries);

        Button button = new Button("Update", e -> {
            updateData(companyService.updateStats(currentCompany.getId()));
        });

        chart.setSizeFull();
        chart.setMaxHeight("75%");

        add(chart, button);
    }

    private void updateData(Companies currentCompany) {
        info = companyService.findAllInfoByCompanyId(currentCompany.getId());
        marketCap = new ArrayList<>();
        npmPctAnnual = new ArrayList<>();

        info.forEach(i -> {
            marketCap.add(i.getMarketCap());
            npmPctAnnual.add(i.getNetProfitMarginPctAnnual());
        });

        marketCapSeries = new ListSeries("Market Cap");
        marketCapSeries.setData(marketCap);
        marketCapSeries.updateSeries();

        npmPctAnnualSeries = new ListSeries("Net profit margin annual (%)");
        npmPctAnnualSeries.setData(npmPctAnnual);
        npmPctAnnualSeries.updateSeries();

        configuration.setSeries(marketCapSeries, npmPctAnnualSeries);
        chart.drawChart(false);
    }
}
