package nu.famroos.spring.hateoas.polymorphism.workaround;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "@type")
@JsonSubTypes({ @Type(value = Cat3.class, name = "Cat3"), @Type(value = Dog3.class, name = "Dog3") })
@JsonRootName("Animal2")
public abstract class Animal3 extends ResourceSupport
{
	private String name;

	public Animal3()
	{
	}

	public Animal3(String name)
	{
		super();
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@JsonProperty(value = "@type")
	public abstract String getType();
}