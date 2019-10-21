package com.kodilla.auxilium_front.webside;

import com.kodilla.auxilium_front.clients.AuxiliumClient;
import com.kodilla.auxilium_front.domain.ServicesTypes;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Objects;

@Route
public class Main extends VerticalLayout implements HasUrlParameter<String>{

    private final AuxiliumClient auxiliumClient;
    private String uuid;
    private HorizontalLayout topMenu = new HorizontalLayout();
    private HorizontalLayout loginButtons = new HorizontalLayout();
    private HorizontalLayout logo = new HorizontalLayout();
    private HorizontalLayout selection = new HorizontalLayout();
    private VerticalLayout leftMenu = new VerticalLayout();
    private FlexLayout pictures = new FlexLayout();
    @Autowired
    private AddServiceFormDialog addServiceFormDialog;
    private AddServiceCCDialog addServiceCCDialog = new AddServiceCCDialog();


    //Images
    private Image womanInGarden = new Image( "http://bz.home.pl/ania/Auxilium/Woman_in_garden2.jpg", "Woman in garden");
    private Image oldManInHat = new Image( "http://bz.home.pl/ania/Auxilium/Old_man_in_hat1.jpg", "Old man in hat");
    private Image oldManUnderground = new Image( "http://bz.home.pl/ania/Auxilium/Old_man_underground3.jpg", "Old man underground");
    private Image ladyGoingDownstairs = new Image( "http://bz.home.pl/ania/Auxilium/Lady_going_downstairs4.jpg", "Lady going downstairs");
    private Image auxiliumLogo = new Image( "http://bz.home.pl/ania/Auxilium/logo.png", "Auxilium logo");
    private Image heart = new Image( "http://bz.home.pl/ania/Auxilium/heart2.jpg", "heart");
    private Image hands = new Image( "http://bz.home.pl/ania/Auxilium/hands.jpeg", "hands");
    private Image manOutside = new Image( "http://bz.home.pl/ania/Auxilium/Man_outside2.jpg", "Man outside");

    //Buttons
    private Button findButton = new Button("Szukaj");
    private Button createAccountButton = new Button("Załóż konto");
    private Button loginButton = new Button("Zaloguj");
    private Button logoButton = new Button();
    private Button logoutButton = new Button("Wyloguj");

    //Select
    private Select<String> citySelect = new Select<>();
    private Select<String> serviceSelect = new Select<>();

    //Labels
    private Label yourPointsTextLabel = new Label("Twoje punkty ");
    private Label yourPointsLabel = new Label();

    private MenuBar menuBar = new MenuBar();

    public Main(AuxiliumClient auxiliumClient) {
        this.auxiliumClient = auxiliumClient;

        //Top menu
        topMenu.setWidth("100%");
        topMenu.getStyle().set("border-bottom", "3px solid #FDDA24");
        topMenu.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        topMenu.setDefaultVerticalComponentAlignment(
                FlexComponent.Alignment.CENTER);
        add(topMenu);
        add(pictures);

        topMenu.addComponentAsFirst(logo);
        topMenu.add(selection);
        topMenu.add(loginButtons);

        //Select
        citySelect.setRequiredIndicatorVisible(true);
        citySelect.setLabel("Miasto");
        citySelect.setItems(auxiliumClient.getCities());
        citySelect.setPlaceholder("Wybierz miasto");
        citySelect.setEmptySelectionCaption("Wybierz miasto");
        citySelect.setEmptySelectionAllowed(true);
        citySelect.setItemEnabledProvider(Objects::nonNull);

        serviceSelect.setRequiredIndicatorVisible(true);
        serviceSelect.setLabel("Rodzaj pomocy");
        serviceSelect.setItems(Arrays.stream(ServicesTypes.values()).map(s -> s.getValue()));
        serviceSelect.setPlaceholder("Wybierz rodzaj");
        serviceSelect.setEmptySelectionCaption("Wybierz rodzaj pomocy");
        serviceSelect.setEmptySelectionAllowed(true);
        serviceSelect.setItemEnabledProvider(Objects::nonNull);

        selection.add(citySelect);
        selection.add(serviceSelect);
        selection.add(findButton);
        selection.setHeight("105px");
        selection.setAlignItems(Alignment.CENTER);
        selection.getStyle().set("margin-right", "7%");
        findButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_TERTIARY_INLINE);
        findButton.getElement().getStyle().set("color", "#007481");
        findButton.getStyle().set("font-size", "20px");
        findButton.addClickListener(e -> {
            String location;
            String selectedCity = citySelect.getValue();
            String selectedService = serviceSelect.getValue();
            if( selectedCity != null && selectedService != null ){
                location = "list/" + selectedService + "&" + selectedCity + "&" + uuid;
                findButton.getUI().ifPresent(ui ->
                        ui.navigate(location));
            } else if (selectedCity == null && selectedService != null){
                location = "list/" + selectedService + "&null" + "&" + uuid;
                findButton.getUI().ifPresent(ui ->
                        ui.navigate(location));
            } else if (selectedCity != null && selectedService == null) {
                location = "list/null&" + selectedCity + "&" + uuid;
                findButton.getUI().ifPresent(ui ->
                        ui.navigate(location));
            }
        });


