package test;

import main.prices.Price;
import main.prices.PriceCombinator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;

/**
 * Created by Savchenko A. on 11.09.2016.
 */
public class PriceCombinatorTest {

    public PriceCombinatorTest() {
        combinator = new PriceCombinator();
    }

    private PriceCombinator combinator;


    // ------------------------ Методы генерирующие комплекты цен из примеров тестового задания -----------------------------------
    // текущие цены
    private Collection<Price> getCurrentPrices() {
        Collection<Price> oldPrices = new ArrayList<>();
        oldPrices.add(new Price(0, "122856", 1, 1, new Date(2013, 1, 1, 0, 0, 0), new Date(2013, 1, 31, 23, 59, 59), 11000));
        oldPrices.add(new Price(1, "122856", 2, 1, new Date(2013, 1, 10, 0, 0, 0), new Date(2013, 1, 20, 23, 59, 59), 99000));
        oldPrices.add(new Price(2, "6654", 1, 2, new Date(2013, 1, 1, 0, 0, 0), new Date(2013, 1, 31, 23, 59, 59), 5000));

        //добавляем дубликаты:
        oldPrices.add(new Price(3, "122856", 2, 1, new Date(2013, 1, 10, 0, 0, 0), new Date(2013, 1, 20, 23, 59, 59), 99000));
        oldPrices.add(new Price(4, "6654", 1, 2, new Date(2013, 1, 1, 0, 0, 0), new Date(2013, 1, 31, 23, 59, 59), 5000));
        return oldPrices;
    }

    // новые цены
    private Collection<Price> getNewPrices() {
        Collection<Price> newPrices = new ArrayList<>();
        newPrices.add(new Price(0, "122856", 1, 1, new Date(2013, 1, 20, 0, 0, 0), new Date(2013, 2, 20, 23, 59, 59), 11000));
        newPrices.add(new Price(1, "122856", 2, 1, new Date(2013, 1, 15, 0, 0, 0), new Date(2013, 1, 25, 23, 59, 59), 92000));
        newPrices.add(new Price(2, "6654", 1, 2, new Date(2013, 1, 12, 0, 0, 0), new Date(2013, 1, 12, 23, 59, 59), 4000));

        //добавляем дубликаты:
        newPrices.add(new Price(3, "122856", 2, 1, new Date(2013, 1, 15, 0, 0, 0), new Date(2013, 1, 25, 23, 59, 59), 92000));
        newPrices.add(new Price(4, "6654", 1, 2, new Date(2013, 1, 12, 0, 0, 0), new Date(2013, 1, 12, 23, 59, 59), 4000));
        return newPrices;
    }

    /**
     * Создаем объединение цен. (без дубликатов)
     */
    private Collection<Price> getPriceUnion() {
        Collection<Price> union = new ArrayList<>();
        union.add(new Price(0, "122856", 2, 1, new Date(2013, 1, 10, 0, 0, 0), new Date(2013, 1, 14, 23, 59, 59), 99000));
        union.add(new Price(1, "122856", 1, 1, new Date(2013, 1, 1, 0, 0, 0), new Date(2013, 2, 20, 23, 59, 59), 11000));
        union.add(new Price(2, "122856", 2, 1, new Date(2013, 1, 15, 0, 0, 0), new Date(2013, 1, 25, 23, 59, 59), 92000));
        union.add(new Price(3, "6654", 1, 2, new Date(2013, 1, 1, 0, 0, 0), new Date(2013, 1, 11, 23, 59, 59), 5000));
        union.add(new Price(4, "6654", 1, 2, new Date(2013, 1, 12, 0, 0, 0), new Date(2013, 1, 12, 23, 59, 59), 4000));
        union.add(new Price(5, "6654", 1, 2, new Date(2013, 1, 13, 0, 0, 0), new Date(2013, 1, 31, 23, 59, 59), 5000));
        return union;
    }

    /**
     * Создаем валидный объект класса Price
     */
    private Price getValidPrice() {
        Price price = new Price();
        price.setId(1024);
        price.setProduct_code("Product_Code&&&");
        price.setNumber(256);
        price.setDepart(128);
        price.setBegin(new Date(2016, 1, 1, 0, 0, 0));
        price.setEnd(new Date(2016, 1, 31, 23, 59, 59));
        price.setValue(5000);
        return price;
    }

