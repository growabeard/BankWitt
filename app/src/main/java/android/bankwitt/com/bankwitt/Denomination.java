package android.bankwitt.com.bankwitt;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by chris on 10/27/2016.
 */
public class Denomination {
    private int count;
    private String displayAmount;
    private Integer value;
    private Integer id;
    private boolean changed;

    NumberFormat n = NumberFormat.getCurrencyInstance(Locale.US);

    public String getDisplayAmount() {
        return displayAmount;
    }

    public void setDisplayAmount(String thatDisplayAmount) {
        this.displayAmount = thatDisplayAmount;
    }

    public int getCount() {
        return count;
    }
    public void setValue(BigDecimal thatValue) {
        value = thatValue.multiply(new BigDecimal(100)).intValue();
    }
    public void setValue(Integer thatValue) {
        value = thatValue * 100;
    }
    public void setCount(int thatCount) {
        if(thatCount < 0) {
            this.count = 0;
        }
        this.count = thatCount;
    }


    public float getValue() {
        return Float.intBitsToFloat(value) / Float.intBitsToFloat(100);
    }



    public String computeTotal() {
        BigDecimal total = BigDecimal.valueOf(getValue()).multiply(new BigDecimal(getCount()));
        return n.format(total);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer thatId) {
        id = thatId;
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }
}
