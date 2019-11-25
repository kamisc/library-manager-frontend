package com.sewerynkamil.librarymanager.ui.view;

import com.sewerynkamil.librarymanager.client.LibraryManagerClient;
import com.sewerynkamil.librarymanager.dto.BookDto;
import com.sewerynkamil.librarymanager.dto.SpecimenDto;
import com.sewerynkamil.librarymanager.dto.enumerated.Status;
import com.sewerynkamil.librarymanager.security.SecurityUtils;
import com.sewerynkamil.librarymanager.ui.components.ButtonFactory;
import com.sewerynkamil.librarymanager.ui.components.ButtonType;
import com.sewerynkamil.librarymanager.ui.view.form.BookForm;
import com.sewerynkamil.librarymanager.ui.view.form.SpecimenForm;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import java.util.ArrayList;
import java.util.List;

/**
 * Author Kamil Seweryn
 */

@SpringComponent
@UIScope
@Secured({"ROLE_User", "ROLE_Admin"})
public class SpecimenView extends FormLayout implements KeyNotifier {
    private LibraryManagerClient client;
    private ButtonFactory buttonFactory = new ButtonFactory();

    private Grid<SpecimenDto> grid = new Grid<>(SpecimenDto.class);
    private VerticalLayout elements = new VerticalLayout();

    private Button addNewSpecimenButton = buttonFactory.createButton(ButtonType.ADDBUTTON, "Add new specimen", "820px");
    private Button close = buttonFactory.createButton(ButtonType.CLOSE, "Close", "820px");

    private Dialog dialog = new Dialog();

    private Label bookTitle = new Label();

    public String title = "";
    public Long id;

    private SpecimenForm specimenForm;

    @Autowired
    public SpecimenView(LibraryManagerClient client, SpecimenForm specimenForm) {
        this.client = client;
        this.specimenForm = specimenForm;

        bookTitle.setClassName("specimen-view-book-title");
        add(elements);

        grid.setWidth("840px");
        grid.setColumns("id", "publisher", "yearOfPublication", "status", "isbn");
        grid.getColumnByKey("id").setWidth("75px").setFlexGrow(0).setTextAlign(ColumnTextAlign.START);

        if(SecurityUtils.isAccessGranted(SpecimenForm.class)) {
            grid.asSingleSelect().addValueChangeListener(e -> {
                specimenForm.editSpecimen(e.getValue());
            });

            specimenForm.setChangeHandler(() -> {
                specimenForm.setVisible(false);
                showSpecimens(id);
            });

            addNewSpecimenButton.addClickListener(e -> specimenForm.editSpecimen(new SpecimenDto(title)));
        }

        close.addClickListener(e -> dialog.close());

        setVisible(false);
    }

    public void showSpecimens(Long bookId) {
        id = client.getOneBook(bookId).getId();
        title = client.getOneBook(bookId).getTitle();
        bookTitle.setText(client.getOneBook(bookId).getTitle());

        getSpecimens(bookId);
        dialog.add(this);
        dialog.open();
        setVisible(true);
    }

    public void getSpecimens(Long bookId) {
        if(SecurityUtils.isAccessGranted(BookForm.class)) {
            grid.setItems(client.getAllSpecimensForOneBook(bookId));
            grid.setHeight("370px");
            elements.remove(close);
            elements.add(bookTitle, grid, addNewSpecimenButton, close);
        } else {
            elements.add(bookTitle, grid, close);
            grid.setItems(client.getAllAvailableSpecimensForOneBook(Status.AVAILABLE.getStatus(), bookId));
            grid.addComponentColumn(specimenDto -> createRentButton(grid, specimenDto));
        }
    }

    private Button createRentButton(Grid<SpecimenDto> grid, SpecimenDto specimenDto) {
        //@SuppressWarnings("unchecked")
        Button button = new Button("Rent this book", clickEvent -> {

        });
        button.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
        return button;
    }
}