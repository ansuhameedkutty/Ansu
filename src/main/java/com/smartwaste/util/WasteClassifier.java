package com.smartwaste.util;

import com.smartwaste.enums.WasteType;

public final class WasteClassifier {
    private WasteClassifier() {}

    public static WasteType classify(String description) {
        if (description == null) return WasteType.NON_BIODEGRADABLE;
        String d = description.toLowerCase();
        if (d.matches(".*(paper|cardboard|plastic bottle|plastic|glass|metal|can|tin|aluminium|aluminum|steel|jar|carton).*")) {
            return WasteType.RECYCLABLE;
        }
        if (d.matches(".*(food|peel|banana|vegetable|fruit|garden|leaves|leaf|compost|biodegradable|egg shell|coffee).*")) {
            return WasteType.BIODEGRADABLE;
        }
        return WasteType.NON_BIODEGRADABLE;
    }
}
