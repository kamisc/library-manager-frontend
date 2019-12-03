package com.sewerynkamil.librarymanager.ui.view;

import com.sewerynkamil.librarymanager.client.LibraryManagerAudiobooksClient;
import com.sewerynkamil.librarymanager.dto.wolnelektury.WolneLekturyAudiobookDto;
import com.sewerynkamil.librarymanager.ui.MainView;
import com.sewerynkamil.librarymanager.ui.components.ComponentDesigner;
import com.sewerynkamil.librarymanager.ui.utils.LibraryConst;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
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
    private LibraryManagerAudiobooksClient audiobooksClient;
    private ComponentDesigner componentDesigner = new ComponentDesigner();

    private Grid<WolneLekturyAudiobookDto> grid = new Grid<>(WolneLekturyAudiobookDto.class);

    private TextField titleFilter = new TextField();
    private TextField authorFilter = new TextField();
    private HeaderRow filterRow = grid.appendHeaderRow();

    @Autowired
    public AudiobookView(LibraryManagerAudiobooksClient audiobooksClient) {
        this.audiobooksClient = audiobooksClient;

        setSizeFull();
        add(grid);
        audiobookList();

        grid.addThemeVariants(GridVariant.LUMO_COMPACT, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        grid.setColumns("author", "title", "genre", "epoch");
        grid.getColumnByKey("author").setTextAlign(ColumnTextAlign.START);
        grid.addComponentColumn(audiobookDto -> createUrlButton(audiobookDto));

        componentDesigner.generateFilter(authorFilter, "Filter by author");
        authorFilter.addValueChangeListener(e -> {
                if (StringUtils.isBlank(e.getValue())) {
                    audiobookList();
                } else {
                    grid.setItems(audiobooksClient.getAllAudiobooksByAuthorStartsWithIgnoreCase(e.getValue().toLowerCase()));
                }
            }
        );

        componentDesigner.generateFilter(titleFilter, "Filter by title");
        titleFilter.addValueChangeListener(e -> {
                if (StringUtils.isBlank(e.getValue())) {
                    audiobookList();
                } else {
                    grid.setItems(audiobooksClient.getAllAudiobooksByTitleStartsWithIgnoreCase(e.getValue().toLowerCase()));
                }
            }
        );

        filterRow.getCell(grid.getColumnByKey("title")).setComponent(titleFilter);
        filterRow.getCell(grid.getColumnByKey("author")).setComponent(authorFilter);
    }

    private void audiobookList() {
        grid.setDataProvider(DataProvider.fromFilteringCallbacks(
                query -> {
                    int offset = query.getOffset();
                    int limit = query.getLimit();
                    return audiobooksClient.getAllAudiobooksWithLazyLoading(offset, limit).stream();
                },
                query -> audiobooksClient.countAudiobooks()
        ));
    }

    private Button createUrlButton(WolneLekturyAudiobookDto wolneLekturyAudiobookDto) {
        Button button = new Button("Listen", clickEvent -> {
            UI.getCurrent().getPage().open(wolneLekturyAudiobookDto.getUrl());
        });
        button.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
        return button;
    }
}