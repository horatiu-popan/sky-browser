package org.example.spring1;

public class UrlMapping {
  public static final String API = "/api";
  public static final String ITEMS = API + "/items";

  public static final String AUTH = API + "/auth";

  public static final String SIGN_IN = "/sign-in";
  public static final String SIGN_UP = "/sign-up";

  public static final String ID_PART = "/{id}";
  public static final String FILTERED_ITEMS_PART = "/filtered";

  public static final String CHANGE_NAME_PART = "/change-name";
  public static final String AIRPORTS = API + "/airports";
  public static final String FILTERED_PART = "/filtered";
  public static final String CHANGE_IATA_CODE_PART = "/change-iata-code";
  public static final String CITIES = API + "/cities";

  public static final String AIRLINES = API + "/airlines";
  public static final String FLIGHTS = API + "/flights";
  public static final String TICKETS = API + "/tickets";
  public static final String BOOKINGS = API + "/bookings";

}
