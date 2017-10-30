package com.akarbowy.spendingsapp.data;

import java.util.EnumSet;

import static com.akarbowy.spendingsapp.data.Dictionaries.CategoryGroup.Category.Accessories;
import static com.akarbowy.spendingsapp.data.Dictionaries.CategoryGroup.Category.Air;
import static com.akarbowy.spendingsapp.data.Dictionaries.CategoryGroup.Category.Bike;
import static com.akarbowy.spendingsapp.data.Dictionaries.CategoryGroup.Category.Boat;
import static com.akarbowy.spendingsapp.data.Dictionaries.CategoryGroup.Category.Books;
import static com.akarbowy.spendingsapp.data.Dictionaries.CategoryGroup.Category.Bus;
import static com.akarbowy.spendingsapp.data.Dictionaries.CategoryGroup.Category.Charity;
import static com.akarbowy.spendingsapp.data.Dictionaries.CategoryGroup.Category.Clothing;
import static com.akarbowy.spendingsapp.data.Dictionaries.CategoryGroup.Category.CoffeeShop;
import static com.akarbowy.spendingsapp.data.Dictionaries.CategoryGroup.Category.Detergents;
import static com.akarbowy.spendingsapp.data.Dictionaries.CategoryGroup.Category.Doctors;
import static com.akarbowy.spendingsapp.data.Dictionaries.CategoryGroup.Category.Family;
import static com.akarbowy.spendingsapp.data.Dictionaries.CategoryGroup.Category.Friends;
import static com.akarbowy.spendingsapp.data.Dictionaries.CategoryGroup.Category.Furniture;
import static com.akarbowy.spendingsapp.data.Dictionaries.CategoryGroup.Category.Grocery;
import static com.akarbowy.spendingsapp.data.Dictionaries.CategoryGroup.Category.Hotel;
import static com.akarbowy.spendingsapp.data.Dictionaries.CategoryGroup.Category.Railway;
import static com.akarbowy.spendingsapp.data.Dictionaries.CategoryGroup.Category.SportingGoods;
import static com.akarbowy.spendingsapp.data.Dictionaries.CategoryGroup.Category.Tram;
import static com.akarbowy.spendingsapp.data.Dictionaries.CategoryGroup.Category.Transport;
import static com.akarbowy.spendingsapp.data.Dictionaries.CategoryGroup.Category.PersonalCareGoods;
import static com.akarbowy.spendingsapp.data.Dictionaries.CategoryGroup.Category.Pharmacy;
import static com.akarbowy.spendingsapp.data.Dictionaries.CategoryGroup.Category.Phone;
import static com.akarbowy.spendingsapp.data.Dictionaries.CategoryGroup.Category.Restaurant;
import static com.akarbowy.spendingsapp.data.Dictionaries.CategoryGroup.Category.Souvenirs;
import static com.akarbowy.spendingsapp.data.Dictionaries.CategoryGroup.Category.Taxi;
import static com.akarbowy.spendingsapp.data.Dictionaries.CategoryGroup.Category.Tickets;
import static com.akarbowy.spendingsapp.data.Dictionaries.CategoryGroup.Category.Tube;
import static com.akarbowy.spendingsapp.data.Dictionaries.CategoryGroup.Category.Undefined;
import static com.akarbowy.spendingsapp.data.Dictionaries.CategoryGroup.Category.Utilities;

/**
 * Created by arek.karbowy on 26/10/2017.
 */

public interface Dictionaries {

    enum Currency {
        PLN("zł", "PLN", "Polish Złoty"),
        GBP("£", "GBP", "British Pound"),
        EUR("€", "EUR", "Euro"),
        USD("$", "USD", "American Dollars");

        public final String symbol;
        public final String isoCode;
        public final String title;

        Currency(String symbol, String isoCode, String title) {
            this.symbol = symbol;
            this.isoCode = isoCode;
            this.title = title;
        }

    }

    enum CategoryGroup {
        Transportation("Transportation", EnumSet.of(Tube, Bus, Taxi, Air, Railway, Tram, Bike, Boat)),
        FoodAndDining("Food & Dining", EnumSet.of(Grocery, CoffeeShop, Restaurant)),
        Bills("Bills", EnumSet.of(Phone, Utilities, Tickets)),
        Travel("Travel", EnumSet.of(Transport, Hotel, Souvenirs)),
        Health("Health", EnumSet.of(Pharmacy, Doctors, PersonalCareGoods)),
        House("House", EnumSet.of(Detergents, Furniture)),
        Shopping("Shopping", EnumSet.of(Clothing, Books, Accessories, SportingGoods)),
        GiftsAndCharity("Gifts & Charity", EnumSet.of(Family, Friends, Charity)),
        Other("Undefined", EnumSet.of(Undefined));

        final String title;
        final EnumSet<Category> categories;

        CategoryGroup(String title, EnumSet<Category> categories) {
            this.title = title;
            this.categories = categories;
        }

        enum Category {
            //Transport
            Tube("Tube"),
            Bus("Bus"),
            Taxi("Taxi"),
            Air("Air"),
            Railway("Railway"),
            Tram("Tram"),
            Bike("Bike"),
            Boat("Boat"),

            //Food & Dining
            Grocery("Grocery"),
            CoffeeShop("Coffee Shop"),
            Restaurant("Restaurant"),

            //Bills
            Phone("Phone"),
            Utilities("Utilities"),
            Tickets("Tickets"),

            //Travel
            Transport("Transport"),
            Hotel("Hotel"),
            Souvenirs("Souvenirs"),

            //Health
            Pharmacy("Pharmacy"),
            Doctors("Doctors"),
            PersonalCareGoods("Goods"),

            //House
            Detergents("Detergents"),
            Furniture("Furniture"),

            //Shopping
            Clothing("Clothing"),
            Books("Books"),
            Accessories("Accessories"),
            SportingGoods("Sporting Goods"),

            //Gifts & Charity
            Family("Family"),
            Friends("Friends"),
            Charity("Charity"),

            //Undefined
            Undefined("Other");

            final String title;

            Category(String title) {
                this.title = title;
            }
        }

    }
}
