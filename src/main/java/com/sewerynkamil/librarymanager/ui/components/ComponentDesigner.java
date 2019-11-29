package com.sewerynkamil.librarymanager.ui.components;

import com.sewerynkamil.librarymanager.ui.view.form.ChangeHandler;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

/**
 * Author Kamil Seweryn
 */

public class ComponentDesigner {
    public void setActions(Notification success, ChangeHandler changeHandler, Dialog form) {
        success.open();
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

    public void generateFilter(TextField field, String placeholder) {
        field.setPlaceholder(placeholder);
        field.setValueChangeMode(ValueChangeMode.EAGER);
        field.setClearButtonVisible(true);
    }
}
