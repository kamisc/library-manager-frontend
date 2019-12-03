package com.sewerynkamil.librarymanager.ui.view.form;

import com.sewerynkamil.librarymanager.client.LibraryManagerBooksClient;
import com.sewerynkamil.librarymanager.client.LibraryManagerRentsClient;
import com.sewerynkamil.librarymanager.dto.BookDto;
import com.sewerynkamil.librarymanager.dto.enumerated.Category;
import com.sewerynkamil.librarymanager.ui.components.ButtonFactory;
import com.sewerynkamil.librarymanager.ui.components.ButtonType;
import com.sewerynkamil.librarymanager.ui.components.ComponentDesigner;
import com.sewerynkamil.librarymanager.ui.utils.StringIntegerConverter;
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
public class BookForm extends FormLayout implements KeyNotifier, FormActions {
    private ButtonFactory buttonFactory = new ButtonFactory();
    private ComponentDesigner componentDesigner = new ComponentDesigner();
    private LibraryManagerBooksClient booksClient;
    private LibraryManagerRentsClient rentsClient;
    private BookDto bookDto;

    private ChangeHandler changeHandler;

    private TextField author = new TextField("Author");
    private TextField title = new TextField("Title");
    private TextField yearOfFirstPublication = new TextField("First publication");
    private ComboBox<String> category = new ComboBox<>("Category");

    private Button save = buttonFactory.createButton(ButtonType.SAVE, "Save", "225px");
    private Button update = buttonFactory.createButton(ButtonType.UPDATE, "Update", "225px");
    private Button reset = buttonFactory.createButton(ButtonType.RESET, "Reset", "225px");
    private Button delete = buttonFactory.createButton(ButtonType.DELETE, "Delete", "225px");
    private Button close = buttonFactory.createButton(ButtonType.CLOSE, "Close", "225px");

    private Notification bookExist = new Notification("This book exist in the base!", 3000);
    private Notification bookSaveSuccessful = new Notification("The book has been added succesfully!", 3000);
    private Notification bookUpdateSuccessful = new Notification("The book has been updated succesfully!", 3000);
    private Notification bookDeleteSuccessful = new Notification("The book has been deleted succesfully!", 3000);
    private Notification bookCantDelete = new Notification("You can't delete this book, beacause it is on rent!", 3000);
    private Dialog dialog = new Dialog();

    private Binder<BookDto> binder = new Binder<>(BookDto.class);

    @Autowired
    public BookForm(LibraryManagerBooksClient booksClient, LibraryManagerRentsClient rentsClient) {
        this.booksClient = booksClient;
        this.rentsClient = rentsClient;

        setSizeUndefined();
        setWidth("260px");
        add(author, title, category, yearOfFirstPublication, save, update, reset, delete, close);
        setVisible(false);

        bookExist.addThemeVariants(NotificationVariant.LUMO_ERROR);
        bookSaveSuccessful.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        bookUpdateSuccessful.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        bookDeleteSuccessful.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        bookCantDelete.addThemeVariants(NotificationVariant.LUMO_ERROR);

        componentDesigner.setComboboxOptions(Category.categoryList(), "Select category", category);
        componentDesigner.setTextFieldsOptions(author, title, yearOfFirstPublication);

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

        save.addClickListener(e -> save());
        update.addClickListener(e -> update());
        delete.addClickListener(e -> delete());
        reset.addClickListener(e -> editBook(bookDto));
        close.addClickListener(e -> dialog.close());
    }

    @Override
    public void save() {
        if(!booksClient.isBookExist(title.getValue())) {
            booksClient.saveNewBook(bookDto);
            componentDesigner.setActions(bookSaveSuccessful, changeHandler, dialog);
        } else {
            bookExist.open();
        }
    }

    @Override
    public void update() {
        if(!title.getValue().equals(bookDto.getTitle()) && booksClient.isBookExist(title.getValue())) {
            bookExist.open();
        } else {
            booksClient.updateBook(bookDto);
            componentDesigner.setActions(bookUpdateSuccessful, changeHandler, dialog);
        }
    }

    @Override
    public void delete() {
        if(rentsClient.isRentExistBySpecimenBookTitle(bookDto.getTitle())) {
            bookCantDelete.open();
        } else {
            booksClient.deleteBook(bookDto.getId());
            componentDesigner.setActions(bookDeleteSuccessful, changeHandler, dialog);
        }
    }

    public final void editBook(BookDto b) {
        dialog.setCloseOnOutsideClick(false);

        if (b == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = b.getId() != null;
        if(persisted) {
            bookDto = booksClient.getOneBook(b.getId());

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