package com.xcheng.view.validator;

import java.util.Comparator;

final class PassersComparator implements Comparator<Passer> {
    @SuppressWarnings("UseCompareMethod")
    @Override
    public int compare(final Passer lhsField, final Passer rhsField) {
        int lhsOrder = lhsField.valid.order();
        int rhsOrder = rhsField.valid.order();
        return lhsOrder == rhsOrder
                ? 0 : lhsOrder > rhsOrder ? 1 : -1;
    }
}
