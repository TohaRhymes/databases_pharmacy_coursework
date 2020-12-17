package se.ifmo.pepe.cwdb.ui.view.trademarks;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import se.ifmo.pepe.cwdb.backend.model.Trademarks;
import se.ifmo.pepe.cwdb.backend.service.TrademarkService;
import se.ifmo.pepe.cwdb.ui.MainLayout;

@Route(value = "trademark", layout = MainLayout.class)
@PageTitle("Trademarks | Coursage")
public class TrademarkView extends VerticalLayout {
    private final Grid<Trademarks> grid = new Grid<>(Trademarks.class);
    private final TextField filterText = new TextField();
    private final TrademarkForm trademarkForm;
    private final TrademarkService trademarkService;

    public TrademarkView(TrademarkService trademarkService) {
        this.trademarkService = trademarkService;
        addClassName("list-view");
        setSizeFull();

        configureGrid();

        trademarkForm = new TrademarkForm(trademarkService.findAll());
        trademarkForm.addListener(TrademarkForm.SaveEvent.class, this::saveTm);
        trademarkForm.addListener(TrademarkForm.DeleteEvent.class, this::deleteTm);
        trademarkForm.addListener(TrademarkForm.CloseEvent.class, e -> closeEditor());
        closeEditor();

        Div content = new Div(grid, trademarkForm);
        content.addClassName("content");
        content.setSizeFull();

        add(getToolbar(), content);
        updateGrid();
        closeEditor();
    }

    private void saveTm(TrademarkForm.SaveEvent event) {
        try {
            trademarkService.save(event.getTrademark());
            updateGrid();
            closeEditor();
            Notification.show("Data was saved", 5000, Notification.Position.TOP_END);
        } catch (TrademarkService.TrademarkServiceSavingException e) {
            e.printStackTrace();
            Notification.show("Empty fields have left. Fill them before saving new record", 5000, Notification.Position.TOP_END);
        }

    }

    private void deleteTm(TrademarkForm.DeleteEvent event) {
        trademarkService.delete(event.getTrademark());
        updateGrid();
        closeEditor();
    }


    private void configureGrid() {
        grid.addClassName("tm-grid");
        grid.setSizeFull();
        grid.setColumns("id", "name", "doze", "releasePrice");

        grid.asSingleSelect().addValueChangeListener(e -> editTm(e.getValue()));
    }

    public void editTm(Trademarks tm) {
        if (tm == null)
            closeEditor();
        else {
            trademarkForm.setTm(tm);
            trademarkForm.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        trademarkForm.setTm(null);
        trademarkForm.setVisible(false);
        removeClassName("editing");
    }

    private void updateGrid() {
        grid.setItems(trademarkService.findAll(filterText.getValue()));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateGrid());

        Button addTrademarkButton = new Button("Add Trademark");
        addTrademarkButton.addClickListener(e -> addTrademark());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addTrademarkButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    void addTrademark() {
        grid.asSingleSelect().clear();
        editTm(new Trademarks());
    }
}