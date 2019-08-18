package com.kodilla.auxilium_front.webside;

import com.kodilla.auxilium_front.clients.AuciliumClient;
import com.kodilla.auxilium_front.domain.ServicesTypes;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.Route;

import java.util.Arrays;
import java.util.Objects;

@Route("login")
public class Login extends VerticalLayout {

    private HorizontalLayout topMenu = new HorizontalLayout();
    private HorizontalLayout loginButtons = new HorizontalLayout();
    private HorizontalLayout logo = new HorizontalLayout();
    private HorizontalLayout selection = new HorizontalLayout();
    private HorizontalLayout login = new HorizontalLayout();
    private VerticalLayout loginForm = new VerticalLayout();
    private VerticalLayout loginLayout = new VerticalLayout();
    private String uuid;

    private final AuciliumClient auciliumClient;

    private boolean logged = true;

    //Images
    private Image auxiliumLogo = new Image( "http://bz.home.pl/ania/Auxilium/logo.png", "Auxilium logo");

    //Labels
    private Label loginLabel = new Label("Zaloguj się");
    private Label loginSentenceLabel = new Label("'Każdy ma coś, co może dać innym'");
    private Label loginAuthorLabel = new Label("Barbara Bush");
    private Label loginIncorrectLabel = new Label("Podany email lub hasło są nieprawidłowe." +
            " Spróbuj ponownie.");


    //Buttons
    private Button findButton = new Button("Szukaj");
    private Button createAccountButton = new Button("Załóż konto");
    private Button loginButton = new Button("Zaloguj");
    private Button loginInFormButton = new Button("Zaloguj");
    private Button createAccountInFormButton = new Button("Nie masz konta, zostań jednym z pomagających");
    private Button logoButton = new Button();

    //Select
    private Select<String> citySelect = new Select<>();
    private Select<String> serviceSelect = new Select<>();

    //Fields
    private EmailField emailField = new EmailField("Email");
    private PasswordField passwordField = new PasswordField("Hasło");

    //Dialog
    private Dialog loginIncorrectDialog = new Dialog();


    public Login(AuciliumClient auciliumClient) {
        this.auciliumClient = auciliumClient;


        //Top menu
        topMenu.setWidth("100%");
        topMenu.getStyle().set("border-bottom", "3px solid #FDDA24");
        topMenu.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        topMenu.setDefaultVerticalComponentAlignment(
                FlexComponent.Alignment.CENTER);
        add(topMenu);
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
                location = "list/" + selectedService + "&" + selectedCity;
                findButton.getUI().ifPresent((ui ->
                        ui.navigate(location)));
            } else if (selectedCity == null && selectedService != null){
                location = "list/" + selectedService + "&null";
                findButton.getUI().ifPresent((ui ->
                        ui.navigate(location)));
            } else if (selectedCity != null && selectedService == null) {
                location = "list/null&" + selectedCity;
                findButton.getUI().ifPresent((ui ->
                        ui.navigate(location)));
            }
        });

        //Login&CreateAccount
        loginButtons.addComponentAsFirst(loginButton);
        loginButtons.addComponentAsFirst(createAccountButton);
        loginButtons.setHeight("105px");
        loginButtons.setAlignItems(Alignment.CENTER);
        loginButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_TERTIARY);
        createAccountButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_TERTIARY);

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
            String location = "" + uuid;
            logoButton.getUI().ifPresent(ui ->
                    ui.navigate(location));
        });

        //LoginIncorrect
        loginIncorrectDialog.add(loginIncorrectLabel);
        loginIncorrectDialog.setWidth("500px");
        loginIncorrectDialog.setMinHeight("400px");
        loginIncorrectLabel.getStyle().set("color", "#007481");
        loginIncorrectLabel.getStyle().set("font-weight", "bold");
        loginIncorrectLabel.getStyle().set("font-size", "20px");

        //Login
        add(login);
        setAlignItems(Alignment.CENTER);
        login.setWidth("60%");
        login.add(loginLayout, loginForm);
        login.setAlignItems(Alignment.STRETCH);
        login.getStyle().set("border", "2px solid #46a0aa");

        loginLayout.getStyle().set("background-color", "#46a0aa");
        loginLayout.setWidth("50%");
        loginLayout.setAlignItems(Alignment.CENTER);
        loginLayout.getStyle().set("padding-top", "8%");
        loginLayout.add(loginSentenceLabel, loginAuthorLabel);
        loginSentenceLabel.getStyle().set("color", "white");
        loginSentenceLabel.getStyle().set("font-size", "30px");
        loginSentenceLabel.getStyle().set("font-style", "italic");
        loginAuthorLabel.getStyle().set("color", "white");

        loginForm.add(loginLabel, emailField, passwordField, loginInFormButton, createAccountInFormButton);
        loginForm.setWidth("50%");
        loginForm.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        createAccountInFormButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
        createAccountInFormButton.addClickListener(e -> {
            createAccountInFormButton.getUI().ifPresent((ui ->
                    ui.navigate("registration")));
        });

        loginLabel.getStyle().set("font-size", "30px");
        loginLabel.getStyle().set("color", "#007481");
        loginLabel.getStyle().set("font-weight", "bold");

        emailField.setClearButtonVisible(true);
        emailField.setPlaceholder("Wpisz email");
        emailField.setErrorMessage("Wpisz poprawny adres email");
        emailField.setValue("gafsss@jk.com");

        passwordField.setPlaceholder("Wpisz hasło");
        passwordField.setErrorMessage("Wpisz hasło");
        passwordField.setValue(")(736Kn");


        loginInFormButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_PRIMARY);
        loginInFormButton.addClickListener(e -> {
            logged = auciliumClient.login(passwordField.getValue(), emailField.getValue());
            if(logged){
                String location = "" + auciliumClient.getUserByLoginData(passwordField.getValue(), emailField.getValue()).getUuid();
                loginInFormButton.getUI().ifPresent(ui ->
                        ui.navigate(location));
            } else {
                loginIncorrectDialog.open();
            }

        });

    }

}
