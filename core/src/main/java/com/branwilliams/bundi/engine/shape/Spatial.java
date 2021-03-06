package com.branwilliams.bundi.engine.shape;

public interface Spatial <Shape, ElementType> extends Iterable<ElementType> {

    boolean add(Shape shape, ElementType element);

    boolean remove(Shape shape, ElementType element);

    /**
     * Removes the shape and element matching
     * */
    boolean removeByShape(Shape shape);

    boolean removeByElement(ElementType element);

    int size();

    void clear();

    /**
     * @return Every {@link Shape} the provided shape intersects with.
     * */
    Iterable<Shape> query(Shape shape);

}
