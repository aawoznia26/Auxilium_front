package com.kodilla.auxilium_front.webside;

import com.kodilla.auxilium_front.clients.AuxiliumClient;
import com.kodilla.auxilium_front.domain.ServicesTypes;
import com.kodilla.auxilium_front.dto.ServicesDto;
import com.kodilla.auxilium_front.dto.TransactionDto;
import com.kodilla.auxilium_front.dto.UserDto;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;

@Component
public class AddServiceFormDialog extends VerticalLayout implements HasUrlParameter<String> {

    private final AuxiliumClient auxiliumClient;
    private VerticalLayout addService = new VerticalLayout();
    private HorizontalLayout addServiceButtons = new HorizontalLayout();
    private String uuid;

    //TextFields
    private TextArea serviceDescriptionTextField = new TextArea();
    private TextField serviceCityTextField = new TextField ();

    //Buttons
    private Button closeServicesDialogButton = new Button("Zamknij");
    private Button createServiceButton = new Button("Dodaj");

    //Select
    private Select<String> serviceTypeSelect = new Select<>();

    //Dialogs
    private Dialog addServiceDialog = new Dialog();

    //Labels
    private Label addServiceTitleLabel = new Label("Prośba o pomoc");

    public AddServiceFormDialog(AuxiliumClient auxiliumClient) {
        this.auxiliumClient = auxiliumClient;

        addServiceDialog.setWidth("400px");
        addServiceDialog.setCloseOnOutsideClick(false);
        addServiceDialog.add(addService);
        addService.add(addServiceTitleLabel, serviceTypeSelect, serviceCityTextField, serviceDescriptionTextField, addServiceButtons);
        addServiceButtons.add(closeServicesDialogButton, createServiceButton);

        addServiceTitleLabel.getStyle().set("font-size", "30px");
        addServiceTitleLabel.getStyle().set("color", "#007481");
        addServiceTitleLabel.getStyle().set("font-weight", "bold");

        serviceTypeSelect.setRequiredIndicatorVisible(true);
        serviceTypeSelect.setLabel("Rodzaj pomocy");
        serviceTypeSelect.setPlaceholder("Wybierz rodzaj");
        serviceTypeSelect.setEmptySelectionCaption("Wybierz rodzaj pomocy");
        serviceTypeSelect.setEmptySelectionAllowed(true);
        serviceTypeSelect.setItemEnabledProvider(Objects::nonNull);
        serviceTypeSelect.setItems(Arrays.stream(ServicesTypes.values()).map(s -> s.getValue()));

        serviceDescriptionTextField.setLabel("Opis");
        serviceDescriptionTextField.setPlaceholder("Podaj więcej szczegółów, jak termin, piętro bloku, transport...");
        serviceDescriptionTextField.setMaxLength(700);
        serviceDescriptionTextField.setWidthFull();
        serviceDescriptionTextField.setHeight("300px");

        serviceCityTextField.setLabel("Miasto");
        serviceCityTextField.setPlaceholder("Wpisz miasto");
        serviceCityTextField.setMinLength(3);

        closeServicesDialogButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_TERTIARY_INLINE);
        closeServicesDialogButton.getElement().getStyle().set("color", "#007481");
        closeServicesDialogButton.getStyle().set("font-size", "15px");
        closeServicesDialogButton.getStyle().set("margin", "0px 200px 0px 20px");
        closeServicesDialogButton.addClickListener( event -> {
            addServiceDialog.close();
        });

        createServiceButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_PRIMARY);
        createServiceButton.addClickListener( event -> {
            createService();
            serviceTypeSelect.clear();
            serviceCityTextField.clear();
            serviceDescriptionTextField.clear();
            addServiceDialog.close();
        });
    }


    public void openDialog(){
        addServiceDialog.open();
    }

    private TransactionDto createService(){
        ServicesDto servicesDto = new ServicesDto(serviceTypeSelect.getValue(), serviceDescriptionTextField.getValue(), serviceCityTextField.getValue());
        UserDto userDto = auxiliumClient.getUserByUUID(uuid);
        ServicesDto servicesSaved = auxiliumClient.createService(servicesDto);
        return auxiliumClient.createTransaction(userDto, servicesSaved);

    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        if(parameter != null){
                uuid = parameter;
            }
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
