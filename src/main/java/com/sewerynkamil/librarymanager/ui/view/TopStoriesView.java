package com.sewerynkamil.librarymanager.ui.view;

import com.sewerynkamil.librarymanager.client.LibraryManagerClient;
import com.sewerynkamil.librarymanager.dto.enumerated.NYTimesSection;
import com.sewerynkamil.librarymanager.dto.nytimes.NYTimesResultsDto;
import com.sewerynkamil.librarymanager.ui.MainView;
import com.sewerynkamil.librarymanager.ui.utils.LibraryConst;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

/**
 * Author Kamil Seweryn
 */

@Route(value = LibraryConst.ROUTE_TOP_STORIES, layout = MainView.class)
@PageTitle(LibraryConst.TITLE_TOP_STORIES)
@Secured({"ROLE_User", "ROLE_Admin"})
public class TopStoriesView extends VerticalLayout {
    private LibraryManagerClient client;

    private Grid<NYTimesResultsDto> grid = new Grid<>(NYTimesResultsDto.class);
    private HorizontalLayout top = new HorizontalLayout();

    private ComboBox<String> section = new ComboBox<>();
    private Label copyright = new Label("Copyright (c) 2019 The New York Times Company. All Rights Reserved.");

    @Autowired
    public TopStoriesView(LibraryManagerClient client) {
        this.client = client;

        top.add(section, copyright);

        setSizeFull();

        add(top, grid);

        grid.setColumns("byline", "title", "published_date", "url");
        grid.addThemeVariants(GridVariant.LUMO_COMPACT, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        grid.getColumnByKey("published_date").setHeader("Published date");
        grid.getColumnByKey("url").setHeader("Read");

        section.setItems(NYTimesSection.sectionList());
        section.setPlaceholder("Select section");
        section.setClearButtonVisible(true);
        section.addValueChangeListener(e -> getTopStories(e.getValue()));

        getTopStories(section.getValue());
    }

    public void getTopStories(String section) {
        if(StringUtils.isBlank(section)) {
            grid.setItems(client.getAllTopStoriesBySection("Books").getResults());
        } else {
            grid.setItems(client.getAllTopStoriesBySection(section.toLowerCase()).getResults());
        }
    }
}