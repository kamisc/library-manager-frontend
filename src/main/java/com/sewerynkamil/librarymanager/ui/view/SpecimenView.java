package com.sewerynkamil.librarymanager.ui.view;

import com.sewerynkamil.librarymanager.client.LibraryManagerClient;
import com.sewerynkamil.librarymanager.dto.SpecimenDto;
import com.sewerynkamil.librarymanager.dto.enumerated.Status;
import com.sewerynkamil.librarymanager.security.SecurityUtils;
import com.sewerynkamil.librarymanager.ui.components.ButtonFactory;
import com.sewerynkamil.librarymanager.ui.components.ButtonType;
import com.sewerynkamil.librarymanager.ui.view.form.BookForm;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
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

    @Autowired
    public SpecimenView(LibraryManagerClient client) {
        this.client = client;

        bookTitle.setClassName("specimen-view-book-title");
        add(elements);

        grid.setWidth("820px");
        grid.setColumns("id", "publisher", "yearOfPublication", "status", "isbn");
        grid.getColumnByKey("id").setWidth("75px").setFlexGrow(0).setTextAlign(ColumnTextAlign.START);

        close.addClickListener(e -> dialog.close());

        setVisible(false);
    }

    public void showSpecimens(Long bookId) {
        bookTitle.setText(client.getOneBook(bookId).getTitle());

        if(SecurityUtils.isAccessGranted(BookForm.class)) {
            grid.setHeight("370px");
            elements.remove(close);
            elements.add(bookTitle, grid, addNewSpecimenButton, close);

            grid.setItems(client.getAllSpecimensForOneBook(bookId));
            dialog.add(this);
            dialog.open();
            setVisible(true);
        } else {
            elements.add(bookTitle, grid, close);

            grid.setItems(client.getAllAvailableSpecimensForOneBook(Status.AVAILABLE.getStatus(), bookId));
            dialog.add(this);
            dialog.open();
            setVisible(true);
        }
    }
}
