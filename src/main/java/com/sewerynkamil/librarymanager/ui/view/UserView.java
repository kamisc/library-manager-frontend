package com.sewerynkamil.librarymanager.ui.view;

import com.sewerynkamil.librarymanager.client.LibraryManagerClient;
import com.sewerynkamil.librarymanager.dto.UserDto;
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

/**
 * Author Kamil Seweryn
 */

@Route(value = LibraryConst.ROUTE_USERS, layout = MainView.class)
@PageTitle(LibraryConst.TITLE_USERS)
@Secured("ROLE_Admin")
public class UserView extends VerticalLayout {
    private LibraryManagerClient client;
    private ButtonFactory buttonFactory = new ButtonFactory();

    private Button addNewUserButton = buttonFactory.createButton(ButtonType.ADDBUTTON, "Add new user", "225px");
    private Grid<UserDto> grid = new Grid<>(UserDto.class);
    private TextField nameFilter = new TextField();
    private TextField surnameFilter = new TextField();
    private TextField emailFilter = new TextField();
    private HeaderRow filterRow = grid.appendHeaderRow();

    private HorizontalLayout actions = new HorizontalLayout(addNewUserButton);
    private HorizontalLayout editors = new HorizontalLayout(grid);

    @Autowired
    public UserView(LibraryManagerClient client) {
        this.client = client;

        editors.setSizeFull();

        add(actions, editors);

        grid.addThemeVariants(GridVariant.LUMO_COMPACT, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        grid.setColumns("name", "surname", "email", "phoneNumber");
        grid.getColumnByKey("name").setTextAlign(ColumnTextAlign.START);
    }
}