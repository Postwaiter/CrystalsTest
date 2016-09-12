package main.prices;

import java.util.*;

/**
 * Created by Savchenko A. on 11.09.2016.
 */
public class PriceCombinator {

    private List<PriceEntry> oldPriceEntries = new ArrayList<>();
    private List<PriceEntry> newPriceEntries = new ArrayList<>();
    private List<Price> generatedPrices = new ArrayList<>();

    /**
     * Метод производит объединение коллекций по логике,
     * описанной тестовом задании.
     */
    public Collection<Price> mergePrices(Collection<Price> current,
                                          Collection<Price> added) {

        // Избавляемся от дубликатов:
        current = delDuplicates(current);
        added = delDuplicates(added);

        // Конвертируем цены в записи
        oldPriceEntries = PriceEntry.getEntries(current);
        newPriceEntries = PriceEntry.getEntries(added);

        List<Price> prices = new ArrayList<>();

        // Если нечего обновлять, то просто возвращаем список цен для добавления
        if (oldPriceEntries.isEmpty()) {
            prices.addAll(added);
            return prices;
        }

        for (PriceEntry pNew : newPriceEntries) {
            for (PriceEntry pOld : oldPriceEntries) {
                priceProcessing(pOld, pNew);
            }
        }
        updateOldPriceEntries();

        return PriceEntry.getPrices(oldPriceEntries);
    }


    private void updateOldPriceEntries() {
        for (PriceEntry price : newPriceEntries) {
            if (price.getOption() != PriceEntry.Option.OLD) {
                oldPriceEntries.add(price);
            }
        }
        oldPriceEntries.addAll(PriceEntry.getEntries(generatedPrices));
    }

    /**
     * Проверка валидности цены. В случае некорректных параметров цены
     * выбрасывается исключение IllegalArgumentException.
     *
     */
    private void priceValidation(Collection<Price> prices) {
        for (Price price : prices) {
            if (!price.isValid()) {
                throw new IllegalArgumentException("Некорректные параметры объекта: "
                        + price);
            }
        }
    }

    /**
     * Удаляем дубликаты. Бросаем IllegalArgumentException в случае, если параметры цены некорректны.
     */
    private Set<Price> delDuplicates(Collection<Price> prices) {
        if (prices == null) {
            return new HashSet<>();
        }
        priceValidation(prices);
        return new HashSet<>(prices);
    }

    /**
     * Метод сравнивает параметры двух цен. Если код, номер, отдел старой цены
     * равны соответствующим параметрам новой цены, то возвращается true.
     */
    protected boolean equalPriceParameters(Price pOld, Price pNew) {
        if (pOld == null || pNew == null) {
            return false;
        }

        String code_a = pOld.getProduct_code();
        String code_b = pNew.getProduct_code();

        if (code_a == null || code_b == null || !code_a.equals(code_b)) {
            return false;
        }

        if (pOld.getNumber() != pNew.getNumber()) {
            return false;
        }

        return pOld.getDepart() == pNew.getDepart();
    }

    /**
     * Общий метод обработки цен.
     */
    private void priceProcessing(PriceEntry pOld, PriceEntry pNew) {
        if (equalPriceParameters(pOld.getPrice(), pNew.getPrice())) {
            IntersectionType intersectType = getIntersectionType(pOld.getPrice(), pNew.getPrice());
            if (intersectType != IntersectionType.NONE && pOld.getValue() == pNew.getValue()) {
                priceUnion(pOld, pNew);
                return;
            }
            switch (intersectType) {
                case NONE:
                    // NONE
                    break;
                case OLD_INSIDE_NEW:
                    priceUnion(pOld, pNew);
                    break;
                case NEW_INSIDE_OLD:
                    pricesInRange(pOld, pNew);
                    break;
                case OLD_BEFORE:
                    System.out.println("OLD_LEFT");
                    pricesOffsetLeftRange(pOld, pNew);
                    break;
                case OLD_AFTER:
                    System.out.println("OLD_RIGHT");
                    pricesOffsetRightRange(pOld, pNew);
                    break;
                default:
                    throw new AssertionError();
            }
        }
    }

    /**
     * Объединение старой и новой цены. Период действия
     * текущей цены увеличивается согласно периоду новой цены;
     */
    protected void priceUnion(PriceEntry pOld, PriceEntry pNew) {
        Date pOldBegin = pOld.getBegin();
        Date pOldEnd = pOld.getEnd();
        Date pNewBegin = pNew.getBegin();
        Date pNewEnd = pNew.getEnd();

        if (pOldBegin.after(pNewBegin)) {
            pOld.setBegin(pNew.getBegin());
        }

        if (pOldEnd.before(pNewEnd)) {
            pOld.setEnd(pNew.getEnd());
        }
        // Помечаем как отработанную.
        pNew.setOption(PriceEntry.Option.OLD);

    }

    /**
     * Получаем тип пересечения сроков действия двух цен
     */
    protected IntersectionType getIntersectionType(Price pOld, Price pNew) {
        Date pOldBegin = pOld.getBegin();
        Date pOldEnd = pOld.getEnd();
        Date pNewBegin = pNew.getBegin();
        Date pNewEnd = pNew.getEnd();

        // Срок действия старой цены лежит внутри срока действия новой цены
        if (pOldBegin.after(pNewBegin) && pOldEnd.before(pNewEnd)) {
            return IntersectionType.OLD_INSIDE_NEW;
        }

        // Срок действия новой цены лежит внутри срока действия старой цены
        if (pNewBegin.after(pOldBegin) && pNewEnd.before(pOldEnd)) {
            return IntersectionType.NEW_INSIDE_OLD;
        }

        // Срок действия старой цены находится левее срока действия новой цены
        if (pOldBegin.before(pNewBegin) && pOldEnd.after(pNewBegin) && pOldEnd.before(pNewEnd)) {
            return IntersectionType.OLD_BEFORE;
        }

        // Срок действия старой цены находится правее срока действия новой цены
        if (pNewBegin.before(pOldBegin) && pNewEnd.after(pOldBegin) && pNewEnd.before(pOldEnd)) {
            return IntersectionType.OLD_AFTER;
        }
        return IntersectionType.NONE;
    }

    /**
     * Если срок действия новой цены лежит внутри срока действия старой цены, то
     * разрезаем старую цену на две части.
     *
     */
    protected void pricesInRange(PriceEntry pOld, PriceEntry pNew) {
        // Создание цены "справа" по сроку действия
        Price right = new Price(pOld.getPrice());
        right.setBegin(pNew.getEnd());
        right.setEnd(pOld.getEnd());
        generatedPrices.add(right);

        // Обрезаем старую цену
        pOld.setEnd(pNew.getBegin());

    }

    /**
     * Если срок действия старой цены смещен влево, происходит корректировка даты
     * окончания срока действия старой цены
     */
    protected void pricesOffsetLeftRange(PriceEntry pOld, PriceEntry pNew) {
        pOld.setEnd(pNew.getBegin());
    }

    /**
     * Если срок действия старой цены смещен вправо, происходит корректировка даты
     * начала срока действия старой цены
     */
    protected void pricesOffsetRightRange(PriceEntry pOld, PriceEntry pNew) {
        pOld.setBegin(pNew.getEnd());
    }
    /**
     * Тип пересечения цен
     */
    public enum IntersectionType {

        NONE, NEW_INSIDE_OLD, OLD_INSIDE_NEW, OLD_BEFORE, OLD_AFTER,
    }
}
