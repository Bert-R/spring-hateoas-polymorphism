package nu.famroos.spring.hateoas.polymorphism.repro1;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonSubTypes({ @Type(value = Cat1.class, name = "Cat1"), @Type(value = Dog1.class, name = "Dog1") })
@JsonRootName("Animal")
public abstract class Animal1
{
	private String name;

	public Animal1()
	{
	}

	public Animal1(String name)
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
}