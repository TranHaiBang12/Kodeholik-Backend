package com.g44.kodeholik.util.mapper;

public interface Mapper<A, B> {
    public A mapTo(B b);

    public B mapFrom(A a);
}
