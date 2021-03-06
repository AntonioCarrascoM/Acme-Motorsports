
package controllers.rider;

import java.util.Collection;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ApplicationService;
import services.GrandPrixService;
import controllers.AbstractController;
import domain.Application;
import domain.GrandPrix;

@Controller
@RequestMapping("application/rider")
public class ApplicationRiderController extends AbstractController {

	//Services

	@Autowired
	private ApplicationService	applicationService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private GrandPrixService	grandPrixService;


	//Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int varId) {
		ModelAndView result;
		Application application;

		application = this.applicationService.findOne(varId);

		if (application == null || application.getRider() != this.actorService.findByPrincipal())
			return new ModelAndView("redirect:/welcome/index.do");

		result = new ModelAndView("application/display");
		result.addObject("application", application);
		result.addObject("actor", "rider");
		result.addObject("requestURI", "application/rider/display.do");

		return result;
	}

	//List
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Application> applications;

		applications = this.applicationService.getAllApplicationsForRider(this.actorService.findByPrincipal().getId());

		result = new ModelAndView("application/list");
		result.addObject("principalId", this.actorService.findByPrincipal().getId());
		result.addObject("applications", applications);
		result.addObject("actor", "rider");
		result.addObject("requestURI", "application/rider/list.do");

		return result;
	}

	//Creation

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		final Application application;

		application = this.applicationService.create();
		result = this.createEditModelAndView(application);

		return result;
	}

	//Save

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Application application, final BindingResult binding) {
		ModelAndView result;

		try {
			application = this.applicationService.reconstruct(application, binding);
		} catch (final ValidationException oops) {
			return this.createEditModelAndView(application);
		} catch (final Throwable oops) {
			return result = this.createEditModelAndView(application, "application.commit.error");
		}

		try {
			this.applicationService.save(application);
			result = new ModelAndView("redirect:/application/rider/list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(application, "application.commit.error");
		}
		return result;
	}

	//Ancillary methods

	protected ModelAndView createEditModelAndView(final Application application) {
		ModelAndView result;

		result = this.createEditModelAndView(application, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Application application, final String messageCode) {
		ModelAndView result;

		final Collection<GrandPrix> grandPrixes = this.grandPrixService.getApplicableGrandPrixesForRider(this.actorService.findByPrincipal().getId());

		result = new ModelAndView("application/edit");
		result.addObject("grandPrixes", grandPrixes);
		result.addObject("actor", "rider");
		result.addObject("application", application);
		result.addObject("message", messageCode);
		result.addObject("requestURI", "application/rider/edit.do");

		return result;

	}
}
