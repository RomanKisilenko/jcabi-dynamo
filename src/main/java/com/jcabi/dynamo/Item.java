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
import com.jcabi.aspects.Immutable;
import java.util.Map;
import javax.validation.constraints.NotNull;

/**
 * Immutable Amazon DynamoDB item.
 *
 * <p>The class is immutable, which means that every call to
 * {@link #put(String,AttributeValue)} or {@link #put(Attributes)} changes
 * data in Amazon, but doesn't change the object. The object will contain
 * dirty data right after PUT operation, and should not be used any more.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 */
@Immutable
public interface Item {

    /**
     * Get one attribute, fetching directly from AWS (runtime exception if
     * the attribute is absent, use {@link #has(String)} first).
     * @param name Attribute name
     * @return Value
     */
    @NotNull
    AttributeValue get(@NotNull String name);

    /**
     * Does this attribute exist?
     * @param name Attribute name
     * @return TRUE if it exists
     */
    boolean has(@NotNull String name);

    /**
     * Change one attribute, immediately saving it to AWS (all other attributes
     * will be set to NULL, except primary keys).
     *
     * <p>Data in memory will become out of sync right after a successful
     * execution of the method.
     *
     * @param name Attribute name
     * @param value Value to save
     */
    void put(@NotNull String name, @NotNull AttributeValue value);

    /**
     * Change all attributes in one call.
     *
     * <p>Data in memory will become out of sync right after a successful
     * execution of the method.
     *
     * @param attrs Attributes
     */
    void put(@NotNull Map<String, AttributeValue> attrs);

    /**
     * Get back to the frame it is from.
     * @return Frame
     */
    @NotNull
    Frame frame();

}
