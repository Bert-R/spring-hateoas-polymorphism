package nu.famroos.spring.hateoas.polymorphism.workaround;

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
@RequestMapping(value = "/animals3")
public class AnimalController3
{
	private SortedMap<String, Animal3> animals = new TreeMap<>();

	public AnimalController3()
	{
		instantiateDog("nameofdog", 1.0);
		instantiateCat("nameofcat", 3);
		instantiateDog("nameofdog2", 10.2);
	}

	private Dog3 instantiateDog(String name, double barkVolume)
	{
		Dog3 dog = new Dog3(name, barkVolume);
		registerAnimal(dog);
		return dog;
	}

	private Animal3 instantiateCat(String name, int lives)
	{
		Cat3 cat = new Cat3(name, lives);
		registerAnimal(cat);
		return cat;
	}

	private void registerAnimal(Animal3 animal)
	{
		animals.put(animal.getName(), animal);
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseBody
	public HttpEntity<PagedResources<Animal3>> getAnimals(Pageable pageable,
			PagedResourcesAssembler<Animal3> pagedResourcesAssembler)
	{

		return createOKResponse(pagedResourcesAssembler.toResource(makePage(new ArrayList<>(animals.values()), pageable),
				new AnimalResourceAssembler()));
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ResponseBody
	public HttpEntity<Animal3> addAnimal(@RequestBody Animal3 animal)
	{
		animals.put(animal.getName(), animal);
		return createResponse(new AnimalResourceAssembler().toResource(animal), HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{name}", method = RequestMethod.GET)
	@ResponseBody
	public HttpEntity<Animal3> getAnimal(@PathVariable String name)
	{
		return createOKResponse(new AnimalResourceAssembler().toResource(findAnimal(name)));
	}

	private Page<Animal3> makePage(List<Animal3> animals, Pageable pageable)
	{
		return new PageImpl<Animal3>(animals, pageable, animals.size());
	}

	private Animal3 findAnimal(String name)
	{
		return animals.get(name);
	}

	private HttpEntity<PagedResources<Animal3>> createOKResponse(PagedResources<Animal3> animals)
	{
		return new ResponseEntity<PagedResources<Animal3>>(animals, HttpStatus.OK);
	}

	private HttpEntity<Animal3> createOKResponse(Animal3 animalResource)
	{
		return createResponse(animalResource, HttpStatus.OK);
	}

	private HttpEntity<Animal3> createResponse(Animal3 animalResource, HttpStatus status)
	{
		return new ResponseEntity<Animal3>(animalResource, status);
	}

	static ControllerLinkBuilder getAnimalLinkBuilder(String name)
	{
		AnimalController3 methodOn = methodOn(AnimalController3.class);
		return linkTo(methodOn.getAnimal(name));
	}

	private static class AnimalResourceAssembler extends ResourceAssemblerSupport<Animal3, Animal3>
	{
		public AnimalResourceAssembler()
		{
			super(AnimalController3.class, Animal3.class);
		}

		@Override
		public Animal3 toResource(Animal3 animal)
		{
			if (!animal.hasLinks())
			{
				ControllerLinkBuilder selfLinkBuilder = getAnimalLinkBuilder(animal.getName());
				addSelfLink(selfLinkBuilder, animal);
			}
			return animal;
		}

		@Override
		protected Animal3 instantiateResource(Animal3 animal)
		{
			return animal;
		}

		private void addSelfLink(ControllerLinkBuilder selfLinkBuilder, Animal3 animalResource)
		{
			animalResource.add(selfLinkBuilder.withSelfRel());
		}
	}
}