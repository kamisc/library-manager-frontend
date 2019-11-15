package com.sewerynkamil.librarymanager.ui.view;

import com.sewerynkamil.librarymanager.client.LibraryManagerClient;
import com.sewerynkamil.librarymanager.dto.BookDto;
import com.sewerynkamil.librarymanager.security.SecurityUtils;
import com.sewerynkamil.librarymanager.ui.MainView;
import com.sewerynkamil.librarymanager.ui.components.ButtonFactory;
import com.sewerynkamil.librarymanager.ui.components.ButtonType;
import com.sewerynkamil.librarymanager.ui.utils.LibraryConst;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import java.util.Arrays;

/**
 * Author Kamil Seweryn
 */

@Route(value = LibraryConst.ROUTE_BOOKS, layout = MainView.class)
@PageTitle(LibraryConst.TITLE_BOOKS)
@Secured({"ROLE_User", "ROLE_Admin"})
public class BookView extends VerticalLayout {
    private LibraryManagerClient libraryManagerClient;
    private ButtonFactory buttonFactory = new ButtonFactory();

    private Button addNewBookButton = buttonFactory.createButton(ButtonType.ADDBUTTON, "Add new book", "225px");
    private Grid<BookDto> grid = new Grid<>(BookDto.class);
    private TextField titleFilter = new TextField();
    private TextField authorFilter = new TextField();
    private TextField categoryFilter = new TextField();
    private HeaderRow filterRow = grid.appendHeaderRow();

    HorizontalLayout actions = new HorizontalLayout(titleFilter);
    HorizontalLayout editors = new HorizontalLayout(grid);

    @Autowired
    public BookView(LibraryManagerClient libraryManagerClient) {
        this.libraryManagerClient = libraryManagerClient;

        editors.setSizeFull();

        add(actions, editors);

        grid.addThemeVariants(GridVariant.LUMO_COMPACT, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        grid.setColumns("author", "title", "category", "yearOfFirstPublication", "isbn");
        grid.getColumnByKey("author").setFlexGrow(0).setTextAlign(ColumnTextAlign.START);

        titleFilter.setPlaceholder("Filter by title");

        authorFilter.setPlaceholder("Filter by author");

        categoryFilter.setPlaceholder("Filter by category");

        filterRow.getCell(grid.getColumnByKey("title")).setComponent(titleFilter);
        filterRow.getCell(grid.getColumnByKey("author")).setComponent(authorFilter);
        filterRow.getCell(grid.getColumnByKey("category")).setComponent(categoryFilter);

        grid.setItems(Arrays.stream(libraryManagerClient.getAllBooks().getBody()));
    }

}
