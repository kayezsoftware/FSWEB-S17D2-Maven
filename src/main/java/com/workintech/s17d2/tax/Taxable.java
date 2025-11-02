package com.workintech.s17d2.tax;

public interface Taxable {
    Double getSimpleTaxRate();  // 15
    Double getMiddleTaxRate();  // 25
    Double getUpperTaxRate();   // 35
}