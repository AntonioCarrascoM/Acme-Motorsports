
package controllers.actor;

import java.util.Collection;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.SocialProfileService;
import controllers.AbstractController;
import domain.SocialProfile;

@Controller
@RequestMapping("/socialProfile")
public class SocialProfileController extends AbstractController {

	//Services

	@Autowired
	private SocialProfileService	socialProfileService;

	@Autowired
	private ActorService			actorService;


	//Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int varId) {
		ModelAndView result;
		SocialProfile socialProfile;

		socialProfile = this.socialProfileService.findOne(varId);

		if (socialProfile == null)
			return new ModelAndView("redirect:/welcome/index.do");

		result = new ModelAndView("socialProfile/display");
		result.addObject("socialProfile", socialProfile);
		result.addObject("requestURI", "socialProfile/display.do");

		return result;
	}

	//Listing

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		Collection<SocialProfile> socialProfiles;

		socialProfiles = this.socialProfileService.socialProfilesFromActor(this.actorService.findByPrincipal().getId());

		result = new ModelAndView("socialProfile/list");
		result.addObject("principalId", this.actorService.findByPrincipal().getId());
		result.addObject("socialProfiles", socialProfiles);
		result.addObject("requestURI", "socialProfile/list.do");

		return result;
	}

	//Listing by rider

	@RequestMapping(value = "/listByRider", method = RequestMethod.GET)
	public ModelAndView listByRider(@RequestParam final int varId) {
		final ModelAndView result;
		Collection<SocialProfile> socialProfiles;

		socialProfiles = this.socialProfileService.socialProfilesFromActor(varId);

		result = new ModelAndView("socialProfile/list");
		result.addObject("principalId", this.actorService.findByPrincipal().getId());
		result.addObject("socialProfiles", socialProfiles);
		result.addObject("requestURI", "socialProfile/listByRider.do");

		return result;
	}

	//Creation

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		SocialProfile socialProfile;

		socialProfile = this.socialProfileService.create();
		result = this.createEditModelAndView(socialProfile);

		return result;
	}

	//Edition

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int varId) {
		final ModelAndView result;
		SocialProfile socialProfile;

		socialProfile = this.socialProfileService.findOne(varId);
		Assert.notNull(socialProfile);

		if (socialProfile.getActor().getId() != this.actorService.findByPrincipal().getId())
			return new ModelAndView("redirect:/welcome/index.do");

		result = this.createEditModelAndView(socialProfile);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(SocialProfile socialProfile, final BindingResult binding) {
		ModelAndView result;

		try {
			socialProfile = this.socialProfileService.reconstruct(socialProfile, binding);
		} catch (final ValidationException oops) {
			return this.createEditModelAndView(socialProfile);
		} catch (final Throwable oops) {
			return result = this.createEditModelAndView(socialProfile, "socialProfile.commit.error");
		}

		try {
			this.socialProfileService.save(socialProfile);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(socialProfile, "socialProfile.commit.error");
		}
		return result;
	}

	//Delete

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int varId) {
		ModelAndView result;
		Collection<SocialProfile> socialProfiles;
		SocialProfile socialProfile;

		result = new ModelAndView("socialProfile/list");
		socialProfiles = this.socialProfileService.socialProfilesFromActor(this.actorService.findByPrincipal().getId());

		socialProfile = this.socialProfileService.findOne(varId);

		if (socialProfile.getActor().getId() != this.actorService.findByPrincipal().getId())
			return new ModelAndView("redirect:/welcome/index.do");

		try {
			this.socialProfileService.delete(socialProfile);
			socialProfiles = this.socialProfileService.socialProfilesFromActor(this.actorService.findByPrincipal().getId());

			result.addObject("socialProfiles", socialProfiles);
			result.addObject("requestURI", "socialProfile/list.do");
		} catch (final Throwable oops) {
			socialProfiles = this.socialProfileService.socialProfilesFromActor(this.actorService.findByPrincipal().getId());

			result = new ModelAndView("socialProfile/list");
			result.addObject("socialProfiles", socialProfiles);
			result.addObject("message", "socialProfile.delete.error");
			result.addObject("requestURI", "socialProfile/list.do");
		}

		return result;
	}

	//Ancillary methods

	protected ModelAndView createEditModelAndView(final SocialProfile socialProfile) {
		ModelAndView result;

		result = this.createEditModelAndView(socialProfile, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final SocialProfile socialProfile, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("socialProfile/edit");
		result.addObject("socialProfile", socialProfile);
		result.addObject("message", messageCode);
		result.addObject("requestURI", "socialProfile/edit.do");

		return result;

	}
}
