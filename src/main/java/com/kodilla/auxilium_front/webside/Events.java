package com.kodilla.auxilium_front.webside;

import com.kodilla.auxilium_front.clients.AuxiliumClient;
import com.kodilla.auxilium_front.domain.ServicesTypes;
import com.kodilla.auxilium_front.dto.EventDto;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;

@Route("events")
@Component
public class Events extends VerticalLayout implements HasUrlParameter<String> {

    private final AuxiliumClient auxiliumClient;
    private String uuid;
    private HorizontalLayout topMenu = new HorizontalLayout();
    private HorizontalLayout loginButtons = new HorizontalLayout();
    private HorizontalLayout logo = new HorizontalLayout();
    private HorizontalLayout selection = new HorizontalLayout();
    private VerticalLayout leftMenu = new VerticalLayout();
    private FlexLayout pictures = new FlexLayout();
    private Grid<EventDto> eventsList = new Grid<>(EventDto.class);

    //Images
    private Image auxiliumLogo = new Image( "http://bz.home.pl/ania/Auxilium/logo.png", "Auxilium logo");

    //Buttons
    private Button findButton = new Button("Szukaj");
    private Button createAccountButton = new Button("Załóż konto");
    private Button loginButton = new Button("Zaloguj");
    private Button logoButton = new Button();
    private Button logoutButton = new Button("Wyloguj");

    //Select
    private Select<String> citySelect = new Select<>();
    private Select<String> serviceSelect = new Select<>();


    public Events(AuxiliumClient auxiliumClient) {
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
            if (selectedCity != null && selectedService != null) {
                location = "list/" + selectedService + "&" + selectedCity + "&" + uuid;
                findButton.getUI().ifPresent(ui ->
                        ui.navigate(location));
            } else if (selectedCity == null && selectedService != null) {
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

        //List
        add(eventsList);
        eventsList.setWidth("100%");
        eventsList.setItems(auxiliumClient.getAllEvents());
        eventsList.setColumns("name", "date", "priceInPoints");
        eventsList.addColumn(new ComponentRenderer<>(e -> {
            Image image = new Image(e.getImage(), e.getName());
            image.setHeight("100px");
            image.setWidth("150px");
            return image;
        }));

        eventsList.addColumn(new ComponentRenderer<>(e -> {
            Button collectProduct = new Button("Odbierz nagrodę");
            collectProduct.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_PRIMARY);
            collectProduct.addClickListener( event -> {
                EventDto eventDto = new EventDto(e.getId(), e.getName(), e.getUrl(), e.getImage(),e.getDate()
                        , e.getSegment(), e.getSubsegment(), e.getPriceInPoints());
                auxiliumClient.collectEvent(eventDto, this.uuid);
            });
            return collectProduct;
        }));
        eventsList.getColumnByKey("name").setHeader("wydrzenie");
        eventsList.getColumnByKey("date").setHeader("data");
        eventsList.getColumnByKey("priceInPoints").setHeader("punkty");
        eventsList.setSelectionMode(Grid.SelectionMode.NONE);
        eventsList.getStyle().set("border-color", "#FDDA24");
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        if (parameter != null && !parameter.equals("null")) {
            uuid = parameter;

        }
    }

}

