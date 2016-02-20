package nu.famroos.spring.hateoas.polymorphism.repro1;

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
import org.springframework.hateoas.Resource;
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
@RequestMapping(value = "/animals1")
public class AnimalController1
{
	private SortedMap<String, Animal1> animals = new TreeMap<>();

	public AnimalController1()
	{
		instantiateDog("nameofdog", 1.0);
		instantiateCat("nameofcat", 3);
		instantiateDog("nameofdog2", 10.2);
	}

	private Dog1 instantiateDog(String name, double barkVolume)
	{
		Dog1 dog = new Dog1(name, barkVolume);
		registerAnimal(dog);
		return dog;
	}

	private Animal1 instantiateCat(String name, int lives)
	{
		Cat1 cat = new Cat1(name, lives);
		registerAnimal(cat);
		return cat;
	}

	private void registerAnimal(Animal1 animal)
	{
		animals.put(animal.getName(), animal);
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseBody
	public HttpEntity<PagedResources<AnimalResource>> getAnimals(Pageable pageable,
			PagedResourcesAssembler<Animal1> pagedResourcesAssembler)
	{

		return createOKResponse(pagedResourcesAssembler.toResource(makePage(new ArrayList<>(animals.values()), pageable),
				new AnimalResourceAssembler()));
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ResponseBody
	public HttpEntity<AnimalResource> addAnimal(@RequestBody Animal1 animal)
	{
		animals.put(animal.getName(), animal);
		return createResponse(new AnimalResourceAssembler().toResource(animal), HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{name}", method = RequestMethod.GET)
	@ResponseBody
	public HttpEntity<AnimalResource> getAnimal(@PathVariable String name)
	{
		return createOKResponse(new AnimalResourceAssembler().toResource(findAnimal(name)));
	}

	private Page<Animal1> makePage(List<Animal1> animals, Pageable pageable)
	{
		return new PageImpl<Animal1>(animals, pageable, animals.size());
	}

	private Animal1 findAnimal(String name)
	{
		return animals.get(name);
	}

	private HttpEntity<PagedResources<AnimalResource>> createOKResponse(PagedResources<AnimalResource> animals)
	{
		return new ResponseEntity<PagedResources<AnimalResource>>(animals, HttpStatus.OK);
	}

	private HttpEntity<AnimalResource> createOKResponse(AnimalResource animalResource)
	{
		return createResponse(animalResource, HttpStatus.OK);
	}

	private HttpEntity<AnimalResource> createResponse(AnimalResource animalResource, HttpStatus status)
	{
		return new ResponseEntity<AnimalResource>(animalResource, status);
	}

	static ControllerLinkBuilder getAnimalLinkBuilder(String name)
	{
		AnimalController1 methodOn = methodOn(AnimalController1.class);
		return linkTo(methodOn.getAnimal(name));
	}

	static class AnimalResource extends Resource<Animal1>
	{
		public AnimalResource(Animal1 animal)
		{
			super(animal);
		}
	}

	private static class AnimalResourceAssembler extends ResourceAssemblerSupport<Animal1, AnimalResource>
	{
		public AnimalResourceAssembler()
		{
			super(AnimalController1.class, AnimalResource.class);
		}

		@Override
		public AnimalResource toResource(Animal1 animal)
		{
			AnimalResource animalResource = instantiateResource(animal);
			ControllerLinkBuilder selfLinkBuilder = getAnimalLinkBuilder(animal.getName());
			addSelfLink(selfLinkBuilder, animalResource);
			return animalResource;
		}

		@Override
		protected AnimalResource instantiateResource(Animal1 animal)
		{
			return new AnimalResource(animal);
		}

		private void addSelfLink(ControllerLinkBuilder selfLinkBuilder, Resource<Animal1> animalResource)
		{
			animalResource.add(selfLinkBuilder.withSelfRel());
		}
	}
}