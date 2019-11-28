package com.sewerynkamil.librarymanager.ui.view;

import com.sewerynkamil.librarymanager.client.LibraryManagerClient;
import com.sewerynkamil.librarymanager.dto.enumerated.NYTimesSection;
import com.sewerynkamil.librarymanager.dto.nytimes.NYTimesResultsDto;
import com.sewerynkamil.librarymanager.ui.MainView;
import com.sewerynkamil.librarymanager.ui.utils.LibraryConst;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
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

    private ComboBox<String> section = new ComboBox<>("Section");

    HorizontalLayout editors = new HorizontalLayout(section, grid);

    @Autowired
    public TopStoriesView(LibraryManagerClient client) {
        this.client = client;

        editors.setSizeFull();

        add(editors);

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
