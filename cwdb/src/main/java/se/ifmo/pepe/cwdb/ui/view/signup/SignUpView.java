package se.ifmo.pepe.cwdb.ui.view.signup;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.*;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import se.ifmo.pepe.cwdb.backend.auth.User;
import se.ifmo.pepe.cwdb.backend.service.UserDetailsService;

@Route("signup")
@PageTitle("Sign Up | Coursage")

public class SignUpView extends VerticalLayout {
    private boolean enablePasswordValidation;
    private final UserDetailsService userDetailsService;
    private final Binder<User> binder;
    private final PasswordField pwd1;
    private final PasswordField pwd2;

    public SignUpView(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
        addClassName("signup-view");

        H2 title = new H2("Sign Up");

        TextField username = new TextField("Username");
        pwd1 = new PasswordField("Password");
        pwd2 = new PasswordField("Confirm password");

        RadioButtonGroup<String> radioButtonGroup = new RadioButtonGroup<>();
        radioButtonGroup.setLabel("Role");
        radioButtonGroup.setItems("Pharmacy", "Company");
        Span errorMessage = new Span();
        Button submitButton = new Button("Sign up");
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        NumberField pharmacyId = new NumberField("Pharmacy ID");
        NumberField companyID = new NumberField("Company ID");
        pharmacyId.setVisible(false);
        companyID.setVisible(false);
        radioButtonGroup.addValueChangeListener(e -> {
            if (e.getValue().equals("Pharmacy")) {
                pharmacyId.setVisible(true);
                pharmacyId.clear();

                companyID.setValue(-1d);
                companyID.setVisible(false);

            } else if (e.getValue().equals("Company")) {
                companyID.setVisible(true);
                companyID.clear();

                pharmacyId.setValue(-1d);
                pharmacyId.setVisible(false);
            }
        });


        FormLayout formLayout = new FormLayout(title, username, pwd1, pwd2, radioButtonGroup, pharmacyId, companyID, errorMessage, submitButton);

        // adjustments
        formLayout.setMaxWidth("500px");
        formLayout.getStyle().set("margin", "0 auto");
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP),
                new FormLayout.ResponsiveStep("490px", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP));

        formLayout.setColspan(title, 1);
        formLayout.setColspan(errorMessage, 1);
        formLayout.setColspan(submitButton, 1);

        errorMessage.getStyle().set("color", "var(--lumo-error-text-color)");
        errorMessage.getStyle().set("padding", "15px 0");

        add(formLayout);

        binder = new Binder<>(User.class);

        binder.forField(pharmacyId).withConverter(new Converter<Double, Long>() {
            @Override
            public Result<Long> convertToModel(Double aDouble, ValueContext valueContext) {
                return Result.ok(aDouble != null ? aDouble.longValue() : 0L);
            }

            @Override
            public Double convertToPresentation(Long aLong, ValueContext valueContext) {
                return aLong.doubleValue();
            }
        }).withValidator(this::pharmacyValidation).bind("pharmacyID");

        binder.forField(companyID).withConverter(new Converter<Double, Long>() {
            @Override
            public Result<Long> convertToModel(Double aDouble, ValueContext valueContext) {
                return Result.ok(aDouble != null ? aDouble.longValue() : 0L);
            }

            @Override
            public Double convertToPresentation(Long aLong, ValueContext valueContext) {
                return aLong.doubleValue();
            }
        }).withValidator(this::companyValidation).bind("companyID");


        binder.forField(username).asRequired().bind("username");
        binder.forField(pwd1).asRequired().withValidator(this::passwordValidation).bind("password");
        pwd2.addValueChangeListener(e -> {
            enablePasswordValidation = true;
            binder.validate();
        });
        binder.setStatusLabel(errorMessage);
        binder.forField(username).withValidator(this::validateUsername).asRequired().bind("username");
        submitButton.addClickListener(e -> {
            try {
                User details = new User();
                details.setRole(radioButtonGroup.getValue());
                details.setCompanyID(companyID.getValue().longValue());
                details.setPharmacyID(pharmacyId.getValue().longValue());
                binder.writeBean(details);
                userDetailsService.store(details);
                UI.getCurrent().navigate("login");

                showSuccess(details);
            } catch (ValidationException validationException) {
                validationException.printStackTrace();
            }
        });
    }

    private void showSuccess(User user) {
        Notification notification = Notification.show("Data saved, welcome " + user.getUsername());
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    private ValidationResult passwordValidation(String pass1, ValueContext ctx) {
        if (pass1 == null || pass1.length() < 8) {
            return ValidationResult.error("Password should be at least 8 characters long");
        }

        if (!enablePasswordValidation) {
            enablePasswordValidation = true;
            return ValidationResult.ok();
        }

        String pass2 = pwd2.getValue();

        if (pass1 != null && pass1.equals(pass2)) {
            return ValidationResult.ok();
        }

        return ValidationResult.error("Passwords do not match");
    }

    private ValidationResult pharmacyValidation(Long pharmacyId, ValueContext ctx) {
        if (pharmacyId == -1)
            return ValidationResult.ok();
        String errorMsg = userDetailsService.validatePharmacyID(pharmacyId);
        if (errorMsg == null) {
            return ValidationResult.ok();
        }
        return ValidationResult.error(errorMsg);
    }

    private ValidationResult companyValidation(Long companyId, ValueContext ctx) {
        if (companyId == -1)
            return ValidationResult.ok();
        String errorMsg = userDetailsService.validateCompanyID(companyId);
        if (errorMsg == null) {
            return ValidationResult.ok();
        }
        return ValidationResult.error(errorMsg);
    }

    private ValidationResult validateUsername(String username, ValueContext ctx) {
        String errorMsg = userDetailsService.validateUsername(username);
        if (errorMsg == null) {
            return ValidationResult.ok();
        }
        return ValidationResult.error(errorMsg);
    }
}
