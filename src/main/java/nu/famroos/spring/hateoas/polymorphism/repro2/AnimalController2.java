package nu.famroos.spring.hateoas.polymorphism.repro2;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/animals2")
public class AnimalController2
{
	private SortedMap<String, Animal2> animals = new TreeMap<>();

	public AnimalController2()
	{
		instantiateDog("nameofdog", 1.0);
		instantiateCat("nameofcat", 3);
		instantiateDog("nameofdog2", 10.2);
	}

	private Dog2 instantiateDog(String name, double barkVolume)
	{
		Dog2 dog = new Dog2(name, barkVolume);
		registerAnimal(dog);
		return dog;
	}

	private Animal2 instantiateCat(String name, int lives)
	{
		Cat2 cat = new Cat2(name, lives);
		registerAnimal(cat);
		return cat;
	}

	private void registerAnimal(Animal2 animal)
	{
		animals.put(animal.getName(), animal);
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseBody
	public HttpEntity<PagedResources<Animal2>> getAnimals(Pageable pageable,
			PagedResourcesAssembler<Animal2> pagedResourcesAssembler)
	{

		return createOKResponse(pagedResourcesAssembler.toResource(makePage(new ArrayList<>(animals.values()), pageable),
				new AnimalResourceAssembler()));
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ResponseBody
	public HttpEntity<Animal2> addAnimal(@RequestBody Animal2 animal)
	{
		animals.put(animal.getName(), animal);
		return createResponse(new AnimalResourceAssembler().toResource(animal), HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{name}", method = RequestMethod.GET)
	@ResponseBody
	public HttpEntity<Animal2> getAnimal(@PathVariable String name)
	{
		return createOKResponse(new AnimalResourceAssembler().toResource(findAnimal(name)));
	}

	private Page<Animal2> makePage(List<Animal2> animals, Pageable pageable)
	{
		return new PageImpl<Animal2>(animals, pageable, animals.size());
	}

	private Animal2 findAnimal(String name)
	{
		return animals.get(name);
	}

	private HttpEntity<PagedResources<Animal2>> createOKResponse(PagedResources<Animal2> animals)
	{
		return new ResponseEntity<PagedResources<Animal2>>(animals, HttpStatus.OK);
	}

	private HttpEntity<Animal2> createOKResponse(Animal2 animalResource)
	{
		return createResponse(animalResource, HttpStatus.OK);
	}

	private HttpEntity<Animal2> createResponse(Animal2 animalResource, HttpStatus status)
	{
		return new ResponseEntity<Animal2>(animalResource, status);
	}

	static ControllerLinkBuilder getAnimalLinkBuilder(String name)
	{
		AnimalController2 methodOn = methodOn(AnimalController2.class);
		return linkTo(methodOn.getAnimal(name));
	}

	private static class AnimalResourceAssembler extends ResourceAssemblerSupport<Animal2, Animal2>
	{
		public AnimalResourceAssembler()
		{
			super(AnimalController2.class, Animal2.class);
		}

		@Override
		public Animal2 toResource(Animal2 animal)
		{
			if (!animal.hasLinks())
			{
				ControllerLinkBuilder selfLinkBuilder = getAnimalLinkBuilder(animal.getName());
				addSelfLink(selfLinkBuilder, animal);
			}
			return animal;
		}

		@Override
		protected Animal2 instantiateResource(Animal2 animal)
		{
			return animal;
		}

		private void addSelfLink(ControllerLinkBuilder selfLinkBuilder, Animal2 animalResource)
		{
			animalResource.add(selfLinkBuilder.withSelfRel());
		}
	}
}