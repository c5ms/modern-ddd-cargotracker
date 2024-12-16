package se.citerus.dddsample.domain.shared;

import jakarta.annotation.Nonnull;

import java.util.*;
import java.util.stream.Stream;

/**
 * This is a value objects container
 * When we get a collection values from a domain, the collection should not be changed, so we define a ImmutableValues for the domain collection values.
 *
 * @param <T> what the class type the values are
 */
public class ImmutableValues<T> implements Iterable<T> {

    private final List<T> values;

    public ImmutableValues(Collection<T> values) {
        this.values = Optional.ofNullable(values)
                .map(ArrayList::new)
                .orElseGet(ArrayList::new);
    }

    public static <T> ImmutableValues<T> of(Collection<T> collection) {
        return new ImmutableValues<>(collection);
    }

    public static <T> ImmutableValues<T> of(Map<?, T> map) {
        return new ImmutableValues<>(map.values());
    }

    public static <T> ImmutableValues<T> of(Set<T> set) {
        return new ImmutableValues<>(set);
    }

    public static <T> ImmutableValues<T> empty(Class<T> ignored) {
        return new ImmutableValues<>(new ArrayList<>());
    }

    @Override
    @Nonnull
    public Iterator<T> iterator() {
        return values.iterator();
    }

    public Stream<T> stream() {
        return values.stream();
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }

    public boolean isNotEmpty() {
        return !values.isEmpty();
    }

    public T get(int index) {
        return values.get(index);
    }

    public void add(T t) {
        values.add(t);
    }

    public Set<T> toSet() {
        return new HashSet<>(values);
    }

    public ArrayList<T> toList() {
        return new ArrayList<>(values);
    }

    public int size() {
        return values.size();
    }
}
