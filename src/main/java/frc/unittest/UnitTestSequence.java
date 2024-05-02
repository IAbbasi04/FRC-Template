package frc.unittest;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.autonomous.commands.NewtonCommand;

/**
 * Treated like a command since it gets passed into the WPILib Command Scheduler.
 * Makes management for the test sequence much simpler since it is mainly
 * handled by WPILib. Also makes it possible to just set once and cancel it
 */
public class UnitTestSequence extends NewtonCommand {
    private Timer sequenceTimer = new Timer();
    private UnitTest currentTest;
    
    private List<Boolean> testResults = new ArrayList<>(); // Whether each test passed or failed
    private List<UnitTest> ranTests = new ArrayList<>(); // Tests that have already finished

    private int currentIndex = 0;

    /**
     * Pass all tests you want to run (in Robot.testPeriodic()) into here.
     * 
     * Treated like a queue of sorts since the tests run in order and not
     * simultaneously.
     */
    public List<UnitTest> testsToRun = List.of(
       new SwerveUnitTest(),
       new IntakeUnitTest()
    );

    @Override
    public void initialize() {
        sequenceTimer.reset();
        sequenceTimer.start();
        currentTest = testsToRun.get(0);
        currentTest.initialize();
        currentTest.start();
    }

    @Override
    public void execute() {
        if (!currentTest.equals(null)) { // If there are tests to run
            currentTest.run(); // Runs through the current test plan
            if (currentTest.hasFinished()) { // Test has completed
                if (currentTest.hasFailed()) { // Test failed the requirements
                    testResults.add(false);
                } else if (currentTest.hasSucceeded()) { // Test passed the requirements
                    testResults.add(true);
                }
                
                ranTests.add(currentTest);

                currentIndex++;

                if (currentIndex == testsToRun.size()) {
                    currentTest = null;
                } else {
                    currentTest = testsToRun.get(currentIndex);
                    currentTest.initialize();
                    currentTest.start();
                }

                
            }
        }
    }

    @Override
    public boolean isFinished() {
        return currentTest == null;
    }

    @Override
    public void end(boolean interrupted) {
        if (interrupted) return; // Do nothing if test plan was interrupted

        if (ranTests.size() != testResults.size()) {
            System.out.println("\n\n===========================================================");
            System.out.println("Error: Number of Ran Tests Does Not Match Number of Results");
            System.out.println("===========================================================\n\n");
            return;
        }

        for (int i = 0; i < ranTests.size(); i++) {
            System.out.println("\n\n===========================================================");
            String displayedResult = ranTests.get(i).getClass().getSimpleName();
            if (ranTests.get(i).hasFailed()) {
                displayedResult += " ... \u001B[31mFAILED\u001B[0m"; // Displayed as green
            } else if (ranTests.get(i).hasSucceeded()) {
                displayedResult += " ... \u001B[32mPASSED\u001B[0m"; // Displayed as red
            } else {
                displayedResult += " ... \u001B[33mRETURNED NO RESULT\u001B[0m"; // Displayed as yellow
            }
            System.out.println(displayedResult);
            System.out.println("===========================================================\n\n");
        }
    }
}