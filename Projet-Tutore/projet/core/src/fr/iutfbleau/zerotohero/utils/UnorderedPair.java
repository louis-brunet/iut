package fr.iutfbleau.zerotohero.utils;

import java.util.Objects;

/**
 * An unordered pair of two elements.
 * @param <E> the elements' class
 */
public class UnorderedPair<E> {
    /**
     * An element in this pair.
     */
    private final E e1;

    /**
     * An element in this pair.
     */
    private final E e2;

    /**
     * Constructs a pair containing the given elements.
     *
     * @param e1 an element to be contained in this pair
     * @param e2 an element to be contained in this pair
     */
    public UnorderedPair(E e1, E e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    /**
     * Returns true if this pair contains the given object, false otherwise.
     *
     * @param e the element to test if is contained by this pair
     * @return true if this pair contains the given object, false otherwise
     */
    public boolean contains(Object e) {
        return Objects.equals(e, e1) || Objects.equals(e, e2);
    }

    /**
     * If the given object is contained in this pair, return the other element in this
     * pair.
     *
     * @param e an element in this pair
     * @return the other element
     * @throws IllegalArgumentException if the given element is not in this pair
     */
    public E getOther(E e) {
        if ( ! this.contains(e) )
            throw new IllegalArgumentException("Given element not contained.");

        return Objects.equals(e, e1) ? e2 : e1;
    }

    /**
     * Return true if the given object is a pair containing the same elements as this
     * pair.
     *
     * @param obj the other object
     * @return true if the object is a pair containing the same objects as this pair.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if ( ! (obj instanceof UnorderedPair<?>) ) return false;

        UnorderedPair<?> other = (UnorderedPair<?>) obj;
        return this.contains(other.e1) && this.contains(other.e2);
//        return Objects.equals(e1, other.e1) && Objects.equals(e2, other.e2)
//               || Objects.equals(e1, other.e2) && Objects.equals(e2, other.e1);
    }
}
