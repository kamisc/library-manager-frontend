package com.sewerynkamil.librarymanager.ui.view.form;

import com.sewerynkamil.librarymanager.client.LibraryManagerClient;
import com.sewerynkamil.librarymanager.dto.BookDto;
import com.sewerynkamil.librarymanager.dto.enumerated.Category;
import com.sewerynkamil.librarymanager.ui.components.ButtonFactory;
import com.sewerynkamil.librarymanager.ui.components.ButtonType;
import com.sewerynkamil.librarymanager.utils.StringIntegerConverter;
import com.sewerynkamil.librarymanager.utils.StringLongConverter;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import java.time.LocalDate;

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
    private TextField yearOfFirstPublication = new TextField("First publication");
    private TextField isbn = new TextField("ISBN");

    private Button save = buttonFactory.createButton(ButtonType.SAVE, "Save", "225px");
    private Button update = buttonFactory.createButton(ButtonType.UPDATE, "Update", "225px");
    private Button reset = buttonFactory.createButton(ButtonType.RESET, "Reset", "225px");
    private Button delete = buttonFactory.createButton(ButtonType.DELETE, "Delete", "225px");
    private Button close = buttonFactory.createButton(ButtonType.CLOSE, "Close", "225px");

    private Notification bookExist = new Notification("This book exist in the base!", 3000);
    private Notification bookSaveSuccessful = new Notification("The book has been added succesfully!", 3000);
    private Notification bookUpdateSuccessful = new Notification("The book has been updated succesfully!", 3000);
    private Notification bookDeleteSuccessful = new Notification("The book has been deleted succesfully!", 3000);
    private Dialog dialog = new Dialog();

    private Binder<BookDto> binder = new Binder<>(BookDto.class);

    private String oldTitle;
    private Long id;

    @Autowired
    public BookForm(LibraryManagerClient client) {
        this.client = client;

        bookExist.addThemeVariants(NotificationVariant.LUMO_ERROR);
        bookSaveSuccessful.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        bookUpdateSuccessful.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        bookDeleteSuccessful.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

        category.setItems(Category.categoryList());
        category.setPlaceholder("Select category");

        setSizeUndefined();

        setWidth("260px");

        add(author, title, category, yearOfFirstPublication, isbn, save, update, reset, delete, close);

        author.setClearButtonVisible(true);
        title.setClearButtonVisible(true);
        category.setClearButtonVisible(true);
        yearOfFirstPublication.setClearButtonVisible(true);
        isbn.setClearButtonVisible(true);

        author.setRequired(true);
        title.setRequired(true);
        category.setRequired(true);
        yearOfFirstPublication.setRequired(true);
        isbn.setRequired(true);

        author.setErrorMessage("Required field");
        title.setErrorMessage("Required field");
        category.setErrorMessage("Required field");
        yearOfFirstPublication.setErrorMessage("Required field");
        isbn.setErrorMessage("Required field");

        binder.forField(author)
                .asRequired("Required field")
                .bind(BookDto::getAuthor, BookDto::setAuthor);
        binder.forField(title)
                .asRequired("Required field")
                .bind(BookDto::getTitle, BookDto::setTitle);
        binder.forField(category)
                .asRequired("Required field")
                .bind(BookDto::getCategory, BookDto::setCategory);
        binder.forField(yearOfFirstPublication)
                .asRequired("Required field")
                .withConverter(new StringIntegerConverter())
                .withValidator(year -> year >= 1600 && year <= LocalDate.now().getYear(), "Dosen't look like a year")
                .bind(BookDto::getYearOfFirstPublication, BookDto::setYearOfFirstPublication);
        binder.forField(isbn)
                .asRequired("Required field")
                .withValidator(isbn -> isbn.length() >= 10 && isbn.length() <= 13, "Invalid ISBN")
                .withConverter(new StringLongConverter())
                .bind(BookDto::getIsbn, BookDto::setIsbn);

        save.addClickListener(e -> save());
        update.addClickListener(e -> update());
        delete.addClickListener(e -> delete());
        reset.addClickListener(e -> editBook(bookDto));
        close.addClickListener(e -> dialog.close());

        setVisible(false);
    }

    private void save() {
        if(!client.isBookExist(title.getValue())) {
            client.saveNewBook(bookDto);
            bookSaveSuccessful.open();
            changeHandler.onChange();
            dialog.close();
        } else {
            bookExist.open();
        }
    }

    private void update() {
        if(!title.getValue().equals(oldTitle) && client.isBookExist(title.getValue())) {
            bookExist.open();
        } else {
            client.updateBook(bookDto);
            bookUpdateSuccessful.open();
            changeHandler.onChange();
            dialog.close();
        }
    }

    private void delete() {
        client.deleteBook(id);
        bookDeleteSuccessful.open();
        changeHandler.onChange();
        dialog.close();
    }

    public final void editBook(BookDto b) {
        dialog.setCloseOnOutsideClick(false);

        if (b == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = b.getId() != null;
        if(persisted) {
            bookDto = client.getOneBook(b.getId());

            id = bookDto.getId();
            oldTitle = bookDto.getTitle();

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