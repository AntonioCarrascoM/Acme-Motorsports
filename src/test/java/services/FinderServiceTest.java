
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Finder;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class FinderServiceTest extends AbstractTest {

	// System under test: Finder ------------------------------------------------------

	// Tests ------------------------------------------------------------------
	// PLEASE READ
	// The Sentence coverage has been obtained with the tool EclEmma from Eclipse. 
	// Since having one @Test for every case is not optimal we divided the user cases in two cases. Positives and Negatives.

	@Autowired
	private FinderService	finderService;


	@Test
	public void FinderPositiveTest() {
		final Object testingData[][] = {
			//Total sentence coverage : Coverage 89,3% | Covered Instructions 50 | Missed Instructions 6 | Total Instructions 56
			{
				"rider1", "finder1", null

			},
		/*
		 * Positive test: A rider edits his finder.
		 * Requisite tested: Functional requirement - 29.5. An actor who is authenticated as a rider must be able to:
		 * Change the filters of his or her finder
		 * Data coverage : From 5 editable attributes we tried to edit 1 attribute (keyword) with valid authority.
		 * Exception expected: None. A rider can edit his finders.
		 */

		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	@Test
	public void FinderNegativeTest() {
		final Object testingData[][] = {
			//Total sentence coverage : Coverage 90,0% | Covered Instructions 54 | Missed Instructions 6 | Total Instructions 60
			{
				"raceDirector1", "finder1", IllegalArgumentException.class
			}
		/*
		 * Negative test: A raceDirector edit finder.
		 * Requisite tested: Functional requirement - 29.5. An actor who is authenticated as a rider must be able to:
		 * Change the filters of his or her finder
		 * Data coverage : From 5 editable attributes we tried to edit 1 attribute (keyword) with invalid authority.
		 * Exception expected: IllegalArgumentException. A raceDirector can not edit a finder.
		 */

		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}
	protected void template(final String username, final String id, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			if (username == "rider1") {
				super.authenticate(username);
				final Finder finder = this.finderService.findOne(this.getEntityId(id));
				finder.setKeyWord("Tester");
				this.finderService.save(finder);
			} else if (username == "raceDirector1") {
				super.authenticate(username);
				final Finder finder = this.finderService.findOne(this.getEntityId(id));
				finder.setKeyWord("Tester");
				this.finderService.save(finder);
			}
			this.finderService.flush();
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}
