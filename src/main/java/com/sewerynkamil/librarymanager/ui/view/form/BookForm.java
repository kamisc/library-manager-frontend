package com.sewerynkamil.librarymanager.ui.view.form;

import com.sewerynkamil.librarymanager.client.LibraryManagerClient;
import com.sewerynkamil.librarymanager.dto.BookDto;
import com.sewerynkamil.librarymanager.dto.enumerated.Category;
import com.sewerynkamil.librarymanager.ui.components.ButtonFactory;
import com.sewerynkamil.librarymanager.ui.components.ButtonType;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

/**
 * Author Kamil Seweryn
 */

@SpringComponent
@UIScope
@Secured("ROLE_Admin")
public class BookForm extends FormLayout implements KeyNotifier {
    private ButtonFactory buttonFactory = new ButtonFactory();
    private LibraryManagerClient client;
    private BookDto bookDto;

    private ChangeHandler changeHandler;

    private TextField author = new TextField("Author");
    private TextField title = new TextField("Title");
    private ComboBox<String> category = new ComboBox<>("Category");
    private NumberField yearOfFirstPublication = new NumberField("First publication");
    private NumberField isbn = new NumberField("ISBN");

    private Button save = buttonFactory.createButton(ButtonType.SAVE, "Save", "225px");
    private Button update = buttonFactory.createButton(ButtonType.UPDATE, "Update", "225px");
    private Button reset = buttonFactory.createButton(ButtonType.RESET, "Reset", "225px");
    private Button delete = buttonFactory.createButton(ButtonType.DELETE, "Delete", "225px");
    private Button close = buttonFactory.createButton(ButtonType.CLOSE, "Close", "225px");

    private Dialog bookExist = new Dialog(new Label("This book exist in the base!"));
    private Dialog bookSaveSuccessful = new Dialog(new Label("The book has been added succesfully!"));
    private Dialog bookUpdateSuccessful = new Dialog(new Label("The book has been updated succesfully!"));
    private Dialog dialog = new Dialog();

    private Binder<BookDto> binder = new Binder<>(BookDto.class);

    @Autowired
    public BookForm(LibraryManagerClient client) {
        this.client = client;

        category.setItems(Category.categoryList());
        category.setPlaceholder("Select category");

        setSizeUndefined();

        setWidth("260px");

        add(author, title, category, yearOfFirstPublication, isbn, save, update, reset, delete, close);

        setVisible(false);
    }

    public final void editBook(BookDto b) {

        dialog.setCloseOnOutsideClick(false);

        if (b == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = b.getAuthor() != null;
        if(persisted) {
            // bookDto = bookService.findById(b.getId());
            dialog.add(this);
            dialog.open();
        } else {
            bookDto = b;
            dialog.add(this);
            dialog.open();
        }

        save.setVisible(!persisted);
        update.setVisible(persisted);
        reset.setVisible(persisted);
        delete.setVisible(persisted);
        binder.setBean(bookDto);
        setVisible(true);
        author.focus();
    }

    public void setChangeHandler(ChangeHandler h) {
        this.changeHandler = h;
    }
}