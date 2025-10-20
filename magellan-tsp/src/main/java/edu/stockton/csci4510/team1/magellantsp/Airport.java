package edu.stockton.csci4510.team1.magellantsp;

public class Airport {
  private String country;
  private String airportName;
  double latitude;
  double longitude;

  public Airport(String country, String airportName, double latitude, double longitude) {
    this.country = country;
    this.airportName = airportName;
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public double getLatitude() {
    return latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public String getCountry() {
    return country;
  }

  public String getAirportName() {
    return airportName;
  }

  public String toString() {
    return airportName + ", " + country + " (" + latitude + ", " + longitude + ")";
  }
}
