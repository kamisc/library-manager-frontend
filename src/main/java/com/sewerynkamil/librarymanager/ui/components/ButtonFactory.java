package com.sewerynkamil.librarymanager.ui.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;

/**
 * Author Kamil Seweryn
 */

public class ButtonFactory {
    private Button button;

    public final Button createButton(ButtonType type, String buttonName, String width) {
        switch (type) {
            case ADDBUTTON:
                button = new Button(buttonName, VaadinIcon.PLUS.create());
                button.setWidth(width);
                return button;
            case SAVE:
            case UPDATE:
                button = new Button(buttonName, VaadinIcon.CHECK.create());
                button.setWidth(width);
                button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                return button;
            case DELETE:
                Button deleteButton = new Button(buttonName, VaadinIcon.TRASH.create());
                deleteButton.setWidth(width);
                deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
                return deleteButton;
            case RESET:
                Button resetButton = new Button(buttonName, VaadinIcon.ROTATE_LEFT.create());
                resetButton.setWidth(width);
                resetButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);
                return resetButton;
            case CLOSE:
                Button closeButton = new Button(buttonName, VaadinIcon.CLOSE.create());
                closeButton.setWidth(width);
                return closeButton;
            default:
                return null;
        }
    }
}