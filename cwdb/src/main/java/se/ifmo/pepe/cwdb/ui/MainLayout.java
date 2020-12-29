package se.ifmo.pepe.cwdb.ui;

import com.github.appreciated.card.content.IconItem;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;
import se.ifmo.pepe.cwdb.backend.auth.Role;
import se.ifmo.pepe.cwdb.backend.auth.User;
import se.ifmo.pepe.cwdb.ui.view.company_info.CompanyInfoView;
import se.ifmo.pepe.cwdb.ui.view.stock.StockView;
import se.ifmo.pepe.cwdb.ui.view.trademarks.TrademarkView;

import java.awt.*;

@Route(value = "")
@CssImport("./styles/shared-styles.css")
public class MainLayout extends AppLayout {

    private final User currentUser = VaadinSession.getCurrent().getAttribute(User.class);

    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Coursage");
        logo.addClassName("logo");
        String prefix = currentUser.getRole() == Role.COMPANY ? "Company" : (currentUser.getRole() == Role.PHARMACY ? "Pharmacy" : "Admin");

        MenuBar menuBar = new MenuBar();
        menuBar.setOpenOnHover(true);
        menuBar.setWidth("10%");
        menuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY);
        MenuItem account = menuBar.addItem(new Icon(VaadinIcon.USER));
        menuBar.addItem("Account").setEnabled(false);
        SubMenu accountSubMenu = account.getSubMenu();
        accountSubMenu.addItem(new Span(String.format("Username: %s", currentUser.getUsername()))).setEnabled(false);
        accountSubMenu.addItem(new Span(String.format("Role: %s", prefix))).setEnabled(false);

        accountSubMenu.addItem("Sign out", e -> {
            VaadinSession.getCurrent().getSession().invalidate();
            UI.getCurrent().navigate("login");
        });

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, menuBar);
        header.expand(logo);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidth("100%");
        header.addClassName("header");

        addToNavbar(header);
    }

    private void createDrawer() {
        if (currentUser.getRole() == Role.COMPANY) {
            RouterLink listLink = new RouterLink("Trademarks", TrademarkView.class);
            listLink.setHighlightCondition(HighlightConditions.sameLocation());
            addToDrawer(new VerticalLayout(listLink,
                    new RouterLink("Company Info", CompanyInfoView.class)));

        } else if (currentUser.getRole() == Role.PHARMACY) {
            RouterLink listLink = new RouterLink("Stock", StockView.class);
            listLink.setHighlightCondition(HighlightConditions.sameLocation());
            addToDrawer(new VerticalLayout(listLink));

        } else if (currentUser.getRole() == Role.ADMIN) {
            RouterLink listLink = new RouterLink("Trademarks", TrademarkView.class);
            listLink.setHighlightCondition(HighlightConditions.sameLocation());
            addToDrawer(new VerticalLayout(listLink,
                    new RouterLink("Company Info", CompanyInfoView.class),
                    new RouterLink("Stock", StockView.class)));
        }
    }
}