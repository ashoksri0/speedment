/**
 * 
 * Copyright (c) 2006-2016, Speedment, Inc. All Rights Reserved.
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
package com.speedment.runtime.field;

import com.speedment.runtime.config.identifier.ColumnIdentifier;
import com.speedment.runtime.field.internal.DoubleFieldImpl;
import com.speedment.runtime.field.method.DoubleGetter;
import com.speedment.runtime.field.method.DoubleSetter;
import com.speedment.runtime.field.trait.HasComparableOperators;
import com.speedment.runtime.field.trait.HasDoubleValue;
import com.speedment.runtime.typemapper.TypeMapper;
import javax.annotation.Generated;

/**
 * A field that represents a primitive {@code double} value.
 * 
 * @param <ENTITY> entity type
 * @param <D>      database type
 * 
 * @author Emil Forslund
 * @since  3.0.0
 * 
 * @see ReferenceField
 */
@Generated(value = "Speedment")
public interface DoubleField<ENTITY, D> extends Field<ENTITY>, HasDoubleValue<ENTITY, D>, HasComparableOperators<ENTITY, Double> {
    
    /**
     * Creates a new {@link DoubleField} using the default implementation.
     * 
     * @param <ENTITY>   entity type
     * @param <D>        database type
     * @param identifier column that this field represents
     * @param getter     method reference to getter in entity
     * @param setter     method reference to setter in entity
     * @param typeMapper type mapper that is applied
     * @param unique     if column only contains unique values
     * @return           the created field
     */
    static <ENTITY, D> DoubleField<ENTITY, D> create(ColumnIdentifier<ENTITY> identifier, DoubleGetter<ENTITY> getter, DoubleSetter<ENTITY> setter, TypeMapper<D, Double> typeMapper, boolean unique) {
        return new DoubleFieldImpl<>(
            identifier, getter, setter, typeMapper, unique
        );
    }
}