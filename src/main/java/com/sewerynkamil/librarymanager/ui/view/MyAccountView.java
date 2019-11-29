package com.sewerynkamil.librarymanager.ui.view;

import com.sewerynkamil.librarymanager.client.LibraryManagerClient;
import com.sewerynkamil.librarymanager.dto.BookDto;
import com.sewerynkamil.librarymanager.dto.RentDto;
import com.sewerynkamil.librarymanager.dto.SpecimenDto;
import com.sewerynkamil.librarymanager.dto.UserDto;
import com.sewerynkamil.librarymanager.security.SecurityUtils;
import com.sewerynkamil.librarymanager.ui.MainView;
import com.sewerynkamil.librarymanager.ui.components.ButtonFactory;
import com.sewerynkamil.librarymanager.ui.components.ButtonType;
import com.sewerynkamil.librarymanager.ui.utils.LibraryConst;
import com.sewerynkamil.librarymanager.ui.view.form.MyAccountForm;
import com.sewerynkamil.librarymanager.ui.view.form.UserForm;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Author Kamil Seweryn
 */

@Route(value = LibraryConst.ROUTE_MY_ACCOUNT, layout = MainView.class)
@PageTitle(LibraryConst.TITLE_MY_ACCOUNT)
@Secured({"ROLE_User", "ROLE_Admin"})
public class MyAccountView extends VerticalLayout {
    private LibraryManagerClient client;
    private ButtonFactory buttonFactory = new ButtonFactory();
    private UserDto userDto;

    private Button editMyUserDataButton = buttonFactory.createButton(ButtonType.UPDATE, "Update My data", "192px");

    private VerticalLayout userDetails = new VerticalLayout();
    private VerticalLayout userRents = new VerticalLayout();

    private HorizontalLayout myAccount = new HorizontalLayout();

    private Grid<RentDto> grid = new Grid<>(RentDto.class);

    private TextField name = new TextField("Name");
    private TextField surname = new TextField("Surame");
    private EmailField email = new EmailField("E-mail");
    private TextField phoneNumber = new TextField("Phone number");

    private MyAccountForm myAccountForm;

    @Autowired
    public MyAccountView(LibraryManagerClient client, MyAccountForm myAccountForm) {
        this.client = client;
        this.myAccountForm = myAccountForm;
        userDto = getCurrentUser(client);

        setAlignItems(Alignment.CENTER);

        grid.setItems(client.getAllRentsByUserId(userDto.getId()));
        grid.setColumns("rentId", "specimenId", "bookTitle", "rentDate", "returnDate");
        grid.addComponentColumn(rentDto -> createProlongationButton(grid, rentDto, userDto));

        userRents.setClassName("my-account-user-rents");
        userRents.add(grid);

        userDetails.setClassName("my-account-user-details");
        userDetails.setWidth("25%");
        userDetails.add(name, surname, email, phoneNumber, editMyUserDataButton);

        myAccount.add(userDetails, userRents);
        myAccount.setClassName("my-account");

        setFieldsValues(userDto);
        setFieldsReadOnly();

        add(myAccount);

        myAccountForm.setChangeHandler(() -> {
            myAccountForm.setVisible(false);
            UI.getCurrent().getPage().reload();
        });

        if(SecurityUtils.isAccessGranted(UserView.class)) {
            myAccount.remove(userRents);
        }

        editMyUserDataButton.addClickListener(e -> myAccountForm.editUser(userDto));
    }

    private UserDto getCurrentUser(LibraryManagerClient client) {
        return client.getOneUserByEmail(getPrincipalUsername());
    }

    private String getPrincipalUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        return username;
    }

    private void setFieldsValues(UserDto userDto) {
        name.setValue(userDto.getName());
        surname.setValue(userDto.getSurname());
        email.setValue(userDto.getEmail());
        phoneNumber.setValue(userDto.getPhoneNumber().toString());
    }

    private void setFieldsReadOnly() {
        name.setReadOnly(true);
        surname.setReadOnly(true);
        email.setReadOnly(true);
        phoneNumber.setReadOnly(true);
    }

    private Button createProlongationButton(Grid<RentDto> grid, RentDto rentDto, UserDto userDto) {
        Button button = new Button("Prolongate rent", clickEvent -> {
            client.prolongationRent(rentDto.getSpecimenId(), userDto.getId());
            grid.setItems(client.getAllRentsByUserId(userDto.getId()));
        });
        button.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
        return button;
    }
}