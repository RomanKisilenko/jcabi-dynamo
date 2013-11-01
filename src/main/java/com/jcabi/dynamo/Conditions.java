/**
 * Copyright (c) 2012-2013, JCabi.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the jcabi.com nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jcabi.dynamo;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.jcabi.aspects.Immutable;
import com.jcabi.aspects.Loggable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * DynamoDB query conditions.
 *
 * <p>It's a convenient immutable builder of a map of conditions for
 * DynamoDB query/scan operations. Use it like this:
 *
 * <pre>Map&lt;String, Condition&gt; conditions = new Conditions()
 *   .with("hash", Conditions.equalTo("some value"))
 *   .with("range", Conditions.equalTo(12345));
 * </pre>
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 */
@Immutable
@Loggable(Loggable.DEBUG)
@EqualsAndHashCode(of = "pairs")
@SuppressWarnings({
    "PMD.TooManyMethods",
    "PMD.AvoidInstantiatingObjectsInLoops"
})
public final class Conditions implements Map<String, Condition> {

    /**
     * Serialization marker.
     */
    private static final long serialVersionUID = 0x3456998922348767L;

    /**
     * Pairs.
     */
    private final transient Object[][] pairs;

    /**
     * Public ctor.
     */
    public Conditions() {
        this(new HashMap<String, Condition>(0));
    }

    /**
     * Public ctor.
     * @param map Map of them
     */
    public Conditions(@NotNull final Map<String, Condition> map) {
        this.pairs = new Object[map.size()][];
        int pos = 0;
        for (Map.Entry<String, Condition> entry : map.entrySet()) {
            this.pairs[pos] = new Object[] {entry.getKey(), entry.getValue()};
            ++pos;
        }
    }

    /**
     * Equal to static condition builder (factory method).
     * @param value The value to equal to
     * @return The condition just created
     */
    @NotNull
    public static Condition equalTo(@NotNull final Object value) {
        return new Condition()
            .withAttributeValueList(new AttributeValue(value.toString()))
            .withComparisonOperator(ComparisonOperator.EQ);
    }

    /**
     * With this condition.
     * @param name Attribute name
     * @param value The condition
     * @return New map of conditions
     */
    @NotNull
    public Conditions with(@NotNull final String name,
        @NotNull final Condition value) {
        final ConcurrentMap<String, Condition> map =
            new ConcurrentHashMap<String, Condition>(
                this.pairs.length + 1
            );
        map.putAll(this);
        map.put(name, value);
        return new Conditions(map);
    }

    /**
     * With these conditions.
     * @param conds The conditions
     * @return New map of conditions
     */
    @NotNull
    public Conditions with(@NotNull final Map<String, Condition> conds) {
        final ConcurrentMap<String, Condition> map =
            new ConcurrentHashMap<String, Condition>(
                this.pairs.length + conds.size()
            );
        map.putAll(this);
        map.putAll(conds);
        return new Conditions(map);
    }

    @Override
    public String toString() {
        final Collection<String> terms =
            new ArrayList<String>(this.pairs.length);
        for (Object[] pair : this.pairs) {
            final Condition condition = Condition.class.cast(pair[1]);
            terms.add(
                String.format(
                    "%s %s %s",
                    pair[0],
                    condition.getComparisonOperator(),
                    condition.getAttributeValueList()
                )
            );
        }
        return StringUtils.join(terms, " AND ");
    }

    @Override
    public int size() {
        return this.pairs.length;
    }

    @Override
    public boolean isEmpty() {
        return this.pairs.length == 0;
    }

    @Override
    public boolean containsKey(final Object key) {
        return this.keySet().contains(key.toString());
    }

    @Override
    public boolean containsValue(final Object value) {
        return this.values().contains(Condition.class.cast(value));
    }

    @Override
    public Condition get(final Object key) {
        Condition value = null;
        for (Map.Entry<String, Condition> entry : this.entrySet()) {
            if (entry.getKey().equals(key)) {
                value = entry.getValue();
                break;
            }
        }
        return value;
    }

    @Override
    public Set<String> keySet() {
        final Set<String> keys = new HashSet<String>(this.pairs.length);
        for (Object[] pair : this.pairs) {
            keys.add(pair[0].toString());
        }
        return keys;
    }

    @Override
    public Collection<Condition> values() {
        final Collection<Condition> values =
            new ArrayList<Condition>(this.pairs.length);
        for (Object[] pair : this.pairs) {
            values.add(Condition.class.cast(pair[1]));
        }
        return values;
    }

    @Override
    public Set<Map.Entry<String, Condition>> entrySet() {
        final Set<Map.Entry<String, Condition>> entries =
            new HashSet<Map.Entry<String, Condition>>(this.pairs.length);
        for (Object[] pair : this.pairs) {
            entries.add(
                new HashMap.SimpleImmutableEntry<String, Condition>(
                    pair[0].toString(),
                    Condition.class.cast(pair[1])
                )
            );
        }
        return entries;
    }

    @Override
    public Condition put(final String key, final Condition value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Condition remove(final Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(
        final Map<? extends String, ? extends Condition> map) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

}
