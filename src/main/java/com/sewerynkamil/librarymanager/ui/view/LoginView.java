package com.sewerynkamil.librarymanager.ui.view;

import com.sewerynkamil.librarymanager.client.LibraryManagerClient;
import com.sewerynkamil.librarymanager.dto.RequestJwtDto;
import com.sewerynkamil.librarymanager.ui.components.ButtonFactory;
import com.sewerynkamil.librarymanager.ui.components.ButtonType;
import com.sewerynkamil.librarymanager.ui.utils.LibraryConst;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Label;
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

@Tag("sa-login-view")
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

    @Autowired
    private LibraryManagerClient libraryManagerClient;

    private static String jwttoken = "";

    @Autowired
    public LoginView(AuthenticationManager authenticationManager) {
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

        add(login);

        loginForm.addLoginListener(e -> {
            try {
                final Authentication authentication = authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(e.getUsername(), e.getPassword()));

                jwttoken = jwttoken + libraryManagerClient.createAuthenticationToken(
                        new RequestJwtDto(e.getUsername(), e.getPassword()));

                if(authentication != null) {
                    libraryManagerClient.setJwttoken("Bearer " + jwttoken.substring(13, jwttoken.length()-2));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    getUI().get().navigate("");
                }
            } catch (AuthenticationException ex) {
                loginForm.setError(true);
            }
        });
    }

}