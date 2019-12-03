package com.sewerynkamil.librarymanager.ui.view.form;

import com.sewerynkamil.librarymanager.client.LibraryManagerAuthenticationClient;
import com.sewerynkamil.librarymanager.client.LibraryManagerUsersClient;
import com.sewerynkamil.librarymanager.dto.UserDto;
import com.sewerynkamil.librarymanager.dto.enumerated.Role;
import com.sewerynkamil.librarymanager.ui.components.ButtonFactory;
import com.sewerynkamil.librarymanager.ui.components.ButtonType;
import com.sewerynkamil.librarymanager.ui.components.ComponentDesigner;
import com.sewerynkamil.librarymanager.ui.utils.StringIntegerConverter;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Author Kamil Seweryn
 */

@SpringComponent
@UIScope
public class RegistrationForm extends FormLayout implements KeyNotifier {
    private ButtonFactory buttonFactory = new ButtonFactory();
    private ComponentDesigner componentDesigner = new ComponentDesigner();
    private LibraryManagerAuthenticationClient authenticationClient;
    private LibraryManagerUsersClient usersClient;
    private UserDto userDto;

    private ChangeHandler changeHandler;

    private TextField name = new TextField("Name");
    private TextField surname = new TextField("Surname");
    private TextField phoneNumber = new TextField("Phone number");
    private EmailField email = new EmailField("E-mail");
    private PasswordField password = new PasswordField("Password");

    private Button save = buttonFactory.createButton(ButtonType.SAVE, "Save", "225px");
    private Button reset = buttonFactory.createButton(ButtonType.RESET, "Reset", "225px");
    private Button close = buttonFactory.createButton(ButtonType.CLOSE, "Close", "225px");

    private Notification userExist = new Notification("User with that e-mail already exist!", 3000);
    private Notification userSaveSuccessful = new Notification("The user has been added succesfully!", 3000);
    private Dialog dialog = new Dialog();

    private Binder<UserDto> binder = new Binder<>();

    @Autowired
    public RegistrationForm(
            LibraryManagerAuthenticationClient authenticationClient,
            LibraryManagerUsersClient usersClient) {
        this.authenticationClient = authenticationClient;
        this.usersClient = usersClient;

        setWidth("260px");
        add(name, surname, email, phoneNumber, password, save, reset, close);
        setVisible(false);

        userExist.addThemeVariants(NotificationVariant.LUMO_ERROR);
        userSaveSuccessful.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

        name.setWidth("225px");
        surname.setWidth("225px");
        email.setWidth("225px");
        phoneNumber.setWidth("225px");
        password.setWidth("225px");

        name.setClearButtonVisible(true);
        surname.setClearButtonVisible(true);
        email.setClearButtonVisible(true);
        phoneNumber.setClearButtonVisible(true);

        binder.forField(name)
                .asRequired("Required field")
                .bind(UserDto::getName, UserDto::setName);
        binder.forField(surname)
                .asRequired("Required field")
                .bind(UserDto::getSurname, UserDto::setSurname);
        binder.forField(email)
                .asRequired("Required field")
                .bind(UserDto::getEmail, UserDto::setEmail);
        binder.forField(phoneNumber)
                .asRequired("Required field")
                .withValidator(number -> number.length() == 9, "Invalid number (9 digits)")
                .withConverter(new StringIntegerConverter())
                .bind(UserDto::getPhoneNumber, UserDto::setPhoneNumber);
        binder.forField(password)
                .asRequired("Required field")
                .bind(UserDto::getPassword, UserDto::setPassword);

        save.setWidth("225px");
        reset.setWidth("225px");
        close.setWidth("225px");

        save.addClickListener(e -> save());
        reset.addClickListener(e -> reset());
        close.addClickListener(e -> close());
    }

    private void save() {
        userDto.setName(name.getValue());
        userDto.setSurname(surname.getValue());
        userDto.setEmail(email.getValue());
        userDto.setPhoneNumber(Integer.parseInt(phoneNumber.getValue()));
        userDto.setPassword(password.getValue());
        userDto.setRole(Role.USER.getRole());

        if(!usersClient.isUserExist(userDto.getEmail())) {
            authenticationClient.registerUser(userDto);
            componentDesigner.setActions(userSaveSuccessful, changeHandler, dialog);
            reset();
        } else {
            userExist.open();
        }
    }

    private void reset() {
        name.clear();
        surname.clear();
        email.clear();
        phoneNumber.clear();
        password.clear();
    }

    private void close() {
        name.clear();
        surname.clear();
        email.clear();
        phoneNumber.clear();
        password.clear();
        dialog.close();
    }

    public final void createUser(UserDto u) {
        dialog.setCloseOnOutsideClick(false);

        userDto = u;

        dialog.add(this);
        dialog.open();

        setVisible(true);
        name.focus();
    }

    public void setChangeHandler(ChangeHandler h) {
        this.changeHandler = h;
    }
}