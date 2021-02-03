package com.santander.fx;

import java.math.BigDecimal;

public interface Margin {

    BigDecimal apply( BigDecimal input);
}
