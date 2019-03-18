package com.itattitude.atlas;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.atlas.model.typedef.AtlasEnumDef.AtlasEnumElementDef;

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
public class EnumElementDef extends AtlasEnumElementDef implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public EnumElementDef() {
		super(null,null,null);
	}
	public EnumElementDef(String value, Integer ordinal) {
		super(value,null,ordinal);
	}
	public EnumElementDef(String value, String description, Integer ordinal) {
		super(value,description,ordinal);
	}
	public EnumElementDef(EnumElementDef other) {
		super(other);
	}
	
	static List<AtlasEnumElementDef> asAtlasEnumElementDef(List<EnumElementDef> e) {
		return e.stream().map(AtlasEnumElementDef::new)
		.collect(Collectors.toList());
	}
	@Override
	public StringBuilder toString(StringBuilder sb) {
		if(sb==null)
			sb=new StringBuilder();
		
		sb.append("EnelEnumDef{");
		sb.append("value='").append(getValue()).append('\'');
		sb.append("description='").append(getDescription()).append('\'');
		sb.append("ordinal='").append(getOrdinal());
		sb.append('}');
		
		return sb;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this==o) return true;
		if (o==null || this.getClass()!=o.getClass()) return false;
		return Objects.equals(getValue(), ((EnumElementDef)o).getValue()) &&
				Objects.equals(getDescription(), ((EnumElementDef)o).getDescription()) &&
				Objects.equals(this.getOrdinal(), ((EnumElementDef)o).getOrdinal());
	}
	
	@Override
	public String toString() {
		return toString(new StringBuilder()).toString();
	}


}
