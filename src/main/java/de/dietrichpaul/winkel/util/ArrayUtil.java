package de.dietrichpaul.winkel.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ArrayUtil {

    @SafeVarargs
    public static <E> List<E> addToNew(Collection<E> a, E... b) {
        List<E> list = new LinkedList<>(a);
        list.addAll(Arrays.asList(b));
        return list;
    }

}
