package com.itattitude.atlas;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.atlas.model.typedef.AtlasRelationshipDef;

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
public class RelationshipDef extends AtlasRelationshipDef implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public enum UMLCategory {
		ASSOCIATION(RelationshipCategory.ASSOCIATION),
		AGGREGATION(RelationshipCategory.AGGREGATION),
		COMPOSITION(RelationshipCategory.COMPOSITION);
		
		private final RelationshipCategory relationshipCategory;
		
		private UMLCategory(RelationshipCategory rc) {
			relationshipCategory=rc;
		}
		
		private RelationshipCategory toRelationshipCategory() {
			return this.relationshipCategory;
		}
	}
	
	public enum PropagationType {
		NONE(PropagateTags.NONE),
		ONE_TO_TWO(PropagateTags.ONE_TO_TWO),
		TWO_TO_ONE(PropagateTags.TWO_TO_ONE),
		BOTH(PropagateTags.BOTH);
		
		private final PropagateTags propagationTags;
		
		private PropagationType(PropagateTags pt) {
			propagationTags=pt;
		}
		private PropagateTags toPropagateTags() {
			return this.propagationTags;
		}

	}
	
	public RelationshipDef(String name, String description, String typeVersion, String serviceType,
			UMLCategory category, PropagationType propagationType, EndDef def1, EndDef def2) {
		super(name, description, typeVersion, serviceType, category.toRelationshipCategory(), 
				propagationType.toPropagateTags(), def1,def2 );
	}
	
	public static List<AtlasRelationshipDef> asAtlasRelationshipDef(List<RelationshipDef> s) {
		return s.stream().map(AtlasRelationshipDef::new)
		.collect(Collectors.toList());
	}

    @Override
    public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }

        sb.append("RelationshipDef{");
        super.toString(sb);
        sb.append(',');
        sb.append(super.getRelationshipCategory());
        sb.append(',');
        sb.append(super.getRelationshipLabel());
        sb.append(',');
        sb.append(super.getPropagateTags());
        sb.append(',');
        if (super.getEndDef1() != null) {
            sb.append(super.getEndDef1().toString());
        }
        else {
            sb.append(" end1 is null!");
        }
        sb.append(',');
        if (super.getEndDef2() != null) {
            sb.append(super.getEndDef2().toString());
        }
        else {
            sb.append(" end2 is null!");
        }
        sb.append('}');
        return sb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        //AttributeDefs are checked in the super
        if (!super.equals(o))
            return false;
        AtlasRelationshipDef that = (AtlasRelationshipDef) o;
        if (!Objects.equals(super.getRelationshipCategory(), that.getRelationshipCategory()))
            return false;
        if (!Objects.equals(super.getRelationshipLabel(), that.getRelationshipLabel()))
            return false;
        if (!Objects.equals(super.getPropagateTags(), that.getPropagateTags()))
            return false;
        if (!Objects.equals(super.getEndDef1(), that.getEndDef1()))
            return false;
        return (Objects.equals(super.getEndDef2(), that.getEndDef2()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), super.getRelationshipCategory(), super.getRelationshipLabel(), super.getPropagateTags(), super.getEndDef1(), 
        		super.getEndDef2());
    }

    @Override
    public String toString() {
        return toString(new StringBuilder()).toString();
    }


}
