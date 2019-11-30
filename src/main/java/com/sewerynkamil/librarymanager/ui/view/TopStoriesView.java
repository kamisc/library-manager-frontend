package com.sewerynkamil.librarymanager.ui.view;

import com.sewerynkamil.librarymanager.client.LibraryManagerClient;
import com.sewerynkamil.librarymanager.dto.enumerated.NYTimesSection;
import com.sewerynkamil.librarymanager.dto.nytimes.NYTimesResultsDto;
import com.sewerynkamil.librarymanager.ui.MainView;
import com.sewerynkamil.librarymanager.ui.components.ComponentDesigner;
import com.sewerynkamil.librarymanager.ui.utils.LibraryConst;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.ColumnTextAlign;
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
    private ComponentDesigner componentDesigner = new ComponentDesigner();
    private LibraryManagerClient client;

    private Grid<NYTimesResultsDto> grid = new Grid<>(NYTimesResultsDto.class);

    private HorizontalLayout top = new HorizontalLayout();

    private ComboBox<String> section = new ComboBox<>();

    private Label copyright = new Label("Copyright (c) 2019 The New York Times Company. All Rights Reserved.");

    @Autowired
    public TopStoriesView(LibraryManagerClient client) {
        this.client = client;

        setSizeFull();
        add(top, grid);
        getTopStories(section.getValue());

        top.add(section, copyright);

        grid.setColumns("byline", "title", "published_date");
        grid.addThemeVariants(GridVariant.LUMO_COMPACT, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        grid.getColumnByKey("byline").setWidth("150px");
        grid.getColumnByKey("title").setWidth("450px");
        grid.getColumnByKey("published_date").setHeader("Published date").setWidth("150px");
        grid.addComponentColumn(nyTimesResultsDto -> createUrlButton(nyTimesResultsDto)).setTextAlign(ColumnTextAlign.CENTER);

        componentDesigner.setComboboxOptions(NYTimesSection.sectionList(), "Select section", section);
        section.addValueChangeListener(e -> getTopStories(section.getValue()));
    }

    private void getTopStories(String section) {
        if(StringUtils.isBlank(section)) {
            grid.setItems(client.getAllTopStoriesBySection("Books").getResults());
        } else {
            grid.setItems(client.getAllTopStoriesBySection(section.toLowerCase()).getResults());
        }
    }

    private Button createUrlButton(NYTimesResultsDto nyTimesResultsDto) {
        Button button = new Button("Read", clickEvent -> {
            UI.getCurrent().getPage().open(nyTimesResultsDto.getUrl());
        });
        button.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
        return button;
    }
}