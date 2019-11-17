package com.sewerynkamil.librarymanager.ui;

import com.sewerynkamil.librarymanager.ui.utils.LibraryConst;
import com.sewerynkamil.librarymanager.ui.view.AudiobookView;
import com.sewerynkamil.librarymanager.ui.view.BookView;
import com.sewerynkamil.librarymanager.ui.view.UserAccountView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.VaadinServlet;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import java.util.ArrayList;
import java.util.List;

/**
 * Author Kamil Seweryn
 */

@Viewport(LibraryConst.VIEWPORT)
@StyleSheet("css/shared-styles.css")
@Theme(value = Lumo.class, variant = Lumo.LIGHT)
@PWA(name = "Library Manager created by Kamil Seweryn", shortName = "Library Manager")
public class MainView extends AppLayout {
    private final Tabs menu;

    public MainView() {
        Span appName = new Span(LibraryConst.TITLE_LOGIN);
        appName.setClassName("span");
        menu = createMenuTabs();
        menu.setFlexGrowForEnclosedTabs(1);
        menu.setClassName("nav-bar");

        this.addToNavbar(appName);
        this.addToNavbar(menu);
    }

    private static Tabs createMenuTabs() {
        final Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.HORIZONTAL);
        tabs.add(getAvailableTabs());
        return tabs;
    }

    private static Tab[] getAvailableTabs() {
        final List<Tab> tabs = new ArrayList<>();

        tabs.add(createTab(VaadinIcon.OPEN_BOOK, LibraryConst.TITLE_BOOKS, BookView.class));
        tabs.add(createTab(VaadinIcon.MUSIC, LibraryConst.TITLE_AUDIOBOOKS, AudiobookView.class));
        tabs.add(createTab(VaadinIcon.USER_CARD, LibraryConst.TITLE_MY_ACCOUNT, UserAccountView.class));

        final String contextPath = VaadinServlet.getCurrent().getServletContext().getContextPath();
        final Tab logoutTab = createTab(createLogoutLink(contextPath));
        tabs.add(logoutTab);

        return tabs.toArray(new Tab[tabs.size()]);
    }

    private static Tab createTab(VaadinIcon icon, String title, Class<? extends Component> viewClass) {
        return createTab(populateLink(new RouterLink(null, viewClass), icon, title));
    }

    private static Tab createTab(Component content) {
        final Tab tab = new Tab();
        tab.addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);
        tab.add(content);
        return tab;
    }

    private static <T extends HasComponents> T populateLink(T a, VaadinIcon icon, String title) {
        a.add(icon.create());
        a.add(title);
        return a;
    }

    private static Anchor createLogoutLink(String contextPath) {
        final Anchor a = populateLink(new Anchor(), VaadinIcon.ARROW_LEFT, LibraryConst.TITLE_LOGOUT);
        a.setHref(contextPath + "/" + LibraryConst.ROUTE_LOGIN);
        return a;
    }
}