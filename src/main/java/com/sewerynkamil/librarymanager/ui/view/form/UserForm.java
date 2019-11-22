package com.sewerynkamil.librarymanager.ui.view.form;

import com.sewerynkamil.librarymanager.client.LibraryManagerClient;
import com.sewerynkamil.librarymanager.dto.BookDto;
import com.sewerynkamil.librarymanager.dto.UserDto;
import com.sewerynkamil.librarymanager.dto.enumerated.Category;
import com.sewerynkamil.librarymanager.dto.enumerated.Role;
import com.sewerynkamil.librarymanager.ui.components.ButtonFactory;
import com.sewerynkamil.librarymanager.ui.components.ButtonType;
import com.sewerynkamil.librarymanager.utils.StringIntegerConverter;
import com.sewerynkamil.librarymanager.utils.StringLongConverter;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import java.time.LocalDate;

/**
 * Author Kamil Seweryn
 */

@SpringComponent
@UIScope
@Secured("ROLE_Admin")
public class UserForm extends FormLayout implements KeyNotifier, FormActions {
    private ButtonFactory buttonFactory = new ButtonFactory();
    private LibraryManagerClient client;
    private UserDto userDto;

    private ChangeHandler changeHandler;

    private TextField name = new TextField("Name");
    private TextField surname = new TextField("Surname");
    private EmailField email = new EmailField("E-mail");
    private TextField phoneNumber = new TextField("Phone number");
    private PasswordField password = new PasswordField("Password");
    private ComboBox<String> role = new ComboBox<>("Role");

    private Button save = buttonFactory.createButton(ButtonType.SAVE, "Save", "225px");
    private Button update = buttonFactory.createButton(ButtonType.UPDATE, "Update", "225px");
    private Button reset = buttonFactory.createButton(ButtonType.RESET, "Reset", "225px");
    private Button delete = buttonFactory.createButton(ButtonType.DELETE, "Delete", "225px");
    private Button close = buttonFactory.createButton(ButtonType.CLOSE, "Close", "225px");

    private Notification userExist = new Notification("This user exist in the base!", 3000);
    private Notification userSaveSuccessful = new Notification("The user has been added succesfully!", 3000);
    private Notification userUpdateSuccessful = new Notification("The user has been updated succesfully!", 3000);
    private Notification userDeleteSuccessful = new Notification("The user has been deleted succesfully!", 3000);
    private Dialog dialog = new Dialog();

    private Binder<UserDto> binder = new Binder<>(UserDto.class);

    private String oldEmail;

    @Autowired
    public UserForm(LibraryManagerClient client) {
        this.client = client;

        userExist.addThemeVariants(NotificationVariant.LUMO_ERROR);
        userSaveSuccessful.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        userUpdateSuccessful.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        userDeleteSuccessful.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

        role.setItems(Role.roleList());
        role.setPlaceholder("Select role");
        role.setClearButtonVisible(true);
        role.setRequired(true);
        role.setErrorMessage("Required field");

        setSizeUndefined();

        setWidth("260px");

        add(name, surname, email, phoneNumber, password, role, save, update, reset, delete, close);

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
        binder.forField(role)
                .asRequired("Required field")
                .bind(UserDto::getRole, UserDto::setRole);

        save.addClickListener(e -> save());
        update.addClickListener(e -> update());
        delete.addClickListener(e -> delete());
        reset.addClickListener(e -> editUser(userDto));
        close.addClickListener(e -> dialog.close());

        setVisible(false);
    }

    @Override
    public void save() {
        if(!client.isUserExist(email.getValue())) {
            client.saveNewUser(userDto);
            setActions(userSaveSuccessful, changeHandler, dialog);
        } else {
            userExist.open();
        }
    }

    @Override
    public void update() {
        if(!email.getValue().equals(oldEmail) && client.isUserExist(email.getValue())) {
            userExist.open();
        } else {
            client.updateUser(userDto);
            setActions(userUpdateSuccessful, changeHandler, dialog);
        }
    }

    @Override
    public void delete() {
        client.deleteUser(userDto.getId());
        setActions(userDeleteSuccessful, changeHandler, dialog);
    }

    public void editUser(UserDto u) {
        dialog.setCloseOnOutsideClick(false);

        if (u == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = u.getId() != null;
        if(persisted) {
            userDto = client.getOneUserById(u.getId());

            oldEmail = userDto.getEmail();

            dialog.add(this);
            dialog.open();
        } else {
            userDto = u;
            dialog.add(this);
            dialog.open();
        }

        password.setVisible(!persisted);
        save.setVisible(!persisted);
        update.setVisible(persisted);
        reset.setVisible(persisted);
        delete.setVisible(persisted);
        binder.setBean(userDto);
        setVisible(true);
        name.focus();
    }

    public void setChangeHandler(ChangeHandler h) {
        this.changeHandler = h;
    }

    private void setActions(Notification success, ChangeHandler changeHandler, Dialog form) {
        success.open();
        changeHandler.onChange();
        form.close();
    }

    private void setTextFieldsOptions(TextField... textFields) {
        for(TextField t : textFields) {
            t.setClearButtonVisible(true);
            t.setRequired(true);
            t.setErrorMessage("Required field");
        }
    }
}
