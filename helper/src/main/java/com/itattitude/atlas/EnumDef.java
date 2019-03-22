package com.itattitude.atlas;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.atlas.model.typedef.AtlasEnumDef;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonAutoDetect(getterVisibility=Visibility.PUBLIC_ONLY, setterVisibility=Visibility.PUBLIC_ONLY, fieldVisibility=Visibility.NONE)
@JsonInclude(value=Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@XmlRootElement
@XmlAccessorType(value=XmlAccessType.PROPERTY)
public class EnumDef extends AtlasEnumDef implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public EnumDef(String name, String description, String typeVersion, 
			String serviceType, List<EnumElementDef> enumElementsDef) {
    	super(name,description,typeVersion,EnumElementDef.asAtlasEnumElementDef(enumElementsDef),
    			null,serviceType,null);
	}

	static List<AtlasEnumDef> asAtlasEnumDef(List<EnumDef> e) {
		return e.stream().map(AtlasEnumDef::new)
		.collect(Collectors.toList());
	}
	
	@Override
    public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }

        sb.append("EnumDef{");
        super.toString(sb);
        sb.append(", elementDefs=[");
        dumpObjects(getElementDefs(), sb);
        sb.append("]");
        sb.append(", defaultValue {");
        sb.append(getDefaultValue());
        sb.append('}');
        sb.append('}');

        return sb;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EnumDef that = (EnumDef) o;
        return Objects.equals(getElementDefs(), that.getElementDefs()) &&
                Objects.equals(getDefaultValue(), that.getDefaultValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getElementDefs(), getDefaultValue());
    }
	
}
