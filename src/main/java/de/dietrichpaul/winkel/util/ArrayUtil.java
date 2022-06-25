package de.dietrichpaul.winkel.util;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ArrayUtil {

    public static <E> List<E> addToNew(Collection<E> a, E b) {
        List<E> list = new LinkedList<>(a);
        list.add(b);
        return list;
    }

}
