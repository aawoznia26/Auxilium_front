package com.kodilla.auxilium_front;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route
public class Main extends HorizontalLayout {

    public Main() {
        add(new Button("Szukaj"));
        add(new Image("src/main/resources/static/aged-1835599_1920.jpg","man"));
    }
}
