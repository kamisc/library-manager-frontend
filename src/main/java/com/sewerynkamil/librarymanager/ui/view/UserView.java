package com.sewerynkamil.librarymanager.ui.view;

import com.sewerynkamil.librarymanager.client.LibraryManagerUsersClient;
import com.sewerynkamil.librarymanager.dto.UserDto;
import com.sewerynkamil.librarymanager.ui.MainView;
import com.sewerynkamil.librarymanager.ui.components.ButtonFactory;
import com.sewerynkamil.librarymanager.ui.components.ButtonType;
import com.sewerynkamil.librarymanager.ui.components.ComponentDesigner;
import com.sewerynkamil.librarymanager.ui.utils.LibraryConst;
import com.sewerynkamil.librarymanager.ui.view.form.UserForm;
import com.vaadin.flow.component.button.Button;
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

@Route(value = LibraryConst.ROUTE_USERS, layout = MainView.class)
@PageTitle(LibraryConst.TITLE_USERS)
@Secured("ROLE_Admin")
public class UserView extends VerticalLayout {
    private ComponentDesigner componentDesigner = new ComponentDesigner();
    private ButtonFactory buttonFactory = new ButtonFactory();
    private LibraryManagerUsersClient usersClient;

    private Button addNewUserButton = buttonFactory.createButton(ButtonType.ADDBUTTON, "Add new user", "225px");
    private Grid<UserDto> grid = new Grid<>(UserDto.class);

    private TextField nameFilter = new TextField();
    private TextField surnameFilter = new TextField();
    private TextField emailFilter = new TextField();
    private HeaderRow filterRow = grid.appendHeaderRow();

    private UserForm userForm;

    @Autowired
    public UserView(LibraryManagerUsersClient usersClient, UserForm userForm) {
        this.usersClient = usersClient;
        this.userForm = userForm;

        setSizeFull();
        add(addNewUserButton, grid);
        userList();

        grid.addThemeVariants(GridVariant.LUMO_COMPACT, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        grid.setColumns("name", "surname", "email", "phoneNumber", "role");
        grid.getColumnByKey("name").setTextAlign(ColumnTextAlign.START);

        componentDesigner.generateFilter(nameFilter, "Filter by name");
        nameFilter.addValueChangeListener(e -> {
                    if (StringUtils.isBlank(e.getValue())) {
                        userList();
                    } else {
                        grid.setItems(usersClient.getAllUsersByNameStartsWithIgnoreCase(e.getValue().toLowerCase()));
                    }
                }
        );

        componentDesigner.generateFilter(surnameFilter, "Filter by surname");
        surnameFilter.addValueChangeListener(e -> {
                    if (StringUtils.isBlank(e.getValue())) {
                        userList();
                    } else {
                        grid.setItems(usersClient.getAllUsersBySurnameStartsWithIgnoreCase(e.getValue().toLowerCase()));
                    }
                }
        );

        componentDesigner.generateFilter(emailFilter, "Filter by email");
        emailFilter.addValueChangeListener(e -> {
                    if (StringUtils.isBlank(e.getValue())) {
                        userList();
                    } else {
                        grid.setItems(usersClient.getAllUsersByEmailStartsWithIgnoreCase(e.getValue().toLowerCase()));
                    }
                }
        );

        filterRow.getCell(grid.getColumnByKey("name")).setComponent(nameFilter);
        filterRow.getCell(grid.getColumnByKey("surname")).setComponent(surnameFilter);
        filterRow.getCell(grid.getColumnByKey("email")).setComponent(emailFilter);

        grid.asSingleSelect().addValueChangeListener(e -> {
            userForm.editUser(e.getValue());
        });

        userForm.setChangeHandler(() -> {
            userForm.setVisible(false);
            userList();
        });

        addNewUserButton.addClickListener(e -> userForm.editUser(new UserDto()));
    }

    private void userList() {
        grid.setDataProvider(DataProvider.fromFilteringCallbacks(
                query -> {
                    int offset = query.getOffset();
                    int limit = query.getLimit();
                    query.getFilter();
                    return usersClient.getAllUsersWithLazyLoading(offset, limit).stream();
                },
                query -> usersClient.countUsers().intValue()
        ));
    }
}