        //Login&CreateAccount
        loginButtons.addComponentAsFirst(loginButton);
        loginButtons.addComponentAsFirst(createAccountButton);
        loginButtons.setHeight("105px");
        loginButtons.setAlignItems(Alignment.CENTER);
        loginButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_TERTIARY);
        createAccountButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_TERTIARY);
        loginButton.getElement().getStyle().set("color", "#007481");
        loginButton.getStyle().set("font-size", "20px");
        createAccountButton.getElement().getStyle().set("color", "#007481");
        createAccountButton.getStyle().set("font-size", "20px");

        logoutButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_TERTIARY);
        logoutButton.getElement().getStyle().set("color", "#007481");
        logoutButton.getStyle().set("font-size", "20px");
        logoutButton.addClickListener(e -> {
            logoutButton.getUI().ifPresent((ui ->
                    ui.navigate("")));
            loginButtons.add(createAccountButton, loginButton);
            logoutButton.getElement().removeFromParent();
            leftMenu.getElement().removeFromParent();
        });

        loginButton.addClickListener(e -> {
            loginButton.getUI().ifPresent((ui ->
                    ui.navigate("login")));
        });

        createAccountButton.addClickListener(e -> {
            createAccountButton.getUI().ifPresent((ui ->
                    ui.navigate("registration")));
        });

        //Logo
        logoButton.setIcon(auxiliumLogo);
        logo.add(logoButton);
        auxiliumLogo.setHeight("100px");
        logo.getStyle().set("margin-right", "7%");
        logoButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_TERTIARY);
        logoButton.addClickListener(e -> {
            leftMenu.getElement().removeFromParent();
            String location = "" + uuid;
            logoButton.getUI().ifPresent(ui ->
                    ui.navigate(location));
        });

        //Pictures
        pictures.add(oldManInHat, manOutside, heart, womanInGarden, ladyGoingDownstairs, oldManUnderground, hands);
        pictures.setWrapMode(FlexLayout.WrapMode.WRAP);
        womanInGarden.setHeight("330px");
        womanInGarden.getStyle().set("padding", "10px");
        oldManInHat.setHeight("330px");
        oldManInHat.getStyle().set("padding", "10px");
        oldManUnderground.setHeight("330px");
        oldManUnderground.getStyle().set("padding", "10px");
        ladyGoingDownstairs.setHeight("330px");
        ladyGoingDownstairs.getStyle().set("padding", "10px");
        heart.setHeight("330px");
        heart.getStyle().set("padding", "10px");
        hands.setHeight("330px");
        hands.getStyle().set("padding", "10px");
        manOutside.setHeight("330px");
        manOutside.getStyle().set("padding", "10px");

    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        if(parameter != null){
            addMenuForLoggedUsers();
            changeLoginButtonToLogout();
            yourPointsLabel.setText(auxiliumClient.getUserAvailablePoints(parameter).toString());
            uuid = parameter;
        }
    }

    private void addMenuForLoggedUsers(){
        pictures.addComponentAsFirst(leftMenu);
        leftMenu.setMaxWidth("433px");
        leftMenu.setMaxHeight("330px");
        leftMenu.getStyle().set("background-color", "rgb(253, 218, 36, 0.3)");
        leftMenu.getStyle().set("margin", "10px 10px 10px 10px");
        leftMenu.getStyle().set("border-radius", "15px");
        leftMenu.getStyle().set("padding", "10px");

        leftMenu.add(yourPointsTextLabel, yourPointsLabel);
        leftMenu.setAlignItems(Alignment.CENTER);
        yourPointsTextLabel.getStyle().set("font-size", "30px");
        yourPointsLabel.getStyle().set("font-size", "40px");
        yourPointsLabel.getStyle().set("font-weight", "bold");
        yourPointsLabel.getStyle().set("margin-top", "0px");

        leftMenu.add(menuBar);
        menuBar.setOpenOnHover(true);
        menuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY);
        menuBar.removeAll();
        MenuItem help = menuBar.addItem("Twoja Pomoc");
        MenuItem prizes = menuBar.addItem("Nagrody");
        MenuItem myAccount = menuBar.addItem("Ustawienia");

        help.getElement().getStyle().set("color", "#007481");
        help.getElement().getStyle().set("font-size", "20px");
        prizes.getElement().getStyle().set("color", "#007481");
        prizes.getElement().getStyle().set("font-size", "20px");
        myAccount.getElement().getStyle().set("color", "#007481");
        myAccount.getElement().getStyle().set("font-size", "20px");

        help.addComponentAsFirst(new Icon(VaadinIcon.HANDSHAKE));
        prizes.addComponentAsFirst(new Icon(VaadinIcon.GIFT));
        myAccount.addComponentAsFirst(new Icon(VaadinIcon.EDIT));

        help.getSubMenu().addItem("Zgłoszone prośby", e -> { getUI().ifPresent((ui -> ui.navigate("myServices/" + uuid))); });
        help.getSubMenu().addItem("Dodaj prośbę - infolinia", e -> addServiceCCDialog.openDialog() );
        help.getSubMenu().addItem("Dodaj prośbę - formularz", e -> {
            addServiceFormDialog.openDialog();
            addServiceFormDialog.setUuid(uuid);
        });
        help.getSubMenu().addItem("Pomogłeś już", e -> { getUI().ifPresent((ui -> ui.navigate("servicesAssigned/" + uuid)));});
        help.getSubMenu().getChildren().forEach(e ->e.getElement().getStyle().set("color", "#007481"));
        help.getSubMenu().getChildren().forEach(e ->e.getElement().getStyle().set("background-color", "rgb(253, 218, 36, 0.3)"));

        prizes.getSubMenu().addItem("Bilety", e -> { getUI().ifPresent((ui -> ui.navigate("events/" + uuid))); });
        prizes.getSubMenu().addItem("Prezenty", e -> { getUI().ifPresent((ui -> ui.navigate("products/" + uuid))); });
        prizes.getSubMenu().getChildren().forEach(e ->e.getElement().getStyle().set("color", "#007481"));
        prizes.getSubMenu().getChildren().forEach(e ->e.getElement().getStyle().set("background-color", "rgb(253, 218, 36, 0.3)"));

        myAccount.addClickListener(e -> { myAccount.getUI().ifPresent(ui -> ui.navigate("myAccount/" + uuid)); });

    }

    private void changeLoginButtonToLogout(){
        loginButton.getElement().removeFromParent();
        createAccountButton.getElement().removeFromParent();
        loginButtons.add(logoutButton);
    }


}