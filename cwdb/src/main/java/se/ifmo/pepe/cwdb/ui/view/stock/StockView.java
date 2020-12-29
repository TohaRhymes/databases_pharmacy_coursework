package se.ifmo.pepe.cwdb.ui.view.stock;

import com.vaadin.componentfactory.Autocomplete;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.*;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import se.ifmo.pepe.cwdb.backend.auth.User;
import se.ifmo.pepe.cwdb.backend.dto.StockDTO;
import se.ifmo.pepe.cwdb.backend.model.Drugs;
import se.ifmo.pepe.cwdb.backend.model.Trademarks;
import se.ifmo.pepe.cwdb.backend.service.StockService;
import se.ifmo.pepe.cwdb.ui.MainLayout;

@Route(value = "stock", layout = MainLayout.class)
@PageTitle("Stock | Coursage")
public class StockView extends VerticalLayout {
    private final Grid<StockDTO> grid = new Grid<>(StockDTO.class);
    private final TextField filterText = new TextField();
    private final User currentUser = VaadinSession.getCurrent().getAttribute(User.class);
    private final StockService stockService;
    private final Binder<StockDTO> binder = new Binder<>(StockDTO.class);

    public StockView(StockService stockService) {
        this.stockService = stockService;
        addClassName("list-view");
        setSizeFull();

        configureGrid();

        Div content = new Div(grid);
        content.addClassName("content");
        content.setSizeFull();

        add(getToolbar(), content);

        updateGrid();

    }

    private void configureGrid() {
        grid.addClassName("tm-grid");
        grid.setSizeFull();
        grid.setColumns("pharmacyName", "trademarkName", "availability");
        grid.addComponentColumn(item ->
                createRemoveButton(grid, item)).setHeader("Action");

    }

    private Button createRemoveButton(Grid<StockDTO> grid, StockDTO item) {

        return new Button("Edit", e -> {
            Dialog dialog = new Dialog();
            dialog.setDraggable(true);
            dialog.setWidth("20%");
            dialog.setMinHeight("25%");

            FormLayout formLayout = new FormLayout();
            formLayout.setSizeFull();
            NumberField amountToSell = new NumberField("Amount");
            binder.forField(amountToSell).asRequired().withConverter(new Converter<Double, Long>() {
                @Override
                public Result<Long> convertToModel(Double aDouble, ValueContext valueContext) {
                    return Result.ok(aDouble != null ? aDouble.longValue() : 0L);
                }

                @Override
                public Double convertToPresentation(Long aLong, ValueContext valueContext) {
                    return aLong.doubleValue();
                }
            });

            Button sell = new Button("Edit");
            if (item.getAvailability() != 0) {
                sell.setText("Sell");
                sell.addClickListener(ev -> {
                    if (item.getAvailability() >= amountToSell.getValue().longValue() && item.getAvailability() != 0) {
                        ListDataProvider<StockDTO> dataProvider = (ListDataProvider<StockDTO>) grid
                                .getDataProvider();
                        dataProvider.getItems().forEach(s -> {
                            if (s.equals(item))
                                s.setAvailability(s.getAvailability() - amountToSell.getValue().longValue());
                        });
                        item.setPharmacyId(currentUser.getPharmacyID());
                        dataProvider.refreshAll();
                        stockService.sell(item, amountToSell.getValue().longValue());
                        dialog.close();
                        configureGrid();
                        updateGrid();
                        Notification.show(String.format("You successfully sold '%s'x%s", item.getTrademarkName(), amountToSell.getValue().longValue()),
                                5000, Notification.Position.TOP_END).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    } else {
                        Notification.show("You can't sell that much", 5000, Notification.Position.TOP_END).addThemeVariants(NotificationVariant.LUMO_ERROR);
                    }
                });
            } else {
                sell.setText("Buy");
                sell.addClickListener(ev -> {
                    ListDataProvider<StockDTO> dataProvider = (ListDataProvider<StockDTO>) grid
                            .getDataProvider();
                    dataProvider.getItems().forEach(s -> {
                        if (s.equals(item)) s.setAvailability(s.getAvailability() + amountToSell.getValue().longValue());
                    });
                    item.setPharmacyId(currentUser.getPharmacyID());
                    dataProvider.refreshAll();
                    stockService.buy(item, amountToSell.getValue().longValue());
                    dialog.close();
                    configureGrid();
                    updateGrid();
                    Notification.show(String.format("You successfully bought '%s'x%s", item.getTrademarkName(), amountToSell.getValue().longValue()),
                            5000, Notification.Position.TOP_END).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                });
            }
            sell.addThemeVariants(ButtonVariant.LUMO_SUCCESS);

            formLayout.add(new H2("Edit trademark"),
                    amountToSell, sell);
            dialog.add(formLayout);
            dialog.open();
            configureGrid();
            updateGrid();
        });
    }

    private void updateGrid() {
        grid.setItems(stockService.findAll(currentUser.getPharmacyID()));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateGrid());

