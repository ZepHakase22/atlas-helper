package com.itattitude.atlas;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.atlas.model.typedef.AtlasStructDef.AtlasAttributeDef;
import org.apache.atlas.model.typedef.AtlasStructDef.AtlasConstraintDef;
import org.apache.commons.collections.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonAutoDetect(getterVisibility=Visibility.PUBLIC_ONLY, setterVisibility=Visibility.PUBLIC_ONLY, fieldVisibility=Visibility.NONE)
@JsonInclude(value=Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@XmlRootElement
@XmlAccessorType(value=XmlAccessType.PROPERTY)
public class AttributeDef extends AtlasAttributeDef implements Serializable{

	private static final long serialVersionUID = 1L;
	public enum DataCatalogueCardinality { 
		SINGLE (Cardinality.SINGLE) , 
		LIST (Cardinality.LIST), 
		SET (Cardinality.SET);
		
		private final Cardinality cardinality;
		
		private DataCatalogueCardinality(Cardinality cardinality) {
			this.cardinality=cardinality;
		}
		
		public Cardinality toCardinality() {
			return cardinality;
		}
	}
	
	public AttributeDef(String name, String typeName, boolean isOptional, DataCatalogueCardinality cardinality, 
			boolean isUnique, boolean isIndexable) {
		super(name,typeName, isOptional, cardinality.toCardinality(),-1,-1,isUnique,isIndexable, false, null);
	}

	public AttributeDef(String name, String typeName, String description, boolean isOptional, DataCatalogueCardinality cardinality, 
			boolean isUnique, boolean isIndexable) {
		super(name,typeName, isOptional, cardinality.toCardinality(),-1,-1,isUnique,isIndexable, false, null, null, null, description);
	}
	
	public AttributeDef(AttributeDef that) {
		super(that.getName(),that.getTypeName(),that.getIsOptional(), that.getCardinality(), 
				that.getValuesMinCount(),that.getValuesMaxCount(),that.getIsUnique(),
				that.getIsIndexable(),that.getIncludeInNotification(),that.getDefaultValue(),
				that.getConstraints(),that.getOptions(),that.getDescription()
			);
	}
	static List<AtlasAttributeDef> asAtlasAttributeDef(List<AttributeDef> e) {
		return e.stream().map(AtlasAttributeDef::new)
		.collect(Collectors.toList());
	}
	
	@Override
    public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }

        sb.append("AttributeDef{");
        sb.append("name='").append(super.getName()).append('\'');
        sb.append(", typeName='").append(super.getTypeName()).append('\'');
        sb.append(", description='").append(super.getDescription()).append('\'');
        sb.append(", getIsOptional=").append(super.getIsOptional());
        sb.append(", cardinality=").append(super.getCardinality());
        sb.append(", valuesMinCount=").append(super.getValuesMinCount());
        sb.append(", valuesMaxCount=").append(super.getValuesMaxCount());
        sb.append(", isUnique=").append(super.getIsUnique());
        sb.append(", isIndexable=").append(super.getIsIndexable());
        sb.append(", includeInNotification=").append(super.getIncludeInNotification());
        sb.append(", defaultValue=").append(super.getDefaultValue());
        sb.append(", options='").append(super.getOptions()).append('\'');
        sb.append(", constraints=[");
        if (CollectionUtils.isNotEmpty(super.getConstraints())) {
            int i = 0;
            for (AtlasConstraintDef constraintDef : super.getConstraints()) {
                constraintDef.toString(sb);
                if (i > 0) {
                    sb.append(", ");
                }
                i++;
            }
        }
        sb.append("]");
        sb.append('}');

        return sb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AtlasAttributeDef that = (AtlasAttributeDef) o;
        return super.getIsOptional() == that.getIsOptional() &&
                super.getValuesMinCount() == that.getValuesMinCount() &&
                super.getValuesMaxCount() == that.getValuesMaxCount() &&
                super.getIsUnique() == that.getIsUnique() &&
                super.getIsIndexable() == that.getIsIndexable() &&
                super.getIncludeInNotification() == that.getIncludeInNotification() &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getTypeName(), that.getTypeName()) &&
                super.getCardinality() == that.getCardinality() &&
                Objects.equals(super.getDefaultValue(), that.getDefaultValue()) &&
                Objects.equals(super.getDescription(), that.getDescription()) &&
                Objects.equals(super.getConstraints(), that.getConstraints()) &&
                Objects.equals(super.getOptions(), that.getOptions());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getName(), super.getTypeName(), super.getIsOptional(), super.getCardinality(), super.getValuesMinCount(), super.getValuesMaxCount(), super.getIsUnique(), super.getIsIndexable(), super.getIncludeInNotification(), super.getDefaultValue(), super.getConstraints(), super.getOptions(), super.getDescription());
    }

    @Override
    public String toString() {
        return toString(new StringBuilder()).toString();
    }
}
