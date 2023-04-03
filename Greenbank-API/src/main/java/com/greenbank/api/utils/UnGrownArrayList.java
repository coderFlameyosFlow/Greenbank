package com.greenbank.api.utils;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;

/**
 * The same as ArrayList, but the array does not grow, and there are a few additions that help performance alongside.
 *
 * @author FlameyosFlow
 * @since 1.3.0-alpha
 */
@SuppressWarnings("unchecked")
public class UnGrownArrayList<E> extends AbstractList<E> implements Serializable, List<E>, Cloneable, RandomAccess {
    private int size;

    private final Object[] elementData;

    public UnGrownArrayList(int size) {
        this.size = size;
        this.elementData = new Object[size];
    }

    public UnGrownArrayList() {
        this.size = 16;
        this.elementData = new Object[size];
    }

    public UnGrownArrayList(Collection<? extends E> collection) {
        this.size = collection.size();
        this.elementData = new Object[size];
        this.addAll(collection);
    }

    public UnGrownArrayList(int size, Collection<? extends E> collection) {
        this.size = Math.max(collection.size(), size);
        this.elementData = new Object[size];
        this.addAll(collection);
    }

    @Override
    @CheckReturnValue
    public int size() {
        return size;
    }

    @Override
    @CheckReturnValue
    public boolean isEmpty() {
        return elementData.length == 0;
    }

    @Override
    @CanIgnoreReturnValue
    public boolean addAll(Collection<? extends E> c) {
        Object[] collectionArray = c.toArray();
        ++this.modCount;
        int numNew = collectionArray.length;
        if (numNew == 0) {
            return false;
        } else {
            System.arraycopy(collectionArray, 0, this.elementData, size, numNew);
            return true;
        }
    }

    @Override
    @CheckReturnValue
    public boolean contains(Object o) {
        for (Object element : this.elementData) {
            if (element.equals(o)) return true;
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @NotNull
    @Override
    @CheckReturnValue
    public E @NotNull [] toArray() {
        return (E[]) this.elementData;
    }

    @Override
    @CanIgnoreReturnValue
    public boolean add(E e) {
        ++this.modCount;
        if (this.size == elementData.length) {
            throw new IndexOutOfBoundsException("Reached end of Array size.");
        }

        elementData[this.size] = e;
        this.size += 1;
        return false;
    }

    @Override
    @CanIgnoreReturnValue
    public boolean remove(Object o) {
        ++this.modCount;
        this.elementData[this.size -= 1] = null;
        return true;
    }

    @Override
    public void clear() {
        for (Object element : elementData) this.remove(element);
    }

    @Override
    @CheckReturnValue
    public E get(int i) {
        return (E) this.elementData[i];
    }

    @Override
    @CanIgnoreReturnValue
    public E set(int i, E e) {
        this.elementData[i] = e;
        return e;
    }

    @Override
    public void add(int index, E element) {
        System.arraycopy(elementData, index, elementData, index + 1, this.size - index);
        elementData[index] = element;
    }
}
