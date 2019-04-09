package com.itattitude.atlas;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.atlas.model.typedef.AtlasClassificationDef;

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
public class ClassificationDef extends AtlasClassificationDef implements Serializable{

	private static final long serialVersionUID = 1L;

	static List<AtlasClassificationDef> asAtlasClassificationDef(List<ClassificationDef> s) {
		if(s.size() == 0) 
			return Collections.<AtlasClassificationDef>emptyList();
		else
			return s.stream().map(AtlasClassificationDef::new)
					.collect(Collectors.toList());
	}


}
