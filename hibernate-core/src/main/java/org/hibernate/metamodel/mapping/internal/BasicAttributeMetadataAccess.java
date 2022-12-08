/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.metamodel.mapping.internal;

import org.hibernate.engine.spi.CascadeStyle;
import org.hibernate.engine.spi.CascadeStyles;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.Value;
import org.hibernate.metamodel.mapping.AttributeMetadata;
import org.hibernate.metamodel.mapping.AttributeMetadataAccess;
import org.hibernate.metamodel.mapping.EntityMappingType;
import org.hibernate.property.access.spi.PropertyAccess;
import org.hibernate.type.descriptor.java.MutabilityPlan;

public final class BasicAttributeMetadataAccess implements AttributeMetadataAccess, AttributeMetadata {

	private final PropertyAccess propertyAccess;
	private final MutabilityPlan<?> mutabilityPlan;
	private final boolean nullable;
	private final boolean insertable;
	private final boolean updateable;
	private final boolean includeInOptimisticLocking;
	private final CascadeStyle cascadeStyle;

	public BasicAttributeMetadataAccess(
			PropertyAccess propertyAccess,
			MutabilityPlan mutabilityPlan,
			Property bootProperty,
			Value value
			) {
		this(
				propertyAccess,
				mutabilityPlan,
				value.isNullable(),
				bootProperty.isInsertable(),
				bootProperty.isUpdateable(),
				bootProperty.isOptimisticLocked()
		);
	}

	public BasicAttributeMetadataAccess(
			PropertyAccess propertyAccess,
			MutabilityPlan mutabilityPlan,
			boolean nullable, boolean insertable, boolean updateable, boolean includeInOptimisticLocking) {
		this(
				propertyAccess,
				mutabilityPlan,
				nullable,
				insertable,
				updateable,
				includeInOptimisticLocking,
				CascadeStyles.NONE // default - but beware of comment on AttributeMetadata#getCascadeStyle having a TODO
		);
	}

	public BasicAttributeMetadataAccess(
			PropertyAccess propertyAccess,
			MutabilityPlan mutabilityPlan,
			boolean nullable,
			boolean insertable,
			boolean updateable,
			boolean includeInOptimisticLocking,
			CascadeStyle cascadeStyle) {
		this.propertyAccess = propertyAccess;
		this.mutabilityPlan = mutabilityPlan;
		this.nullable = nullable;
		this.insertable = insertable;
		this.updateable = updateable;
		this.includeInOptimisticLocking = includeInOptimisticLocking;
		this.cascadeStyle = cascadeStyle;
	}

	@Override
	public AttributeMetadata resolveAttributeMetadata(EntityMappingType entityMappingType) {
		return this;
	}

	@Override
	public PropertyAccess getPropertyAccess() {
		return propertyAccess;
	}

	@Override
	public MutabilityPlan getMutabilityPlan() {
		return mutabilityPlan;
	}

	@Override
	public boolean isNullable() {
		return nullable;
	}

	@Override
	public boolean isInsertable() {
		return insertable;
	}

	@Override
	public boolean isUpdatable() {
		return updateable;
	}

	@Override
	public boolean isIncludedInDirtyChecking() {
		// todo (6.0) : do not believe this is correct
		return updateable;
	}

	@Override
	public boolean isIncludedInOptimisticLocking() {
		return includeInOptimisticLocking;
	}

	@Override
	public CascadeStyle getCascadeStyle() {
		return cascadeStyle;
	}

}