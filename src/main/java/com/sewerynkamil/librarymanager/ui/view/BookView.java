package com.sewerynkamil.librarymanager.ui.view;

import com.sewerynkamil.librarymanager.client.LibraryManagerClient;
import com.sewerynkamil.librarymanager.dto.BookDto;
import com.sewerynkamil.librarymanager.security.SecurityUtils;
import com.sewerynkamil.librarymanager.ui.MainView;
import com.sewerynkamil.librarymanager.ui.components.ButtonFactory;
import com.sewerynkamil.librarymanager.ui.components.ButtonType;
import com.sewerynkamil.librarymanager.ui.utils.LibraryConst;
import com.sewerynkamil.librarymanager.ui.view.form.BookForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

/**
 * Author Kamil Seweryn
 */

@Route(value = LibraryConst.ROUTE_BOOKS, layout = MainView.class)
@PageTitle(LibraryConst.TITLE_BOOKS)
@Secured({"ROLE_User", "ROLE_Admin"})
public class BookView extends VerticalLayout {
    private LibraryManagerClient client;
    private ButtonFactory buttonFactory = new ButtonFactory();

    private Button addNewBookButton = buttonFactory.createButton(ButtonType.ADDBUTTON, "Add new book", "225px");
    private Grid<BookDto> grid = new Grid<>(BookDto.class);

    private TextField authorFilter = new TextField();
    private TextField titleFilter = new TextField();
    private TextField categoryFilter = new TextField();
    private HeaderRow filterRow = grid.appendHeaderRow();

    private HorizontalLayout actions = new HorizontalLayout();
    private HorizontalLayout editors = new HorizontalLayout(grid);

    private BookForm bookForm;
    private SpecimenView specimenView;

    @Autowired
    public BookView(LibraryManagerClient client, BookForm bookForm, SpecimenView specimenView) {
        this.client = client;
        this.bookForm = bookForm;
        this.specimenView = specimenView;

        editors.setSizeFull();

        add(actions, editors);

        grid.addThemeVariants(GridVariant.LUMO_COMPACT, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        grid.setColumns("author", "title", "category", "yearOfFirstPublication", "isbn");
        grid.getColumnByKey("author").setTextAlign(ColumnTextAlign.START);
        grid.addComponentColumn(bookDto -> createSpecimenButton(grid, bookDto)).setHeader("Specimens");

        generateFilter(authorFilter, "Filter by author");
        authorFilter.addValueChangeListener(e -> {
                if (StringUtils.isBlank(e.getValue())) {
                    bookList();
                } else {
                    grid.setItems(client.getAllBooksByAuthorStartsWithIgnoreCase(e.getValue().toLowerCase()));
                }
            }
        );

        generateFilter(titleFilter, "Filter by title");
        titleFilter.addValueChangeListener(e -> {
                if (StringUtils.isBlank(e.getValue())) {
                        bookList();
                } else {
                    grid.setItems(client.getAllBooksByTitleStartsWithIgnoreCase(e.getValue().toLowerCase()));
                }
            }
        );

        generateFilter(categoryFilter, "Filter by category");
        categoryFilter.addValueChangeListener(e -> {
                if (StringUtils.isBlank(e.getValue())) {
                    bookList();
                } else {
                        grid.setItems(client.getAllBooksByCategoryStartsWithIgnoreCase(e.getValue().toLowerCase()));
                }
            }
        );

        filterRow.getCell(grid.getColumnByKey("author")).setComponent(authorFilter);
        filterRow.getCell(grid.getColumnByKey("title")).setComponent(titleFilter);
        filterRow.getCell(grid.getColumnByKey("category")).setComponent(categoryFilter);

        if(SecurityUtils.isAccessGranted(BookForm.class)) {
            grid.asSingleSelect().addValueChangeListener(e -> {
                bookForm.editBook(e.getValue());
            });

            bookForm.setChangeHandler(() -> {
                bookForm.setVisible(false);
                bookList();
            });

            actions.add(addNewBookButton);

            addNewBookButton.addClickListener(e -> bookForm.editBook(new BookDto()));
        }

        bookList();
    }

    private void bookList() {
        grid.setDataProvider(DataProvider.fromFilteringCallbacks(
                query -> {
                    int offset = query.getOffset();
                    int limit = query.getLimit();
                    query.getFilter();
                    return client.getAllBooksWithLazyLoading(offset, limit).stream();
                },
                query -> client.countBooks().intValue()
        ));
    }

    private void generateFilter(TextField field, String placeholder) {
        field.setPlaceholder(placeholder);
        field.setValueChangeMode(ValueChangeMode.EAGER);
        field.setClearButtonVisible(true);
    }

    private Button createSpecimenButton(Grid<BookDto> grid, BookDto bookDto) {
        //@SuppressWarnings("unchecked")
        Button button = new Button("Show specimens", clickEvent -> {
            specimenView.showSpecimens(bookDto.getId());
        });
        button.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
        return button;
    }
}