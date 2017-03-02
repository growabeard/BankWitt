package android.bankwitt.com.bankwitt;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    List<Denomination> denomList = new ArrayList<Denomination>();


    @Before
    public void setUp() {
        generateDenomList();
    }
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("android.bankwitt.com.bankwitt", appContext.getPackageName());
    }

    @Test
    public void insertInCorrectPlace_shouldBeSecond() {
        DenominationAdapter adapt = new DenominationAdapter(denomList);

        Denomination newDenom = new Denomination();
        newDenom.setCount(1);
        newDenom.setId(11111);
        newDenom.setValue(new BigDecimal(0.50));
        newDenom.setDisplayAmount("$0.50");
        adapt.addDenominationInRightPlace(newDenom);

        assertEquals(newDenom.getId(), adapt.getAllCurrentDatasets().get(2).getId());
    }

    private void generateDenomList() {
        Denomination firstDenom = new Denomination();
        firstDenom.setCount(2);
        firstDenom.setDisplayAmount("$0.25");
        firstDenom.setValue(new BigDecimal(0.25));
        firstDenom.setId(12345);
        Denomination secondDenom = new Denomination();
        secondDenom.setCount(3);
        secondDenom.setDisplayAmount("$0.75");
        secondDenom.setValue(new BigDecimal(0.75));
        secondDenom.setId(12346);
        Denomination thirdDenom = new Denomination();
        thirdDenom.setCount(1);
        thirdDenom.setDisplayAmount("$1.00");
        thirdDenom.setValue(new BigDecimal(1.00));
        thirdDenom.setId(12356);
        denomList.add(firstDenom);
        denomList.add(secondDenom);
        denomList.add(thirdDenom);
    }
}
