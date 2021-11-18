package com.tcmj.shampug.modules.address;

import com.tcmj.shampug.modules.custom.AbstractRecord;
import com.tcmj.shampug.pub.RandomUnit;
import com.tcmj.shampug.util.Strings;

import java.util.Objects;
import java.util.Set;

/**
 * obj Name(vname midname lname title m-w-both)
 * address (line1 line2 line3) ?
 * location (cityname, plz, country-name, state-name, iso2, iso3, usw.)
 * xNAL Name and Address Standard (xNL, xAL)
 */
public class Address extends AbstractRecord<Address> {
    /*
    Name Part */
    public static final String TITLE = "title";
    public static final String FIRST_NAME = "firstname";
    public static final String MIDDLE_NAME = "middlename";
    public static final String LAST_NAME = "lastname";
    /*Street name (without number)*/
    public static final String STREET = "street";
    /*Postal-/Zipcode*/
    public static final String ZIPCODE = "zipcode";
    /* State/Province/Prefecture/County/administrative divisions/department*/
    public static final String STATE = "state";
    /*City/Town/Village*/
    public static final String CITY = "city";
    public static final String COUNTRY = "country";
    public static final String CONTINENT = "continent";
    /*
    Address Part ISO */
    /*
     * Tokens to configure lines (streetline, cityline, additionalline).
     * Numbers (street numbers, house numbers, apartment numbers) can be inserted with '#'
     */
    private final static Set<String> TOKENS = Strings.set(
        "title", "firstname", "middlename", "lastname",
        "street", "zipcode", "state", "city", "country", "continent"
    );
    private LineFactory nameLineFactory;


    /*

    Address Lines */
    private LineFactory streetLineFactory;
    private LineFactory cityLineFactory;
    private LineFactory additionalLineFactory;
    private String nameLine;
    private String streetLine;
    private String cityLine;
    private String additionalLine;

    public Address(LineFactory nameLineFactory,
                   LineFactory streetLineFactory,
                   LineFactory cityLineFactory,
                   LineFactory additionalLineFactory, RandomUnit randomUnit) {
        super(Address.class.getName(), randomUnit);
        this.nameLineFactory = nameLineFactory;
        this.streetLineFactory = streetLineFactory;
        this.cityLineFactory = cityLineFactory;
        this.additionalLineFactory = additionalLineFactory;
    }

    public Address(RandomUnit randomUnit) {
        super(Address.class.getName(), randomUnit);

    }


    public Address(String title, String firstName, String middleName, String lastName, String street, String zipcode, String state, String city, String country, String continent,
                   String nameLineTokens, String streetLineTokens, String cityLineTokens, String additionalLineTokens, RandomUnit randomUnit) {
        super(Address.class.getName(), randomUnit);
        set(TITLE, title);
        set(FIRST_NAME, firstName);
        set(MIDDLE_NAME, middleName);
        set(LAST_NAME, lastName);
        set(STREET, street);
        set(ZIPCODE, zipcode);
        set(STATE, state);
        set(CITY, city);
        set(COUNTRY, country);
        set(CONTINENT, continent);

        this.streetLine = buildStreetLine();

    }

    @Override
    public Set<String> getTokens() {
        return TOKENS;
    }

    private String buildStreetLine() {

        return "dfdfjdf";
    }

    public String nameLineTokens() {
        return this.nameLineFactory.getLineTokenString();
    }


    public Address withNameFactory(LineFactory nameLineSupplier) {
        this.nameLineFactory = nameLineSupplier;
        return this;
    }

    public Address withDefaultNameFactory() {
        this.nameLineFactory = LineFactory.DEFAULT_NAME;
        return this;
    }

    public Address withStreetFactory(LineFactory streetLineSupplier) {
        this.streetLineFactory = streetLineSupplier;
        return this;
    }


    /**
     * a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     */
    public int compareTo(Address other) {
        if (other == null) {
            return 1;
        }
        return Strings.compare(getNameLine() + getStreetLine() + getCityLine(), other.getNameLine() + other.getStreetLine() + other.getCityLine());
    }

    public Address set(String field, String value) {
        super.set(field, value);
        return this;
    }

    public String getStreetLine() {
        if (this.streetLine == null) {
            this.streetLine = Objects.requireNonNull(this.streetLineFactory, "Please define a LineFactory for the Street!").getLine(this, getRandomUnit());
        }
        return this.streetLine;
    }

    public String getCityLine() {
        if (this.cityLine == null) {
            this.cityLine = Objects.requireNonNull(this.cityLineFactory, "Please define a LineFactory for the City!").getLine(this, getRandomUnit());
        }
        return this.cityLine;
    }

    public String getNameLine() {
        if (this.nameLine == null) {
            this.nameLine = Objects.requireNonNull(this.nameLineFactory, "Please define a LineFactory for the Name!").getLine(this, getRandomUnit());
        }
        return this.nameLine;
    }

    public String getAdditionalLine() {
        if (this.additionalLine == null) {
            this.additionalLine = Objects.requireNonNull(this.additionalLineFactory, "Please define a LineFactory for the AdditionalLine!").getLine(this, getRandomUnit());
        }
        return this.additionalLine;
    }


    public String title() {
        return get(TITLE);
    }

    public Address title(String title) {
        set(TITLE, title);
        return this;
    }

    public String firstName() {
        return get(FIRST_NAME);
    }

    public Address firstName(String firstName) {
        set(FIRST_NAME, firstName);
        return this;
    }

    public String middleName() {
        return get(MIDDLE_NAME);
    }

    public Address middleName(String middleName) {
        set(MIDDLE_NAME, middleName);
        return this;
    }

    public String lastName() {
        return get(LAST_NAME);
    }

    public Address lastName(String lastName) {
        set(LAST_NAME, lastName);
        return this;
    }

    public Address street(String street) {
        set(STREET, street);
        return this;
    }

    public Address zipcode(String zipcode) {
        set(ZIPCODE, zipcode);
        return this;
    }

    public Address state(String state) {
        set(STATE, state);
        return this;
    }

    public Address city(String city) {
        set(CITY, city);
        return this;
    }

    public Address country(String country) {
        set(COUNTRY, country);
        return this;
    }

    public Address continent(String continent) {
        set(CONTINENT, continent);
        return this;
    }

    public String street() {
        return get(STREET);
    }

    public String zipcode() {
        return get(ZIPCODE);
    }

    public String state() {
        return get(STATE);
    }

    public String city() {
        return get(CITY);
    }

    public String country() {
        return get(COUNTRY);
    }

    public String continent() {
        return get(CONTINENT);
    }

    public String streetLineTokens() {
        return this.streetLineFactory.getLineTokenString();
    }

    public String cityLineTokens() {
        return this.cityLineFactory.getLineTokenString();
    }

    public String additionalLineTokens() {
        return this.additionalLineFactory.getLineTokenString();
    }


}
