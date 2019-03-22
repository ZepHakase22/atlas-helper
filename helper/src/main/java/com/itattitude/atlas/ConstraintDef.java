package com.itattitude.atlas;

import java.io.Serializable;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.atlas.model.typedef.AtlasStructDef.AtlasConstraintDef;

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
public class ConstraintDef extends AtlasConstraintDef implements Serializable{

	private static final long serialVersionUID = 1L;
	
    @Override
	public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }

        sb.append("ConstraintDef{");
        sb.append("type='").append(super.getType()).append('\'');
        sb.append(", params='").append(super.getParams()).append('\'');
        sb.append('}');

        return sb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AtlasConstraintDef that = (AtlasConstraintDef) o;
        return Objects.equals(super.getType(), that.getType()) &&
                Objects.equals(super.getParams(), that.getParams());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getType(), super.getParams());
    }

    @Override
    public String toString() { return toString(new StringBuilder()).toString(); }

}
