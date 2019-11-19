package com.sewerynkamil.librarymanager.ui.view.form;

import com.sewerynkamil.librarymanager.client.LibraryManagerClient;
import com.sewerynkamil.librarymanager.dto.UserDto;
import com.sewerynkamil.librarymanager.ui.components.ButtonFactory;
import com.sewerynkamil.librarymanager.ui.components.ButtonType;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
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
    private LibraryManagerClient client;
    private UserDto userDto;

    private ChangeHandler changeHandler;

    private TextField name = new TextField("Name");
    private TextField surname = new TextField("Surname");
    private EmailField email = new EmailField("E-mail");
    private NumberField phoneNumber = new NumberField("Phone number");
    private PasswordField password = new PasswordField("Password");

    private Button save = buttonFactory.createButton(ButtonType.SAVE, "Save", "225px");
    private Button reset = buttonFactory.createButton(ButtonType.RESET, "Reset", "225px");
    private Button close = buttonFactory.createButton(ButtonType.CLOSE, "Close", "225px");

    private Notification userExist = new Notification("User with that e-mail already exist!", 3000);
    private Notification userSaveSuccessful = new Notification("The user has been added succesfully!", 3000);
    private Dialog dialog = new Dialog();

    @Autowired
    public RegistrationForm(LibraryManagerClient client) {
        this.client = client;

        userExist.addThemeVariants(NotificationVariant.LUMO_ERROR);
        userSaveSuccessful.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

        setWidth("260px");

        name.setWidth("225px");
        surname.setWidth("225px");
        email.setWidth("225px");
        phoneNumber.setWidth("225px");
        password.setWidth("225px");

        name.setRequired(true);
        surname.setRequired(true);
        email.setRequiredIndicatorVisible(true);
        phoneNumber.setRequiredIndicatorVisible(true);
        password.setRequired(true);

        name.setClearButtonVisible(true);
        surname.setClearButtonVisible(true);
        email.setClearButtonVisible(true);
        phoneNumber.setClearButtonVisible(true);

        save.setWidth("225px");
        reset.setWidth("225px");
        close.setWidth("225px");

        add(name, surname, email, phoneNumber, password, save, reset, close);

        save.addClickListener(e -> save());
        reset.addClickListener(e -> reset());
        close.addClickListener(e -> close());

        setVisible(false);
    }

    private void save() {
        userDto.setName(name.getValue());
        userDto.setSurname(surname.getValue());
        userDto.setEmail(email.getValue());
        userDto.setPhoneNumber(phoneNumber.getValue().intValue());
        userDto.setPassword(password.getValue());

        if(!client.isUserExist(userDto.getEmail())) {
            client.registerUser(userDto);
            userSaveSuccessful.open();
            changeHandler.onChange();
            dialog.close();
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

        if (u == null) {
            setVisible(false);
            return;
        }

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