package nu.famroos.spring.hateoas.polymorphism.workaround;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("cat2")
public class Cat3 extends Animal3
{
	private int lives;

	public Cat3()
	{
	}

	public Cat3(String name)
	{
		super(name);
	}

	public Cat3(String name, int lives)
	{
		super(name);
		this.lives = lives;
	}

	public int getLives()
	{
		return lives;
	}

	public void setLives(int lives)
	{
		this.lives = lives;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + lives;
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
		Cat3 other = (Cat3) obj;
		if (lives != other.lives)
			return false;
		return true;
	}

	@Override
	public String getType()
	{
		return "Cat3";
	}
}