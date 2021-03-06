package nu.famroos.spring.hateoas.polymorphism.workaround;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("dog2")
public class Dog3 extends Animal3
{
	private double barkVolume;

	public Dog3()
	{
	}

	public Dog3(String name)
	{
		super(name);
	}

	public Dog3(String name, double barkVolume)
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
		Dog3 other = (Dog3) obj;
		if (Double.doubleToLongBits(barkVolume) != Double.doubleToLongBits(other.barkVolume))
			return false;
		return true;
	}

	@Override
	public String getType()
	{
		return "Dog3";
	}
}