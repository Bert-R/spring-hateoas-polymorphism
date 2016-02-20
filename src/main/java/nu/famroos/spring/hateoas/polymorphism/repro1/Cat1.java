package nu.famroos.spring.hateoas.polymorphism.repro1;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("cat")
public class Cat1 extends Animal1
{

	private int lives;

	public Cat1()
	{
	}

	public Cat1(String name)
	{
		super(name);
	}

	public Cat1(String name, int lives)
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
		Cat1 other = (Cat1) obj;
		if (lives != other.lives)
			return false;
		return true;
	}

}