    // Сначала проверки основной логики тестового задания
        /**
         * Если значения цен одинаковы, срок действия имеющейся цены увеличивается
         * согласно периоду новой цены. Дубликаты удаляются. Если значения цен
         * отличаются, добавляется новая цена, а период действия текущей цены
         * уменьшается согласно сроку действия новой цены.
         */
        @Test
        public void equalValues() {
            Collection<Price> currentPrices = new HashSet<>(getCurrentPrices());
            Collection<Price> newPrices = new HashSet<>(getNewPrices());
            Collection<Price> result = new HashSet<>(combinator.mergePrices(currentPrices, newPrices));
            // ожидаемый результат:
            Collection<Price> expResult = new HashSet<>(getPriceUnion());

            assertEquals(expResult, result);
        }

        // первый пример из тестового задания
        @Test
        public void testEmptyCurrentPrices() {
            Collection<Price> currentPrices = new ArrayList<>();
            Collection<Price> newPrices = getNewPrices();
            Collection<Price> result = combinator.mergePrices(currentPrices, newPrices);
            // ожидаемый результат:
            Collection<Price> expResult = new ArrayList<>();
            expResult.add(new Price(0, "122856", 1, 1, new Date(2013, 1, 20, 0, 0, 0), new Date(2013, 2, 20, 23, 59, 59), 11000));
            expResult.add(new Price(1, "6654", 1, 2, new Date(2013, 1, 12, 0, 0, 0), new Date(2013, 1, 12, 23, 59, 59), 4000));
            expResult.add(new Price(2, "122856", 2, 1, new Date(2013, 1, 15, 0, 0, 0), new Date(2013, 1, 25, 23, 59, 59), 92000));

            assertEquals(expResult, result);
        }

        // второй пример из тестового задания
        @Test
        public void equalValuesForTest() {
            Collection<Price> currentPrices = new HashSet<>();
            currentPrices.add(new Price(0, "122856", 1, 1, new Date(2013, 1, 1, 0, 0, 0), new Date(2013, 1, 9, 23, 59, 59), 11000));

            Collection<Price> newPrices = new HashSet<>();
            newPrices.add(new Price(1, "122444", 2, 2, new Date(2013, 1, 1, 0, 0, 0), new Date(2013, 1, 9, 23, 59, 59), 11000));

            Collection<Price> result = new HashSet<>(combinator.mergePrices(currentPrices, newPrices));

            // ожидаемый результат:
            Collection<Price> expResult = new HashSet<>();
            expResult.add(new Price(0, "122856", 1, 1, new Date(2013, 1, 1, 0, 0, 0), new Date(2013, 1, 9, 23, 59, 59), 11000));
            expResult.add(new Price(1, "122444", 2, 2, new Date(2013, 1, 1, 0, 0, 0), new Date(2013, 1, 9, 23, 59, 59), 11000));

            assertEquals(expResult, result);
        }

        /**
         * Если в качестве текущих цен переадется null, новые цены должны быть
         * добавлены, а дубликаты удалены.
         */
        @Test
        public void mergePricesNullCurrentPrices() {
            Collection<Price> newPrices = getNewPrices();
            Collection<Price> result = new HashSet<>(combinator.mergePrices(null, newPrices));
            // ожидаемый результат:
            Collection<Price> expResult = new HashSet<>(getNewPrices());

            assertEquals(result, expResult);
        }

        /**
         * Если в качестве новых цен переадется null, метод должен вернуть текущую
         * коллекцию цен с удалением дубликатов
         */
        @Test
        public void mergePricesNullNewPrices() {
            Collection<Price> currentPrices = getCurrentPrices();
            Collection<Price> result = new HashSet<>(combinator.mergePrices(currentPrices, null));
            // ожидаемый результат:
            Collection<Price> expResult = new HashSet<>(getCurrentPrices());

            assertEquals(result, expResult);
        }

        /**
         * Если в качестве коллекций цен переадются null'ы, метод должен вернуть
         * пустую коллекцию.
         */
        @Test
        public void mergeNullPrices() {
            Collection<Price> result = new HashSet<>(combinator.mergePrices(null, null));
            // ожидаемый результат:
            Collection<Price> expResult = new HashSet<>();

            assertEquals(result, expResult);
        }


    //-------------- проверка валидности параметров объекта Price --------------------------------

        /**
         * Если начало срока действия не установлено, то должно выбрасываться исключение.
         */
        @Test(expected = IllegalArgumentException.class)
        public void illegalRangeBegin() {
            Collection<Price> currentPrices = getCurrentPrices();
            Collection<Price> newPrices = getNewPrices();
            //Добавляем цену с неустановленным началом срока действия:
            Price price = getValidPrice();
            price.setBegin(null);
            currentPrices.add(price);
            //ожидаем исключение:
            combinator.mergePrices(currentPrices, newPrices);
        }

