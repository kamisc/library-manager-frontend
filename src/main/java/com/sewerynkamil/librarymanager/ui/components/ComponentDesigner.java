package com.sewerynkamil.librarymanager.ui.components;

import com.sewerynkamil.librarymanager.ui.view.form.ChangeHandler;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.util.List;

/**
 * Author Kamil Seweryn
 */

public class ComponentDesigner {
    public void setActions(Notification notification, ChangeHandler changeHandler, Dialog form) {
        notification.open();
        changeHandler.onChange();
        form.close();
    }

    public void setTextFieldsOptions(TextField... textFields) {
        for(TextField t : textFields) {
            t.setClearButtonVisible(true);
            t.setRequired(true);
            t.setErrorMessage("Required field");
        }
    }

    public void setComboboxOptions(List<String> options, String placeholder, ComboBox comboBox) {
        comboBox.setItems(options);
        comboBox.setPlaceholder(placeholder);
        comboBox.setClearButtonVisible(true);
        comboBox.setRequired(true);
        comboBox.setErrorMessage("Required field");
    }

    public void generateFilter(TextField field, String placeholder) {
        field.setPlaceholder(placeholder);
        field.setValueChangeMode(ValueChangeMode.EAGER);
        field.setClearButtonVisible(true);
    }
}
