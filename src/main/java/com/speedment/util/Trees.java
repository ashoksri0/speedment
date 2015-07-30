/**
 *
 * Copyright (c) 2006-2015, Speedment, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); You may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.speedment.util;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 *
 * @author pemi
 */
public class Trees implements PureStatic {

    private Trees() {
        instanceNotAllowed();
    }

    public static enum WalkingOrder {

        FORWARD, BACKWARD;
    }

    /**
     * The TraversalType can control how a tree is walked.
     *
     * http://en.wikipedia.org/wiki/Tree_traversal
     *
     */
    public static enum TraversalOrder {

        DEPTH_FIRST_PRE, /*DEPTH_FIRST_IN, Supported only for left/right trees*/ DEPTH_FIRST_POST, BREADTH_FIRST;
    }

    public static <T> Stream<? extends T> walk(T first, Function<T, T> traverser) {
        return walk(first, traverser, WalkingOrder.FORWARD, Stream.builder()).build();
    }

    public static <T> Stream<? extends T> walk(T first, Function<T, T> traverser, WalkingOrder order) {
        return walk(first, traverser, order, Stream.builder()).build();
    }

    public static <T> Stream<? extends T> walkOptional(T first, Function<T, Optional<T>> traverser) {
        return walkOptional(first, traverser, WalkingOrder.FORWARD, Stream.builder()).build();
    }

    public static <T> Stream<? extends T> walkOptional(T first, Function<T, Optional<T>> traverser, WalkingOrder order) {
        return walkOptional(first, traverser, order, Stream.builder()).build();
    }

    public static <T> Stream<? extends T> traverse(T first, Function<T, Stream<T>> traverser, TraversalOrder traversalOrder) {
        if (traversalOrder == TraversalOrder.BREADTH_FIRST) {
            return traverseBredthFirst(first, traverser, Stream.builder()).build();
        } else {
            return traverse(first, traverser, traversalOrder, Stream.builder()).build();
        }
    }

    //
    // Private support methods
    //
    private static <T> Stream.Builder<? extends T> walkOptional(T first, Function<T, Optional<T>> traverser, WalkingOrder order, Stream.Builder<T> builder) {
        if (order == WalkingOrder.FORWARD) {
            builder.add(first);
        }
        traverser.apply(first).ifPresent(p -> walkOptional(p, traverser, order, builder));
        if (order == WalkingOrder.BACKWARD) {
            builder.add(first);
        }
        return builder;
    }

    private static <T> Stream.Builder<? extends T> walk(T first, Function<T, T> traverser, WalkingOrder order, Stream.Builder<T> builder) {
        if (order == WalkingOrder.FORWARD) {
            builder.add(first);
        }
        final T next = traverser.apply(first);
        if (next != null) {
            walk(next, traverser, order, builder);
        }
        if (order == WalkingOrder.BACKWARD) {
            builder.add(first);
        }
        return builder;
    }

    private static <T> Stream.Builder<? extends T> traverse(T first, Function<T, Stream<T>> traverser, TraversalOrder traversalOrder, Stream.Builder<T> builder) {
        if (first == null) {
            return builder;
        }
        if (traversalOrder == TraversalOrder.DEPTH_FIRST_PRE) {
            builder.add(first);
        }
        final Stream<T> next = traverser.apply(first);
        if (next != null) {
            next.filter(Objects::nonNull).forEach((T n) -> {
                traverse(n, traverser, traversalOrder, builder);
            });
        }
        if (traversalOrder == TraversalOrder.DEPTH_FIRST_POST) {
            builder.add(first);
        }
        return builder;
    }

    private static <T> Stream.Builder<? extends T> traverseBredthFirst(T first, Function<T, Stream<T>> traverser, Stream.Builder<T> builder) {
        if (first == null) {
            return builder;
        }
        final Queue<T> q = new ArrayDeque<>();
        q.add(first);
        while (!q.isEmpty()) {
            final T node = q.poll();
            builder.add(node);
            traverser
                .apply(node)
                .filter(Objects::nonNull)
                .forEach(q::add);
        }
        return builder;
    }

}
