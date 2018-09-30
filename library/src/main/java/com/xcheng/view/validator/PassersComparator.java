package com.xcheng.view.validator;

import java.util.Comparator;

final class PassersComparator implements Comparator<Passer> {
    @SuppressWarnings("UseCompareMethod")
    @Override
    public int compare(final Passer lhsField, final Passer rhsField) {
        int lhsOrder = lhsField.val.order();
        int rhsOrder = rhsField.val.order();
        return lhsOrder == rhsOrder
                ? 0 : lhsOrder > rhsOrder ? 1 : -1;
    }
}
