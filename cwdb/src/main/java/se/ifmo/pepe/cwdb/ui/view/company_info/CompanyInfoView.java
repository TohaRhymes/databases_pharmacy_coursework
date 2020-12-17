package se.ifmo.pepe.cwdb.ui.view.company_info;

import com.github.appreciated.card.Card;
import com.github.appreciated.card.action.ActionButton;
import com.github.appreciated.card.action.Actions;
import com.github.appreciated.card.label.PrimaryLabel;
import com.github.appreciated.card.label.SecondaryLabel;
import com.github.appreciated.card.label.TitleLabel;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import se.ifmo.pepe.cwdb.backend.auth.User;
import se.ifmo.pepe.cwdb.backend.model.Companies;
import se.ifmo.pepe.cwdb.backend.service.CompanyService;
import se.ifmo.pepe.cwdb.ui.MainLayout;


@Route(value = "company-info", layout = MainLayout.class)
@PageTitle("Company Info | Coursage")
public class CompanyInfoView extends HorizontalLayout {

    private final CompanyService companyService;
    private final User currentUser = VaadinSession.getCurrent().getAttribute(User.class);
    private final Companies currentCompany;

    public CompanyInfoView(CompanyService companyService) {
        this.companyService = companyService;
        this.currentCompany = companyService.findById(currentUser.getCompanyID());
        addClassName("company-info-view");
        setDefaultVerticalComponentAlignment(Alignment.CENTER);

        Card card = new Card(
                new TitleLabel(String.format("Name:\n%s", currentCompany.getName())),
                new PrimaryLabel(String.format("Specialization:\n%s", currentCompany.getSpecialization())),
                new SecondaryLabel(String.format("Market cap:\n%s", currentCompany.getMarketCap())),
                new SecondaryLabel(String.format("Net profit margin annual (%%):\n%s", currentCompany.getNetProfitMarginPctAnnual())),
                new Actions(
                        new ActionButton("Update", e -> {
                            Notification.show("Updated");
                        })
                )
        );

        Div content = new Div(card);

        add(content);
    }
}
