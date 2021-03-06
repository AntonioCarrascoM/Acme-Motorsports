
package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Forecast;
import domain.GrandPrix;
import domain.RaceDirector;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ForecastServiceTest extends AbstractTest {

	// System under test: Forecast ------------------------------------------------------

	// Tests ------------------------------------------------------------------
	// PLEASE READ
	// The Sentence coverage has been obtained with the tool EclEmma from Eclipse. 
	// Since having one @Test for every case is not optimal we divided the user cases in two cases. Positives and Negatives.

	@Autowired
	private ForecastService		forecastService;

	@Autowired
	private RaceDirectorService	raceDirectorService;

	@Autowired
	private GrandPrixService	grandPrixService;


	@Test
	public void ForecastPositiveTest() {
		final Object testingData[][] = {
			//Total sentence coverage : Coverage 93.5% | Covered Instructions 87 | Missed Instructions 6 | Total Instructions 93
			{
				"raceDirector1", "forecast1", "grandPrix1", "create", null
			},
			/*
			 * Positive test: A race director creates an forecast.
			 * Requisite tested: Functional requirement - 26.6. An actor who is authenticated as a raceDirector must be able to
			 * Publish a forecast of his or her grand prixes, which includes showing, creating, updating them.
			 * Data coverage : We created an forecast with 7 out of 7 valid parameters.
			 * Exception expected: None. A RaceDirector can create forecasts.
			 */

			{
				"raceDirector1", null, "forecast1", "edit", null
			},
		/*
		 * Positive test: A race director edits his forecast.
		 * Requisite tested: Functional requirement - 26.6. An actor who is authenticated as a raceDirector must be able to
		 * Publish a forecast of his or her grand prixes, which includes showing, creating, updating them.
		 * Data coverage : From 6 editable attributes we tried to edit 1 attribute (AmbientTemperature) with valid data.
		 * Exception expected: None. A RaceDirector can edit his forecasts.
		 */

		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	@Test
	public void ForecastNegativeTest() {
		final Object testingData[][] = {
			//Total sentence coverage : Coverage 95.9% | Covered Instructions 141 | Missed Instructions 6 | Total Instructions 147
			{
				"raceDirector3", "forecast1", "grandPrix1", "createNegative", IllegalArgumentException.class
			},
			/*
			 * Negative test: A race director tries to create an forecast for another race director's grand prix.
			 * Requisite tested: Functional requirement - 26.6. An actor who is authenticated as a raceDirector must be able to
			 * Publish a forecast of his or her grand prixes, which includes showing, creating, updating them.
			 * Data coverage : We tried to create an forecast with 4 out of 5 valid parameters.
			 * Exception expected:IllegalArgumentException.class. A race director can not create an forecast for another race director's grand prix.
			 */

			{
				"raceDirector1", "forecast1", "grandPrix1", "createNegative2", ConstraintViolationException.class
			},
			/*
			 * Negative test: A race director tries to create an forecast with a negative RainMm.
			 * Requisite tested: Functional requirement - 26.6. An actor who is authenticated as a raceDirector must be able to
			 * Publish a forecast of his or her grand prixes, which includes showing, creating, updating them.
			 * Data coverage : We tried to create an forecast with 4 out of 5 valid parameters.
			 * Exception expected:ConstraintViolationException.RainMm must be positive.
			 */

			{
				"raceDirector1", null, "forecast1", "editNegative", ConstraintViolationException.class
			},
			/*
			 * Negative test: A race director tries to edit an forecast with a negative RainMm.
			 * Requisite tested: Functional requirement - 26.6. An actor who is authenticated as a raceDirector must be able to
			 * Publish a forecast of his or her grand prixes, which includes showing, creating, updating them.
			 * Data coverage : From 6 editable attributes we tried to edit 1 attribute (RainMm) with invalid data.
			 * Exception expected: ConstraintViolationException.RainMm must be positive.
			 */
			{
				"raceDirector3", null, "forecast1", "edit", IllegalArgumentException.class
			},
		/*
		 * Negative test: A race director tries to edit a forecast that not owns.
		 * Requisite tested: Functional requirement - 26.6. An actor who is authenticated as a raceDirector must be able to
		 * Publish a forecast of his or her grand prixes, which includes showing, creating, updating them.
		 * Data coverage : From 6 editable attributes we tried to edit 1 attribute (RainMm) with invalid data.
		 * Exception expected: CIllegalArgumentException.class. A race director can not edit another race director's forecasts.
		 */

		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}
	protected void template(final String username, final String st, final String id, final String operation, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			if (operation.equals("create")) {
				final Forecast forecastOld = this.forecastService.findOne(this.getEntityId(st));
				this.forecastService.delete(forecastOld);
				final Forecast forecast = this.forecastService.create();

				forecast.setAsphaltTemperature(50);
				forecast.setAmbientTemperature(35);
				forecast.setWindDirection("North");
				forecast.setRainMm(15);
				forecast.setCloudPercentage(15);
				forecast.setWindSpeed(10);
				//				final RaceDirector raceDirector = this.raceDirectorService.findOne(this.getEntityId(username));
				//				forecast.setRaceDirector(raceDirector);
				final GrandPrix grandPrix = this.grandPrixService.findOne(this.getEntityId(id));
				forecast.setGrandPrix(grandPrix);

				this.forecastService.save(forecast);

			} else if (operation.equals("edit")) {
				final Forecast forecast = this.forecastService.findOne(this.getEntityId(id));
				forecast.setAmbientTemperature(15);
				this.forecastService.save(forecast);

			} else if (operation.equals("createNegative")) {
				final Forecast forecastOld = this.forecastService.findOne(this.getEntityId(st));
				this.forecastService.delete(forecastOld);
				final Forecast forecast = this.forecastService.create();

				forecast.setAsphaltTemperature(50);
				forecast.setAmbientTemperature(35);
				forecast.setWindDirection("North");
				forecast.setRainMm(15);
				forecast.setCloudPercentage(15);
				forecast.setWindSpeed(10);
				final RaceDirector raceDirector = this.raceDirectorService.findOne(this.getEntityId(username));
				forecast.setRaceDirector(raceDirector);
				final GrandPrix grandPrix = this.grandPrixService.findOne(this.getEntityId(id));
				forecast.setGrandPrix(grandPrix);

				this.forecastService.save(forecast);

			} else if (operation.equals("createNegative2")) {
				final Forecast forecastOld = this.forecastService.findOne(this.getEntityId(st));
				this.forecastService.delete(forecastOld);
				final Forecast forecast = this.forecastService.create();

				forecast.setAsphaltTemperature(50);
				forecast.setAmbientTemperature(35);
				forecast.setWindDirection("North");
				forecast.setRainMm(-15);
				forecast.setCloudPercentage(15);
				forecast.setWindSpeed(10);
				final RaceDirector raceDirector = this.raceDirectorService.findOne(this.getEntityId(username));
				forecast.setRaceDirector(raceDirector);
				final GrandPrix grandPrix = this.grandPrixService.findOne(this.getEntityId(id));
				forecast.setGrandPrix(grandPrix);

				this.forecastService.save(forecast);

			} else if (operation.equals("editNegative")) {
				final Forecast forecast = this.forecastService.findOne(this.getEntityId(id));
				forecast.setRainMm(-15);
				this.forecastService.save(forecast);

			}

			this.forecastService.flush();
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}
