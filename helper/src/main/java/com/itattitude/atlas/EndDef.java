package com.itattitude.atlas;

import java.io.Serializable;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.atlas.model.typedef.AtlasRelationshipEndDef;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.itattitude.atlas.AttributeDef.DataCatalogueCardinality;

@JsonAutoDetect(getterVisibility=Visibility.PUBLIC_ONLY, setterVisibility=Visibility.PUBLIC_ONLY, fieldVisibility=Visibility.NONE)
@JsonInclude(value=Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@XmlRootElement
@XmlAccessorType(value=XmlAccessType.PROPERTY)
public class EndDef extends AtlasRelationshipEndDef implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public EndDef(String name, String type, DataCatalogueCardinality cardinality) {
		this(name, type,cardinality, false);
	}
	
	public EndDef(String name, String type, DataCatalogueCardinality cardinality, boolean isContainer) {
		super(name, type, cardinality.toCardinality(), isContainer);
	}
	
	public StringBuilder toString(StringBuilder sb) {
		if (sb == null) {
			sb = new StringBuilder();
		}
	
		sb.append("AtlasRelationshipEndDef{");
		sb.append("type='").append(getType()).append('\'');
		sb.append(", name==>'").append(getName()).append('\'');
		sb.append(", description==>'").append(getDescription()).append('\'');
		sb.append(", isContainer==>'").append(getIsContainer()).append('\'');
		sb.append(", cardinality==>'").append(getCardinality()).append('\'');
		sb.append(", isLegacyAttribute==>'").append(getIsLegacyAttribute()).append('\'');
		sb.append('}');
		
	    return sb;
	}
	
	@Override
	public boolean equals(Object o) {
	    if (this == o)
	        return true;
	    if (o == null || getClass() != o.getClass())
	        return false;
	    EndDef that = (EndDef) o;
	
	    return Objects.equals(getType(), that.getType()) &&
	           Objects.equals(getName(), that.getName()) &&
	           Objects.equals(getDescription(), that.getDescription()) &&
	           getIsContainer() == that.getIsContainer() &&
	           getCardinality() == that.getCardinality() &&
	           getIsLegacyAttribute() == that.getIsLegacyAttribute();
	}
	
	@Override
	public int hashCode() {
	    return Objects.hash(getType(), getName(), getDescription(), getIsContainer(), getCardinality(), 
	    		getIsLegacyAttribute());
	}
	
	@Override
	public String toString() {
	    return toString(new StringBuilder()).toString();
	}

}
