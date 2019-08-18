package com.kodilla.auxilium_front.webside;

import com.kodilla.auxilium_front.clients.AuciliumClient;
import com.kodilla.auxilium_front.domain.ServicesTypes;
import com.kodilla.auxilium_front.dto.ServicesDto;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.*;

import java.util.Arrays;
import java.util.Objects;

@Route("list")
public class ServicesListing extends VerticalLayout implements HasUrlParameter<String> {

    private HorizontalLayout topMenu = new HorizontalLayout();
    private HorizontalLayout loginButtons = new HorizontalLayout();
    private HorizontalLayout logo = new HorizontalLayout();
    private HorizontalLayout selection = new HorizontalLayout();
    private Grid<ServicesDto> servicesList = new Grid<>(ServicesDto.class);
    private VerticalLayout dialogView = new VerticalLayout();
    private VerticalLayout dialogButtonView = new VerticalLayout();
    private String uuid;


    private final AuciliumClient auciliumClient;

    //Images
    private Image auxiliumLogo = new Image( "http://bz.home.pl/ania/Auxilium/logo.png", "Auxilium logo");


    //Buttons
    private Button findButton = new Button("Szukaj");
    private Button createAccountButton = new Button("Załóż konto");
    private Button loginButton = new Button("Zaloguj");
    private Button assignServiceButton = new Button("Pomagam");
    private Button logoButton = new Button();
    private Button logoutButton = new Button("Wyloguj");

    //Dialogs
    private Dialog serviceDialog = new Dialog();
    private Dialog noSearchResultDialog = new Dialog();


    //Select
    private Select<String> citySelect = new Select<>();
    private Select<String> serviceSelect = new Select<>();

    //Labels
    private Label serviceTitle = new Label();
    private Label serviceCity = new Label();
    private Label servicePoints = new Label();
    private Label serviceDescriptionTextField = new Label();
    private Label noSearchReslutLabel = new Label("Niestety, nic nie znaleźliśmy. Zmodyfikuj kryteria wyszukiwania i sprawdź ponownie");



    public ServicesListing(AuciliumClient auciliumClient) {
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
        selection.setAlignItems(FlexComponent.Alignment.CENTER);
        selection.getStyle().set("margin-right", "7%");
        findButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_TERTIARY_INLINE);
        findButton.addClickListener(e -> {
            String selectedCity = citySelect.getValue();
            String selectedService = serviceSelect.getValue();
            if(!selectedCity.equals("null") && !selectedService.equals("null")){
                servicesList.setItems(auciliumClient.getServicesFilteredByCityAndType(selectedCity, selectedService));
            } else if (!selectedCity.equals("null") && selectedService.equals("null")) {
                servicesList.setItems(auciliumClient.getServicesFilteredByCity(selectedCity));
            } else if (selectedCity.equals("null") && !selectedService.equals("null")) {
                servicesList.setItems(auciliumClient.getServicesFilteredByType(selectedService));
            }
            long l = servicesList.getDataProvider().size(new Query<>());
            if(l == 0){
                noSearchResultDialog.open();
            };
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
            String location;
            if(uuid == null){
                location = "";
            } else {
                location = "" + uuid;
            }
            logoButton.getUI().ifPresent(ui ->
                    ui.navigate(location));
        });


        //List
        add(servicesList);
        servicesList.setWidth("100%");
        servicesList.setItems(auciliumClient.getAllServices());
        servicesList.setColumns("city", "name", "description", "points");
        servicesList.getColumnByKey("city").setHeader("miasto");
        servicesList.getColumnByKey("description").setHeader("szczegóły");
        servicesList.getColumnByKey("name").setHeader("rodzaj pomocy");
        servicesList.getColumnByKey("points").setHeader("punkty");

        dialogView.add(serviceTitle, serviceCity, serviceDescriptionTextField, dialogButtonView);
        dialogButtonView.add(servicePoints, assignServiceButton);
        dialogButtonView.setAlignItems(Alignment.END);
        serviceDialog.setWidth("500px");
        serviceDialog.setMinHeight("400px");
        serviceTitle.getStyle().set("font-size", "30px");
        serviceTitle.getStyle().set("color", "#007481");
        serviceTitle.getStyle().set("font-weight", "bold");
        serviceDescriptionTextField.getStyle().set("font-size", "20px");
        assignServiceButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_PRIMARY);


        servicesList.setSelectionMode(Grid.SelectionMode.NONE);
        servicesList.getStyle().set("border-color", "#007481");
        servicesList.addItemClickListener(
                event -> {
                    event.getItem().getId();
                    serviceDialog.open();
                    serviceDialog.add(dialogView);
                    serviceTitle.setText(event.getItem().getName());
                    serviceDescriptionTextField.setText(event.getItem().getDescription());
                    serviceCity.setText(event.getItem().getCity());
                    servicePoints.setText(String.valueOf(event.getItem().getPoints()) + " punktów");
                });

        //Search
        noSearchResultDialog.add(noSearchReslutLabel);
        noSearchResultDialog.setWidth("500px");
        noSearchResultDialog.setMinHeight("400px");
        noSearchReslutLabel.getStyle().set("color", "#007481");
        noSearchReslutLabel.getStyle().set("font-weight", "bold");
        noSearchReslutLabel.getStyle().set("font-size", "20px");


    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        if(parameter != null){
            String[] splitedParameter = parameter.split("&");
            serviceSelect.setValue(splitedParameter[0]);
            citySelect.setValue(splitedParameter[1]);
            findButton.click();
            if(!splitedParameter[2].equals("null")){
                changeLoginButtonToLogout();
                uuid = splitedParameter[2];
            }
        }
    }

    private void changeLoginButtonToLogout(){
        loginButton.getElement().removeFromParent();
        createAccountButton.getElement().removeFromParent();
        loginButtons.add(logoutButton);
    }
}
