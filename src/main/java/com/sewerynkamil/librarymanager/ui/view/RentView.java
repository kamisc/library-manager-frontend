package com.sewerynkamil.librarymanager.ui.view;

import com.sewerynkamil.librarymanager.client.LibraryManagerRentsClient;
import com.sewerynkamil.librarymanager.dto.RentDto;
import com.sewerynkamil.librarymanager.ui.MainView;
import com.sewerynkamil.librarymanager.ui.components.ComponentDesigner;
import com.sewerynkamil.librarymanager.ui.utils.LibraryConst;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

/**
 * Author Kamil Seweryn
 */

@Route(value = LibraryConst.ROUTE_RENTS, layout = MainView.class)
@PageTitle(LibraryConst.TITLE_RENTS)
@Secured("ROLE_Admin")
public class RentView extends VerticalLayout {
    private ComponentDesigner componentDesigner = new ComponentDesigner();
    private LibraryManagerRentsClient rentsClient;

    private Grid<RentDto> grid = new Grid<>(RentDto.class);

    private TextField bookTitleFiter = new TextField();
    private TextField emailFilter = new TextField();

    private HeaderRow filterRow = grid.appendHeaderRow();

    @Autowired
    public RentView(LibraryManagerRentsClient rentsClient) {
        this.rentsClient = rentsClient;

        setSizeFull();
        add(grid);
        rentList();

        grid.addThemeVariants(GridVariant.LUMO_COMPACT, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        grid.setColumns("rentId", "specimenId", "bookTitle", "userEmail", "rentDate", "returnDate");
        grid.getColumnByKey("rentId").setTextAlign(ColumnTextAlign.START);
        grid.addComponentColumn(rentDto -> createProlongationButton(rentDto));

        componentDesigner.generateFilter(bookTitleFiter, "Filter by book title");
        bookTitleFiter.addValueChangeListener(e -> {
                    if (StringUtils.isBlank(e.getValue())) {
                        rentList();
                    } else {
                        grid.setItems(rentsClient.getAllRentsByBookTitleStartsWithIgnoreCase(e.getValue().toLowerCase()));
                    }
                }
        );

        componentDesigner.generateFilter(emailFilter, "Filter by user email");
        emailFilter.addValueChangeListener(e -> {
                    if (StringUtils.isBlank(e.getValue())) {
                        rentList();
                    } else {
                        grid.setItems(rentsClient.getAllRentsByUserEmailStartsWithIgnoreCase(e.getValue().toLowerCase()));
                    }
                }
        );

        filterRow.getCell(grid.getColumnByKey("bookTitle")).setComponent(bookTitleFiter);
        filterRow.getCell(grid.getColumnByKey("userEmail")).setComponent(emailFilter);
    }

    private void rentList() {
        grid.setDataProvider(DataProvider.fromFilteringCallbacks(
                query -> {
                    int offset = query.getOffset();
                    int limit = query.getLimit();
                    query.getFilter();
                    return rentsClient.getAllRentsWithLazyLoading(offset, limit).stream();
                },
                query -> rentsClient.countRents().intValue()
        ));
    }

    private Button createProlongationButton(RentDto rentDto) {
        Button button = new Button("Return book", clickEvent -> {
            rentsClient.returnBook(rentDto.getRentId());
            rentList();
        });
        button.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
        return button;
    }
}