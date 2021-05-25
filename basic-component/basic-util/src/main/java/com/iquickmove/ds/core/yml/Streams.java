package com.iquickmove.ds.core.yml;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Streams {

	/**
     * Returns the element in the stream, if the stream has exactly one element.
	 * Otherwise returns null
	 */
	public static <T> T getSingle(Stream<T> stream) {
		ArrayList<T> els = stream
				.limit(2) //Don't need more than 2 to know there is more than 1
				.collect(Collectors.toCollection(() -> new ArrayList<>(2)));
		return els.size()==1 ? els.get(0) : null;
	}

	/**
	 * Like java.util.Stream.of but returns Stream.empty if the element is null
	 */
	public static <T> Stream<T> fromNullable(T e) {
		return e==null ? Stream.empty() : Stream.of(e);
	}

}