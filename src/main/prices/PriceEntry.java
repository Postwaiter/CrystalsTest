package main.prices;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by Savchenko A. on 11.09.2016.
 */

public class PriceEntry {
    // цена
    private Price price;

    // флаг, обработана цена или нет
    private Option option;

    public PriceEntry() {
    }

    public PriceEntry(Price price, Option option) {
        this.price = price;
        this.option = option;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
    }


    public String getProduct_code() {
        return price.getProduct_code();
    }


    public void setProduct_code(String product_code) {
        price.setProduct_code(product_code);
    }


    public int getNumber() {
        return price.getNumber();
    }


    public void setNumber(int number) {
        price.setNumber(number);
    }

    public int getDepart() {
        return price.getDepart();
    }


    public void setDepart(int depart) {
        price.setDepart(depart);
    }

    public Date getBegin() {
        return price.getBegin();
    }


    public void setBegin(Date begin) {
        price.setBegin(begin);
    }


    public Date getEnd() {
        return price.getEnd();
    }

    public void setEnd(Date end) {
        price.setEnd(end);
    }

    public long getValue() {
        return price.getValue();
    }

    public void setValue(long value) {
        price.setValue(value);
    }

    /** получаем список записей цен по коллекции цен */
    public static List<PriceEntry> getEntries(Collection<Price> prices) {
        List<PriceEntry> records = new ArrayList<>();
        for (Price price : prices) {
            records.add(new PriceEntry(price, Option.NEW));
        }
        return records;
    }

    /** получаем список цен по коллекции записей */
    public static List<Price> getPrices(Collection<PriceEntry> entries) {
        List<Price> prices = new ArrayList<>();
        for (PriceEntry entry : entries) {
            prices.add(entry.getPrice());
        }
        return prices;
    }
    // флаг, обработана цена или нет
    public enum Option {
        NEW, OLD
    }

    @Override
    public String toString() {
        return "PriceEntry{" + "price=" + price + ", status=" + option + '}';
    }
}
