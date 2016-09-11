package main.prices;

import java.util.Date;


/**
 * Created by Savchenko A. on 07.09.2016.
 */
public class Price {
    private long id;
    private String product_code;
    private int number;
    private int depart;
    private Date begin;
    private Date end;
    private long value;

    public Price(long id, String product_code, int number, int depart, Date begin, Date end, long value) {
        this.id = id;
        this.product_code = product_code;
        this.number = number;
        this.depart = depart;
        this.begin = begin;
        this.end = end;
        this.value = value;
    }

    public Price(Price otherPrice)
    {
        this.id = otherPrice.getId();
        this.product_code = otherPrice.getProduct_code();
        this.number = otherPrice.getNumber();
        this.depart = otherPrice.getDepart();
        this.begin = otherPrice.getBegin();
        this.end = otherPrice.getEnd();
        this.value = otherPrice.getValue();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getDepart() {
        return depart;
    }

    public void setDepart(int depart) {
        this.depart = depart;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    /**
     * Проверка корректности параметров цены
     */
    public boolean isValid() {
        // данные параметры не могут быть не положительны
        if (number <= 0 || depart <= 0 || value <= 0) {
            return false;
        }

        // обязательно должен быть установлен код продукта
        if (product_code == null || product_code.isEmpty()) {
            return false;
        }
        // даты должны быть установлены и конец должен быть после начала
        if (begin == null || end == null || end.before(begin)) {
            return false;
        }
        return true;
    }

    // перегружаем для тестирования

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + this.product_code.hashCode();
        hash = 31 * hash + this.depart;
        hash = 31 * hash + this.number;
        hash = 31 * hash + this.begin.hashCode();
        hash = 31 * hash + this.end.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Price other = (Price) obj;
        if (this.number != other.number) {
            return false;
        }
        if (this.depart != other.depart) {
            return false;
        }
        if (!this.product_code.equals(other.product_code)) {
            return false;
        }
        if (!this.begin.equals(other.begin)) {
            return false;
        }
        return this.end.equals(other.end);
    }

    @Override
    public String toString() {
        return "Price{" + "id=" + id + ", product_code=" + product_code
                + ", number=" + number + ", depart=" + depart
                + ", begin=" + begin + ", end=" + end
                + ", value=" + value + '}';
    }
}
