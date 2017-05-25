package vn.giki.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.giki.rest.GikiAPIInitializer;
import vn.giki.rest.dao.PackageRepository;
import vn.giki.rest.entity.Package;

@RestController
@RequestMapping(value = "/packages")
public class PackageController {
	private PackageRepository repo;

	@Autowired
	public void setPackageRepository(PackageRepository packageRepository) {
		this.repo = packageRepository;
	}

	@ModelAttribute(name = "pageRequest")
	public PageRequest getPageRequest(Model model,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
			@RequestParam(value = "sort", required = false) String sortString) {
		Sort sort = null;
		if (sortString != null && sortString.matches(GikiAPIInitializer.SORT_REGEX)) {
			int semiCommaIndex = sortString.indexOf(";");
			Direction direction = Direction.ASC;
			if (semiCommaIndex > 0) {
				direction = Direction.valueOf(sortString.substring(semiCommaIndex + 1).toUpperCase());
			}
			String properties = sortString.substring(0, semiCommaIndex);
			sort = new Sort(direction, properties.split(","));
		}
		return new PageRequest(page, size, sort);
	}

	@GetMapping
	public Page<Package> getPackages(@ModelAttribute(name = "pageRequest") PageRequest pageable) {
		Page<Package> p = repo.findAll(pageable);
		if (p.getTotalElements() < pageable.getPageSize()) {
			pageable = new PageRequest(pageable.getPageNumber(), (int) p.getTotalElements(), pageable.getSort());
			return repo.findAll(pageable);
		}
		return p;
	}

	@PostMapping
	public Package addPackage(@RequestBody Package p) {
		if (repo.exists(p.getId())) {
			return null;
		}
		return repo.save(p);
	}

	@GetMapping("/{id}")
	public Package getPackage(@PathVariable String id) {
		return repo.findOne(id);
	}

	@PutMapping("/{id}")
	public Package updatePackage(@RequestBody Package p) {
		if (!repo.exists(p.getId())) {
			return null;
		}
		return repo.save(p);
	}

	@DeleteMapping("/{id}")
	public Package deletePackage(@PathVariable String id) {
		if (repo.exists(id)) {
			repo.delete(id);
			return repo.findOne(id);
		}
		return null;
	}
}
