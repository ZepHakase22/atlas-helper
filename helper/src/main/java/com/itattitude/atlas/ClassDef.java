package com.itattitude.atlas;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.atlas.model.typedef.AtlasEntityDef;

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
public class ClassDef extends AtlasEntityDef implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public enum BaseClassType {
		REFERENCEABLE("Referenceable"),
		ASSET("Asset"),
		INFRASTRUCTURE("Infrastructure"),
		DATASET("DataSet"),
		PROCESS("Process");
		
		private final String strBaseClassType;
		
		private BaseClassType(String strBaseClassType) {
			this.strBaseClassType=strBaseClassType;
		}
		
		public String toString() {
			return strBaseClassType;
		}
 	}
	
	public ClassDef(String name, String description, String typeVersion, 
			String baseType, String serviceType, List<AttributeDef> attributesDef,
			HashMap<String,String> options) {
		super(name, description, typeVersion, serviceType, AttributeDef.asAtlasAttributeDef(attributesDef),Collections.singleton(baseType));
		
	}

	static List<AtlasEntityDef> asAtlasEntityDef(List<ClassDef> s) {
		if(s.size() == 0) 
			return Collections.<AtlasEntityDef>emptyList();
		else
			return s.stream().map(AtlasEntityDef::new)
					.collect(Collectors.toList());
	}
	
	   @Override
	    public StringBuilder toString(StringBuilder sb) {
	        if (sb == null) {
	            sb = new StringBuilder();
	        }

	        sb.append("ClassDef{");
	        super.toString(sb);
	        sb.append(", superTypes=[");
	        dumpObjects(super.getSuperTypes(), sb);
	        sb.append("]");
	        sb.append('}');

	        return sb;
	    }

	    @Override
	    public boolean equals(Object o) {
	        if (this == o) { return true; }
	        if (o == null || getClass() != o.getClass()) { return false; }
	        if (!super.equals(o)) { return false; }

	        AtlasEntityDef that = (AtlasEntityDef) o;
	        return Objects.equals(super.getSuperTypes(), that.getSuperTypes());
	    }

	    @Override
	    public int hashCode() {
	        return Objects.hash(super.hashCode(), super.getSuperTypes());
	    }

	    @Override
	    public String toString() {
	        return toString(new StringBuilder()).toString();
	    }

}
