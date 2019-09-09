package com.kodilla.auxilium_front.webside;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.stereotype.Component;

@Component
public class AddServiceCCDialog extends VerticalLayout {

    //Buttons
    private Button closeCCDialogButton = new Button("Zamknij");

    //Dialogs
    private Dialog callCCDialog = new Dialog();

    //Labels
    private Label callCCLabel = new Label("Aby zgłosić prośbę o pomoc skontaktuj się z naszym konsultantem pod numerem: 000 000 000");

    public AddServiceCCDialog() {
        callCCDialog.add(callCCLabel, closeCCDialogButton);
        callCCDialog.setWidth("500px");
        callCCDialog.setHeight("200px");
        closeCCDialogButton.addClickListener( event -> {
            callCCDialog.close();
        });

        callCCLabel.getStyle().set("font-size", "20px");
        callCCLabel.getStyle().set("font-weight", "bold");
        callCCLabel.getStyle().set("margin-top", "50px");
        closeCCDialogButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_TERTIARY_INLINE);
        closeCCDialogButton.getElement().getStyle().set("color", "#007481");
        closeCCDialogButton.getStyle().set("font-size", "15px");
        closeCCDialogButton.getStyle().set("margin", "70px 214px 10px 214px");
    }

    public void openDialog(){
        callCCDialog.open();
    }


}
