package com.sewerynkamil.librarymanager.ui.view.form;

import com.sewerynkamil.librarymanager.client.LibraryManagerClient;
import com.sewerynkamil.librarymanager.dto.SpecimenDto;
import com.sewerynkamil.librarymanager.ui.components.ButtonFactory;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.security.access.annotation.Secured;

/**
 * Author Kamil Seweryn
 */

@SpringComponent
@UIScope
@Secured("ROLE_Admin")
public class SpecimenForm extends FormLayout implements KeyNotifier, FormActions {
    private ButtonFactory buttonFactory = new ButtonFactory();
    private LibraryManagerClient client;
    private SpecimenDto specimenDto;



    @Override
    public void save() {

    }

    @Override
    public void update() {

    }

    @Override
    public void delete() {

    }
}