        Button addTrademarkButton = new Button("Purchase");
        addTrademarkButton.addClickListener(e -> {
            createFormInsideDialog();
        });

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addTrademarkButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void createFormInsideDialog() {
        Dialog dialog = new Dialog();
        dialog.setDraggable(true);
        dialog.setWidth("20%");
        dialog.setMinHeight("55%");

        FormLayout formLayout = new FormLayout();
        formLayout.setSizeFull();

        NumberField amount = new NumberField("Amount");
        Autocomplete autocomplete = new Autocomplete(5);
        autocomplete.setLabel("Trademark name");
        autocomplete.addChangeListener(event -> {
            String text = event.getValue();
            autocomplete.setOptions(stockService.findTmNamesLikeInput(text));

        });
        Div info = new Div();
        info.setClassName("info");

        autocomplete.addValueChangeListener(e -> {
            Span s2 = new Span();
            Span s3 = new Span();
            Span s4 = new Span();
            Span s5 = new Span();
            Span s6 = new Span();
            Trademarks t = stockService.findFirstTmByName(e.getValue());
            Drugs d = stockService.findOneDrugById(t.getDrugId());
            info.removeAll();
            info.setVisible(true);
            s2.getElement().setProperty("innerHTML", newLineHtml(String.format("<b>Release price:</b> $%s\n", t.getReleasePrice())));
            s3.getElement().setProperty("innerHTML", newLineHtml(String.format("<b>Doze:</b> %s\n", t.getDoze())));
            s4.getElement().setProperty("innerHTML", newLineHtml(String.format("<b>Active substance:</b> %s\n", d.getActiveSubstance())));
            s5.getElement().setProperty("innerHTML", newLineHtml(String.format("<b>Homeopathy:</b> %s\n", d.getHomeopathy() ? "yes" : "no")));
            s6.getElement().setProperty("innerHTML", newLineHtml(String.format("<b>Drug group:</b> %s\n", d.getDrugsGroup())));
            info.add(s2, s3, s4, s5, s6);

        });

        autocomplete.addValueClearListener(e -> {
            info.setVisible(false);
            info.removeAll();
        });

        binder.forField(autocomplete).asRequired().withValidator(this::validateTmName).bind("trademarkName");
        binder.forField(amount).withConverter(new Converter<Double, Long>() {
            @Override
            public Result<Long> convertToModel(Double aDouble, ValueContext valueContext) {
                return Result.ok(aDouble != null ? aDouble.longValue() : 0L);
            }

            @Override
            public Double convertToPresentation(Long aLong, ValueContext valueContext) {
                return aLong.doubleValue();
            }
        }).asRequired().withValidator(this::validateAmount).bind("availability");

        Button purchase = new Button("Purchase", e -> {
            try {
                StockDTO stockDTO = new StockDTO();
                stockDTO.setCompanyId(currentUser.getCompanyID());
                stockDTO.setPharmacyId(currentUser.getPharmacyID());
                stockDTO.setPharmacyName(stockService.findPharmacyById(currentUser.getPharmacyID()).getName());
                binder.writeBean(stockDTO);
                stockService.purchase(stockDTO);
                Notification.show(String.format("%s was successfully purchased", stockDTO.getTrademarkName()),
                        5000, Notification.Position.TOP_END).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                updateGrid();
            } catch (ValidationException validationException) {
                Notification.show("No such trademark", 5000, Notification.Position.TOP_END).addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (StockService.StockServicePurchasingException stockServicePurchasingException) {
                Notification.show("Already on the stock", 5000, Notification.Position.TOP_END).addThemeVariants(NotificationVariant.LUMO_CONTRAST);
            }
        });
        Button cancel = new Button("Cancel", e -> dialog.close());

        purchase.addClickShortcut(Key.ENTER);
        purchase.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        cancel.addClickShortcut(Key.ESCAPE);
        cancel.addThemeVariants(ButtonVariant.LUMO_ERROR);
        amount.setValueChangeMode(ValueChangeMode.EAGER);

        final Double[] total = {0d};
        Span span = new Span();
        amount.addValueChangeListener(e -> {
            if (e.getValue() == null || e.getValue().toString().equals("")) {
                span.setVisible(false);
            } else {
                span.setVisible(true);
                total[0] = e.getValue() *
                        stockService.findFirstTmByName(autocomplete.getValue()).getReleasePrice();
                if (total[0] >= 0 && !autocomplete.getValue().equals("")) {
                    span.setText(String.format("Total: $%s", total[0]));
                }
            }
            autocomplete.addValueChangeListener(ev -> {
                if (e.getValue() == null || e.getValue().toString().equals("")) {
                    span.setVisible(false);
                } else {
                    span.setVisible(true);
                    total[0] = e.getValue() *
                            stockService.findFirstTmByName(autocomplete.getValue()).getReleasePrice();
                    if (total[0] >= 0 && !autocomplete.getValue().equals("")) {
                        span.setText(String.format("Total: $%s", total[0]));
                    }
                }
            });
        });
        formLayout.add(new H2("Purchase new trademark"), autocomplete, info, amount, span, purchase, cancel);

        dialog.add(formLayout);
        dialog.open();
    }

    private ValidationResult validateAmount(Long amount, ValueContext ctx) {
        String errorMsg = stockService.validate(amount);
        if (errorMsg == null) {
            return ValidationResult.ok();
        }
        return ValidationResult.error(errorMsg);
    }

    private ValidationResult validateAmountToSell(Long amount, Long available,ValueContext ctx) {
        String errorMsg = stockService.validateAmountToSell(amount, available);
        if (errorMsg == null) {
            return ValidationResult.ok();
        }
        return ValidationResult.error(errorMsg);
    }

    private ValidationResult validateTmName(String tmName, ValueContext ctx) {
        String errorMsg = stockService.validate(tmName);
        if (errorMsg == null) {
            return ValidationResult.ok();
        }
        return ValidationResult.error(errorMsg);
    }


    private String newLineHtml(String text) {
        return String.format("<br>%s", text);
    }
}
