package android.bankwitt.com.bankwitt;

import android.provider.BaseColumns;

/**
 * Created by chris on 10/31/2016.
 */

public class DenominationContract {

    private DenominationContract(){};

    public static class DenominationEntry implements BaseColumns {
        public static final String TABLE_NAME = "denomination";
        public static final String COUNT_TITLE = "count";
        public static final String COUNT_SUBTITLE = "number of this denomination";
        public static final String VALUE_TITLE = "value";
        public static final String VALUE_SUBTITLE = "how much this denomination is worth";
        public static final String DISPLAY_AMOUNT_TITLE = "display_amount";
        public static final String DISPLAY_AMOUNT_SUBTITLE = "pretty print version of denomination";
    }
}
