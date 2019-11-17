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
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
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
        grid.getColumnByKey("author").setTextAlign(ColumnTextAlign.START);
        grid.getColumnByKey("url").setHeader("Listen");

        generateFilter(authorFilter, "Filter by author");
        authorFilter.addValueChangeListener(e -> {
                if (StringUtils.isBlank(e.getValue())) {
                    audiobookList();
                } else {
                    grid.setItems(libraryManagerClient.getAllAudiobooksByAuthorStartsWithIgnoreCase(e.getValue().toLowerCase()));
                }
            }
        );

        generateFilter(titleFilter, "Filter by title");
        titleFilter.addValueChangeListener(e -> {
                if (StringUtils.isBlank(e.getValue())) {
                    audiobookList();
                } else {
                    grid.setItems(libraryManagerClient.getAllAudiobooksByTitleStartsWithIgnoreCase(e.getValue().toLowerCase()));
                }
            }
        );

        filterRow.getCell(grid.getColumnByKey("title")).setComponent(titleFilter);
        filterRow.getCell(grid.getColumnByKey("author")).setComponent(authorFilter);

        audiobookList();
    }

    private void audiobookList() {
        grid.setDataProvider(DataProvider.fromFilteringCallbacks(
                query -> {
                    int offset = query.getOffset();
                    int limit = query.getLimit();
                    return libraryManagerClient.getAllAudiobooksWithLazyLoading(offset, limit).stream();
                },
                query -> libraryManagerClient.countAudiobooks()
        ));
    }

    private void generateFilter(TextField field, String placeholder) {
        field.setPlaceholder(placeholder);
        field.setValueChangeMode(ValueChangeMode.EAGER);
        field.setClearButtonVisible(true);
    }
}