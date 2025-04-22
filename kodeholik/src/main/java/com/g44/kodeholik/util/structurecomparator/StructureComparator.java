package com.g44.kodeholik.util.structurecomparator;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class StructureComparator {
    public static boolean haveSameStructure(Object obj1, Object obj2) {
        log.info(obj1 + " " + obj1.getClass());
        log.info(obj2 + " " + obj2.getClass());
        if (obj1 == null || obj2 == null)
            return obj1 == obj2;

        if (obj1 instanceof Map && obj2 instanceof Map) {
            Map<?, ?> map1 = (Map<?, ?>) obj1;
            Map<?, ?> map2 = (Map<?, ?>) obj2;

            if (!map1.keySet().equals(map2.keySet()))
                return false;

            for (Object key : map1.keySet()) {
                if (!haveSameStructure(map1.get(key), map2.get(key)))
                    return false;
            }
            return true;
        }

        if (obj1 instanceof List && obj2 instanceof List) {
            List<?> list1 = (List<?>) obj1;
            List<?> list2 = (List<?>) obj2;

            if (list1.isEmpty() || list2.isEmpty()) {
                return true;
            }
            return haveSameStructure(list1.get(0), list2.get(0));
        }

        return obj1.getClass().equals(obj2.getClass());
    }
}
