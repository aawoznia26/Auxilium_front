package com.kodilla.auxilium_front.webside;

import com.kodilla.auxilium_front.clients.AuciliumClient;
import com.kodilla.auxilium_front.domain.ServicesTypes;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;

import java.util.*;

@Route
public class Main extends VerticalLayout implements HasUrlParameter<String>{

    private final AuciliumClient auciliumClient;
    private String uuid;
    private HorizontalLayout topMenu = new HorizontalLayout();
    private HorizontalLayout loginButtons = new HorizontalLayout();
    private HorizontalLayout logo = new HorizontalLayout();
    private HorizontalLayout selection = new HorizontalLayout();
    private HorizontalLayout split = new HorizontalLayout();
    private VerticalLayout leftMenu = new VerticalLayout();
    private FlexLayout pictures = new FlexLayout();
    private VerticalLayout addService = new VerticalLayout();
    private HorizontalLayout addServiceButtons = new HorizontalLayout();

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
    private Button exchangePointsButton = new Button("Odbierz nagrodę");
    private Button addServiceManualyButton = new Button("Formularz");
    private Button addServiceCCButton = new Button("Infolinia");
    private Button closeCCDialogButton = new Button("Zamknij");
    private Button closeServicesDialogButton = new Button("Zamknij");
    private Button createServiceButton = new Button("Dodaj");
    private Button logoButton = new Button();
    private Button logoutButton = new Button("Wyloguj");

    //Select
    private Select<String> citySelect = new Select<>();
    private Select<String> serviceSelect = new Select<>();
    private Select<String> serviceTypeSelect = new Select<>();

    //Labels
    private Label yourPointsTextLabel = new Label("Twoje punkty ");
    private Label yourPointsLabel = new Label();
    private Label callCCLabel = new Label("Aby zgłosić prośbę o pomoc skontaktuj się z naszym konsultantem pod numerem: 000 000 000");
    private Label addServiceLabel = new Label("Zgłoś prośbę o pomoc");
    private Label addServiceTitleLabel = new Label("Prośba o pomoc");

    //Dialog
    private Dialog callCCDialog = new Dialog();
    private Dialog addServiceDialog = new Dialog();

    //TextFields
    private TextArea serviceDescriptionTextField = new TextArea();
    private TextField serviceCityTextField = new TextField ();

    public Main(AuciliumClient auciliumClient) {
        this.auciliumClient = auciliumClient;

        //Top menu
        topMenu.setWidth("100%");
        topMenu.getStyle().set("border-bottom", "3px solid #FDDA24");
        topMenu.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        topMenu.setDefaultVerticalComponentAlignment(
                FlexComponent.Alignment.CENTER);
        add(topMenu);
        add(split);
        split.setHeight("100%");
        split.add(pictures);

        topMenu.addComponentAsFirst(logo);
        topMenu.add(selection);
        topMenu.add(loginButtons);

        //Select
        citySelect.setRequiredIndicatorVisible(true);
        citySelect.setLabel("Miasto");
        citySelect.setItems(auciliumClient.getCities());
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

        logoutButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_TERTIARY);
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
        pictures.add(ladyGoingDownstairs, oldManUnderground, oldManInHat, heart, womanInGarden, manOutside, hands);
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
            yourPointsLabel.setText(auciliumClient.getUserAvailablePoints(parameter).toString());
            uuid = parameter;
        }
    }

    private void addMenuForLoggedUsers(){
        split.addComponentAsFirst(leftMenu);

        //Points
        leftMenu.add(yourPointsTextLabel, yourPointsLabel,exchangePointsButton);
        exchangePointsButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        exchangePointsButton.getStyle().set("margin-bottom", "30px");
        yourPointsTextLabel.getStyle().set("font-size", "30px");
        yourPointsLabel.getStyle().set("font-size", "40px");
        yourPointsLabel.getStyle().set("font-weight", "bold");
        yourPointsLabel.getStyle().set("margin-top", "0px");

        //Services
        leftMenu.add(addServiceLabel);
        leftMenu.add(addServiceManualyButton);
        leftMenu.add(addServiceCCButton);
        addServiceLabel.getStyle().set("font-size", "30px");

        addServiceManualyButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        addServiceCCButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        leftMenu.setMaxWidth("256px");
        leftMenu.getStyle().set("background-color", "rgb(253, 218, 36, 0.3)");
        leftMenu.getStyle().set("border-radius", "15px");
        leftMenu.getStyle().set("padding", "10px");

        addServiceCCButton.addClickListener(event -> callCCDialog.open());
        callCCDialog.add(callCCLabel, closeCCDialogButton);
        callCCDialog.setWidth("500px");
        callCCDialog.setHeight("200px");
        closeCCDialogButton.addClickListener( event -> {
            callCCDialog.close();
        });

        callCCLabel.getStyle().set("font-size", "20px");
        callCCLabel.getStyle().set("font-weight", "bold");
        callCCLabel.getStyle().set("color", "#007481");
        callCCLabel.getStyle().set("margin-top", "30px");
        closeCCDialogButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        closeCCDialogButton.getStyle().set("margin", "70px 214px 10px 214px");


        addServiceDialog.setWidth("400px");
        addServiceDialog.setCloseOnOutsideClick(false);
        addServiceManualyButton.addClickListener(event -> addServiceDialog.open());
        addServiceDialog.add(addService);
        addService.add(addServiceTitleLabel, serviceTypeSelect, serviceCityTextField, serviceDescriptionTextField, addServiceButtons);
        addServiceButtons.add(closeServicesDialogButton, createServiceButton);

        addServiceTitleLabel.getStyle().set("font-size", "30px");
        addServiceTitleLabel.getStyle().set("color", "#007481");
        addServiceTitleLabel.getStyle().set("font-weight", "bold");

        serviceTypeSelect.setRequiredIndicatorVisible(true);
        serviceTypeSelect.setLabel("Rodzaj pomocy");
        serviceTypeSelect.setItems(Arrays.stream(ServicesTypes.values()).map(s -> s.getValue()));
        serviceTypeSelect.setPlaceholder("Wybierz rodzaj");
        serviceTypeSelect.setEmptySelectionCaption("Wybierz rodzaj pomocy");
        serviceTypeSelect.setEmptySelectionAllowed(true);
        serviceTypeSelect.setItemEnabledProvider(Objects::nonNull);

        serviceDescriptionTextField.setLabel("Opis");
        serviceDescriptionTextField.setPlaceholder("Podaj więcej szczegółów, jak termin, piętro bloku, transport...");
        serviceDescriptionTextField.setMaxLength(700);
        serviceDescriptionTextField.setWidthFull();
        serviceDescriptionTextField.setHeight("300px");

        serviceCityTextField.setLabel("Miasto");
        serviceCityTextField.setPlaceholder("Wpisz miasto");
        serviceCityTextField.setMinLength(3);

        closeServicesDialogButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_TERTIARY);
        closeServicesDialogButton.getStyle().set("margin", "0px 250px 0px 0px");
        closeServicesDialogButton.addClickListener( event -> {
            addServiceDialog.close();
        });
        createServiceButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_TERTIARY);
        createServiceButton.addClickListener( event -> {
            serviceTypeSelect.clear();
            serviceCityTextField.clear();
            serviceDescriptionTextField.clear();
        });

    }

    private void changeLoginButtonToLogout(){
        loginButton.getElement().removeFromParent();
        createAccountButton.getElement().removeFromParent();
        loginButtons.add(logoutButton);
    }


}