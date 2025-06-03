package org.example.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * List 相關工具函式
 */
public final class Lists {

    public static <E> ArrayList<E> newArrayList() {
        return new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    public static <E> ArrayList<E> newArrayList(E... elements) {
        // Avoid integer overflow when a large array is passed in
        ArrayList<E> list = new ArrayList<>();
        Collections.addAll(list, elements);
        return list;
    }

    public static <E> LinkedList<E> newLinkedList() {
        return new LinkedList<>();
    }

    @SuppressWarnings("unchecked")
    public static <E> LinkedList<E> newLinkedList(E... elements) {
        // Avoid integer overflow when a large array is passed in
        LinkedList<E> list = new LinkedList<>();
        Collections.addAll(list, elements);
        return list;
    }

    public static <T> List<T> fromSet(Set<T> set) {
        return set.stream()
                  .collect(Collectors.toList());
    }
    
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
	    Map<Object, Boolean> seen = new ConcurrentHashMap<>(); 
	    return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null; 
	}

    private Lists() {
        super();
    }

}
