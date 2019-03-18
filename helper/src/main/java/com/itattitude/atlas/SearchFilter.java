package com.itattitude.atlas;

import javax.ws.rs.core.MultivaluedMap;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

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
public class SearchFilter extends org.apache.atlas.model.SearchFilter{

	public SearchFilter(MultivaluedMap<String, String> searchParams) {
		super(searchParams);
	}

}
