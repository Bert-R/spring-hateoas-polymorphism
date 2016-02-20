package nu.famroos.spring.hateoas.polymorphism.repro1;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("dog")
public class Dog1 extends Animal1
{

	private double barkVolume;

	public Dog1()
	{
	}

	public Dog1(String name)
	{
		super(name);
	}

	public Dog1(String name, double barkVolume)
	{
		super(name);
		this.barkVolume = barkVolume;
	}

	public double getBarkVolume()
	{
		return barkVolume;
	}

	public void setBarkVolume(double barkVolume)
	{
		this.barkVolume = barkVolume;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(barkVolume);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Dog1 other = (Dog1) obj;
		if (Double.doubleToLongBits(barkVolume) != Double.doubleToLongBits(other.barkVolume))
			return false;
		return true;
	}

}