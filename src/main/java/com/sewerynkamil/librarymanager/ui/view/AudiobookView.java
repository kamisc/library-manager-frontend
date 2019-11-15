package com.sewerynkamil.librarymanager.ui.view;

import com.sewerynkamil.librarymanager.client.LibraryManagerClient;
import com.sewerynkamil.librarymanager.dto.WolneLekturyAudiobookDto;
import com.sewerynkamil.librarymanager.ui.MainView;
import com.sewerynkamil.librarymanager.ui.utils.LibraryConst;
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

/**
 * Author Kamil Seweryn
 */

@Route(value = LibraryConst.ROUTE_AUDIOBOOKS, layout = MainView.class)
@PageTitle(LibraryConst.TITLE_AUDIOBOOKS)
@Secured({"ROLE_User", "ROLE_Admin"})
public class AudiobookView extends VerticalLayout {
    private LibraryManagerClient libraryManagerClient;

    private Grid<WolneLekturyAudiobookDto> grid = new Grid<>(WolneLekturyAudiobookDto.class);

    private TextField titleFilter = new TextField();
    private TextField authorFilter = new TextField();
    private HeaderRow filterRow = grid.appendHeaderRow();

    HorizontalLayout actions = new HorizontalLayout(titleFilter);
    HorizontalLayout editors = new HorizontalLayout(grid);

    @Autowired
    public AudiobookView(LibraryManagerClient libraryManagerClient) {
        this.libraryManagerClient = libraryManagerClient;

        editors.setSizeFull();

        add(actions, editors);

        grid.addThemeVariants(GridVariant.LUMO_COMPACT, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        grid.setColumns("author", "title", "genre", "epoch", "url");
        grid.getColumnByKey("author").setFlexGrow(0).setTextAlign(ColumnTextAlign.START);

        titleFilter.setPlaceholder("Filter by title");

        authorFilter.setPlaceholder("Filter by author");

        filterRow.getCell(grid.getColumnByKey("title")).setComponent(titleFilter);
        filterRow.getCell(grid.getColumnByKey("author")).setComponent(authorFilter);

        grid.setItems(libraryManagerClient.getAllAudiobooks());
    }
}
