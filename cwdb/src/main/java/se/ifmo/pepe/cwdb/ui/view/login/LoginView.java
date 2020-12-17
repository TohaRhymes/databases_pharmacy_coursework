package se.ifmo.pepe.cwdb.ui.view.login;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.springframework.security.core.context.SecurityContextHolder;
import se.ifmo.pepe.cwdb.backend.service.UserDetailsService;
import se.ifmo.pepe.cwdb.ui.view.signup.SignUpView;

import javax.security.auth.message.AuthException;

@Route("login")
@PageTitle("Login | Coursage")
public class LoginView extends VerticalLayout {
        public static final String ROUTE = "login";

        public LoginView(UserDetailsService userDetailsService) {
            addClassName("login-view");
            setSizeFull();
            setAlignItems(Alignment.CENTER);
            setJustifyContentMode(JustifyContentMode.CENTER);
            TextField username = new TextField("Username");
            PasswordField password = new PasswordField("Password");
            Button loginBtn = new Button("Login", e -> {
                try {
                    SecurityContextHolder.getContext().setAuthentication(
                            userDetailsService.authenticate(username.getValue(), password.getValue()));
                    UI.getCurrent().navigate("");
                } catch (AuthException exception) {
                    Notification.show("u r fucked");
                }
            });
            loginBtn.addClickShortcut(Key.ENTER);

            add(new H1("Welcome"),
                    username, password, loginBtn,
                    new RouterLink("Sign up", SignUpView.class));
        }
}