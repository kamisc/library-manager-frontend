package com.sewerynkamil.librarymanager.ui.view;

import com.sewerynkamil.librarymanager.ui.utils.LibraryConst;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.login.LoginOverlay;
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
public class LoginView extends VerticalLayout {
    private LoginOverlay login = new LoginOverlay();

    @Autowired
    public LoginView(AuthenticationManager authenticationManager) {
        login.setOpened(true);
        login.setTitle(LibraryConst.TITLE_LOGIN);
        login.setDescription("created by Kamil Seweryn");

        add(login);

        login.addLoginListener(e -> {
            try {
                final Authentication authentication = authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(e.getUsername(), e.getPassword()));

                if(authentication != null) {
                    login.close();
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    getUI().get().navigate("test");
                }
            } catch (AuthenticationException ex) {
                login.setError(true);
            }
        });
    }
}