        /**
         * Если окончание срока действия какого-либо объекта "цена" не
         * установлено, то должно выбрасываться исключение.
         */
        @Test(expected = IllegalArgumentException.class)
        public void illegalRangeEnd() {
            Collection<Price> currentPrices = getCurrentPrices();
            Collection<Price> newPrices = getNewPrices();
            //Добавляем цену с неустановленным началом срока действия:
            Price price = getValidPrice();
            price.setEnd(null);
            currentPrices.add(price);
            //ожидаем исключение:
            combinator.mergePrices(currentPrices, newPrices);
        }

        /**
         * Если срок действия какого-либо объекта "цена" не установлен, то
         * должно выбрасываться исключение.
         */
        @Test(expected = IllegalArgumentException.class)
        public void nullRange() {
            Collection<Price> currentPrices = getCurrentPrices();
            Collection<Price> newPrices = getNewPrices();
            //Добавляем цену с неустановленным началом срока действия:
            Price price = getValidPrice();
            price.setBegin(null);
            price.setEnd(null);
            currentPrices.add(price);
            //ожидаем исключение:
            combinator.mergePrices(currentPrices, newPrices);
        }

        /**
         * Если срок действия какого-либо объекта "цена" некорректен, то должно
         * выбрасываться исключение. Под корректным диапазоном понимается когда дата
         * начала диапазона больше или равна даты окончания диапазона.
         */
        @Test(expected = IllegalArgumentException.class)
        public void illegalRange() {
            Collection<Price> currentPrices = getCurrentPrices();
            Collection<Price> newPrices = getNewPrices();
            //Добавляем цену с ошибочным сроком действия:
            Price price = getValidPrice();
            price.setBegin(new Date(2016, 12, 13, 23, 59, 59));
            price.setEnd(new Date(2016, 1, 1, 0, 0, 0));
            currentPrices.add(price);
            //ожидаем исключение:
            combinator.mergePrices(currentPrices, newPrices);
        }

        /**
         * Если значение цены какого-либо объекта "цена" некорректено, то
         * выбрасывается исключение. Под корректным значением подразумевается число
         * больше нуля.
         */
        @Test(expected = IllegalArgumentException.class)
        public void illegalValues() {
            Collection<Price> currentPrices = getCurrentPrices();
            Collection<Price> newPrices = getNewPrices();
            //Добавляем цену с ошибочной ценой:
            Price price = getValidPrice();
            price.setValue(0);
            currentPrices.add(price);
            //ожидаем исключение:
            combinator.mergePrices(currentPrices, newPrices);
        }

        /**
         * Если номер цены какого-либо объекта "цена" некорректен, то выбрасывается
         * исключение. Под корректным номером подразумевается число больше нуля.
         */
        @Test(expected = IllegalArgumentException.class)
        public void illegalNumber() {
            Collection<Price> currentPrices = getCurrentPrices();
            Collection<Price> newPrices = getNewPrices();
            //Добавляем цену с ошибочным номером:
            Price price = getValidPrice();
            price.setNumber(-1);
            currentPrices.add(price);
            //ожидаем исключение:
            combinator.mergePrices(currentPrices, newPrices);
        }

        /**
         * Если номер отдела какого-либо объекта "цена" некорректен, то
         * выбрасывается исключение. Под корректным номером отдела подразумевается
         * число больше нуля.
         */
        @Test(expected = IllegalArgumentException.class)
        public void illegalDepart() {
            Collection<Price> currentPrices = getCurrentPrices();
            Collection<Price> newPrices = getNewPrices();
            //Добавляем цену с ошибочным номером отдела:
            Price price = getValidPrice();
            price.setDepart(0);
            currentPrices.add(price);
            //ожидаем исключение:
            combinator.mergePrices(currentPrices, newPrices);
        }

        /**
         * Если код товара какого-либо объекта "цена" некорректен, то выбрасывается
         * исключение.Под корректным кодом товара подразумевается строка ненулевой
         * длины.
         */
        @Test(expected = IllegalArgumentException.class)
        public void illegalProduct() {
            Collection<Price> currentPrices = getCurrentPrices();
            Collection<Price> newPrices = getNewPrices();
            //Добавляем цену с пустым кодом товара:
            Price price = getValidPrice();
            price.setProduct_code("");
            currentPrices.add(price);
            //ожидаем исключение:
            combinator.mergePrices(currentPrices, newPrices);
        }
    }