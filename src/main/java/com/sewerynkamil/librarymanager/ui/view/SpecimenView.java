package com.sewerynkamil.librarymanager.ui.view;

import com.sewerynkamil.librarymanager.client.LibraryManagerClient;
import com.sewerynkamil.librarymanager.dto.SpecimenDto;
import com.sewerynkamil.librarymanager.ui.components.ButtonFactory;
import com.sewerynkamil.librarymanager.ui.components.ButtonType;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

/**
 * Author Kamil Seweryn
 */

@SpringComponent
@UIScope
@Secured({"ROLE_User", "ROLE_Admin"})
public class SpecimenView  extends FormLayout implements KeyNotifier {
    private LibraryManagerClient client;
    private ButtonFactory buttonFactory = new ButtonFactory();

    private Grid<SpecimenDto> grid = new Grid<>(SpecimenDto.class);
    private VerticalLayout elements = new VerticalLayout();

    private Button close = buttonFactory.createButton(ButtonType.CLOSE, "Close", "820px");

    private Dialog dialog = new Dialog();

    @Autowired
    public SpecimenView(LibraryManagerClient client) {
        this.client = client;

        setWidth("850px");


        grid.setWidth("820px");
        elements.add(grid, close);

        add(elements);

        close.addClickListener(e -> dialog.close());

        setVisible(false);
    }

    public void showSpecimens() {
        dialog.add(this);
        dialog.open();
        setVisible(true);
    }
}
