package se.ifmo.pepe.cwdb.ui.view.trademarks;

import com.vaadin.componentfactory.Autocomplete;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.shared.Registration;
import se.ifmo.pepe.cwdb.backend.model.Drugs;
import se.ifmo.pepe.cwdb.backend.model.Trademarks;
import se.ifmo.pepe.cwdb.backend.service.TrademarkService;

import java.util.List;

public class TrademarkForm extends FormLayout {
    private final TrademarkService trademarkService;
    TextField name = new TextField("Name");
    NumberField doze = new NumberField("Doze");
    NumberField releasePrice = new NumberField("Release Price");

    Select<String> select = new Select<>();
    Autocomplete autocomplete = new Autocomplete(5);


    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Close");

    Binder<Trademarks> binder = new Binder<>(Trademarks.class);
    Binder<Drugs> pBinder = new Binder<>(Drugs.class);
    private Trademarks tm;

    public TrademarkForm(List<Trademarks> trademarks, TrademarkService trademarkService) {
        this.trademarkService = trademarkService;
        addClassName("tm-form");
        binder.bindInstanceFields(this);

        select.setLabel("Patent distribution");
        select.setPlaceholder("Placeholder");
        select.setItems("Free to use", "Usage with constraints", "Restricted to use");

        autocomplete.setLabel("Active substance");
        autocomplete.addChangeListener(event -> {
            String text = event.getValue();
            autocomplete.setOptions(trademarkService.findAnyByName(text));
        });
        pBinder.forField(autocomplete).asRequired().withValidator(this::validateDrugName).bind("activeSubstance");

        add(name,
                doze,
                releasePrice,
                autocomplete,
                select,
                createBtnLayout()
        );
    }

    private HorizontalLayout createBtnLayout() {


        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(e -> validateAndSave());
        delete.addClickListener(e -> fireEvent(new DeleteEvent(this, tm)));
        close.addClickListener(e -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(tm);
            fireEvent(new SaveEvent(this, tm));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public void setTm(Trademarks tm) {
        this.tm = tm;
        if (tm != null)
            if (tm.getDrugId() != null && tm.getPatentId() != null) {
                autocomplete.setValue(trademarkService.findDrugByDrugId(tm.getDrugId()).getActiveSubstance());
                select.setValue(trademarkService.findPatentByPatentId(tm.getPatentId()).resolveDistribution());
            }
        binder.readBean(tm);

    }

    private ValidationResult validateDrugName(String drugName, ValueContext ctx) {
        String errorMsg = trademarkService.validate(drugName);
        if (errorMsg == null) {
            return ValidationResult.ok();
        }
        return ValidationResult.error(errorMsg);
    }

    public static abstract class TmFormEvent extends ComponentEvent<TrademarkForm> {
        private final Trademarks tm;

        protected TmFormEvent(TrademarkForm source, Trademarks tm) {
            super(source, false);
            this.tm = tm;
        }

        public Trademarks getTrademark() {
            return tm;
        }
    }

    public static class SaveEvent extends TmFormEvent {
        SaveEvent(TrademarkForm source, Trademarks tm) {
            super(source, tm);
        }
    }

    public static class DeleteEvent extends TmFormEvent {
        DeleteEvent(TrademarkForm source, Trademarks tm) {
            super(source, tm);
        }
    }

    public static class CloseEvent extends TmFormEvent {
        CloseEvent(TrademarkForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}


