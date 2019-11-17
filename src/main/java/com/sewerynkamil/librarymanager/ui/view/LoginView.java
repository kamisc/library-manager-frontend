package com.sewerynkamil.librarymanager.ui.view;

import com.sewerynkamil.librarymanager.client.LibraryManagerClient;
import com.sewerynkamil.librarymanager.dto.RequestJwtDto;
import com.sewerynkamil.librarymanager.dto.UserDto;
import com.sewerynkamil.librarymanager.ui.components.ButtonFactory;
import com.sewerynkamil.librarymanager.ui.components.ButtonType;
import com.sewerynkamil.librarymanager.ui.utils.LibraryConst;
import com.sewerynkamil.librarymanager.ui.view.form.RegistrationForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.login.AbstractLogin;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Author Kamil Seweryn
 */

@Route(value = LibraryConst.ROUTE_LOGIN)
@PageTitle(LibraryConst.TITLE_LOGIN)
@StyleSheet("css/shared-styles.css")
public class LoginView extends VerticalLayout {
    private ButtonFactory buttonFactory = new ButtonFactory();

    private LoginI18n loginI18n = LoginI18n.createDefault();
    private LoginForm loginForm = new LoginForm();

    private VerticalLayout loginLayout = new VerticalLayout();
    private VerticalLayout loginInformation = new VerticalLayout();

    private HorizontalLayout login = new HorizontalLayout();

    private Label title = new Label(LibraryConst.TITLE_LOGIN);
    private Label author = new Label("created by Kamil Seweryn");

    private Button createAccountButton = buttonFactory.createButton(ButtonType.ADDBUTTON, "Create new account", "237.667px");

    private RegistrationForm registrationForm;

    private static String jwttoken = "";

    @Autowired
    public LoginView(
            AuthenticationManager authenticationManager,
            LibraryManagerClient libraryManagerClient,
            RegistrationForm registrationForm) {
        this.registrationForm = registrationForm;

        setClassName("login");

        loginI18n.getForm().setUsername("E-mail");

        loginForm.setForgotPasswordButtonVisible(false);
        loginForm.setI18n(loginI18n);

        loginLayout.setClassName("login-login-layout");
        loginLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        loginLayout.add(loginForm, createAccountButton);

        loginInformation.setClassName("login-login-information");
        loginInformation.setAlignItems(Alignment.CENTER);
        loginInformation.add(title, author);

        login.setClassName("login-login");
        login.add(loginInformation, loginLayout);

        title.setClassName("login-title");
        author.setClassName("login-author");

        add(login, registrationForm);

        generateLoginListener(authenticationManager, libraryManagerClient);

        registrationForm.setChangeHandler(() -> {
            registrationForm.setVisible(false);
        });

        createAccountButton.addClickListener(e -> registrationForm.createUser(new UserDto()));
    }

    private void generateLoginListener(AuthenticationManager authenticationManager, LibraryManagerClient libraryManagerClient) {
        loginForm.addLoginListener(e -> {
            try {
                final Authentication authentication = authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(e.getUsername(), e.getPassword()));

                createAuthenticationToken(libraryManagerClient, e.getUsername(), e.getPassword());

                if(authentication != null) {
                    libraryManagerClient.setJwttoken("Bearer " + jwttoken.substring(13, jwttoken.length()-2));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    getUI().get().navigate(LibraryConst.ROUTE_BOOKS);
                }
            } catch (AuthenticationException ex) {
                loginForm.setError(true);
            }
        });
    }

    private void createAuthenticationToken(LibraryManagerClient libraryManagerClient, String username, String password) {
        if(jwttoken != null) {
            jwttoken = "";
            jwttoken = jwttoken + libraryManagerClient.createAuthenticationToken(
                    new RequestJwtDto(username, password));
        }
    }
}