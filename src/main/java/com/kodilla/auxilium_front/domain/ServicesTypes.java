package com.kodilla.auxilium_front.domain;

public enum ServicesTypes {
    GO_FOR_SHOPPING("Zrobienie zakupów")
    , ASSIST_IN_SHOPPING("Pomoc w zakupach")
    , ASSIST_IN_CHURCH_VISIT("Asysta w wizycie w kościele")
    , ASSIST_IN_WALK("Wyjście na spacer")
    , HOUSE_CLEANING("Sprzątanie domu")
    , FLAT_CLEANING("Sprzątanie mieszkania")
    , ASSIST_IN_DOCTOR_VISIT("Asysta podczas wizyty u lekarza")
    , MAKE_DINNER("Przygotowanie posiłku")
    , ASSIST_IN_OFFICIAL_BUSINESS("Asysta w urzędzie");

    ServicesTypes(String value) {
        this.value = value;
    }

    private String value;
    public String getValue() {
        return value;
    }


}
