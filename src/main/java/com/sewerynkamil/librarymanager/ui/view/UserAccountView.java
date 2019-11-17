package com.sewerynkamil.librarymanager.ui.view;

import com.sewerynkamil.librarymanager.client.LibraryManagerClient;
import com.sewerynkamil.librarymanager.dto.UserDto;
import com.sewerynkamil.librarymanager.ui.MainView;
import com.sewerynkamil.librarymanager.ui.utils.LibraryConst;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Author Kamil Seweryn
 */

@Route(value = LibraryConst.ROUTE_MY_ACCOUNT, layout = MainView.class)
@PageTitle(LibraryConst.TITLE_MY_ACCOUNT)
@Secured({"ROLE_User", "ROLE_Admin"})
public class UserAccountView extends VerticalLayout {
    private LibraryManagerClient libraryManagerClient;

    private TextField textField = new TextField("E-mail");

    @Autowired
    public UserAccountView(LibraryManagerClient libraryManagerClient) {
        this.libraryManagerClient = libraryManagerClient;

        UserDto userDto = getCurrentUser(libraryManagerClient);

        textField.setValue(userDto.getEmail());

        add(textField);

    }

    private UserDto getCurrentUser(LibraryManagerClient libraryManagerClient) {
        return libraryManagerClient.getOneUserByEmail(getPrincipalUsername());
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
}