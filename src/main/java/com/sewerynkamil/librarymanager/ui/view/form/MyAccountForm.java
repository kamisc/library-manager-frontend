package com.sewerynkamil.librarymanager.ui.view.form;

import com.sewerynkamil.librarymanager.client.LibraryManagerClient;
import com.sewerynkamil.librarymanager.dto.UserDto;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Author Kamil Seweryn
 */

@SpringComponent
@UIScope
@Secured({"ROLE_User", "ROLE_Admin"})
public class MyAccountForm extends FormLayout implements KeyNotifier {
    private ButtonFactory buttonFactory = new ButtonFactory();
    private ComponentDesigner componentDesigner = new ComponentDesigner();
    private LibraryManagerClient client;
    private UserDto userDto;

    private ChangeHandler changeHandler;

    private TextField name = new TextField("Name");
    private TextField surname = new TextField("Surname");
    private TextField phoneNumber = new TextField("Phone number");
    private EmailField email = new EmailField("E-mail");
    private PasswordField password = new PasswordField("Password");

    private Button update = buttonFactory.createButton(ButtonType.UPDATE, "Update", "225px");
    private Button reset = buttonFactory.createButton(ButtonType.RESET, "Reset", "225px");
    private Button close = buttonFactory.createButton(ButtonType.CLOSE, "Close", "225px");

    private Notification userUpdateSuccessful = new Notification("You update your data succesfully!", 3000);
    private Notification passwordError = new Notification("Password field can't be empty!", 3000);
    private Dialog dialog = new Dialog();

    private Binder<UserDto> binder = new Binder<>(UserDto.class);

    @Autowired
    public MyAccountForm(LibraryManagerClient client) {
        this.client = client;

        setSizeUndefined();
        setWidth("260px");
        add(name, surname, email, phoneNumber, password, update, reset, close);
        setVisible(false);

        userUpdateSuccessful.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        passwordError.addThemeVariants(NotificationVariant.LUMO_ERROR);

        email.setReadOnly(true);
        password.setRequired(true);
        password.setErrorMessage("Write old password or new password to confirm");

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

        update.addClickListener(e -> update());
        reset.addClickListener(e -> editUser(userDto));
        close.addClickListener(e -> dialog.close());
    }

    public void update() {
        if(StringUtils.isBlank(password.getValue())) {
            passwordError.open();
        } else {
            userDto.setPassword(passwordEncoder().encode(password.getValue()));
            client.updateUser(userDto);
            componentDesigner.setActions(userUpdateSuccessful, changeHandler, dialog);
        }
    }

    public void editUser(UserDto u) {
        dialog.setCloseOnOutsideClick(false);

        userDto = client.getOneUserById(u.getId());
        password.clear();

        dialog.add(this);
        dialog.open();

        binder.setBean(userDto);
        setVisible(true);
        name.focus();
    }

    public void setChangeHandler(ChangeHandler h) {
        this.changeHandler = h;
    }

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}