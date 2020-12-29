package se.ifmo.pepe.cwdb.backend.service;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.ifmo.pepe.cwdb.backend.auth.Role;
import se.ifmo.pepe.cwdb.backend.auth.User;
import se.ifmo.pepe.cwdb.backend.auth.UserPrincipal;
import se.ifmo.pepe.cwdb.backend.repo.CompanyRepo;
import se.ifmo.pepe.cwdb.backend.repo.PharmacyRepo;
import se.ifmo.pepe.cwdb.backend.repo.UserRepo;
import se.ifmo.pepe.cwdb.ui.MainLayout;
import se.ifmo.pepe.cwdb.ui.view.company_info.CompanyInfoView;
import se.ifmo.pepe.cwdb.ui.view.login.LoginView;
import se.ifmo.pepe.cwdb.ui.view.stock.StockView;
import se.ifmo.pepe.cwdb.ui.view.trademarks.TrademarkView;

import javax.security.auth.message.AuthException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    public class AuthorizedRoute {
        private String route;
        private String name;
        private Class<? extends Component> view;

        public AuthorizedRoute(String route, String name, Class<? extends Component> view) {
            this.route = route;
            this.name = name;
            this.view = view;
        }

        public String getRoute() {
            return route;
        }

        public void setRoute(String route) {
            this.route = route;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Class<? extends Component> getView() {
            return view;
        }

        public void setView(Class<? extends Component> view) {
            this.view = view;
        }
    }

    private final UserRepo userRepository;
    private final PharmacyRepo pharmacyRepository;
    private final CompanyRepo companyRepository;

    public UserDetailsService(UserRepo userRepository, PharmacyRepo pharmacyRepository, CompanyRepo companyRepository) {
        this.userRepository = userRepository;
        this.pharmacyRepository = pharmacyRepository;
        this.companyRepository = companyRepository;
    }
    @Transactional
    public Authentication authenticate(String username, String password) throws AuthException {
        User user = userRepository.findByUsername(username);
        if (user != null && password.equals(user.getPassword())) {
            VaadinSession.getCurrent().setAttribute(User.class, user);
            createRoutes(user.getRole());
            return new UsernamePasswordAuthenticationToken(username, password, Collections.emptyList());
        } else
            throw new AuthException();
    }

    private void createRoutes(Role role) {
        getAuthorizedRoutes(role)
                .forEach(r ->
                        RouteConfiguration.forSessionScope().setRoute(
                                r.route, r.view, MainLayout.class));
    }

    public List<AuthorizedRoute> getAuthorizedRoutes(Role role) {
        List<AuthorizedRoute> routes = new ArrayList<AuthorizedRoute>();
        if (role.equals(Role.COMPANY)) {
            routes.add(new AuthorizedRoute("trademarks", "Trademarks", TrademarkView.class));
            routes.add(new AuthorizedRoute("company-info", "Company Info", CompanyInfoView.class));
            routes.add(new AuthorizedRoute("logout", "Logout", LoginView.class));
        } else if (role.equals(Role.PHARMACY)) {
            routes.add(new AuthorizedRoute("stock", "Stock", StockView.class));
            routes.add(new AuthorizedRoute("logout", "Logout", LoginView.class));
        } else if (role.equals(Role.ADMIN)) {
            routes.add(new AuthorizedRoute("trademarks", "Trademarks", TrademarkView.class));
            routes.add(new AuthorizedRoute("company-info", "Company Info", CompanyInfoView.class));
            routes.add(new AuthorizedRoute("stock", "Stock", StockView.class));
            routes.add(new AuthorizedRoute("logout", "Logout", LoginView.class));
        }
        return routes;
    }

    public void store(User userDetails) {
        userRepository.save(userDetails);
    }

    public String validateUsername(String username) {
        if (username == null)
            return "Username can't be empty u stupid fuck";

        if (username.length() < 4)
            return "ya'll have too short huh?";

        List<String> reserved = Arrays.asList("admin", "null", "void", "test", "root");
        if (reserved.contains(username))
            return String.format("'%s' is not available", username);

        if (userRepository.findByUsername(username) != null)
            return String.format("'%s' is not available", username);

        return null;
    }

    public String validatePharmacyID(Long id) {
        if (!pharmacyRepository.existsById(id))
            return String.format("Pharmacy #%s doesn't exist", id);
        else if (id == -1) return null;
        return null;
    }

    public String validateCompanyID(Long id) {
        if (!companyRepository.existsById(id))
            return String.format("Company #%s doesn't exist", id);
        else if (id == -1) return null;
        return null;
    }

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(s);
        if (user == null) {
            throw new UsernameNotFoundException(s);
        }
        return new UserPrincipal(user);
    }

    public static class UserDetailsServiceException extends Exception {
        public UserDetailsServiceException(String msg) {
            super(msg);
        }
    }
}
